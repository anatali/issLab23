package coap

import org.eclipse.californium.core.CoapClient
import it.unibo.kactor.ActorBasic
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil

object testcoap{
lateinit var client : CoapClient
lateinit var host   : String
	
	fun connect( address : String ){
		host = address
 	}
	
	private fun setClientForPath( path : String ){
		val url = host + "/" + path
		println("testcoap | setClientForPath url=$url")
		client = CoapClient( url )
		client.setTimeout( 1000L )
	}
	
//	fun updateResource(  path: String, msg : String ){
//		setClientForPath( path )
//		println("testcoap | updateResource $msg $client")
// 		//val d = MsgUtil.buildDispatch("test", "cmd", "cmd($msg)", "basicrobot" )
//		lateinit var m : ApplMessage 
//		if( msg == "p"){
//			m = MsgUtil.buildRequest("test", "step", "step(350)", "basicrobot" )
//		}else{
//			m = MsgUtil.buildDispatch("test", "cmd", "cmd($msg)", "basicrobot" )
//		}
// 		println("testcoap | updateResource $m  ")
//		val resp = client.put(m.toString(), MediaTypeRegistry.TEXT_PLAIN) //: CoapResponse 
//		println("testcoap | updateResource respCode=${resp.getCode()}")
//		//println("testcoap | updateResource respCode=${resp.getResponseText()}")
//	}
	
	fun readResource(   path : String ){
		setClientForPath( path )
		val respGet : CoapResponse = client.get( )
		val v = respGet.getResponseText()
		println("	testcoap | readResource: $v  ")
	}
}

fun main() = runBlocking{
	testcoap.connect("coap://localhost:8037")
// 	testcoap.readResource("example")
  	testcoap.readResource("ctxcoapdemo/actorcoap")
}