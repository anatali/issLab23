package unibo.actors23;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.udp.UdpClientSupport;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


public class Proxy {
private Interaction conn; 
protected String name ;		//could be a uri
protected String entry = "";
protected ProtocolType protocol ;
protected String pfx = "    +++ ";
protected IContext23 ctx;

/*
 * Realizza la connessione di tipo Interaction (concetto astratto)
 * in modo diverso, a seconda del protocollo indicato (tecnologia specifica)
 */
	public Proxy(String name, String host, String entry, ProtocolType protocol, IContext23 ctx ) {
		try {
			if( Connection.trace) CommUtils.outgray(pfx + name+"  | CREATING entry= "+entry+" protocol=" + protocol  );
			this.name     = name;
			this.entry    = entry;
			this.protocol = protocol;
			this.ctx      = ctx;
			setConnection(host,  entry,  protocol);
			activateReceiver(conn);
			//proxyMap.put(name+host+entry,this);  //VEDI NOTA
			//CommUtils.outgray(pfx + name+"  | CREATED entry= "+entry+" conn=" + conn );
		} catch (Exception e) {
			CommUtils.outred( pfx + name+"  |  ERROR " + e.getMessage());		}
	}
	
 	public String getName() {
 		return name;
 	}
 	
	protected void setConnection( String host, String entry, ProtocolType protocol  ) throws Exception {
		switch( protocol ) {
			case tcp : {
				int port = Integer.parseInt(entry);
				//conn = new TcpConnection( new Socket( host, port ) ) ; //non fa attempts
				conn = TcpClientSupport.connect(host,  port, 50); //50 = num of attempts
				if( Actor23Utils.trace)  CommUtils.outgray(pfx + name + " |  setConnection "  + conn  );
				break;
			}
			case udp : {
				int port = Integer.parseInt(entry);
 				conn = UdpClientSupport.connect(host,  port );
				break;
			}
			case coap : {
 				//conn = new CoapConnection( host,  entry );
				break;
			}
			case mqtt : {
				//La connessione col Broker viene stabilita in fase di configurazione
				//La entry è quella definita per ricevere risposte;
				//CommUtils.out(name+"  | ProxyAsClient connect MQTT entry=" + entry );
				//conn = MqttConnection.getSupport();					
 				break;
			}	
			default :{
				CommUtils.outred(pfx + name + " | Protocol unknown");
			}
		}
	}
  	
	//msg potrebbe essere QUALSISI (anche request)
	public  void sendMsgOnConnection( String msg )  {
		if( Connection.trace)  CommUtils.outgray( pfx + name+"  | sendMsgOnConnection " + entry + " " + msg   ); //+ " conn=" + conn
		try {
			conn.forward(msg);
		} catch (Exception e) {
			CommUtils.outred( pfx + name+"  | sendMsgOnConnection " + entry + " ERROR=" + e.getMessage()  );
		}
	}
 
	public Interaction getConn() {
		return conn;
	}
	
	
	public void close() {
		try {
			conn.close();
			if( Connection.trace) CommUtils.outgray(pfx + name + " |  CLOSED " + conn  );
		} catch (Exception e) {
			CommUtils.outred( pfx + name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );
		}
	}
	
//------------------------------------------------
	protected void activateReceiver( Interaction conn) {
		new Thread() {
			public void run() {
				try {
					if( Connection.trace) CommUtils.outgray(pfx + name + " |  activateReceiver STARTED on conn=" + conn   );
					while(true) {
						String msgStr    = conn.receiveMsg();
						IApplMessage msg = new ApplMessage(msgStr);
						if( Connection.trace) CommUtils.outgray(pfx + name + " |   RECEIVES " + msg  );
						ActorBasic23 dest = ctx.getActor(msg.msgReceiver());
						if( dest != null ) dest.msgQueue.put(msg);
						else  CommUtils.outred( pfx + name+"  | activateReceiver INCOSISTENCE for " + dest  );
					}
				} catch (Exception e) {
					CommUtils.outred( pfx + name+"  | activateReceiver ERROR=" + e.getMessage()  );				}
				}
		}.start();
	}

}
