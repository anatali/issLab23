package javacode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import it.unibo.kactor.ApplMessageType;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;


public class SonarOnRaspCoap {
private CoapSupport coapSupport ;
private BufferedReader reader;

	public SonarOnRaspCoap() throws Exception {
		reader = new BufferedReader(
				new InputStreamReader( new FileInputStream("coapConfig.txt")) );
	    String coapAddr = reader.readLine();
		String path     = reader.readLine();
		System.out.println("coapAddr=" + coapAddr + " path=" + path);
		coapSupport     = new CoapSupport(coapAddr, path);		
		
		new Thread() {
			public void run() {
				System.out.println("SonarOnRaspCoap reader starts  "   );
		        int numData     = 5;
		        int dataCounter = 1;
		        Process p;
				try {
					p = Runtime.getRuntime().exec("sudo ./SonarAlone");
			        BufferedReader reader = new BufferedReader(
			        		new InputStreamReader(p.getInputStream()));		        		
			        while( true ){
 			        	String data = reader.readLine();
				        dataCounter++;
				        if( dataCounter % numData == 0 ) { //every numData ...
					        System.out.println("data  " + data );
 				        	IApplMessage m = new ApplMessage(
						        "sonar", ApplMessageType.event.toString(),
					        	"sonarRasp", "none", "sonar("+data+")", ""+dataCounter, null);
 				        	System.out.println("EMIT m=  " + m );
 				        	if( ! coapSupport.updateResource( m.toString() ) ) 
 				        		System.out.println("EMIT failure"  );
				        }
 			        }//while
				} catch (Exception e1) {
 					e1.printStackTrace();
				}
			}//run
		}.start();
	}//constructor
	
	public static void main(String[] args)  {
		try {
			new SonarOnRaspCoap();
		} catch (Exception e) {
			System.out.println("WARNING: "+ e.getMessage() );
			e.printStackTrace();
		}
	}
	
}
