package it.unibo.kactor

import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.interfaces.IApplMsgHandler
import unibo.basicomm23.interfaces.Interaction
import unibo.basicomm23.utils.ColorsOut

class ActorForReply( name:  String,
                     val h: IApplMsgHandler, val conn: Interaction ) : ActorBasic( name ) {

    override suspend fun actorBody(msg: IApplMessage) {
        /*
        ColorsOut.outappl(name + " | actorBody $msg conn= $conn", ColorsOut.MAGENTA);
        if( msg.isReply() ) h.sendAnswerToClient(msg.toString(), conn);
        context!!.removeInternalActor(this);

         */
    }

}