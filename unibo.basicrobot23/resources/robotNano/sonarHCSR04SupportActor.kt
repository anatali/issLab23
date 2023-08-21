package robotNano

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import kotlin.math.roundToInt


/* 
 Emits the event sonarRobot : sonar( V )
 */
class sonarHCSR04SupportActor ( name : String ) : ActorBasic( name ) {
	lateinit var reader : BufferedReader

	init{
		//autostart
		runBlocking{  autoMsg("sonarstart","do") }
	}

//@kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun actorBody(msg : IApplMessage){
 		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "sonarstart"){
			println("sonarHCSR04SupportActor CREATING")
			try{
				//val p = Runtime.getRuntime().exec("sudo ./SonarAlone")
				val p  = Runtime.getRuntime().exec("sudo python3 sonar.py")
				reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
				startRead(   )
			}catch( e : Exception){
				println("WARNING: sonarHCSR04SupportActor does not find SonarAlone")
			}
 		}
     }
		
//@kotlinx.coroutines.ObsoleteCoroutinesApi

	suspend fun startRead(   ){
		println("sonarHCSR04SupportActor startRead  "   )
 		var counter = 0
		GlobalScope.launch{	//to allow message handling
		while( true ){
				var data = reader.readLine()
				//println("sonarHCSR04SupportActor data = $data"   )
				if( data != null ){
					try{
						val v = data.toFloat().roundToInt()
						if( v <= 150 ){	//A first filter ...
							val m1 = "sonar( $v )"
							val event = MsgUtil.buildEvent( "sonarHCSR04Support","sonarRobot",m1)
							//if( counter % 10 == 0) println("$tt $name | emits $event after ${counter++}"  )
							emitLocalStreamEvent( event )		//not propagated to remote actors
						}
					}catch(e: Exception){
						println("sonarHCSR04SupportActor startRead ERROR: ${e.message} "   )
					}
				}
				delay( 100 ) 
 		}
		}
	}
}