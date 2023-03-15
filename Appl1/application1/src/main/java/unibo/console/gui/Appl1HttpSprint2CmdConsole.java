package unibo.console.gui;

import unibo.appl1.http.Appl1Core;
import unibo.basicomm23.utils.CommUtils;
import java.util.Observable;
import java.util.Observer;

/*
 */
//TODO: Fsre IApplication
public class Appl1HttpSprint2CmdConsole implements  Observer{
private String[] buttonLabels  = new String[] {"start", "stop", "resume", "exit" };
private boolean started        = false;
	private Appl1Core appl;
	public Appl1HttpSprint2CmdConsole( ) {
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
   	}

   	protected void outState(){
		CommUtils.outmagenta("outState "+appl );
		if( appl != null ){
			//appl.stop();
			CommUtils.outred("PATH="+appl.getPath());
			CommUtils.outgreen("RESULT="+appl.evalBoundaryDone());
		}
	}
 	@Override
	public void update( Observable o , Object arg ) {
		try {
			String move = arg.toString();
			CommUtils.outgreen("GUI input move=" + move);
			//if( move.equals("exit")) {outState(); System.exit(1);}
			switch( move ){
				case "start"   : {  if( started ) return;
									appl = new Appl1Core( );
				                    new Thread(){
				                   	public void run() {
										try {
											appl.start();
										} catch (Exception e) {
											CommUtils.outred(
                                            " StartStopGuiLocal | start ERROR:" + e.getMessage() );;
										}
									}
								   }.start();
				                   break;}
				case "stop"    : {appl.stop();break;}
				case "resume"  : {appl.resume();break;}
				case "exit"    : {
				                     if( appl != null ){
 										CommUtils.outred("CURPATH="+appl.getCurrentPath());
					                    if( ! appl.isRunning() ) CommUtils.outgreen("RESULT="+appl.evalBoundaryDone());
				                    }
				                    //CommUtils.waitTheUser("hot to exit");
									System.exit(0);
									break;
								}
			}
		} catch (Exception e) {
			CommUtils.outred(" StartStopGuiLocal | update ERROR:" + e.getMessage() );;
		}	
	}
	
	public static void main( String[] args) {
 		new Appl1HttpSprint2CmdConsole(   );
    }
}

