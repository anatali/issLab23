package it.unibo.radarSystem23.sprint2.handlers;
 
import org.json.JSONObject;

import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;


public class RadarApplHandler extends ApplMsgHandler {
private IRadarDisplay radar;
private int curDistance = 0;

	public RadarApplHandler(String name, IRadarDisplay radar) {
		super(name);
		this.radar = radar; 
	}

 	
 	@Override
	public void elaborate(IApplMessage message, Interaction conn) {
		CommUtils.outblue(name + " | elaborate " + message + " conn=" + conn);
		if( message.equals("getCurDistance")) {
			try {
				//conn.forward(""+curDistance);
				conn.reply(""+curDistance);
			} catch (Exception e) {
 				e.printStackTrace();
			}
			return;
		}
		//{ "distance" : 90 , "angle" : 90 }
		try {
			
			JSONObject jsonObj   = new JSONObject(message.msgContent().replace("'",""));	
			curDistance          = jsonObj.getInt("distance");
			String distance = ""+curDistance;
			String angle    = ""+jsonObj.getInt("angle");
			radar.update( distance, angle );
		}catch(Exception e) {
			
		}
	}

}
