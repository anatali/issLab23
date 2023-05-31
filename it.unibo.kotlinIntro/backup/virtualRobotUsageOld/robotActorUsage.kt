package virtualRobotUsageOld
//robotActorUsage.kt

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

val initMsg         = AppMsg.create("init","main","robotactor")
val endMsg          = AppMsg.create("end", "main","robotactor")
val moveForwardMsg  = AppMsg.create("move","main","robotactor","w", AppMsgType.dispatch) 
val moveBackwardMsg = AppMsg.create("move","main","robotactor","s") 
val haltRobotMsg    = AppMsg.create("move","main","robotactor","h") 

@kotlinx.coroutines.ObsoleteCoroutinesApi

suspend fun forward(  msg : AppMsg ){
	if( msg.MSGTYPE  == AppMsgType.dispatch.toString() )
		robotActor.send( msg.toString()  )
}
 
@kotlinx.coroutines.ObsoleteCoroutinesApi

suspend fun sendCommands(   ) {
	virtualRobotSupport.setRobotTarget( robotActor  ) //Configure - Inject

	forward( initMsg )
//    for (i in 1..2) {
		 forward( moveForwardMsg )
         delay(1000)
		 forward( haltRobotMsg )
         delay(1000)

         forward( moveBackwardMsg )
         delay(1000)
 		 forward( haltRobotMsg )
         delay(500)
//   }
	forward( endMsg )
}

@kotlinx.coroutines.ObsoleteCoroutinesApi

fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
	sendCommands(   )
    println("BYE")
}

