package it.unibo.radarSystem23.sprint1.usecases;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import unibo.basicomm23.utils.CommUtils;

public class RadarGuiUsecase {
 
	public static void doUseCase( IRadarDisplay radar, IDistance d ) {	    
		CommUtils.outyellow("RadarGuiUsecase |  doUseCase  d=" + d.getVal() );
		if( radar != null ) {
			int v = d.getVal() ;
			radar.update(""+v, "30");
		}
  	}	
}
