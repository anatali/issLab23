package coap.observer

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic

object ch : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("actorQakCoapObserver ch | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("actorQakCoapObserver ch | FAILED")
            }
        } 
 
object actorQakCoapObserver {

    private val client = CoapClient()
    
@kotlinx.coroutines.ObsoleteCoroutinesApi

	 fun activate( context: String, destactor: String, ipaddr : String , owner: ActorBasic? = null){ 
       val uriStr = "$ipaddr/$context/$destactor"
 	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
	   client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
	   client.observe( ch )
      
    }

 }

 
@kotlinx.coroutines.ObsoleteCoroutinesApi

fun main( ) { //82.56.16.191
		actorQakCoapObserver.activate("ctxledalone", "led", "192.168.1.22:8080" )
		System.`in`.read()   //to avoid exit 
 }

