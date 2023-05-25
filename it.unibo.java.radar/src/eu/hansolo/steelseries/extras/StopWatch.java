package eu.hansolo.steelseries.extras;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class StopWatch extends eu.hansolo.steelseries.gauges.AbstractRadial implements java.awt.event.ActionListener
{    
    private static final double ANGLE_STEP = 6;    
    private final javax.swing.Timer CLOCK_TIMER = new javax.swing.Timer(100, this);    
    private double minutePointerAngle = 0;
    private double secondPointerAngle = 0;
    private final java.awt.Rectangle INNER_BOUNDS;
    // Background   
    private final java.awt.geom.Point2D MAIN_CENTER = new java.awt.geom.Point2D.Double();    
    private final java.awt.geom.Point2D SMALL_CENTER = new java.awt.geom.Point2D.Double();
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    private java.awt.image.BufferedImage smallTickmarkImage;    
    private java.awt.image.BufferedImage mainPointerImage;
    private java.awt.image.BufferedImage mainPointerShadowImage;
    private java.awt.image.BufferedImage smallPointerImage;
    private java.awt.image.BufferedImage smallPointerShadowImage;
    private java.awt.image.BufferedImage disabledImage;          
    private long start = 0;
    private long currentMilliSeconds = 0;
    private long minutes = 0;    
    private long seconds = 0;
    private long milliSeconds = 0;    
    private boolean running = false;
    private boolean flatNeedle = false;
    private final java.awt.Color SHADOW_COLOR = new java.awt.Color(0.0f, 0.0f, 0.0f, 0.65f);
    
    
    public StopWatch()
    {
        super();         
        INNER_BOUNDS = new java.awt.Rectangle(getPreferredSize());
        init(getInnerBounds().width, getInnerBounds().height);   
        setPointerColor(eu.hansolo.steelseries.tools.ColorDef.BLACK);
        setBackgroundColor(eu.hansolo.steelseries.tools.BackgroundColor.LIGHT_GRAY);        
    }

    @Override
    public eu.hansolo.steelseries.gauges.AbstractGauge init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        // Create Background Image
        if (bImage != null)
        {
            bImage.flush();
        }
        bImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        
        // Create Foreground Image
        if (fImage != null)
        {
            fImage.flush();
        }
        fImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        
        if (isFrameVisible())
        {
            switch (getFrameType())
            {                                    
                case SQUARE:
                    FRAME_FACTORY.createLinearFrame(WIDTH, WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);
                    break;
                case ROUND:
                    
                default:
                    FRAME_FACTORY.createRadialFrame(WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);
                    break;
            }
        }        
                
        if (isBackgroundVisible())
        {
            switch(getFrameType())
            {                            
                case SQUARE:
                    BACKGROUND_FACTORY.createLinearBackground(WIDTH, WIDTH, getBackgroundColor(), getCustomBackground(), bImage);
                    break;
                case ROUND:

                default:                
                    BACKGROUND_FACTORY.createRadialBackground(WIDTH, getBackgroundColor(), getCustomBackground(), bImage);
                    break;
            }
        } 
                
        create_TICKMARKS_Image(WIDTH, 60f, 0.075, 0.14, bImage);
        
        if (smallTickmarkImage != null)
        {
            smallTickmarkImage.flush();
        }
        smallTickmarkImage = create_TICKMARKS_Image((int) (0.285 * WIDTH), 30f, 0.095, 0.17, null);
                        
        if (mainPointerImage != null)
        {
            mainPointerImage.flush();
        }
        mainPointerImage = create_MAIN_POINTER_Image(WIDTH);
        
        if (mainPointerShadowImage != null)
        {
            mainPointerShadowImage.flush();
        }
        mainPointerShadowImage = create_MAIN_POINTER_SHADOW_Image(WIDTH);
        
        if (smallPointerImage != null)
        {
            smallPointerImage.flush();
        }
        smallPointerImage = create_SMALL_POINTER_Image(WIDTH);
        
        if (smallPointerShadowImage != null)
        {
            smallPointerShadowImage.flush();
        }
        smallPointerShadowImage = create_SMALL_POINTER_SHADOW_Image(WIDTH);
                       
        if (isForegroundVisible())
        {            
            switch(getFrameType())
            {
                case SQUARE:
                    FOREGROUND_FACTORY.createLinearForeground(WIDTH, WIDTH, false, bImage);
                    break;
                    
                case ROUND:
                    
                default:
                    FOREGROUND_FACTORY.createRadialForeground(WIDTH, false, getForegroundType(), fImage);
                    break;
            }
        }
        
        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = DISABLED_FACTORY.createRadialDisabled(WIDTH);
        
        return this;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        MAIN_CENTER.setLocation(INNER_BOUNDS.getCenterX(), INNER_BOUNDS.getCenterX());
        SMALL_CENTER.setLocation(INNER_BOUNDS.getCenterX(), INNER_BOUNDS.width * 0.3130841121);

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        // Translate the coordinate system related to the insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);
        
        G2.drawImage(smallTickmarkImage, ((INNER_BOUNDS.width - smallTickmarkImage.getWidth()) / 2), (int) (SMALL_CENTER.getY() - smallTickmarkImage.getHeight() / 2.0), null);        
        
        // Draw the small pointer
        G2.rotate(Math.toRadians(minutePointerAngle + (2 * Math.sin(Math.toRadians(minutePointerAngle)))), SMALL_CENTER.getX(), SMALL_CENTER.getY());
        G2.drawImage(smallPointerShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(Math.toRadians(minutePointerAngle), SMALL_CENTER.getX(), SMALL_CENTER.getY());
        G2.drawImage(smallPointerImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        
        // Draw the main pointer
        G2.rotate(Math.toRadians(secondPointerAngle + (2 * Math.sin(Math.toRadians(secondPointerAngle)))), MAIN_CENTER.getX(), MAIN_CENTER.getY());
        G2.drawImage(mainPointerShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(Math.toRadians(secondPointerAngle), MAIN_CENTER.getX(), MAIN_CENTER.getY());
        G2.drawImage(mainPointerImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        
        // Draw combined foreground image
        G2.drawImage(fImage, 0, 0, null);
        
        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate the coordinate system back to original
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }
        
    /**
     * Returns true if the stopwatch is running
     * @return true if the stopwatch is running
     */
    public boolean isRunning()
    {
        return running;
    }    
    
    /**
     * Start or stop the stopwatch
     * @param RUNNING 
     */
    public void setRunning(final boolean RUNNING)
    {
        running = RUNNING;
        if(RUNNING)
        {
            if (!CLOCK_TIMER.isRunning())
            {
                CLOCK_TIMER.start();
                start = System.currentTimeMillis();
                repaint(INNER_BOUNDS);
            }
        }
        else
        {
            if (CLOCK_TIMER.isRunning())
            {
                CLOCK_TIMER.stop();
            }
        }
    }
    
    /**
     * Starts the stopwatch
     */
    public void start()
    {
        setRunning(true);        
    }
    
    /**
     * Stops the stopwatch
     */
    public void stop()
    {
        setRunning(false);
    }
    
    /**
     * Resets the stopwatch
     */
    public void reset()
    {
        setRunning(false);
        start = 0;
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns a string that contains MIN:SEC:MILLISEC of the measured time
     * @return a string that contains MIN:SEC:MILLISEC of the measured time
     */
    public String getMeasuredTime()
    {
        return (minutes + ":" + seconds + ":" + milliSeconds);
    }
    
    public boolean isFlatNeedle()
    {
        return flatNeedle;
    }
    
    public void setFlatNeedle(final boolean FLAT_NEEDLE)
    {
        flatNeedle = FLAT_NEEDLE;
        init(getWidth(), getWidth());
        repaint(INNER_BOUNDS);
    }
    
    @Override
    public java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(bImage.getWidth() / 2.0 + getInnerBounds().x, bImage.getHeight() / 2.0 + getInnerBounds().y);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return new java.awt.geom.Rectangle2D.Double(bImage.getMinX(), bImage.getMinY(), bImage.getWidth(), bImage.getHeight());
    }
            
    private java.awt.image.BufferedImage create_TICKMARKS_Image(final int WIDTH, final float RANGE, final double TEXT_SCALE, final double TEXT_DISTANCE_FACTOR, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        }
        
        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
        
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        final java.awt.Font STD_FONT = new java.awt.Font("Verdana", 0, (int) (TEXT_SCALE * WIDTH));
        final java.awt.BasicStroke THIN_STROKE = new java.awt.BasicStroke(0.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);        
        final java.awt.BasicStroke MEDIUM_STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);      
        final java.awt.BasicStroke THICK_STROKE = new java.awt.BasicStroke(1.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);      
        final int TEXT_DISTANCE = (int) (TEXT_DISTANCE_FACTOR * WIDTH);        
        final int MIN_LENGTH = (int) (0.025 * WIDTH);
        final int MED_LENGTH = (int) (0.035 * WIDTH);
        final int MAX_LENGTH = (int) (0.045 * WIDTH);        
        final java.awt.Color TEXT_COLOR = getBackgroundColor().LABEL_COLOR;
        final java.awt.Color TICK_COLOR = getBackgroundColor().LABEL_COLOR;

        // Create the ticks itself
        final float RADIUS = IMAGE_WIDTH * 0.4f;
        final java.awt.geom.Point2D IMAGE_CENTER = new java.awt.geom.Point2D.Double(IMAGE_WIDTH / 2.0f, IMAGE_HEIGHT / 2.0f);

        // Draw ticks
        java.awt.geom.Point2D innerPoint;
        java.awt.geom.Point2D outerPoint;
        java.awt.geom.Point2D textPoint = null;        
        java.awt.geom.Line2D tick;
        int counter = 0;
        int numberCounter = 0;
        int tickCounter = 0;

        G2.setFont(STD_FONT);

        double sinValue = 0;
        double cosValue = 0;

        double alpha; // angle for the tickmarks
        final double ALPHA_START = -Math.PI;
        float valueCounter; // value for the tickmarks
        
        final double ANGLE_STEPSIZE = (2 * Math.PI) / (RANGE);
        
        for (alpha = ALPHA_START, valueCounter = 0 ; Float.compare(valueCounter, RANGE + 1) <= 0 ; alpha -= ANGLE_STEPSIZE * 0.1, valueCounter += 0.1f)
        {
            G2.setStroke(THIN_STROKE);
            sinValue = Math.sin(alpha);
            cosValue = Math.cos(alpha);                        

            // tickmark every 2 units
            if (counter % 2 == 0)
            {
                G2.setStroke(THIN_STROKE);
                innerPoint = new java.awt.geom.Point2D.Double(IMAGE_CENTER.getX() + (RADIUS - MIN_LENGTH) * sinValue, IMAGE_CENTER.getY() + (RADIUS - MIN_LENGTH) * cosValue);
                outerPoint = new java.awt.geom.Point2D.Double(IMAGE_CENTER.getX() + RADIUS * sinValue, IMAGE_CENTER.getY() + RADIUS * cosValue);
                // Draw ticks
                G2.setColor(TICK_COLOR);
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);
            }
            
            // Different tickmark every 10 units
            if (counter == 10 || counter == 0)
            {
                G2.setColor(TEXT_COLOR);
                G2.setStroke(MEDIUM_STROKE);                
                outerPoint = new java.awt.geom.Point2D.Double(IMAGE_CENTER.getX() + RADIUS * sinValue, IMAGE_CENTER.getY() + RADIUS * cosValue);
                textPoint = new java.awt.geom.Point2D.Double(IMAGE_CENTER.getX() + (RADIUS - TEXT_DISTANCE + STD_FONT.getSize() / 2f) * sinValue, IMAGE_CENTER.getY() + (RADIUS - TEXT_DISTANCE + STD_FONT.getSize() / 2f) * cosValue + TEXT_DISTANCE / 2.5f);

                // Draw text
                if (numberCounter == 5)
                {
                    final java.awt.font.TextLayout TEXT_LAYOUT = new java.awt.font.TextLayout(String.valueOf(Math.round(valueCounter)), G2.getFont(), RENDER_CONTEXT);
                    final java.awt.geom.Rectangle2D TEXT_BOUNDARY = TEXT_LAYOUT.getBounds();                                
                    
                    if (Float.compare(valueCounter, RANGE) != 0)
                    {                           
                        if (Math.ceil(valueCounter) != 60)
                        {
                            G2.drawString(String.valueOf(Math.round(valueCounter)), (int) (textPoint.getX() - TEXT_BOUNDARY.getWidth() / 2.0), (int) ((textPoint.getY() - TEXT_BOUNDARY.getHeight() / 2.0)));
                        }                                                
                    }                    
                    G2.setStroke(THICK_STROKE);
                    innerPoint = new java.awt.geom.Point2D.Double(IMAGE_CENTER.getX() + (RADIUS - MAX_LENGTH) * sinValue, IMAGE_CENTER.getY() + (RADIUS - MAX_LENGTH) * cosValue);
                    numberCounter = 0;
                }  
                else
                {
                    G2.setStroke(MEDIUM_STROKE);
                    innerPoint = new java.awt.geom.Point2D.Double(IMAGE_CENTER.getX() + (RADIUS - MED_LENGTH) * sinValue, IMAGE_CENTER.getY() + (RADIUS - MED_LENGTH) * cosValue);
                }
                                
                // Draw ticks 
                G2.setColor(TICK_COLOR);
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);
                
                counter = 0;
                tickCounter++;
                numberCounter++;
            }

            counter++;
        }

        G2.dispose();

        return image;
    }
    
    private java.awt.image.BufferedImage create_MAIN_POINTER_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);  
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);        
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);        
        
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        
        final java.awt.geom.GeneralPath STOPWATCHPOINTER = new java.awt.geom.GeneralPath();
        STOPWATCHPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        STOPWATCHPOINTER.moveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.102803738317757);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.46261682242990654, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5373831775700935, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6214953271028038);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6214953271028038);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5373831775700935, IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.5);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.46261682242990654, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.closePath();        
        if (flatNeedle)
        {
            G2.setColor(getPointerColor().MEDIUM);
            G2.fill(STOPWATCHPOINTER);
        }
        else
        {
            final java.awt.geom.Point2D POINTER_START = new java.awt.geom.Point2D.Double(STOPWATCHPOINTER.getBounds2D().getMinX(), 0);
            final java.awt.geom.Point2D POINTER_STOP = new java.awt.geom.Point2D.Double(STOPWATCHPOINTER.getBounds2D().getMaxX(), 0);
            final float[] POINTER_FRACTIONS =
            {
                0.0f,
                0.3888888889f,
                0.5f,
                0.6111111111f,
                1.0f
            };
            final java.awt.Color[] POINTER_COLORS =
            {
                getPointerColor().MEDIUM,
                getPointerColor().MEDIUM,
                getPointerColor().LIGHT,
                getPointerColor().MEDIUM,
                getPointerColor().MEDIUM
            };
            final java.awt.LinearGradientPaint GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
            G2.setPaint(GRADIENT);
            G2.fill(STOPWATCHPOINTER);
            G2.setPaint(getPointerColor().DARK);
            G2.draw(STOPWATCHPOINTER);
        }                

        final java.awt.geom.Ellipse2D SWBRASSRING = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4672897160053253, IMAGE_HEIGHT * 0.4672897160053253, IMAGE_WIDTH * 0.06542053818702698, IMAGE_HEIGHT * 0.06542053818702698);
        final java.awt.geom.Point2D SWBRASSRING_START = new java.awt.geom.Point2D.Double(0, SWBRASSRING.getBounds2D().getMaxY() );
        final java.awt.geom.Point2D SWBRASSRING_STOP = new java.awt.geom.Point2D.Double(0, SWBRASSRING.getBounds2D().getMinY() );
        final float[] SWBRASSRING_FRACTIONS = 
        {
            0.0f,
            0.01f,
            0.99f,
            1.0f
        };
        final java.awt.Color[] SWBRASSRING_COLORS = 
        {
            new java.awt.Color(230, 179, 92, 255),
            new java.awt.Color(230, 179, 92, 255),
            new java.awt.Color(196, 130, 0, 255),
            new java.awt.Color(196, 130, 0, 255)
        };
        final java.awt.LinearGradientPaint SWBRASSRING_GRADIENT = new java.awt.LinearGradientPaint(SWBRASSRING_START, SWBRASSRING_STOP, SWBRASSRING_FRACTIONS, SWBRASSRING_COLORS);
        G2.setPaint(SWBRASSRING_GRADIENT);
        G2.fill(SWBRASSRING);

        final java.awt.geom.Ellipse2D SWRING1 = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.47663551568984985, IMAGE_HEIGHT * 0.47663551568984985, IMAGE_WIDTH * 0.04672896862030029, IMAGE_HEIGHT * 0.04672896862030029);
        final java.awt.geom.Point2D SWRING1_CENTER = new java.awt.geom.Point2D.Double( (0.5 * IMAGE_WIDTH), (0.5 * IMAGE_HEIGHT) );
        final float[] SWRING1_FRACTIONS = 
        {
            0.0f,
            0.19f,
            0.22f,
            0.8f,
            0.99f,
            1.0f
        };
        final java.awt.Color[] SWRING1_COLORS = 
        {
            new java.awt.Color(197, 197, 197, 255),
            new java.awt.Color(197, 197, 197, 255),
            new java.awt.Color(0, 0, 0, 255),
            new java.awt.Color(0, 0, 0, 255),
            new java.awt.Color(112, 112, 112, 255),
            new java.awt.Color(112, 112, 112, 255)
        };
        final java.awt.RadialGradientPaint SWRING1_GRADIENT = new java.awt.RadialGradientPaint(SWRING1_CENTER, (float)(0.02336448598130841 * IMAGE_WIDTH), SWRING1_FRACTIONS, SWRING1_COLORS);
        G2.setPaint(SWRING1_GRADIENT);
        G2.fill(SWRING1);
        
        G2.dispose();
        
        return IMAGE;
    }

    private java.awt.image.BufferedImage create_MAIN_POINTER_SHADOW_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);        
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath STOPWATCHPOINTER = new java.awt.geom.GeneralPath();
        STOPWATCHPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        STOPWATCHPOINTER.moveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.102803738317757);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.46261682242990654, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5373831775700935, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6214953271028038);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6214953271028038);
        STOPWATCHPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5420560747663551);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5373831775700935, IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.5);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.46261682242990654, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.curveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486);
        STOPWATCHPOINTER.closePath();       

        G2.setPaint(SHADOW_COLOR);
        G2.fill(STOPWATCHPOINTER);
        
        G2.dispose();

        return IMAGE;
    }
        
    private java.awt.image.BufferedImage create_SMALL_POINTER_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);        
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath STOPWATCHPOINTERSMALL = new java.awt.geom.GeneralPath();
        STOPWATCHPOINTERSMALL.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        STOPWATCHPOINTERSMALL.moveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.3130841121495327);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.32242990654205606, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.3317757009345794, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3364485981308411);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3364485981308411, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.35046728971962615, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.35046728971962615);
        STOPWATCHPOINTERSMALL.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.35046728971962615);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.35046728971962615, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3364485981308411, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3364485981308411);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3317757009345794, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.32242990654205606, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.3130841121495327);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.3037383177570093, IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.29439252336448596, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2897196261682243);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.20093457943925233, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.20093457943925233);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.20093457943925233, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.29439252336448596, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.3037383177570093, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.3130841121495327);
        STOPWATCHPOINTERSMALL.closePath();  
        if (flatNeedle)
        {
            G2.setColor(getPointerColor().MEDIUM);
            G2.fill(STOPWATCHPOINTERSMALL);
        }
        else
        {        
            final java.awt.geom.Point2D POINTER_START = new java.awt.geom.Point2D.Double(STOPWATCHPOINTERSMALL.getBounds2D().getMinX(), 0);
            final java.awt.geom.Point2D POINTER_STOP = new java.awt.geom.Point2D.Double(STOPWATCHPOINTERSMALL.getBounds2D().getMaxX(), 0);
            final float[] POINTER_FRACTIONS =
            {
                0.0f,
                0.3888888889f,
                0.5f,
                0.6111111111f,
                1.0f
            };
            final java.awt.Color[] POINTER_COLORS =
            {
                getPointerColor().MEDIUM,
                getPointerColor().MEDIUM,
                getPointerColor().LIGHT,
                getPointerColor().MEDIUM,
                getPointerColor().MEDIUM
            };
            final java.awt.LinearGradientPaint GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
            G2.setPaint(GRADIENT);
            G2.fill(STOPWATCHPOINTERSMALL);
            G2.setPaint(getPointerColor().DARK);
            G2.draw(STOPWATCHPOINTERSMALL);
        }                        

        final java.awt.geom.Ellipse2D SWBRASSRINGSMALL = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4813084006309509, IMAGE_HEIGHT * 0.29439252614974976, IMAGE_WIDTH * 0.037383198738098145, IMAGE_HEIGHT * 0.03738316893577576);        
        G2.setColor(new java.awt.Color(0xC48200));
        G2.fill(SWBRASSRINGSMALL);

        final java.awt.geom.Ellipse2D SWRING1SMALL = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4859813153743744, IMAGE_HEIGHT * 0.29906541109085083, IMAGE_WIDTH * 0.02803739905357361, IMAGE_HEIGHT * 0.02803739905357361);        
        G2.setColor(new java.awt.Color(0x999999));
        G2.fill(SWRING1SMALL);

        final java.awt.geom.Ellipse2D SWRING1SMALL0 = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.49065420031547546, IMAGE_HEIGHT * 0.3037383258342743, IMAGE_WIDTH * 0.018691569566726685, IMAGE_HEIGHT * 0.018691569566726685);        
        G2.setColor(java.awt.Color.BLACK);
        G2.fill(SWRING1SMALL0);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_SMALL_POINTER_SHADOW_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath STOPWATCHPOINTERSMALL = new java.awt.geom.GeneralPath();
        STOPWATCHPOINTERSMALL.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        STOPWATCHPOINTERSMALL.moveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.3130841121495327);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.32242990654205606, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.3317757009345794, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3364485981308411);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3364485981308411, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.35046728971962615, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.35046728971962615);
        STOPWATCHPOINTERSMALL.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.35046728971962615);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.35046728971962615, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3364485981308411, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3364485981308411);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3317757009345794, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.32242990654205606, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.3130841121495327);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.3037383177570093, IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.29439252336448596, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2897196261682243);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.20093457943925233, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.20093457943925233);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.20093457943925233, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243);
        STOPWATCHPOINTERSMALL.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.29439252336448596, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.3037383177570093, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.3130841121495327);
        STOPWATCHPOINTERSMALL.closePath();

        G2.setPaint(SHADOW_COLOR);
        G2.fill(STOPWATCHPOINTERSMALL);
        
        G2.dispose();

        return IMAGE;
    }
    
    @Override
    public void calcInnerBounds()
    {
        if (getWidth() - getInsets().left - getInsets().right < getHeight() - getInsets().top - getInsets().bottom)
        {
            //OFFSET_Y = getInsets().top + (int) (((double) (getHeight() - getInsets().top - getInsets().bottom) - (double) (getWidth() - getInsets().left - getInsets().right)) / 2.0);
            INNER_BOUNDS.setBounds(getInsets().left, getInsets().top, getWidth() - getInsets().left - getInsets().right, getHeight() - getInsets().top - getInsets().bottom);
        }
        else
        {
            INNER_BOUNDS.setBounds(getInsets().left + (int) (((double) (getWidth() - getInsets().left - getInsets().right) - (double) (getHeight() - getInsets().top - getInsets().bottom)) / 2.0), getInsets().top, getHeight() - getInsets().top - getInsets().bottom, getHeight() - getInsets().top - getInsets().bottom);
        }
        //innerBounds.setBounds(getInsets().left, getInsets().top, getWidth() - getInsets().left - getInsets().right, getHeight() - getInsets().top - getInsets().bottom);
    }

    @Override
    public java.awt.Rectangle getInnerBounds()
    {
        return INNER_BOUNDS;
    }

    @Override
    public java.awt.Dimension getMinimumSize()
    {        
        return new java.awt.Dimension(200, 200);
    }
    
    @Override
    public void setPreferredSize(final java.awt.Dimension DIM)
    {        
        super.setPreferredSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.width);        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final int WIDTH, final int HEIGHT)
    {
        super.setSize(WIDTH, WIDTH);
        calcInnerBounds();
        init(WIDTH, WIDTH);        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final java.awt.Dimension DIM)
    {
        super.setSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.width);        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final java.awt.Rectangle BOUNDS)
    {
        super.setBounds(BOUNDS);
        calcInnerBounds();
        init(BOUNDS.width, BOUNDS.width);        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        super.setBounds(X, Y, WIDTH, WIDTH);
        calcInnerBounds();
        init(WIDTH, WIDTH);        
        setInitialized(true);
        revalidate();
        repaint();
    }    
    
    @Override
    public void componentResized(java.awt.event.ComponentEvent event)
    {
        final int SIZE = getWidth() < getHeight() ? getWidth() : getHeight();
        setPreferredSize(new java.awt.Dimension(SIZE, SIZE));        

        if (SIZE < getMinimumSize().width || SIZE < getMinimumSize().height)
        {
            setPreferredSize(getMinimumSize());
        }
        calcInnerBounds();

        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        
        revalidate();
        repaint();
    }
   
    @Override
    public void actionPerformed(final java.awt.event.ActionEvent EVENT)
    {
        if (EVENT.getSource().equals(CLOCK_TIMER))
        {           
            currentMilliSeconds = (System.currentTimeMillis() - start);
            secondPointerAngle = (currentMilliSeconds * ANGLE_STEP / 1000);                                                        
            minutePointerAngle = (secondPointerAngle % 1000) / 30;            
            
            minutes = (currentMilliSeconds) % 60000;
            seconds = (currentMilliSeconds) % 60;
            milliSeconds = (currentMilliSeconds) % 1000;
                                    
            repaint(INNER_BOUNDS);
        }
    }
    
    @Override
    public String toString()
    {
        return "StopWatch";
    }
}
