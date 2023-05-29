package unibo.basicomm23.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


public class TcpConnection extends Connection { //implements Interaction
private DataOutputStream outputChannel;
private BufferedReader inputChannel;
private Socket socket;
private int port;

	public static Interaction create(String host, int port) throws Exception{
		for( int i=1; i<=10; i++ ) {
			try {
				Socket socket      =  new Socket( host, port );
				Interaction  conn  =  new TcpConnection( socket );
				return conn;
			}catch(Exception e) {
				ColorsOut.out("    +++ TcpConnection | Another attempt to connect with host:" + host + " port=" + port);
				Thread.sleep(500);
			}
		}
		throw new Exception("    +++ TcpConnection ERROR");
	}
	public TcpConnection( String host, int port  ) throws Exception {
		//Socket socket  = new Socket( host, port );
		this( new Socket( host, port ) );
	}
	public TcpConnection( Socket socket  )  throws Exception {
		//try {
			this.socket = socket;
			OutputStream outStream = socket.getOutputStream();
			InputStream inStream   = socket.getInputStream();
			outputChannel = new DataOutputStream(outStream);
			inputChannel  = new BufferedReader(new InputStreamReader(inStream));
		//}catch(Exception e){ CommUtils.outred("    +++ TcpConnection ERROR " + e.getMessage()); }
	}
	
	@Override
	public void forward(String msg)  throws Exception {
		//if( trace ) CommUtils.outyellow( "    +++ TcpConnection | sendALine  " + msg + " on " + outputChannel );
		try {
			outputChannel.writeBytes( msg+"\n" );
			outputChannel.flush();
			//if( trace ) CommUtils.outyellow( "    +++ TcpConnection | has sent   " + msg  );
		} catch (IOException e) {
			//CommUtils.outred( "    +++ TcpConnection | sendALine ERROR " + e.getMessage());
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
	public String receiveMsg()  { //called by TcpApplMessageHandler
 		try {
			//CommUtils.outyellow( "    +++ TcpConnection | receiveMsg..... " + " in " + Thread.currentThread().getName()  );
			//socket.setSoTimeout(timeOut)
			String	line = inputChannel.readLine() ; //blocking =>
			if( trace ) CommUtils.outyellow( "    +++ TcpConnection | receiveMsg on port:" + socket.getPort()
					+ " " +line + " thname=" + Thread.currentThread().getName() );
			return line;
		} catch ( Exception e   ) {
			CommUtils.outred( "    +++ TcpConnection | receiveMsg ERROR  " + e.getMessage() );
	 		return null;
		}		
	}

	@Override
	public void close() {  //called by TcpApplMessageHandler
		try {
			socket.close();
			if( trace ) CommUtils.outyellow( "    +++ TcpConnection | CLOSED port=" + socket.getPort() );
		} catch (IOException e) {
			CommUtils.outred( "    +++ TcpConnection | close ERROR " + e.getMessage());
		}
	}



}
