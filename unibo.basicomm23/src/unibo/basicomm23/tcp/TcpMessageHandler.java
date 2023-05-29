package unibo.basicomm23.tcp;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpMessageHandler extends Thread{
private IApplMsgHandler handler ;
private Interaction  conn;

public TcpMessageHandler(IApplMsgHandler handler, Interaction conn ) {
		this.handler = handler;
		this.conn    = conn;
	    //CommUtils.outblue("TcpMessageHandler | STARTING with user handler:" + handler.getName()  );
 		this.start();
}
 	
	@Override 
	public void run() {
		String name = handler.getName();
			//CommUtils.outyellow(getName() + " | TcpMessageHandler  STARTS with user-handler=" + name + " conn=" + conn );
			while( true ) {
			try {
				//CommUtils.outblue(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
				//CommUtils.outblue(name + "  | TcpMessageHandler received:" + msg   );
			    if( msg == null ) {
			    	//conn.close();	//Feb23
			    	break;
			    } else{ 
			    	IApplMessage m = new ApplMessage(msg);
			    	handler.elaborate( m, conn ); 
			    }
			}catch( Exception e) {
				CommUtils.outred( getName() + "  | TcpMessageHandler: " + e.getMessage()  );
			}
        }
		//CommUtils.outblue(getName() + " | TcpMessageHandler BYE"   );
	}
}
