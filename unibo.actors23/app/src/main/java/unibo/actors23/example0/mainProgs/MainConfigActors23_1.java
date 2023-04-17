package unibo.actors23.example0.mainProgs;

import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class MainConfigActors23_1 {

    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/unibo/actors23/example0/exampleActor23_1.pl",
                "app/src/main/java/unibo/actors23/example0/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainConfigActors23_1().configureTheSystem();
    }
}

//C:\\Didattica2023\\issLab2023\\unibo.actors23\\app\\src\\main\\java\\example0\\exampleActor23_1.pl