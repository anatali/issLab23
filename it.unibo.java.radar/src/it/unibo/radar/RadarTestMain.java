package it.unibo.radar;

public class RadarTestMain {
	
	public static void init(   ) {
		radarPojo.radarSupport.setUpRadarGui();
		System.out.println("RadarTest STARTED ");
	}
	
	
	public static void doJob() {
		delay( 1000 ); 
		for( int i = 0; i<10; i++ ) {
			String angle    = ""+i*20;
			String distance = ""+ (90 - i*10);
			radarPojo.radarSupport.update(distance,angle);
			delay( 500 ); 
 		}		
//		radarPojo.radarSupport.update("60","90");
//		delay( 3000 ); 
//		radarPojo.radarSupport.update("60","0");
	}
	
	
	private static void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		init();
		doJob();
  	}

}
