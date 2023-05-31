package concur

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.actor
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.launch

fun curThread() : String { 
	return "thread=${Thread.currentThread().name}" 
}

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
class CounterMsg(
  val cmd:String,
  val response:CompletableDeferred<Int>?=null){
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
//fun  counterActor()  =  GlobalScope.actor<CounterMsg> {
val  counter  =  GlobalScope.actor<CounterMsg> {
    var localCounter = 0 // actor state
    for (msg in channel) { // iterate over incoming messages
		if( localCounter % 10000 == 0 ) println("${msg.cmd} | $localCounter in ${curThread()}")
        when ( msg.cmd ) {
            "INC" -> localCounter++
            "DEC" -> localCounter--
            "GET" -> msg.response?.complete(localCounter) 
            else -> throw Exception( "unknown" )
        }    }
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() = runBlocking{
    val cpus = Runtime.getRuntime().availableProcessors();
    println("BEGINS with $cpus  cores")
    //val counter = counterActor() // create the actor
    val initVal = CompletableDeferred<Int>()
    counter.send(CounterMsg("GET", initVal))
    println("Counter INITIAL VALUE=${initVal.await()}")
    massiveRun {
        counter.send(CounterMsg("INC") )
    }
    val finalVal = CompletableDeferred<Int>()
    counter.send(CounterMsg("GET", finalVal))
    println("Counter FINAL VALUE= = ${finalVal.await()}")
    counter.close() // shutdown the actor
    println("ENDS ")
}
