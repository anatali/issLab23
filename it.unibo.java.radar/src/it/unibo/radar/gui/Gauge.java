package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IGauge;
import it.unibo.radar.interfaces.IGaugeValue;

public abstract class Gauge implements IGauge {

  private IGaugeValue gaugeValue;

  /* { Constructors and factories */

  protected Gauge() {
    this.gaugeValue = null;
  }

  /* { {IGauge} implementation */

  /**
   * @see IGauge#getValue()
   */
  @Override
  public IGaugeValue getValue() {
    if (this.gaugeValue == null) {
      throw new IllegalStateException("The gauge hasn't been updated yet.");
    }
    return this.gaugeValue;
  }

  /**
   * @see IGauge#getValueDefRep()
   */
  @Override
  public String getValueDefRep() {
    return this.getValue().getDefRep();
  }

  /**
   * @see IGauge#update(IGaugeValue)
   */
  @Override
  public void update(final IGaugeValue gaugeValue) throws Exception {
//    if (!this.validateGaugeValue(gaugeValue)) {
//      throw new Exception("Invalid gaugeValue");
//    }
    this.setGaugeValue(gaugeValue);
  }

  /* } */

  /* { Utility */

  /**
   * Set the gauge value to the provided one.
   * 
   * @param gaugeValue
   *          The new gauge value for the gauge
   */
  private void setGaugeValue(final IGaugeValue gaugeValue) {
    this.gaugeValue = gaugeValue;
  }

  /**
   * Validate the provided gaugeValue.
   * 
   * @param gaugeValue
   *           
   * @return true if the gaugeValue has been validated, otherwise false.
   */
  protected abstract Boolean validateGaugeValue(IGaugeValue gaugeValue);

  /* } */

}
