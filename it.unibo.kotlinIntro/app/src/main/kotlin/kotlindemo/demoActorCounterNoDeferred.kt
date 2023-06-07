/*
-------------------------------------
demoActorCounterNoDeferred.kt
-------------------------------------
*/
/*
Example of  wrong code
 */
package kotlindemo

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel



class CounterMsgNoDeferred(
	val cmd:String,
	var response: Int ){
}

@kotlinx.coroutines.ObsoleteCoroutinesApi

fun createCounterNoDeferred(scope : CoroutineScope):SendChannel<CounterMsgNoDeferred>{
 val counterActor = scope.actor<CounterMsgNoDeferred> {
	var k = 0 	//actor state
	for (msg in channel) { // iterate over incoming messages
	   if( k>0 && k % 10000 == 0  && msg.cmd != "GET" )
		println("counter ${msg.cmd} | $k in ${curThread()} full=${channel.isFull}")
		when ( msg.cmd ) {
			"INC" -> k++
			"DEC" -> k--
			"GET" -> { println("GET ... ${msg.response}"); msg.response = k}
			"END" -> { println("counter closing ..."); channel.close() }  
			else -> throw Exception( "unknown" )
		}
 	  }//for
	}
 return counterActor
}

suspend fun showValueNoDeferred(counterActor: SendChannel<CounterMsgNoDeferred>){
    var cVal : Int = 0
    counterActor.send( CounterMsgNoDeferred("GET", cVal) )
	println("CounterNoDeferred VALUE=${cVal}")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi

fun main() {
	println("BEGINS CPU=$cpus ${curThread()}")
	runBlocking {
		val counter = createCounterNoDeferred(this)
		showValueNoDeferred(counter)
		var cVal : Int = 0
		//sendManyMessages(this, counter)
		manyRun {counter.send( CounterMsgNoDeferred("INC", cVal) ) }

		//println("CounterNoDeferred IMMEDIATE VALUE=${cVal}")

		showValueNoDeferred(counter)

		//counter.send(CounterMsgNoDeferred("END", cVal))

		println("JOIN ${curThread()}")
		(counter as Job).join()    //WAIT for termination
		showValueNoDeferred(counter)

		counter.close() //shutdown the actor

		println("ENDS runBlocking ${curThread()}")
	}
	println("ENDS main ${curThread()}")
}