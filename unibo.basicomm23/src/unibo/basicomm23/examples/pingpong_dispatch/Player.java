package unibo.basicomm23.examples.pingpong_dispatch;

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
    }

    public void startBatsman(){
        playerCaller = new BatsmanCaller(name, protocol, "localhost", ""+port);
        playerCaller.activate();
    }
    public void startReceiver(){
        IApplMsgHandler playerMsgHandler;
        playerMsgHandler = new PlayerReceiverMsgHandler(name+"ReceiverHandler");
        server = new ServerFactory("server", port, protocol);
        server.addMsgHandler(playerMsgHandler);
        server.start();
    }

}
