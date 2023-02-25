println(" ...... build in taskClasses sub-project  ")

open class GreetingTask : DefaultTask() {
    //@get:Input
    @Input
    var greeting = "default hello from GreetingTask"

    @TaskAction
    fun greet() {
        println(greeting)
    }
}

// Create a task using the new tasktype and customize
 tasks.register<GreetingTask>("mygreetings"){
     greeting = "my customised greetings from GreetingTask"
 }