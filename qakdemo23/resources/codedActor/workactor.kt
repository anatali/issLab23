package codedActor

import it.unibo.kactor.*
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils

/*
-------------------------------------------------------------------------------------------------
workactor
-------------------------------------------------------------------------------------------------
 */

class workactor ( name : String ) : ActorBasic( name, confined=true ) {
 	
 	var i = 0
     
    override suspend fun actorBody(msg : IApplMessage){
		if( msg.msgId() == "start"){
			workStep(   )
		}else CommUtils.outgreen("$tt $name | received  $msg "  )
     }
  	
	suspend fun workStep(    ){
		i++		
		val alarm = CommUtils.buildEvent(name,"alarm","alarm$name-$i")
		CommUtils.outmagenta("$tt $name | emitting $alarm")
		emit( alarm )
 		if( i == 3 ) terminate()
 		else {
 			//delay( 30L * i )
 			delay( 2000L   )
 			forward("start", "start(do)" , name )
 		}
	}
} 
 