package connQak

import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.interfaces.Interaction
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils



class connQakTcp(  ) : connQakBase( ){
	lateinit var conn   : Interaction //IConnInteraction
	
	override fun createConnection( ){ //hostIP: String, port: String
		conn = TcpClientSupport.connect( hostAddr, port.toInt(),10 )
		println("connQakTcp createConnection $hostAddr:$port")
	}
	
	override fun forward( msg: IApplMessage){
		println("connQakTcp | forward: $msg")	
 		conn.forward( msg.toString()  )
	}
	
	override fun request( msg: IApplMessage ) : String {
 		conn.forward( msg.toString()  )
		//Acquire the answer	
		val answer = conn.receiveMsg()
		CommUtils.outmagenta("connQakTcp | answer= $answer")
		return answer
	}
	
	override fun emit( msg: IApplMessage ){
 		conn.forward( msg.toString()  )
	}	
}