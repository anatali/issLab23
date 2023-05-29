package unibo.basicomm23.enablers;

import org.eclipse.californium.core.CoapResource;
import unibo.basicomm23.coap.CoapApplServer;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.IServer;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.tcp.TcpServer;
import unibo.basicomm23.udp.UdpServer;
import unibo.basicomm23.utils.CommUtils;

public class ServerFactory implements IServer {
private static int count=1;
protected int port;
protected String name;
protected ProtocolType protocol;
protected TcpServer serverTcp;
protected UdpServer serverUdp;
protected boolean isactive = false;

public static IServer create(String name, int port, ProtocolType protocol, IApplMsgHandler handler){
	return new ServerFactory(name,   port,   protocol, handler);
}
	public ServerFactory(String name, int port, ProtocolType protocol  ) {
		this.name     			= name;
		this.port               = port;
		this.protocol 			= protocol;
	}
	public ServerFactory(String name, int port, ProtocolType protocol, IApplMsgHandler handler ){
			this(name,   port,   protocol);
			addMsgHandler(handler);
	}
	public void addMsgHandler(IApplMsgHandler handler)  {
		if( protocol != null  ) {
			setServerSupport( port, protocol, handler  );
		}else CommUtils.outred(name+" | ServerFactory CREATED no protocol"  );
	}
	//Crea il server
 	protected void setServerSupport(
 			int port, ProtocolType protocol, IApplMsgHandler handler )  {
      try {
		if( protocol == ProtocolType.tcp  ) {
			serverTcp = new TcpServer( "SrvTcp_"+count++, port,  handler );
			//CommUtils.outblue(name+" |  ServerFactory CREATED  on port=" + port + " protocol=" + protocol + " handler="+handler);
		}else if( protocol == ProtocolType.udp ) {
			serverUdp = new UdpServer("SrvUdp" + count++, port, handler);
		}
		else if( protocol == ProtocolType.coap ) {
			CoapApplServer server = new CoapApplServer(port);
			CoapResource res      = (CoapResource) handler;
			server.addCoapResource(  res, "basicomm23" );
			//CoapApplServer.getTheServer();	//Le risorse sono create alla configurazione del sistema
			//CommUtils.outblue(name+" |  CREATED  CoapApplServer"  );
		}/*
		else if( protocol == ProtocolType.mqtt ) {  
			CommUtils.outblue(name+" |  Do nothing for mqtt" );
		}*/
		else{
			throw new IllegalAccessException("protocol not supported");
		}
	  } catch (Exception e) {
		CommUtils.outred(name+" |  ServerFactory CREATE Error: " + e.getMessage()  );
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
		//CommUtils.outblue(name+" |  ServerFactory start" );
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
