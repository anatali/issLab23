package observers

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapHandler

object  pathexecCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8020"		//5683 default
	private val context     = "ctxbasicrobot"
 	private val destactor   = "pathexec"


	 fun activate(  ){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
           println("pathexecCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("pathexecCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
            } 
            override fun onError() {
                println("pathexecCoapObserver | FAILED")
            }
        })		
	}
 }

 


fun main( ) {
        pathexecCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }