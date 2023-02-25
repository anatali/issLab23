import org.gradle.internal.jvm.Jvm
println(" ...... build in taskCustom sub-project  ")

tasks.register<Copy>("mycopy") {    //Registers a new task of type Copy and configures it
    println("projectDir= $projectDir") //GradleIntro\taskCustom
    println("buildDir  = $buildDir")   //GradleIntro\taskCustom\build
    from("$projectDir/../app/src"){
        exclude( "**/main/resources", "**/test" )
    }
    into( "../copiedFiles" )
}

task<Exec>("mycmd") {
    workingDir("$projectDir")
    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        commandLine(
                "cmd", "/c",    //Esegue il comando specificato dalla stringa e quindi si arresta.
                "echo", "Hello from Windows"
                //"cmd1.bat"
        )
    } else {
        commandLine(
                "sh", "-c",
                "echo", "Hello from NO-Windows" )
    }
}


tasks.register<Exec>("printJavaVersion") { //Inline function with reified type!
// Configuration action is of type T.() -> Unit, in this case Exec.T() -> Unit
    val javaExecutable = Jvm.current().javaExecutable.absolutePath
    //this is a method of class org.gradle.api.Exec
     commandLine( javaExecutable, "-version" )
// There is no need of doLast / doFirst, actions are already configured
// Still, we may want to do something before or after the task has been executed
    doLast {  println("-------- printJavaVersion END") }
    doFirst { println("-------- printJavaVersion STARTS" ) }
}

