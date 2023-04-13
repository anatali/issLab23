package ApplsActor23;


import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class MainProdCons1 {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab23\ProducerConsumer
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/ApplsActor23/ProdConsActor23_1.pl",
                "app/src/main/java/ApplsActor23/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdCons1().configureTheSystem();
    }
}
