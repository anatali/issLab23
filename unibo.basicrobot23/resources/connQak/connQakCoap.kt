package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import unibo.basicomm23.interfaces.IApplMessage


class connQakCoap() : connQakBase() {

lateinit var client   : CoapClient
	
	override fun createConnection(  ){
 			println("connQakCoap | createConnection hostIP=${hostAddr} port=${port}")
			val url = "coap://$hostAddr:$port/${ctxqakdest}/${qakdestination}"
			client = CoapClient( url )
			client.setTimeout( 1000L )
 			//initialCmd: to make console more reactive at the first user cmd
 		    val respGet  = client.get( ) //CoapResponse
			if( respGet != null )
				println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code}")
			else
				println("connQakCoap | url=  ${url} FAILURE")
	}
	
	override fun forward( msg: IApplMessage ){		
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        if( respPut != null ) println("connQakCoap | PUT forward ${msg} RESPONSE CODE=  ${respPut.code}")
		else println("connQakCoap | PUT forward ${msg} RESPONSE null")		
	}
	
	override fun request( msg: IApplMessage ) : String {
 		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		if( respPut != null )
  		println("connQakCoap | answer= ${respPut.getResponseText()}")
		return 	respPut.toString()
	}
	
	override fun emit( msg: IApplMessage){
		val url = "coap://$hostAddr:$port/$ctxqakdest"		 
		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
         val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")		
		
	}	
}