package it.unibo.radarSystem22.sprint2.proxy;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

public class RadarGuiProxyAsClient extends ProxyAsClient implements IRadarDisplay {
 
	public RadarGuiProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name, host, entry,protocol );
 	}

	@Override
	public int getCurDistance() {
		String answer = sendRequestOnConnection("getCurDistance");
 		return Integer.parseInt(answer);
	}
	
	@Override  //from IRadarDisplay
	public void update(String d, String a) {		 
 		String msg= "{ \"distance\" : D , \"angle\" : A }".replace("D",d).replace("A",a);
		try {
			sendCommandOnConnection(msg);
		} catch (Exception e) {
 			ColorsOut.outerr(name+" | update ERROR:" +e.getMessage());;
		}   
 	}


 	
}
