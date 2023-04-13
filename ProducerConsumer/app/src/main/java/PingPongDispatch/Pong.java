package PingPongDispatch;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Pong extends ActorBasic23 {


    public Pong(String name, ActorContext23 ctx) {
        super(name, ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        //CommUtils.outgreen(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());
        if( msg.msgId().equals("hit")){  //dispatch from Ping
            IApplMessage hitMsg = CommUtils.buildDispatch(name, "hit", "hitFrom"+name+msg.msgContent(), msg.msgSender());
                CommUtils.outgreen(name + " | SENDS " + hitMsg + " in:" + Thread.currentThread().getName());
                sendMsg(hitMsg);
        }
    }
}
