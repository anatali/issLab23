package unibo.actors23.example0.pythonconsole;

import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;


public class MainConfigActors23_all {

    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir");
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "actors23/src/main/java/unibo/actors23/example0/pythonconsole/exampleActor23_all.pl",
                "actors23/src/main/java/unibo/actors23/example0/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainConfigActors23_all().configureTheSystem();
    }
}

