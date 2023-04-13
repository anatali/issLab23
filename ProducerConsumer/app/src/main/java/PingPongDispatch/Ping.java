package PingPongDispatch;
import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class Ping extends ActorBasic23 {
protected int n = 0;

    public Ping(String name, ActorContext23 ctx) {
        super(name, ctx);
        this.autostart = true;
    }




    @Override
    protected void elabMsg(IApplMessage msg) throws Exception {
        //CommUtils.outblue(name + " | elabMsg " + msg + " in:" + Thread.currentThread().getName());

        IApplMessage hitMsg = CommUtils.buildDispatch(name, "hit", "" + n, "pong");
        if( msg.msgId().equals("cmd") && msg.msgContent().equals("start")){
            CommUtils.outblue(name + " | SENDS " + hitMsg + " in:" + Thread.currentThread().getName());
            sendMsg(hitMsg);
        }
        if( msg.msgId().equals("hit")){  //dispatch from Pong
            n++;
            if( n <= 3 ) {
                hitMsg = CommUtils.buildDispatch(name, "hit", "hit"+n+name, "pong");
                CommUtils.outblue(name + " | SENDS " + hitMsg + " in:" + Thread.currentThread().getName());
                sendMsg(hitMsg);
            }
        }
     }
}
