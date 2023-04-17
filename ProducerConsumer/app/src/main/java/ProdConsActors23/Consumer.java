package ProdConsActors23;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Consumer extends ActorBasic23 {
    private ConsumerLogic consLogic = new ConsumerLogic();

    public Consumer(String name, ActorContext23 ctx) {
        super(name, ctx);
    }

    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        CommUtils.outgreen(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());
        if(msg.isRequest()){
            String answer = consLogic.evalDistance( msg.msgContent() );
            IApplMessage replyMsg = CommUtils.buildReply(
                    "consumer", "outdata", answer, msg.msgSender());
            CommUtils.outgreen(name + " | reply " + replyMsg + " in:" + Thread.currentThread().getName());
            reply( replyMsg, msg );
        }  else CommUtils.outred(name + " | elaborate ERROR: not a request");
    }
}
