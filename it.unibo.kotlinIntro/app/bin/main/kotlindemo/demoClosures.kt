package kotlindemo

import kotlinx.coroutines.runBlocking

fun closureDemoWork(){
	
}

fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { closureDemoWork() } )}"  )
	
    println("ENDS ${curThread()}")
}