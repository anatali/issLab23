package rx
 

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils


class dataCleaner (name : String ) : ActorBasic( name ) {
val LimitLow  = 2	
val LimitHigh = 150
//@kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun actorBody(msg: IApplMessage) {
        if( msg.msgId() != "sonardistance") return
	    if( msg.msgSender() == name) return //AVOID to handle the event emitted by itself
  		elabData( msg )
 	}

//@kotlinx.coroutines.ObsoleteCoroutinesApi
	  suspend fun elabData( msg: IApplMessage){ //OPTIMISTIC
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		CommUtils.outyellow("$tt $name |  data = $data ")
		val Distance = Integer.parseInt( data ) 
 		if( Distance > LimitLow && Distance < LimitHigh ){
			emitLocalStreamEvent( msg ) //propagate
  	 	    val m0 = MsgUtil.buildEvent(name, "sonardata", "distance($data)")
		    CommUtils.outgreen("$tt $name |  emits = $m0 ")
		    emit( m0 )    	
		  }else{
			CommUtils.outmagenta("$tt $name |  DISCARDS $Distance ")
 		}				
 	}
}