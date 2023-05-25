package it.unibo.radar.interfaces;
 
public interface IDistance extends IGaugeValue, Comparable<IDistance> {
  public abstract double getFloatRep();
}
