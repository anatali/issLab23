package coap.client

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler 

object ch : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("actorQakCoapClient | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("actorQakCoapClient | FAILED")
            }
        } 
object actorQakCoapClient {

    private val client = CoapClient()
	
	private val context     = "ctxledalone"
	private val sendactor   = "coapalien"
	private val destactor   = "led"
	private val msgId       = "cmd"
	
	private val resorcePath = "$context/destactor"
	private var counter     = 0
	
	fun init(){
       val uriStr = "coap://192.168.1.14:8080/$context/$destactor"
       client.uri = uriStr
	   print("actorQakCoapClient init uriStr ")
	   client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
       client.observe( ch  )		
	}

	fun sendToServer(move: String) {
		if( move == "h" ){
			client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
			return
		}
		
		if( move == "1" ){
 			val d = MsgUtil.buildDispatch("coapalien", "turnOn", "turnOn(ok)", "led" )
	        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
	        println("PUT ${d} RESPONSE CODE=  ${respPut.code}")
			return
		}
		
		if( move == "0" ){
 			val d = MsgUtil.buildDispatch("coapalien", "turnOff", "turnOff(ok)", "led" )
	        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
	        println("PUT ${d} RESPONSE CODE=  ${respPut.code}")
			return
		}

    }
}

fun console(){
	val read = Scanner(System.`in`)
	print("MOVE (h,1,0)>")
	var move = read.next()
	while( move != "q"){
		when( move ){
			"h" -> actorQakCoapClient.sendToServer("h")
			"1" -> actorQakCoapClient.sendToServer("1")
			"0" -> actorQakCoapClient.sendToServer("0")
  			else -> println("unknown")
		}
		print("MOVE (h,1,0)>")
		move = read.next()
	}
	print("BYE")
	System.exit(1)
}

fun main( )  { 
		actorQakCoapClient.init() 		
 		console()
}

