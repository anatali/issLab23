package unibo.basicomm23.enablers;

import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.tcp.TcpServer;
import unibo.basicomm23.udp.UdpServer;
import unibo.basicomm23.utils.CommUtils;

public class EnablerAsServer   {  
private static int count=1;
protected int port;
protected String name;
protected ProtocolType protocol;
protected TcpServer serverTcp;
protected UdpServer serverUdp;
protected boolean isactive = false;

	public EnablerAsServer( String name, int port, ProtocolType protocol  ) {
		this.name     			= name;
		this.port               = port;
		this.protocol 			= protocol;
	}
	public EnablerAsServer(
			String name, int port, ProtocolType protocol, IApplMsgHandler handler ){
			this(name,   port,   protocol);
			addMsgHandler(handler);
	}
	public void addMsgHandler(IApplMsgHandler handler)  {
		if( protocol != null  ) {
			setServerSupport( port, protocol, handler  );
		}else CommUtils.outred(name+" | EnablerAsServer CREATED no protocol"  );
	}
	//Crea il server
 	protected void setServerSupport( int port, ProtocolType protocol, IApplMsgHandler handler   )  {
      try {
		if( protocol == ProtocolType.tcp  ) {
			serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port,  handler );
			//CommUtils.outblue(name+" |  EnablerAsServer CREATED  on port=" + port + " protocol=" + protocol + " handler="+handler);
		}
		else if( protocol == ProtocolType.udp ) {  
			//CommUtils.out(name+" |  Do nothing for udp" );
			serverUdp = new UdpServer("EnabSrvUdp"+count++,port,handler);
		}
		else if( protocol == ProtocolType.coap ) {
			//CoapApplServer.getTheServer();	//Le risorse sono create alla configurazione del sistema
			CommUtils.outblue(name+" |  TODO CREATED  CoapApplServer"  );
		}
		else if( protocol == ProtocolType.mqtt ) {  
			CommUtils.outblue(name+" |  Do nothing for mqtt" );
		}
     } catch (Exception e) {
		CommUtils.outred(name+" |  EnablerAsServer CREATE Error: " + e.getMessage()  );
     }
	}
 	
 	public String getName() {
 		return name;
	}
 	public boolean isActive() {
 		return isactive;
 	}

 	//Attiva il server
	public void  start() {
		//CommUtils.outblue(name+" |  EnablerAsServer start" );
		switch( protocol ) {
 	   		case tcp :  { serverTcp.activate();break;}
 	   		case udp:   { serverUdp.activate();break;}
 	   		default: break;
 	    }
		isactive = true;
 	}
 
 	public void stop() {
 		//Colors.out(name+" |  deactivate  "  );
		if( ! isactive ) return;
		switch( protocol ) {
	   		case tcp :  { serverTcp.deactivate();break;}
	   		case udp:   { serverUdp.deactivate();break;}
	   		default: break;
	    }
		isactive = false;
 	}
  	 
}
