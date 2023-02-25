import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.internal.jvm.Jvm

println(" ...... build in taskJavaRun  ")


data class FinderInFolder(val directory: String) {
    fun withExtension(extension: String): Array<String> = projectDir
            .listFiles { it: File -> it.isDirectory && it.name == directory }
            ?.firstOrNull()
            ?.walk()
            ?.filter { it.extension == extension }
            ?.map { it.absolutePath }
            ?.toList()
            ?.toTypedArray()
            ?: emptyArray()
}
fun findFilesIn(directory: String) = FinderInFolder(directory)
fun findSources() = findFilesIn("src").withExtension("java")
fun findLibraries() = findFilesIn("lib").withExtension("jar")
fun DependencyHandlerScope.forEachLibrary(todo: DependencyHandlerScope.(String) -> Unit) {
    findLibraries().forEach {
        println("library $it")
        todo(it)
    }
}
val sep = if (Os.isFamily(Os.FAMILY_WINDOWS)) ";" else ":"

val compileClasspath by configurations.creating
val runtimeClasspath by configurations.creating {
    extendsFrom(compileClasspath) // Built-in machinery to say that one configuration is like another "plus stuff"
}

dependencies {
    forEachLibrary {
        compileClasspath(files(it))
    }
    runtimeClasspath(files("$projectDir/bin"))
}

val compileJava = tasks.register<Exec>("compileJava") {
    val classpathFiles = compileClasspath.resolve()
    val myclasspath    = if( classpathFiles.size==0 ) classpathFiles;
                         else classpathFiles.joinToString(separator = sep )
    // Build the command
    val sources = findSources()
    if (sources != null)  {
        // Use the current JVM's javac
        val javacExecutable = Jvm.current().javacExecutable.absolutePath

        commandLine(
                "$javacExecutable",
                "-cp", myclasspath, //classpathFiles.joinToString(separator = sep),
                "-d", "$projectDir/bin", //The compiled files go here
                *sources
        )

    }
}

fun showJavaJvm(){
    val jvm = Jvm.current()
    val jreHome = jvm .getJre()?.getHomeDir() ?: null  //safe op
    println("Java version: ${jvm.getJavaVersion()} jre HomeDir: ${jreHome}")
}

tasks.register<Exec>("runJava") {
    val classpathFiles = runtimeClasspath.resolve()
    val myclasspath    = if( classpathFiles.size==0 ) classpathFiles;
                         else classpathFiles.joinToString(separator = sep )
    println("myclasspath: ${myclasspath}")
    val mainClass = "Program1" // Horribly hardcoded, we must do something
    val javaExecutable = Jvm.current().javaExecutable.absolutePath
    showJavaJvm() //just to test ...
    commandLine(
            "$javaExecutable",
            "-cp", myclasspath,
            mainClass
    )

    dependsOn(compileJava)
}

tasks.register<RunJava >("dorunJava") {
    mainClass = "$projectDir/src/Program1"
}