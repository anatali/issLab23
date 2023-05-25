package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IPosition2D;

public class Position2D extends GaugeValue implements IPosition2D {
private final double r;
private final double arg;
private boolean polar = false;

  protected Position2D(final double x, final double y) {
     this.r = x;
     this.arg = y;
     polar = true;
  }
  public static Position2D createPolar(final double r, final double arg) {
    return new Position2D(r,arg);
  }
   @Override
  public String getDefRep() {
	String mode = polar ? "polar" : "cartesian";
    return "p("+mode+","+r+","+arg+")";
  }
  @Override
  public boolean equals(Object obj) {
	   return false; //TODO
  }
  @Override
  public int compareTo(final IPosition2D p) {
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
 
@Override
public double getX() {
 	return r;
}
@Override
public double getY() {
 	return arg;
}
}
