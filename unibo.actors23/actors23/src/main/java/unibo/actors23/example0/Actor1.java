package unibo.actors23.example0;

import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class Actor1 extends ActorBasic23 {
    protected int n = 1;
    protected boolean stopped = false;

    public Actor1(String name, ActorContext23 ctx) {
        super(name, ctx);
    }


    @Override
    protected void elabMsg( IApplMessage msg ) throws Exception{
        //CommUtils.outgrayn(name + " | elabMsg " + msg + " in " + Thread.currentThread().getName());
        if( msg.isReply() ){ //a2 è più lento a rispondere
            CommUtils.outcyan(name + " | elabMsg answer: " + " " + msg  + Thread.currentThread().getName());
            CommUtils.delay(800); //Simulate some work ...
            if (!stopped) sendRequestTo(msg.msgSender());
            //CommUtils.aboutThreads(name + " | after the reply ");
            return;
        }
        if( msg.isDispatch() ) {
            if (msg.msgId().equals("cmd") && msg.msgContent().equals("start")) {
                CommUtils.outmagenta(name + " | elabMsg start in " + Thread.currentThread().getName());
                sendRequestTo("a2");
                sendRequestTo("a3");
            } else if (msg.msgId().equals("cmd") && msg.msgContent().equals("stop")) {
                CommUtils.outmagenta(name + " | elabMsg stopped in " + Thread.currentThread().getName());
                stopped = true;
            }
            return;
        }
    }

    protected void sendDispatchTo(String dest){
        IApplMessage m = CommUtils.buildDispatch(name, "info", "hello" + n, "a2");
        n++;
        sendMsg(m);
        CommUtils.delay(800); //Simulate some work ...
        if (!stopped) sendMsg(autoStartMsg);
    }

    protected void sendRequestTo(String dest){
        IApplMessage m = CommUtils.buildRequest(name, "info", "hello" + n, dest);
        n++;
        CommUtils.outblue(name + " | sendRequest " + m + " " + Thread.currentThread().getName());
        sendMsg(m);
    }

}
