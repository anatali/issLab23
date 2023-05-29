package unibo.basicomm23.actors23.example;

import unibo.basicomm23.actors23.ActorBasic23;
import unibo.basicomm23.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Actor2 extends ActorBasic23 {

    public Actor2(String name, ActorContext23 ctx) {
        super(name,ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outgreen(name + " | elab " + msg + " in:" + Thread.currentThread().getName());
        if( msg.isRequest() ) {
            IApplMessage reply = CommUtils.buildReply(
                    name,"answer", "ok" + msg.msgContent(), msg.msgSender());
            if( name.equals("a2")){
                CommUtils.delay(1000);  //a2 ritarda a rispondere
            }
            CommUtils.outgreen(name + " | sendRepy " + reply + " " + Thread.currentThread().getName());
            sendMsg(reply);  //invio la reply come un msg a dest (remoto)
        }
    }
}
