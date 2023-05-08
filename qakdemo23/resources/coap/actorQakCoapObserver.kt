package coap

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 
 
object actorQakCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8037"		//5683 default
	private val context     = "ctxcoapdemo"
 	private val destactor   = "actorcoap"
//	private val msgId       = "cmd"

 

	 fun activate( owner: ActorBasic? = null){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
 				if(  owner!== null ) owner.scope.launch{
 					val event = MsgUtil.buildEvent( "observer","local_resrep","resrep('$content')")								
					owner.emit( event, avatar=true ) //to avoid that auto-event will be discarded
				}
           } 
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}

 }

 
 

fun main( ) {
		actorQakCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }

