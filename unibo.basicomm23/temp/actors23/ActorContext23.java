package unibo.basicomm23.actors23;

import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.IContext23;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

import java.util.HashMap;
import java.util.Vector;

public  class ActorContext23 implements IContext23 {
    public static final String actorReplyPrefix = "arply_";
    protected HashMap<String, ActorBasic23> ctxActorMap = new HashMap<String, ActorBasic23>();
    protected HashMap<String, Proxy> ctxProxyMap = new HashMap<String, Proxy>();
    protected HashMap<String, Proxy> proxiesMap  = new HashMap<String, Proxy>();

    protected String name="ctxdummy";
    protected ServerFactory server;
    protected String hostName;
    protected int port;

    public ActorContext23( String name, String hostName, int port ){
        this.name     = name;
        this.hostName = hostName;
        this.port = port;
        IApplMsgHandler ctxMsgHandler = new ContextMsgHandler(name+"CtxMsgHandler", this);
        server = new ServerFactory("appl1Server",port, ProtocolType.tcp, ctxMsgHandler);
        server.start();
    }

    @Override
    public ActorBasic23 getActor(String actorName) {
        return ctxActorMap.get(actorName);
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public   void addActor( ActorBasic23 a ) {
        ctxActorMap.put(a.getName(), a );
        if( a.autostart ) a.activateAndStart();
        else a.activate();
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
        if(Connection.trace) CommUtils.outgray(name + " setActorAsRemote:" + actorName + " on " + host + ":" + entry);
        Proxy pxy = ctxProxyMap.get(PxyName);
        if( pxy == null ) { //un solo proxy per contesto remoto
            pxy = new Proxy(  PxyName, host, entry, protocol);
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
        CommUtils.outmagenta("CURRENT ACTORS in context:" + name );
        ctxActorMap.forEach( (v, x) ->
                CommUtils.outmagenta("" + v + " in " + x.getContextName() )
        );
    }
    @Override
    public  void showProxies( ) {
        CommUtils.outmagenta("CURRENT PROXY in context:" + name );
        proxiesMap.forEach( (v, x) ->
                CommUtils.outmagenta("" + v + " in " + x )
        );
    }


}
