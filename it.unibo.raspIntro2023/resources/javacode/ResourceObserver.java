package javacode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
 
public class ResourceObserver {
	private CoapObserveRelation relation = null;
	private CoapClient client = null;
	
	public ResourceObserver(){
		client = new CoapClient("coap://localhost:5683/robot/sonar");
	}
	
	public void  observe( ) {
		relation = client.observe(
				new CoapHandler() {
					@Override public void onLoad(CoapResponse response) {
						String content = response.getResponseText();
						System.out.println("ResourceObserver | value=" + content);
					}					
					@Override public void onError() {
						System.err.println("OBSERVING FAILED (press enter to exit)");
					}
				});		
	}
	
	public void waitUserEnd() {
 		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 		System.out.println("ResourceObserver | press enter to end ...");		
		try { br.readLine(); } catch (IOException e) { }		
		System.out.println("ResourceObserver | CANCELLATION");		
		relation.proactiveCancel();		
	}
	
	public static void main(String[] args) {
  		ResourceObserver rco = new ResourceObserver();
		rco.observe( );
		rco.waitUserEnd();
	}

}
