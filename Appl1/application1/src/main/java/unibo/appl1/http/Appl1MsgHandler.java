package unibo.appl1.http;

import unibo.appl1.common.IAppl1Core;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;

import java.util.Locale;

public class Appl1MsgHandler extends ApplMsgHandler {
    private IAppl1Core appl1Core ;

    public Appl1MsgHandler(String name, IAppl1Core appl1Core) {
        super(name);
        this.appl1Core = appl1Core;
    }



    @Override
    public void elaborate(IApplMessage msg, Interaction conn) {
        CommUtils.outmagenta("Appl1Sprint3MsgHandler msg="
                + msg + " " + msg.msgType() + " " + Thread.currentThread().getName() );
        if( msg.isRequest() ){
            //Considera l'applicazione come POJO che fornisce properties
            if( msg.msgId().equals("isrunning")){
                boolean answer = appl1Core.isRunning();
                //Creo e invio reply
                IApplMessage reply = CommUtils.buildReply(name.toLowerCase(Locale.ROOT), "isrunninganswer", ""+answer, msg.msgSender());
                try {
                    conn.reply(reply.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if( msg.msgId().equals("getpath")){
                String curPath = CommUtils.convertToSend( appl1Core.getCurrentPath() );
                //Creo e invio reply
                IApplMessage reply = CommUtils.buildReply(name.toLowerCase(Locale.ROOT), "getpathanswer",  curPath , msg.msgSender());
                try {
                    conn.reply(reply.toString() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        if( msg.msgContent().contains("start")){
            if( appl1Core.getClass().getName().equals("unibo.appl1.ws.Appl1CoreActorlike")){
                //Inserito dopo SPRINT3: cosidera Appl come actor
                try {
                    appl1Core.start(); //start fa partire il mainloop Thread
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
            new Thread(){
                public void run() {  //Per permettere all'handler di operare essendo Appl POJO
                    try {
                         appl1Core.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        else if( msg.msgContent().contains("stop")){
            appl1Core.stop();
        }
        else if( msg.msgContent().contains("resume")){
            appl1Core.resume();
        }

      }
}
