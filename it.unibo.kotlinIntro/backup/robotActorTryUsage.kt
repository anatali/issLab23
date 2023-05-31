package virtualRobotUsage
//robotActorTryUsage.kt
/*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
 
@kotlinx.coroutines.ObsoleteCoroutinesApi

suspend fun sendCrilCommands(   ) {
	virtualRobotSupport.setRobotTarget( robotActorTry, appMsg = false ) //Configure - Inject
    robotActorTry.send("init")
    var jsonString  : String
	val time = 2000L	//time = 1000 => collision
//    for (i in 1..2) {
        jsonString = "{ 'type': 'moveForward', 'arg': $time }"
        robotActorTry.send("move($jsonString)")
        delay(time)

        jsonString = "{ 'type': 'moveBackward', 'arg': ${time} }"
		robotActorTry.send("move($jsonString)")
        delay(1000)
//    }
	robotActorTry.send("end")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi

fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
	sendCrilCommands(   )
    println("BYE")
}
*/
