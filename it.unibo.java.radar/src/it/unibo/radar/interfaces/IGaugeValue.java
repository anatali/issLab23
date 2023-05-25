package it.unibo.radar.interfaces;
 

public interface IGaugeValue {

	  /**
	   * @return The default representation of the underlying it.unibo
	   *         .dronesystem.gauge value.
	   */
	  public abstract String getDefRep();

	  /**
	   * Check if the gauge value is equal to the provided obj.
	   * 
	   * @param obj
	   *          Object to be compared.
	   * @return true if the underlying gauge value is equal to obj,
	   *         otherwise false.
	   */
	  @Override
	  public abstract boolean equals(final Object obj);
}