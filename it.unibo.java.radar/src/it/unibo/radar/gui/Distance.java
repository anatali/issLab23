package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IDistance;

public class Distance extends GaugeValue implements IDistance {
private final double rep;
  protected Distance(final double rep) {
     this.rep = rep;
  }
  public static Distance create(final double rep) {
    return new Distance(rep);
  }
  @Override
  public double getFloatRep() {
    return this.rep;
  }
  @Override
  public String getDefRep() {
    return ""+this.rep ;
  }
  @Override
  public boolean equals(Object obj) {
	   return false; //TODO
  }
  @Override
  public int compareTo(final IDistance revol) {
    return 0; //TODO
  }
  @Override
  public int hashCode() {
    return 0;	//TODO
  }
  @Override
  public String toString() {
    return String.format("Radar [value=%s]", this.getDefRep());
  }
}
