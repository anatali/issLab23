package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IGaugeValue;
import it.unibo.radar.interfaces.IRadar;

public class RadarA extends Gauge implements IRadar {
   protected RadarA( ) {
    super();
    }

  public static IRadar create( ) {
    return new RadarA( );
  }
 
@Override
protected Boolean validateGaugeValue(IGaugeValue gaugeValue) {
 	return true;
}
 }
