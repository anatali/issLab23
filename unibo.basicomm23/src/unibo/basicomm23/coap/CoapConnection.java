package unibo.basicomm23.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


public class CoapConnection extends Connection {
private CoapClient client;
private String url;
 
private String answer = "unknown";

	public static Interaction create(String host, String path) throws Exception {
	 	return new CoapConnection(host,path);
	}

	public CoapConnection( String address, String path) { //"coap://localhost:5683/" + path
 		setCoapClient(address,path);
	}
	
	protected void setCoapClient(String addressWithPort, String path) {
		//CommUtils.outmagenta(  "    +++ CoapConn | setCoapClient addressWithPort=" +  addressWithPort  );
		//url            = "coap://"+address + ":5683/"+ path;
		url            = "coap://"+addressWithPort + "/"+ path;
		if( Connection.trace )  CommUtils.outyellow(  "    +++ CoapConn | setCoapClient url=" +  url  );
		client          = new CoapClient( url );
 		client.useExecutor(); //To be shutdown
		if( Connection.trace )  CommUtils.outyellow("    +++ CoapConn | STARTS client url=" +  url ); //+ " client=" + client );
		client.setTimeout( 1000L );		 		
	}
 	
	public void removeObserve(CoapObserveRelation relation) {
		relation.proactiveCancel();
		if( Connection.trace )  CommUtils.outyellow("    +++ CoapConn | removeObserve !!!!!!!!!!!!!!!" + relation   );
	}
	public CoapObserveRelation observeResource( CoapHandler handler  ) {
		CoapObserveRelation relation = client.observe( handler ); 
		//if( Connection.trace )  CommUtils.outyellow("    +++ CoapConn |  added " + handler + " relation=" + relation + relation );
 		return relation;
	}


	
//From Interaction 
	@Override
	public void forward(String msg)   {
	    if( Connection.trace ) CommUtils.outyellow(  "    +++ CoapConn | forward " + url + " msg=" + msg );

		if( client != null ) {
			CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN); //Blocking!
			if( resp != null ) {
				if (Connection.trace)
					CommUtils.outyellow("    +++ CoapConn | forward " + msg + " resp=" + resp.getCode());
			}else {
		    	CommUtils.outred("    +++ CoapConn | forward - resp null for " + msg  );
		    }  //?????
		} 
	}

	
	@Override
	public String request(String query)   {
		if( Connection.trace ) CommUtils.outyellow(  "    +++ CoapConn | request query=" + query + " url="+url  );
		String param = query.isEmpty() ? "" :  "?q="+query;
		if( Connection.trace ) CommUtils.outyellow(  "    +++ CoapConn | param=" + (url+param)  );

  		//client.setURI(url+param);
		//CoapResponse response = client.get(  );

		client.setURI(url );
		CoapResponse response = client.put(query, MediaTypeRegistry.TEXT_PLAIN);
		if( response != null ) {
			if( Connection.trace )  CommUtils.outyellow(  "    +++ CoapConn | request=" + query
 	 				+" RESPONSE CODEEEE: " + response.getCode() + " answer=" + response.getResponseText()  );
			answer = response.getResponseText();
 	 		return answer;
		}else {
	 		CommUtils.outred(  "    +++ CoapConn | request=" + query +" RESPONSE NULL " );
			return null;
		}
	}
	
	//https://phoenixnap.com/kb/install-java-raspberry-pi
	
	@Override
	public void reply(String reqid) throws Exception {
		throw new Exception( "   +++ CoapConn | reply not allowed");
	} 

	@Override
	public String receiveMsg() throws Exception {
		if( Connection.trace )  CommUtils.outyellow(  "    +++ CoapConn | receiveMsg" );
		return answer;
	}

	@Override
	public void close()  {
		if( Connection.trace ) CommUtils.outyellow(  "    +++ CoapConn | client shutdown=" + client);
		client.shutdown();	
	}

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
*/