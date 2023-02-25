println(" ...... build in taskAccess sub-project  ")

tasks.named("explain") {
    doFirst{	//additional configuration to the task explain
        println( "	- I'm the taskAccess sub-project: START - ")
    }
    doLast{	//additional configuration to the task explain
        println( "	- I'm the taskAccess sub-project: END - ")
    }
}

tasks.register("hello") {
    doLast {
        println("Hello world from taskAccess -- this=${this}  ")
        tasks.forEach { println("${it.name}") }
    }
}

//Adding a dependency on a task
tasks.named("commontask0") { 	//Accessing a task via API -
    dependsOn("commontask3", "commontask2")
}

//Control order execution
tasks.named("commontask2") { mustRunAfter(tasks.named("commontask3")) }

val t1 = tasks.named("commontask1")

//Modify an existing behaviour
t1{	//Accessing a task via API - adding behaviour
    doFirst {
        println("Configured later, but executed as first in task named ${t1.name}")
    }
}
t1{ //Accessing a task via API - adding behaviour
    doLast {
        println("Another last of task named ${t1.name}")
    }
}

/*
ADDED AFTER buildSrc

val compileClasspath by configurations.creating

task<CommonTask>("ct"){
    println("compileClasspath= $compileClasspath " )
    msg ="Hello from ${this.name} in ${project.name}"
}
*/
