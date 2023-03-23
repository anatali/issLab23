package unibo.console.gui;

import unibo.appl1.http.Appl1CoreSprint2;
import unibo.basicomm23.utils.CommUtils;
import java.util.Observable;
import java.util.Observer;


public class Appl1HttpSprint2CmdConsole implements  Observer{
private String[] buttonLabels  = new String[] {"start", "stop", "resume", "getpath" };
private boolean started        = false;
	private Appl1CoreSprint2 appl;  //REFERENCE to the core appl

	public Appl1HttpSprint2CmdConsole( ) {
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
   	}


 	@Override
	public void update( Observable o , Object arg ) {
		try {
			String move = arg.toString();
			//CommUtils.outgreen("GUI input move=" + move);
			switch( move ){
				case "start"   : {  if( started ) return;
									appl = new Appl1CoreSprint2( );
									started = true;
				                    new Thread(){  //RUN THE PROACTIVE PART
				                   	public void run() {
										try {
											appl.start();
										} catch (Exception e) {
											CommUtils.outred(
                                            " Appl1HttpSprint2CmdConsole | start ERROR:" + e.getMessage() );;
										}
									}
								   }.start();
				                   break;}
				case "stop"    : { if( appl != null ) appl.stop();break;}
				case "resume"  : { if( appl != null ) appl.resume();break;}
				case "getpath"    : {
				                     if( appl != null ){
					                    if( ! appl.isRunning() ) {
											CommUtils.outblue("FINAL PATH="+appl.getCurrentPath());
					                    	CommUtils.outgreen("RESULT="+appl.evalBoundaryDone());
										}else{
											CommUtils.outblue("CURPATH="+appl.getCurrentPath());
										}
				                    }else{
										 CommUtils.outmagenta("Please start the application");
				                    }
									break;
								}
			}
		} catch (Exception e) {
			CommUtils.outred(" Appl1HttpSprint2CmdConsole | update ERROR:" + e.getMessage() );;
		}	
	}
	
	public static void main( String[] args) {
 		new Appl1HttpSprint2CmdConsole(   );
    }
}

