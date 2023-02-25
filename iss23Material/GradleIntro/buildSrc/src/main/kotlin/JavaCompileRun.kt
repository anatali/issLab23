import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.*
import org.gradle.internal.jvm.Jvm
import org.gradle.kotlin.dsl.withType
import java.io.File
import javax.inject.Inject

// Inject annotation is required!
open class Clean @Inject constructor() : DefaultTask() {
    @TaskAction
    fun clean() {
        if (!project.buildDir.deleteRecursively()) {
            throw IllegalStateException("Cannot delete ${project.buildDir}")
        }
    }
}
data class FinderInFolder(val project: Project, val directory: String) {
    fun withExtension(extension: String): Array<String> = project.projectDir
            .listFiles { it: File -> it.isDirectory && it.name == directory }
            ?.firstOrNull()
            ?.walk()
            ?.filter { it.extension == extension }
            ?.map { it.absolutePath }
            ?.toList()
            ?.toTypedArray()
            ?: emptyArray()
}
fun Project.findFilesIn(directory: String) = FinderInFolder(this, directory)
fun Project.findSources()   = this.findFilesIn("src").withExtension("java")
fun Project.findLibraries() = this.findFilesIn("lib").withExtension("jar")

abstract class JavaTask(javaExecutable: File = Jvm.current().javaExecutable) : Exec() {

    companion object {
        val separator = if (Os.isFamily(Os.FAMILY_WINDOWS)) ";" else ":"
    }

    init { executable = javaExecutable.absolutePath }

    @Input
    var classPath: Set<File> = FinderInFolder(project, "lib")
            .withExtension("jar")
            .map { project.file(it) }
            .toSet()

    private val classPathDescriptor get() = classPath.joinToString(separator = separator)

    fun fromConfiguration(configuration: Configuration) {
        classPath = configuration.resolve()
        println("JavaTask | $this - fromConfiguration classPath=${classPath}")
        update()  //could compile/run Main
    }

    fun javaCommandLine(msg: String, vararg arguments: String) {
        //println("JavaTask | javaCommandLine num of arguments=${arguments.size} ${arguments[0]}")
        showArguments(msg, arguments)  //ADDED to better understand the behavior
        commandLine(
                executable,
                *(if (classPath.isEmpty()) emptyArray() else arrayOf("-cp", classPathDescriptor)),
                *arguments
        )
    }

    fun showArguments(msg: String, arguments: Array<out String>){
        print("JavaTask | javaCommandLine $msg - num of arguments= ${arguments.size} the first is: ${arguments[0]}")
        //arguments.forEach { a -> print( "${a}   " ) }
        println("")
    }

    abstract fun update(): Unit


}

open class CompileJava @javax.inject.Inject constructor() : JavaTask(Jvm.current().javacExecutable) {
    @OutputDirectory
    var outputFolder: String = "${project.buildDir}/bin/"
        set(value) {
            field = value
            update()
        }
    // The shorter version does not work due to a Gradle/Kotlin bug
    @InputFiles
    var sources: Set<String> = FinderInFolder(project, "src").withExtension("java").toSet()
        set(value) {
            field = value
            update()
        }
    init { update() }

    final override fun update() {
        //println("CompileJava | update num of sources=${sources.size}  ")
        javaCommandLine("CompileJava update","-d", outputFolder, *sources.toTypedArray())
    }
}

open class RunJava @javax.inject.Inject constructor() : JavaTask() {
    @Input
    var mainClass: String = "Main"
        set(value) {
            println("RunJava  | going to run ... ${value}  ")
            field = value
            update()
        }

    init {
        dependsOn(project.tasks.withType<CompileJava>())
    }

    final override fun update() {
        //println("RunJava | update classPath=${classPath} mainClass=${mainClass}  executable=$executable") //{classPath.joinToString(separator = ";")}
        javaCommandLine("RunJava update", mainClass)
    }
}
