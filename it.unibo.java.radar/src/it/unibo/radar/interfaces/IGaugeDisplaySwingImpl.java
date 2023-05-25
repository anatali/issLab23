package it.unibo.radar.interfaces;

import javax.swing.JPanel;

 
public interface IGaugeDisplaySwingImpl extends IGaugeDisplayImpl {

  /**
   * @return The main panel for the underlying gauge display.
   */
  public abstract JPanel getMainPanel();
}
