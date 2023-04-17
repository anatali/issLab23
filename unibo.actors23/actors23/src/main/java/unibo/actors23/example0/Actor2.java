package unibo.actors23.example0;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Actor2 extends ActorBasic23 {

    public Actor2(String name, ActorContext23 ctx) {
        super(name,ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        //CommUtils.outgrayn(name + " | elab " + msg + " in:" + Thread.currentThread().getName());
        if( msg.isRequest() ) {
            IApplMessage replyMsg = CommUtils.buildReply(
                    name,"answer", "ok" + msg.msgContent(), msg.msgSender());
            if( name.equals("a2")){
                CommUtils.delay(1000);  //a2 ritarda a rispondere
            }
            CommUtils.outgreen(name + " | sendRepy " + replyMsg + " " + Thread.currentThread().getName());
            reply(replyMsg, msg);  //invio la reply come un msg a dest (remoto)
        }
    }
}
