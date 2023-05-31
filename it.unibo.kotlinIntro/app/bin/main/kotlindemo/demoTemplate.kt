package kotlindemo
/*
 demoTemplate.kt
 TEMPLATE for the examples
 */
import kotlinx.coroutines.runBlocking

val cpus = Runtime.getRuntime().availableProcessors();

inline fun measureTimeMillis( block: () -> Unit ): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}

//var maxNumThreads = 0

fun curThread() : String { 
 	val nt = Thread.activeCount()
//	if( maxNumThreads < nt ) maxNumThreads = nt
	return "thread=${Thread.currentThread().name} / nthreads=${nt}  " 
}
		
//fun curThread() : String { 
//	return "thread=${Thread.currentThread().name} | nthreads=${Thread.activeCount()}" 
//}

fun myDemoWork(){
	println("Hello from myDemoWork"); 
}

fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { myDemoWork() } )}"  )
	
    println("ENDS ${curThread()}")
}