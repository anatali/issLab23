package unibo.basicomm23.examples.pingpong_dispatch;

public class MainPingPongDispatch {
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


    public static void main( String[] args ){
        new MainPingPongDispatch().configureTheSystem();
    }

}
