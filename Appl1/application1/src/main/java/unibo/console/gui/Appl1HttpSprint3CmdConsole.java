package unibo.console.gui;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import java.util.Observable;
import java.util.Observer;

/*
 */

public class Appl1HttpSprint3CmdConsole implements  Observer{
private String[] buttonLabels  = new String[] {"start", "stop", "resume", "getpath" };
private Interaction conn;
private String myName;
private IApplMessage curMsg;
private IApplMessage applRunningRequest, applGetPathRequest ;

	public Appl1HttpSprint3CmdConsole(String name, ProtocolType protocol, String port) {
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.myName       = name;
		applRunningRequest = CommUtils.buildRequest("gui", "isrunning", "test", myName);
		applGetPathRequest = CommUtils.buildRequest("gui", "getpath", "test", myName);
		try {
			conn = ConnectionFactory.createClientSupport(protocol,"localhost", port);
		} catch (Exception e) {
			CommUtils.outred(" Appl1HttpSprint3CmdConsole | ERROR:" + e.getMessage() );
		}
 	}


 	@Override
	public void update( Observable o , Object arg ) {
		try {
			if( conn == null ){
				CommUtils.outred(" Appl1HttpSprint3CmdConsole | Please start Appl1HTTPSprint3"   );
				return;
			}
			String move = arg.toString();
			if( move.equals("getpath")) {
				String answer = conn.request(applRunningRequest.toString());
				CommUtils.outyellow("application ruuning: " + answer);
				String curPathAnswer = conn.request(applGetPathRequest.toString());
				CommUtils.outyellow("application path: " + curPathAnswer);
				IApplMessage answMsg = new ApplMessage(answer);
				IApplMessage pathMsg = new ApplMessage(curPathAnswer);
				String path          = pathMsg.msgContent();
				if (answMsg.msgContent().equals("true")) {
					CommUtils.outmagenta("CURRENT PATH="+ path);
				}else{
					String spath = CommUtils.restoreFromConvertToSend(path);
	 				CommUtils.outmagenta("FINAL PATH="+ spath);
				}
			}else {
				curMsg = CommUtils.buildDispatch("gui", move, move, myName);
				CommUtils.outyellow("forward: " + curMsg);
				conn.forward(curMsg.toString());
			}
		} catch (Exception e) {
			CommUtils.outred(" Appl1HttpSprint3CmdConsole | update ERROR:" + e.getMessage() );;
		}	
	}
	
	public static void main( String[] args) {
 		new Appl1HttpSprint3CmdConsole( "cmdconsole", ProtocolType.tcp, "8030" );
    }
}

