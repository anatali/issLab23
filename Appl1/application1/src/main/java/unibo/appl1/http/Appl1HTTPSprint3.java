package unibo.appl1.http;

import unibo.appl1.common.IAppl1Core;
import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.console.gui.CmdConsoleRemote;

/*
Applicazione deployable con Docker
 */
public class Appl1HTTPSprint3 {
    private Appl1Core appl1Core;
    private ServerFactory server;
    private Appl1MsgHandler applMsgHandler;

    public Appl1HTTPSprint3() { }


    public void configureTheSystem() throws Exception {
        CommUtils.aboutThreads("Appl1HTTPSprint3 Before start - ");

        //Create the application
        IAppl1Core applCore = new Appl1Core();
        Appl1 appl          = new Appl1(applCore);

        //Create the console
        //CmdConsoleRemote console =
        //        new CmdConsoleRemote("appl1Console", ProtocolType.tcp, "localhost", "8030");

        //Activate
        appl.start();
        //console.activate();

        //CommUtils.delay(200000);
        CommUtils.aboutThreads("After start - ");
    }
    public void doJob() throws Exception{
        //activateTheServer();
        configureTheSystem();  //done by appl1Core.start, so to be reusable
        CommUtils.outmagenta("Please activate Appl1HttpSprint3CmdConsole ");
    }
    public static void main( String[] args ) throws Exception {
        CommUtils.aboutThreads("At start - ");
        Appl1HTTPSprint3 appl = new Appl1HTTPSprint3();
        appl.configureTheSystem();
        CommUtils.aboutThreads("At end - ");
    }

}
