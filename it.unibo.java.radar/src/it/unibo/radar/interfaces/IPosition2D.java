package it.unibo.radar.interfaces;
 
public interface IPosition2D extends IGaugeValue, Comparable<IPosition2D> {
  public double getX();
  public double getY();
  
}
