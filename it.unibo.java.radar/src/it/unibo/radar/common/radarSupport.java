package it.unibo.radar.common;

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
