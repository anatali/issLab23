package unibo.basicomm23.examples.pingpong_dispatch;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;

/*

 */
public class BatsmanMsgHandler extends ApplMsgHandler {
    protected PlayerLogic plogic = new PlayerLogic();
    protected BatsmanCaller batsman;

    public BatsmanMsgHandler(String name, BatsmanCaller batsman) {
        super(name);
        this.batsman = batsman;
    }

    @Override
    public void elaborate(IApplMessage message, Interaction conn) {
        try{
            CommUtils.outmagenta(name + " elaborate:" + message  ) ;
            if( message.isRequest() ) { //WRONG
                String hit = message.msgContent();
                String m = "'" + plogic.hitBallAsAnswer(hit) + "'";
                //CommUtils.outgreen(m);
                IApplMessage reply = CommUtils.buildReply(
                        message.msgReceiver(), "respondehit", m, message.msgSender());
                conn.reply(reply);
            }else{ //Dispatch
                String hit = message.msgContent();
                String m = "'" + plogic.hitBallAsAnswer(hit) + "'";
                //CommUtils.outblue("BatsmanMsgHandler |  mmmmmmmmmmmm=" + m);
                //Deve sbloccare receiveMsg di BastsmanCaller
                if( ! hit.contains("wrong"))
                    batsman.playUsingDispatch( message.msgSender() );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
