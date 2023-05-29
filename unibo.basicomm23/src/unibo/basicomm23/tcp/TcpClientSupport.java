package unibo.basicomm23.tcp;
import java.net.Socket;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ColorsOut;

public class TcpClientSupport {

	
	public static Interaction connect(String host, int port, int nattempts ) throws Exception {

		for( int i=1; i<=nattempts; i++ ) {
			try {
				Socket socket     =  new Socket( host, port );
 				Interaction conn  =  new TcpConnection( socket );
				return conn;
			}catch(Exception e) {
				ColorsOut.out("TcpClient | Another attempt to connect with host:" + host + " port=" + port);
				Thread.sleep(500);
			}
		}//for
		throw new Exception("TcpClient | Unable to connect to host:" + host);
		 
 	}
 
}
