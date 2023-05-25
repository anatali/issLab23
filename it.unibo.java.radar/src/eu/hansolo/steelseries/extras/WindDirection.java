package eu.hansolo.steelseries.extras;


/**
 *
 * @author hansolo
 */
public class WindDirection extends eu.hansolo.steelseries.gauges.AbstractRadial
{
    private double visibleValue = 0;    
    private double angleStep;
    //private java.awt.Font font = new java.awt.Font("Verdana", 0, 30);
    private final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double();        
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
        
    private java.awt.image.BufferedImage pointerImage;   
    private java.awt.image.BufferedImage pointer2Image;    
    private java.awt.image.BufferedImage disabledImage;
    private eu.hansolo.steelseries.tools.ColorDef pointer2Color = eu.hansolo.steelseries.tools.ColorDef.BLUE;
    private eu.hansolo.steelseries.tools.CustomColorDef customPointer2Color = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.BLUE);
    private eu.hansolo.steelseries.tools.PointerType pointer2Type = eu.hansolo.steelseries.tools.PointerType.TYPE3;
    private boolean pointer2Visible;
    private double value2;
    private org.pushingpixels.trident.Timeline timeline = new org.pushingpixels.trident.Timeline(this);
    private final org.pushingpixels.trident.ease.Spline EASE = new org.pushingpixels.trident.ease.Spline(0.5f);
    private long easingDuration = 250;        
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();

    
    public WindDirection()
    {
        super();
        setMinValue(-360);
        setMaxValue(360);
        setPointerType(eu.hansolo.steelseries.tools.PointerType.TYPE5);
        setLcdColor(eu.hansolo.steelseries.tools.LcdColor.BLACK_LCD);
        setValueCoupled(false);
        setLcdDecimals(1);
        setLcdVisible(true);
        calcAngleStep();            
        value2 = 0;
        pointer2Visible = true;
        init(getInnerBounds().width, getInnerBounds().height);
    }
    
    @Override
    public final eu.hansolo.steelseries.gauges.AbstractGauge init(int WIDTH, int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        if (isDigitalFont())
        {
            setLcdValueFont(getModel().getDigitalBaseFont().deriveFont(0.7f * WIDTH * 0.15f));            
        }
        else
        {
            setLcdValueFont(getModel().getStandardBaseFont().deriveFont(0.625f * WIDTH * 0.15f));       
        }

        if (isCustomLcdUnitFontEnabled())
        {
            setLcdUnitFont(getCustomLcdUnitFont().deriveFont(0.25f * WIDTH * 0.15f));
        }
        else
        {
            setLcdUnitFont(getModel().getStandardBaseFont().deriveFont(0.25f * WIDTH * 0.15f));
        }
        
        setLcdInfoFont(getModel().getStandardInfoFont().deriveFont(0.15f * WIDTH * 0.15f));
        
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
            create_BACKGROUND_Image(WIDTH, "", "", bImage);
        }
        
        create_SHIP_Image(WIDTH, bImage);
        
        create_TITLE_Image(WIDTH, getTitle(), getUnitString(), bImage);
                
        // Create sections if not empty
        if (!getSections().isEmpty())
        {
            createSections(bImage);
        }
        
        create_TICKMARKS_Image(WIDTH, 0, 0, 0, 0, 0, 0, 0, true, true, null, bImage);
                      
        create_LCD_Image(new java.awt.geom.Rectangle2D.Double(((getGaugeBounds().width - WIDTH * 0.4) / 2.0), (getGaugeBounds().height * 0.55), (WIDTH * 0.4), (WIDTH * 0.15)), getLcdColor(), getCustomLcdBackground(), bImage);
        LCD.setRect(((getGaugeBounds().width - WIDTH * 0.4) / 2.0), (getGaugeBounds().height * 0.55), WIDTH * 0.4, WIDTH * 0.15);                
        
        if (pointerImage != null)
        {
            pointerImage.flush();
        }
        pointerImage = create_POINTER_Image(WIDTH, getPointerType());
                
        if (pointer2Image != null)
        {
            pointer2Image.flush();
        }
        pointer2Image = create_POINTER_Image(WIDTH, pointer2Type, pointer2Color, customPointer2Color);
        
        create_POSTS_Image(WIDTH, fImage, eu.hansolo.steelseries.tools.PostPosition.CENTER);
        
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
        disabledImage = create_DISABLED_Image(WIDTH);
                        
        return this;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        if (!isInitialized())
        {
            return;
        }
        
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();
        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Translate the coordinate system related to insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterX());
                
        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);
                                
        // Draw LCD display
        if (isLcdVisible())
        {            
            if (getLcdColor() == eu.hansolo.steelseries.tools.LcdColor.CUSTOM)
            {
                G2.setColor(getCustomLcdForeground());
            }
            else
            {
                G2.setColor(getLcdColor().TEXT_COLOR);
            }
            G2.setFont(getLcdUnitFont());            
            final double UNIT_STRING_WIDTH;
            if (isLcdUnitStringVisible())
            {
                unitLayout = new java.awt.font.TextLayout(getLcdUnitString(), G2.getFont(), RENDER_CONTEXT);
                UNIT_BOUNDARY.setFrame(unitLayout.getBounds());
                G2.drawString(getLcdUnitString(), (int) (LCD.getX() + (LCD.getWidth() - UNIT_BOUNDARY.getWidth()) - LCD.getWidth() * 0.03), (int) (LCD.getY() + LCD.getHeight() * 0.76f));
                UNIT_STRING_WIDTH = UNIT_BOUNDARY.getWidth();
            }
            else
            {
                UNIT_STRING_WIDTH = 0;
            }               
            G2.setFont(getLcdValueFont());            
            switch(getModel().getNumberSystem())
            {
                case HEX:
                    valueLayout = new java.awt.font.TextLayout(Integer.toHexString((int) getLcdValue()).toUpperCase(), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toHexString((int) getLcdValue()).toUpperCase(), (int) (LCD.getX() + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76f));
                    break;
                    
                case OCT:
                    valueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toOctalString((int) getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76f));
                    break;
                    
                case DEC:
                    
                default:
                    valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(formatLcdValue(getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76f));
                    break;
            }
            // Draw lcd info string
            if (!getLcdInfoString().isEmpty())
            {
                G2.setFont(getLcdInfoFont());
                infoLayout = new java.awt.font.TextLayout(getLcdInfoString(), G2.getFont(), RENDER_CONTEXT);
                INFO_BOUNDARY.setFrame(infoLayout.getBounds());
                G2.drawString(getLcdInfoString(), LCD.getBounds().x + 5, LCD.getBounds().y + (int) INFO_BOUNDARY.getHeight() + 5);
            }
        }
        
        // Draw the pointer2 
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        G2.rotate(value2 * angleStep, CENTER.getX(), CENTER.getY());
        G2.drawImage(pointer2Image, 0, 0, null);                
        G2.setTransform(OLD_TRANSFORM);
        
        // Draw the pointer                       
        G2.rotate(getValue() * angleStep, CENTER.getX(), CENTER.getY());
        G2.drawImage(pointerImage, 0, 0, null);        
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
        
    @Override
    public void setValue(final double VALUE)
    {
        if (isEnabled())
        {                
            super.setValue((VALUE % 360));

            if ((VALUE % 360) == 0)
            {
                this.visibleValue = 90;
            }

            if ((VALUE % 360) > 0 && (VALUE % 360) <= 180)
            {
                this.visibleValue = ((VALUE % 360));
            }

            if ((VALUE % 360) > 180 && (VALUE % 360) <= 360)
            {
                this.visibleValue = (360 - (VALUE % 360));
            }

            if (isValueCoupled())
            {
                setLcdValue(visibleValue);
            }
            
            fireStateChanged();
            repaint(getInnerBounds());
        }
    }

    @Override
    public void setValueAnimated(double value)
    {
        if (isEnabled())
        {                
            if (timeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD || timeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_REVERSE)
            {
                timeline.abort();
            }
            timeline = new org.pushingpixels.trident.Timeline(this);
            timeline.addPropertyToInterpolate("value", getValue(), value);
            timeline.setEase(EASE);

            timeline.setDuration(easingDuration);
            timeline.play();
        }
    }

    @Override
    public double getMinValue()
    {
        return -360.0;
    }

    @Override
    public double getMaxValue()
    {
        return 360.0;
    }

    public long getEasingDuration()
    {
        return this.easingDuration;
    }

    public void setEasingDuration(final long EASING_DURATION)
    {
        this.easingDuration = EASING_DURATION;
    }

    /**
     * Returns the colordefinition of the second pointer
     * @return the colordefinition of the second pointer
     */
    public eu.hansolo.steelseries.tools.ColorDef getPointer2Color()
    {
        return pointer2Color;
    }
    
    /**
     * Sets the colordefinition of the second pointer
     * @param POINTER2_COLOR 
     */
    public void setPointer2Color(final eu.hansolo.steelseries.tools.ColorDef POINTER2_COLOR)
    {
        pointer2Color = POINTER2_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the value of the second pointer
     * @return the value of the second pointer
     */
    public double getValue2()
    {
        return value2;
    }
    
    /**
     * Sets the value of the second pointer
     * @param VALUE2 
     */
    public void setValue2(final double VALUE2)
    {        
        if (isEnabled())
        {                                        
            value2 = (VALUE2 % 360);
            fireStateChanged();
            repaint(getInnerBounds());
        }
    }
    
    /**
     * Returns true if the second pointer is visible
     * @return true if the second pointer is visible
     */
    public boolean isPointer2Visible()
    {
        return pointer2Visible;
    }
    
    /**
     * Enables / disables the visibility of the second pointer
     * @param POINTER2_VISIBLE 
     */
    public void setPointer2Visible(final boolean POINTER2_VISIBLE)
    {
        pointer2Visible = POINTER2_VISIBLE;
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the pointertype of the second pointer
     * @return the pointertype of the second pointer
     */
    public eu.hansolo.steelseries.tools.PointerType getPointer2Type()
    {
        return pointer2Type;
    }
    
    /**
     * Sets the pointertype of the second pointer
     * @param POINTER2_TYPE 
     */
    public void setPointer2Type(final eu.hansolo.steelseries.tools.PointerType POINTER2_TYPE)
    {
        pointer2Type = POINTER2_TYPE;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the color from which the custom pointer2 color will be calculated
     * @return the color from which the custom pointer2 color will be calculated
     */
    public java.awt.Color getCustomPointer2Color()
    {
        return this.customPointer2Color.COLOR;
    }
    
    /**
     * Sets the color from which the custom pointer2 color is calculated
     * @param COLOR 
     */
    public void setCustomPointer2Color(final java.awt.Color COLOR)
    {
        this.customPointer2Color = new eu.hansolo.steelseries.tools.CustomColorDef(COLOR);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the object that represents the custom pointer2 color
     * @return the object that represents the custom pointer2 color
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomPointer2ColorObject()
    {
        return this.customPointer2Color;
    }
    
    private void calcAngleStep()
    {
        angleStep = (2.0 * Math.PI) / 360.0;
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
    
    private void createSections(final java.awt.image.BufferedImage IMAGE)
    {                                            
        final double ORIGIN_CORRECTION;
        final double ANGLE_STEP;
        final double OUTER_RADIUS;
        final double INNER_RADIUS;
        final double FREE_AREA_OUTER_RADIUS;
        final double FREE_AREA_INNER_RADIUS;
        final java.awt.geom.Ellipse2D INNER;
        
        if (bImage != null)
        {
            ORIGIN_CORRECTION = 90.0;
            ANGLE_STEP = 1.0;
            OUTER_RADIUS = bImage.getWidth() * 0.38f;
            INNER_RADIUS = bImage.getWidth() * 0.38f - bImage.getWidth() * 0.04f;
            FREE_AREA_OUTER_RADIUS = bImage.getWidth() / 2.0 - OUTER_RADIUS;
            FREE_AREA_INNER_RADIUS = bImage.getWidth() / 2.0 - INNER_RADIUS;
            INNER = new java.awt.geom.Ellipse2D.Double(bImage.getMinX() + FREE_AREA_INNER_RADIUS, bImage.getMinY() + FREE_AREA_INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);            

            for (eu.hansolo.steelseries.tools.Section section : getSections())
            {                            
                final double ANGLE_START = ORIGIN_CORRECTION - (section.getStart() * ANGLE_STEP) + (getMinValue() * ANGLE_STEP);
                final double ANGLE_EXTEND = -(section.getStop() - section.getStart()) * ANGLE_STEP;                

                final java.awt.geom.Arc2D OUTER_ARC = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
                OUTER_ARC.setFrame(bImage.getMinX() + FREE_AREA_OUTER_RADIUS, bImage.getMinY() + FREE_AREA_OUTER_RADIUS, 2 * OUTER_RADIUS, 2 * OUTER_RADIUS);
                OUTER_ARC.setAngleStart(ANGLE_START);
                OUTER_ARC.setAngleExtent(ANGLE_EXTEND);
                final java.awt.geom.Area SECTION = new java.awt.geom.Area(OUTER_ARC);

                SECTION.subtract(new java.awt.geom.Area(INNER));

                section.setSectionArea(SECTION);
            }
            
            if (isSectionsVisible() && IMAGE != null)
            {
                final java.awt.Graphics2D G2 = IMAGE.createGraphics();
                G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                for (eu.hansolo.steelseries.tools.Section section : getSections())
                {
                    G2.setColor(section.getColor());
                    G2.fill(section.getSectionArea());                
                }
                G2.dispose();
            }
        }
    }
        
    protected java.awt.image.BufferedImage create_TICKMARKS_Image(final int WIDTH, final double FREE_AREA_ANGLE, final double OFFSET, final double MIN_VALUE, final double MAX_VALUE, final double ANGLE_STEP, final int TICK_LABEL_PERIOD, final int SCALE_DIVIDER_POWER, final boolean DRAW_TICKS, final boolean DRAW_TICK_LABELS, java.util.ArrayList<eu.hansolo.steelseries.tools.Section> tickmarkSections, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, (int) (1.0 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        }
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

        final java.awt.Font STD_FONT = new java.awt.Font("Verdana", 0, (int) (0.04 * WIDTH));                
        final java.awt.BasicStroke MEDIUM_STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);
        final java.awt.BasicStroke THIN_STROKE = new java.awt.BasicStroke(0.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);
        final int TEXT_DISTANCE = (int) (0.08 * WIDTH);
        //final int MIN_LENGTH = (int) (0.0133333333 * WIDTH);
        final int MED_LENGTH = (int) (0.02 * WIDTH);
        final int MAX_LENGTH = (int) (0.04 * WIDTH);
        //final int MIN_DIAMETER = (int) (0.0093457944 * WIDTH);
        //final int MED_DIAMETER = (int) (0.0186915888 * WIDTH);
        //final int MAX_DIAMETER = (int) (0.0280373832 * WIDTH);
              
        // Create the ticks itself
        final float RADIUS = IMAGE_WIDTH * 0.38f;
        final java.awt.geom.Point2D GAUGE_CENTER = new java.awt.geom.Point2D.Double(IMAGE_WIDTH / 2.0f, IMAGE_HEIGHT / 2.0f);

        // Draw ticks
        final java.awt.geom.Point2D INNER_POINT = new java.awt.geom.Point2D.Double(0,0);
        final java.awt.geom.Point2D OUTER_POINT = new java.awt.geom.Point2D.Double(0,0);
        final java.awt.geom.Point2D TEXT_POINT = new java.awt.geom.Point2D.Double(0,0); 
        final java.awt.geom.Line2D TICK_LINE = new java.awt.geom.Line2D.Double(0, 0, 1, 1);
        final java.awt.geom.Ellipse2D TICK_CIRCLE = new java.awt.geom.Ellipse2D.Double(0, 0, 1, 1);               
        //final java.awt.geom.Rectangle2D TICK_RECT = new java.awt.geom.Rectangle2D.Double(0, 0, 1, 1);   
        
        //final int MINI_DIAMETER = (int) (0.0093457944 * WIDTH);
        final int MINOR_DIAMETER = (int) (0.0186915888 * WIDTH);
        final int MAJOR_DIAMETER = (int) (0.03 * WIDTH);
        
        int counter = 0;
        int tickCounter = 0;
        float valueCounter = 90;
        boolean countUp = false;
        float valueStep = 1;

        G2.setFont(STD_FONT);
        
        double sinValue = 0;
        double cosValue = 0;

        final double STEP = (2.0 * Math.PI) / (360.0);

        for (double alpha = (2.0 * Math.PI) ; alpha >= STEP; alpha -= STEP)
        {
            G2.setStroke(THIN_STROKE);
            sinValue = Math.sin(alpha - Math.PI / 2);
            cosValue = Math.cos(alpha - Math.PI / 2);

            // Different tickmark every 5 units
            if (counter % 5 == 0)
            {
                G2.setColor(super.getBackgroundColor().LABEL_COLOR);
                G2.setStroke(THIN_STROKE);
                INNER_POINT.setLocation(GAUGE_CENTER.getX() + (RADIUS - MED_LENGTH) * sinValue, GAUGE_CENTER.getY() + (RADIUS - MED_LENGTH) * cosValue);
                OUTER_POINT.setLocation(GAUGE_CENTER.getX() + RADIUS * sinValue, GAUGE_CENTER.getY() + RADIUS * cosValue);

                // Draw ticks
                switch(getMinorTickmarkType())
                {
                    case LINE:
                        TICK_LINE.setLine(INNER_POINT, OUTER_POINT);
                        G2.draw(TICK_LINE);
                        break;
                    case CIRCLE:
                        TICK_CIRCLE.setFrame(OUTER_POINT.getX() - MINOR_DIAMETER / 2.0, OUTER_POINT.getY() - MINOR_DIAMETER / 2.0, MINOR_DIAMETER, MINOR_DIAMETER);
                        G2.fill(TICK_CIRCLE);
                        break;                     
                    default:
                        TICK_LINE.setLine(INNER_POINT, OUTER_POINT);
                        G2.draw(TICK_LINE);
                        break;
                }                       
            }

            // Different tickmark every 45 units plus text
            if (counter == 30 || counter == 0)
            {
                G2.setColor(super.getBackgroundColor().LABEL_COLOR);
                G2.setStroke(MEDIUM_STROKE);
                INNER_POINT.setLocation(GAUGE_CENTER.getX() + (RADIUS - MAX_LENGTH) * sinValue, GAUGE_CENTER.getY() + (RADIUS - MAX_LENGTH) * cosValue);
                OUTER_POINT.setLocation(GAUGE_CENTER.getX() + RADIUS * sinValue, GAUGE_CENTER.getY() + RADIUS * cosValue);

                // Draw outer text
                TEXT_POINT.setLocation(GAUGE_CENTER.getX() + (RADIUS - TEXT_DISTANCE) * sinValue, GAUGE_CENTER.getY() + (RADIUS - TEXT_DISTANCE) * cosValue);
                G2.setFont(STD_FONT);                
                
                //G2.fill(UTIL.rotateTextAroundCenter(G2, String.valueOf((int) valueCounter), (int) textPoint.getX(), (int) textPoint.getY(), Math.toDegrees(Math.PI - alpha)));
                G2.fill(UTIL.rotateTextAroundCenter(G2, String.valueOf((int) valueCounter), (int) TEXT_POINT.getX(), (int) TEXT_POINT.getY(), Math.toDegrees(0)));
                
                counter = 0;
                tickCounter++;
                                                
                // Draw ticks
                switch(getMajorTickmarkType())
                {
                    case LINE:
                        TICK_LINE.setLine(INNER_POINT, OUTER_POINT);
                        G2.draw(TICK_LINE);
                        break;
                    case CIRCLE:
                        TICK_CIRCLE.setFrame(OUTER_POINT.getX() - MAJOR_DIAMETER / 2.0, OUTER_POINT.getY() - MAJOR_DIAMETER / 2.0, MAJOR_DIAMETER, MAJOR_DIAMETER);
                        G2.fill(TICK_CIRCLE);
                        break;                         
                    default:
                        TICK_LINE.setLine(INNER_POINT, OUTER_POINT);
                        G2.draw(TICK_LINE);
                        break;
                }             
            }

            counter++;
            if (valueCounter == 0)
            {
                countUp = true;                
            }
            if (valueCounter == 180)
            {
                countUp = false;                
            }
            if (countUp)
            {
                valueCounter += valueStep;                
            }
            else
            {
                valueCounter -= valueStep;                
            }
        }        

        G2.dispose();

        return image;
    }
    
    private java.awt.image.BufferedImage create_SHIP_Image(final int WIDTH, java.awt.image.BufferedImage image)
    {        
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        }
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

        final java.awt.geom.GeneralPath SHIP = new java.awt.geom.GeneralPath();
        SHIP.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        SHIP.moveTo(IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.7242990654205608);
        SHIP.curveTo(IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.7383177570093458, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7383177570093458);
        SHIP.curveTo(IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.7383177570093458, IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.7242990654205608);
        SHIP.curveTo(IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.5654205607476636, IMAGE_HEIGHT * 0.6962616822429907, IMAGE_WIDTH * 0.5654205607476636, IMAGE_HEIGHT * 0.48598130841121495);
        SHIP.curveTo(IMAGE_WIDTH * 0.5654205607476636, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.2336448598130841, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.2336448598130841);
        SHIP.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.2336448598130841, IMAGE_WIDTH * 0.42990654205607476, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.42990654205607476, IMAGE_HEIGHT * 0.48598130841121495);
        SHIP.curveTo(IMAGE_WIDTH * 0.42990654205607476, IMAGE_HEIGHT * 0.719626168224299, IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.7242990654205608);
        SHIP.closePath();        
        G2.setColor(getBackgroundColor().LABEL_COLOR);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(SHIP);

        G2.dispose();

        return image;
    }
        
    @Override
    public String toString()
    {
        return "WindDirection";
    }
}
