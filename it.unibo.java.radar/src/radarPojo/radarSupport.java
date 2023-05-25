package radarPojo;

import it.unibo.radar.common.RadarControl;

public class radarSupport {
private  static RadarControl radarControl;

public static void setUpRadarGui( ) {
		try {
			radarControl = new RadarControl( null );
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	public static void update(  String dist, String theta ){
		if( radarControl != null ) radarControl.update( dist,   theta );
	}
}
