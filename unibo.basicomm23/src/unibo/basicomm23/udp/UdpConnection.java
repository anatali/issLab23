package unibo.basicomm23.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.utils.BasicUtils;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


public class UdpConnection extends Connection {//implements Interaction{
	
public static final int MAX_PACKET_LEN = 1025;
public static final String closeMsg    = "@+-systemUdpClose@+-";
protected DatagramSocket socket;
protected UdpEndpoint endpoint;
protected boolean closed;

	public static Interaction create(String host, int port) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		InetAddress address   = InetAddress.getByName(host);
		UdpEndpoint server    = new UdpEndpoint(address, port);
		Interaction  conn     = new UdpConnection(socket, server);
		return conn;
	}

	public UdpConnection( DatagramSocket socket, UdpEndpoint endpoint) throws Exception {
		closed        = false;
		this.socket   = socket;
		this.endpoint = endpoint;
	}
	
	@Override
	public void forward(String msg)  throws Exception {
		CommUtils.outyellow( "    +++ UdpConnection | forward  " + msg   );
		if (closed) { throw new Exception("The connection has been previously closed"); }
		try {
			byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, endpoint.getAddress(), endpoint.getPort());
	        socket.send(packet);
			CommUtils.outyellow( "    +++ UdpConnection | has sent   " + msg  );
		} catch (IOException e) {
			CommUtils.outred( "    +++ UdpConnection | forward ERROR " + e.getMessage());
			throw e;
		}	
	}

	@Override
	public String request(String msg)  throws Exception {
		forward(  msg );
		String answer = receiveMsg();
		return answer;
	}
	
	@Override
	public void reply(String msg) throws Exception {
		forward(msg);
	} 
	
	@Override
	public String receiveMsg()  {
		String line;
 		try {
			if(closed) {
				line = null; //UdpApplMessageHandler will terminate
			}else {
				byte[] buf = new byte[UdpConnection.MAX_PACKET_LEN];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
			    //BasicUtils.aboutThreads( " | UdpConnection Before Receive  " );  
				socket.receive(packet);  //CREA UN Thread
			    //BasicUtils.aboutThreads( " | UdpConnection After Receive  "  );  
				line = new String(packet.getData(), 0, packet.getLength());
				if( line.equals(closeMsg)) {
					close();
				}
				packet = null;
			}
			CommUtils.outyellow( "    +++ UdpConnection | receiveMsg  " + line   );
			return line;
		} catch ( Exception e ) {
			CommUtils.outred( "    +++ UdpConnection | receiveMsg ERROR  " + e.getMessage() );
	 		return null;
		}		
	}



	@Override
	public void close() {
		try {
			//CommUtils.buildDispatch("", "closeUdpConn", closeMsg, "system");
			forward(closeMsg);
		} catch (Exception e) {
			CommUtils.outred( "    +++ UdpConnection | close ERROR  " + e.getMessage() );
		}
		closed = true;
		CommUtils.outyellow( "    +++ UdpConnection | closing   "  );
		socket.close();
	}



}
