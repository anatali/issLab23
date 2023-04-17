package unibo.actors23.example0.threectx;

import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainConfigActors23_3 {

    public void configureTheSystem(){
        //System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "actors23/src/main/java/unibo/actors23/example0/threectx/exampleActor23_3.pl",
                "actors23/src/main/java/unibo/actors23/example0/sysRules.pl");
        //Qui il main perde il controllo ...
    }
    public static void main(String[] args ){
        new MainConfigActors23_3().configureTheSystem();
    }
}

