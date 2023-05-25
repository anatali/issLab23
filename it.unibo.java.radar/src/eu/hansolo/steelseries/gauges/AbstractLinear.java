package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public abstract class AbstractLinear extends AbstractGauge implements Lcd
{    
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">       
    private final java.awt.Rectangle INNER_BOUNDS;    
    // Bar related
    private boolean startingFromZero;  
    // LED related variables
    private java.awt.geom.Point2D ledPosition;
    // LCD related variables    
    private String lcdUnitString;        
    private double lcdValue;  
    private String lcdInfoString;
    protected static final java.awt.Font LCD_STANDARD_FONT = new java.awt.Font("Verdana", 1, 24);  
    protected static final java.awt.Font LCD_DIGITAL_FONT = eu.hansolo.steelseries.tools.Util.INSTANCE.getDigitalFont().deriveFont(24);    
    private org.pushingpixels.trident.Timeline lcdTimeline;       
    // Animation related variables
    private org.pushingpixels.trident.Timeline timeline;
    private final org.pushingpixels.trident.ease.TimelineEase STANDARD_EASING;
    private final org.pushingpixels.trident.ease.TimelineEase RETURN_TO_ZERO_EASING;
    private org.pushingpixels.trident.callback.TimelineCallback timelineCallback;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public AbstractLinear()
    {
        super();        
        INNER_BOUNDS = new java.awt.Rectangle(getMinimumSize());           
        startingFromZero = false;        
        ledPosition = new java.awt.geom.Point2D.Double((getInnerBounds().width - 18.0 - 16.0) / getInnerBounds().width, 0.453271028);
        lcdValue = 0;           
        lcdTimeline = new org.pushingpixels.trident.Timeline(this);
        lcdUnitString = getUnitString();      
        lcdInfoString = "";
        timeline = new org.pushingpixels.trident.Timeline(this);
        STANDARD_EASING = new org.pushingpixels.trident.ease.Spline(0.5f);
        RETURN_TO_ZERO_EASING = new org.pushingpixels.trident.ease.Sine();     
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Uses trident animation library to animate
     * the setting of the value.
     * The method plays a defined trident timeline
     * that calls the setValue(double value) method
     * with a given easing behaviour and duration.
     * You should always use this method to set the
     * gauge to a given value.
     * @param VALUE
     */
    public void setValueAnimated(final double VALUE)
    {        
        if (isEnabled())
        {
            if (timeline.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
            {
                timeline.abort();            
            }

            //double overallRange = getMaxValue() - getMinValue();
            //double range = Math.abs(getValue() - VALUE);
            //double fraction = range / overallRange;

            if (!isAutoResetToZero())
            {
                timeline.removeCallback(timelineCallback);
                timeline = new org.pushingpixels.trident.Timeline(this);
                timeline.addPropertyToInterpolate("value", getValue(), VALUE);
                timeline.setEase(STANDARD_EASING);
                //TIMELINE.setDuration((long) (getStdTimeToValue() * fraction));
                timeline.setDuration(getStdTimeToValue());
                timelineCallback = new org.pushingpixels.trident.callback.TimelineCallback()
                {        
            @Override
            public void onTimelineStateChanged(final org.pushingpixels.trident.Timeline.TimelineState OLD_STATE, final org.pushingpixels.trident.Timeline.TimelineState NEW_STATE, final float OLD_VALUE, final float NEW_VALUE)
            {
                if (NEW_STATE == org.pushingpixels.trident.Timeline.TimelineState.IDLE)
                {
                    repaint(getInnerBounds());
                }
                
                // Check if current value exceeds maxMeasuredValue
                if (getValue() > getMaxMeasuredValue())
                {
                    setMaxMeasuredValue(getValue());
                }
            }

            @Override
            public void onTimelinePulse(final float OLD_VALUE, final float NEW_VALUE)
            {                                      
                // Check if current value exceeds maxMeasuredValue
                if (getValue() > getMaxMeasuredValue())
                {
                    setMaxMeasuredValue(getValue());
                }

                // Check if current value exceeds minMeasuredValue
                if (getValue() < getMinMeasuredValue())
                {
                    setMinMeasuredValue(getValue());
                }
            }
        };
        
                timeline.addCallback(timelineCallback);
                timeline.play();
            } 
            else
            {
                final org.pushingpixels.trident.TimelineScenario AUTOZERO_SCENARIO = new org.pushingpixels.trident.TimelineScenario.Sequence();

                final org.pushingpixels.trident.Timeline TIMELINE_TO_VALUE = new org.pushingpixels.trident.Timeline(this);
                TIMELINE_TO_VALUE.addPropertyToInterpolate("value", getValue(), VALUE);
                TIMELINE_TO_VALUE.setEase(RETURN_TO_ZERO_EASING);
                //TIMELINE_TO_VALUE.setDuration((long) (getRtzTimeToValue() * fraction));
                TIMELINE_TO_VALUE.setDuration(getRtzTimeToValue());
                TIMELINE_TO_VALUE.addCallback(new org.pushingpixels.trident.callback.TimelineCallback()
                {
                    @Override
                    public void onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState oldState, org.pushingpixels.trident.Timeline.TimelineState newState, float oldValue, float newValue)
                    {            
                        if (oldState == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD && newState == org.pushingpixels.trident.Timeline.TimelineState.DONE)
                        {
                            // Set the peak value and start the timer
                            getModel().setPeakValue(getValue());
                            getModel().setPeakValueVisible(true);
                            if (getPeakTimer().isRunning())
                            {
                                stopPeakTimer();
                            }
                            startPeakTimer();

                            // Check if current value exceeds maxMeasuredValue
                            if (getValue() > getMaxMeasuredValue())
                            {
                                setMaxMeasuredValue(getValue());
                            }                    
                        }
                    }

                    @Override
                    public void onTimelinePulse(float oldValue, float newValue)
                    {                        
                        // Check if current value exceeds maxMeasuredValue
                        if (getValue() > getMaxMeasuredValue())
                        {
                            setMaxMeasuredValue(getValue());
                        }

                        // Check if current value exceeds minMeasuredValue
                        if (getValue() < getMinMeasuredValue())
                        {
                            setMinMeasuredValue(getValue());
                        }
                    }
                });

                final org.pushingpixels.trident.Timeline TIMELINE_TO_ZERO = new org.pushingpixels.trident.Timeline(this);
                TIMELINE_TO_ZERO.addPropertyToInterpolate("value", VALUE, 0.0);
                TIMELINE_TO_ZERO.setEase(RETURN_TO_ZERO_EASING);
                //TIMELINE_TO_ZERO.setDuration((long) (getRtzTimeBackToZero() * fraction));
                TIMELINE_TO_ZERO.setDuration(getRtzTimeBackToZero());

                AUTOZERO_SCENARIO.addScenarioActor(TIMELINE_TO_VALUE);
                AUTOZERO_SCENARIO.addScenarioActor(TIMELINE_TO_ZERO);

//                AUTOZERO_SCENARIO.addCallback(new org.pushingpixels.trident.callback.TimelineScenarioCallback()
//                {
//                    @Override
//                    public void onTimelineScenarioDone()
//                    {
//
//                    }
//                });

                AUTOZERO_SCENARIO.play();
            }                                 
        }
    }
 
    /**
     * Returns the color of the bar
     * @return the selected color of the bar
     */
    public eu.hansolo.steelseries.tools.ColorDef getValueColor()
    {
        return getModel().getValueColor();
    }

    /**
     * Sets the color of the bar
     * @param VALUE_COLOR
     */
    public void setValueColor(final eu.hansolo.steelseries.tools.ColorDef VALUE_COLOR)
    {
        getModel().setValueColor(VALUE_COLOR);
        repaint(getInnerBounds());
    }

    /**
     * Returns the object that represents holds the custom value color
     * @return the object that represents the custom value color
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomValueColorObject()
    {
        return getModel().getCustomValueColorObject();
    }
    
    /**
     * Returns the color of the bar from which the custom bar color will calculated
     * @return the color of the bar from which the custom bar color will be calculated
     */
    public java.awt.Color getCustomValueColor()
    {
        return getModel().getCustomValueColor();
    }
    
    /**
     * Sets the color of the bar from which the custom bar color will be calculated
     * @param COLOR 
     */
    public void setCustomValueColor(final java.awt.Color COLOR)
    {
        getModel().setCustomValueColorObject(new eu.hansolo.steelseries.tools.CustomColorDef(COLOR));
        repaint(getInnerBounds());
    }
    
    /**
     * Returns true if the bar/bargraph will always start from zero instead from
     * the minValue. This could be useful if you would like to create something
     * like a g-force meter, where 0 is in the center of the range and the bar
     * could move in negative and positive direction. In combination with 
     * AutoReturnToZero this feature might be useful.
     * @return true if the bar/bargraph will always start to in-/decrease from zero
     */
    public boolean isStartingFromZero()
    {
        return this.startingFromZero;
    }

    /**
     * Enables/Disables the feature that the bar/bargraph will always start from zero
     * instead from the minValue. This could be useful if you would like to create
     * something like a g-force meter, where 0 is in the center of the range and
     * the bar could move in negative and positive direction. In combination with
     * AutoReturnToZero this feature might be useful.
     * @param STARTING_FROM_ZERO
     */
    public void setStartingFromZero(final boolean STARTING_FROM_ZERO)
    {
        this.startingFromZero = STARTING_FROM_ZERO;
    }

    public int getMaxNoOfMinorTicks()
    {
        return getModel().getMaxNoOfMinorTicks();
    }
    
    public void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS)
    {
        getModel().setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    public int getMaxNoOfMajorTicks()
    {
        return getModel().getMaxNoOfMajorTicks();
    }
    
    public void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS)
    {
        getModel().setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the current position of the gauge threshold led
     * @return the current position of the gauge threshold led
     */
    @Override
    public java.awt.geom.Point2D getLedPosition()
    {
        return ledPosition;
    }
    
    /**
     * Sets the position of the gauge threshold led to the given values
     * @param X
     * @param Y 
     */
    @Override
    public void setLedPosition(final double X, final double Y)
    {
        ledPosition.setLocation(X, Y);
        repaint(getInnerBounds());
    }
    
    /**
     * Sets the position of the gauge threshold led to the given values
     * @param LED_POSITION 
     */
    public void setLedPosition(final java.awt.geom.Point2D LED_POSITION)
    {
        ledPosition.setLocation(LED_POSITION);
        repaint(getInnerBounds());
    }
    
    protected void createLedImages()
    {
        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {
            recreateLedImages(getWidth());
        }
        else
        {
            recreateLedImages(getHeight());
        }
    }
    
    /**
     * Returns the visibility of the lcd display
     * @return true if the lcd display is visible
     */
    public boolean isLcdVisible()
    {
        return getModel().isLcdVisible();        
    }
    
    /**
     * Enables or disables the visibility of the lcd display
     * @param LCD_VISIBLE 
     */
    public void setLcdVisible(final boolean LCD_VISIBLE)
    {
        getModel().setLcdVisible(LCD_VISIBLE);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    @Override
    public boolean isValueCoupled()
    {
        return getModel().isValueCoupled();        
    }
    
    @Override
    public void setValueCoupled(final boolean VALUE_COUPLED)
    {
        getModel().setValueCoupled(VALUE_COUPLED);        
        repaint(getInnerBounds());
    }
    
    @Override
    public double getLcdValue()
    {
        return this.lcdValue;
    }

    @Override
    public void setLcdValue(final double LCD_VALUE)
    {
        this.lcdValue = LCD_VALUE;
        repaint(getInnerBounds());
    }

    @Override
    public void setLcdValueAnimated(final double LCD_VALUE)
    {
        if (lcdTimeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD || lcdTimeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_REVERSE)
        {
            lcdTimeline.abort();
        }
        lcdTimeline = new org.pushingpixels.trident.Timeline(this);
        lcdTimeline.addPropertyToInterpolate("lcdValue", this.lcdValue, LCD_VALUE);
        lcdTimeline.setEase(new org.pushingpixels.trident.ease.Spline(0.5f));
  
        lcdTimeline.play();
    }

    @Override
    public String getLcdUnitString()
    {
        return lcdUnitString;
    }

    @Override
    public void setLcdUnitString(final String UNIT_STRING)
    {
        this.lcdUnitString = UNIT_STRING;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public boolean isLcdUnitStringVisible()
    {
        return getModel().isLcdUnitStringVisible();        
    }

    @Override
    public void setLcdUnitStringVisible(final boolean UNIT_STRING_VISIBLE)
    {
        getModel().setLcdUnitStringVisible(UNIT_STRING_VISIBLE);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public boolean isDigitalFont()
    {
        return getModel().isDigitalFontEnabled();        
    }

    @Override
    public void setDigitalFont(final boolean DIGITAL_FONT)
    {
        getModel().setDigitalFontEnabled(DIGITAL_FONT);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public boolean isCustomLcdUnitFontEnabled()
    {
        return getModel().isCustomLcdUnitFontEnabled();        
    }

    @Override
    public void setCustomLcdUnitFontEnabled(final boolean USE_CUSTOM_LCD_UNIT_FONT)
    {
        getModel().setCustomLcdUnitFontEnabled(USE_CUSTOM_LCD_UNIT_FONT);        
        repaint(getInnerBounds());
    }

    @Override
    public java.awt.Font getCustomLcdUnitFont()
    {
        return getModel().getCustomLcdUnitFont();        
    }

    @Override
    public void setCustomLcdUnitFont(final java.awt.Font CUSTOM_LCD_UNIT_FONT)
    {
        getModel().setCustomLcdUnitFont(CUSTOM_LCD_UNIT_FONT);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public int getLcdDecimals()
    {
        return getModel().getLcdDecimals();        
    }

    @Override
    public void setLcdDecimals(final int DECIMALS)
    {
        getModel().setLcdDecimals(DECIMALS);        
        repaint(getInnerBounds());
    }

    @Override
    public eu.hansolo.steelseries.tools.LcdColor getLcdColor()
    {
        return getModel().getLcdColor();
    }

    @Override
    public void setLcdColor(final eu.hansolo.steelseries.tools.LcdColor COLOR)
    {
        getModel().setLcdColor(COLOR);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public java.awt.Paint getCustomLcdBackground()
    {
        return getModel().getCustomLcdBackground();
    }
    
    @Override
    public void setCustomLcdBackground(final java.awt.Paint CUSTOM_LCD_BACKGROUND)
    {
        getModel().setCustomLcdBackground(CUSTOM_LCD_BACKGROUND);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    @Override
    public java.awt.Color getCustomLcdForeground()
    {
        return getModel().getCustomLcdForeground();        
    }
    
    @Override
    public void setCustomLcdForeground(final java.awt.Color CUSTOM_LCD_FOREGROUND)
    {
        getModel().setCustomLcdForeground(CUSTOM_LCD_FOREGROUND);        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    @Override
    public String formatLcdValue(final double VALUE)
    {
        final StringBuilder DEC_BUFFER = new StringBuilder(16);
        DEC_BUFFER.append("0");

        if (getModel().getLcdDecimals() > 0)
        {
            DEC_BUFFER.append(".");
        }

        for (int i = 0 ; i < getModel().getLcdDecimals() ; i++)
        {
            DEC_BUFFER.append("0");
        }

        if(getModel().isLcdScientificFormatEnabled())
        {
            DEC_BUFFER.append("E0");
        }
        DEC_BUFFER.trimToSize();
        final java.text.DecimalFormat DEC_FORMAT = new java.text.DecimalFormat(DEC_BUFFER.toString(), new java.text.DecimalFormatSymbols(java.util.Locale.US));

        return DEC_FORMAT.format(VALUE);
    }

    @Override
    public boolean isLcdScientificFormat()
    {
        return getModel().isLcdScientificFormatEnabled();
    }

    @Override
    public void setLcdScientificFormat(boolean LCD_SCIENTIFIC_FORMAT)
    {
        getModel().setLcdScientificFormatEnabled(LCD_SCIENTIFIC_FORMAT);
        repaint(getInnerBounds());
    }
    
    @Override
    public java.awt.Font getLcdValueFont()
    {
        return getModel().getLcdValueFont();
    }
    
    @Override
    public void setLcdValueFont(final java.awt.Font LCD_VALUE_FONT)
    {
        getModel().setLcdValueFont(LCD_VALUE_FONT);
        repaint(getInnerBounds());
    }
    
    @Override
    public java.awt.Font getLcdUnitFont()
    {
        return getModel().getLcdUnitFont();
    }
    
    @Override
    public void setLcdUnitFont(final java.awt.Font LCD_UNIT_FONT)
    {
        getModel().setLcdUnitFont(LCD_UNIT_FONT);
        repaint(getInnerBounds());
    }
    
    @Override
    public java.awt.Font getLcdInfoFont()
    {
        return getModel().getLcdInfoFont();
    }
    
    @Override
    public void setLcdInfoFont(final java.awt.Font LCD_INFO_FONT)
    {
        getModel().setLcdInfoFont(LCD_INFO_FONT);
        repaint(getInnerBounds());
    }
    
    @Override
    public String getLcdInfoString()
    {
        return lcdInfoString;
    }
    
    @Override
    public void setLcdInfoString(final String LCD_INFO_STRING)
    {
        lcdInfoString = LCD_INFO_STRING;
        repaint(getInnerBounds());
    }
    
    @Override
    public eu.hansolo.steelseries.tools.NumberSystem getLcdNumberSystem()
    {
        return getModel().getNumberSystem();
    }
    
    @Override
    public void setLcdNumberSystem(final eu.hansolo.steelseries.tools.NumberSystem NUMBER_SYSTEM)
    {
        getModel().setNumberSystem(NUMBER_SYSTEM);
        switch(NUMBER_SYSTEM)
        {
            case HEX:
                lcdInfoString = "hex";
                break;
            case OCT:
                lcdInfoString = "oct";
                break;
            case DEC:
                
            default:
                lcdInfoString = "";
                break;
        }
        repaint(getInnerBounds());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Image creation methods">
    /**
     * Returns the frame image with the currently active framedesign
     * with the given with and height.
     * @param WIDTH
     * @param HEIGHT
     * @return buffered image containing the frame in the active frame design
     */
    protected java.awt.image.BufferedImage create_FRAME_Image(final int WIDTH, final int HEIGHT)
    {
        return FRAME_FACTORY.createLinearFrame(WIDTH, HEIGHT, getFrameDesign(), getCustomFrameDesign(), getFrameEffect());
    }

    /**
     * Returns the frame image with the currently active framedesign
     * with the given with and height.
     * @param WIDTH
     * @param HEIGHT
     * @param IMAGE 
     * @return buffered image containing the frame in the active frame design
     */
    protected java.awt.image.BufferedImage create_FRAME_Image(final int WIDTH, final int HEIGHT, final java.awt.image.BufferedImage IMAGE)
    {
        return FRAME_FACTORY.createLinearFrame(WIDTH, HEIGHT, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), IMAGE);
    }
    
    /**
     * Returns the background image with the currently active backgroundcolor
     * with the given width and height.
     * @param WIDTH
     * @param HEIGHT
     * @return buffered image containing the background with the selected background design
     */
    protected java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH, final int HEIGHT)
    {        
        return create_BACKGROUND_Image(WIDTH, HEIGHT, null);
    }

    /**
     * Returns the background image with the currently active backgroundcolor
     * with the given width and height.
     * @param WIDTH
     * @param HEIGHT
     * @param image
     * @return buffered image containing the background with the selected background design
     */
    protected java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH, final int HEIGHT, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();
       
        // Draw the background image
        BACKGROUND_FACTORY.createLinearBackground(WIDTH, HEIGHT, getBackgroundColor(), getModel().getCustomBackground(), image);
                
        // Draw the custom layer if selected
        if (isCustomLayerVisible())
        {
            G2.drawImage(UTIL.getScaledInstance(getCustomLayer(), IMAGE_WIDTH, IMAGE_HEIGHT, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC), 0, 0, null);
        }
                
        G2.dispose();

        return image;
    }
    
    /**
     * Returns the track image with the given values.
     * @param WIDTH
     * @param HEIGHT
     * @param MIN_VALUE
     * @param MAX_VALUE
     * @param TRACK_START
     * @param TRACK_SECTION
     * @param TRACK_STOP
     * @param TRACK_START_COLOR
     * @param TRACK_SECTION_COLOR
     * @param TRACK_STOP_COLOR
     * @return a buffered image of the track colored with the given values
     */
    protected java.awt.image.BufferedImage create_TRACK_Image(final int WIDTH, final int HEIGHT, final double MIN_VALUE, final double MAX_VALUE, final double TRACK_START, final double TRACK_SECTION, final double TRACK_STOP, final java.awt.Color TRACK_START_COLOR, final java.awt.Color TRACK_SECTION_COLOR, final java.awt.Color TRACK_STOP_COLOR)
    {        
        return create_TRACK_Image(WIDTH, HEIGHT, MIN_VALUE, MAX_VALUE, TRACK_START, TRACK_SECTION, TRACK_STOP, TRACK_START_COLOR, TRACK_SECTION_COLOR, TRACK_STOP_COLOR, null);
    }

    /**
     * Returns the track image with the given values.
     * @param WIDTH
     * @param HEIGHT
     * @param MIN_VALUE
     * @param MAX_VALUE
     * @param TRACK_START
     * @param TRACK_SECTION
     * @param TRACK_STOP
     * @param TRACK_START_COLOR
     * @param TRACK_SECTION_COLOR
     * @param TRACK_STOP_COLOR
     * @param image
     * @return a buffered image of the track colored with the given values
     */
    protected java.awt.image.BufferedImage create_TRACK_Image(final int WIDTH, final int HEIGHT, final double MIN_VALUE, final double MAX_VALUE, final double TRACK_START, final double TRACK_SECTION, final double TRACK_STOP, final java.awt.Color TRACK_START_COLOR, final java.awt.Color TRACK_SECTION_COLOR, final java.awt.Color TRACK_STOP_COLOR, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        if (TRACK_STOP > MAX_VALUE)
        {
            throw new IllegalArgumentException("Please adjust track start and/or track stop values");
        }
        
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();
        
        final java.awt.geom.Rectangle2D TRACK;
        final java.awt.geom.Point2D TRACK_START_POINT;
        final java.awt.geom.Point2D TRACK_STOP_POINT;

        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {            
                // Vertical orientation
                TRACK = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.315, IMAGE_HEIGHT * 0.1276, IMAGE_WIDTH * 0.05, IMAGE_HEIGHT * 0.7280);
                TRACK_START_POINT = new java.awt.geom.Point2D.Double(0, TRACK.getMaxY());
                TRACK_STOP_POINT = new java.awt.geom.Point2D.Double(0, TRACK.getMinY());
        }
        else
        {
                // Horizontal orientation
                TRACK = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.139, IMAGE_HEIGHT * 0.6285714285714286, IMAGE_WIDTH * 0.735, IMAGE_HEIGHT * 0.05);
                TRACK_START_POINT = new java.awt.geom.Point2D.Double(TRACK.getMinX(), 0);
                TRACK_STOP_POINT = new java.awt.geom.Point2D.Double(TRACK.getMaxX(), 0);        
        }
        
        // Calculate the track start and stop position for the gradient
        final float TRACK_START_POSITION = (float) ((TRACK_START - MIN_VALUE) / (MAX_VALUE - MIN_VALUE));
        final float TRACK_STOP_POSITION = (float) ((TRACK_STOP - MIN_VALUE) / (MAX_VALUE - MIN_VALUE));

        final java.awt.Color FULLY_TRANSPARENT = new java.awt.Color(0.0f, 0.0f, 0.0f, 0.0f);

        final float[] TRACK_FRACTIONS;
        final java.awt.Color[] TRACK_COLORS;
        
        // Three color gradient from trackStart over trackSection to trackStop
        final float TRACK_SECTION_POSITION = (float) ((TRACK_SECTION - MIN_VALUE) / (MAX_VALUE - MIN_VALUE));
        
        TRACK_FRACTIONS = new float[]
        {
            0.0f,
            TRACK_START_POSITION + 0.001f,
            TRACK_START_POSITION + 0.002f,
            TRACK_SECTION_POSITION,
            TRACK_STOP_POSITION - 0.002f,
            TRACK_STOP_POSITION - 0.001f,
            1.0f
        };

        TRACK_COLORS = new java.awt.Color[]
        {
            FULLY_TRANSPARENT,
            FULLY_TRANSPARENT,
            TRACK_START_COLOR,
            TRACK_SECTION_COLOR,
            TRACK_STOP_COLOR,
            FULLY_TRANSPARENT,
            FULLY_TRANSPARENT
        };
       
        final java.awt.LinearGradientPaint TRACK_GRADIENT = new java.awt.LinearGradientPaint(TRACK_START_POINT, TRACK_STOP_POINT, TRACK_FRACTIONS, TRACK_COLORS);
        G2.setPaint(TRACK_GRADIENT);
        G2.fill(TRACK);

        G2.dispose();

        return image;
    }
    
    /**
     * Returns the image of the title.
     * @param WIDTH
     * @param HEIGHT
     * @param UNIT_STRING_VISIBLE
     * @return a buffered image of the title and unit string
     */
    protected java.awt.image.BufferedImage create_TITLE_Image(final int WIDTH, final int HEIGHT, final boolean UNIT_STRING_VISIBLE)
    {        
        return create_TITLE_Image(WIDTH, HEIGHT, UNIT_STRING_VISIBLE, null);
    }

    /**
     * Returns the image of the title.
     * @param WIDTH
     * @param HEIGHT
     * @param UNIT_STRING_VISIBLE
     * @param image
     * @return a buffered image of the title and unit string
     */
    protected java.awt.image.BufferedImage create_TITLE_Image(final int WIDTH, final int HEIGHT, final boolean UNIT_STRING_VISIBLE, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);

        if (isLabelColorFromThemeEnabled())
        {
            G2.setColor(getBackgroundColor().LABEL_COLOR);
        }
        else
        {
            G2.setColor(getLabelColor());
        }

        final java.awt.font.TextLayout LAYOUT_TITLE;

        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {            
                // Vertical orientation
            // Draw title
            // Use custom font if selected
            if (isTitleAndUnitFontEnabled())
            {
                G2.setFont(new java.awt.Font(getTitleAndUnitFont().getFamily(), getTitleAndUnitFont().getStyle(), getTitleAndUnitFont().getSize()));
            }
            else
            {
                G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.1 * IMAGE_WIDTH)));
            }
            LAYOUT_TITLE = new java.awt.font.TextLayout(getTitle(), G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
            G2.translate(0.0, -0.05 * IMAGE_HEIGHT);
            G2.rotate(1.5707963267948966, 0.6714285714285714f * IMAGE_WIDTH, 0.1375f * IMAGE_HEIGHT + LAYOUT_TITLE.getAscent() - LAYOUT_TITLE.getDescent());
            G2.drawString(getTitle(), 0.6714285714285714f * IMAGE_WIDTH, 0.1375f * IMAGE_HEIGHT + LAYOUT_TITLE.getAscent() - LAYOUT_TITLE.getDescent());
            G2.setTransform(OLD_TRANSFORM);

            // Draw unit string
            if (UNIT_STRING_VISIBLE)
            {
                if (isTitleAndUnitFontEnabled())
                {
                    G2.setFont(new java.awt.Font(getTitleAndUnitFont().getFamily(), 0, (int) (0.07142857142857142 * IMAGE_WIDTH)));
                }
                else
                {
                    G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.07142857142857142 * IMAGE_WIDTH)));
                }
                final java.awt.font.TextLayout LAYOUT_UNIT = new java.awt.font.TextLayout(getUnitString(), G2.getFont(), RENDER_CONTEXT);
                final java.awt.geom.Rectangle2D UNIT_BOUNDARY = LAYOUT_UNIT.getBounds();
                G2.drawString(getUnitString(), (float) (IMAGE_WIDTH - UNIT_BOUNDARY.getWidth()) / 2f, 0.8875f * IMAGE_HEIGHT + LAYOUT_UNIT.getAscent() - LAYOUT_UNIT.getDescent());
            }
        }
        else
        {
            // Horizontal orientation
            // Draw title
            if (isTitleAndUnitFontEnabled())
            {
                G2.setFont(new java.awt.Font(getTitleAndUnitFont().getFamily(), 0, (int) (0.1 * IMAGE_HEIGHT)));
            }
            else
            {
                G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.1 * IMAGE_HEIGHT)));
            }
            LAYOUT_TITLE = new java.awt.font.TextLayout(getTitle(), G2.getFont(), RENDER_CONTEXT);
            G2.drawString(getTitle(), 0.15f * IMAGE_WIDTH, 0.25f * IMAGE_HEIGHT + LAYOUT_TITLE.getAscent() - LAYOUT_TITLE.getDescent());

            // Draw unit string
            if (UNIT_STRING_VISIBLE)
            {
                if (isTitleAndUnitFontEnabled())
                {
                    G2.setFont(new java.awt.Font(getTitleAndUnitFont().getFamily(), 0, (int) (0.025 * IMAGE_WIDTH)));
                }
                else
                {
                    G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.025 * IMAGE_WIDTH)));
                }
                final java.awt.font.TextLayout LAYOUT_UNIT = new java.awt.font.TextLayout(getUnitString(), G2.getFont(), RENDER_CONTEXT);
                G2.drawString(getUnitString(), 0.0625f * IMAGE_WIDTH, 0.7f * IMAGE_HEIGHT + LAYOUT_UNIT.getAscent() - LAYOUT_UNIT.getDescent());
            }
        }

        G2.dispose();

        return image;
    }
    
    /**
     * Returns the image with the given lcd color.
     * @param WIDTH
     * @param HEIGHT
     * @param LCD_COLOR
     * @param CUSTOM_LCD_BACKGROUND 
     * @return buffered image containing the lcd with the selected lcd color
     */
    protected java.awt.image.BufferedImage create_LCD_Image(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.LcdColor LCD_COLOR, final java.awt.Paint CUSTOM_LCD_BACKGROUND)
    {        
        return create_LCD_Image(new java.awt.geom.Rectangle2D.Double(0, 0, WIDTH, HEIGHT), LCD_COLOR, CUSTOM_LCD_BACKGROUND, null);
    }
    
    /**
     * Returns the image with the given lcd color.
     * @param BOUNDS 
     * @param LCD_COLOR
     * @param CUSTOM_LCD_BACKGROUND 
     * @param IMAGE 
     * @return buffered image containing the lcd with the selected lcd color
     */
    protected java.awt.image.BufferedImage create_LCD_Image(final java.awt.geom.Rectangle2D BOUNDS, final eu.hansolo.steelseries.tools.LcdColor LCD_COLOR, final java.awt.Paint CUSTOM_LCD_BACKGROUND, final java.awt.image.BufferedImage IMAGE)
    {
        return LCD_FACTORY.create_LCD_Image(BOUNDS, LCD_COLOR, CUSTOM_LCD_BACKGROUND, IMAGE);    
    }
        
    /**
     * Returns the image of the threshold indicator
     * @param WIDTH
     * @param HEIGHT
     * @return a buffered image of the threshold indicator
     */
    protected java.awt.image.BufferedImage create_THRESHOLD_Image(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 14 || HEIGHT <= 14) // 14 is needed otherwise the image size could be smaller than 1
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
                
        final int IMAGE_WIDTH;
        final int IMAGE_HEIGHT;
        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {            
                // Vertical orientation
                IMAGE_WIDTH = (int) (WIDTH * 0.0714285714);
                IMAGE_HEIGHT = (int) (IMAGE_WIDTH * 0.8);
        }
        else
        {
                // Horizontal orientation
                IMAGE_HEIGHT = (int) (HEIGHT * 0.0714285714);
                IMAGE_WIDTH = (int) (IMAGE_HEIGHT * 0.8);        
        }
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(IMAGE_WIDTH, IMAGE_HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        G2.translate(0, IMAGE_WIDTH * 0.005);
        final java.awt.geom.GeneralPath THRESHOLD = new java.awt.geom.GeneralPath();
        THRESHOLD.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        final java.awt.geom.Point2D THRESHOLD_START;
        final java.awt.geom.Point2D THRESHOLD_STOP;

        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {
            // Vertical orientation
            THRESHOLD.moveTo(IMAGE_WIDTH * 0.1, IMAGE_HEIGHT * 0.5);
            THRESHOLD.lineTo(IMAGE_WIDTH * 0.9, IMAGE_HEIGHT * 0.1);
            THRESHOLD.lineTo(IMAGE_WIDTH * 0.9, IMAGE_HEIGHT * 0.9);
            THRESHOLD.lineTo(IMAGE_WIDTH * 0.1, IMAGE_HEIGHT * 0.5);
            THRESHOLD.closePath();
            THRESHOLD_START = new java.awt.geom.Point2D.Double(THRESHOLD.getBounds2D().getMinX(), 0);
            THRESHOLD_STOP = new java.awt.geom.Point2D.Double(THRESHOLD.getBounds2D().getMaxX(), 0);
        }
        else
        {
            // Horizontal orientation
            THRESHOLD.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.9);
            THRESHOLD.lineTo(IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.1);
            THRESHOLD.lineTo(IMAGE_WIDTH * 0.1, IMAGE_HEIGHT * 0.1);
            THRESHOLD.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.9);
            THRESHOLD.closePath();
            THRESHOLD_START = new java.awt.geom.Point2D.Double(0, THRESHOLD.getBounds2D().getMaxY() );
            THRESHOLD_STOP = new java.awt.geom.Point2D.Double(0, THRESHOLD.getBounds2D().getMinY() );
        }
        final float[] THRESHOLD_FRACTIONS =
        {
            0.0f,
            0.3f,
            0.59f,
            1.0f
        };
        final java.awt.Color[] THRESHOLD_COLORS =
        {
            getThresholdColor().DARK,
            getThresholdColor().MEDIUM,
            getThresholdColor().MEDIUM,
            getThresholdColor().DARK            
        };
        final java.awt.LinearGradientPaint THRESHOLD_GRADIENT = new java.awt.LinearGradientPaint(THRESHOLD_START, THRESHOLD_STOP, THRESHOLD_FRACTIONS, THRESHOLD_COLORS);
        G2.setPaint(THRESHOLD_GRADIENT);
        G2.fill(THRESHOLD);
                
        G2.setColor(java.awt.Color.WHITE);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(THRESHOLD);

        G2.dispose();

        return IMAGE;
    }

    /**
     * Returns the image of the MinMeasuredValue and MaxMeasuredValue dependend
     * on the given color
     * @param WIDTH
     * @param HEIGHT
     * @param COLOR
     * @return a buffered image of either the min measured value or the max measured value indicator
     */
    protected java.awt.image.BufferedImage create_MEASURED_VALUE_Image(final int WIDTH, final int HEIGHT, final java.awt.Color COLOR)
    {
        if (WIDTH <= 20 || HEIGHT <= 20) // 20 is needed otherwise the image size could be smaller than 1
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
                
        final int IMAGE_WIDTH;
        final int IMAGE_HEIGHT;
        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {
            // Vertical orientation
            IMAGE_WIDTH = (int) (WIDTH * 0.05);
            IMAGE_HEIGHT = IMAGE_WIDTH;
        }
        else
        {
            // Horizontal orientation
            IMAGE_HEIGHT = (int) (HEIGHT * 0.05);
            IMAGE_WIDTH = IMAGE_HEIGHT;
        }
       
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(IMAGE_WIDTH, IMAGE_HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);  

        final java.awt.geom.GeneralPath INDICATOR = new java.awt.geom.GeneralPath();
        INDICATOR.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {
            INDICATOR.moveTo(IMAGE_WIDTH, IMAGE_HEIGHT * 0.5);
            INDICATOR.lineTo(0.0, 0.0);
            INDICATOR.lineTo(0.0, IMAGE_HEIGHT);
            INDICATOR.lineTo(IMAGE_WIDTH, IMAGE_HEIGHT * 0.5);
            INDICATOR.closePath();
        }
        else
        {
            INDICATOR.moveTo(IMAGE_WIDTH * 0.5, 0.0);
            INDICATOR.lineTo(IMAGE_WIDTH, IMAGE_HEIGHT);
            INDICATOR.lineTo(0.0, IMAGE_HEIGHT);
            INDICATOR.lineTo(IMAGE_WIDTH * 0.5, 0.0);
            INDICATOR.closePath();
        }
        G2.setColor(COLOR);
        G2.fill(INDICATOR);

        G2.dispose();

        return IMAGE;
    }

    /**
     * Returns the image of the glasseffect image
     * @param WIDTH
     * @param HEIGHT
     * @return a buffered image of the foreground glass effect
     */
    protected java.awt.image.BufferedImage create_FOREGROUND_Image(final int WIDTH, final int HEIGHT)
    {
        return FOREGROUND_FACTORY.createLinearForeground(WIDTH, HEIGHT);
    }
        
    /**
     * Returns the image that will be displayed if the gauge is disabled
     * @param WIDTH
     * @param HEIGHT 
     * @return the disabled image that will be displayed if the gauge is disabled
     */
    protected java.awt.image.BufferedImage create_DISABLED_Image(final int WIDTH, final int HEIGHT)
    {
        return DISABLED_FACTORY.createLinearDisabled(WIDTH, HEIGHT);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Size related methods">    
    @Override
    public void calcInnerBounds()
    {
        final java.awt.Insets INSETS = getInsets();
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, getWidth() - INSETS.left - INSETS.right, getHeight() - INSETS.top - INSETS.bottom);        
    }

    /**
     * Returns the rectangle that is defined by the dimension of the component and
     * it's insets given by e.g. a border.
     * @return rectangle that defines the inner area available for painting
     */
    @Override
    public final java.awt.Rectangle getInnerBounds()
    {
        return INNER_BOUNDS;
    }

    @Override
    public java.awt.Dimension getMinimumSize()
    {        
        return new java.awt.Dimension(140, 140);
    }
            
    @Override
    public void setPreferredSize(final java.awt.Dimension DIM)
    {        
        super.setPreferredSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final int WIDTH, final int HEIGHT)
    {
        super.setSize(WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final java.awt.Dimension DIM)
    {
        super.setSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final java.awt.Rectangle BOUNDS)
    {
        super.setBounds(BOUNDS);
        calcInnerBounds();
        init(BOUNDS.width, BOUNDS.height);        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        super.setBounds(X, Y, WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);        
        setInitialized(true);
        revalidate();
        repaint();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ComponentListener methods">
    // ComponentListener methods
    @Override
    public void componentResized(final java.awt.event.ComponentEvent EVENT)
    {        
        final java.awt.Container PARENT = getParent();
        if ((PARENT != null) && (PARENT.getLayout() == null))
        {        
            setSize(getWidth(), getHeight());
        }
        else
        {
            setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
        }
        
        calcInnerBounds();
                       
        if (getWidth() >= getHeight())
        {
            // Horizontal
            setOrientation(eu.hansolo.steelseries.tools.Orientation.HORIZONTAL);
            recreateLedImages(getInnerBounds().height);
            
            if (isLedOn())
            {
                setCurrentLedImage(getLedImageOn());
            }
            else
            {
                setCurrentLedImage(getLedImageOff());
            }
            setLedPosition((getInnerBounds().width - 18.0 - 16.0) / getInnerBounds().width, 0.453271028);
        }
        else
        {
            // Vertical
            setOrientation(eu.hansolo.steelseries.tools.Orientation.VERTICAL);
            recreateLedImages(getInnerBounds().width);
            
            if (isLedOn())
            {
                setCurrentLedImage(getLedImageOn());
            }
            else
            {
                setCurrentLedImage(getLedImageOff());
            }
            setLedPosition(0.453271028, (18.0 / getInnerBounds().height));
        }        
        getModel().setSize(getLocation().x, getLocation().y, getWidth(), getHeight());    
        init(getInnerBounds().width, getInnerBounds().height);  
        revalidate();
        repaint();
    }
    // </editor-fold>
 
    @Override
    public String toString()
    {
        return "AbstractLinear";
    }
}
