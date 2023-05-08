package coap

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
	
	private val context     = "ctxcoapdemo"
	private val sendactor   = "coapalien"
	private val destactor   = "actorcoap"
	private val msgId       = "cmd"
	
	private val resorcePath = "$context/destactor"
	private var counter     = 0
	
	fun init(){
       val uriStr = "coap://localhost:8037/$context/$destactor"
       client.uri = uriStr
//       client.observe(object : CoapHandler {
//            override fun onLoad(response: CoapResponse) {
//                println("actorQakCoapClient GET RESP-CODE= " + response.code + " content:" + response.responseText)
//            }
//            override fun onError() {
//                println("FAILED")
//            }
//        })		
	}

	fun sendToServer(move: String) {
		if( move == "h" ){
			client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
			//println("GET RESPONSE CODE=  ${respGet.code} ${respGet.getResponseText()}")
			return
		}
		
		if( move == "p" ){
			val r = MsgUtil.buildRequest("coapalien", "cmd", "cmd(${counter++})", "actorcoap" )
			val respPut = client.put(r.toString(), MediaTypeRegistry.TEXT_PLAIN)
			println("PUT ${r} RESPONSE CODE=  ${respPut.code} ${respPut.getResponseText()}")
		}else{
			val d = MsgUtil.buildDispatch("coapalien", "cmd", "cmd($move)", "actorcoap" )
	        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
	        println("PUT ${d} RESPONSE CODE=  ${respPut.code}")
		}
    }
}

fun console(){
	val read = Scanner(System.`in`)
	print("MOVE (h,w,s,r,l,z,x,a,d,p,q)>")
	var move = read.next()
	while( move != "q"){
		when( move ){
			"h" -> actorQakCoapClient.sendToServer("h")
			"w" -> actorQakCoapClient.sendToServer("w")
			"s" -> actorQakCoapClient.sendToServer("s")
			"r" -> actorQakCoapClient.sendToServer("r")
			"l" -> actorQakCoapClient.sendToServer("l")
			"x" -> actorQakCoapClient.sendToServer("x")
			"z" -> actorQakCoapClient.sendToServer("z")
			"p" -> actorQakCoapClient.sendToServer("p")
			"d" -> actorQakCoapClient.sendToServer("d")
			"a" -> actorQakCoapClient.sendToServer("a")
 			else -> println("unknown")
		}
		print("MOVE (h,w,s,r,l,z,x,a,d,q)>")
		move = read.next()
	}
	print("BYE")
	System.exit(1)
}



fun main( ) = runBlocking  {
		actorQakCoapClient.init()
		
		console()
}

