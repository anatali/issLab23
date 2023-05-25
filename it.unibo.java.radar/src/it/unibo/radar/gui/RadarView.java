package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IRadarView;
import it.unibo.radar.interfaces.IRadarViewImpl;
 
 
public class RadarView extends GaugeView implements IRadarView {
 
  protected RadarView(final IRadarViewImpl implementor) {
    super(implementor);
  }

  public static IRadarView create(final IRadarViewImpl implementor) {
    return new RadarView(implementor);
  }



}
