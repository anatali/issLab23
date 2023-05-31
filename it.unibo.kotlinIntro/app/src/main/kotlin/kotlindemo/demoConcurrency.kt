package kotlindemo
//demoConcurrency.kt

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

var counterAtomic = java.util.concurrent.atomic.AtomicInteger()

suspend fun CoroutineScope.massiveRun( action: suspend () -> Unit ) {
    val n=100		//number of coroutines to launch
    val k=1000		//times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch { repeat(k) { action() }  }
        }
        jobs.forEach { it.join() } //wait for termination of all coroutines
    }
    println("Completed ${n * k} actions in $time ms")
}

//@kotlinx.coroutines.ObsoleteCoroutinesApi
//
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
 
    GlobalScope.massiveRun{counterAtomic.incrementAndGet()}
 
    println("BYE with Counter = $counterAtomic")
    println("ENDS ${curThread()}")
}