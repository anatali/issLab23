import org.gradle.internal.impldep.org.mozilla.javascript.tools.shell.Global.readline

println(" ...... build in taskForBuildSrc sub-project  ")

/*
In this subproject, we compile and run Java programs
without using the application (java) plugin.
The imperative stuff of the job is defined under buildSrc
 */
val compileClasspath by configurations.creating
val runtimeClasspath by configurations.creating {
    extendsFrom(compileClasspath)
}
dependencies {
    //println("taskForBuildSrc | dependencies BUIILDIR= $buildDir")
    findLibraries().forEach {
        //println("taskForBuildSrc | FOUND lib: $it")
        compileClasspath(files(it))
    }
    runtimeClasspath(files("$buildDir/bin"))
}
tasks.register<CompileJava>("compileJava")

tasks.register<RunJava>("runJava") {
    //fromConfiguration(configurations.runtimeClasspath.get())
    fromConfiguration( runtimeClasspath )
    val progName = readLine()
    mainClass = progName!!
}

/*
ADDED AFTER buildSrc
*/
//val compileClasspath by configurations.creating

task<CommonTask>("ct"){
    //println("compileClasspath= $compileClasspath " )
    msg ="Hello from ${this.name} in ${project.name}"
}