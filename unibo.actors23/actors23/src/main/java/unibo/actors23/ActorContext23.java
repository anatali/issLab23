package unibo.actors23;


import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;
import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public  class ActorContext23 implements IContext23 {
    public static final String actorReplyPrefix = "arply_";
    protected HashMap<String, ActorBasic23> ctxActorMap = new HashMap<String, ActorBasic23>();
    protected HashMap<String, Proxy> ctxProxyMap = new HashMap<String, Proxy>();
    protected HashMap<String, Proxy> proxiesMap  = new HashMap<String, Proxy>();

    protected String name="ctxdummy";
    protected ServerFactory server;
    protected CoapServer serverCoap;
    protected String hostName;
    protected int port;

    protected CoapResourceCtx root = new CoapResourceCtx("actors");

    public ActorContext23( String name, String hostName, int port ){
        this.name     = name;
        this.hostName = hostName;
        this.port = port;
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        IApplMsgHandler ctxMsgHandler = new ContextMsgHandler(name+"CtxMsgHandler", this);

        server = new ServerFactory("appl1Server",port, ProtocolType.tcp, ctxMsgHandler);
        server.start();

        serverCoap = new CoapServer(port);
        serverCoap.add(root);
        serverCoap.start();
    }

    @Override
    public ActorBasic23 getActor(String actorName) {
        return ctxActorMap.get(actorName);
    }
    @Override
    public String getName(){
        return name;
    }
    //public HashMap<String, IApplMessage> getRequestMap(){ return requestMap;};

    public void activateLocalActors(){
        Vector<String> actors = getLocalActorNames();
        Iterator<String> iter = actors.iterator();
        while (iter.hasNext()) {
            ActorBasic23 a =  getActor(iter.next());
            if( a.autostart ) a.activateAndStart(); else a.activate();
        }
    }

    @Override
    public   void addActor( ActorBasic23 a ) {
        if(Actor23Utils.trace) CommUtils.outyellow(name + " | added CoapResource " + a.name );
        root.addActorResource(a);
        ctxActorMap.put(a.getName(), a );
        //La attivazione degli attori deve essere fatta quando sono stati creati tutti
        //if( a.autostart ) a.activateAndStart(); else a.activate();
    }
    @Override
    public  void removeActor(ActorBasic23 a) {
        ctxActorMap.remove( a.getName() );
    }
    @Override
    public Proxy getProxy(String actorName){
        return proxiesMap.get(actorName);
    }
    @Override
    public void setActorAsRemote(
            String actorName, String entry, String host, ProtocolType protocol ) {
        String PxyName = "Pxy_"+host+"_"+entry;
        if(Actor23Utils.trace) CommUtils.outgray(name + " setActorAsRemote:" + actorName + " on " + PxyName);
        Proxy pxy = ctxProxyMap.get(PxyName);
        if( pxy == null ) { //un solo proxy per contesto remoto
            pxy = new Proxy(  PxyName, host, entry, protocol,this);
            ctxProxyMap.put(PxyName, pxy);
        }
        proxiesMap.put(actorName, pxy); //memo il proxy per l'attore
    }
    @Override
    public Vector<String> getLocalActorNames( ) {
        Vector<String> actorList = new Vector<String>();
        ctxActorMap.forEach( (name, actor) -> actorList.add(name) );
        return actorList;
    }

    @Override
    public  void showActorNames( ) {
        CommUtils.outyellow("CURRENT ACTORS in context:" + name );
        ctxActorMap.forEach( (v, x) ->
                CommUtils.outyellow(name + ":" + v + " in " + x.getContextName() )
        );
    }
    @Override
    public  void showProxies( ) {
        CommUtils.outyellow("CURRENT PROXY in context:" + name );
        proxiesMap.forEach( (v, x) ->
                CommUtils.outmagenta(name + ":" + v + " in " + x )
        );
    }

    @Override
    public void propagateEventToActors(IApplMessage event){
        //propaga a tutti gli attori locali tranne se stesso
        ctxActorMap.forEach( (actorName, actor) -> {
            try {
                if( ! actor.getName().equals(event.msgSender())) {
                    if(Actor23Utils.trace) CommUtils.outyellow(name + "propagateEvent " + " to " + actorName);
                    actor.msgQueue.put(event);
                }
            } catch (InterruptedException e) {
                CommUtils.outred(name + " propagateEvent ERROR " + e.getMessage());
            }
        });
    }
    public void propagateEventToProxies(IApplMessage event){
         proxiesMap.forEach( (v, proxy) -> {
             if(Actor23Utils.trace) CommUtils.outyellow("propagateEvent" + event + " to " + v);
            try {
                proxy.getConn().forward(event);
            } catch (Exception e) {
                CommUtils.outred(name + " propagateEvent ERROR " + e.getMessage());
            }
        });

    }




}
