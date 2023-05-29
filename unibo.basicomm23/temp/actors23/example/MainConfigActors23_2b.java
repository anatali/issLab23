package unibo.basicomm23.actors23.example;

import unibo.basicomm23.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainConfigActors23_2b {

    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023
        CommUtils.outblue("Working Directory = " + userDir);

        Actor23Utils.createContexts("localhost",
                "src/unibo/basicomm23/actors23/example/exampleActor23_2b.pl",
                "src/unibo/basicomm23/actors23/example/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainConfigActors23_2b().configureTheSystem();
    }
}
