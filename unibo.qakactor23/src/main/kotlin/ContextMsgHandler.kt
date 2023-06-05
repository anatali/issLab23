package it.unibo.kactor

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.msg.ApplMsgHandler
import unibo.basicomm23.interfaces.IApplMsgHandler
import unibo.basicomm23.interfaces.Interaction
import unibo.basicomm23.msg.ApplMessage
import unibo.basicomm23.utils.ColorsOut
import unibo.basicomm23.utils.CommUtils


class ContextMsgHandler(name: String, val ctx: QakContext) :
    ApplMsgHandler(name), IApplMsgHandler {

    override fun elaborate(msg: IApplMessage, conn: Interaction) {
        //CommUtils.outblue(name + " | elaborate $msg conn= $conn" );
        if( msg.isRequest() ) elabRequest(msg,conn);
        else if( msg.isEvent()) elabEvent(msg,conn);
            else elabNonRequest(msg,conn);
    }

    protected fun elabRequest(msg: IApplMessage, conn: Interaction) {
        //CommUtils.outblue(name + " | ${ctx.name} elabRequest  $msg conn= $conn" );
        //Inserisco conn nel messaggio di richiesta
        val requestMsg = ApplMessage(msg.msgId(),msg.msgType(),
            msg.msgSender(), msg.msgReceiver(), msg.msgContent(), msg.msgNum(), conn);

        elabNonRequest(requestMsg, conn)
    }

    protected fun elabNonRequest(msg: IApplMessage, conn: Interaction?) {
        for( i in 1..10) {
            val a = QakContext.getActor(msg.msgReceiver())
            if (a == null){
                CommUtils.outblue(name + " | not found destination=${msg.msgReceiver()} RETRYING $i ..." )
                runBlocking { delay( 500 ) }
            }else{
                runBlocking {
                    MsgUtil.sendMsg(msg, a)
                }
                return
            }
        }
        ColorsOut.outerr(name + " | not found destination actor:" + msg.msgReceiver())
    }

    protected fun elabEvent(event: IApplMessage, conn: Interaction?) {
        sysUtil.traceprintln(name + " | ContextMsgHandler elabEvent $event conn= $conn" );
        runBlocking {
            ctx.actorMap.forEach {
                sysUtil.traceprintln(name + " |  ContextMsgHandler $name | in ${ctx.name} propag $event to ${it.key} in ${it.value}")
                val a = it.value
                try {
                    a.actor.send(event)
                } catch (e1: Exception) {
                    CommUtils.outred(name + " |  ContextMsgHandler propagateEvent WARNING: ${e1.message}")
                }
            }
        }
    }


}