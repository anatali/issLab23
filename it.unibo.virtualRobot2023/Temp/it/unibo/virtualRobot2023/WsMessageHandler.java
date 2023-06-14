package it.unibo.virtualRobot2023;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.IWsMsgHandler;
import unibo.basicomm23.utils.CommUtils;

public class WsMessageHandler implements IWsMsgHandler {
	private JSONParser simpleparser = new JSONParser();
	
	public void handleMessage(String message) {
        try {
            //{"collision":"move ","target":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
        	CommUtils.outgreen("WsMessageHandler | handleMessage:" + message);
            JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
            /*
            if (jsonObj.get("endmove") != null) {
                boolean endmove = jsonObj.get("endmove").toString().equals("true");
                String  move    = jsonObj.get("move").toString();
                //CommUtils.outgreen("WsMessageHandler | handleMessage " + move + " endmove=" + endmove);
                if( endmove ) goon.nextStep(false);
            } else if (jsonObj.get("collision") != null) {
                boolean collision = true; //jsonObj.get("collision").toString().equals("true");
                String move = jsonObj.get("collision").toString();
                CommUtils.outmagenta(message);
                goon.nextStep(collision);
            } else if (jsonObj.get("sonarName") != null) {
                String sonarNAme = jsonObj.get("sonarName").toString();
                String distance = jsonObj.get("distance").toString();
                CommUtils.outgreen("WsMessageHandler | handleMessage sonaraAme=" + sonarNAme + " distance=" + distance);
            }
*/
        } catch (Exception e) {
        	CommUtils.outred("handleMessage ERROR:"+e.getMessage());
        }
	};
}
