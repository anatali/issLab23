package unibo.basicomm23.actors23;

import unibo.basicomm23.interfaces.IActor23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class ActorBasic23 implements IActor23 {
    protected BlockingQueue<String> msgQueue = new LinkedBlockingDeque<>();
    protected String name="dummy";
    protected boolean autostart = false;
    protected ActorContext23 ctx;
    protected IApplMessage autoStartMsg ;

    public ActorBasic23(String name, ActorContext23 ctx ){
        this.name    = name;
        this.ctx     = ctx;
        autoStartMsg = CommUtils.buildDispatch(name, "cmd", "start", name);
    }

    public String getName(){
        return name;
    }

    public String getContextName(){
        return ctx.name;
    }

    protected void mainLoop(){
        try {
            CommUtils.outmagenta(name + "| mainLoop STARTS:" + " thname=" + Thread.currentThread().getName() );
            while( true ) {
                String msg = msgQueue.take();
                IApplMessage inputMsg = new ApplMessage(msg);
                elabMsg(inputMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activate(){
        new Thread(){
            public void run(){
                mainLoop();
            }
        }.start();
    }
    public void activateAndStart(){
        new Thread(){
            public void run(){
                sendMsg( autoStartMsg );
                mainLoop();
            }
        }.start();
    }
    protected void sendMsg( IApplMessage msg  )   {
        ActorSupport23.sendMsg(msg,ctx);
    }
        /*
        try {
            String destActorName = msg.msgReceiver();
            ActorBasic23 dest    = ctx.getActor(destActorName);
            //CommUtils.outyellow(name + " | sendMsg to " + dest.name );
            if (dest != null) {
                //CommUtils.outyellow(name + " | sendMsg to " + dest.name);
                dest.msgQueue.put(msg.toString()); //attore locale
            } else {
                sendMsgToRemoteActor(msg);  //attore non locale
            }
        }catch(Exception e){
            CommUtils.outred( name + " | sendMsg ERROR  " + e.getMessage() );
        }
    }

    protected void sendMsgToRemoteActor( IApplMessage msg ) {
        String destActorName = msg.msgReceiver();
        //CommUtils.outyellow(name + " | sendMsg to REMOTE " + destActorName );
        //Chiedo al contesto un proxy per l'attore
        ProxyAsClient pxy    = ctx.getProxy(destActorName);
        if( pxy == null ) {
            CommUtils.outred("Perhaps no setActorAsRemote for " + destActorName );
            return;
        }
        pxy.sendMsgOnConnection(msg.toString());
    }*/

    protected void elabMsg( String msg ) throws Exception{
        IApplMessage m = new ApplMessage(msg);
        elabMsg(m);
    }
    protected abstract void elabMsg( IApplMessage msg ) throws Exception;
}
