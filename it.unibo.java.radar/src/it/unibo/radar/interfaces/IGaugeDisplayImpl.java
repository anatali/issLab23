package it.unibo.radar.interfaces;

import it.unibo.radar.gui.GaugeDisplayStatus;

 
public interface IGaugeDisplayImpl { 

  /**
   * Implementor-side for IGaugeDisplay#setStatus(GaugeDisplayStatus)
   * 
   * @param status
   *          The display status.
   */
  public abstract void setStatus(final GaugeDisplayStatus status);
}
