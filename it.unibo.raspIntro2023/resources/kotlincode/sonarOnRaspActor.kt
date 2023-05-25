package kotlincode

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.GlobalScope
  
class sonarOnRaspActor( val name : String )  {
	
		@kotlinx.coroutines.ObsoleteCoroutinesApi
		val sonarActor = GlobalScope.actor<String>{
 			    for (msg in channel) { // iterate over incoming messages
					println("sonarActor receives: ${msg}  " )
			        when ( msg  ) {
						"start" ->  readInputData()  
			            else -> throw Exception( "unknown" )
			        }
			    }
		}

		@kotlinx.coroutines.ObsoleteCoroutinesApi
		val otherActor = GlobalScope.actor<String>{
				var counter = 0
 			    for (msg in channel) {  
					println("otherActor receives: ${msg}  " )
			        when ( msg  ) {
						"start" ->  while(true) {
							println("otherActor | ${counter++}")
							kotlinx.coroutines.delay( 2000 )
						}
			            else -> throw Exception( "unknown" )
			        }
			    }
		}
	 	
    fun readInputData(){
        val numData = 5
        var dataCounter = 1
        val p : Process = Runtime.getRuntime().exec("sudo ./SonarAlone")   
        val reader = BufferedReader(InputStreamReader(p.getInputStream()))
		
        while( true ){
             var data = reader.readLine()    //blocking
			 //println("data ${dataCounter} => $data " )
			 try{
				 var v    = data.toInt()
				 if( v <= 400 && (dataCounter++ % numData == 0) ){
					 //println("data ${dataCounter} = $data " )
	                 println("sonar data= : $v"  )
	             }
			 }catch( e : Exception){}
        }
    }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi	
fun main() = runBlocking {
	val maxtime = 600000L
	println("WARNING: THE SYSTEM WILL STOP AFTER ${maxtime/60000} minutes")
	val appl = sonarOnRaspActor("sonarOnRaspActor")
 	appl.sonarActor.send("start")
 	appl.otherActor.send("start")
	kotlinx.coroutines.delay( maxtime )
 }