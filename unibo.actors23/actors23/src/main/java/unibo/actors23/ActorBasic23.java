package unibo.actors23;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public abstract class ActorBasic23 extends CoapResource implements IActor23 {
    //Non observable come POJO ma come  producer di eventi locali
    protected BlockingQueue<IApplMessage> msgQueue =
            new LinkedBlockingDeque<IApplMessage>();
    protected String name       = "dummy";
    protected boolean autostart = false;
    protected ActorContext23 ctx;
    protected IApplMessage autoStartMsg ;
    protected  HashMap<String, IApplMessage> requestMap = new java.util.HashMap<String, IApplMessage>();

    protected Vector<ActorBasic23> subscribers = new Vector<ActorBasic23>();
    protected String actorResourceRep = "unknown";
    protected final String startSysCmdId = "sysstartcmd";

    public ActorBasic23(String name, ActorContext23 ctx ){
        super(name);
        this.name    = this.getName();
        this.ctx     = ctx;
        autoStartMsg = CommUtils.buildDispatch(name, startSysCmdId, "start", name);
        super.setObservable(true);  //DO NOT FORGET
    }

    @Override
    public void handleGET(CoapExchange exchange){
        if( Actor23Utils.trace)  CommUtils.outyellow(name + " | handleGET ");
        exchange.respond(actorResourceRep);
    }
    @Override
    public void handlePOST(CoapExchange exchange) {
        CommUtils.outred( name + " | handlePOST not implemented") ;
    }
    @Override
    public void handlePUT(CoapExchange exchange){
        String arg       = exchange.getRequestText();
        IApplMessage msg = new ApplMessage( arg );
        if( Actor23Utils.trace)  CommUtils.outyellow(name + " | handlePUT " + msg);
        fromPutToMsg( msg, exchange );
    }

    protected void fromPutToMsg( IApplMessage msg, CoapExchange exchange ) {
        if( msg.isDispatch() || msg.isEvent() ) {
            autoMsg( msg );
            exchange.respond( CoAP.ResponseCode.CHANGED );
        }
    }
    @Override
    public void handleDELETE(CoapExchange exchange) {
        if( Actor23Utils.trace)  CommUtils.outyellow( name + " | handleDELETE  ") ;
        delete(); //inherited from CoapResource
     }

    public void updateResourceRep(String val){
        if( Actor23Utils.trace)  CommUtils.outyellow( name + " | updateResourceRep  " + val) ;
        actorResourceRep = val;
        changed();     //DO NOT FORGET!!!
    }

    public String sendCoapRequest(String host,  String path,  String msg)  {
        try {
            Interaction conn = CoapConnection.create(host,path);
            String answer = conn.request(msg);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return "error " + e.getMessage();
        }
    }

    public void autoMsg(IApplMessage msg){
        try {
            msgQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String getContextName(){
        return ctx.name;
    }
    public ActorContext23 getContext(){
        return ctx ;
    }

/*
 --------------------------------------------
 OBSERVABLE
 --------------------------------------------
*/

    public void subscribeLocalActor( String actorName){
        //if( Actor23Utils.trace)
            CommUtils.outblack(name + " | subscribeLocalActor " + actorName);
        ActorBasic23 actor = ctx.getActor(actorName);
        if( actor != null ) actor.subscribers.add(this);
        else CommUtils.outred("subscribeLocalActor ERROR:" + actorName);
    }
    public void subscribe( ActorBasic23 actor){
        actor.subscribers.add(this);
    }
    public void unsubscribe( ActorBasic23 actor){
        actor.subscribers.remove(this);
    }
    protected void emitLocalStreamEvent( IApplMessage event ){
        if( Actor23Utils.trace)  CommUtils.outyellow(name + " | emitLocalStreamEvent " + event
                + " numOfSubscribers=" + subscribers.size() ) ;
        subscribers.forEach( (actor) -> {
            try {
                actor.msgQueue.put(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } );
    }

    protected void mainLoop(){
        try {
            CommUtils.outgray(name + "| mainLoop STARTS:" + " thname=" + Thread.currentThread().getName() );
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
                if(Actor23Utils.trace) CommUtils.outmagenta(name + "| activateAndStart " + autoStartMsg);
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

    protected  void forward( IApplMessage msg   )   {
        if(  msg.isDispatch() ){
            sendMsg(msg);
        } else{
            CommUtils.outred(  " | forward ERROR: msg not a dispatch:" + msg   );
        }
    }
    protected  void request( IApplMessage msg   )   {
        if(  msg.isRequest() ){
            sendMsg(msg);
        } else{
            CommUtils.outred(  " | request ERROR: msg not a request:" + msg  );
        }
    }

    protected  void emit( IApplMessage msg   )   {  //per consentire l'emissione da supporti
        if( msg.isEvent()){
            ctx.propagateEventToProxies(msg);
            ctx.propagateEventToActors(msg); //per gli attori locali
        }else{
            CommUtils.outred(  " | emit ERROR: msg is not an event:  " + msg.msgType() );
        }
    }
    protected  void emitLocal( IApplMessage msg   )   {  //per consentire l'emissione da supporti
        if( msg.isEvent()){
            //ctx.propagateEventToProxies(msg);
                    ctx.propagateEventToActors(msg); //per gli attori locali
        }else{
            CommUtils.outred(  " | emit ERROR: msg is not an event:  " + msg.msgType() );
        }
    }
    protected  void sendMsg( IApplMessage msg   )   {
    try {
        if( msg.isEvent()){
            ctx.propagateEventToProxies(msg);
            ctx.propagateEventToActors(msg);
            return;
        }
        String destActorName = msg.msgReceiver();
        ActorBasic23 dest    = ctx.getActor(destActorName);
        //CommUtils.outyellow(  name + " | sendMsg to " + dest.name );
        if (dest != null) {
            if(Actor23Utils.trace) CommUtils.outyellow( name + " | sendMsg "+ msg + " to " + dest.name);
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
        if(Actor23Utils.trace) CommUtils.outyellow( name + " | sendMsg " + msg + " to REMOTE " + destActorName );
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
