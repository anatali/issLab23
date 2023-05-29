package unibo.basicomm23.udp;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;

/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class UdpApplMessageHandler extends Thread{
private  IApplMsgHandler handler ;
private Interaction conn;

public UdpApplMessageHandler(  IApplMsgHandler handler, Interaction  conn ) {
		this.handler = handler;
		this.conn    = conn;
		CommUtils.outyellow("    +++ UdpApplMessageHandler | CREATED for conn=" + conn   ); 
 		this.start();
	}
 	
	@Override 
	public void run() {
		String name = handler.getName();
		try {
			CommUtils.outyellow( "    +++ UdpApplMessageHandler | STARTS with handler=" + name + " conn=" + conn  );
			while( true ) {
				//ColorsOut.out(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    ColorsOut.out("    +++ UdpApplMessageHandler received:" + msg + " handler="+handler, ColorsOut.BLUE );
			    if( msg == null || msg.equals(UdpConnection.closeMsg) ) {
			    	conn.close();
			    	break;
			    } else{ 
			    	IApplMessage m = new ApplMessage(msg);
			    	handler.elaborate( m, conn ); 
			    }
			}
			//CommUtils.outyellow("    +++ UdpApplMessageHandler | BYE"   );
		}catch( Exception e) {
			CommUtils.outred( "    +++ UdpApplMessageHandler  | ERROR:" + e.getMessage()  );
		}	
	}
}
