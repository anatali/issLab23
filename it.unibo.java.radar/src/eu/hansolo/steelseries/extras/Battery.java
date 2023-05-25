package eu.hansolo.steelseries.extras;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class Battery extends javax.swing.JComponent
{
    // <editor-fold defaultstate="collapsed" desc="Variable declaration">
    private static final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    private final java.awt.Rectangle INNER_BOUNDS; 
    private int value = 0;
    private boolean initialized;
    private java.awt.image.BufferedImage batteryImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private final java.awt.Color FULL_BORDER = new java.awt.Color(0x79A24B);
    private final java.awt.Color HALF_BORDER = new java.awt.Color(0xDBA715);
    private final java.awt.Color EMPTY_BORDER = new java.awt.Color(0xB11902);
    private final java.awt.Color FULL_DARK = new java.awt.Color(0xA3D866);
    private final java.awt.Color FULL_LIGHT = new java.awt.Color(0xDFE956);
    private final java.awt.Color HALF_DARK = new java.awt.Color(0xE4BD20);
    private final java.awt.Color HALF_LIGHT = new java.awt.Color(0xF6F49D);
    private final java.awt.Color EMPTY_DARK = new java.awt.Color(0xC62705);
    private final java.awt.Color EMPTY_LIGHT = new java.awt.Color(0xF67930);
    private eu.hansolo.steelseries.tools.GradientWrapper borderGradient;
    private eu.hansolo.steelseries.tools.GradientWrapper liquidGradientDark;
    private eu.hansolo.steelseries.tools.GradientWrapper liquidGradientLight;
    private final transient java.awt.event.ComponentListener COMPONENT_LISTENER = new java.awt.event.ComponentAdapter() 
    {
        @Override
        public void componentResized(java.awt.event.ComponentEvent event)
        {
            //init(getWidth(), getHeight());

            //repaint(INNER_BOUNDS);

            //****************//
            java.awt.Container parent = getParent();
            if ((parent != null) && (parent.getLayout() == null))
            {        
                setSize(getWidth(), getHeight());
            }
            else
            {
                setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
            }
                                                            
            calcInnerBounds();

            init(INNER_BOUNDS.width, INNER_BOUNDS.height);
            revalidate();
            repaint(INNER_BOUNDS);

        }
    };
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Battery()
    {
        super();
        addComponentListener(COMPONENT_LISTENER);
        INNER_BOUNDS = new java.awt.Rectangle(getPreferredSize()); 
        initialized = false;                        
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);       
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Initialization">
    private void init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1)
        {
            return;
        }
        
        if (batteryImage != null)
        {
            batteryImage.flush();
        }
        batteryImage = create_BATTERY_Image(WIDTH, HEIGHT, value);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Visualization">
    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        if (!initialized)
        {
            return;
        }
        
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);        

        G2.translate(INNER_BOUNDS.x, INNER_BOUNDS.y); 
        
        G2.drawImage(batteryImage, 0, 0, null);
        
        G2.translate(-INNER_BOUNDS.x, -INNER_BOUNDS.y); 
        
        G2.dispose();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    
    /**
     * Returns the value of the battery as integer (0 - 100)
     * @return the value of the battery as integer (0 - 100)
     */public int getValue()
    {
        return value;
    }
    
    /**
     * Sets the current charge of the battery as integer from 0 - 100
     * @param VALUE 
     */ 
    public void setValue(final int VALUE)
    {
        value = VALUE < 0 ? 0 : (VALUE > 100 ? 100 : VALUE);        
        init(getWidth(), getHeight());
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the current component as buffered image.
     * To save this buffered image as png you could use for example:
     * File file = new File("image.png");
     * ImageIO.write(Image, "png", file);
     * @return the current component as buffered image
     */
    public java.awt.image.BufferedImage getAsImage()
    {
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(getWidth(), getHeight(), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        paintAll(G2);
        G2.dispose();
        return IMAGE;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Image related">
    /**
     * Returns a buffered image that represents a battery 
     * @param WIDTH
     * @param HEIGHT     
     * @return a buffered image that represents a battery
     */
    private java.awt.image.BufferedImage create_BATTERY_Image(final int WIDTH, final int HEIGHT, final int VALUE)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
               
        // Background
        final java.awt.geom.GeneralPath BATTERY = new java.awt.geom.GeneralPath();
        BATTERY.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        BATTERY.moveTo(IMAGE_WIDTH * 0.025, IMAGE_HEIGHT * 0.05555555555555555);
        BATTERY.lineTo(IMAGE_WIDTH * 0.9, IMAGE_HEIGHT * 0.05555555555555555);
        BATTERY.lineTo(IMAGE_WIDTH * 0.9, IMAGE_HEIGHT * 0.9444444444444444);
        BATTERY.lineTo(IMAGE_WIDTH * 0.025, IMAGE_HEIGHT * 0.9444444444444444);
        BATTERY.lineTo(IMAGE_WIDTH * 0.025, IMAGE_HEIGHT * 0.05555555555555555);
        BATTERY.closePath();
        BATTERY.moveTo(IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 0.0);
        BATTERY.lineTo(IMAGE_WIDTH * 0.0, IMAGE_HEIGHT * 0.0);
        BATTERY.lineTo(IMAGE_WIDTH * 0.0, IMAGE_HEIGHT * 1.0);
        BATTERY.lineTo(IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 1.0);
        BATTERY.lineTo(IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 0.7222222222222222);
        BATTERY.curveTo(IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 0.7222222222222222, IMAGE_WIDTH * 0.975, IMAGE_HEIGHT * 0.7222222222222222, IMAGE_WIDTH * 0.975, IMAGE_HEIGHT * 0.7222222222222222);
        BATTERY.curveTo(IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.7222222222222222, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.6666666666666666, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.6666666666666666);
        BATTERY.curveTo(IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.6666666666666666, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.3333333333333333, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.3333333333333333);
        BATTERY.curveTo(IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.3333333333333333, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.2777777777777778, IMAGE_WIDTH * 0.975, IMAGE_HEIGHT * 0.2777777777777778);
        BATTERY.curveTo(IMAGE_WIDTH * 0.975, IMAGE_HEIGHT * 0.2777777777777778, IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 0.2777777777777778, IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 0.2777777777777778);
        BATTERY.lineTo(IMAGE_WIDTH * 0.925, IMAGE_HEIGHT * 0.0);
        BATTERY.closePath();
        final java.awt.geom.Point2D BATTERY_START = new java.awt.geom.Point2D.Double(0, BATTERY.getBounds2D().getMinY() );
        final java.awt.geom.Point2D BATTERY_STOP = new java.awt.geom.Point2D.Double(0, BATTERY.getBounds2D().getMaxY() );
        final float[] BATTERY_FRACTIONS = 
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] BATTERY_COLORS = 
        {
            new java.awt.Color(255, 255, 255, 255),
            new java.awt.Color(126, 126, 126, 255)
        };
        final java.awt.LinearGradientPaint BATTERY_GRADIENT = new java.awt.LinearGradientPaint(BATTERY_START, BATTERY_STOP, BATTERY_FRACTIONS, BATTERY_COLORS);
        G2.setPaint(BATTERY_GRADIENT);
        G2.fill(BATTERY);

        // Main
        final java.awt.geom.Rectangle2D BORDER = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.025, IMAGE_WIDTH * 0.025, IMAGE_WIDTH * 0.875 * (VALUE / 100.0), IMAGE_HEIGHT * 0.88888888888888);        
        final float[] BORDER_FRACTIONS = 
        {
            0.0f, 
            0.40f,
            1.0f
        };
        final java.awt.Color[] BORDER_COLORS =
        {
            EMPTY_BORDER,
            HALF_BORDER,
            FULL_BORDER
        };
        borderGradient = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(0,0), new java.awt.geom.Point2D.Double(100,0), BORDER_FRACTIONS, BORDER_COLORS);        
        G2.setPaint(borderGradient.getColorAt(VALUE / 100f));
        G2.fill(BORDER);
        
        final java.awt.geom.Rectangle2D LIQUID = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.05, IMAGE_WIDTH * 0.05, IMAGE_WIDTH * 0.85 * (VALUE / 100.0), IMAGE_HEIGHT * 0.77777777777777);        
        final java.awt.geom.Point2D LIQUID_START = new java.awt.geom.Point2D.Double(IMAGE_WIDTH * 0.05, 0);
        final java.awt.geom.Point2D LIQUID_STOP = new java.awt.geom.Point2D.Double(IMAGE_WIDTH * 0.875, 0);
        final float[] LIQUID_FRACTIONS = 
        {
            0.0f,
            0.5f,
            1.0f
        };
        final java.awt.Color[] LIQUID_COLORS_DARK = 
        {
            EMPTY_DARK,
            HALF_DARK,
            FULL_DARK
        };
        final java.awt.Color[] LIQUID_COLORS_LIGHT = 
        {
            EMPTY_LIGHT,
            HALF_LIGHT,
            FULL_LIGHT
        };        
        final float[] LIQUID_GRADIENT_FRACTIONS =
        {
            0.0f,
            0.4f,
            1.0f
        };
        liquidGradientDark = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(0,0), new java.awt.geom.Point2D.Double(100,0), LIQUID_GRADIENT_FRACTIONS, LIQUID_COLORS_DARK);        
        liquidGradientLight = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(0,0), new java.awt.geom.Point2D.Double(100,0), LIQUID_GRADIENT_FRACTIONS, LIQUID_COLORS_LIGHT);        
        final java.awt.Color[] LIQUID_COLORS =
        {
            liquidGradientDark.getColorAt(VALUE / 100f),
            liquidGradientLight.getColorAt(VALUE / 100f),
            liquidGradientDark.getColorAt(VALUE / 100f)
        };
        final java.awt.LinearGradientPaint LIQUID_GRADIENT = new java.awt.LinearGradientPaint(LIQUID_START, LIQUID_STOP, LIQUID_FRACTIONS, LIQUID_COLORS);
        G2.setPaint(LIQUID_GRADIENT);
        G2.fill(LIQUID);
                        
        // Foreground
        final java.awt.geom.Rectangle2D HIGHLIGHT = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.025, IMAGE_WIDTH * 0.025, IMAGE_WIDTH * 0.875, IMAGE_HEIGHT * 0.44444444444444);
        final java.awt.geom.Point2D HIGHLIGHT_START = new java.awt.geom.Point2D.Double(0, HIGHLIGHT.getBounds2D().getMinY() );
        final java.awt.geom.Point2D HIGHLIGHT_STOP = new java.awt.geom.Point2D.Double(0, HIGHLIGHT.getBounds2D().getMaxY() );                
        final float[] HIGHLIGHT_FRACTIONS = 
        {
            0.0f,            
            1.0f
        };
        final java.awt.Color[] HIGHLIGHT_COLORS = 
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.8f)            
        };
        
        final java.awt.LinearGradientPaint HIGHLIGHT_GRADIENT = new java.awt.LinearGradientPaint(HIGHLIGHT_START, HIGHLIGHT_STOP, HIGHLIGHT_FRACTIONS, HIGHLIGHT_COLORS);
        G2.setPaint(HIGHLIGHT_GRADIENT);
        G2.fill(HIGHLIGHT);
        
        G2.dispose();

        return IMAGE;
    }
    // </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc="Size related">
    /**
     * Calculates the rectangle that specifies the area that is available
     * for painting the gauge. This means that if the component has insets
     * that are larger than 0, these will be taken into account.
     */
    private void calcInnerBounds()
    {
        final java.awt.Insets INSETS = getInsets();                        
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, (getWidth() - INSETS.left - INSETS.right), (getHeight() - INSETS.top - INSETS.bottom));
    }
    
    @Override
    public java.awt.Dimension getMinimumSize()
    {
        return new java.awt.Dimension(40, 18);
    }

    @Override
    public void setPreferredSize(final java.awt.Dimension DIM)
    {                
        super.setPreferredSize(new java.awt.Dimension(DIM.width, (int) (0.45 * DIM.width)));
        calcInnerBounds();
        init(DIM.width, (int) (0.45 * DIM.width));        
        initialized = true;
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final int WIDTH, final int HEIGHT)
    {
        super.setSize(WIDTH, (int) (0.45 * WIDTH));
        calcInnerBounds();
        init(WIDTH, (int) (0.45 * WIDTH));        
        initialized = true;
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final java.awt.Dimension DIM)
    {
        super.setPreferredSize(new java.awt.Dimension(DIM.width, (int) (0.45 * DIM.width)));
        calcInnerBounds();
        init(DIM.width, (int) (0.45 * DIM.width));        
        initialized = true;
        revalidate();
        repaint();
    }
    
     @Override
    public void setBounds(final java.awt.Rectangle BOUNDS)
    {
        super.setBounds(new java.awt.Rectangle(BOUNDS.x, BOUNDS.y, BOUNDS.width, (int) (0.45 * BOUNDS.width)));
        calcInnerBounds();
        init(BOUNDS.width, (int) (0.45 * BOUNDS.width));        
        initialized = true;
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        super.setBounds(X, Y, WIDTH, (int) (0.45 * WIDTH));
        calcInnerBounds();
        init(WIDTH, (int) (0.45 * WIDTH));        
        initialized = true;
        revalidate();
        repaint();
    }
    // </editor-fold>
    
    @Override
    public String toString()
    {
        return "Battery";
    }
}
