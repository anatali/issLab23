package javacode;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import it.unibo.kactor.ApplMessageType;
import org.eclipse.californium.elements.exception.ConnectorException;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

import java.io.IOException;


class MyHandler implements CoapHandler {
	public MyHandler ( ) {		 
	}
	@Override public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		System.out.println("MyHandler | NOTIFICATION: " + content);
 	}					
	@Override public void onError() {
		System.err.println("MyHandler  |  FAILED (press enter to exit)");
	}
}

public class CoapSupport {
private CoapClient client;
private CoapObserveRelation relation = null;

	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
		String url = address + "/" + path;
		client = new CoapClient( url );
		System.out.println("CoapSupport | STARTS url=" +  url + " client=" + client );
		client.setTimeout( 1000L );		 
	}
	public CoapSupport( String address ) {  
		this(address, Resource.path);
	}
	
	public String readResource(   ) throws ConnectorException, IOException {
		CoapResponse respGet = client.get( );
		System.out.println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode());		
		return respGet.getResponseText();
	}

	public void removeObserve() {
		relation.proactiveCancel();	
	}
	public void  observeResource( CoapHandler handler  ) {
		relation = client.observe( handler );
	}

	public boolean updateResource( String msg ) throws ConnectorException, IOException {
     	IApplMessage m = new ApplMessage(
	        "sonar", ApplMessageType.event.toString(),
        	"sonarRasp", "none", "sonar("+msg+")", "1" , null);

		CoapResponse resp = client.put(m.toString(), MediaTypeRegistry.TEXT_PLAIN);
//			if( resp != null ) System.out.println("CoapSupport | updateResource RESPONSE CODE: " + resp.getCode());	
//			else System.out.println("CoapSupport | updateResource FAILURE: "  );
		return resp != null;
	}
	
	
	public void test() throws ConnectorException, IOException {
		String v = readResource();
		System.out.println("   CoapSupport | v=" + v);
		updateResource("23");
//		v = readResource();
//		System.out.println("   CoapSupport | v=" + v);		
	}
	
	public static void main(String[] args) throws ConnectorException, IOException {
		CoapSupport cs = new CoapSupport("coap://localhost:5683","robot/sonar");
		cs.test();		
	}
	
}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/