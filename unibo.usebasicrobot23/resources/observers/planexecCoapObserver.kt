package observers

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapHandler

object  planexecCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8020"		//5683 default
	private val context     = "ctxbasicrobot"
 	private val destactor   = "planexec"


	 fun activate(  ){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
       println("planexecCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("planexecCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
            } 
            override fun onError() {
                println("planexecCoapObserver | FAILED")
            }
        })		
	}
 }

 


fun main( ) {
        planexecCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }