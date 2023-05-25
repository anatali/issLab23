package it.unibo.radarSystem22.sprint2.proxy;

import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import unibo.basicomm23.enablers.CallerAsClient;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;


public class RadarGuiClient extends CallerAsClient implements IRadarDisplay {
 
	public RadarGuiClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name, host, entry,protocol );
		//CommUtils.outgreen(name + "connect to host=" + host + " entry=" + entry);
 	}

	@Override
	public int getCurDistance() {
		String answer;
		try {
			answer = getConn().request("getCurDistance");
	 		return Integer.parseInt(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			CommUtils.outred(name + " | getCurDistance ERROR:" + e.getMessage());
			return 0;
		}
	}
	
	@Override  //from IRadarDisplay
	public void update(String d, String a) {		 
 		String msg= "'"+ "{ \"distance\" : D , \"angle\" : A }".replace("D",d).replace("A",a) + "'";
		try {
	 		IApplMessage dispatch = CommUtils.buildDispatch(name, "distance", msg,  "radar");
			CommUtils.outyellow(name+" | update-> " + dispatch);
			getConn().forward(dispatch);
		} catch (Exception e) {
 			CommUtils.outred(name+" | update ERROR:" +e.getMessage());;
		}   
 	}


 	
}
