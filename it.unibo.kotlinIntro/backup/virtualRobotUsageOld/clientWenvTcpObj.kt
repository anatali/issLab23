package virtualRobotUsageOld
//clientWenvTcpObj.kt in it.unibo.kotlinIntro\src\virtualRobotUsage\clientWenvTcpObj.kt
 
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object clientWenvTcpObj {
        private var hostName = "localhost"
        private var port     = 8999
        private val sep      = ";"
        private var outToServer: PrintWriter?     = null
        private var inFromServer: BufferedReader? = null 


        fun initClientConn( hostNameStr: String = hostName, portStr: String = "$port"  ) {
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
                 val clientSocket = Socket(hostName, port)
                 println("clientWenvTcp |  CONNECTION DONE")
                 inFromServer = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                 outToServer  = PrintWriter(clientSocket.getOutputStream())
                 startTheReader( )
             }catch( e:Exception ){
                 println("clientWenvTcp | ERROR $e")
             }
        }


//Send a message wriiten in JSON on the TCP connection
	fun sendMsg(jsonString: String) {
		val jsonObject = JSONObject(jsonString)
		val msg = "$sep${jsonObject.toString()}$sep"
	println("clientWenvTcp | sendMsg $msg")
		outToServer?.println(msg)
		outToServer?.flush()		//QUITE IMPORTANT!!
	}

 
//Launch a coroutine that waits for data from the TCP connection
        private fun startTheReader(  ) {
	    val scope : CoroutineScope = CoroutineScope( Dispatchers.Default )
        scope.launch {
                while (true) {
                    try {
                         val inpuStr = inFromServer?.readLine()
                        val jsonMsgStr =
                            inpuStr!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        //println("clientWenvTcp | inpuStr= $jsonMsgStr")
                        val jsonObject = JSONObject(jsonMsgStr)
                        //println( "type: " + jsonObject.getString("type"));
                        when (jsonObject.getString("type")) {
                            "webpage-ready" -> println("webpage-ready ")
                            "sonar-activated" -> {
                                //println("sonar-activated ")
                                val jsonArg = jsonObject.getJSONObject("arg")
                                val sonarName = jsonArg.getString("sonarName")
                                val distance = jsonArg.getInt("distance")
                                println("clientWenvTcp | sonarName=$sonarName distance=$distance")

                            }
                            "collision" -> {
                                //println( "collision"   );
                                val jsonArg = jsonObject.getJSONObject("arg")
                                val objectName = jsonArg.getString("objectName")
                                println("clientWenvTcp | collision objectName=$objectName")
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
         }//startTheReader
}//clientWenvTcpObj


suspend fun sendSomeCommand(   ) {
    clientWenvTcpObj.initClientConn( )
    var jsonString  : String
	val time = 1300
//    for (i in 1..2) {
        jsonString = "{ 'type': 'moveForward', 'arg': $time }"
         clientWenvTcpObj.sendMsg(jsonString)
         delay(1000)

        jsonString = "{ 'type': 'moveBackward', 'arg': $time }"
         clientWenvTcpObj.sendMsg(jsonString)
         delay(1000)
//    }
}
 
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    sendSomeCommand(   )
    println("BYE")
}
