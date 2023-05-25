package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IGaugeView;
import it.unibo.radar.interfaces.IGaugeViewImpl;
 
 
 
public class GaugeView implements IGaugeView {
  private String               shownValue;
  private final IGaugeViewImpl implementor;

  /* { Constructors and factories */
  protected GaugeView(final IGaugeViewImpl implementor) {
    this.shownValue = null;
    this.implementor = implementor;
  }

  public static IGaugeView create(final IGaugeViewImpl implementor) {
    return new GaugeView(implementor);
  }

  /* { {IGaugeView} implementation */

  /**
   * @see IGaugeView#getShownValue()
   */
  @Override
  public String getShownValue() {
    return this.shownValue;
  }

  /**
   * @see IGaugeView#update(String)
   */
  @Override
  public void update(final String value) {
    this.setShownValue(value);
    this.implementor.update(this.shownValue);
  }



  /* { Utility */

  /**
   * Set the currently shown value.
   * 
   * @param shownValue
   *          The value to be set.
   */
  protected void setShownValue(final String shownValue) {
    this.shownValue = shownValue;
  }

  /**
   * @return The gauge view implementor
   */
  protected IGaugeViewImpl getImplementor() {
    return this.implementor;
  }



}
