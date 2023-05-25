package it.unibo.radar.gui;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import it.unibo.radar.interfaces.IDashSwing;
import java.awt.*;
import java.awt.event.WindowEvent;

 
public abstract class DashSwing implements IDashSwing {
  protected final String name;
  protected JFrame       frame;

  /* { Constructors and factories */

  protected DashSwing(final String name) {
    this.name = name;
    this.frame = new JFrame();
    this.setupFrame();
  }

  /* } */

  /* { Setup */

  /**
   * Setup the underlying frame.
   */
  protected void setupFrame() {
    this.frame.setTitle(this.name);
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.frame.getContentPane().setLayout(new GridBagLayout());   
    
  }

  /* } */

  /* { Utility */

  /**
   * Start the frame, making it visible.
   */
  protected void start(int w, int h) {
    this.locateFrame();
    this.frame.setSize(w,h);  
    this.frame.setVisible(true);
  }

  /**
   * Stop the frame, closing the window.
   */
  protected void stop() {
    this.frame.setVisible(false);
    WindowEvent event = new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
  }

  /**
   * Position the frame correctly.
   */
  protected void locateFrame() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Point location = new Point(
        (screenSize.width - this.frame.getSize().width) / 2,
        (screenSize.height - this.frame.getSize().height) / 2);
    this.frame.setLocation(location);
  }

  /**
   * Add the provided component in the frame.
   * 
   * @param component
   *          The component
   */
  public void addComponent(Component component, int rowIdx, int colIdx,
      int rowCount, int colCount, double horizontalWeight, double verticalWeight) {
    Insets insets = new Insets(0, 0, 0, 0);
    GridBagConstraints gbc = new GridBagConstraints(colIdx, rowIdx, colCount,
        rowCount, horizontalWeight, verticalWeight, GridBagConstraints.NORTH,
        GridBagConstraints.BOTH, insets, 0, 0);
    this.frame.getContentPane().add(component, gbc);
  }
 

}
