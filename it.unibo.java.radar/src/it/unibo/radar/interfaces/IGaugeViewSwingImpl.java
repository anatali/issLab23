package it.unibo.radar.interfaces;

import it.unibo.radar.gui.ViewSwingFavor;

import javax.swing.JPanel;
 
public interface IGaugeViewSwingImpl extends IGaugeViewImpl {

  /**
   * @return The main panel for the view.
   */
  public abstract JPanel getMainPanel();

  /**
   * @return The view favor.
   */
  public abstract ViewSwingFavor getFavor();
}
