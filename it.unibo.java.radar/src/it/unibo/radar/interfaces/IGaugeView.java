package it.unibo.radar.interfaces;
 
public interface IGaugeView {

  /**
   * @return The currently shown value.
   */
  public abstract String getShownValue();

  /**
   * Update the displays with the provided value.
   * 
   * @param value
   *          The value which the displays should be updated.
   */
  public abstract void update(final String value);
}
