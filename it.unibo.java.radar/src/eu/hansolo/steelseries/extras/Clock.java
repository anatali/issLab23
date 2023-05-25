package eu.hansolo.steelseries.extras;


/**
 *
 * @author hansolo
 */
public final class Clock extends eu.hansolo.steelseries.gauges.AbstractRadial implements java.awt.event.ActionListener
{       
    private static final double ANGLE_STEP = 6;    
    private final javax.swing.Timer CLOCK_TIMER = new javax.swing.Timer(1000, this);
    private boolean automatic = false;
    private double minutePointerAngle = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE) * ANGLE_STEP;
    private double hourPointerAngle = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR) * ANGLE_STEP * 5 + 0.5 * java.util.Calendar.getInstance().get( java.util.Calendar.MINUTE);
    private double secondPointerAngle = java.util.Calendar.getInstance().get(java.util.Calendar.SECOND) * ANGLE_STEP;    
    private final java.awt.Rectangle INNER_BOUNDS;
    private boolean secondMovesContinuous = false;
    // Background   
    private final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double();    
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage hourImage;
    private java.awt.image.BufferedImage hourShadowImage;
    private java.awt.image.BufferedImage minuteImage;
    private java.awt.image.BufferedImage minuteShadowImage;
    private java.awt.image.BufferedImage knobImage;
    private java.awt.image.BufferedImage secondImage;
    private java.awt.image.BufferedImage secondShadowImage;
    private java.awt.image.BufferedImage topKnobImage;    
    private java.awt.image.BufferedImage disabledImage;      
    private int hour = 11;
    private int minute = 55;
    private int second = 0;
    private int timeZoneOffsetHour = 0;
    private int timeZoneOffsetMinute = 0;
    private boolean secondPointerVisible = true;
    private final java.awt.Color SHADOW_COLOR = new java.awt.Color(0.0f, 0.0f, 0.0f, 0.65f);    

    
    public Clock()
    {
        super();         
        INNER_BOUNDS = new java.awt.Rectangle(getPreferredSize());
        init(getInnerBounds().width, getInnerBounds().height);
        setPointerColor(eu.hansolo.steelseries.tools.ColorDef.BLACK);
        //CLOCK_TIMER.start(); 
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
                case ROUND:
                    FRAME_FACTORY.createRadialFrame(WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);   
                    break;
                case SQUARE:
                    FRAME_FACTORY.createLinearFrame(WIDTH, WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);
                    break;
                default:
                    FRAME_FACTORY.createRadialFrame(WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);
                    break;
            }
        }        
                
        if (isBackgroundVisible())
        {
            create_BACKGROUND_Image(WIDTH, bImage);
        } 
        
        create_TICKMARKS_Image(WIDTH, bImage);
        
        if (hourImage != null)
        {
            hourImage.flush();
        }
        hourImage = create_HOUR_Image(WIDTH);
        
        if (hourShadowImage != null)
        {
            hourShadowImage.flush();
        }
        hourShadowImage = create_HOUR_SHADOW_Image(WIDTH);
        
        if (minuteImage != null)
        {
            minuteImage.flush();
        }
        minuteImage = create_MINUTE_Image(WIDTH);
        
        if (minuteShadowImage != null)
        {
            minuteShadowImage.flush();
        }
        minuteShadowImage = create_MINUTE_SHADOW_Image(WIDTH);
        
        if (knobImage != null)
        {
            knobImage.flush();
        }
        knobImage = create_KNOB_Image(WIDTH);
        
        if (secondImage != null)
        {
            secondImage.flush();
        }
        secondImage = create_SECOND_Image(WIDTH);
        
        if (secondShadowImage != null)
        {
            secondShadowImage.flush();
        }
        secondShadowImage = create_SECOND_SHADOW_Image(WIDTH);
        
        if (topKnobImage != null)
        {
            topKnobImage.flush();
        }
        topKnobImage = create_TOP_KNOB_Image(WIDTH);
        
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

        CENTER.setLocation(INNER_BOUNDS.getCenterX(), INNER_BOUNDS.getCenterX());

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Translate the coordinate system related to the insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);

        // Draw the hour pointer
        G2.rotate(Math.toRadians(hourPointerAngle + (2 * Math.sin(Math.toRadians(hourPointerAngle)))), CENTER.getX(), CENTER.getY());
        G2.drawImage(hourShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(Math.toRadians(hourPointerAngle), CENTER.getX(), CENTER.getY());
        G2.drawImage(hourImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);

        // Draw the minute pointer
        G2.rotate(Math.toRadians(minutePointerAngle + (2 * Math.sin(Math.toRadians(minutePointerAngle)))), CENTER.getX(), CENTER.getY());
        G2.drawImage(minuteShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(Math.toRadians(minutePointerAngle), CENTER.getX(), CENTER.getY());
        G2.drawImage(minuteImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);

        // Draw knob image
        if (getPointerType() == eu.hansolo.steelseries.tools.PointerType.TYPE1)
        {
            G2.drawImage(knobImage, 0, 0, null);
        }

        // Draw the second pointer
        if (secondPointerVisible)
        {    
            G2.rotate(Math.toRadians(secondPointerAngle + (2 * Math.sin(Math.toRadians(secondPointerAngle)))), CENTER.getX(), CENTER.getY());
            G2.drawImage(secondShadowImage, 0, 0, null);
            G2.setTransform(OLD_TRANSFORM);
            G2.rotate(Math.toRadians(secondPointerAngle), CENTER.getX(), CENTER.getY());
            G2.drawImage(secondImage, 0, 0, null);
            G2.setTransform(OLD_TRANSFORM);
        }
        
        // Draw the top knob                
        G2.drawImage(topKnobImage, 0, 0, null);        

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
     * Returns true if the clock will be visualized by the current time
     * @return true if the clock will be visualized by the current time
     */
    public boolean isAutomatic()
    {
        return automatic;
    }
    
    /**
     * Enables / disables the visualization of the clock by using the current time
     * @param AUTOMATIC 
     */
    public void setAutomatic(final boolean AUTOMATIC)
    {
        automatic = AUTOMATIC;
        if (AUTOMATIC)
        {
            if (!CLOCK_TIMER.isRunning())
            {
                CLOCK_TIMER.start();
            }               
        }
        else
        {
            if (CLOCK_TIMER.isRunning())
            {
                CLOCK_TIMER.stop();
            }
        }
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the current hour of the clock
     * @return the current hour of the clock
     */
    public int getHour()
    {
        return hour;
    }
    
    /**
     * Sets the current hour of the clock
     * @param HOUR 
     */
    public void setHour(final int HOUR)
    {
        hour = HOUR % 12;
        calculateAngles(hour, minute, second);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the current minute of the clock
     * @return the current minute of the clock
     */
    public int getMinute()
    {
        return minute;
    }
    
    /**
     * Sets the current minute of the clock
     * @param MINUTE 
     */
    public void setMinute(final int MINUTE)
    {
        minute = MINUTE % 60;
        calculateAngles(hour, minute, second);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the current second of the clock
     * @return the current second of the clock
     */
    public int getSecond()
    {
        return second;
    }
    
    /**
     * Sets the current second of the clock
     * @param SECOND 
     */
    public void setSecond(final int SECOND)
    {
        second = SECOND % 60;
        calculateAngles(hour, minute, second);
        repaint(getInnerBounds());
    }
           
    /**
     * Returns the current timezone offset in hours
     * @return the current timezone offset in hours
     */
    public int getTimeZoneOffsetHour()
    {
        return this.timeZoneOffsetHour;
    }

    /**
     * Sets the current timezone offset in hours
     * @param TIMEZONE_OFFSET_HOUR 
     */
    public void setTimeZoneOffsetHour(final int TIMEZONE_OFFSET_HOUR)
    {
        this.timeZoneOffsetHour = TIMEZONE_OFFSET_HOUR;
    }

    /**
     * Returns the additional timezone offset in minutes
     * @return the additional timezone offset in minutes
     */
    public int getTimeZoneOffsetMinute()
    {
        return this.timeZoneOffsetMinute;
    }

    /**
     * Sets the additional timezone offset in minutes
     * @param TIMEZONE_OFFSET_MINUTE 
     */
    public void setTimeZoneOffsetMinute(final int TIMEZONE_OFFSET_MINUTE)
    {
        this.timeZoneOffsetMinute = TIMEZONE_OFFSET_MINUTE;
    }
        
    /**
     * Returns true if the second pointer of the clock is visible
     * @return true if the second pointer of the clock is visible
     */
    public boolean isSecondPointerVisible()
    {
        return secondPointerVisible;
    }
    
    /**
     * Enables / disables the visibility of the second pointer of the clock
     * @param SECOND_POINTER_VISIBLE 
     */
    public void setSecondPointerVisible(final boolean SECOND_POINTER_VISIBLE)
    {
        secondPointerVisible = SECOND_POINTER_VISIBLE;
        repaint(getInnerBounds());
    }
    
    /**
     * Returns true if the second pointer moves continuously as you might know it from
     * an automatic clock. Otherwise the second pointer will move only once each second.
     * @return true if the second pointer moves continuously
     */
    public boolean isSecondMovesContinuous()
    {
        return secondMovesContinuous;
    }
    
    /**
     * Enables / disables the continuous movement of the second pointer
     * @param SECOND_MOVES_CONTINUOUS 
     */
    public void setSecondMovesContinuous(final boolean SECOND_MOVES_CONTINUOUS)
    {
        if (SECOND_MOVES_CONTINUOUS)
        {
            CLOCK_TIMER.setDelay(100);
        }
        else
        {
            CLOCK_TIMER.setDelay(1000);
        }
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

    private java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH, java.awt.image.BufferedImage image)
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
        
        switch(getFrameType())
        {                            
            case SQUARE:
                BACKGROUND_FACTORY.createLinearBackground(WIDTH, WIDTH, getBackgroundColor(), getCustomBackground(), image);
                break;
            case ROUND:
                
            default:                
                BACKGROUND_FACTORY.createRadialBackground(WIDTH, getBackgroundColor(), getCustomBackground(), image);
                break;
        }   
        G2.dispose();

        return image;
    }

    private java.awt.image.BufferedImage create_TICKMARKS_Image(final int WIDTH, java.awt.image.BufferedImage image)
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

        
        final int IMAGE_WIDTH = image.getWidth();
        //final int IMAGE_HEIGHT = image.getHeight();
        
        final java.awt.Color TICKMARK_COLOR = getBackgroundColor().LABEL_COLOR;
        final double SMALL_TICK_WIDTH;
        final double SMALL_TICK_HEIGHT;
        final double BIG_TICK_WIDTH;
        final double BIG_TICK_HEIGHT;
        final java.awt.geom.Rectangle2D SMALL_TICK = new java.awt.geom.Rectangle2D.Double();
        final java.awt.geom.Rectangle2D BIG_TICK = new java.awt.geom.Rectangle2D.Double();
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        
        switch (getPointerType())
        {
            case TYPE2:
                
                // Draw minutes tickmarks
                SMALL_TICK_WIDTH = IMAGE_WIDTH * 0.0140186916;
                SMALL_TICK_HEIGHT = IMAGE_WIDTH * 0.0373831776;
                SMALL_TICK.setFrame(CENTER.getX() - (SMALL_TICK_WIDTH / 2), IMAGE_WIDTH * 0.0981308411, SMALL_TICK_WIDTH, SMALL_TICK_HEIGHT);                        
                G2.setColor(TICKMARK_COLOR);
                for (int tickAngle = 0 ; tickAngle < 360 ; tickAngle += 6)
                {
                    G2.setTransform(OLD_TRANSFORM);
                    G2.rotate(Math.toRadians(tickAngle), CENTER.getX(), CENTER.getY());
                    G2.fill(SMALL_TICK);
                }
                                
                // Draw hours tickmarks
                BIG_TICK_WIDTH = IMAGE_WIDTH * 0.0327102804;
                BIG_TICK_HEIGHT = IMAGE_WIDTH * 0.1261682243;
                BIG_TICK.setFrame(CENTER.getX() - (BIG_TICK_WIDTH / 2), IMAGE_WIDTH * 0.0981308411, BIG_TICK_WIDTH, BIG_TICK_HEIGHT);
                for (int tickAngle = 0 ; tickAngle < 360 ; tickAngle += 30)
                {
                    G2.setTransform(OLD_TRANSFORM);
                    G2.rotate(Math.toRadians(tickAngle), CENTER.getX(), CENTER.getY());
                    G2.fill(BIG_TICK);
                }        
                break;
                
            case TYPE1:                
                
            default:                
                SMALL_TICK_WIDTH = IMAGE_WIDTH * 0.0093457944;
                SMALL_TICK_HEIGHT = IMAGE_WIDTH * 0.0747663551;
                SMALL_TICK.setFrame(CENTER.getX() - (SMALL_TICK_WIDTH / 2), IMAGE_WIDTH * 0.0981308411, SMALL_TICK_WIDTH, SMALL_TICK_HEIGHT);                
                for (int tickAngle = 0 ; tickAngle < 360 ; tickAngle += 30)
                {
                    G2.setTransform(OLD_TRANSFORM);
                    G2.rotate(Math.toRadians(tickAngle), CENTER.getX(), CENTER.getY());
                    G2.setColor(TICKMARK_COLOR);
                    G2.fill(SMALL_TICK);
                    G2.setColor(TICKMARK_COLOR.darker());
                    G2.draw(SMALL_TICK);
                }
                
                BIG_TICK_WIDTH = IMAGE_WIDTH * 0.0280373832;
                BIG_TICK_HEIGHT = IMAGE_WIDTH * 0.0841121495;                              
                BIG_TICK.setFrame(CENTER.getX() - (BIG_TICK_WIDTH / 2), IMAGE_WIDTH * 0.0981308411, BIG_TICK_WIDTH, BIG_TICK_HEIGHT);
                for (int tickAngle = 0 ; tickAngle < 360 ; tickAngle += 90)
                {
                    G2.setTransform(OLD_TRANSFORM);
                    G2.rotate(Math.toRadians(tickAngle), CENTER.getX(), CENTER.getY());
                    G2.setColor(TICKMARK_COLOR);
                    G2.fill(BIG_TICK);
                    G2.setColor(TICKMARK_COLOR.darker());
                    G2.draw(BIG_TICK);
                }                     
                break;
        }

        G2.dispose();

        return image;
    }
    
    private java.awt.image.BufferedImage create_HOUR_Image(final int WIDTH)
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

        switch(getPointerType())
        {
            case TYPE2:                
                final double HOUR_POINTER_WIDTH = IMAGE_WIDTH * 0.046728972;
                final double HOUR_POINTER_HEIGHT = IMAGE_WIDTH * 0.2242990654;
                final java.awt.geom.Rectangle2D HOUR_POINTER = new java.awt.geom.Rectangle2D.Double(CENTER.getX() - (HOUR_POINTER_WIDTH / 2), (IMAGE_WIDTH * 0.2897196262), HOUR_POINTER_WIDTH, HOUR_POINTER_HEIGHT);
                G2.setPaint(getPointerColor().MEDIUM);
                G2.fill(HOUR_POINTER);
                break;
                
            case TYPE1:
                
            default:                                        
                final java.awt.geom.GeneralPath HOURPOINTER = new java.awt.geom.GeneralPath();
                HOURPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                HOURPOINTER.moveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5607476635514018);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.21495327102803738);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1822429906542056);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.21495327102803738);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5607476635514018);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5607476635514018);
                HOURPOINTER.closePath();
                final java.awt.geom.Point2D HOURPOINTER_START = new java.awt.geom.Point2D.Double(0, HOURPOINTER.getBounds2D().getMaxY() );
                final java.awt.geom.Point2D HOURPOINTER_STOP = new java.awt.geom.Point2D.Double(0, HOURPOINTER.getBounds2D().getMinY() );
                final float[] HOURPOINTER_FRACTIONS =
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] HOURPOINTER_COLORS =
                {
                    new java.awt.Color(245, 246, 248, 255),
                    new java.awt.Color(176, 181, 188, 255)
                };
                final java.awt.LinearGradientPaint HOURPOINTER_GRADIENT = new java.awt.LinearGradientPaint(HOURPOINTER_START, HOURPOINTER_STOP, HOURPOINTER_FRACTIONS, HOURPOINTER_COLORS);
                G2.setPaint(HOURPOINTER_GRADIENT);
                G2.fill(HOURPOINTER);
                final java.awt.Color STROKE_COLOR_HOURPOINTER = new java.awt.Color(0xDADDE1);
                G2.setColor(STROKE_COLOR_HOURPOINTER);
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(HOURPOINTER);
                break;
        }
        
        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_HOUR_SHADOW_Image(final int WIDTH)
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
        switch (getPointerType())
        {
            case TYPE2:
                
                break;
                
            case TYPE1:
                
            default:
                final java.awt.geom.GeneralPath HOURPOINTER = new java.awt.geom.GeneralPath();
                HOURPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                HOURPOINTER.moveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5607476635514018);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.21495327102803738);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1822429906542056);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.21495327102803738);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5607476635514018);
                HOURPOINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5607476635514018);
                HOURPOINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(HOURPOINTER);
                break;
        }        

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_MINUTE_Image(final int WIDTH)
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

        switch(getPointerType())
        {
            case TYPE2:                
                final double MINUTE_POINTER_WIDTH = IMAGE_WIDTH * 0.0327102804;
                final double MINUTE_POINTER_HEIGHT = IMAGE_WIDTH * 0.3878504673;
                final java.awt.geom.Rectangle2D MINUTE_POINTER = new java.awt.geom.Rectangle2D.Double(CENTER.getX() - (MINUTE_POINTER_WIDTH / 2), (IMAGE_WIDTH * 0.1168224299), MINUTE_POINTER_WIDTH, MINUTE_POINTER_HEIGHT);
                G2.setPaint(getPointerColor().MEDIUM);
                G2.fill(MINUTE_POINTER);
                break;
                
            case TYPE1:
                
            default:
                final java.awt.geom.GeneralPath MINUTEPOINTER = new java.awt.geom.GeneralPath();
                MINUTEPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                MINUTEPOINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5747663551401869);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.13551401869158877);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.10747663551401869);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.14018691588785046);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5747663551401869);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5747663551401869);
                MINUTEPOINTER.closePath();
                final java.awt.geom.Point2D MINUTEPOINTER_START = new java.awt.geom.Point2D.Double(0, MINUTEPOINTER.getBounds2D().getMinY() );
                final java.awt.geom.Point2D MINUTEPOINTER_STOP = new java.awt.geom.Point2D.Double(0, MINUTEPOINTER.getBounds2D().getMaxY() );
                final float[] MINUTEPOINTER_FRACTIONS =
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] MINUTEPOINTER_COLORS =
                {
                    new java.awt.Color(245, 246, 248, 255),
                    new java.awt.Color(176, 181, 188, 255)
                };
                final java.awt.LinearGradientPaint MINUTEPOINTER_GRADIENT = new java.awt.LinearGradientPaint(MINUTEPOINTER_START, MINUTEPOINTER_STOP, MINUTEPOINTER_FRACTIONS, MINUTEPOINTER_COLORS);
                G2.setPaint(MINUTEPOINTER_GRADIENT);
                G2.fill(MINUTEPOINTER);
                final java.awt.Color STROKE_COLOR_MINUTEPOINTER = new java.awt.Color(0xDADDE1);
                G2.setColor(STROKE_COLOR_MINUTEPOINTER);
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(MINUTEPOINTER);
                break;
        }
                
        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_MINUTE_SHADOW_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, (int) (1.0 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        switch(getPointerType())
        {
            case TYPE2:
                
                break;
                
            case TYPE1:
                
            default:
                final java.awt.geom.GeneralPath MINUTEPOINTER = new java.awt.geom.GeneralPath();
                MINUTEPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                MINUTEPOINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5747663551401869);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.13551401869158877);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.10747663551401869);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.14018691588785046);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5747663551401869);
                MINUTEPOINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5747663551401869);
                MINUTEPOINTER.closePath();        
                G2.setColor(SHADOW_COLOR);
                G2.fill(MINUTEPOINTER);
                break;
        }                

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_KNOB_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath KNOBSHADOW = new java.awt.geom.GeneralPath();
        KNOBSHADOW.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        KNOBSHADOW.moveTo(IMAGE_WIDTH * 0.4532710280373832, IMAGE_HEIGHT * 0.5046728971962616);
        KNOBSHADOW.curveTo(IMAGE_WIDTH * 0.4532710280373832, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.45794392523364486);
        KNOBSHADOW.curveTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5467289719626168, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5467289719626168, IMAGE_HEIGHT * 0.5046728971962616);
        KNOBSHADOW.curveTo(IMAGE_WIDTH * 0.5467289719626168, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5560747663551402);
        KNOBSHADOW.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.4532710280373832, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.4532710280373832, IMAGE_HEIGHT * 0.5046728971962616);
        KNOBSHADOW.closePath();
        final java.awt.geom.Point2D KNOBSHADOW_START = new java.awt.geom.Point2D.Double(0, KNOBSHADOW.getBounds2D().getMinY() );
        final java.awt.geom.Point2D KNOBSHADOW_STOP = new java.awt.geom.Point2D.Double(0, KNOBSHADOW.getBounds2D().getMaxY() );
        final float[] KNOBSHADOW_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] KNOBSHADOW_COLORS =
        {
            new java.awt.Color(40, 40, 41, 255),
            new java.awt.Color(13, 13, 13, 255)
        };
        final java.awt.LinearGradientPaint KNOBSHADOW_GRADIENT = new java.awt.LinearGradientPaint(KNOBSHADOW_START, KNOBSHADOW_STOP, KNOBSHADOW_FRACTIONS, KNOBSHADOW_COLORS);
        G2.setPaint(KNOBSHADOW_GRADIENT);
        G2.fill(KNOBSHADOW);

        final java.awt.geom.GeneralPath KNOB = new java.awt.geom.GeneralPath();
        KNOB.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        KNOB.moveTo(IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5);
        KNOB.curveTo(IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.4766355140186916, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.45794392523364486);
        KNOB.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.4766355140186916, IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.5);
        KNOB.curveTo(IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.5233644859813084, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5420560747663551);
        KNOB.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5420560747663551, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5233644859813084, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.5);
        KNOB.closePath();
        final java.awt.geom.Point2D KNOB_START = new java.awt.geom.Point2D.Double(0, KNOB.getBounds2D().getMinY() );
        final java.awt.geom.Point2D KNOB_STOP = new java.awt.geom.Point2D.Double(0, KNOB.getBounds2D().getMaxY() );
        final float[] KNOB_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] KNOB_COLORS =
        {
            new java.awt.Color(238, 240, 242, 255),
            new java.awt.Color(101, 105, 109, 255)
        };
        final java.awt.LinearGradientPaint KNOB_GRADIENT = new java.awt.LinearGradientPaint(KNOB_START, KNOB_STOP, KNOB_FRACTIONS, KNOB_COLORS);
        G2.setPaint(KNOB_GRADIENT);
        G2.fill(KNOB);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_SECOND_Image(final int WIDTH)
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

        switch(getPointerType())
        {
            case TYPE2:                
                final java.awt.geom.Rectangle2D TOP = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.4953271028, IMAGE_WIDTH * 0.0981308411, IMAGE_WIDTH * 0.0093457944, IMAGE_WIDTH * 0.1261682243);
                final java.awt.geom.Rectangle2D BOTTOM = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.4906542056, IMAGE_WIDTH * 0.308411215, IMAGE_WIDTH * 0.0186915888, IMAGE_WIDTH * 0.191588785);
                final java.awt.geom.Area SECOND = new java.awt.geom.Area(TOP);                
                SECOND.add(new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.453271028, IMAGE_WIDTH * 0.2196261682, IMAGE_WIDTH * 0.0934579439, IMAGE_WIDTH * 0.0934579439)));
                SECOND.subtract(new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4672897196, IMAGE_WIDTH * 0.2336448598, IMAGE_WIDTH * 0.0654205607, IMAGE_WIDTH * 0.0654205607)));
                SECOND.add(new java.awt.geom.Area(BOTTOM));
                final java.awt.geom.GeneralPath SECOND_POINTER = new java.awt.geom.GeneralPath(SECOND);
                G2.setPaint(eu.hansolo.steelseries.tools.ColorDef.RED.MEDIUM);
                G2.fill(SECOND_POINTER);
                break;
            
            case TYPE1:
                
            default:
                final java.awt.geom.GeneralPath SECONDPOINTER = new java.awt.geom.GeneralPath();
                SECONDPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                SECONDPOINTER.moveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.11682242990654206);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5747663551401869);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5747663551401869);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.11682242990654206);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.11682242990654206);
                SECONDPOINTER.closePath();
                final java.awt.geom.Point2D SECONDPOINTER_START = new java.awt.geom.Point2D.Double(SECONDPOINTER.getBounds2D().getMaxX(), 0);
                final java.awt.geom.Point2D SECONDPOINTER_STOP = new java.awt.geom.Point2D.Double(SECONDPOINTER.getBounds2D().getMinX(), 0);
                final float[] SECONDPOINTER_FRACTIONS =
                {
                    0.0f,
                    0.47f,
                    1.0f
                };
                final java.awt.Color[] SECONDPOINTER_COLORS =
                {
                    new java.awt.Color(236, 123, 125, 255),
                    new java.awt.Color(231, 27, 33, 255),
                    new java.awt.Color(166, 40, 46, 255)
                };
                final java.awt.LinearGradientPaint SECONDPOINTER_GRADIENT = new java.awt.LinearGradientPaint(SECONDPOINTER_START, SECONDPOINTER_STOP, SECONDPOINTER_FRACTIONS, SECONDPOINTER_COLORS);
                G2.setPaint(SECONDPOINTER_GRADIENT);
                G2.fill(SECONDPOINTER);
                break;
        }
                
        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_SECOND_SHADOW_Image(final int WIDTH)
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
                
        switch(getPointerType())
        {
            case TYPE2:
                
                final java.awt.geom.Rectangle2D TOP = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.4953271028, IMAGE_WIDTH * 0.0981308411, IMAGE_WIDTH * 0.0093457944, IMAGE_WIDTH * 0.1261682243);
                final java.awt.geom.Rectangle2D BOTTOM = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.4906542056, IMAGE_WIDTH * 0.308411215, IMAGE_WIDTH * 0.0186915888, IMAGE_WIDTH * 0.191588785);
                final java.awt.geom.Area SECOND = new java.awt.geom.Area(TOP);                
                SECOND.add(new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.453271028, IMAGE_WIDTH * 0.2196261682, IMAGE_WIDTH * 0.0934579439, IMAGE_WIDTH * 0.0934579439)));
                SECOND.subtract(new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4672897196, IMAGE_WIDTH * 0.2336448598, IMAGE_WIDTH * 0.0654205607, IMAGE_WIDTH * 0.0654205607)));
                SECOND.add(new java.awt.geom.Area(BOTTOM));
                final java.awt.geom.GeneralPath SECOND_POINTER = new java.awt.geom.GeneralPath(SECOND);;
                G2.setPaint(SHADOW_COLOR);
                G2.fill(SECOND_POINTER);
                break;
                
            case TYPE1:
                
            default:
                final java.awt.geom.GeneralPath SECONDPOINTER = new java.awt.geom.GeneralPath();
                SECONDPOINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                SECONDPOINTER.moveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.11682242990654206);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5747663551401869);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5747663551401869);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.11682242990654206);
                SECONDPOINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.11682242990654206);
                SECONDPOINTER.closePath();
                G2.setPaint(SHADOW_COLOR);
                G2.fill(SECONDPOINTER);
                break;
        }                
        
        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_TOP_KNOB_Image(final int WIDTH)
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
                
        switch(getPointerType())
        {
            case TYPE2:
                final double CENTER_KNOB_DIAMETER = WIDTH * 0.0887850467;
                final java.awt.geom.Ellipse2D CENTER_KNOB = new  java.awt.geom.Ellipse2D.Double(CENTER.getX() - CENTER_KNOB_DIAMETER / 2, CENTER.getY() - CENTER_KNOB_DIAMETER / 2, CENTER_KNOB_DIAMETER, CENTER_KNOB_DIAMETER);   
                G2.setPaint(getPointerColor().MEDIUM);
                G2.fill(CENTER_KNOB);
                break;
                
            case TYPE1:
                
            default:
                final java.awt.geom.GeneralPath TOPKNOBSHADOW = new java.awt.geom.GeneralPath();
                TOPKNOBSHADOW.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                TOPKNOBSHADOW.moveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5);
                TOPKNOBSHADOW.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.48598130841121495, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4719626168224299);
                TOPKNOBSHADOW.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.48598130841121495, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5);
                TOPKNOBSHADOW.curveTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.514018691588785, IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5280373831775701);
                TOPKNOBSHADOW.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.514018691588785, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5);
                TOPKNOBSHADOW.closePath();
                final java.awt.geom.Point2D TOPKNOBSHADOW_START = new java.awt.geom.Point2D.Double(0, TOPKNOBSHADOW.getBounds2D().getMinY() );
                final java.awt.geom.Point2D TOPKNOBSHADOW_STOP = new java.awt.geom.Point2D.Double(0, TOPKNOBSHADOW.getBounds2D().getMaxY() );
                final float[] TOPKNOBSHADOW_FRACTIONS =
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] TOPKNOBSHADOW_COLORS =
                {
                    new java.awt.Color(221, 223, 223, 255),
                    new java.awt.Color(38, 40, 41, 255)
                };
                final java.awt.LinearGradientPaint TOPKNOBSHADOW_GRADIENT = new java.awt.LinearGradientPaint(TOPKNOBSHADOW_START, TOPKNOBSHADOW_STOP, TOPKNOBSHADOW_FRACTIONS, TOPKNOBSHADOW_COLORS);
                G2.setPaint(TOPKNOBSHADOW_GRADIENT);
                G2.fill(TOPKNOBSHADOW);

                final java.awt.geom.GeneralPath TOPKNOB = new java.awt.geom.GeneralPath();
                TOPKNOB.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                TOPKNOB.moveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5);
                TOPKNOB.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.48598130841121495, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.4766355140186916, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4766355140186916);
                TOPKNOB.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.4766355140186916, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.48598130841121495, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.5);
                TOPKNOB.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.514018691588785, IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.5233644859813084, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5233644859813084);
                TOPKNOB.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.5233644859813084, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.514018691588785, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5);
                TOPKNOB.closePath();
                final java.awt.geom.Point2D TOPKNOB_START = new java.awt.geom.Point2D.Double(0, TOPKNOB.getBounds2D().getMinY() );
                final java.awt.geom.Point2D TOPKNOB_STOP = new java.awt.geom.Point2D.Double(0, TOPKNOB.getBounds2D().getMaxY() );
                final float[] TOPKNOB_FRACTIONS =
                {
                    0.0f,
                    0.11f,
                    0.12f,
                    0.2f,
                    0.2001f,
                    1.0f
                };
                final java.awt.Color[] TOPKNOB_COLORS =
                {
                    new java.awt.Color(234, 235, 238, 255),
                    new java.awt.Color(234, 236, 238, 255),
                    new java.awt.Color(232, 234, 236, 255),
                    new java.awt.Color(192, 197, 203, 255),
                    new java.awt.Color(190, 195, 201, 255),
                    new java.awt.Color(169, 174, 181, 255)
                };
                final java.awt.LinearGradientPaint TOPKNOB_GRADIENT = new java.awt.LinearGradientPaint(TOPKNOB_START, TOPKNOB_STOP, TOPKNOB_FRACTIONS, TOPKNOB_COLORS);
                G2.setPaint(TOPKNOB_GRADIENT);
                G2.fill(TOPKNOB);
                break;
        }
                
        G2.dispose();

        return IMAGE;
    }
    
    private void calculateAngles(final int HOUR, final int MINUTE, final int SECOND)
    {        
        secondPointerAngle = SECOND * ANGLE_STEP;
        minutePointerAngle = MINUTE * ANGLE_STEP;
        hourPointerAngle = HOUR * ANGLE_STEP * 5 + (0.5) * minute;        
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

        init(getInnerBounds().width, getInnerBounds().height);
        
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
        if (event.getSource().equals(CLOCK_TIMER))
        {
            // Seconds
            secondPointerAngle = java.util.Calendar.getInstance().get(java.util.Calendar.SECOND) * ANGLE_STEP + java.util.Calendar.getInstance().get(java.util.Calendar.MILLISECOND) * ANGLE_STEP / 1000;

            // Hours
            hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR) - this.timeZoneOffsetHour;
            if (hour > 12)
            {
                hour -= 12;
            }
            if (hour < 0)
            {
                hour += 12;
            }

            // Minutes
            minute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE) + this.timeZoneOffsetMinute;
            if (minute > 60)
            {
                minute -= 60;
                hour++;
            }
            if (minute < 0)
            {
                minute += 60;
                hour--;
            }

            // Calculate angles from current hour and minute values
            hourPointerAngle = hour * ANGLE_STEP * 5 + (0.5) * minute;
            minutePointerAngle = minute * ANGLE_STEP;

            repaint(getInnerBounds());
        }
    }

    @Override
    public String toString()
    {
        return "Clock";
    }   
}