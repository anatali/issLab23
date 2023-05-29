package unibo.basicomm23.udp;
import java.net.DatagramSocket;
import java.net.InetAddress;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ColorsOut;


public class UdpClientSupport {

	
	public static Interaction connect(String host, int port) throws Exception {
		DatagramSocket socket = new DatagramSocket();
        InetAddress address   = InetAddress.getByName(host);
        UdpEndpoint server    = new UdpEndpoint(address, port);
		Interaction conn      = new UdpConnection(socket, server);
		return conn;
 	}
 
}
