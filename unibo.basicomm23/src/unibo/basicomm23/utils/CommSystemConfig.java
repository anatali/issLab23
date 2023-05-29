package unibo.basicomm23.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import unibo.basicomm23.msg.ProtocolType;



public class CommSystemConfig {
	
	public static  String mqttBrokerAddr = "tcp://localhost:1883"; //: 1883  OPTIONAL  tcp://broker.hivemq.com
	public static int serverTimeOut        =  6000000;  //100 minuti	
 	public static ProtocolType protcolType = ProtocolType.tcp;
 	public static boolean tracing          = false;

 	private static JSONParser simpleparser        = new JSONParser();;
 	
	public static void setTheConfiguration(  ) {
		setTheConfiguration("../CommSystemConfig.json");
	}
	
	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName � in una dir che include la bin  
//		FileInputStream fis = null;
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);
//			if(  fis == null ) {
// 				 fis = new FileInputStream(new File(resourceName));
//			}
	        //JSONTokener tokener = new JSONTokener(fis);
			//Reader reader       = new InputStreamReader(fis);
			//JSONTokener tokener = new JSONTokener(reader);     
			Stream<String> stream = Files.lines(Paths.get(resourceName), StandardCharsets.UTF_8);
				  //stream.forEach(s -> contentBuilder.append(s).append("\n"));
				stream.forEach(s -> {
					 JSONObject object;
					try {
						object = (JSONObject) simpleparser.parse(s);
					     mqttBrokerAddr   = object.get("mqttBrokerAddr").toString();
					     tracing          = object.get("tracing").equals("true");
					        switch( object.get("protocolType").toString() ) {
						        case "tcp"  : protcolType = ProtocolType.tcp; break;
						        case "coap" : protcolType = ProtocolType.coap; break;
						        case "mqtt" : protcolType = ProtocolType.mqtt; break;
					        }
					} catch (ParseException e) {
						ColorsOut.outerr("setTheConfiguration read ERROR " + e.getMessage() );
					}
				}  );
 
	        //JSONObject object   = (JSONObject) simpleparser.parse(message);
	        
//	        mqttBrokerAddr   = object.getString("mqttBrokerAddr");
//	        tracing          = object.getBoolean("tracing");
	        
//	        switch( object.getString("protocolType") ) {
//		        case "tcp"  : protcolType = ProtocolType.tcp; break;
//		        case "coap" : protcolType = ProtocolType.coap; break;
//		        case "mqtt" : protcolType = ProtocolType.mqtt; break;
//	        }
 	        
		} 	catch ( Exception e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
		} 

	}	
	 
}
