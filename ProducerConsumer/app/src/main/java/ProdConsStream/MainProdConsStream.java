package ProdConsStream;
import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainProdConsStream {
    public void configureTheSystem(){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
        String userDir = System.getProperty("user.dir");
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/ProdConsStream/ProdConsStream.pl",
                "app/src/main/java/shared/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdConsStream().configureTheSystem();
    }
}
