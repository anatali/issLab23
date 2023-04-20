package ProdConsActors23;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Producer extends ActorBasic23 {
    ProducerLogic prodLogic = new ProducerLogic();

    public Producer(String name, ActorContext23 ctx) {
        super(name, ctx);
        autostart = true;
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        //CommUtils.outgray(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());
        if( msg.msgId().equals("startcmd") && msg.msgContent().equals("start")){
            String d = prodLogic.getDistance();
            //CommUtils.delay(3000); //wait before producing ..
            IApplMessage infoMsg  = CommUtils.buildRequest(name, "info", d, "consumer");
            CommUtils.outblue(name + " | SENDS " + infoMsg + " in:" + Thread.currentThread().getName());
            request(infoMsg);
            return;
        }
        if( msg.isReply()){
            CommUtils.outblue(name + " | RECEIVES answer=" + msg.msgContent());
            CommUtils.aboutThreads(  name + "  | AFTER ANSWER ");
        }
    }
}
