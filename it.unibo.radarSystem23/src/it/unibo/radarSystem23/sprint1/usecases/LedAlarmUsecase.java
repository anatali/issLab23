package it.unibo.radarSystem23.sprint1.usecases;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem23.sprint1.RadarSystemConfig;
import it.unibo.radarSystem22.domain.interfaces.*;

public class LedAlarmUsecase {
 	public static void doUseCase(ILed led, IDistance d) {
 		try {
 			//Colors.out("LedAlarmUsecase | sonar distance=" + d);
			if( d.getVal() <  RadarSystemConfig.DLIMIT ) led.turnOn(); else  led.turnOff();
 		} catch (Exception e) {
 			ColorsOut.outerr("LedAlarmUsecase | ERROR " + e.getMessage() );
		}					
 	}
 
}
