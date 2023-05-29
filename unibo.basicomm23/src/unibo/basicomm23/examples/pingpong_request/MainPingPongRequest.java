package unibo.basicomm23.examples.pingpong_request;

import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.msg.ProtocolType;

public class MainPingPongRequest {
    public void configureTheSystem() {
        //PLAYERS
        Player ping = new Player("ping");
        Player pong = new Player("pong");

        //Activate
        pong.startReceiver();  //Lavora come PlayerReceiverMsgHandler
        ping.startBatsman();   //Lavora come BatsmanCaller

        //ping.startReceiver();
        //pong.startBatsman();

    }

    public void configureTheSystemOld() {
        String hostAddr = "localhost";
        int port        = 9999;
        ProtocolType protocol = ProtocolType.udp;

        //PLAYERS
        BatsmanCaller ping = new BatsmanCaller("ping", protocol, hostAddr, ""+port);
        BatsmanCaller pong = new BatsmanCaller("pong", protocol, hostAddr, ""+port);

        //RECEIVERS
        PlayerReceiverMsgHandler playerRec = new PlayerReceiverMsgHandler("ppmh");
        ServerFactory server       = new ServerFactory("server", port, protocol);
        server.addMsgHandler(playerRec);

        //Activate
        server.start();
        //ping.activate();  //MASTER ping
        pong.activate();  //MASTER pong
    }

    public static void main( String[] args ){
        new MainPingPongRequest().configureTheSystem();
    }

}
