package kotlindemo

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine
 
		
fun curThread() : String { 
	return "thread=${Thread.currentThread().name} / nthreads=${Thread.activeCount()}" 
}

fun runBlockThread(){
	    //Calls the specified function block and returns its result.
	    run {
            println("Out start: ${curThread()}")
            Thread.sleep(1500) 
            println("Out ended: ${curThread()}")
        }
}

suspend fun ioBoundFun(){
	val timeElapsed = measureTimeMillis {
		println("IO operation | STARTS in ${curThread()}")
		delay(1000)
	}
	println("IO operation | Done, TIME=$timeElapsed")
}

suspend fun activate(){
    val job1 = GlobalScope.async{
    	ioBoundFun()
    }
    val job2 = GlobalScope.async{
        ioBoundFun()
    }
    if(! job1.isCompleted || ! job2.isCompleted)
    println("Waiting for completion")
    val end1 = job1.await()
    val end2 = job2.await()
    println("All jobs done end1=$end1 end2=$end2" )
}

/*
fun main() = runBlocking {
    println("BEGINS")
    activate()
    println("ENDS")	
//	launch{ ioBoundFun() }
//	println("BYE")
//	val job = launch{ runBlockThread() }
//	//runBlockThread()
//	job.join()
	//Thread.sleep(1510)
}
 		*/

//var counter = 0

suspend fun CoroutineScope.massiveRun( action: suspend () -> Unit) {
    val n=100  //number of coroutines to launch
    val k=1000 //times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")    
}

suspend fun channelTest(){
 val timeElapsed = measureTimeMillis {
     val n = 5
     val channel = Channel<Int>(2)

     val sender = GlobalScope.launch {
         repeat( n ) {
             channel.send( it )
             println("SENDER | sent $it in ${curThread()}")
         }
     }
     delay(500) //The receiver starts after a while ...
     val receiver = GlobalScope.launch {
         for( i in 1..n ) {
             val v = channel.receive()
             println("RECEIVER | receives $v in ${curThread()}")
         }
     }

     delay(3000)
 }
 println("Done. time=$timeElapsed")
}
var counter = 0 //java.util.concurrent.atomic.AtomicInteger()
val counterContext=newSingleThreadContext("CC")
val mutex = kotlinx.coroutines.sync.Mutex()

suspend fun actionWithContext( n: Int){
 withContext(Dispatchers.Default) {
   println("$n) thread=${Thread.currentThread().name}")  
   delay(1000)
   println("ActionWithContext $n done")
 }
}
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testOnContinuation(){
    println("TEST | ${curThread()}")
    var continuation: Continuation<Int>? = null
    GlobalScope.launch( Dispatchers.Unconfined ) {
        println("COROUTINE |  started")
        suspendCoroutine<Int> {
            println("COROUTINE | suspended ${curThread()}")
            continuation = it
        }
        println("COROUTINE|resumes&finish ${curThread()}")
    }
    println("TEST | resumes continuation ${curThread()}")
    continuation!!.resume(3) ?: println("MAIN | no contin")
    println("TEST |  back after resume")	
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun testDispatchers() {
runBlocking {
  launch { //context of the parent runBlocking  
   println("1) runBlocking | ${curThread()}")
 }
 launch( Dispatchers.Unconfined) { //in main thread
   println("2) Unconfined | ${curThread()}")
 }
 launch( Dispatchers.Default) { //DefaultDispatcher
   println("3) Default | ${curThread()}")
 }
 launch( newSingleThreadContext("MyThr")) { //new thread
   println("4) newSingleThreadContext | ${curThread()}")
 }
}
}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(){
    val cpus = Runtime.getRuntime().availableProcessors();
    println("BEGINS CPU=$cpus ${curThread()}")
	testDispatchers()
    println("ENDS ${curThread()}")
}