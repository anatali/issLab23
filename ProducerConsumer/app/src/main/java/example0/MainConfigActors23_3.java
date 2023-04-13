package example0;

import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainConfigActors23_3 {

    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023
        CommUtils.outblue("Working Directory = " + userDir);
        Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/example0/exampleActor23_3.pl",
                "app/src/main/java/example0/sysRules.pl");
        //Qui il main perde il controllo ...
    }
    public static void main(String[] args ){
        new MainConfigActors23_3().configureTheSystem();
    }
}
