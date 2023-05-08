package codedActor

import it.unibo.kactor.*
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage

/*
-------------------------------------------------------------------------------------------------
sonarDataGen
-------------------------------------------------------------------------------------------------
 */

class sonarDataGen ( name : String ) : ActorBasic( name ) {
  
	val data = sequence<Int>{
		var v0 = 20
		yield(v0)
		while(true){
			v0 = v0 - 1
			yield( v0 )
		}
	}
		
 
    override suspend fun actorBody(msg : IApplMessage){
		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "start") startDataReadSimulation(   )
     }
  	
 
	suspend fun startDataReadSimulation(    ){
  			var i = 0
			while( i < 10 ){
 	 			val m1 = "sonar( ${data.elementAt(i*2)} )"
				i++
 				val event = MsgUtil.buildEvent( name,"sonarrobot",m1)								
 				//println("$tt $name | emits $event")
				this.emit( event )
 				delay( 300 )
  			}			
			terminate()
	}

} 
 