package unibo.basicomm23.examples.pingpong_request;

import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;

public class Player {

    private String name;
    private ServerFactory server;
    private BatsmanCaller playerCaller;
    private int port = 9696;
    private ProtocolType protocol = ProtocolType.tcp;

    public Player(String name){
        this.name = name;
        //PlayerLogic plogic = new PlayerLogic();
    }

    public void startBatsman(){
        playerCaller = new BatsmanCaller(name, protocol, "localhost", ""+port);
        playerCaller.activate();
    }
    public void startReceiver(){
        IApplMsgHandler playerMsgHandler;
        playerMsgHandler = new PlayerReceiverMsgHandler("playerReceiverHandler");
        server = new ServerFactory("server", port, protocol);
        server.addMsgHandler(playerMsgHandler);
        server.start();
    }

}
