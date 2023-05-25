package sensors

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay

/*
 Emits the event sonarRobot : sonar( V )
 */
object sonarHCSR04Support {
	lateinit var reader : BufferedReader
	
@kotlinx.coroutines.ObsoleteCoroutinesApi

	fun create( owner : ActorBasic? = null  ){
		println("sonarHCSR04Support CREATING")
		try{
			val p = Runtime.getRuntime().exec("sudo ./SonarAlone")
			reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
			startRead( owner )
		}catch( e : Exception){
			println("WARNING: sonarHCSR04Support does not find SonarAlone")
		}
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi

	fun startRead( owner: ActorBasic?  ){
		GlobalScope.launch{
			var counter = 0
			while( true ){
				var data = reader.readLine()
				//println("sonarHCSR04Support data = $data"   )
				if( data != null ){
					try{
						val v = data.toInt()
						if( v <= 150 ){	//A first fileter ...
							val m1 = "sonar( $v )"
							if(owner != null) owner.emit("sonarRobot",m1 )
							println("sonarHCSR04Support ${counter++} m1 = $m1 "   )
						}
					}catch(e: Exception){}
				}
				delay( 100 ) 
			}
		}
	}
}