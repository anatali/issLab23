package rx

import it.unibo.kactor.*
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage

/*
 -------------------------------------------------------------------------------------------------
 
-------------------------------------------------------------------------------------------------
 */

class sonarSimulator ( name : String ) : ActorBasic( name ) {
  
	val data = sequence<Int>{
		var v0 = 15
		yield(v0) 
		while(true){
			v0 = v0 - 1
			yield( v0 )
		}
	}
		
 
    override suspend fun actorBody(msg : IApplMessage){
        //println("	--- sonarSimulator | received  msg= $msg "  ) 
		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "start") startDataReadSimulation(   )
     }
  	
 
	suspend fun startDataReadSimulation(    ){
  			var i = 0
			while( i < 10 ){
 	 			val m1 = "sonar( ${data.elementAt(i*2)} )"
				i++
				//println("$tt $name | generates $m1")
 				val event = MsgUtil.buildEvent( name,"sonarRobot",m1)								
 				emitLocalStreamEvent( event )
 				delay( 500 )
  			}			
			terminate()
	}

} 

 