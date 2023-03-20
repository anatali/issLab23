package unibo.appl1.http;

import unibo.basicomm23.utils.CommUtils;
import unibo.console.gui.Appl1HttpSprint2CmdConsole;

public class Appl1HTTPSprint2 {
    private Appl1CoreSprint2 appl1Core;

    public Appl1HTTPSprint2() throws Exception{
        configureTheSystem();
    }

    private void configureTheSystem() throws Exception{
        appl1Core  = new Appl1CoreSprint2();
    }

    public void doJob() throws Exception{
        CommUtils.outmagenta("Activate Appl1HttpSprint3CmdConsole ");
        new Appl1HttpSprint2CmdConsole();
    }

    public static void main( String[] args ) throws Exception {
        CommUtils.aboutThreads("Before start - ");
        Appl1HTTPSprint2 appl = new Appl1HTTPSprint2();
        appl.doJob();
        CommUtils.aboutThreads("At end - ");
    }
}
