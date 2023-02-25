println(" ...... build in taskProperties  ")

//Properties at project level
val myprop1: String by project
extra["purpose"] = "todo"

//Properties
val myprop2 by extra("myprop2-value defined using by extra")
ext{
    set( "myprop3", "myprop3-value defined using the ext namespace ")
}

//Properties for tasks
tasks.all { extra["logo"] = "this is a task" }
//sourceSets.all { extra["purpose"] = null }        //also this works

task("showMyProps"){
     doLast {
        println("myprop1                   = ${myprop1}")
        println("myprop2                   = ${myprop2}")
        println("ext.get(\"myprop3\")      = ${ext.get("myprop3")}")
        println("extra[\"logo\"]           = ${extra["logo"]}")
        println("project.extra[\"purpose\"]= ${project.extra["purpose"]}") //does not work
    }
}
