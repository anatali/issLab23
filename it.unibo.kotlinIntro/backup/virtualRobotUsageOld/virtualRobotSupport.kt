package virtualRobotUsageOld
//virtualRobotSupport.kt

import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import java.io.BufferedReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.InputStreamReader
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay

 //A support for using the virtual robot
 
@kotlinx.coroutines.ObsoleteCoroutinesApi

object virtualRobotSupport {
        private var hostName = "localhost"
        private var port     = 8999
        private val sep      = ";"
        private var outToServer : PrintWriter?     = null
        private var useAppMsg   : Boolean = true
        private var targetRobot : SendChannel<String> = robotActor
        private val applCmdset = setOf("w","s","a","d","z","x","r","l","h"  )
	
	fun setRobotTarget( robot : SendChannel<String>, appMsg : Boolean = true ){
		targetRobot = robot
		useAppMsg   = appMsg
	}
	fun initClientConn( hostNameStr: String = hostName, portStr: String = "$port"  ) {
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
                 val clientSocket = Socket(hostName, port)
                 println("virtualRobotSupport |  CONNECTION DONE")
                 outToServer  = PrintWriter(clientSocket.getOutputStream())
                 startSensorObserver( clientSocket )
             }catch( e:Exception ){
                 println("virtualRobotSupport | ERROR $e")
             }
	}

/*
 	Performs a move 
*/		
        fun domove(cmd: String) {	//cmd is written in cril 
            val jsonObject = JSONObject(cmd )
            val msg = "$sep${jsonObject.toString()}$sep"
            outToServer?.println(msg)
            outToServer?.flush()
        }
//translates application-language in cril
        fun translate(cmd: String) : String{ //cmd is written in application-language
		var jsonMsg = "{ 'type': 'alarm', 'arg': -1 }"
			when( cmd ){
				"msg(w)", "w" -> jsonMsg = "{ 'type': 'moveForward', 'arg': -1 }"
				"msg(s)", "s" -> jsonMsg = "{ 'type': 'moveBackward', 'arg': -1 }"
				"msg(a)", "a" -> jsonMsg = "{ 'type': 'turnLeft', 'arg': -1 }"
				"msg(d)", "d" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1 }"
				"msg(l)", "l" -> jsonMsg = "{ 'type': 'turnLeft', 'arg': 300 }"
				"msg(r)", "r" -> jsonMsg = "{ 'type': 'turnRight', 'arg': 300 }"
				"msg(z)", "z" -> jsonMsg = "{ 'type': 'turnLeft', 'arg': -1 }"
				"msg(x)", "x" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1 }"
				"msg(h)", "h" -> jsonMsg = "{ 'type': 'alarm', 'arg': -1 }"
				else -> println("mbotSupport command $cmd unknown")
			}
            val jsonObject = JSONObject( jsonMsg )
            val msg = "$sep${jsonObject.toString()}$sep"
			return msg
		}
	
        suspend fun doApplMove(cmd: String) {	//cmd is written in application-language
			val msg = translate( cmd )
            outToServer?.println(msg)
            outToServer?.flush()
            if( cmd=="l" || cmd =="r") delay( 300 )
        }
	
suspend fun sendMsgToRobot(msg : String){
	if( useAppMsg  ) forward( msg )   
    else sendJSON_message("sensor($msg)")
}
suspend private fun sendJSON_message(msg : String){
//	println("virtualRobotSupport | sendJSON_message $msg to $targetRobot")
	targetRobot.send( msg  ) 
}
suspend private fun forward( msgContent : String ){
//	println("virtualRobotSupport | forward $msgContent to $targetRobot")
	val dataMsg   = AppMsg.create("sensor","vr","robotactor","$msgContent")
	targetRobot.send( dataMsg.toString() )  
}
	
	
@kotlinx.coroutines.ObsoleteCoroutinesApi

        private fun startSensorObserver( clientSocket : Socket ) {
		val inFromServer = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
		val scope : CoroutineScope = CoroutineScope( Dispatchers.Default )
	    val sensorObserver = scope.launch {
//			println("virtualRobotSupport | startSensorObserver STARTS ")
                while (true) {
                    try {
                        val inpuStr = inFromServer.readLine()
                        val jsonMsgStr =
                            inpuStr!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        //println("virtualRobotSupport | inpuStr= $jsonMsgStr")
                        val jsonObject = JSONObject(jsonMsgStr)
                        //println( "type: " + jsonObject.getString("type"));
                        when (jsonObject.getString("type")) {
                            "webpage-ready" -> println("webpage-ready ")
                            "sonar-activated" -> {
                                val jsonArg   = jsonObject.getJSONObject("arg")
                                val sonarName = jsonArg.getString("sonarName")
                                val distance  = jsonArg.getInt("distance")
								sendMsgToRobot( "$sonarName-$distance" )
 
                            }
                            "collision" -> {
                                val jsonArg    = jsonObject.getJSONObject("arg")
                                val objectName = jsonArg.getString("objectName")
								sendMsgToRobot( "collision_$objectName" )
                             }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
         }//startSensorObserver
	 
}

 







