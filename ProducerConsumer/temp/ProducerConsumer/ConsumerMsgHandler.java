package ProdConsInteraction;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;

/*
Gestore dei messaggi rivolti al consumer
 */
public class ConsumerMsgHandler extends ApplMsgHandler {
private unibo.basicomm23.examples.prodcons.ConsumerLogic consunerLogic;

    public ConsumerMsgHandler(String name, unibo.basicomm23.examples.prodcons.ConsumerLogic consunerLogic ) {
        super(name);
        this.consunerLogic = consunerLogic;
    }

    @Override
    public void elaborate(IApplMessage message, Interaction conn) {
        try {
            CommUtils.outgreen(name + " elaborate " + message );
            String d = message.msgContent();
            String m = consunerLogic.evalDistance( d ) ;
            //CommUtils.outgreen(m);
            IApplMessage reply = CommUtils.buildReply("consumer", "outdata", m, message.msgSender());
            conn.reply( reply );
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
}