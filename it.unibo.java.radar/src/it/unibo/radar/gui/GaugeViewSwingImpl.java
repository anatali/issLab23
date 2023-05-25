package it.unibo.radar.gui;
 
import it.unibo.radar.interfaces.IGaugeViewSwingImpl;
import javax.swing.JPanel;
import java.awt.*;
 
public abstract class GaugeViewSwingImpl implements IGaugeViewSwingImpl {
  private final String name;
  private final JPanel mainPanel;

  /* { Constructors and factories */

  protected GaugeViewSwingImpl(final String name) {
    this.name = name;
    this.mainPanel = new JPanel();
    this.mainPanel.setLayout(new GridBagLayout());
  }

  /* } */

  /* { {IGaugeViewSwingImpl} implementation */

  /**
   * @see IGaugeViewSwingImpl#getMainPanel()
   */
  @Override
  public JPanel getMainPanel() {
    return this.mainPanel;
  }

  /* } */

  /* { Utility */

  /**
   * @return The view name.
   */
  protected String getName() {
    return this.name;
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
