package unibo.appl1.http;

import unibo.basicomm23.utils.CommUtils;
import unibo.console.gui.Appl1HttpSprint2CmdConsole;
//import unibo.console.CmdConsoleSimulator;

public class Appl1HTTPSprint2 {
    private Appl1CoreSprint2 appl1Core;
    //private CmdConsoleSimulator cmdConsole;
    //private Appl1ObserverForpath obsForpath;

    public Appl1HTTPSprint2() throws Exception{
        configureTheSystem();
    }

    private void configureTheSystem() throws Exception{
        appl1Core  = new Appl1CoreSprint2();
        //cmdConsole = new CmdConsoleSimulator(appl1Core);
        //obsForpath = new Appl1ObserverForpath();
        //appl1Core.addObserver(  obsForpath );
    }

    public void doJob() throws Exception{
        CommUtils.outmagenta("Activate Appl1HttpSprint3CmdConsole ");
        new Appl1HttpSprint2CmdConsole();
        //appl1Core.start();           //PROATTIVA: attiva walkAtBoundary con stop/resume
        //CommUtils.outblue( "Appl1HTTPSprint2 | PATH="+appl1Core.getPath() ); //bloccante
        //CommUtils.outblue( "Appl1HTTPSprint2 | result="+appl1Core.evalBoundaryDone());
    }

    public static void main( String[] args ) throws Exception {
        CommUtils.aboutThreads("Before start - ");
        Appl1HTTPSprint2 appl = new Appl1HTTPSprint2();
        appl.doJob();
        CommUtils.aboutThreads("At end - ");
    }
}
