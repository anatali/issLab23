import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.internal.jvm.Jvm

println(" ...... build in taskJavaCompile  ")


tasks.register("hello") {
    doLast {
        println("Hello world from taskJavaCompile - task=$this")
    }
}

fun findJavaSources(): Array<String> = projectDir // From the project
        .listFiles { it: File -> it.isDirectory && it.name == "src" } // Find a folder named 'src'
        ?.firstOrNull() // If it's not there we're done
        ?.walk() // If it's there, iterate all its content (returns a Sequence<File>)
        ?.filter { it.extension == "java" } // Pick all Java files
        ?.map { it.absolutePath } // Map them to their absolute path
        ?.toList() // Sequences can't get converted to arrays, we must go through lists
        ?.toTypedArray() // Convert to Array<String>
        ?: emptyArray() // Yeah if anything's missing there are no sources

// Create a new configuration, our compileClasspath
val compileClasspath by configurations.creating

// Write a task of type Exec that launches javac
tasks.register<Exec>("mycompileJava") {
    // Resolve the classpath configuration
    // (in general, files could be remote and need fetching)
    val sep  = if (Os.isFamily(Os.FAMILY_WINDOWS)) ";" else ":"
    val classpathFiles = compileClasspath.resolve() //could be empty NO joinToString
    val myclasspath    = if( classpathFiles.size==0 ) classpathFiles;
                         else classpathFiles.joinToString(separator = sep )
    // Build the command
    val sources = findJavaSources()
    println("sources.size=" + sources.size)
    if (sources.isNotEmpty())  {
        //println(" ----------------------- ${sources.get(0)} classpathFiles=$classpathFiles  " )
        val javacExecutable = Jvm.current().javacExecutable.absolutePath
        commandLine(
            "$javacExecutable",
            "-cp", myclasspath,
            "-d", "$projectDir/output",
            *sources
        )
    }
}

