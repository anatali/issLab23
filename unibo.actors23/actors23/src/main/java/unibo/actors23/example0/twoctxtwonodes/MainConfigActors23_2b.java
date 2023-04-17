package unibo.actors23.example0.twoctxtwonodes;


import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainConfigActors23_2b {

    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023
        CommUtils.outblue("Working Directory = " + userDir);

        //Actor23Utils.trace = true;  //USEFUL TO SEE the interaction for reply
        //Connection.trace   = true;

        Actor23Utils.createContexts("localhost",
                "actors23/src/main/java/unibo/actors23/example0/twoctxtwonodes/exampleActor23_2b.pl",
                "actors23/src/main/java/unibo/actors23/example0/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainConfigActors23_2b().configureTheSystem();
    }
}
