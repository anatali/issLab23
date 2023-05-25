package eu.hansolo.steelseries.gauges;

/**
 * The "mother" of most of the gauges in the steelseries library.
 * It contains common methods that are used in most of the gauges, no
 * matter if they are radial or linear.
 * @author hansolo
 */
public abstract class AbstractGauge extends javax.swing.JComponent implements java.awt.event.ComponentListener, java.awt.event.ActionListener, java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">
    private static final long serialVersionUID = 31269L;
    public static final String VALUE_PROPERTY = "Value";
    private java.beans.PropertyChangeSupport propertySupport;
    protected static final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.FrameImageFactory FRAME_FACTORY = eu.hansolo.steelseries.tools.FrameImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.BackgroundImageFactory BACKGROUND_FACTORY = eu.hansolo.steelseries.tools.BackgroundImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.TickmarkImageFactory TICKMARK_FACTORY = eu.hansolo.steelseries.tools.TickmarkImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.LcdImageFactory LCD_FACTORY = eu.hansolo.steelseries.tools.LcdImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.LedImageFactory LED_FACTORY = eu.hansolo.steelseries.tools.LedImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.KnobImageFactory KNOB_FACTORY = eu.hansolo.steelseries.tools.KnobImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.PointerImageFactory POINTER_FACTORY = eu.hansolo.steelseries.tools.PointerImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.ForegroundImageFactory FOREGROUND_FACTORY = eu.hansolo.steelseries.tools.ForegroundImageFactory.INSTANCE;
    protected static final eu.hansolo.steelseries.tools.DisabledImageFactory DISABLED_FACTORY = eu.hansolo.steelseries.tools.DisabledImageFactory.INSTANCE;
    // Initialization
    private boolean initialized;
    // Models
    private volatile eu.hansolo.steelseries.tools.Model model;
    // Value related
    private javax.swing.event.ChangeEvent changeEvent;
    private final javax.swing.event.EventListenerList LISTENER_LIST = new javax.swing.event.EventListenerList();
    protected static final String THRESHOLD_PROPERTY = "Threshold";
    private java.awt.image.BufferedImage ledImageOff;
    private java.awt.image.BufferedImage ledImageOn;
    private java.awt.image.BufferedImage currentLedImage;
    private final javax.swing.Timer LED_BLINKING_TIMER;
    private boolean ledBlinking;
    private boolean ledOn;
    // Peak value related
    private final javax.swing.Timer PEAK_TIMER;
    // Tickmark related            
    private boolean customTickmarkLabelsEnabled;
    private java.util.ArrayList<Double> customTickmarkLabels;
    // Title and unit related
    private String title;
    private String unitString;
    private java.awt.Font titleAndUnitFont;
    // Timeline
    private long stdTimeToValue;
    private long rtzTimeToValue;
    private long rtzTimeBackToZero;
    // Orientation
    private eu.hansolo.steelseries.tools.Orientation orientation;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public AbstractGauge()
    {
        super();
        propertySupport = new java.beans.PropertyChangeSupport(this);
        addComponentListener(this);
        initialized = false;
        model = new eu.hansolo.steelseries.tools.Model();

        ledImageOff = create_LED_Image(200, 0, model.getLedColor());
        ledImageOn = create_LED_Image(200, 1, model.getLedColor());
        LED_BLINKING_TIMER = new javax.swing.Timer(500, this);
        ledOn = false;
        ledBlinking = false;
        PEAK_TIMER = new javax.swing.Timer(1000, this);
        customTickmarkLabelsEnabled = false;
        customTickmarkLabels = new java.util.ArrayList<Double>(10);
        title = "Title";
        unitString = "unit";
        titleAndUnitFont = new java.awt.Font("Verdana", 0, 10);
        stdTimeToValue = 800;
        rtzTimeToValue = 800;
        rtzTimeBackToZero = 1200;
        orientation = eu.hansolo.steelseries.tools.Orientation.NORTH;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    /**
     * In the init method all the images will be created.
     * @param WIDTH
     * @param HEIGHT
     * @return a instance of the current gauge
     */
    abstract public AbstractGauge init(final int WIDTH, final int HEIGHT);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter/Setter">
    /**
     * Returns true if the component is initialized and ready to display
     * @return a boolean that represents the initialzation state
     */
    public boolean isInitialized()
    {
        return this.initialized;
    }

    /**
     * Sets the state of initialization of the component
     * @param INITIALIZED 
     */
    public void setInitialized(final boolean INITIALIZED)
    {
        this.initialized = INITIALIZED;
    }

    /**
     * Returns the state model of the gauge
     * @return the state model of the gauge
     */
    public eu.hansolo.steelseries.tools.Model getModel()
    {
        return model;
    }

    /**
     * Sets the state model of the gauge
     * @param MODEL 
     */
    public void setModel(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        this.model = MODEL;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the value of the gauge as a double
     * @return the value of the gauge
     */
    public double getValue()
    {
        return model.getValue();
    }

    /**
     * Sets the value of the gauge. This method is primarly used for
     * static gauges or if you really have measurement results that
     * are occuring within the range of a second. If you have slow
     * changing values you should better use the method setValueAnimated.
     * @param VALUE
     */
    public void setValue(final double VALUE)
    {
        if (isEnabled())
        {
            model.setValue(VALUE);

            // LED blinking makes only sense when autoResetToZero == OFF
            if (!isAutoResetToZero())
            {
                // Check if current value exceeds threshold and activate led as indicator
                if (model.getValue() >= model.getThreshold())
                {
                    if (!LED_BLINKING_TIMER.isRunning())
                    {
                        LED_BLINKING_TIMER.start();
                        propertySupport.firePropertyChange(THRESHOLD_PROPERTY, false, true);
                    }
                }
                else
                {
                    LED_BLINKING_TIMER.stop();
                    setCurrentLedImage(getLedImageOff());
                }
            }

            repaint(getInnerBounds());

            fireStateChanged();
            propertySupport.firePropertyChange(VALUE_PROPERTY, model.getOldValue(), model.getValue());
        }
    }

    /**
     * Returns the minimum value of the measurement
     * range of this gauge.
     * @return a dobule representing the min value the gauge could visualize
     */
    public double getMinValue()
    {
        return model.getNiceMinValue();
    }

    /**
     * Sets the minimum value of the measurement
     * range of this gauge. This value defines the
     * minimum value the gauge could display.
     * @param MIN_VALUE
     */
    public void setMinValue(final double MIN_VALUE)
    {
        model.setMinValue(MIN_VALUE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the maximum value of the measurement
     * range of this gauge. This means the gauge could
     * not display values larger than this value.
     * @return Double that represents the maximum value the gauge could display
     */
    public double getMaxValue()
    {
        return model.getNiceMaxValue();
    }

    /**
     * Sets the maximum value of the measurement
     * range of this gauge. This value defines the
     * maximum value the gauge could display.
     * It has nothing to do with MaxMeasuredValue,
     * which represents the max. value that was
     * measured since the last reset of MaxMeasuredValue
     * @param MAX_VALUE
     */
    public void setMaxValue(final double MAX_VALUE)
    {
        model.setMaxValue(MAX_VALUE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the peak value which is the last
     * value that was set before the current one
     * This feature is used in the bargraphs
     * @return a double that represents the peak value
     */
    public double getPeakValue()
    {
        return model.getPeakValue();
    }

    /**
     * Sets the peak value of the gauge. This method will
     * be used in the bargraph gauges to visualize the last
     * displayed value
     * @param PEAK_VALUE
     */
    public void setPeakValue(final double PEAK_VALUE)
    {
        model.setPeakValue(PEAK_VALUE);
    }

    /**
     * Returns true if the last measured value (peak value)
     * is visible and will be painted.
     * @return the visibility of the peak value
     */
    public boolean isPeakValueVisible()
    {
        return model.isPeakValueVisible();
    }

    /**
     * Sets the visbility of the peak value which
     * is the last measured value.
     * @param PEAK_VALUE_VISIBLE
     */
    public void setPeakValueVisible(final boolean PEAK_VALUE_VISIBLE)
    {
        model.setPeakValueVisible(PEAK_VALUE_VISIBLE);
    }

    /**
     * Returns true if the gauge will be reseted to
     * zero after each value.
     * Means if you set a value the pointer will
     * move to this value and after it reached the
     * given value it will return back to zero.
     * @return true if the value will be set to
     * zero automaticaly
     */
    public boolean isAutoResetToZero()
    {
        return model.isAutoResetToZero();
    }

    /**
     * Enables/disables the mode where the gauge
     * will return to zero after a value was set.
     * Means if you set a value the pointer will
     * move to this value and after it reached the
     * given value it will return back to zero.
     * @param AUTO_RESET_TO_ZERO
     */
    public void setAutoResetToZero(final boolean AUTO_RESET_TO_ZERO)
    {
        model.setAutoResetToZero(AUTO_RESET_TO_ZERO);

        if (model.isAutoResetToZero())
        {
            setThresholdVisible(false);
            setLedVisible(false);
        }
    }

    /**
     * Returns the value that is defined as a threshold.
     * If the current value of the gauge exceeds this
     * threshold, a event will be fired and the led will
     * start blinking (if the led is visible).
     * @return the threshold value where the led starts blinking
     */
    public double getThreshold()
    {
        return model.getThreshold();
    }

    /**
     * Sets the given value as the threshold.
     * If the current value of the gauge exceeds this
     * threshold, a event will be fired and the led will
     * start blinking (if the led is visible).
     * @param THRESHOLD
     */
    public void setThreshold(final double THRESHOLD)
    {
        model.setThreshold(THRESHOLD);
    }

    /**
     * Returns the visibility of the threshold indicator.
     * The value of the threshold will be visualized by
     * a small red triangle that points on the threshold
     * value.
     * @return true if the threshold indicator is visible
     */
    public boolean isThresholdVisible()
    {
        return model.isThresholdVisible();
    }

    /**
     * Sets the visibility of the threshold indicator.
     * The value of the threshold will be visualized by
     * a small red triangle that points on the threshold
     * value.
     * @param THRESHOLD_VISIBLE
     */
    public void setThresholdVisible(final boolean THRESHOLD_VISIBLE)
    {
        model.setThresholdVisible(THRESHOLD_VISIBLE);
        repaint(getInnerBounds());
    }

    /**
     * Returns the visiblity of the threshold led.
     * @return a boolean that indicates if the led is visible
     */
    public boolean isLedVisible()
    {
        return model.isLedVisible();
    }

    /**
     * Sets the visibility of the threshold led.
     * @param LED_VISIBLE
     */
    public void setLedVisible(final boolean LED_VISIBLE)
    {
        model.setLedVisible(LED_VISIBLE);
        repaint(getInnerBounds());
    }

    abstract java.awt.geom.Point2D getLedPosition();

    abstract void setLedPosition(final double X, final double Y);

    /**
     * Returns the color of the threshold led.
     * The LedColor is not a standard color but defines a
     * color scheme for the led. The default ledcolor is RED
     * @return the selected the color for the led
     */
    public eu.hansolo.steelseries.tools.LedColor getLedColor()
    {
        return model.getLedColor();
    }

    /**
     * Sets the color of the threshold led dependend on the orientation of
     * a component. This is only important for the linear gauges where the width
     * and the height are different.
     * The LedColor is not a standard color but defines a
     * color scheme for the led. The default ledcolor is RED
     * @param LED_COLOR
     */
    public void setLedColor(final eu.hansolo.steelseries.tools.LedColor LED_COLOR)
    {
        model.setLedColor(LED_COLOR);
        final boolean LED_WAS_ON = currentLedImage.equals(ledImageOn) ? true : false;

        switch (getOrientation())
        {
            case HORIZONTAL:
                recreateLedImages(getHeight());
                break;
            case VERTICAL:
                recreateLedImages(getWidth());
                break;
            default:
                recreateLedImages();
                break;
        }
        if (currentLedImage != null)
        {
            currentLedImage.flush();
        }
        currentLedImage = LED_WAS_ON == true ? ledImageOn : ledImageOff;

        repaint(getInnerBounds());
    }

    /**
     * Returns the color from which the custom ledcolor will be calculated
     * @return the color from which the custom ledcolor will be calculated
     */
    public java.awt.Color getCustomLedColor()
    {
        return model.getCustomLedColor().COLOR;
    }

    /**
     * Sets the color from which the custom ledcolor will be calculated
     * @param COLOR 
     */
    public void setCustomLedColor(final java.awt.Color COLOR)
    {
        model.setCustomLedColor(new eu.hansolo.steelseries.tools.CustomLedColor(COLOR));
        final boolean LED_WAS_ON = currentLedImage.equals(ledImageOn) ? true : false;

        switch (getOrientation())
        {
            case HORIZONTAL:
                recreateLedImages(getHeight());
                break;
            case VERTICAL:
                recreateLedImages(getWidth());
                break;
            default:
                recreateLedImages();
                break;
        }

        if (currentLedImage != null)
        {
            currentLedImage.flush();
        }
        currentLedImage = LED_WAS_ON == true ? ledImageOn : ledImageOff;

        repaint(getInnerBounds());
    }

    /**
     * Returns the state of the threshold led.
     * The led could blink which will be triggered by a javax.swing.Timer
     * that triggers every 500 ms. The blinking will be done by switching
     * between two images.
     * @return true if the led is blinking
     */
    public boolean isLedBlinking()
    {
        return this.ledBlinking;
    }

    /**
     * Sets the state of the threshold led.
     * The led could blink which will be triggered by a javax.swing.Timer
     * that triggers every 500 ms. The blinking will be done by switching
     * between two images.
     * @param LED_BLINKING
     */
    public void setLedBlinking(final boolean LED_BLINKING)
    {
        this.ledBlinking = LED_BLINKING;
        if (LED_BLINKING)
        {
            LED_BLINKING_TIMER.start();
        }
        else
        {
            setCurrentLedImage(getLedImageOff());
            LED_BLINKING_TIMER.stop();
        }
    }

    /**
     * Returns the image of the switched on threshold led
     * with the currently active ledcolor.
     * @return the image of the led with the state active
     * and the selected led color
     */
    protected java.awt.image.BufferedImage getLedImageOn()
    {
        return this.ledImageOn;
    }

    /**
     * Returns the image of the switched off threshold led
     * with the currently active ledcolor.
     * @return the image of the led with the state inactive
     * and the selected led color
     */
    protected java.awt.image.BufferedImage getLedImageOff()
    {
        return this.ledImageOff;
    }

    /**
     * Recreates the current threshold led images due to the size of the component
     */
    protected void recreateLedImages()
    {
        recreateLedImages(getInnerBounds().width);
    }

    /**
     * Recreates the current threshold led images due to the given width
     * @param SIZE 
     */
    protected void recreateLedImages(final int SIZE)
    {
        if (ledImageOff != null)
        {
            ledImageOff.flush();
        }
        ledImageOff = create_LED_Image(SIZE, 0, model.getLedColor());

        if (ledImageOn != null)
        {
            ledImageOn.flush();
        }
        ledImageOn = create_LED_Image(SIZE, 1, model.getLedColor());
    }

    /**
     * Returns the image of the currently used led image.
     * @return the led image at the moment (depends on blinking)
     */
    protected java.awt.image.BufferedImage getCurrentLedImage()
    {
        return this.currentLedImage;
    }

    /**
     * Sets the image of the currently used led image.
     * @param CURRENT_LED_IMAGE
     */
    protected void setCurrentLedImage(final java.awt.image.BufferedImage CURRENT_LED_IMAGE)
    {
        if (currentLedImage != null)
        {
            currentLedImage.flush();
        }
        currentLedImage = CURRENT_LED_IMAGE;
        repaint(getInnerBounds());
    }

    /**
     * Returns the current state of the threshold led
     * @return a boolean that represents the state of the threshold led
     */
    protected boolean isLedOn()
    {
        return this.ledOn;
    }

    /**
     * Returns the lowest measured value.
     * On every move of the bar/pointer the lowest value
     * will be stored in the minMeasuredValue variable.
     * @return a double representing the min measure value
     */
    public double getMinMeasuredValue()
    {
        return model.getMinMeasuredValue();
    }

    /**
     * Sets the lowest value that was measured
     * On every move of the bar/pointer the lowest value
     * will be stored in the minMeasuredValue variable.
     * @param MIN_MEASURED_VALUE
     */
    protected void setMinMeasuredValue(final double MIN_MEASURED_VALUE)
    {
        model.setMinMeasuredValue(MIN_MEASURED_VALUE);
        repaint(getInnerBounds());
    }

    /**
     * Returns the visibility of the minMeasuredValue indicator.
     * The lowest value that was measured by the gauge will
     * be visualized by a little blue triangle.
     * @return a boolean that indicates if the min measured value image is visible
     */
    public boolean isMinMeasuredValueVisible()
    {
        return model.isMinMeasuredValueVisible();
    }

    /**
     * Sets the visibility of the minMeasuredValue indicator.
     * The lowest value that was measured by the gauge will
     * be visualized by a little blue triangle.
     * @param MIN_MEASURED_VALUE_VISIBLE
     */
    public void setMinMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE)
    {
        model.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
        repaint(getInnerBounds());
    }

    /**
     * Resets the minMeasureValue variable to the maximum value
     * that the gauge could display. So on the next move of the
     * pointer/bar the indicator will be set to the pointer/bar
     * position again.
     */
    public void resetMinMeasuredValue()
    {
        model.resetMinMeasuredValue();
        repaint(getInnerBounds());
    }

    /**
     * Resets the minMeasuredValue variable to the given value.
     * So on the next move of the pointer/bar the indicator will
     * be set to the pointer/bar position again.
     * @param VALUE
     */
    public void resetMinMeasuredValue(final double VALUE)
    {
        model.resetMinMeasuredValue(VALUE);
        repaint(getInnerBounds());
    }

    /**
     * Returns the biggest measured value.
     * On every move of the bar/pointer the biggest value
     * will be stored in the maxMeasuredValue variable.
     * @return a double representing the max measured value
     */
    public double getMaxMeasuredValue()
    {
        return model.getMaxMeasuredValue();
    }

    /**
     * Sets the highest value that was measured
     * On every move of the bar/pointer the highest value
     * will be stored in the maxMeasuredValue variable.
     * @param MAX_MEASURED_VALUE
     */
    protected void setMaxMeasuredValue(final double MAX_MEASURED_VALUE)
    {
        model.setMaxMeasuredValue(MAX_MEASURED_VALUE);
        repaint(getInnerBounds());
    }

    /**
     * Returns the visibility of the maxMeasuredValue indicator.
     * The biggest value that was measured by the gauge will
     * be visualized by a little red triangle.
     * @return a boolean that indicates if the max measured value image is visible
     */
    public boolean isMaxMeasuredValueVisible()
    {
        return model.isMaxMeasuredValueVisible();
    }

    /**
     * Sets the visibility of the maxMeasuredValue indicator.
     * The biggest value that was measured by the gauge will
     * be visualized by a little red triangle.
     * @param MAX_MEASURED_VALUE_VISIBLE
     */
    public void setMaxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE)
    {
        model.setMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
        repaint(getInnerBounds());
    }

    /**
     * Resets the maxMeasureValue variable to the minimum value
     * that the gauge could display. So on the next move of the
     * pointer/bar the indicator will be set to the pointer/bar
     * position again.
     */
    public void resetMaxMeasuredValue()
    {
        model.resetMaxMeasuredValue();
        repaint(getInnerBounds());
    }

    /**
     * Resets the maxMeasuredValue variable to the given value.
     * So on the next move of the pointer/bar the indicator will
     * be set to the pointer/bar position again.
     * @param VALUE
     */
    public void resetMaxMeasuredValue(final double VALUE)
    {
        model.resetMaxMeasuredValue(VALUE);
        repaint(getInnerBounds());
    }

    /**
     * Returns the time in milliseconds that the pointer/bar/led needs to move from
     * the minimum value of the gauge to the maximum of the gauge in standard mode.
     * The minimum time is 250 ms and the maximum time is 5000 ms.
     * @return time in ms that the pointer/bar/led needs to move from minValue to maxValue in standard mode
     */
    public long getStdTimeToValue()
    {
        return this.stdTimeToValue;
    }

    /**
     * Sets the time in milliseconds that the pointer/bar/led needs to move from
     * the minimum value of the gauge to the maximum of the gauge in standard mode.
     * The minimum time is 250 ms and the maximum time is 5000 ms.
     * @param STD_TIME_TO_VALUE
     */
    public void setStdTimeToValue(final long STD_TIME_TO_VALUE)
    {
        stdTimeToValue = STD_TIME_TO_VALUE < 250 ? 250 : (STD_TIME_TO_VALUE > 5000 ? 5000 : STD_TIME_TO_VALUE);
    }

    /**
     * Returns the time in milliseconds that the pointer/bar/led needs to move from
     * the minimum value of the gauge to the maximum of the gauge in autoreturn to zero mode.
     * The minimum time is 250 ms and the maximum time is 5000 ms.
     * @return time in ms that the pointer/bar/led needs to move from minValue to maxValue in autoreturn to zero mode
     */
    public long getRtzTimeToValue()
    {
        return this.rtzTimeToValue;
    }

    /**
     * Sets the time in milliseconds that the pointer/bar/led needs to move from
     * the minimum value of the gauge to the maximum of the gauge in autoreturn to zero mode.
     * The minimum time is 250 ms and the maximum time is 5000 ms.
     * @param RTZ_TIME_TO_VALUE
     */
    public void setRtzTimeToValue(final long RTZ_TIME_TO_VALUE)
    {
        rtzTimeToValue = RTZ_TIME_TO_VALUE < 250 ? 250 : (RTZ_TIME_TO_VALUE > 5000 ? 5000 : RTZ_TIME_TO_VALUE);
    }

    /**
     * Returns the time in milliseconds that the pointer/bar/led needs back from the value to zero
     * in autoreturn to zero mode. The minimum time is 250 ms and the maximum time is 5000 ms.
     * @return the time in ms that the pointer/bar/led needs to move back from the value to zero
     */
    public long getRtzTimeBackToZero()
    {
        return this.rtzTimeBackToZero;
    }

    /**
     * Sets the time in milliseconds that the pointer/bar/led needs to move back from the value
     * to zero in autoreturn to zero mode. The minimum time is 250 ms and the maximum time is 5000 ms.
     * @param RTZ_TIME_BACK_TO_ZERO
     */
    public void setRtzTimeBackToZero(final long RTZ_TIME_BACK_TO_ZERO)
    {
        rtzTimeBackToZero = RTZ_TIME_BACK_TO_ZERO < 250 ? 250 : (RTZ_TIME_BACK_TO_ZERO > 5000 ? 5000 : RTZ_TIME_BACK_TO_ZERO);
    }

    /**
     * Returns the timer that is used to timeout the peak value in
     * the bargraph gauges.
     * @return a javax.swing.Timer object
     */
    public javax.swing.Timer getPeakTimer()
    {
        return this.PEAK_TIMER;
    }

    /**
     * Start the peak timer
     */
    public void startPeakTimer()
    {
        if (!PEAK_TIMER.isRunning())
        {
            PEAK_TIMER.start();
        }
    }

    /**
     * Stop the peak timer
     */
    public void stopPeakTimer()
    {
        if (PEAK_TIMER.isRunning())
        {
            PEAK_TIMER.stop();
        }
    }

    /**
     * Returns an int that represents the orientation of the gauge.
     * The values are taken from eu.hansolo.steelseries.tools.Orientation     
     * NORTH         => Used in Radial1Vertical
     * NORTH_EAST    => Used in Radial1Square
     * EAST          => Used in Radial1Vertical
     * SOUTH_EAST    => Used in Radial1Square
     * SOUTH         => Used in Radial1Vertical
     * SOUTH_WEST    => Used in Radial1Square
     * WEST          => Used in Radial1Vertical
     * NORTH_WEST    => Used in Radial1Square
     * HORIZONTAL    => Used in Linear
     * VERTICAL      => Used in Linear
     * @return a enum that represents the orientation
     */
    public eu.hansolo.steelseries.tools.Orientation getOrientation()
    {
        return this.orientation;
    }

    /**
     * Sets the orientation of the gauge.
     * The values are taken from eu.hansolo.steelseries.tools.Orientation     
     * NORTH         => Used in Radial1Vertical
     * NORTH_EAST    => Used in Radial1Square
     * EAST          => Used in Radial1Vertical
     * SOUTH_EAST    => Used in Radial1Square
     * SOUTH         => Used in Radial1Vertical
     * SOUTH_WEST    => Used in Radial1Square
     * WEST          => Used in Radial1Vertical
     * NORTH_WEST    => Used in Radial1Square
     * HORIZONTAL    => Used in Linear
     * VERTICAL      => Used in Linear
     * @param ORIENTATION 
     */
    public void setOrientation(final eu.hansolo.steelseries.tools.Orientation ORIENTATION)
    {
        this.orientation = ORIENTATION;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color of the threshold indicator
     * @return the color of the threshold indicator
     */
    public eu.hansolo.steelseries.tools.ColorDef getThresholdColor()
    {
        return model.getThresholdColor();
    }

    /**
     * Sets the color of the threshold indicator
     * @param THRESHOLD_COLOR 
     */
    public void setThresholdColor(final eu.hansolo.steelseries.tools.ColorDef THRESHOLD_COLOR)
    {
        model.setThresholdColor(THRESHOLD_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the custom color definition of the threshold indicator
     * @return the custom color definition of the threshold indicator
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomThresholdColor()
    {
        return model.getCustomThresholdColor();
    }

    /**
     * Sets the custom color definition of the threshold indicator
     * @param CUSTOM_THRESHOLD_COLOR 
     */
    public void setCustomThresholdColor(final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_THRESHOLD_COLOR)
    {
        model.setCustomThresholdColor(CUSTOM_THRESHOLD_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the type of the threshold indicator
     * @return the type of the threshold indicator
     */
    public eu.hansolo.steelseries.tools.ThresholdType getThresholdType()
    {
        return model.getThresholdType();
    }

    /**
     * Sets the type of the threshold indicator
     * @param THRESHOLD_TYPE 
     */
    public void setThresholdType(final eu.hansolo.steelseries.tools.ThresholdType THRESHOLD_TYPE)
    {
        model.setThresholdType(THRESHOLD_TYPE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
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
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tickmarks related">    
    /**
     * Returns true if the color of the tickmarks will be
     * used from the defined background color.
     * @return true if tickmarks will use the color defined in the current
     * background color
     */
    public boolean isTickmarkColorFromThemeEnabled()
    {
        return model.isTickmarkColorFromThemeEnabled();
    }

    /**
     * Enables/disables the usage of a separate color for the
     * tickmarks.
     * @param TICKMARK_COLOR_FROM_THEME_ENABLED
     */
    public void setTickmarkColorFromThemeEnabled(final boolean TICKMARK_COLOR_FROM_THEME_ENABLED)
    {
        model.setTickmarkColorFromThemeEnabled(TICKMARK_COLOR_FROM_THEME_ENABLED);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color of the tickmarks and their labels
     * @return the custom defined color for the tickmarks and labels
     */
    public java.awt.Color getTickmarkColor()
    {
        return model.getTickmarkColor();
    }

    /**
     * Sets the color of the tickmarks and their labels
     * @param TICKMARK_COLOR
     */
    public void setTickmarkColor(final java.awt.Color TICKMARK_COLOR)
    {
        model.setTickmarkColor(TICKMARK_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the tickmarks are visible
     * @return true if the tickmarks are visible
     */
    public boolean isTickmarksVisible()
    {
        return model.isTickmarksVisible();
    }

    /**
     * Enables or disables the visibility of the tickmarks
     * @param TICKMARKS_VISIBLE
     */
    public void setTickmarksVisible(final boolean TICKMARKS_VISIBLE)
    {
        model.setTickmarksVisible(TICKMARKS_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the tickmark labels are visible
     * @return true if the tickmark labels are visible
     */
    public boolean isTicklabelsVisible()
    {
        return model.isTicklabelsVisible();
    }

    /**
     * Enables or disables the visibility of the tickmark labels
     * @param TICKLABELS_VISIBLE
     */
    public void setTicklabelsVisible(final boolean TICKLABELS_VISIBLE)
    {
        model.setTicklabelsVisible(TICKLABELS_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the number format that is used to display the labels of the tickmarks
     * @return the number format that is used to display the labels of the tickmarks
     */
    public eu.hansolo.steelseries.tools.NumberFormat getLabelNumberFormat()
    {
        return model.getLabelNumberFormat();
    }

    /**
     * Sets the number format that will be used to display the labels of the tickmarks
     * @param NUMBER_FORMAT Possible values are AUTO, STANDARD, FRACTIONAL and SCIENTIFIC
     */
    public void setLabelNumberFormat(final eu.hansolo.steelseries.tools.NumberFormat NUMBER_FORMAT)
    {
        model.setLabelNumberFormat(NUMBER_FORMAT);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if customer defined tickmark labels will be
     * used for the scaling.
     * e.g. you only want to show "0, 10, 50, 100" in your
     * gauge scale so you could set the custom tickmarklabels
     * to these values.
     * @return a boolean that indicates if custom tickmark labels will be used
     */
    public boolean isCustomTickmarkLabelsEnabled()
    {
        return this.customTickmarkLabelsEnabled;
    }

    /**
     * Enables/Disables the usage of custom tickmark labels.
     * e.g. you only want to show "0, 10, 50, 100" in your
     * gauge scale so you could set the custom tickmarklabels
     * to these values.
     * @param CUSTOM_TICKMARK_LABELS_ENABLED
     */
    public void setCustomTickmarkLabelsEnabled(final boolean CUSTOM_TICKMARK_LABELS_ENABLED)
    {
        this.customTickmarkLabelsEnabled = CUSTOM_TICKMARK_LABELS_ENABLED;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns a list of the defined custom tickmark labels
     * e.g. you only want to show "0, 10, 50, 100" in your
     * gauge scale so you could set the custom tickmarklabels
     * to these values.
     * @return the arraylist containing custom tickmark labels
     */
    public java.util.List<Double> getCustomTickmarkLabels()
    {
        java.util.List<Double> customTickmarkLabelsCopy = new java.util.ArrayList<Double>(10);
        customTickmarkLabelsCopy.addAll(customTickmarkLabels);
        //return (java.util.ArrayList<Double>) this.CUSTOM_TICKMARK_LABELS.clone();
        return customTickmarkLabelsCopy;
    }

    /**
     * Takes a array of doubles that will be used as custom tickmark labels
     * e.g. you only want to show "0, 10, 50, 100" in your
     * gauge scale so you could set the custom tickmarklabels
     * to these values.
     * @param CUSTOM_TICKMARK_LABELS_ARRAY
     */
    public void setCustomTickmarkLabels(final double... CUSTOM_TICKMARK_LABELS_ARRAY)
    {
        customTickmarkLabels.clear();
        for (Double label : CUSTOM_TICKMARK_LABELS_ARRAY)
        {
            customTickmarkLabels.add(label);
        }
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Adds the given double to the list of custom tickmark labels
     * e.g. you only want to show "0, 10, 50, 100" in your
     * gauge scale so you could set the custom tickmarklabels
     * to these values.
     * @param CUSTOM_TICKMARK_LABEL
     */
    public void addCustomTickmarkLabel(final double CUSTOM_TICKMARK_LABEL)
    {
        customTickmarkLabels.add(CUSTOM_TICKMARK_LABEL);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Reset the list of custom tickmark labels, which means clear the list
     */
    public void resetCustomTickmarkLabels()
    {
        customTickmarkLabels.clear();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns a copy of the ArrayList that stores the sections.
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you would like to visualize by
     * colored tickmarks.
     * @return a arraylist representing the sections for the tickmarks
     */
    public java.util.List<eu.hansolo.steelseries.tools.Section> getTickmarkSections()
    {
        return model.getTickmarkSections();
    }

    /**
     * Sets the sections given in a array of sections (Section[])
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you would like to visualize by
     * by colored tickmarks.
     * @param TICKMARK_SECTIONS_ARRAY
     */
    public void setTickmarkSections(final eu.hansolo.steelseries.tools.Section... TICKMARK_SECTIONS_ARRAY)
    {
        model.setTickmarkSections(TICKMARK_SECTIONS_ARRAY);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Adds a given section to the list of sections
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you would like to visualize by
     * by colored tickmarks.
     * @param TICKMARK_SECTION
     */
    public void addTickmarkSection(final eu.hansolo.steelseries.tools.Section TICKMARK_SECTION)
    {
        model.addTickmarkSection(TICKMARK_SECTION);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Clear the TICKMARK_SECTIONS arraylist
     */
    public void resetTickmarkSections()
    {
        model.resetTickmarkSections();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the visibility of the tickmark sections.
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * the tickmark labels colored for specific areas.
     * @return true if the tickmark sections are visible
     */
    public boolean isTickmarkSectionsVisible()
    {
        return model.isTickmarkSectionsVisible();
    }

    /**
     * Sets the visibility of the tickmark sections.
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * the tickmark labels colored for specific areas.
     * @param TICKMARK_SECTIONS_VISIBLE
     */
    public void setTickmarkSectionsVisible(final boolean TICKMARK_SECTIONS_VISIBLE)
    {
        model.setTickmarkSectionsVisible(TICKMARK_SECTIONS_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the current type of tickmark that is used for minor tickmarks. Could be LINE (default), CIRCLE, TRIANGLE or SQUARE
     * @return the current type of tickmark that is used for minor tickmarks. Could be LINE (default), CIRCLE, TRIANGLE or SQUARE
     */
    public eu.hansolo.steelseries.tools.TickmarkType getMinorTickmarkType()
    {
        return model.getMinorTickmarkType();
    }

    /**
     * Sets the current type of tickmark that is used for minor tickmarks. Value could be LINE (default), CIRCLE, TRIANGLE or SQUARE
     * @param TICKMARK_TYPE 
     */
    public void setMinorTickmarkType(final eu.hansolo.steelseries.tools.TickmarkType TICKMARK_TYPE)
    {
        model.setMinorTickmarkType(TICKMARK_TYPE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the current type of tickmark that is used for major tickmarks. Could be LINE (default), CIRCLE, TRIANGLE or SQUARE
     * @return the current type of tickmark that is used for major tickmarks. Could be LINE (default), CIRCLE, TRIANGLE or SQUARE
     */
    public eu.hansolo.steelseries.tools.TickmarkType getMajorTickmarkType()
    {
        return model.getMajorTickmarkType();
    }

    /**
     * Sets the current type of tickmark that is used for major tickmarks. Value could be LINE (default), CIRCLE, TRIANGLE or SQUARE
     * @param TICKMARK_TYPE 
     */
    public void setMajorTickmarkType(final eu.hansolo.steelseries.tools.TickmarkType TICKMARK_TYPE)
    {
        model.setMajorTickmarkType(TICKMARK_TYPE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the minor tickmarks are visible (every 5th tickmark)
     * @return true if the minor tickmarks are visible (every 5th tickmark)
     */
    public boolean isMinorTickmarkVisible()
    {
        return model.isMinorTickmarksVisible();
    }

    /**
     * Enables / Disables the visibility of the minor tickmarks (every 5th tickmark)
     * @param MINOR_TICKMARK_VISIBLE 
     */
    public void setMinorTickmarkVisible(final boolean MINOR_TICKMARK_VISIBLE)
    {
        model.setMinorTickmarksVisible(MINOR_TICKMARK_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the major tickmarks are visible (every 10th tickmark)
     * @return true if the major tickmarks are visible (every 10th tickmark)
     */
    public boolean isMajorTickmarkVisible()
    {
        return model.isMajorTickmarksVisible();
    }

    /**
     * Enables / Disables the visibility of the major tickmarks (every 10th tickmark)
     * @param MAJOR_TICKMARK_VISIBLE 
     */
    public void setMajorTickmarkVisible(final boolean MAJOR_TICKMARK_VISIBLE)
    {
        model.setMajorTickmarksVisible(MAJOR_TICKMARK_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the calculation of nice values for the min and max values of the scale is enabled
     * @return true if the calculation of nice values for the min and max values of the scale is enabled
     */
    public boolean isNiceScale()
    {
        return model.isNiceScale();
    }

    /**
     * Enables / disables the calculation of nice values for the min and max values of the scale
     * @param NICE_SCALE 
     */
    public void setNiceScale(final boolean NICE_SCALE)
    {
        model.setNiceScale(NICE_SCALE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the spacing between the minor tickmarks
     * @return the spacing between the minor tickmarks
     */
    public double getMinorTickSpacing()
    {
        return model.getMinorTickSpacing();
    }

    /**
     * Sets the spacing between the minor tickmarks if the niceScale property is disabled
     * @param MINOR_TICKSPACING 
     */
    public void setMinorTickSpacing(final double MINOR_TICKSPACING)
    {
        model.setMinorTickSpacing(MINOR_TICKSPACING);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the spacing between the major tickmarks
     * @return the spacing between the major tickmarks
     */
    public double getMajorTickSpacing()
    {
        return model.getMajorTickSpacing();
    }

    /**
     * Sets the spacing between the major tickmarks if the niceScale property is disabled
     * @param MAJOR_TICKSPACING 
     */
    public void setMajorTickSpacing(final double MAJOR_TICKSPACING)
    {
        model.setMajorTickSpacing(MAJOR_TICKSPACING);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());

    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Track related">
    /**
     * Returns the visibility of the track.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return true if the track is visible
     */
    public boolean isTrackVisible()
    {
        return model.isTrackVisible();
    }

    /**
     * Sets the visibility of the track.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_VISIBLE
     */
    public void setTrackVisible(final boolean TRACK_VISIBLE)
    {
        model.setTrackVisible(TRACK_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the value where the track will start.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return represents the value where the track starts
     */
    public double getTrackStart()
    {
        return model.getTrackStart();
    }

    /**
     * Sets the value where the track will start.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_START
     */
    public void setTrackStart(final double TRACK_START)
    {
        model.setTrackStart(TRACK_START);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the value of the point between trackStart and trackStop.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return represents the value where the intermediate position
     * of the track is defined.
     */
    public double getTrackSection()
    {
        return model.getTrackSection();
    }

    /**
     * Sets the value of the point between trackStart and trackStop.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_SECTION
     */
    public void setTrackSection(final double TRACK_SECTION)
    {
        model.setTrackSection(TRACK_SECTION);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the value of the point where the track will stop
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return represents the position where the track stops
     */
    public double getTrackStop()
    {
        return model.getTrackStop();
    }

    /**
     * Sets the value of the end of the track.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_STOP
     */
    public void setTrackStop(final double TRACK_STOP)
    {
        model.setTrackStop(TRACK_STOP);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color of the point where the track will start.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return represents the color at the point where the track starts
     */
    public java.awt.Color getTrackStartColor()
    {
        return model.getTrackStartColor();
    }

    /**
     * Sets the color of the point where the track will start.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_START_COLOR
     */
    public void setTrackStartColor(final java.awt.Color TRACK_START_COLOR)
    {
        model.setTrackStartColor(TRACK_START_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color of the value between trackStart and trackStop
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return represents the color of the intermediate position on the track
     */
    public java.awt.Color getTrackSectionColor()
    {
        return model.getTrackSectionColor();
    }

    /**
     * Sets the color of the value between trackStart and trackStop
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_SECTION_COLOR
     */
    public void setTrackSectionColor(final java.awt.Color TRACK_SECTION_COLOR)
    {
        model.setTrackSectionColor(TRACK_SECTION_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color of the point where the track will stop.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @return represents the color of the point where the track stops
     */
    public java.awt.Color getTrackStopColor()
    {
        return model.getTrackStopColor();
    }

    /**
     * Sets the color of the point where the track will stop.
     * The track is a area that could be defined by a start value,
     * a section stop value. This area will be painted with a
     * gradient that uses two or three given colors.
     * E.g. a critical area of a thermometer could be defined between
     * 30 and 100 degrees celsius and could have a gradient from
     * green over yellow to red. In this case the start
     * value would be 30, the stop value would be 100 and the section could
     * be somewhere between 30 and 100 degrees.
     * @param TRACK_STOP_COLOR
     */
    public void setTrackStopColor(final java.awt.Color TRACK_STOP_COLOR)
    {
        model.setTrackStopColor(TRACK_STOP_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Sections related">
    /**
     * Returns the visibility of the sections.
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @return true if the sections are visible
     */
    public boolean isSectionsVisible()
    {
        return model.isSectionsVisible();
    }

    /**
     * Sets the visibility of the sections.
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @param SECTIONS_VISIBLE
     */
    public void setSectionsVisible(final boolean SECTIONS_VISIBLE)
    {
        model.setSectionsVisible(SECTIONS_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns a copy of the ArrayList that stores the sections.
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @return a list of sections
     */
    protected java.util.List<eu.hansolo.steelseries.tools.Section> getSections()
    {
        return model.getSections();
    }

    /**
     * Sets the sections given in a array of sections (Section[])
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @param SECTIONS_ARRAY
     */
    public void setSections(final eu.hansolo.steelseries.tools.Section... SECTIONS_ARRAY)
    {
        model.setSections(SECTIONS_ARRAY);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Adds a given section to the list of sections
     * The sections could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The sections are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @param SECTION
     */
    public void addSection(final eu.hansolo.steelseries.tools.Section SECTION)
    {
        model.addSection(SECTION);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Clear the SECTIONS arraylist
     */
    public void resetSections()
    {
        model.resetSections();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Area related">
    /**
     * Returns the visibility of the areas.
     * The areas could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The areas are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @return true if the areas are visible
     */
    public boolean isAreasVisible()
    {
        return model.isAreasVisible();
    }

    /**
     * Sets the visibility of the areas.
     * The areas could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The areas are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @param AREAS_VISIBLE
     */
    public void setAreasVisible(final boolean AREAS_VISIBLE)
    {
        model.setAreasVisible(AREAS_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns a copy of the ArrayList that stores the areas.
     * The areas could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The areas are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @return a clone of the list of areas
     */
    protected java.util.List<eu.hansolo.steelseries.tools.Section> getAreas()
    {
        return model.getAreas();
    }

    /**
     * Sets the areas given in a array of areas (Section[])
     * A local copy of the Section object will created and will
     * be stored in the component.
     * The areas could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The areas are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @param AREAS_ARRAY
     */
    public void setAreas(final eu.hansolo.steelseries.tools.Section... AREAS_ARRAY)
    {
        model.setAreas(AREAS_ARRAY);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Adds a given area to the list of areas
     * The areas could be defined by a start value, a stop value
     * and a color. One has to create a Section object from the
     * class eu.hansolo.steelseries.tools.Section.
     * The areas are stored in a ArrayList so there could be
     * multiple. This might be a useful feature if you need to have
     * exactly defined areas that you could not visualize with the
     * track feature.
     * @param AREA
     */
    public void addArea(final eu.hansolo.steelseries.tools.Section AREA)
    {
        model.addArea(AREA);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Clear the AREAS arraylist
     */
    public void resetAreas()
    {
        model.resetAreas();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Title/Unit related">
    /**
     * Returns the title of the gauge.
     * A title could be for example "Temperature".
     * @return the title of the gauge
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * Sets the title of the gauge.
     * A title could be for example "Temperature".
     * @param TITLE
     */
    public void setTitle(final String TITLE)
    {
        this.title = TITLE;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the unit string of the gauge.
     * A unit string could be for example "[cm]".
     * @return the unit string of the gauge
     */
    public String getUnitString()
    {
        return this.unitString;
    }

    /**
     * Sets the unit string of the gauge.
     * A unit string could be for example "[cm]"
     * @param UNIT_STRING
     */
    public void setUnitString(final String UNIT_STRING)
    {
        this.unitString = UNIT_STRING;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the color of the tickmarks will be
     * used from the defined background color.
     * @return true if the color for the tickmarks and labels
     * will be used from the selected backgroundcolor
     */
    public boolean isLabelColorFromThemeEnabled()
    {
        return model.isLabelColorFromThemeEnabled();
    }

    /**
     * Enables/disables the usage of a separate color for the
     * title and unit string.
     * @param LABEL_COLOR_FROM_THEME_ENABLED
     */
    public void setLabelColorFromThemeEnabled(final boolean LABEL_COLOR_FROM_THEME_ENABLED)
    {
        model.setLabelColorFromThemeEnabled(LABEL_COLOR_FROM_THEME_ENABLED);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color of the Title and the Unit string.
     * @return the color of the title and unit string
     */
    public java.awt.Color getLabelColor()
    {
        return model.getLabelColor();
    }

    /**
     * Sets the color of the Title and the Unit string.
     * @param LABEL_COLOR
     */
    public void setLabelColor(final java.awt.Color LABEL_COLOR)
    {
        model.setLabelColor(LABEL_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if a custom font will be used for the title and unit string
     * @return true if a custom font will be used for the title and unit string
     */
    public boolean isTitleAndUnitFontEnabled()
    {
        return model.isCustomLcdUnitFontEnabled();
    }

    /**
     * Enables and disables the usage of a custom title and unit string font
     * @param TITLE_AND_UNIT_FONT_ENABLED
     */
    public void setTitleAndUnitFontEnabled(final boolean TITLE_AND_UNIT_FONT_ENABLED)
    {
        model.setCustomLcdUnitFontEnabled(TITLE_AND_UNIT_FONT_ENABLED);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Sets the given font for the title and unit string.
     * @return the custom defined font for the title and unit string
     */
    public java.awt.Font getTitleAndUnitFont()
    {
        return this.titleAndUnitFont;
    }

    /**
     * Returns the font that will be used for the title and unit string
     * @param TITLE_UNIT_FONT
     */
    public void setTitleAndUnitFont(final java.awt.Font TITLE_UNIT_FONT)
    {
        this.titleAndUnitFont = TITLE_UNIT_FONT;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Frame related">
    /**
     * Returns the framedesign of the component.
     * The framedesign is some kind of a color scheme for the
     * frame of the component.
     * The typical framedesign is METAL
     * @return the selected framedesign
     */
    public eu.hansolo.steelseries.tools.FrameDesign getFrameDesign()
    {
        return model.getFrameDesign();
    }

    /**
     * Sets the framedesign of the component.
     * The framedesign is some kind of a color scheme for the
     * frame of the component.
     * The typical framedesign is METAL
     * @param FRAME_DESIGN
     */
    public void setFrameDesign(final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN)
    {
        model.setFrameDesign(FRAME_DESIGN);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the java.awt.Paint that will be used to visualize the frame
     * @return the java.awt.Paint that will be used to visualize the frame
     */
    public java.awt.Paint getCustomFrameDesign()
    {
        return model.getCustomFrameDesign();
    }

    /**
     * Seths the custom framedesign of the type java.awt.Paint
     * This will be used if the frameDesign property is set to CUSTOM
     * @param CUSTOM_FRAME_DESIGN 
     */
    public void setCustomFrameDesign(final java.awt.Paint CUSTOM_FRAME_DESIGN)
    {
        model.setCustomFrameDesign(CUSTOM_FRAME_DESIGN);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the frameImage is visible and will be painted
     * @return a boolean that represents the visibility of the frameImage
     */
    public boolean isFrameVisible()
    {
        return model.isFrameVisible();
    }

    /**
     * Enables/Disables the visibility of the frame.
     * If enabled the frame will be painted in the paintComponent() method.
     * Setting the frameDesign to NO_FRAME will only make the frame transparent.
     * @param FRAME_VISIBLE
     */
    public void setFrameVisible(final boolean FRAME_VISIBLE)
    {
        model.setFrameVisible(FRAME_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the frame effect
     * @return the frame effect
     */
    public eu.hansolo.steelseries.tools.FrameEffect getFrameEffect()
    {
        return model.getFrameEffect();
    }

    /**
     * Sets the pseudo 3d effect of the frame
     * @param FRAME_EFFECT 
     */
    public void setFrameEffect(final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT)
    {
        model.setFrameEffect(FRAME_EFFECT);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Background related">
    /**
     * Returns the backgroundcolor of the gauge.
     * The backgroundcolor is not a standard color but more a
     * color scheme with colors and a gradient.
     * The typical backgroundcolor is DARK_GRAY.
     * @return the selected backgroundcolor
     */
    public eu.hansolo.steelseries.tools.BackgroundColor getBackgroundColor()
    {
        return model.getBackgroundColor();
    }

    /**
     * Sets the backgroundcolor of the gauge.
     * The backgroundcolor is not a standard color but more a
     * color scheme with colors and a gradient.
     * The typical backgroundcolor is DARK_GRAY.
     * @param BACKGROUND_COLOR
     */
    public void setBackgroundColor(final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR)
    {
        model.setBackgroundColor(BACKGROUND_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns true if the backgroundImage is visible and will be painted
     * @return a boolean that represents the visibility of the backgroundImage
     */
    public boolean isBackgroundVisible()
    {
        return model.isBackgroundVisible();
    }

    /**
     * Enables/Disables the visibility of the backgroundImage.
     * If enabled the backgroundImage will be painted in the
     * paintComponent() method. The backgroundColor TRANSPARENT
     * only makes the background transparent but the custom
     * background will still be visible.
     * @param BACKGROUND_VISIBLE
     */
    public void setBackgroundVisible(final boolean BACKGROUND_VISIBLE)
    {
        model.setBackgroundVisible(BACKGROUND_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Custom background related">
    /**
     * Returns the custom background paint that will be used instead of
     * the predefined backgroundcolors like DARK_GRAY, BEIGE etc.
     * @return the custom paint that will be used for the background of the gauge
     */
    public java.awt.Paint getCustomBackground()
    {
        return model.getCustomBackground();
    }

    /**
     * Sets the custom background paint that will be used instead of
     * the predefined backgroundcolors like DARK_GRAY, BEIGE etc.
     * @param CUSTOM_BACKGROUND
     */
    public void setCustomBackground(final java.awt.Paint CUSTOM_BACKGROUND)
    {
        model.setCustomBackground(CUSTOM_BACKGROUND);
        if (model.getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {
            init(getInnerBounds().width, getInnerBounds().height);
            repaint(getInnerBounds());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Custom layer related">
    /**
     * Returns true if the custom layer is visible.
     * The custom layer (which is a buffered image) will be
     * drawn on the background of the gauge and could be used
     * to display logos or icons.
     * @return true if custom layer is visible
     */
    public boolean isCustomLayerVisible()
    {
        return model.isCustomLayerVisible();
    }

    /**
     * Enables/disables the usage of the custom layer.
     * The custom layer (which is a buffered image) will be
     * drawn on the background of the gauge and could be used
     * to display logos or icons.
     * @param CUSTOM_LAYER_VISIBLE
     */
    public void setCustomLayerVisible(final boolean CUSTOM_LAYER_VISIBLE)
    {
        if (model.getCustomLayer() != null)
        {
            model.setCustomLayerVisible(CUSTOM_LAYER_VISIBLE);
        }
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the buffered image that represents the custom layer.
     * The custom layer (which is a buffered image) will be
     * drawn on the background of the gauge and could be used
     * to display logos or icons.
     * @return the buffered image that represents the custom layer
     */
    public java.awt.image.BufferedImage getCustomLayer()
    {
        return model.getCustomLayer();
    }

    /**
     * Sets the buffered image that represents the custom layer.
     * It will automaticaly scale the given image to the bounds.
     * The custom layer (which is a buffered image) will be
     * drawn on the background of the gauge and could be used
     * to display logos or icons.
     * @param CUSTOM_LAYER
     */
    public void setCustomLayer(final java.awt.image.BufferedImage CUSTOM_LAYER)
    {
        if (CUSTOM_LAYER == null)
        {
            model.setCustomLayerVisible(false);
            return;
        }

        model.setCustomLayer(CUSTOM_LAYER);

        if (model.isCustomLayerVisible())
        {
            init(getInnerBounds().width, getInnerBounds().height);
            repaint(getInnerBounds());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Foreground related">
    /**
     * Returns true if the foreground image is visible
     * The foreground image will only be painted if
     * it is set to true.
     * @return visibility of the foreground image
     */
    public boolean isForegroundVisible()
    {
        return model.isForegroundVisible();
    }

    /**
     * Enables/Disables the visibility of the glass effect foreground image.
     * If enabled the foregroundImage will be painted.
     * @param FOREGROUND_VISIBLE
     */
    public void setForegroundVisible(final boolean FOREGROUND_VISIBLE)
    {
        model.setForegroundVisible(FOREGROUND_VISIBLE);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Miscellaneous">
    /**
     * Returns a image of a led with the given size, state and color.
     * If the LED_COLOR parameter equals CUSTOM the customLedColor will be used
     * to calculate the custom led colors
     * @param SIZE
     * @param STATE
     * @param LED_COLOR 
     * @return the led image 
     */
    protected final java.awt.image.BufferedImage create_LED_Image(final int SIZE, final int STATE,
                                                                  final eu.hansolo.steelseries.tools.LedColor LED_COLOR)
    {
        return LED_FACTORY.create_LED_Image(SIZE, STATE, LED_COLOR, model.getCustomLedColor());
    }

    /**
     * Calculates the rectangle that is defined by the dimension of the component
     * and it's insets given by e.g. a border.
     */
    abstract public void calcInnerBounds();

    /**
     * Returns the rectangle that is defined by the dimension of the component and
     * it's insets given by e.g. a border.
     * @return rectangle that defines the inner area available for painting
     */
    abstract public java.awt.Rectangle getInnerBounds();

    /**
     * Returns a point2d object that defines the center of the gauge.
     * This method will take the insets and the real position of the
     * gauge into account.
     * @return a point2d object that represents the center of the gauge
     */
    abstract protected java.awt.geom.Point2D getCenter();

    /**
     * Returns the boundary of the gauge itself as a rectangle2d.
     * @return a rectangle2d that represents the boundary of the gauge itself
     */
    abstract protected java.awt.geom.Rectangle2D getBounds2D();

    @Override
    public void setBorder(final javax.swing.border.Border BORDER)
    {
        super.setBorder(BORDER);
        calcInnerBounds();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public javax.swing.JComponent clone()
    {
        try
        {
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream objOut = new java.io.ObjectOutputStream(out);
            objOut.writeObject(this);
            objOut.flush();
            java.io.ObjectInputStream objIn = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(out.toByteArray()));
            javax.swing.JComponent compClone = (javax.swing.JComponent) objIn.readObject();
            objOut.close();
            objIn.close();
            return compClone;
        }
        catch (java.io.IOException exception)
        {
        }
        catch (java.lang.ClassNotFoundException exception)
        {
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Component listener methods">
    @Override
    public void componentResized(java.awt.event.ComponentEvent event)
    {
        // Radial gauge
        if (event.getComponent() instanceof AbstractRadial)
        {
            final int SIZE = getWidth() < getHeight() ? getWidth() : getHeight();
            setSize(SIZE, SIZE);
            setPreferredSize(getSize());

            if (SIZE < getMinimumSize().width || SIZE < getMinimumSize().height)
            {
                setSize(getMinimumSize());
            }

            calcInnerBounds();

            recreateLedImages();
            if (isLedOn())
            {
                setCurrentLedImage(getLedImageOn());
            }
            else
            {
                setCurrentLedImage(getLedImageOff());
            }

            init(getInnerBounds().width, getInnerBounds().height);
            revalidate();
            repaint();
        }

        // Linear gauge
        if (event.getComponent() instanceof AbstractLinear)
        {
            setSize(getWidth(), getHeight());
            setPreferredSize(getSize());
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

            init(getInnerBounds().width, getInnerBounds().height);
            revalidate();
            repaint();
        }
    }

    @Override
    public void componentMoved(java.awt.event.ComponentEvent event)
    {
    }

    @Override
    public void componentShown(java.awt.event.ComponentEvent event)
    {
    }

    @Override
    public void componentHidden(java.awt.event.ComponentEvent event)
    {
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change listener methods">
    /**
     * Add a given ChangeListener to the list of listeners
     * @param LISTENER 
     */
    public void addChangeListener(final javax.swing.event.ChangeListener LISTENER)
    {
        LISTENER_LIST.add(javax.swing.event.ChangeListener.class, LISTENER);
    }

    /**
     * Remove the given ChangeListener from the list of listeners
     * @param LISTENER 
     */
    public void removeChangeListener(javax.swing.event.ChangeListener LISTENER)
    {
        LISTENER_LIST.remove(javax.swing.event.ChangeListener.class, LISTENER);
    }

    /**
     * Notify all registered listeners about a state change
     */
    protected void fireStateChanged()
    {
        Object[] listeners = LISTENER_LIST.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == javax.swing.event.ChangeListener.class)
            {
                if (changeEvent == null)
                {
                    changeEvent = new javax.swing.event.ChangeEvent(this);
                }
                ((javax.swing.event.ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PropertyChange listener methods">
    @Override
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener LISTENER)
    {
        propertySupport.addPropertyChangeListener(LISTENER);
    }

    @Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener LISTENER)
    {
        propertySupport.removePropertyChangeListener(LISTENER);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ActionListener methods">
    @Override
    public void actionPerformed(final java.awt.event.ActionEvent EVENT)
    {
        if (EVENT.getSource().equals(LED_BLINKING_TIMER))
        {
            currentLedImage.flush();
            currentLedImage = ledOn == true ? getLedImageOn() : getLedImageOff();
            ledOn ^= true;

            repaint((int) (getInnerBounds().width * getLedPosition().getX() + getInnerBounds().x), (int) (getInnerBounds().height * getLedPosition().getY() + getInnerBounds().y), currentLedImage.getWidth(), currentLedImage.getHeight());
            //repaint(getInnerBounds());
        }

        if (EVENT.getSource().equals(PEAK_TIMER))
        {
            setPeakValueVisible(false);
            PEAK_TIMER.stop();
        }
    }
    // </editor-fold>
}
