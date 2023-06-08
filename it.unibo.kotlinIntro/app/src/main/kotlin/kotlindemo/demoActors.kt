/*
-------------------------------------
demoActors.kt
-------------------------------------
*/
package kotlindemo

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import unibo.basicomm23.utils.CommUtils

var dispatcher    = newSingleThreadContext("single")
lateinit var receiverActor : SendChannel<String>

@kotlinx.coroutines.ObsoleteCoroutinesApi

fun startReceiver( scope : CoroutineScope){
	receiverActor = scope.actor<String>( dispatcher, capacity = 2) {
		//actor is a coroutine builder (dual of produce)
		CommUtils.outgreen("receiverActor STARTS")
		/*
		channel is a reference to the mailbox channel that this coroutine receives messages from.
		It is provided for convenience, so that the code in the coroutine can refer to the channel
		as channel as apposed to this.
		All the ReceiveChannel functions on this interface delegate to the channel instance
		returned by this function.
		 */
		delay(500)		//time of initialization ...
		var msg = channel.receive()
		while( msg != "end" ){ 	//message-driven
			delay(500)   //time to elaborate the msg ...
			CommUtils.outgreen("receiverActor receives $msg ${curThread()}")
			msg = channel.receive()
		}
		println("receiverActor ENDS ${curThread()}")
	}
}
@kotlinx.coroutines.ObsoleteCoroutinesApi

fun startSender( ){
	val myScope = CoroutineScope(dispatcher)
	//myScope.launch{	//(1)
	val senderActor = myScope.actor<String> {	//(2)
		CommUtils.outblue("sender STARTS")
		for( i in 1..4 ) {
			receiverActor.send("Hello$i")
			CommUtils.outblue("sender has sent Hello$i ${curThread()}")
		}
		receiverActor.send("end")
		CommUtils.outblue("sender ENDS ${curThread()}" )
 	}
}

fun actorsSenderReceiver(){
	runBlocking {
		startReceiver(this)	//first
		startSender()
	}
}

fun main() {
	println("BEGINS CPU=$cpus ${curThread()}")
	actorsSenderReceiver();
	println("ENDS main ${curThread()}")
}