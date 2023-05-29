package unibo.basicomm23.examples.pingpong_dispatch;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

public class PlayerReceiverMsgHandler extends ApplMsgHandler {
    protected PlayerLogic plogic = new PlayerLogic();
    protected PlayerCaller pcaller;
    protected boolean connectedToBatsman = false;
    protected int n = 0;

    public PlayerReceiverMsgHandler(String name) {
        super(name);
        pcaller = new PlayerCaller(
                name+"_caller", ProtocolType.tcp, "localhost", "9797" );
    }

    @Override
    public void elaborate(IApplMessage message, Interaction conn) {
        try{
            CommUtils.outmagenta(name + " elaborate:" + message);
            n++;
            if( message.isRequest() ) {
                String hit = message.msgContent();
                String m = "'" + plogic.hitBallAsAnswer(hit) + "'";
                //CommUtils.outgreen(m);
                IApplMessage reply = CommUtils.buildReply(
                        message.msgReceiver(), "respondehit", m, message.msgSender());
                conn.reply(reply);
            }else{ //Dispatch
                if( ! connectedToBatsman ){
                    pcaller.activate();
                    connectedToBatsman = true;
                }
                String hit = message.msgContent();
                String m;
                if( n == 3 ){
                    m = "'" + plogic.hitBallWrongAsAnswer(hit) + "'";
                }else {
                    m = "'" + plogic.hitBallAsAnswer(hit) + "'";
                }
                IApplMessage hitanswer = CommUtils.buildDispatch(
                        message.msgReceiver(),"hitanswer", m, message.msgSender());
                CommUtils.outmagenta(name + " elaborate hitanswer:" + hitanswer);
                pcaller.sendMsg(hitanswer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
