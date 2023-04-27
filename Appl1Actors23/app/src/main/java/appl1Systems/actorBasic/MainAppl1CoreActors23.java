package appl1Systems.actorBasic;

import appl1Actors23.Appl1StateObject;
import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainAppl1CoreActors23 {

    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023\Appl1
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Appl1StateObject.setConfigFilePath("app/robotConfig.json");
        Actor23Utils.createContexts("localhost",
           "app/src/main/java/appl1Systems/actorBasic/appl1CoreActor23.pl",
            "app/src/main/java/appl1Actors23/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainAppl1CoreActors23().configureTheSystem();
    }
}

