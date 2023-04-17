package unibo.actors23.example0;

import unibo.actors23.ActorBasic23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Actor2 extends ActorBasic23 {

    public Actor2(String name, unibo.actors23.ActorContext23 ctx) {
        super(name,ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outgreen(name + " | elab " + msg + " in:" + Thread.currentThread().getName());
        if( msg.isRequest() ) {
            IApplMessage answer = CommUtils.buildReply(
                    name,"answer", "ok" + msg.msgContent(), msg.msgSender());
            if( name.equals("a2")){
                CommUtils.delay(1000);  //a2 ritarda a rispondere
            }
            CommUtils.outgreen(name + " | sendRepy " + answer + " " + Thread.currentThread().getName());
            //sendMsg(answer);  //invio answer come un msg a dest (remoto)
            reply( answer,msg ); //invio answer sulla connessione
        }
    }
}
