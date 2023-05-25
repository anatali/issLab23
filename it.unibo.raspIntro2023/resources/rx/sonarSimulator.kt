package rx

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils

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
		
//@kotlinx.coroutines.ObsoleteCoroutinesApi

    override suspend fun actorBody(msg : IApplMessage){
  		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "simulatorstart") startDataReadSimulation(   )
     }
  	
//@kotlinx.coroutines.ObsoleteCoroutinesApi

	suspend fun startDataReadSimulation(    ){
  			var i = 0
			while( i < 10 ){
 	 			val m1 = "sonar( ${data.elementAt(i*2)} )"
				i++
 				val event = CommUtils.buildEvent( name,"sonarRobot",m1)
  				emitLocalStreamEvent( event )
 				CommUtils.outyellow("$tt $name | generates $event")
//				emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
 				delay( 500 )
  			}			
			terminate()
	}

} 

//@kotlinx.coroutines.ObsoleteCoroutinesApi
//
//fun main() = runBlocking{
// //	val startMsg = MsgUtil.buildDispatch("main","start","start","datasimulator")
//	val consumer  = dataConsumer("dataconsumer")
//	val simulator = sonarSimulator( "datasimulator" )
//	val filter    = dataFilter("datafilter", consumer)
//	val logger    = dataLogger("logger")
//	simulator.subscribe( logger ).subscribe( filter ).subscribe( consumer ) 
//	MsgUtil.sendMsg("start","start",simulator)
//	simulator.waitTermination()
// } 