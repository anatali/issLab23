package kotlindemo
//demoCpsAsynch.kt

fun readCpsAsynch( callback:(String)-> Unit ) : Unit{
  kotlin.concurrent.thread(start = true) {		// Single Abstract Method conversion (SAM)
	println("readCpsAsynch  ... | ${curThread()} ")
	Thread.sleep(3000)	//Long-term action
	println("readCpsAsynch done ")
	callback( "myinputasynchcps" )
  }
}

fun doJobAsynchCps( n: Int  ){
	readCpsAsynch{evalCps( n, it) { showAction( it )}}
	println("Here I can do other jobs ... ")
}
 
fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")	
    println( "work done in time= ${measureTimeMillis(  { doJobAsynchCps(10) } )}"  )	
    println("ENDS ${curThread()}")
}

