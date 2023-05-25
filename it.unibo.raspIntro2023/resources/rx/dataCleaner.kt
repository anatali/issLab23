package rx
 

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct
import unibo.basicomm23.interfaces.IApplMessage


class dataCleaner (name : String ) : ActorBasic( name ) {
val LimitLow  = 2	
val LimitHigh = 150
//@kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun actorBody(msg: IApplMessage) {
  		elabData( msg )
 	}

//@kotlinx.coroutines.ObsoleteCoroutinesApi
	  suspend fun elabData( msg: IApplMessage){ //OPTIMISTIC
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		//println("$tt $name |  data = $data ")		
		val Distance = Integer.parseInt( data ) 
 		if( Distance > LimitLow && Distance < LimitHigh ){
			emitLocalStreamEvent( msg ) //propagate
     	}else{
			println("$tt $name |  DISCARDS $Distance ")
 		}				
 	}
}