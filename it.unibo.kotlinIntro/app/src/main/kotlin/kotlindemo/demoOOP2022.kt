/*
-------------------------------------
demoOOP2022.kt
-------------------------------------
*/
import kotlindemo.*


/*
-----------------------------------------------------------
User interface per le demo
-----------------------------------------------------------
*/
val choicheOop = arrayOf(
     "testObject","testClass","testPropertyDelegate",
     "testDataClass","testCompanion","testInheritance"  )

var demoOopTodo : () -> Unit = { println("nothing to do") }

fun showOopChoices(){
    var n=1
    println("-------------------------------------------")
    choicheOop.forEach{ println("$n:$it"); n=n+1 }
    println("-------------------------------------------")
}

//DEMO user interface
fun doOopDemo( input : Int ){
    println("BEGINS CPU=$cpus ${curThread()} " )
    when( input ){
        1 ->  demoTodo =  { testObject() }
        2 ->  demoTodo =  { testClass() }
        3 ->  demoTodo =  { testPropertyDelegate() }
        4 ->  demoTodo =  { testDataClass() }
        5 ->  demoTodo =  { testCompanion() }
        6 ->  demoTodo =  { testInheritance() }
         else ->  { println("command unknown") }  //Note the block
    }
    println( "work done in time= ${measureTimeMillis(  demoOopTodo )}"  )
    println("ENDS ${curThread()}")
}

fun main() {
    showOopChoices()
    var input =  readInt()
    while( input != 0 ){
        doOopDemo( input )
        demoOopTodo = 	{ println("nothing to do") }
        showOopChoices()
        input    =  readInt()
    }
    println( "BYE")
}

