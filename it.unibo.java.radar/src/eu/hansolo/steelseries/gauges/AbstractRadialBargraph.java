package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public abstract class AbstractRadialBargraph extends AbstractRadial
{
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">        
    private final float[] LED_FRACTIONS =
    {
        0.0f,
        1.0f
    };
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public AbstractRadialBargraph()
    {
        super();
        setLedPosition(0.453271028, 0.7);
    }        
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getter/Setter">
    /**
     * Returns the enum colordef that is defined for the current bargraph
     * @return enum colordef that represents the current bargraph color
     */
    public eu.hansolo.steelseries.tools.ColorDef getBarGraphColor()
    {
        return getModel().getValueColor();        
    }

    /**
     * Sets the current bargraph color to the given enum colordef
     * @param BARGRAPH_COLOR
     */
    public void setBarGraphColor(final eu.hansolo.steelseries.tools.ColorDef BARGRAPH_COLOR)
    {
        getModel().setValueColor(BARGRAPH_COLOR);
        init(getInnerBounds().width, getInnerBounds().width);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color that will be used to calculate the custom bargraph color
     * @return the color that will be used to calculate the custom bargraph color
     */
    public java.awt.Color getCustomBargraphColor()
    {
        return getModel().getCustomValueColor();        
    }
    
    /**
     * Sets the color that will be used to calculate the custom bargraph color
     * @param COLOR 
     */
    public void setCustomBarGraphColor(final java.awt.Color COLOR)
    {
        getModel().setCustomValueColorObject(new eu.hansolo.steelseries.tools.CustomColorDef(COLOR));        
        init(getInnerBounds().width, getInnerBounds().width);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the object that represents holds the custom bargraph color
     * @return the object that represents the custom bargraph color
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomBarGraphColorObject()
    {
        return getModel().getCustomValueColorObject();        
    }
    
    /**
     * Returns true if the peak value is visible
     * @return true if the park value is visible
     */
    public boolean isPeakValueEnabled()
    {
        return getModel().isPeakValueVisible();        
    }

    /**
     * Enables/Disables the visibility of the peak value
     * @param PEAK_VALUE_ENABLED
     */
    public void setPeakValueEnabled(final boolean PEAK_VALUE_ENABLED)
    {
        getModel().setPeakValueVisible(PEAK_VALUE_ENABLED);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Image creation methods">
    /**
     * Returns the bargraph track image
     * with the given with and height.
     * @param WIDTH
     * @param START_ANGLE
     * @param ANGLE_EXTEND
     * @param APEX_ANGLE
     * @param BARGRAPH_OFFSET
     * @return buffered image containing the bargraph track image
     */
    protected java.awt.image.BufferedImage create_BARGRAPH_TRACK_Image(final int WIDTH, final double START_ANGLE, final double ANGLE_EXTEND, final double APEX_ANGLE, final double BARGRAPH_OFFSET)
    {        
        return create_BARGRAPH_TRACK_Image(WIDTH, START_ANGLE, ANGLE_EXTEND, APEX_ANGLE, BARGRAPH_OFFSET, null);
    }
    
    /**
     * Returns the bargraph track image
     * with the given with and height.
     * @param WIDTH
     * @param START_ANGLE
     * @param ANGLE_EXTEND
     * @param APEX_ANGLE
     * @param BARGRAPH_OFFSET
     * @param image 
     * @return buffered image containing the bargraph track image
     */
    protected java.awt.image.BufferedImage create_BARGRAPH_TRACK_Image(final int WIDTH, final double START_ANGLE, final double ANGLE_EXTEND, final double APEX_ANGLE, final double BARGRAPH_OFFSET, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        if (image == null)
        {
            image = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        // Create led track
        final java.awt.geom.Arc2D BACK = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
        BACK.setFrame(IMAGE_WIDTH * 0.1074766355, IMAGE_HEIGHT * 0.1074766355, IMAGE_WIDTH * 0.785046729, IMAGE_HEIGHT * 0.785046729);
        BACK.setAngleStart(START_ANGLE + 2);
        BACK.setAngleExtent(ANGLE_EXTEND - 5);

        final java.awt.geom.Ellipse2D BACK_SUB = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.1822429907, IMAGE_HEIGHT * 0.1822429907, IMAGE_WIDTH * 0.6355140187, IMAGE_HEIGHT * 0.6355140187);

        final java.awt.geom.Area LED_TRACK_FRAME = new java.awt.geom.Area(BACK);
        LED_TRACK_FRAME.subtract(new java.awt.geom.Area(BACK_SUB));

        final java.awt.geom.Point2D LED_TRACK_FRAME_START = new java.awt.geom.Point2D.Double(0, LED_TRACK_FRAME.getBounds2D().getMinY() );
        final java.awt.geom.Point2D LED_TRACK_FRAME_STOP = new java.awt.geom.Point2D.Double(0, LED_TRACK_FRAME.getBounds2D().getMaxY() );
        final float[] LED_TRACK_FRAME_FRACTIONS =
        {
            0.0f,
            0.22f,
            0.76f,
            1.0f
        };
        final java.awt.Color[] LED_TRACK_FRAME_COLORS =
        {
            new java.awt.Color(0, 0, 0, 255),
            new java.awt.Color(51, 51, 51, 255),
            new java.awt.Color(51, 51, 51, 255),
            new java.awt.Color(100, 100, 100, 255)
        };
        final java.awt.LinearGradientPaint LED_TRACK_FRAME_GRADIENT = new java.awt.LinearGradientPaint(LED_TRACK_FRAME_START, LED_TRACK_FRAME_STOP, LED_TRACK_FRAME_FRACTIONS, LED_TRACK_FRAME_COLORS);
        G2.setPaint(LED_TRACK_FRAME_GRADIENT);
        G2.fill(LED_TRACK_FRAME);

        final java.awt.geom.Arc2D FRONT = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
        FRONT.setFrame(IMAGE_WIDTH * 0.1121495327, IMAGE_HEIGHT * 0.1121495327, IMAGE_WIDTH * 0.7803738318, IMAGE_HEIGHT * 0.7803738318);
        FRONT.setAngleStart(START_ANGLE);
        FRONT.setAngleExtent(ANGLE_EXTEND);

        final java.awt.geom.Ellipse2D FRONT_SUB = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.1822429907, IMAGE_HEIGHT * 0.1822429907, IMAGE_WIDTH * 0.6448598131, IMAGE_HEIGHT * 0.6448598131);

        final java.awt.geom.Area LED_TRACK_MAIN = new java.awt.geom.Area(BACK);
        LED_TRACK_MAIN.subtract(new java.awt.geom.Area(FRONT_SUB));

        final java.awt.geom.Point2D LED_TRACK_MAIN_START = new java.awt.geom.Point2D.Double(0, LED_TRACK_MAIN.getBounds2D().getMinY() );
        final java.awt.geom.Point2D LED_TRACK_MAIN_STOP = new java.awt.geom.Point2D.Double(0, LED_TRACK_MAIN.getBounds2D().getMaxY() );
        final float[] LED_TRACK_MAIN_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] LED_TRACK_MAIN_COLORS =
        {
            new java.awt.Color(17, 17, 17, 255),
            new java.awt.Color(51, 51, 51, 255)
        };
        final java.awt.LinearGradientPaint LED_TRACK_MAIN_GRADIENT = new java.awt.LinearGradientPaint(LED_TRACK_MAIN_START, LED_TRACK_MAIN_STOP, LED_TRACK_MAIN_FRACTIONS, LED_TRACK_MAIN_COLORS);
        G2.setPaint(LED_TRACK_MAIN_GRADIENT);
        G2.fill(LED_TRACK_MAIN);

        // Draw the inactive leds
        final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double(WIDTH / 2.0, WIDTH / 2.0);
        final java.awt.geom.Rectangle2D LED = new java.awt.geom.Rectangle2D.Double(WIDTH * 0.1168224299, WIDTH * 0.4859813084, WIDTH * 0.06074766355140187, WIDTH * 0.023364486);
        final java.awt.geom.Point2D LED_CENTER = new java.awt.geom.Point2D.Double(LED.getCenterX(), LED.getCenterY());

        final java.awt.Color[] LED_COLORS = new java.awt.Color[]
        {
            new java.awt.Color(60, 60, 60, 255),
            new java.awt.Color(50, 50, 50, 255)
        };
        final java.awt.RadialGradientPaint LED_GRADIENT = new java.awt.RadialGradientPaint(LED_CENTER, (float)(0.030373831775700934 * IMAGE_WIDTH), LED_FRACTIONS, LED_COLORS);
        G2.setPaint(LED_GRADIENT);

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        for (double angle = 0 ; angle <= APEX_ANGLE ; angle += 5.0)
        {
            G2.rotate(Math.toRadians(angle + BARGRAPH_OFFSET), CENTER.getX(), CENTER.getY());
            G2.fill(LED);
            G2.setTransform(OLD_TRANSFORM);
        }

        G2.dispose();

        return image;
    }
    // </editor-fold>
}
