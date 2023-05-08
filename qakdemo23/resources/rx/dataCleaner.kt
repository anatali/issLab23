package rx
 

import it.unibo.kactor.*
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct
import unibo.basicomm23.interfaces.IApplMessage
 

 
class dataCleaner (name : String ) : ActorBasic( name ) {
val LimitLow  = 2	
val LimitHigh = 150
 
    override suspend fun actorBody(msg: IApplMessage) {
  		elabData( msg )
 	}

 	
 
	  suspend fun elabData( msg: IApplMessage ){ //		 
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		//println("   $name |  data = $data ")		
		val Distance = Integer.parseInt( data ) 
 		if( Distance > LimitLow && Distance < LimitHigh ){
			emitLocalStreamEvent( msg ) //propagate
     	}else{
			//println("   $name |  DISCARDS $Distance ")
 		}				
 	}
}