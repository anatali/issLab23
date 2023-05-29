package unibo.basicomm23.actors23.example;

import unibo.basicomm23.examples.ActorNaiveCaller;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

import java.util.Observable;
import java.util.Observer;

/*

 */

public class CmdConsoleRemote extends ActorNaiveCaller  {
private IApplMessage curMsg;

	public CmdConsoleRemote(String name, ProtocolType protocol, String hostAddr, String entry) {
		super(name, protocol,   hostAddr,   entry);
  	}

	@Override
	protected void body() throws Exception {
		//connect();
		//CommUtils.outmagenta("CmdConsoleRemote simply reac ts to buttons");
		curMsg = CommUtils.buildDispatch("gui", "cmd", "start", "a1");
		connSupport.forward(curMsg.toString());
		CommUtils.delay(3000);
		CommUtils.outmagenta(name + " | send stop " + Thread.currentThread().getName());
		curMsg = CommUtils.buildDispatch("gui", "cmd", "stop", "a1");
		connSupport.forward(curMsg.toString());
	}

	public static void main( String[] args) {
		CmdConsoleRemote console = new CmdConsoleRemote( "cmdconsole", ProtocolType.tcp, "localhost", "8123" );
		console.activate();
	}

}

