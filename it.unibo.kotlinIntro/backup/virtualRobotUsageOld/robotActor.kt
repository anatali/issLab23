package virtualRobotUsageOld
//robotActor.kt

import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
  
//Actor that includes the business logic; the behavior is message-driven 
@kotlinx.coroutines.ObsoleteCoroutinesApi

val robotActor  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
	var state    = "working"
	
	fun doInit() = virtualRobotSupport.initClientConn() 
	fun doEnd()  = { state = "end"  }
		
	suspend fun doCollision(msg : AppMsg){
		println("robotActor handles ${msg.CONTENT} by going back a little");
		virtualRobotSupport.doApplMove( "s" )
		delay(100)		
		virtualRobotSupport.doApplMove( "h" )
	}
	suspend fun doSensor(msg : AppMsg){
		println("robotActor handles: ${msg.CONTENT}")
		if( msg.CONTENT.startsWith("collision") ) doCollision(msg)
	}
	
	suspend fun doMove( move: String ){
  		virtualRobotSupport.doApplMove( move )		//move in the application-language 
	}
	
	while( state == "working" ){
		var msg = channel.receive()
		println("robotActor receives: $msg ")
		val applMsg = AppMsg.create(msg)
 		//println("robotActor applMsg.MSGID=${applMsg.MSGID} ")
		when( applMsg.MSGID ){
			"init"      -> doInit()
			"end"       -> doEnd()
			"sensor"    -> doSensor(applMsg)
 			"move"      -> doMove(applMsg.CONTENT)
			else        -> println("NO HANDLE for $msg")
		}		
 	}//while
 	println("robotActor ENDS state=$state")
}

 

