package ProdConsFsm23;


import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainProdConsFsm23All {
    public void configureTheSystem(){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab23\ProducerConsumer
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/ProdConsFsm23/ProdConsActor23_1.pl",
                "app/src/main/java/shared/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdConsFsm23All().configureTheSystem();
    }
}
