package ProdConsEvents;


import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
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
        if( msg.msgId().equals("startcmd") && msg.msgContent().equals("start")){
            IApplMessage infoEvent  = CommUtils.buildEvent(name, "info", "hello" );
            CommUtils.outblue(name + " | SENDS " + infoEvent + " in:" + Thread.currentThread().getName());
            emit(infoEvent);
        }
    }
}
