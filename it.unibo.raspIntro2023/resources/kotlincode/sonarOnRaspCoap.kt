package kotlincode

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor 
import java.io.BufferedReader
import java.io.InputStreamReader
import javacode.CoapSupport
import it.unibo.kactor.MsgUtil
import java.io.FileInputStream

class sonarOnRaspCoap( val name : String, val scope: CoroutineScope = GlobalScope )  {

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	val actor = scope.actor<String>{
 			    for (msg in channel) { // iterate over incoming messages
					println("sonarOnRaspCoap receives: ${msg}  " )
			        when ( msg  ) {
						"start" ->  readInputData() //scope.launch{ readInputData() }
			            else -> throw Exception( "unknown" )
			        }
			    }
			}
	
	var coapSupport : CoapSupport	 

	
	init{
	    val reader = BufferedReader(InputStreamReader(FileInputStream("coapConfig.txt")) )
	    val coapAddr = reader.readLine()
		val path     = reader.readLine()
		println("$coapAddr - $path")
		coapSupport = CoapSupport(coapAddr, path)
	}

    suspend fun readInputData(){
		println("readInputData starts" )
        val numData = 10
        var dataCounter = 1
        val p : Process = Runtime.getRuntime().exec("sudo ./SonarAlone")  //machineExec("sudo ./SonarAlone")
        val reader = BufferedReader(InputStreamReader(p.getInputStream()))
		
        while( true ){
             var data = reader.readLine()    //blocking
			 dataCounter++
             println("data ${dataCounter} = $data " )
             if( dataCounter % numData == 0 ) { //every numData ...
                val m = MsgUtil.buildEvent(name, "sonar", "sonar($data)")
                println("EMIT to CoAP: $m"  )
				if( ! coapSupport.updateResource( m.toString() ) ) println("EMIT failure"  )
             }
        }
    }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
	
fun main() = runBlocking {
	val maxtime = 600000L
	val appl = sonarOnRaspCoap("sonarOnRaspCoap")
    appl.actor.send( "start" )
	println("WARNING: THE SYSTEM WILL STOP AFTER ${maxtime/60000} minutes")
	kotlinx.coroutines.delay( maxtime )
}