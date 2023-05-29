package unibo.basicomm23.actors23.ApplsActor23;

import unibo.basicomm23.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class MainProdCons2 {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab23\ProducerConsumer
        CommUtils.outblue("Working Directory = " + userDir);
        Actor23Utils.trace = true;
        Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "src/unibo/basicomm23/actors23/ApplsActor23/ProdConsActor23_2.pl",
                "src/unibo/basicomm23/actors23/ApplsActor23/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdCons2().configureTheSystem();
    }
}
