package unibo.actors23;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class ActorBasic23 implements IActor23 {
    protected BlockingQueue<IApplMessage> msgQueue =
            new LinkedBlockingDeque<IApplMessage>();
    protected String name       = "dummy";
    protected boolean autostart = false;
    protected ActorContext23 ctx;
    protected IApplMessage autoStartMsg ;
    protected  HashMap<String, IApplMessage> requestMap = new java.util.HashMap<String, IApplMessage>();

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
                //String msg = msgQueue.take();
                IApplMessage inputMsg = msgQueue.take();
                //elabRequest di ContextMsgHandler ha fatto setConn

                if( inputMsg.isRequest() ){
                    //CommUtils.outred(name + "| elabMsg " + ((ApplMessage)inputMsg).getConn());
                    requestMap.put(inputMsg.msgId(), inputMsg);
                }
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

    protected void elabMsg( String msg ) throws Exception{
        IApplMessage m = new ApplMessage(msg);
        elabMsg(m);
    }

    
/*
    
*/
//protected void sendMsg( IApplMessage msg  )   { ActorSupport23.sendMsg(msg,ctx); }

    protected  void sendMsg( IApplMessage msg   )   {
    try {
        String destActorName = msg.msgReceiver();
        ActorBasic23 dest    = ctx.getActor(destActorName);
        //CommUtils.outyellow(  name + " | sendMsg to " + dest.name );
        if (dest != null) {
            //CommUtils.outyellow( name + " | sendMsg to " + dest.name);
            dest.msgQueue.put( msg ); //attore locale
        } else {
            sendMsgToRemoteActor( msg );  //attore non locale
        }
    }catch(Exception e){
        CommUtils.outred(  " | sendMsg ERROR  " + e.getMessage() );
    }
}

    protected  void sendMsgToRemoteActor( IApplMessage msg  ) {
        String destActorName = msg.msgReceiver();
        if(Connection.trace) CommUtils.outyellow( name + " | sendMsg " + msg + " to REMOTE " + destActorName );
        //Chiedo al contesto un proxy per l'attore
        Proxy pxy    = ctx.getProxy(destActorName);
        if( pxy == null ) {
            CommUtils.outred(name + " | Perhaps no setActorAsRemote for " + destActorName );
            return;
        }
        pxy.sendMsgOnConnection(msg.toString());
    }


    protected void reply(IApplMessage answer, IApplMessage request){
        String reqId  = request.msgId();
        String caller = request.msgSender();
        IApplMessage reqMsg = requestMap.remove(reqId); //REMOVE: one request, one reply
        if( reqMsg == null ) {
            CommUtils.outred(name + "| answer NO request found");
            return;
        }
        //CommUtils.outred(name + "| elabMsg request removed " + reqMsg.getConn());
        ActorBasic23 destactor = ctx.getActor(caller);
        if( destactor != null ){ //Attore caller locale
            try {
                destactor.msgQueue.put( answer );
            } catch (Exception e) {
                CommUtils.outred(name + "| answer ERROR " + e.getMessage() );
            }
        }
        else sendMessageOnConn( answer, reqMsg.getConn() );
    }

    protected void sendMessageOnConn(IApplMessage msg, Interaction2021 conn){
        try {
            if( Actor23Utils.trace) CommUtils.outmagenta(name + "| sendMessageOnConn " +  conn );
            if( conn != null ) conn.forward( msg.toString() );
            else CommUtils.outred(name + "| sendMessageOnConn conn is NULL " );
        } catch (Exception e) {
            CommUtils.outred(name + "| sendMessageOnConn ERROR " + e.getMessage()   );
        }
    }

    protected abstract void elabMsg( IApplMessage msg ) throws Exception;
}
