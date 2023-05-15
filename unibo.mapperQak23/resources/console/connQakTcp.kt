package console

import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.interfaces.Interaction
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils



class connQakTcp(  )  {
	lateinit var conn   : Interaction //IConnInteraction
	val hostAddr 		= "localhost" //   172.17.0.2 "192.168.1.5" "localhost"
	val port     		= "8020"

	  fun createConnection( ){ //hostIP: String, port: String
		conn = TcpClientSupport.connect( hostAddr, port.toInt(),10 )
		println("connQakTcp createConnection $hostAddr:$port")
	}
	
	  fun forward( msg: IApplMessage){
		println("connQakTcp | forward: $msg")	
 		conn.forward( msg.toString()  )
	}
	
	  fun request( msg: IApplMessage ) : String {
 		conn.forward( msg.toString()  )
		//Acquire the answer	
		val answer = conn.receiveMsg()
		CommUtils.outmagenta("connQakTcp | answer= $answer")
		return answer
	}
	
	  fun emit( msg: IApplMessage ){
 		conn.forward( msg.toString()  )
	}	
}