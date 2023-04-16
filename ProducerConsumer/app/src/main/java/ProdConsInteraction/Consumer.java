package ProdConsInteraction;

import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;

public class Consumer {
    private ServerFactory server;

    public Consumer(String name){

        ConsumerLogic consunerLogic = new ConsumerLogic();
        IApplMsgHandler chandle;
        if( ProdConsConfig.protocol == ProtocolType.coap )
            chandle  = new ConsumerCoapResource("consumer", consunerLogic );
        else chandle = new ConsumerMsgHandler("consumerhanlder", consunerLogic );
        server = new ServerFactory("server", ProdConsConfig.port, ProdConsConfig.protocol);
        server.addMsgHandler(chandle);
    }

    public void start(){
        server.start();
    }
}
