package unibo.basicomm23.actors23.ApplsActor23;

import unibo.basicomm23.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class MainProdConsAll {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\\Didattica2023\\issLab2023\\unibo.basicomm23
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "src/unibo/basicomm23/actors23/ApplsActor23/ProdConsActor23_all.pl",
                "src/unibo/basicomm23/actors23/ApplsActor23/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdConsAll().configureTheSystem();
    }
}
