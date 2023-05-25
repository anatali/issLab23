package it.unibo.radar.interfaces;
public interface IGauge {

  /**
   * @return The current it.unibo.dronesystem.gauge value.
   */
  public abstract IGaugeValue getValue();

  /**
   * @return The default representation for the current it.unibo.dronesystem
   *         .gauge value.
   */
  public abstract String getValueDefRep();

  /**
   * @param gaugeValue
   *          Update the current it.unibo.dronesystem.gauge value
   *          with the provided one.
   */
  public abstract void update(final IGaugeValue gaugeValue) throws Exception;
}
