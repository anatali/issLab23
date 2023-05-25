package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IGaugeValue;
import it.unibo.radar.interfaces.IGaugeView;
import it.unibo.radar.interfaces.IRadar;
 

public class RadarWithView extends RadarA{
protected IGaugeView radarView; 

public static IRadar create( IGaugeView radarView) {
    return new RadarWithView( radarView);
}
	protected RadarWithView( IGaugeView radarView) {
			super( );
			this.radarView = radarView;
 	}
 
	  @Override
	  public void update(final IGaugeValue gaugeValue) throws Exception {
	     super.update(gaugeValue);
 	     radarView.update( getValueDefRep() );
	  }
}
