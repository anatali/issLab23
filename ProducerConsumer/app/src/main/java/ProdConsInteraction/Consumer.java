package ProdConsInteraction;

import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.IServer;
import unibo.basicomm23.msg.ProtocolType;

public class Consumer {
    private IServer server;

    public Consumer(String name){

        ConsumerLogic consunerLogic = new ConsumerLogic();
        IApplMsgHandler chandle;
        if( ProdConsConfig.protocol == ProtocolType.coap )
            chandle  = new ConsumerCoapResource("consumer", consunerLogic );
        else chandle = new ConsumerMsgHandler("consumerhandler", consunerLogic );
        server = ServerFactory.create("server", ProdConsConfig.port, ProdConsConfig.protocol, chandle);
    }

    public void start(){
        server.start();
    }
}
