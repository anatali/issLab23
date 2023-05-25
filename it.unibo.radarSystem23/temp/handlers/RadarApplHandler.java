package it.unibo.radarSystem22.sprint2.handlers;
 
import org.json.JSONObject;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.ColorsOut;


public class RadarApplHandler extends ApplMsgHandler {
private IRadarDisplay radar;
private int curDistance = 0;

	public RadarApplHandler(String name, IRadarDisplay radar) {
		super(name);
		this.radar = radar; 
	}

 	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message + " conn=" + conn);
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
		JSONObject jsonObj   = new JSONObject(message);	
		curDistance = jsonObj.getInt("distance");
		String distance = ""+curDistance;
		String angle    = ""+jsonObj.getInt("angle");
		radar.update( distance, angle );
	}

}
