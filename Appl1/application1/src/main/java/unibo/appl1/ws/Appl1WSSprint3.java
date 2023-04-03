package unibo.appl1.ws;

import unibo.appl1.common.IAppl1Core;
import unibo.appl1.http.Appl1;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.console.gui.CmdConsoleRemote;

public class Appl1WSSprint3 {
    public void configureTheSystem() throws Exception {
        CommUtils.aboutThreads("Appl1WSSprint3 Before start - ");

        //CommUtils.waitTheUser("Modify sprint3Config.json");

        //Create the application
        //IAppl1Core applCore  = new Appl1Core();
        //IAppl1Core applCore  = new Appl1CoreStepAsynch();
        //IAppl1Core applCore  = new Appl1CoreStepAsynchQueue();
        IAppl1Core applCore  = new Appl1CoreActorlike();
        Appl1 appl           = new Appl1(applCore);

        //((Appl1Core)applCore).addAnObserverOnWsconn();  //MArch31 per osservare msgs su WS

        //Create the console
       // CmdConsoleRemote console =
       //         new CmdConsoleRemote("appl1Console", ProtocolType.tcp, "localhost", "8030");

        //Activate
        appl.start();
        //console.activate();

        //CommUtils.delay(200000);
        CommUtils.aboutThreads("After start - ");
    }
    public void doJob() throws Exception{
        //activateTheServer();
        configureTheSystem();  //done by appl1Core.start, so to be reusable
        CommUtils.outmagenta("Please activate Appl1WSSprint3CmdConsole ");
    }
    public static void main( String[] args ) throws Exception {
        Appl1WSSprint3 appl = new Appl1WSSprint3();
        appl.configureTheSystem();
        CommUtils.aboutThreads("Appl1WSSprint3 Main at end - ");
    }    
}
