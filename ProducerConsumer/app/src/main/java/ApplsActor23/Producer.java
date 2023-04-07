package ApplsActor23;

import unibo.basicomm23.actors23.ActorBasic23;
import unibo.basicomm23.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Producer extends ActorBasic23 {
    public Producer(String name, ActorContext23 ctx) {
        super(name, ctx);
        autostart = true;
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outblue(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());
        if( msg.msgId().equals("cmd") && msg.msgContent().equals("start")){
            IApplMessage infoMsg  = CommUtils.buildDispatch(name, "info", "hello", "consumer");
            CommUtils.outblue(name + " | SENDS " + infoMsg + " in:" + Thread.currentThread().getName());
            sendMsg(infoMsg);
        }
    }
}
