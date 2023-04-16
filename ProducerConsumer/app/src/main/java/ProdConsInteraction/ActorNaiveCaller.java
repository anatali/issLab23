package ProdConsInteraction;


import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public abstract class ActorNaiveCaller {
    protected String name;
    protected Interaction connSupport;
    protected ProtocolType protocol;
    protected String hostAddr;
    protected String entry;
    protected boolean connected = false;

    public ActorNaiveCaller(String name, ProtocolType protocol, String hostAddr, String entry){
        this.name     = name;
        this.protocol = protocol;
        this.hostAddr = hostAddr;
        this. entry   = entry;
    }


    protected void connect(){
        if( connected ) return;
        connected   = true;
        connSupport = ConnectionFactory.createClientSupport23(protocol, hostAddr, entry);
        //CommUtils.outblue(name + " | connected client=" + connSupport);
    }

    public void activate(){
        new Thread(){
            public void run(){
                try {
                    connect();
                    body();
                  } catch (Exception e) {
                    CommUtils.outred("");
                }
            }
        }.start();
    }

    protected abstract void body() throws Exception;

}
