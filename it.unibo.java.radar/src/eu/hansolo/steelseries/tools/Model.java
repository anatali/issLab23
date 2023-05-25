package eu.hansolo.steelseries.tools;

/**
 *
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class Model implements java.lang.Cloneable
{
    // <editor-fold defaultstate="collapsed" desc="Variable definitions">    
    private javax.swing.event.ChangeEvent changeEvent;
    private final javax.swing.event.EventListenerList LISTENER_LIST = new javax.swing.event.EventListenerList();
    private java.awt.Rectangle bounds;
    private double minValue;
    private double maxValue;
    private double range;
    private double value;
    private double oldValue;
    private double peakValue;
    private boolean peakValueVisible;
    private boolean singleLedBargraphEnabled;
    private boolean autoResetToZero;
    private boolean frameVisible;
    private eu.hansolo.steelseries.tools.FrameEffect frameEffect;    
    private boolean backgroundVisible;
    private boolean titleVisible;
    private boolean unitVisible;
    private boolean customTitleAndUnitFontEnabled;
    private boolean customLayerVisible;
    private boolean ledVisible;
    private boolean lcdVisible;
    private boolean lcdUnitStringVisible;
    private boolean lcdScientificFormatEnabled;
    private boolean valueCoupled;
    private boolean digitalFontEnabled;
    private boolean customLcdUnitFontEnabled;
    private eu.hansolo.steelseries.tools.NumberSystem numberSystem;
    private boolean foregroundVisible;
    private boolean tickmarksVisible;
    private boolean ticklabelsVisible;
    private boolean minorTickmarksVisible;
    private boolean majorTickmarksVisible;
    private boolean tickmarkColorFromThemeEnabled;
    private boolean labelColorFromThemeEnabled;
    private double threshold;
    private boolean thresholdVisible;
    private eu.hansolo.steelseries.tools.ColorDef thresholdColor;
    private eu.hansolo.steelseries.tools.CustomColorDef customThresholdColor;
    private eu.hansolo.steelseries.tools.ThresholdType thresholdType;
    private double minMeasuredValue;
    private boolean minMeasuredValueVisible;
    private double maxMeasuredValue;
    private boolean maxMeasuredValueVisible;
    private boolean rangeOfMeasuredValuesVisible;
    private java.awt.Shape radialRangeOfMeasuredValues = new java.awt.geom.Arc2D.Double();
    private boolean collectingData;
    private double trackStart;
    private double trackSection;
    private double trackStop;
    private boolean trackVisible;
    private java.util.ArrayList<eu.hansolo.steelseries.tools.Section> sections;
    private boolean sectionsVisible;
    private java.util.ArrayList<eu.hansolo.steelseries.tools.Section> areas;
    private boolean areasVisible;
    private java.util.ArrayList<eu.hansolo.steelseries.tools.Section> tickmarkSections;
    private boolean tickmarkSectionsVisible;
    private eu.hansolo.steelseries.tools.GaugeType gaugeType;
    private double angleStep;
    private boolean niceScale = true;
    private double niceRange;
    private int maxNoOfMajorTicks;
    private int maxNoOfMinorTicks;
    private double majorTickSpacing;
    private double minorTickSpacing;
    private double niceMinValue;
    private double niceMaxValue;
    private eu.hansolo.steelseries.tools.BackgroundColor backgroundColor;
    private java.awt.Paint customBackground;
    private java.awt.image.BufferedImage customLayer;
    private eu.hansolo.steelseries.tools.FrameType frameType;
    private eu.hansolo.steelseries.tools.FrameDesign frameDesign;
    private java.awt.Paint customFrameDesign;
    private eu.hansolo.steelseries.tools.LedColor ledColor;    
    private eu.hansolo.steelseries.tools.CustomLedColor customLedColor;
    private eu.hansolo.steelseries.tools.LcdColor lcdColor;
    private java.awt.Paint customLcdColor;
    private java.awt.Color customLcdForegroundColor;
    private eu.hansolo.steelseries.tools.TickmarkType minorTickmarkType;
    private eu.hansolo.steelseries.tools.TickmarkType majorTickmarkType;
    private eu.hansolo.steelseries.tools.NumberFormat labelNumberFormat;
    private java.awt.Color tickmarkColor;
    private java.awt.Color customTickmarkColor;
    private java.awt.Color labelColor;
    private java.awt.Color trackStartColor;
    private java.awt.Color trackSectionColor;
    private java.awt.Color trackStopColor;
    private eu.hansolo.steelseries.tools.PointerType pointerType;
    private eu.hansolo.steelseries.tools.ColorDef pointerColor;
    private eu.hansolo.steelseries.tools.CustomColorDef customPointerColorObject;
    private eu.hansolo.steelseries.tools.ColorDef valueColor;
    private eu.hansolo.steelseries.tools.CustomColorDef customValueColorObject;
    private eu.hansolo.steelseries.tools.KnobType knobType;
    private eu.hansolo.steelseries.tools.KnobStyle knobStyle;
    private eu.hansolo.steelseries.tools.ForegroundType foregroundType;
    private java.awt.Font lcdValueFont;
    private java.awt.Font lcdUnitFont;
    private java.awt.Font customLcdUnitFont;
    private java.awt.Font lcdInfoFont;
    private int lcdDecimals;
    private java.awt.Paint rangeOfMeasuredValuesPaint;
    private final java.awt.Font STANDARD_BASE_FONT = new java.awt.Font("Verdana", 1, 24);
    private final java.awt.Font STANDARD_INFO_FONT = new java.awt.Font("Verdana", 0, 24);
    private final java.awt.Font DIGITAL_BASE_FONT = eu.hansolo.steelseries.tools.Util.INSTANCE.getDigitalFont().deriveFont(24);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    /**
     * Creates a new Model with it's default parameters
     */
    public Model()
    {
        sections = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
        areas = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
        tickmarkSections = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);        
        init();
    }

    private void init()
    {
        bounds = new java.awt.Rectangle();
        minValue = 0;
        maxValue = 100;
        range = maxValue - minValue;
        value = minValue;
        oldValue = minValue;
        peakValue = minValue;
        peakValueVisible = false;
        autoResetToZero = false;
        frameVisible = true;
        frameEffect = eu.hansolo.steelseries.tools.FrameEffect.NONE;        
        backgroundVisible = true;
        titleVisible = true;
        unitVisible = true;
        customTitleAndUnitFontEnabled = false;
        customLayerVisible = false;
        ledVisible = true;
        lcdVisible = true;
        lcdUnitStringVisible = false;
        lcdScientificFormatEnabled = false;
        valueCoupled = true;
        digitalFontEnabled = false;
        customLcdUnitFontEnabled = false;
        numberSystem = eu.hansolo.steelseries.tools.NumberSystem.DEC;
        foregroundVisible = true;
        tickmarksVisible = true;
        ticklabelsVisible = true;
        minorTickmarksVisible = true;
        majorTickmarksVisible = true;
        tickmarkColorFromThemeEnabled = true;
        labelColorFromThemeEnabled = true;
        threshold = range / 2.0;
        thresholdVisible = false;
        thresholdColor = eu.hansolo.steelseries.tools.ColorDef.RED;
        customThresholdColor = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.RED);
        thresholdType = eu.hansolo.steelseries.tools.ThresholdType.TRIANGLE;
        minMeasuredValue = maxValue;
        minMeasuredValueVisible = false;
        maxMeasuredValue = minValue;
        maxMeasuredValueVisible = false;
        rangeOfMeasuredValuesVisible = false;
        collectingData = false;
        trackStart = minValue;
        trackSection = range / 2.0;
        trackStop = maxValue;
        trackVisible = false;
        sections.clear();
        sectionsVisible = false;
        areas.clear();
        areasVisible = false;
        tickmarkSections.clear();
        tickmarkSectionsVisible = false;
        gaugeType = eu.hansolo.steelseries.tools.GaugeType.TYPE4;
        angleStep = (2.0 * Math.PI - gaugeType.FREE_AREA_ANGLE) / range;
        maxNoOfMajorTicks = 10;
        maxNoOfMinorTicks = 10;
        backgroundColor = eu.hansolo.steelseries.tools.BackgroundColor.DARK_GRAY;
        customBackground = java.awt.Color.RED;
        customLayer = null;
        frameType = eu.hansolo.steelseries.tools.FrameType.ROUND;
        frameDesign = eu.hansolo.steelseries.tools.FrameDesign.METAL;
        customFrameDesign = java.awt.Color.RED;
        ledColor = eu.hansolo.steelseries.tools.LedColor.RED_LED;        
        customLedColor = new eu.hansolo.steelseries.tools.CustomLedColor(java.awt.Color.RED);
        lcdColor = eu.hansolo.steelseries.tools.LcdColor.WHITE_LCD;
        customLcdColor = java.awt.Color.RED;
        customLcdForegroundColor = java.awt.Color.BLACK;
        minorTickmarkType = eu.hansolo.steelseries.tools.TickmarkType.LINE;
        majorTickmarkType = eu.hansolo.steelseries.tools.TickmarkType.LINE;
        labelNumberFormat = eu.hansolo.steelseries.tools.NumberFormat.AUTO;
        tickmarkColor = backgroundColor.LABEL_COLOR;
        customTickmarkColor = java.awt.Color.BLACK;
        labelColor = backgroundColor.LABEL_COLOR;
        trackStartColor = new java.awt.Color(0.0f, 1.0f, 0.0f, 0.35f);
        trackSectionColor = new java.awt.Color(1.0f, 1.0f, 0.0f, 0.35f);
        trackStopColor = new java.awt.Color(1.0f, 0.0f, 0.0f, 0.35f);
        pointerType = eu.hansolo.steelseries.tools.PointerType.TYPE1;
        pointerColor = eu.hansolo.steelseries.tools.ColorDef.RED;
        customPointerColorObject = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.RED);
        valueColor = eu.hansolo.steelseries.tools.ColorDef.RED;
        customValueColorObject = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.RED);
        knobType = eu.hansolo.steelseries.tools.KnobType.SMALL_STD_KNOB;
        knobStyle = eu.hansolo.steelseries.tools.KnobStyle.SILVER;
        foregroundType = eu.hansolo.steelseries.tools.ForegroundType.FG_TYPE1;
        customLcdUnitFont = new java.awt.Font("Verdana", 1, 24);
        lcdInfoFont = new java.awt.Font("Verdana", 0, 24);
        rangeOfMeasuredValuesPaint = new java.awt.Color(1.0f, 0.0f, 0.0f, 0.1f);

        calculate();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">            
    /**
     * Returns the size of the gauge as a rectangle
     * @return the size of the gauge as a rectangle
     */
    public java.awt.Rectangle getSize()
    {
        return bounds;
    }

    /**
     * Sets the width and height of the gauge
     * @param X 
     * @param Y 
     * @param WIDTH
     * @param HEIGHT 
     */
    public void setSize(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        bounds.setBounds(X, Y, WIDTH, HEIGHT);
        fireStateChanged();
    }

    /**
     * Returns the minimum value that will be displayed by the gauge
     * @return the minium value that will be displayed by the gauge
     */
    public double getMinValue()
    {
        return minValue;
    }

    /**
     * Sets the minium value that will be used for the calculation
     * of the nice minimum value for the scale.
     * @param MIN_VALUE 
     */
    public void setMinValue(final double MIN_VALUE)
    {
        minValue = MIN_VALUE;
        calculate();
        validate();
        calcAngleStep();
        fireStateChanged();
    }

    /**
     * Returns the maximum value that will be displayed by the gauge
     * @return the maximum value that will be displayed by the gauge
     */
    public double getMaxValue()
    {
        return maxValue;
    }

    /**
     * Sets the maximum value that will be used for the calculation
     * of the nice maximum vlaue for the scale.
     * @param MAX_VALUE 
     */
    public void setMaxValue(final double MAX_VALUE)
    {
        System.out.println("Model setMaxValue(" + MAX_VALUE + ")");
        maxValue = MAX_VALUE;
        calculate();
        validate();
        calcAngleStep();
        fireStateChanged();
    }

    /**
     * Returns the difference between the maximum and minimum value
     * @return the difference between the maximum and minimum value
     */
    public double getRange()
    {
        return (maxValue - minValue);
    }

    /**
     * Sets the minimum and maximum value for the calculation of the
     * nice minimum and nice maximum values.
     * @param MIN_VALUE
     * @param MAX_VALUE 
     */
    public void setRange(final double MIN_VALUE, final double MAX_VALUE)
    {
        maxValue = MAX_VALUE;
        minValue = MIN_VALUE;
        calculate();
        validate();
        calcAngleStep();
        fireStateChanged();
    }

    /**
     * Returns the current value of the gauge
     * @return the current value of the gauge
     */
    public double getValue()
    {
        return value;
    }

    /**
     * Sets the current value of the gauge
     * @param VALUE 
     */
    public void setValue(final double VALUE)
    {
        oldValue = value;        
                
        value = VALUE < niceMinValue ? niceMinValue : (VALUE > niceMaxValue ? niceMaxValue : VALUE);        
        
        fireStateChanged();
    }

    /**
     * Returns the old value of the gauge
     * @return the old value of the gauge
     */
    public double getOldValue()
    {
        return oldValue;
    }

    /**
     * Returns the peak value of the gauge
     * @return the peak value of the gauge
     */
    public double getPeakValue()
    {
        return peakValue;
    }

    /**
     * Sets the peak value of the gauge
     * @param PEAK_VALUE 
     */
    public void setPeakValue(final double PEAK_VALUE)
    {
        peakValue = PEAK_VALUE;
        fireStateChanged();
    }

    /**
     * Returns true if the peak value is visible
     * @return true if the peak value is visible
     */
    public boolean isPeakValueVisible()
    {
        return peakValueVisible;
    }

    /**
     * Enables / disables the visibility of the peak value
     * @param PEAK_VALUE_VISIBLE 
     */
    public void setPeakValueVisible(final boolean PEAK_VALUE_VISIBLE)
    {
        peakValueVisible = PEAK_VALUE_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the single led bargraph feature is enabled
     * @return true if the single led bargraph feature is enabled
     */
    public boolean isSingleLedBargraphEnabled()
    {
        return singleLedBargraphEnabled;
    }

    /**
     * Enables / disables the single led bargraph feature of the gauge
     * @param SINGLE_LED_BARGRAPH_ENABLED 
     */
    public void setSingleLedBargraphEnabled(final boolean SINGLE_LED_BARGRAPH_ENABLED)
    {
        singleLedBargraphEnabled = SINGLE_LED_BARGRAPH_ENABLED;
        fireStateChanged();
    }

    /**
     * Returns true if the auto reset to zero feature is enabled.
     * The auto reset to zero feature will automaticaly reset the value
     * to zero after it reached the given value.
     * @return true if the auto reset to zero feature is enabled
     */
    public boolean isAutoResetToZero()
    {
        return autoResetToZero;
    }

    /**
     * Enables / disables the auto reset to zero feature
     * @param AUTO_RESET_TO_ZERO 
     */
    public void setAutoResetToZero(final boolean AUTO_RESET_TO_ZERO)
    {
        if (niceMinValue > 0 || niceMaxValue < 0)
        {
            autoResetToZero = false;
        }
        else
        {
            autoResetToZero = AUTO_RESET_TO_ZERO;
        }
        fireStateChanged();
    }

    /**
     * Returns true if the frame of the gauge is visible
     * @return true if the frame of the gauge is visible
     */
    public boolean isFrameVisible()
    {
        return frameVisible;
    }

    /**
     * Enables / disables the visibility of the frame
     * @param FRAME_VISIBLE 
     */
    public void setFrameVisible(final boolean FRAME_VISIBLE)
    {
        frameVisible = FRAME_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns the frame effect
     * @return the frame effect
     */
    public eu.hansolo.steelseries.tools.FrameEffect getFrameEffect()
    {
        return frameEffect;
    }

    /**
     * Sets the frame effect
     * @param FRAME_EFFECT 
     */
    public void setFrameEffect(final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT)
    {
        frameEffect = FRAME_EFFECT;
        fireStateChanged();
    }

    /**
     * Returns true if the background of the gauge is visible
     * @return true if the background of the gauge is visible
     */
    public boolean isBackgroundVisible()
    {
        return backgroundVisible;
    }

    /**
     * Enables / disables the visibility of the gauge background
     * @param BACKGROUND_VISIBLE 
     */
    public void setBackgroundVisible(final boolean BACKGROUND_VISIBLE)
    {
        backgroundVisible = BACKGROUND_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the title of the gauge is visible
     * @return true if the title of the gauge is visible
     */
    public boolean isTitleVisible()
    {
        return titleVisible;
    }

    /**
     * Enables / disables the visibility of the gauge title
     * @param TITLE_VISIBLE 
     */
    public void setTitleVisible(final boolean TITLE_VISIBLE)
    {
        titleVisible = TITLE_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the unit of the gauge is visible
     * @return true if the unit of the gauge is visible
     */
    public boolean isUnitVisible()
    {
        return unitVisible;
    }

    /**
     * Enables / disables the visibility of the gauge unit
     * @param UNIT_VISIBLE 
     */
    public void setUnitVisible(final boolean UNIT_VISIBLE)
    {
        unitVisible = UNIT_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the custom font for the title and unit
     * will be used
     * @return true if the custom font for the title and unit will be used
     */
    public boolean isCustomTitleAndUnitFontEnabled()
    {
        return customTitleAndUnitFontEnabled;
    }

    /**
     * Enables / disables the usage of the custom font for the gauge title and unit
     * @param CUSTOM_TITLE_AND_UNIT_FONT_ENABLED 
     */
    public void setCustomTitleAndUnitFontEnabled(final boolean CUSTOM_TITLE_AND_UNIT_FONT_ENABLED)
    {
        customTitleAndUnitFontEnabled = CUSTOM_TITLE_AND_UNIT_FONT_ENABLED;
        fireStateChanged();
    }

    /**
     * Returns true if the custom layer of the gauge is visible
     * @return true if the custom layer of the gauge is visible
     */
    public boolean isCustomLayerVisible()
    {
        return customLayerVisible;
    }

    /**
     * Enables / disables the visibility of the gauge custom layer
     * @param CUSTOM_LAYER_VISIBLE 
     */
    public void setCustomLayerVisible(final boolean CUSTOM_LAYER_VISIBLE)
    {
        customLayerVisible = CUSTOM_LAYER_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the threshold led of the gauge is visible (if led available)
     * @return true if the threshold led of the gauge is visible (if led available)
     */
    public boolean isLedVisible()
    {
        return ledVisible;
    }

    /**
     * Enables / disables the visibility of the gauge threshold led (if led available)
     * @param LED_VISIBLE 
     */
    public void setLedVisible(final boolean LED_VISIBLE)
    {
        ledVisible = LED_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the lcd display of the gauge is visible (if lcd available)
     * @return true if the lcd display of the gauge is visible (if lcd available)
     */
    public boolean isLcdVisible()
    {
        return lcdVisible;
    }

    /**
     * Enables / disables the visibility of the gauge lcd display (if lcd available)
     * @param LCD_VISIBLE 
     */
    public void setLcdVisible(final boolean LCD_VISIBLE)
    {
        lcdVisible = LCD_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the unit in the lcd display of the gauge is visible (if lcd available)
     * @return true if the unit in the lcd display of the gauge is visible (if lcd available)
     */
    public boolean isLcdUnitStringVisible()
    {
        return lcdUnitStringVisible;
    }

    /**
     * Enables / disables the visibility of the unit string in the lcd display of the gauge (if lcd available)
     * @param LCD_UNIT_STRING_VISIBLE 
     */
    public void setLcdUnitStringVisible(final boolean LCD_UNIT_STRING_VISIBLE)
    {
        lcdUnitStringVisible = LCD_UNIT_STRING_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the lcd value will be displayed in a scientific format (if lcd available)
     * @return true if the lcd value will be displayed in a scientific format (if lcd available)
     */
    public boolean isLcdScientificFormatEnabled()
    {
        return lcdScientificFormatEnabled;
    }

    /**
     * Enables / disables the scientific format of the lcd value (if lcd available)
     * @param LCD_SCIENTIFIC_FORMAT_ENABLED 
     */
    public void setLcdScientificFormatEnabled(final boolean LCD_SCIENTIFIC_FORMAT_ENABLED)
    {
        lcdScientificFormatEnabled = LCD_SCIENTIFIC_FORMAT_ENABLED;
        fireStateChanged();
    }

    /**
     * Returns true if the value of the lcd display is coupled to the value of the gauge (if lcd available)
     * @return true if the value of the lcd display is coupled to the value of the gauge (if lcd available)
     */
    public boolean isValueCoupled()
    {
        return valueCoupled;
    }

    /**
     * Enables / disables the coupling of the lcd value and the gauge value (if lcd available)
     * @param VALUE_COUPLED 
     */
    public void setValueCoupled(final boolean VALUE_COUPLED)
    {
        valueCoupled = VALUE_COUPLED;
        fireStateChanged();
    }

    /**
     * Returns true if the lcd display will use the digital font to display the values (if lcd available)
     * @return true if the lcd display will use the digital font to display the values (if lcd available)
     */
    public boolean isDigitalFontEnabled()
    {
        return digitalFontEnabled;
    }

    /**
     * Enables / disables the usage of the digital font in the lcd display of the gauge (if lcd available)
     * @param DIGITAL_FONT_ENABLED 
     */
    public void setDigitalFontEnabled(final boolean DIGITAL_FONT_ENABLED)
    {
        digitalFontEnabled = DIGITAL_FONT_ENABLED;
        fireStateChanged();
    }

    /**
     * Returns true if the custom font for the unit in the lcd display of the gauge is enabled (if lcd available)
     * @return true if the custom font for the unit in the lcd display of the gauge is enabled (if lcd available)
     */
    public boolean isCustomLcdUnitFontEnabled()
    {
        return customLcdUnitFontEnabled;
    }

    /**
     * Enables / disables the usage of the custom unit font in the lcd display of the gauge (if lcd available)
     * @param CUSTOM_LCD_UNIT_FONT_ENABLED 
     */
    public void setCustomLcdUnitFontEnabled(final boolean CUSTOM_LCD_UNIT_FONT_ENABLED)
    {
        customLcdUnitFontEnabled = CUSTOM_LCD_UNIT_FONT_ENABLED;
        fireStateChanged();
    }
    
    /**
     * Returns the number system that will be used to display the current lcd value
     * @return the number system that will be used to display the current lcd value
     */
    public eu.hansolo.steelseries.tools.NumberSystem getNumberSystem()
    {
        return numberSystem;
    }
    
    /**
     * Sets the number system that will be used to display the current lcd value
     * @param NUMBER_SYSTEM 
     */
    public void setNumberSystem(final eu.hansolo.steelseries.tools.NumberSystem NUMBER_SYSTEM)
    {
        numberSystem = NUMBER_SYSTEM;
        fireStateChanged();
    }
    
    /**
     * Returns true if the foreground (highlight) of the gauge is visible
     * @return true if the foreground (highlight) of the gauge is visible
     */
    public boolean isForegroundVisible()
    {
        return foregroundVisible;
    }

    /**
     * Enables /disables the visibility of the foreground (highlight) of the gauge
     * @param FOREGROUND_VISIBLE 
     */
    public void setForegroundVisible(final boolean FOREGROUND_VISIBLE)
    {
        foregroundVisible = FOREGROUND_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the minor tickmarks of the gauge dial are visible
     * @return true if the minor tickmarks of the gauge dial are visible
     */
    public boolean isMinorTickmarksVisible()
    {
        return minorTickmarksVisible;
    }

    /**
     * Enables / disables the visibility of the minor tickmarks of the gauge dial
     * @param MINOR_TICKMARKS_VISIBLE 
     */
    public void setMinorTickmarksVisible(final boolean MINOR_TICKMARKS_VISIBLE)
    {
        minorTickmarksVisible = MINOR_TICKMARKS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the major tickmarks of the gauge dial are visible
     * @return true if the major tickmarks of the gauge dial are visible
     */
    public boolean isMajorTickmarksVisible()
    {
        return majorTickmarksVisible;
    }

    /**
     * Enables / disables the visibility of the major tickmarks of the gauge dial
     * @param MAJOR_TICKMARKS_VISIBLE 
     */
    public void setMajorTickmarksVisible(final boolean MAJOR_TICKMARKS_VISIBLE)
    {
        majorTickmarksVisible = MAJOR_TICKMARKS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the color for the tickmarks will be taken from the current gauge background color
     * @return true if the color for the tickmarks will be taken from the current gauge background color
     */
    public boolean isTickmarkColorFromThemeEnabled()
    {
        return tickmarkColorFromThemeEnabled;
    }

    /**
     * Enables / disables the usage of the color from the current gauge background color for the tickmarks
     * @param TICKMARK_COLOR_FROM_THEME_ENABLED 
     */
    public void setTickmarkColorFromThemeEnabled(final boolean TICKMARK_COLOR_FROM_THEME_ENABLED)
    {
        tickmarkColorFromThemeEnabled = TICKMARK_COLOR_FROM_THEME_ENABLED;
        fireStateChanged();
    }

    /**
     * Returns true if the color for the ticklabels will be taken from the current gauge background color
     * @return true if the color for the ticklabels will be taken from the current gauge background color
     */
    public boolean isLabelColorFromThemeEnabled()
    {
        return labelColorFromThemeEnabled;
    }

    /**
     * Enables / disables the usage of the color from the current gauge background color for the tickmarks
     * @param LABEL_COLOR_FROM_THEME_ENABLED 
     */
    public void setLabelColorFromThemeEnabled(final boolean LABEL_COLOR_FROM_THEME_ENABLED)
    {
        labelColorFromThemeEnabled = LABEL_COLOR_FROM_THEME_ENABLED;
        fireStateChanged();
    }

    /**
     * Returns true if the tickmarks of the gauge dial are visible
     * @return true if the tickmarks of the gauge dial are visible
     */
    public boolean isTickmarksVisible()
    {
        return tickmarksVisible;
    }

    /**
     * Enables / disables the visibility of the tickmarks in the gauge dial
     * @param TICKMARKS_VISIBLE 
     */
    public void setTickmarksVisible(final boolean TICKMARKS_VISIBLE)
    {
        tickmarksVisible = TICKMARKS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the tick labels of the gauge dial are visible
     * @return true if the tick labels of the gauge dial are visible
     */
    public boolean isTicklabelsVisible()
    {
        return ticklabelsVisible;
    }

    /**
     * Enables / disables the visibility of the ticklabels in the gauge dial
     * @param TICKLABELS_VISIBLE 
     */
    public void setTicklabelsVisible(final boolean TICKLABELS_VISIBLE)
    {
        ticklabelsVisible = TICKLABELS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns the value of the current threshold of the gauge
     * @return the value of the current threshold of the gauge
     */
    public double getThreshold()
    {
        return threshold;
    }

    /**
     * Sets the value for the threshold of the gauge
     * @param THRESHOLD 
     */
    public void setThreshold(final double THRESHOLD)
    {
        if (Double.compare(THRESHOLD, minValue) >= 0 && Double.compare(THRESHOLD, maxValue) <= 0)
        {
            threshold = THRESHOLD;
        }
        else
        {
            if (THRESHOLD < niceMinValue)
            {
                threshold = niceMinValue;
            }

            if (THRESHOLD > niceMaxValue)
            {
                threshold = niceMaxValue;
            }
        }
        fireStateChanged();
    }

    /**
     * Returns true if the threshold indicator of the gauge is visible
     * @return true if the threshold indicator of the gauge is visible
     */
    public boolean isThresholdVisible()
    {
        return thresholdVisible;
    }

    /**
     * Enables / disables the visibility of the threshold indicator of the gauge
     * @param THRESHOLD_VISIBLE 
     */
    public void setThresholdVisible(final boolean THRESHOLD_VISIBLE)
    {
        thresholdVisible = THRESHOLD_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns the color definition of the threshold indicator
     * @return the color definition of the threshold indicator
     */
    public eu.hansolo.steelseries.tools.ColorDef getThresholdColor()
    {
        return thresholdColor;
    }
    
    /**
     * Sets the color definition of the threshold indicator
     * @param THRESHOLD_COLOR 
     */
    public void setThresholdColor(final eu.hansolo.steelseries.tools.ColorDef THRESHOLD_COLOR)
    {
        thresholdColor = THRESHOLD_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the custom color definition of the threshold indicator
     * @return the custom color definition of the threshold indicator
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomThresholdColor()
    {
        return customThresholdColor;
    }
    
    /**
     * Sets the custom color definition of the threshold indicator
     * @param CUSTOM_THRESHOLD_COLOR 
     */
    public void setCustomThresholdColor(final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_THRESHOLD_COLOR)
    {
        customThresholdColor = CUSTOM_THRESHOLD_COLOR;
        fireStateChanged();
    }
    
    /**
     * Returns the type of the threshold indicator
     * @return the type of the threshold indicator
     */
    public eu.hansolo.steelseries.tools.ThresholdType getThresholdType()
    {
        return thresholdType;
    }
    
    /**
     * Sets the type of the threshold indicator
     * @param THRESHOLD_TYPE 
     */
    public void setThresholdType(final eu.hansolo.steelseries.tools.ThresholdType THRESHOLD_TYPE)
    {
        thresholdType = THRESHOLD_TYPE;
        fireStateChanged();
    }
    
    /**
     * Returns the minimum measured value of the gauge
     * @return the minimum measured value of the gauge
     */
    public double getMinMeasuredValue()
    {
        return minMeasuredValue;
    }

    /**
     * Sets the minimum measured value of the gauge to the given value
     * @param MIN_MEASURED_VALUE 
     */
    public void setMinMeasuredValue(final double MIN_MEASURED_VALUE)
    {
        if (Double.compare(MIN_MEASURED_VALUE, niceMinValue) >= 0 && Double.compare(MIN_MEASURED_VALUE, niceMaxValue) <= 0)
        {
            minMeasuredValue = MIN_MEASURED_VALUE;
        }
        else
        {
            if (MIN_MEASURED_VALUE < niceMinValue)
            {
                minMeasuredValue = niceMinValue;
            }

            if (MIN_MEASURED_VALUE > niceMaxValue)
            {
                minMeasuredValue = niceMaxValue;
            }
        }
        createRadialRangeOfMeasureValuesArea();
        fireStateChanged();
    }

    /**
     * Resets the minimum measured value to the current value of the gauge
     */
    public void resetMinMeasuredValue()
    {
        minMeasuredValue = value;
        createRadialRangeOfMeasureValuesArea();
        fireStateChanged();
    }

    /**
     * Resets the minimum measured value of the gauge to the given value
     * @param MIN_MEASURED_VALUE 
     */
    public void resetMinMeasuredValue(final double MIN_MEASURED_VALUE)
    {
        minMeasuredValue = MIN_MEASURED_VALUE < niceMinValue ? niceMinValue : (MIN_MEASURED_VALUE > niceMaxValue ? niceMaxValue : MIN_MEASURED_VALUE);        
        createRadialRangeOfMeasureValuesArea();
        fireStateChanged();
    }

    /**
     * Returns true if the minimum measured value indicator of the gauge is visible
     * @return true if the minimum measured value indicator of the gauge is visible
     */
    public boolean isMinMeasuredValueVisible()
    {
        return minMeasuredValueVisible;
    }

    /**
     * Enables / disables the visibility of the minimum measured value indicator of the gauge
     * @param MIN_MEASURED_VALUE_VISIBLE 
     */
    public void setMinMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE)
    {
        minMeasuredValueVisible = MIN_MEASURED_VALUE_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns the maximum measured value of the gauge
     * @return the maximum measured value of the gauge
     */
    public double getMaxMeasuredValue()
    {
        return maxMeasuredValue;
    }

    /**
     * Sets the maximum measured value of the gauge to the given value
     * @param MAX_MEASURED_VALUE 
     */
    public void setMaxMeasuredValue(final double MAX_MEASURED_VALUE)
    {
        if (Double.compare(MAX_MEASURED_VALUE, niceMinValue) >= 0 && Double.compare(MAX_MEASURED_VALUE, niceMaxValue) <= 0)
        {
            maxMeasuredValue = MAX_MEASURED_VALUE;
        }
        else
        {
            if (MAX_MEASURED_VALUE < niceMinValue)
            {
                maxMeasuredValue = niceMinValue;
            }

            if (MAX_MEASURED_VALUE > niceMaxValue)
            {
                maxMeasuredValue = niceMaxValue;
            }
        }
        createRadialRangeOfMeasureValuesArea();
        fireStateChanged();
    }

    /**
     * Resets the maximum measured value to the current value of the gauge
     */
    public void resetMaxMeasuredValue()
    {
        maxMeasuredValue = value;
        createRadialRangeOfMeasureValuesArea();
        fireStateChanged();
    }

    /**
     * Resets the maximum measured value of the gauge to the given value
     * @param MAX_MEASURED_VALUE 
     */
    public void resetMaxMeasuredValue(final double MAX_MEASURED_VALUE)
    {        
        maxMeasuredValue = MAX_MEASURED_VALUE < niceMinValue ? niceMinValue : (MAX_MEASURED_VALUE > niceMaxValue ? niceMaxValue : MAX_MEASURED_VALUE);
        createRadialRangeOfMeasureValuesArea();
        fireStateChanged();
    }

    /**
     * Returns true if the maximum measured value indicator of the gauge is visible
     * @return true if the maximum measured value indicator of the gauge is visible
     */
    public boolean isMaxMeasuredValueVisible()
    {
        return maxMeasuredValueVisible;
    }

    /**
     * Enables / disables the visibility of the maximum measured value indicator of the gauge
     * @param MAX_MEASURED_VALUE_VISIBLE 
     */
    public void setMaxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE)
    {
        maxMeasuredValueVisible = MAX_MEASURED_VALUE_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns true if the range of measured values is visible. The range will be visualized
     * by an area which will be filled with a gradient of colors.     
     * @return true if the range of measured values is visible;
     */
    public boolean isRangeOfMeasuredValuesVisible()
    {
        return rangeOfMeasuredValuesVisible;
    }

    /**
     * Enables / disables the visibility of the range of measured values.
     * @param RANGE_OF_MEASURED_VALUES_VISIBLE 
     */
    public void setRangeOfMeasuredValuesVisible(final boolean RANGE_OF_MEASURED_VALUES_VISIBLE)
    {
        rangeOfMeasuredValuesVisible = RANGE_OF_MEASURED_VALUES_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns the paint object that will be used to fill the area of measured values.
     * @return the paint object that will be used to fill the area of measured values.
     */
    public java.awt.Paint getRangeOfMeasuredValuesPaint()
    {
        return rangeOfMeasuredValuesPaint;
    }

    /**
     * Returns the shape that represents the range of measured values
     * @return the shape that represents the range of measured values
     */
    public java.awt.Shape getRadialRangeOfMeasuredValues()
    {
        return radialRangeOfMeasuredValues;
    }

    /**
     * Sets the paint object that will be used to fill the area of measured values to the given paint object.
     * @param RANGE_OF_MEASURED_VALUES_PAINT 
     */
    public void setRangeOfMeasuredValuesPaint(final java.awt.Paint RANGE_OF_MEASURED_VALUES_PAINT)
    {
        rangeOfMeasuredValuesPaint = RANGE_OF_MEASURED_VALUES_PAINT;
        fireStateChanged();
    }

    /**
     * Returns true if the gauge is collecting all measured values to calculate a histogram
     * @return true if the gauge is collecting all measured values to calculate a histogram
     */
    public boolean isCollectingData()
    {
        return collectingData;
    }
    
    /**
     * Enables / disables the collection of measured values
     * @param COLLECTING_DATA 
     */
    public void setCollectingData(final boolean COLLECTING_DATA)
    {
        collectingData = COLLECTING_DATA;
    }
    
    /**
     * Returns the value where the track of the gauge starts
     * @return the value where the track of the gauge starts
     */
    public double getTrackStart()
    {
        return trackStart;
    }

    /**
     * Sets the track start value of the gauge to the given value
     * @param TRACK_START 
     */
    public void setTrackStart(final double TRACK_START)
    {
        trackStart = TRACK_START;
        validate();
        fireStateChanged();
    }

    /**
     * Returns the value of the track section of the gauge
     * @return the value of the track section of the gauge
     */
    public double getTrackSection()
    {
        return trackSection;
    }

    /**
     * Sets the track section of the gauge to the given value
     * @param TRACK_SECTION 
     */
    public void setTrackSection(final double TRACK_SECTION)
    {
        trackSection = TRACK_SECTION;
        validate();
        fireStateChanged();
    }

    /**
     * Returns the value where the track of the gauge stops
     * @return the value where the track of the gauge stops
     */
    public double getTrackStop()
    {
        return trackStop;
    }

    /**
     * Sets the track stop value of the gauge to the given value
     * @param TRACK_STOP 
     */
    public void setTrackStop(final double TRACK_STOP)
    {
        trackStop = TRACK_STOP;
        validate();
        fireStateChanged();
    }

    /**
     * Returns true if the track of the gauge is visible
     * @return true if the track of the gauge is visible
     */
    public boolean isTrackVisible()
    {
        return trackVisible;
    }

    /**
     * Enables / disables the visibility of the three-colored gauge track
     * @param TRACK_VISIBLE 
     */
    public void setTrackVisible(final boolean TRACK_VISIBLE)
    {
        trackVisible = TRACK_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns a list of section objects that will be used to display the sections
     * of a gauge with their different colors
     * @return a list of section objects that represent the sections of the gauge
     */
    public java.util.List<Section> getSections()
    {
        java.util.List<eu.hansolo.steelseries.tools.Section> sectionsCopy = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
        sectionsCopy.addAll(sections);
        //return (java.util.ArrayList<eu.hansolo.steelseries.tools.Section>) SECTIONS.clone();
        return sectionsCopy;
    }

    /**
     * Sets the sections of the gauge to the given array of section objects
     * @param SECTIONS_ARRAY 
     */
    public void setSections(Section... SECTIONS_ARRAY)
    {
        sections.clear();
        for (eu.hansolo.steelseries.tools.Section section : SECTIONS_ARRAY)
        {
            sections.add(new eu.hansolo.steelseries.tools.Section(section.getStart(), section.getStop(), section.getColor()));
        }
        validate();
        fireStateChanged();
    }

    /**
     * Adds the given section object to the list of section objects that will be
     * used to display the sections of a gauge with their different colors
     * @param SECTION 
     */
    public void addSection(Section SECTION)
    {
        sections.add(SECTION);
        fireStateChanged();
    }

    /**
     * Clear the list of sections of the gauge
     */
    public void resetSections()
    {
        sections.clear();
        fireStateChanged();
    }

    /**
     * Returns true if the sections of the gauge are visible
     * @return true if the sections of the gauge are visible
     */
    public boolean isSectionsVisible()
    {
        return sectionsVisible;
    }

    /**
     * Enables / disables the sections of the gauge
     * @param SECTIONS_VISIBLE 
     */
    public void setSectionsVisible(final boolean SECTIONS_VISIBLE)
    {
        sectionsVisible = SECTIONS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns a list of section objects that will used to display the areas of
     * a gauge with their colors.
     * @return a list of section objects that will represent the areas of the gauge
     */
    public java.util.List<Section> getAreas()
    {
        java.util.List<eu.hansolo.steelseries.tools.Section> areasCopy = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
        areasCopy.addAll(areas);
        //return (java.util.ArrayList<eu.hansolo.steelseries.tools.Section>) AREAS.clone();
        return areasCopy;
    }

    /**
     * Sets the areas of the gauge to the given array of section objects
     * @param AREAS_ARRAY 
     */
    public void setAreas(Section... AREAS_ARRAY)
    {
        areas.clear();
        for (eu.hansolo.steelseries.tools.Section area : AREAS_ARRAY)
        {
            areas.add(new eu.hansolo.steelseries.tools.Section(area.getStart(), area.getStop(), area.getColor()));
        }
        validate();
        fireStateChanged();
    }

    /**
     * Adds the given section object to the list of section objects that will be
     * used to display the areas of a gauge with their different colors.
     * @param AREA 
     */
    public void addArea(Section AREA)
    {
        areas.add(AREA);
    }

    /**
     * Clear the list of areas of the gauge
     */
    public void resetAreas()
    {
        areas.clear();
    }

    /**
     * Returns true if the areas of the gauge are visible
     * @return true if the areas of the gauges are visible
     */
    public boolean isAreasVisible()
    {
        return areasVisible;
    }

    /**
     * Enables / disables the visibility of the gauge areas
     * @param AREAS_VISIBLE 
     */
    public void setAreasVisible(final boolean AREAS_VISIBLE)
    {
        areasVisible = AREAS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns a list of section objects that will be used to display
     * to display the tickmark sections of a gauge with their different colors.
     * @return a list of section objects that represents the tickmark sections of the gauge
     */
    public java.util.List<eu.hansolo.steelseries.tools.Section> getTickmarkSections()
    {
        java.util.List<eu.hansolo.steelseries.tools.Section> tickmarkSectionsCopy = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
        tickmarkSectionsCopy.addAll(tickmarkSections);
        //return (java.util.ArrayList<eu.hansolo.steelseries.tools.Section>) TICKMARK_SECTIONS.clone();
        return tickmarkSectionsCopy;
    }

    /**
     * Sets the tickmark sections of the gauge to the given array of section objects
     * @param TICKMARK_SECTIONS_ARRAY 
     */
    public void setTickmarkSections(final eu.hansolo.steelseries.tools.Section... TICKMARK_SECTIONS_ARRAY)
    {
        tickmarkSections.clear();
        for (eu.hansolo.steelseries.tools.Section tickmarkSection : TICKMARK_SECTIONS_ARRAY)
        {
            tickmarkSections.add(new eu.hansolo.steelseries.tools.Section(tickmarkSection.getStart(), tickmarkSection.getStop(), tickmarkSection.getColor()));
        }
        validate();
        fireStateChanged();
    }

    /**
     * Adds the given section object to the list of section objects that will be
     * used to display the tickmark sections of a gauge with their different colors
     * @param TICKMARK_SECTION 
     */
    public void addTickmarkSection(final eu.hansolo.steelseries.tools.Section TICKMARK_SECTION)
    {
        tickmarkSections.add(TICKMARK_SECTION);
        fireStateChanged();
    }

    /**
     * Clear the list of tickmark sections of the gauge
     */
    public void resetTickmarkSections()
    {
        tickmarkSections.clear();
        fireStateChanged();
    }

    /**
     * Returns true if the tickmark sections of the gauge are visible
     * @return true if the tickmark sections of the gauge are visible
     */
    public boolean isTickmarkSectionsVisible()
    {
        return tickmarkSectionsVisible;
    }

    /**
     * Enables / disables the visibility of the tickmark sections of the gauge
     * @param TICKMARK_SECTIONS_VISIBLE 
     */
    public void setTickmarkSectionsVisible(final boolean TICKMARK_SECTIONS_VISIBLE)
    {
        tickmarkSectionsVisible = TICKMARK_SECTIONS_VISIBLE;
        fireStateChanged();
    }

    /**
     * Returns the type of the radial gauge
     * TYPE1 : a quarter gauge (range 90°)
     * TYPE2 : a two quarter gauge (range 180°)
     * TYPE3 : a three quarter gauge (range 270°)
     * TYPE4 : a 300° gauge
     * TYPE5 : a quarter gauge that is rotated by 90°
     * @return the type of the radial gauge
     */
    public eu.hansolo.steelseries.tools.GaugeType getGaugeType()
    {
        return gaugeType;
    }

    /**
     * Sets the radial type of the gauge
     * @param GAUGE_TYPE 
     */
    public void setGaugeType(final eu.hansolo.steelseries.tools.GaugeType GAUGE_TYPE)
    {
        gaugeType = GAUGE_TYPE;
        calcAngleStep();
        fireStateChanged();
    }

    /**
     * Returns the range in rad where no tickmarks will be placed in a dial of a radial gauge
     * @return the range in rad where no tickmarks will be placed in a dial of a radial gauge
     */
    public double getFreeAreaAngle()
    {
        return gaugeType.FREE_AREA_ANGLE;
    }

    /**
     * Returns the stepsize in rad of the gauge dial
     * @return the stepsize in rad of the gauge dial
     */
    public double getAngleStep()
    {
        return angleStep;
    }

    /**
     * Returns the angle in rad that will be used to define the start position of the gauge pointer
     * @return the angle in rad that will be used to define the start position of the gauge pointer
     */
    public double getRotationOffset()
    {
        return gaugeType.ROTATION_OFFSET;
    }

    /**
     * Returns the angle in degree that will be used to define the start position of the gauge dial
     * @return the angle in degree that will be used to define the start position of the gauge dial
     */
    public double getTickmarkOffset()
    {
        return gaugeType.TICKMARK_OFFSET;
    }

    /**
     * Sets the minimum and maximum value of the gauge dial
     * @param MIN_VALUE 
     * @param MAX_VALUE 
     */
    public void setMinMaxValues(final double MIN_VALUE, final double MAX_VALUE)
    {
        this.minValue = MIN_VALUE;
        this.maxValue = MAX_VALUE;
        calculate();
    }

    /**
     * Sets the minimum and maximum values and the number of minor and major tickmarks of the gauge dial
     * @param MIN_VALUE
     * @param MAX_VALUE
     * @param NO_OF_MINOR_TICKS
     * @param NO_OF_MAJOR_TICKS 
     */
    public void setMinMaxAndNoOfTicks(final double MIN_VALUE, final double MAX_VALUE, final int NO_OF_MINOR_TICKS, final int NO_OF_MAJOR_TICKS)
    {
        this.maxNoOfMinorTicks = NO_OF_MINOR_TICKS;
        this.maxNoOfMajorTicks = NO_OF_MAJOR_TICKS;
        this.minValue = MIN_VALUE;
        this.maxValue = MAX_VALUE;
        calculate();
    }

    /**
     * Returns the maximum number of major tickmarks we're comfortable with
     * @return the maximum number of major tickmarks we're comfortable with
     */
    public int getMaxNoOfMajorTicks()
    {
        return this.maxNoOfMajorTicks;
    }

    /**
     * Sets the maximum number of major tickmarks we're comfortable with
     * @param MAX_NO_OF_MAJOR_TICKS the maximum number of major tickmarks for the axis
     */
    public void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS)
    {
        if (MAX_NO_OF_MAJOR_TICKS > 20)
        {
            this.maxNoOfMajorTicks = 20;
        }
        else if (MAX_NO_OF_MAJOR_TICKS < 2)
        {
            this.maxNoOfMajorTicks = 2;
        }
        else
        {
            this.maxNoOfMajorTicks = MAX_NO_OF_MAJOR_TICKS;
        }
        calculate();
        fireStateChanged();
    }

    /**
     * Returns the maximum number of minor tickmarks we're comfortable with
     * @return the maximum number of minor tickmarks we're comfortable with
     */
    public int getMaxNoOfMinorTicks()
    {
        return this.maxNoOfMinorTicks;
    }

    /**
     * Sets the maximum number of minor tickmarks we're comfortable with
     * @param MAX_NO_OF_MINOR_TICKS the maxmium number of minor tickmarks for the axis
     */
    public void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS)
    {
        if (MAX_NO_OF_MINOR_TICKS > 10)
        {
            this.maxNoOfMinorTicks = 10;
        }
        else if (MAX_NO_OF_MINOR_TICKS < 1)
        {
            this.maxNoOfMinorTicks = 1;
        }
        else
        {
            this.maxNoOfMinorTicks = MAX_NO_OF_MINOR_TICKS;
        }
        calculate();
        fireStateChanged();
    }

    /**
     * Returns the tick spacing between the major tickmarks
     * @return the tick spacing between the major tickmarks
     */
    public double getMajorTickSpacing()
    {
        return majorTickSpacing;
    }

    /**
     * Sets the major tickspacing if niceScale property is disabled
     * @param MAJOR_TICKSPACING 
     */
    public void setMajorTickSpacing(final double MAJOR_TICKSPACING)
    {
        if (!niceScale)
        {
            majorTickSpacing = MAJOR_TICKSPACING;
            calculate();
            validate();
            fireStateChanged();
        }
    }
    
    /**
     * Returns the tick spacing between the minor tickmarks
     * @return the tick spacing between the minor tickmarks
     */
    public double getMinorTickSpacing()
    {
        return minorTickSpacing;
    }
    
    /**
     * Sets the minor tickspacing if niceScale property is disabled
     * @param MINOR_TICKSPACING 
     */
    public void setMinorTickSpacing(final double MINOR_TICKSPACING)
    {
        if (!niceScale)
        {
            minorTickSpacing = MINOR_TICKSPACING;
            calculate();
            validate();
            fireStateChanged();
        }
    }
        
    /**
     * Returns the new minimum value for the gauge dial
     * @return the new minimum value for the gauge dial
     */
    public double getNiceMinValue()
    {
        return niceMinValue;
    }

    /**
     * Returns the new maximum value for the gauge dial
     * @return the new maximum value for the gauge dial
     */
    public double getNiceMaxValue()
    {
        return niceMaxValue;
    }

    /**
     * Returns the calculated range of the gauge dial
     * @return the calculated range of the gauge dial
     */
    public double getNiceRange()
    {
        return this.niceRange;
    }

    /**
     * Returns the background color of the gauge
     * @return the background color of the gauge
     */
    public eu.hansolo.steelseries.tools.BackgroundColor getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * Sets the background color of the gauge
     * @param BACKGROUND_COLOR 
     */
    public void setBackgroundColor(final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR)
    {
        backgroundColor = BACKGROUND_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the custom background paint of the gauge
     * @return the custom background paint of the gauge
     */
    public java.awt.Paint getCustomBackground()
    {
        return customBackground;
    }

    /**
     * Sets the custom background paint of the gauge
     * @param CUSTOM_BACKGROUND 
     */
    public void setCustomBackground(final java.awt.Paint CUSTOM_BACKGROUND)
    {
        customBackground = CUSTOM_BACKGROUND;
        fireStateChanged();
    }

    /**
     * Returns the buffered image that is used as a custom layer of the gauge
     * @return the buffered image that is used as a custom layer of the gauge
     */
    public java.awt.image.BufferedImage getCustomLayer()
    {
        return customLayer;
    }

    /**
     * Sets the given buffered image as the custom layer of the gauge
     * @param CUSTOM_LAYER 
     */
    public void setCustomLayer(final java.awt.image.BufferedImage CUSTOM_LAYER)
    {
        if (customLayer != null)
        {
            customLayer.flush();
        }
        customLayer = CUSTOM_LAYER;
        fireStateChanged();
    }

    /**
     * Returns the frame type of the gauge
     * @return the frame type of the gauge
     */
    public eu.hansolo.steelseries.tools.FrameType getFrameType()
    {
        return frameType;
    }

    /**
     * Sets the given frame type object as frame type of the gauge
     * ROUND
     * SQUARE
     * @param FRAME_TYPE 
     */
    public void setFrameType(final eu.hansolo.steelseries.tools.FrameType FRAME_TYPE)
    {
        frameType = FRAME_TYPE;
        fireStateChanged();
    }

    /**
     * Returns the current frame design of the gauge
     * @return the current frame design of the gauge
     */
    public eu.hansolo.steelseries.tools.FrameDesign getFrameDesign()
    {
        return frameDesign;
    }

    /**
     * Sets the given frame design as the custom frame design of the gauge
     * @param FRAME_DESIGN 
     */
    public void setFrameDesign(final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN)
    {
        frameDesign = FRAME_DESIGN;
        fireStateChanged();
    }

    /**
     * Returns the custom frame design paint object of the gauge
     * @return the cusotm frame design paint object of the gauge
     */
    public java.awt.Paint getCustomFrameDesign()
    {
        return customFrameDesign;
    }

    /**
     * Sets the given paint object as the current custom frame design of the gauge
     * @param CUSTOM_FRAME_DESIGN 
     */
    public void setCustomFrameDesign(final java.awt.Paint CUSTOM_FRAME_DESIGN)
    {
        customFrameDesign = CUSTOM_FRAME_DESIGN;
        fireStateChanged();
    }

    /**
     * Returns the led color of the gauge threshold led
     * @return the led color of the gauge threshold led
     */
    public eu.hansolo.steelseries.tools.LedColor getLedColor()
    {
        return ledColor;
    }

    /**
     * Sets the given led color as the color of the gauge threshold led
     * @param LED_COLOR 
     */
    public void setLedColor(final eu.hansolo.steelseries.tools.LedColor LED_COLOR)
    {
        ledColor = LED_COLOR;
        fireStateChanged();
    }
    
    /**
     * Returns the custom led color of the gauge threshold led
     * @return the custom led color of the gauge threshold led
     */
    public eu.hansolo.steelseries.tools.CustomLedColor getCustomLedColor()
    {
        return customLedColor;
    }

    /**
     * Sets the custom color of the gauge threshold led to the given led color
     * @param CUSTOM_LED_COLOR 
     */
    public void setCustomLedColor(final eu.hansolo.steelseries.tools.CustomLedColor CUSTOM_LED_COLOR)
    {
        customLedColor = CUSTOM_LED_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the lcd background color of the gauge (if lcd available)
     * @return the lcd background color of the gauge (if lcd available)
     */
    public eu.hansolo.steelseries.tools.LcdColor getLcdColor()
    {
        return lcdColor;
    }

    /**
     * Sets the lcd background color of the gauge to the given lcd color
     * @param LCD_COLOR 
     */
    public void setLcdColor(final eu.hansolo.steelseries.tools.LcdColor LCD_COLOR)
    {
        lcdColor = LCD_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the custom lcd background color of the gauge
     * @return the custom lcd background color of the gauge
     */
    public java.awt.Paint getCustomLcdBackground()
    {
        return customLcdColor;
    }

    /**
     * Sets the custom lcd background color of the gauge to the given lcd color
     * @param CUSTOM_LCD_COLOR 
     */
    public void setCustomLcdBackground(final java.awt.Paint CUSTOM_LCD_COLOR)
    {
        customLcdColor = CUSTOM_LCD_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the custom lcd foreground color of the gauge
     * @return the custom lcd foreground color of the gauge
     */
    public java.awt.Color getCustomLcdForeground()
    {
        return customLcdForegroundColor;
    }

    /**
     * Sets the custom lcd foreground color of the gauge to the given lcd foreground color
     * @param CUSTOM_LCD_FOREGROUND_COLOR 
     */
    public void setCustomLcdForeground(final java.awt.Color CUSTOM_LCD_FOREGROUND_COLOR)
    {
        customLcdForegroundColor = CUSTOM_LCD_FOREGROUND_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the type of the tickmark that will be used for the minor tickmarks in the gauge dial
     * @return the type of the tickmark that will be used for the minor tickmarks int he gauge dial
     */
    public eu.hansolo.steelseries.tools.TickmarkType getMinorTickmarkType()
    {
        return minorTickmarkType;
    }

    /**
     * Sets the type of tickmark that will be used for the minor tickmarks in the gauge dial to the given tickmarktype
     * @param MINOR_TICKMARK_TYPE 
     */
    public void setMinorTickmarkType(final eu.hansolo.steelseries.tools.TickmarkType MINOR_TICKMARK_TYPE)
    {
        minorTickmarkType = MINOR_TICKMARK_TYPE;
        fireStateChanged();
    }

    /**
     * Returns the type of tickmark that will be used for the major tickmarks in the gauge dial
     * @return the type of tickmark that will be used for the major tickmarks in the gauge dial
     */
    public eu.hansolo.steelseries.tools.TickmarkType getMajorTickmarkType()
    {
        return majorTickmarkType;
    }

    /**
     * Sets the type of tickmark that will be used for the major tickmarks in the gauge dial to the given tickmarktype
     * @param MAJOR_TICKMARK_TYPE 
     */
    public void setMajorTickmarkType(final eu.hansolo.steelseries.tools.TickmarkType MAJOR_TICKMARK_TYPE)
    {
        majorTickmarkType = MAJOR_TICKMARK_TYPE;
        fireStateChanged();
    }

    /**
     * Returns the numberformat that will be used to format the labels of the gauge dial
     * @return the numberformat that will be used to format the labels of the gauge dial
     */
    public eu.hansolo.steelseries.tools.NumberFormat getLabelNumberFormat()
    {
        return labelNumberFormat;
    }

    /**
     * Sets the number format that will be used to format the labels of the gauge dial
     * @param LABEL_NUMBERFORMAT 
     */
    public void setLabelNumberFormat(final eu.hansolo.steelseries.tools.NumberFormat LABEL_NUMBERFORMAT)
    {
        labelNumberFormat = LABEL_NUMBERFORMAT;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used to draw the tickmarks in the gauge dial
     * @return the color that will be used to draw the tickmarks in the gauge dial
     */
    public java.awt.Color getTickmarkColor()
    {
        return tickmarkColor;
    }

    /**
     * Sets the color of the tickmarks in the gauge dial to the given color
     * @param TICKMARK_COLOR 
     */
    public void setTickmarkColor(final java.awt.Color TICKMARK_COLOR)
    {
        tickmarkColor = TICKMARK_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used as custom tickmark color in the gauge dial
     * @return the color that will be used as custom tickmark color in the gauge dial
     */
    public java.awt.Color getCustomTickmarkColor()
    {
        return customTickmarkColor;
    }

    /**
     * Sets the custom tickmark color of the gauge to the given value
     * @param CUSTOM_TICKMARK_COLOR 
     */
    public void setCustomTickmarkColor(final java.awt.Color CUSTOM_TICKMARK_COLOR)
    {
        customTickmarkColor = CUSTOM_TICKMARK_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used to draw the labels of the gauge dial
     * @return the color that will be used to draw the labels of the gauge dial
     */
    public java.awt.Color getLabelColor()
    {
        return labelColor;
    }

    /**
     * Sets the color that will be used to draw the labels of the gauge dial to the given color
     * @param LABEL_COLOR 
     */
    public void setLabelColor(final java.awt.Color LABEL_COLOR)
    {
        labelColor = LABEL_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used as start color of the track gradient
     * @return the color that will be used as start color of the track gradient
     */
    public java.awt.Color getTrackStartColor()
    {
        return trackStartColor;
    }

    /**
     * Sets the color that will be used as start color of the track gradient to the given color
     * @param TRACK_START_COLOR 
     */
    public void setTrackStartColor(final java.awt.Color TRACK_START_COLOR)
    {
        trackStartColor = TRACK_START_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used as intermediate color of the track gradient
     * @return the color that will be used as intermediate color of the track gradient
     */
    public java.awt.Color getTrackSectionColor()
    {
        return trackSectionColor;
    }

    /**
     * Sets the color that will be used as intermediate color of the track gradient to the given color
     * @param TRACK_SECTION_COLOR 
     */
    public void setTrackSectionColor(final java.awt.Color TRACK_SECTION_COLOR)
    {
        trackSectionColor = TRACK_SECTION_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used as stop color of the track gradient
     * @return the color that will be used as stop color of the track gradient
     */
    public java.awt.Color getTrackStopColor()
    {
        return trackStopColor;
    }

    /**
     * Sets the color that will be used as stop color of the track gradient to the given color
     * @param TRACK_STOP_COLOR 
     */
    public void setTrackStopColor(final java.awt.Color TRACK_STOP_COLOR)
    {
        trackStopColor = TRACK_STOP_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the type of pointer that will be used by the radial gauge
     * @return the type of pointer that will be used by the radial gauge
     */
    public eu.hansolo.steelseries.tools.PointerType getPointerType()
    {
        return pointerType;
    }

    /**
     * Sets the type of pointer that will be used by the radial gauge to the given type
     * @param POINTER_TYPE 
     */
    public void setPointerType(final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        pointerType = POINTER_TYPE;
        fireStateChanged();
    }

    /**
     * Returns the color of the pointer of the radial gauge
     * @return the color of the pointer of the radial gauge
     */
    public eu.hansolo.steelseries.tools.ColorDef getPointerColor()
    {
        return pointerColor;
    }

    /**
     * Sets the color of the pointer of the radial gauge to the given color
     * @param POINTER_COLOR 
     */
    public void setPointerColor(final eu.hansolo.steelseries.tools.ColorDef POINTER_COLOR)
    {
        pointerColor = POINTER_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the custom color object of the pointer of the radial gauge
     * @return the custom color object of the pointer of the radial gauge
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomPointerColorObject()
    {
        return customPointerColorObject;
    }

    /**
     * Sets the custom color object of the pointer of the radial gauge to the given color object
     * @param CUSTOM_POINTER_COLOR_OBJECT 
     */
    public void setCustomPointerColorObject(
        final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_POINTER_COLOR_OBJECT)
    {
        customPointerColorObject = CUSTOM_POINTER_COLOR_OBJECT;
        fireStateChanged();
    }

    /**
     * Returns the custom color of the pointer of the radial gauge
     * @return the custom color of the pointer of the radial gauge
     */
    public java.awt.Color getCustomPointerColor()
    {
        return customPointerColorObject.COLOR;
    }

    /**
     * Returns the color definition that will be used to visualize the color of the value
     * @return the color definition that will be used to visualize the color of the value
     */
    public eu.hansolo.steelseries.tools.ColorDef getValueColor()
    {
        return valueColor;
    }

    /**
     * Sets the color definition that will be used to visualize the color of the value
     * @param VALUE_COLOR 
     */
    public void setValueColor(final eu.hansolo.steelseries.tools.ColorDef VALUE_COLOR)
    {
        valueColor = VALUE_COLOR;
        fireStateChanged();
    }

    /**
     * Returns the color definition that will be used to visualize the custom color of the value
     * @return the color definition that will be used to visualize the custom color of the value
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomValueColorObject()
    {
        return customValueColorObject;
    }

    /**
     * Sets the color definition that will be used to visualize the custom color of the value
     * @param CUSTOM_VALUE_COLOR_OBJECT 
     */
    public void setCustomValueColorObject(final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_VALUE_COLOR_OBJECT)
    {
        customValueColorObject = CUSTOM_VALUE_COLOR_OBJECT;
        fireStateChanged();
    }

    /**
     * Returns the color that will be used to visualize the value
     * @return the color that will be used to visualize the value
     */
    public java.awt.Color getCustomValueColor()
    {
        return customValueColorObject.COLOR;
    }

    /**
     * Returns the type of knob that will be used as center knob in a radial gauge
     * @return the type of knob that will be used as center knob in a radial gauge
     */
    public eu.hansolo.steelseries.tools.KnobType getKnobType()
    {
        return knobType;
    }

    /**
     * Sets the type of knob that will be used as center knob in a radial gauge
     * @param KNOB_TYPE 
     */
    public void setKnobType(final eu.hansolo.steelseries.tools.KnobType KNOB_TYPE)
    {
        knobType = KNOB_TYPE;
        fireStateChanged();
    }

    /**
     * Returns the style of the center knob of a radial gauge
     * @return the style of the center knob of a radial gauge
     */
    public eu.hansolo.steelseries.tools.KnobStyle getKnobStyle()
    {
        return knobStyle;
    }
    
    /**
     * Sets the style of the center knob of a radial gauge
     * @param KNOB_STYLE 
     */
    public void setKnobStyle(final eu.hansolo.steelseries.tools.KnobStyle KNOB_STYLE)
    {
        knobStyle = KNOB_STYLE;
        fireStateChanged();
    }
    
    /**
     * Returns the type of foreground that will be used to visualize the highlight effect in a radial gauge
     * @return the type of foreground that will be used to visualize the highlight effect in a radial gauge
     */
    public eu.hansolo.steelseries.tools.ForegroundType getForegroundType()
    {
        return foregroundType;
    }

    /**
     * Sets the type of foreground that will be used to visualize the highlight effect in a radial gauge to the given type
     * @param FOREGROUND_TYPE 
     */
    public void setForegroundType(final eu.hansolo.steelseries.tools.ForegroundType FOREGROUND_TYPE)
    {
        foregroundType = FOREGROUND_TYPE;
        fireStateChanged();
    }

    /**
     * Returns the font that will be used to visualize values in the lcd display
     * @return the font that will be used to visualize values in the lcd display
     */
    public java.awt.Font getStandardBaseFont()
    {
        return STANDARD_BASE_FONT;
    }

    /**
     * Returns the font that will be used to visualize infos in the lcd display
     * @return the font that will be used to visualize infos in the lcd display
     */
    public java.awt.Font getStandardInfoFont()
    {
        return STANDARD_INFO_FONT;
    }
    
    /**
     * Returns the font that will be used as digital font in the lcd display
     * @return the font that will be used as digital font in the lcd display
     */
    public java.awt.Font getDigitalBaseFont()
    {
        return DIGITAL_BASE_FONT;
    }

    /**
     * Returns the font that will be used to visualize the value on the lcd display
     * @return the font that will be used to visualize the value on the lcd display
     */
    public java.awt.Font getLcdValueFont()
    {
        return lcdValueFont;
    }
    
    /**
     * Sets the font that will be used to visualize the value on the lcd display to the given font
     * @param LCD_VALUE_FONT 
     */
    public void setLcdValueFont(final java.awt.Font LCD_VALUE_FONT)
    {
        lcdValueFont = LCD_VALUE_FONT;
        fireStateChanged();
    }
    
    /**
     * Returns the font that will be used to visualize the unit on the lcd display
     * @return the font that will be used to visualize the unit on the lcd display
     */
    public java.awt.Font getLcdUnitFont()
    {
        return lcdUnitFont;
    }
    
    /**
     * Sets the font that will be used to visualize the unit on the lcd display to the given font
     * @param LCD_UNIT_FONT 
     */
    public void setLcdUnitFont(final java.awt.Font LCD_UNIT_FONT)
    {
        lcdUnitFont = LCD_UNIT_FONT;
        fireStateChanged();
    }
    
    /**
     * Returns the custom font that will be used to visualize the unit in the lcd display
     * @return the custom font that will be used to visualize the unit in the lcd display
     */
    public java.awt.Font getCustomLcdUnitFont()
    {
        return customLcdUnitFont;
    }

    /**
     * Sets the custom font that will be used to visualize the unit in the lcd display to the given font
     * @param CUSTOM_LCD_UNIT_FONT 
     */
    public void setCustomLcdUnitFont(final java.awt.Font CUSTOM_LCD_UNIT_FONT)
    {
        customLcdUnitFont = CUSTOM_LCD_UNIT_FONT;
        fireStateChanged();
    }

    /**
     * Returns the font that will be used to visualize the info on the lcd display
     * @return the font that will be used to visualize the info on the lcd display
     */
    public java.awt.Font getLcdInfoFont()
    {
        return lcdInfoFont;
    }
    
    /**
     * Sets the font that will be used to visualize the info on the lcd display
     * @param LCD_INFO_FONT 
     */
    public void setLcdInfoFont(final java.awt.Font LCD_INFO_FONT)
    {
        lcdInfoFont = LCD_INFO_FONT;
        fireStateChanged();
    }
    
    /**
     * Returns the number of decimals that will be used to visualize values in the lcd display
     * @return the number of decimals that will be used to visualize values in the lcd display
     */
    public int getLcdDecimals()
    {
        return lcdDecimals;
    }

    /**
     * Sets the number of decimals that will be used to visualize values in the lcd display to the given value
     * @param LCD_DECIMALS 
     */
    public void setLcdDecimals(final int LCD_DECIMALS)
    {
        lcdDecimals = LCD_DECIMALS;
        fireStateChanged();
    }
    
    /**
     * Returns true if the calculation method will try to calculate
     * nice values for min and max values of the scale.
     * @return true if the calculation method will try to calculate nice values for min and max of the scale
     */
    public boolean isNiceScale()
    {
        return niceScale;
    }
    
    /**
     * Enables / disables the calculation of nice values for min and max of the scale
     * @param NICE_SCALE 
     */
    public void setNiceScale(final boolean NICE_SCALE)
    {
        niceScale = NICE_SCALE;
        if (!niceScale)
        {
            minorTickSpacing = 1;
            majorTickSpacing = 10;
        }
        calculate();
        validate();
        calcAngleStep();
        fireStateChanged();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Model related">   
    /**
     * Validates many important values and adjust them if they do not fit.
     * e.g. If the threshold is higher than the maximum visible value it
     * will set the threshold to the maximum value.
     */
    public void validate()
    {
        // AutoResetToZero
        if (niceMinValue > 0 || niceMaxValue < 0)
        {
            autoResetToZero = false;
        }

        // Threshold
        if (threshold < niceMinValue || threshold > niceMaxValue)
        {
            threshold = niceMaxValue;
        }

        // MinMeasuredValue
        if (minMeasuredValue < niceMinValue || minMeasuredValue > niceMaxValue)
        {
            minMeasuredValue = value;
        }

        // MaxMeasuredValue
        if (maxMeasuredValue > niceMaxValue || maxMeasuredValue < niceMinValue)
        {
            maxMeasuredValue = value;
        }

        // PeakValue
        if (peakValue < niceMinValue || peakValue > niceMaxValue)
        {
            peakValue = value;
        }

        // TrackStart
        if (Double.compare(trackStart, niceMinValue) <= 0 || Double.compare(trackStart, niceMaxValue) >= 0 || Double.compare(trackStart, trackStop) >= 0)
        {
            trackStart = niceMinValue;
        }

        // TrackStop
        if ((Double.compare(trackStop, niceMinValue)) <= 0 || Double.compare(trackStop, niceMaxValue) >= 0 || Double.compare(trackStop, trackStart) <= 0)
        {
            trackStop = niceMaxValue;
        }

        // TrackSection        
        if (Double.compare(trackSection, niceMinValue) <= 0 || Double.compare(trackSection, niceMaxValue) >= 0 || Double.compare(trackSection, trackStart) <= 0 || Double.compare(trackSection, trackStop) >= 0)
        {
            trackSection = ((trackStart + (trackStop - trackStart) / 2.0));
        }

        // Areas
        for (eu.hansolo.steelseries.tools.Section area : areas)
        {
            if ((area.getStart() < niceMinValue) || Double.compare(area.getStart(), niceMaxValue) >= 0 || Double.compare(area.getStart(), area.getStop()) >= 0)
            {
                area.setStart(niceMinValue);
            }

            if (area.getStop() < niceMinValue || area.getStop() > niceMaxValue || Double.compare(area.getStop(), area.getStart()) <= 0)
            {
                area.setStop(niceMaxValue);
            }

            if (Double.compare(area.getStart(), minValue) == 0)
            {
                area.setStart(niceMinValue);
            }

            if (Double.compare(area.getStop(), maxValue) == 0)
            {
                area.setStop(niceMaxValue);
            }
        }

        // Sections
        for (eu.hansolo.steelseries.tools.Section section : sections)
        {
            if ((section.getStart() < niceMinValue) || Double.compare(section.getStart(), niceMaxValue) >= 0 || Double.compare(section.getStart(), section.getStop()) >= 0)
            {
                section.setStart(niceMinValue);
            }

            if (section.getStop() < niceMinValue || section.getStop() > niceMaxValue || Double.compare(section.getStop(), section.getStart()) <= 0)
            {
                section.setStop(niceMaxValue);
            }

            if (Double.compare(section.getStart(), minValue) == 0)
            {
                section.setStart(niceMinValue);
            }

            if (Double.compare(section.getStop(), maxValue) == 0)
            {
                section.setStop(niceMaxValue);
            }
        }

        // TickmarkSections
        for (eu.hansolo.steelseries.tools.Section tickmarkSection : tickmarkSections)
        {
            if ((tickmarkSection.getStart() < niceMinValue) || Double.compare(tickmarkSection.getStart(), niceMaxValue) >= 0 || Double.compare(tickmarkSection.getStart(), tickmarkSection.getStop()) >= 0)
            {
                tickmarkSection.setStart(niceMinValue);
            }

            if (tickmarkSection.getStop() < niceMinValue || tickmarkSection.getStop() > niceMaxValue || Double.compare(tickmarkSection.getStop(), tickmarkSection.getStart()) <= 0)
            {
                tickmarkSection.setStop(niceMaxValue);
            }

            if (Double.compare(tickmarkSection.getStart(), minValue) == 0)
            {
                tickmarkSection.setStart(niceMinValue);
            }

            if (Double.compare(tickmarkSection.getStop(), maxValue) == 0)
            {
                tickmarkSection.setStop(niceMaxValue);
            }
        }

        // Correct the value (needed for init with negative minValue)        
        value = value < niceMinValue ? niceMinValue : (value > niceMaxValue ? niceMaxValue : value);        
    }

    /**
     * Calculates the stepsize in rad for the given gaugetype and range
     */
    private void calcAngleStep()
    {
        angleStep = gaugeType.ANGLE_RANGE / range;
    }

    /**
     * Calculate and update values for majro and minor tick spacing and nice
     * minimum and maximum values on the axis.
     */
    private void calculate()
    {
        if (niceScale)
        {
            this.niceRange = calcNiceNumber(maxValue - minValue, false);
            this.majorTickSpacing = calcNiceNumber(niceRange / (maxNoOfMajorTicks - 1), true);
            this.niceMinValue = Math.floor(minValue / majorTickSpacing) * majorTickSpacing;
            this.niceMaxValue = Math.ceil(maxValue / majorTickSpacing) * majorTickSpacing;
            this.minorTickSpacing = calcNiceNumber(majorTickSpacing / (maxNoOfMinorTicks - 1), true);
            this.range = niceMaxValue - niceMinValue;
        }
        else
        {
            this.niceRange = (maxValue - minValue);
            this.niceMinValue = minValue;
            this.niceMaxValue = maxValue;
            this.range = this.niceRange;
        }
    }

    /**
     * Returns a "nice" number approximately equal to the range. 
     * Rounds the number if ROUND == true.
     * Takes the ceiling if ROUND = false.    
     * @param RANGE the value range (maxValue - minValue)
     * @param ROUND whether to round the result or ceil
     * @return a "nice" number to be used for the value range
     */
    private double calcNiceNumber(final double RANGE, final boolean ROUND)
    {        
        final double EXPONENT = Math.floor(Math.log10(RANGE));   // exponent of range
        final double FRACTION = RANGE / Math.pow(10, EXPONENT);  // fractional part of range

        // nice, rounded fraction
        final double NICE_FRACTION;
        
        if (ROUND)
        {
            if (FRACTION < 1.5)
            {
                NICE_FRACTION = 1;
            }
            else if (FRACTION < 3)
            {
                NICE_FRACTION = 2;
            }
            else if (FRACTION < 7)
            {
                NICE_FRACTION = 5;
            }
            else
            {
                NICE_FRACTION = 10;
            }
        }
        else
        {
            if (FRACTION <= 1)
            {
                NICE_FRACTION = 1;
            }
            else if (FRACTION <= 2)
            {
                NICE_FRACTION = 2;
            }
            else if (FRACTION <= 5)
            {
                NICE_FRACTION = 5;
            }
            else
            {
                NICE_FRACTION = 10;
            }
        }

        return NICE_FRACTION * Math.pow(10, EXPONENT);
    }

    private void createRadialRangeOfMeasureValuesArea()
    {
        if (bounds.width > 1 && bounds.height > 1 && Double.compare(getMinMeasuredValue(), getMaxMeasuredValue()) != 0)
        {
            final double ANGLE_STEP = Math.toDegrees(getGaugeType().ANGLE_RANGE) / (getMaxValue() - getMinValue());
            //final double RADIUS = bounds.width * 0.38f - bounds.height * 0.04f;
            final double RADIUS = bounds.width * 0.35f - bounds.height * 0.04f;
            final double FREE_AREA = bounds.width / 2.0 - RADIUS;            
            ((java.awt.geom.Arc2D) radialRangeOfMeasuredValues).setFrame(new java.awt.geom.Rectangle2D.Double(bounds.x + FREE_AREA, bounds.y + FREE_AREA, 2 * RADIUS, 2 * RADIUS));
            ((java.awt.geom.Arc2D) radialRangeOfMeasuredValues).setAngleStart(getGaugeType().ORIGIN_CORRECTION - (getMinMeasuredValue() * ANGLE_STEP) + (getMinValue() * ANGLE_STEP));
            ((java.awt.geom.Arc2D) radialRangeOfMeasuredValues).setAngleExtent(-(getMaxMeasuredValue() - getMinMeasuredValue()) * ANGLE_STEP);
            ((java.awt.geom.Arc2D) radialRangeOfMeasuredValues).setArcType(java.awt.geom.Arc2D.PIE);
        }
    }

    /**
     * Resets the model by calling the init() method
     */
    public void reset()
    {
        init();
    }

    /**
     * Returns a shallow copy of the gauge model
     * @return a shallow copy of the gauge model
     */
    @Override
    public Model clone()
    {
        try
        {
            return (Model) super.clone();
        }
        catch (java.lang.CloneNotSupportedException exception)
        {
        }
        return new Model();
    }    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Listener related">
    /**
     * Adds the given listener to the listener list
     * @param LISTENER 
     */
    public void addChangeListener(javax.swing.event.ChangeListener LISTENER)
    {
        LISTENER_LIST.add(javax.swing.event.ChangeListener.class, LISTENER);
    }

    /**
     * Removes all listeners from the listener list
     * @param LISTENER
     */
    public void removeChangeListener(javax.swing.event.ChangeListener LISTENER)
    {
        LISTENER_LIST.remove(javax.swing.event.ChangeListener.class, LISTENER);
    }

    /**
     * Fires an state change event every time the data model changes
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

    @Override
    public String toString()
    {
        return "Model";
    }
}
