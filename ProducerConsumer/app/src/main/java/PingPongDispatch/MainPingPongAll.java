package PingPongDispatch;

import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainPingPongAll {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab23\ProducerConsumer
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/PingPongDispatch/PingPongActor23_all.pl",
                "app/src/main/java/PingPongDispatch/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainPingPongAll().configureTheSystem();
    }
}
