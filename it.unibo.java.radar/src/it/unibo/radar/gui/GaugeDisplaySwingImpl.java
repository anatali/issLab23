package it.unibo.radar.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.radar.interfaces.IGaugeDisplaySwingImpl;
import it.unibo.radar.interfaces.IGaugeViewSwingImpl;
 
public class GaugeDisplaySwingImpl implements IGaugeDisplaySwingImpl {

  private final String                    name;

   private final IGaugeViewSwingImpl	  tachometerView;
 
  private final JPanel                    mainPanel;
  private final JLabel                    titleLbl;
  private final JPanel                    gaugesPanel;
  private final JLabel                    statusLbl;

  /* { Constructors and factories */

  public GaugeDisplaySwingImpl(final String name,
        final IGaugeViewSwingImpl tachometerView){
 
    this.name = name;
     this.tachometerView = tachometerView;
 
    this.mainPanel = new JPanel();
    this.titleLbl = new JLabel();
    this.gaugesPanel = new JPanel();
    this.statusLbl = new JLabel();

    this.setupTitleLbl();
    this.setupGaugesPanel();
    this.setupStatusLbl();
    this.setupMainPanel();
  }

 
  public static GaugeDisplaySwingImpl create(final String name,
 	      final IGaugeViewSwingImpl tachometerView ) {
	    return new GaugeDisplaySwingImpl(name,  tachometerView );
	  }


  /* { Setup */

  protected void setupTitleLbl() {
    this.titleLbl.setFont(new Font("Arial", Font.BOLD, 16));
    this.titleLbl.setText(this.name);
  }

  protected void setupStatusLbl() {
    this.statusLbl.setFont(new Font("Arial", Font.ITALIC, 12));
    this.statusLbl.setText("Gauge Display initialized.");
  }

  protected void setupGaugesPanel() {
    this.getGaugesPanel().setLayout(new GridBagLayout());

     this.addComponent(this.getGaugesPanel(),
        this.tachometerView.getMainPanel(), 1, 0, 2, 3, 1.0, 0.1);
   }

  protected void setupMainPanel() {
    this.getMainPanel().setLayout(new BorderLayout());
    this.getMainPanel().add(this.getTitleLbl(), BorderLayout.NORTH);
    this.getMainPanel().add(this.getGaugesPanel(), BorderLayout.CENTER);
    this.getMainPanel().add(this.getStatusLbl(), BorderLayout.SOUTH);
  }

  /* } */

  /* { {IGaugeDisplaySwingImpl} implementation */

  /**
   * @see IGaugeDisplaySwingImpl#getMainPanel()
   */
  @Override
  public JPanel getMainPanel() {
    return this.mainPanel;
  }

  /**
   * @see IGaugeDisplaySwingImpl#setStatus(GaugeDisplayStatus)
   */
  @Override
  public void setStatus(final GaugeDisplayStatus status) {
    switch (status) {
      case CONNECTED_TO_THE_SYSTEM:
        this.setStatus("The display has been connected to the system.");
        break;
      case DISCONNECTED_FROM_THE_SYSTEM:
        this.setStatus("The display has been disconnected from the system.");
        break;
    }
  }

  /* } */

  /* { Utility */

  /**
   * Getter for this.gaugesPanel
   * 
   * @return The gauges panel
   */
  protected JPanel getGaugesPanel() {
    return this.gaugesPanel;
  }

  /**
   * Getter for this.titleLbl
   * 
   * @return The title label
   */
  protected JLabel getTitleLbl() {
    return this.titleLbl;
  }

  public JLabel getStatusLbl() {
    return statusLbl;
  }

  public void setStatus(String txt) {
    this.statusLbl.setText(txt);
  }

  /**
   * Add the provided component in the provided container
   * 
   * @param container
   *          The container in which the component will be added
   * @param component
   *          The component row
   */
  protected void addComponent(Container container, Component component,
      int rowIdx, int colIdx, int rowCount, int colCount,
      double horizontalWeight, double verticalWeight) {
    Insets insets = new Insets(0, 0, 0, 0);
    GridBagConstraints gbc = new GridBagConstraints(colIdx, rowIdx, colCount,
        rowCount, horizontalWeight, verticalWeight, GridBagConstraints.NORTH,
        GridBagConstraints.BOTH, insets, 0, 0);
    container.add(component, gbc);
  }

  /* } */

}
