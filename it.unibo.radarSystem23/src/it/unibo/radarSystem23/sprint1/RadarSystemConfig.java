package it.unibo.radarSystem23.sprint1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import unibo.basicomm23.msg.ProtocolType;


public class RadarSystemConfig {
 	public static boolean tracing         = false;	
	public static boolean testing         = false;			
	public static int DLIMIT              =  15;     	
	public static boolean  RadarGuiRemote = false;
	
	private static final JSONParser simpleparser = new JSONParser();
	
//Aggiunte dello SPRINT2	
 	public static String hostAddr         = "localhost";		
	public static String raspAddr         = "localhost";		
	public static int serverPort          = 8080;
//Aggiunte dello SPRINT2a e 3
	public static int ledPort             = 8010;
	public static int sonarPort           = 8015;
	public static ProtocolType protcolType= ProtocolType.tcp;
	
	public static void setTheConfiguration(  ) {
		setTheConfiguration("../RadarSystemConfig.json");
	}
	
	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName è in una dir che include la bin  
		FileInputStream fis = null;
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);
			/*
	 			 if(  fis == null ) {
 				 fis = new FileInputStream(new File(resourceName));
			}

	        JSONTokener tokener = new JSONTokener(fis);
	        JSONObject object   = new JSONObject(tokener);
	        */
			JSONObject jsonObject = (JSONObject) simpleparser.parse(new FileReader(resourceName));
	        
			for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				  String key = (String) iterator.next();
				  System.out.println("key="+ key + " -> " +jsonObject.get(key));
			}
			
   	        tracing          = (boolean) jsonObject.get("tracing");
	        testing          = (boolean) jsonObject.get("testing");
	        RadarGuiRemote   = (boolean) jsonObject.get("RadarGuiRemote");
	        DLIMIT           = (int) jsonObject.get("DLIMIT");	
//Aggiunte dello SPRINT2	
	        serverPort		= (int) jsonObject.get("serverPort");
 	        hostAddr 		= (String) jsonObject.get("hostAddr");
	        raspAddr 		= (String) jsonObject.get("raspAddr");
//Aggiunte dello SPRINT2a
	        if( jsonObject.get("ledPort") != null && jsonObject.get("sonarPort") != null) {
		        ledPort         = (int) jsonObject.get("ledPort");
		        sonarPort       = (int) jsonObject.get("sonarPort");
	        }
	        /*
	        switch( jsonObject.getString("protocolType") ) {
		        case "tcp"  : protcolType = ProtocolType.tcp; break;
		        case "coap" : protcolType = ProtocolType.coap; break;
		        case "mqtt" : protcolType = ProtocolType.mqtt; break;
		        default: protcolType = ProtocolType.tcp;
	        }	        */
	        
		} catch (Exception e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
		}

	}	
	 
}
