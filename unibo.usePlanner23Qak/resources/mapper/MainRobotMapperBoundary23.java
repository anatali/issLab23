package mapper;

import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainRobotMapperBoundary23 {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab2023\Appl1
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        //Appl1StateObject.setConfigFilePath("app/robotConfig.json");
        Actor23Utils.createContexts("localhost",
                "resources/mapper/mapperBoundary23.pl",
                "./sysRules.pl");
    }
    public static void main( String[] args) throws Exception {
        CommUtils.aboutThreads("Before start - ");
        new MainRobotMapperBoundary23( ).configureTheSystem();

    }
}
