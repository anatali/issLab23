println(" ...... build in taskEvents sub-project  ")

version="not-yet-defined"

tasks.register("distribution") {
    doLast {
        println("We build the zip with version=$version")
    }
}

tasks.register("release") {
    dependsOn("distribution")
    doLast {
        println("We release now")
    }
}
/*
the method  whenReady registers a closure that’s executed immediately after the
task graph has been populated.
 */
gradle.taskGraph.whenReady {
    println("whenReady version=$version ${hasTask(":taskEvents:release")}" )
    version =
            if (hasTask(":taskEvents:release")) "1.0"
            else "1.0-SNAPSHOT"
}

//gradle -q :taskEvents:distribution
//gradle -q :taskEvents:release