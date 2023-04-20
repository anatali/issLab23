package ProdConsActors23.twoctxmixed;
import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;

public class MainProdCons2b {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir");
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/ProdConsActors23/twoctxmixed/ProdConsActor23_2b.pl",
                "app/src/main/java/shared/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdCons2b().configureTheSystem();
    }
}
