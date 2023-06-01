package rx

 

import alice.tuprolog.Struct
import alice.tuprolog.Term
import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import it.unibo.kactor.ActorBasic
import unibo.basicomm23.interfaces.IApplMessage
import java.io.File

class dataLogger(name : String) : ActorBasic(name){
	var pw : PrintWriter
	
 	init{
		pw = PrintWriter( FileWriter(name+".txt") )
 	}
    
//@kotlinx.coroutines.ObsoleteCoroutinesApi

	override suspend fun actorBody(msg: IApplMessage) {
 	    if( msg.msgSender() == name) return //AVOID to handle the event emitted by itself
  		elabData( msg )
		emitLocalStreamEvent(msg)	//propagate ... 
	}
 
 	protected suspend fun elabData( msg: IApplMessage){
		if( msg.msgId() == "sonardata" ) return; //avoid ...
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
		println("	-------------------------------------------- $name msg=$msg")
   		pw.append( "$data\n " )
		pw.flush()
     }

}