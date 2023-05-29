package unibo.basicomm23.tcp;

import java.net.ServerSocket;
import java.net.Socket;

import unibo.basicomm23.enablers.ConnectionHandler;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


public class TcpServer extends Thread{
private ServerSocket serversock;
protected IApplMsgHandler userDefHandler;
protected String name;
protected boolean stopped = true;
protected int port;

 	public TcpServer( String name, int port,  IApplMsgHandler userDefHandler   ) {
		super(name);
	      try {
	  		this.userDefHandler   = userDefHandler;
	  		if( Connection.trace )
	  			CommUtils.outyellow(getName() + " | port=" + port + " " + userDefHandler.getName()  );
			this.name             = getName();
			this.port             = port;
		    serversock            = new ServerSocket( port );
		    serversock.setSoTimeout(CommSystemConfig.serverTimeOut);
	     }catch (Exception e) { 
	    	 ColorsOut.outerr(getName() + " | costruct ERROR: " + e.getMessage());
	     }
	}
	
	@Override
	public void run() {
	      try {
	      	//CommUtils.outyellow(getName() + " | STARTING ... "   );
			while( ! stopped ) {
				//Accept a connection				 
				if( Connection.trace ) CommUtils.outyellow(getName() + " | TcpServer waiting on port " + port );
		 		Socket sock          = serversock.accept();
				if( Connection.trace ) CommUtils.outyellow(getName()
						+ " | TcpServer " + port + " accepted connection on "+ sock.getPort()
						+ " with userDefHandler=" + userDefHandler.getName()
						+ " thname=" + Thread.currentThread().getName() );
		 		Interaction conn     = new TcpConnection(sock);
		 		//Create a message handler on the connection
		 		//new TcpMessageHandler( userDefHandler, conn );		//thread
					new ConnectionHandler("TcpMsgHandler", userDefHandler, conn);
			}//while
		  }catch (Exception e) {  //Scatta quando la deactive esegue: serversock.close();
			  CommUtils.outred(getName() + " | probably socket closed: " + e.getMessage() );
		  }
	}
	
	public void activate() {
		if( stopped ) {
			stopped = false;
			this.start();
		}//else already activated
	}
 
	public void deactivate() {
		try {
			CommUtils.outyellow(getName()+" |  DEACTIVATE serversock=" +  serversock);
			stopped = true;
            serversock.close();
		} catch (Exception e) {
			CommUtils.outred(getName() + " | deactivate ERROR: " + e.getMessage());
		}
	}
 
}
