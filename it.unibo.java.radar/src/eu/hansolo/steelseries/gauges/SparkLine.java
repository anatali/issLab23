package eu.hansolo.steelseries.gauges;

/**
 *
 * @author hansolo
 */
public class SparkLine extends javax.swing.JComponent
{   
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">
    private static final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    private static final java.text.DecimalFormat DF = new java.text.DecimalFormat("0.00");
    private final java.awt.Rectangle INNER_BOUNDS = new java.awt.Rectangle(0, 0, 128, 48);    
    private boolean recreateImages;
    private final java.util.LinkedList<eu.hansolo.steelseries.tools.DataPoint> DATA_LIST;  
    private java.util.List<Double> sortedList = new java.util.ArrayList<Double>(3600);
    private java.util.List<eu.hansolo.steelseries.tools.DataPoint> trashList;
    private java.util.List<java.awt.geom.Point2D> pointList;    
    private final java.awt.Color DISABLED_COLOR;
    private double start;
    private double stop;
    private double lo;
    private double hi;        
    private int loIndex;
    private int hiIndex;        
    private double offset;    
    private double scaleY;
    private double rangeY;
    private boolean filled;
    private eu.hansolo.steelseries.tools.SmoothingFunction smoothFunction;
    private boolean smoothing;
    private float lineWidth;
    private eu.hansolo.steelseries.tools.LcdColor sparkLineColor;
    private java.awt.Paint customSparkLineColor;
    private java.awt.Color lineColor;
    private eu.hansolo.steelseries.tools.ColorDef areaFill;        
    private java.awt.Color customAreaFillTop;
    private java.awt.Color customAreaFillBottom;
    private boolean lineShadowVisible;
    private boolean startStopIndicatorVisible;
    private boolean hiLoIndicatorVisible;
    private boolean backgroundVisible;
    private boolean infoLabelsVisible;
    private final java.awt.Font INFO_LABEL_FONT;    
    private java.awt.image.BufferedImage sparkLineBackgroundImage;
    private java.awt.image.BufferedImage startIndicatorImage;
    private java.awt.image.BufferedImage stopIndicatorImage;
    private java.awt.image.BufferedImage loIndicatorImage;
    private java.awt.image.BufferedImage hiIndicatorImage;
    private java.awt.image.BufferedImage sparkLineImage;
    private java.awt.Shape disabledShape;  
    private final java.awt.geom.RoundRectangle2D CLIP_SHAPE;
    private long timeFrame;
    private double pixelResolution;   
    private double baseLineY;    
    private boolean baseLineVisible;
    private boolean averageVisible;
    private boolean normalAreaVisible;
    private final java.awt.geom.Line2D AVERAGE_LINE;
    private final java.awt.geom.Rectangle2D NORMAL_AREA;
    private java.awt.Color averageColor;
    private java.awt.Color normalAreaColor;
    private final transient java.awt.event.ComponentListener COMPONENT_LISTENER = new java.awt.event.ComponentAdapter() 
    {
        @Override
        public void componentResized(java.awt.event.ComponentEvent event)
        {
            java.awt.Container parent = getParent();
        
            if (getWidth() < getMinimumSize().width && getHeight() < getMinimumSize().height)
            {
                if (parent != null && getParent().getLayout() == null)
                {
                    setSize(getMinimumSize());                
                }
                else
                {
                    setPreferredSize(getMinimumSize());
                }            
            }

            if (parent != null && getParent().getLayout() == null)
            {
                setSize(getWidth(), getHeight());
            }
            else
            {
                setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
            }

            calcInnerBounds();
            recreateAllImages();
            init(INNER_BOUNDS.width, INNER_BOUNDS.height);    
            revalidate();   
            repaint();
        }
    };
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public SparkLine()
    {        
        super();
        addComponentListener(COMPONENT_LISTENER);        
        recreateImages = true;
        DATA_LIST = new java.util.LinkedList<eu.hansolo.steelseries.tools.DataPoint>();
        trashList = new java.util.ArrayList<eu.hansolo.steelseries.tools.DataPoint>(512);
        pointList = new java.util.ArrayList<java.awt.geom.Point2D>(INNER_BOUNDS.width);        
        DISABLED_COLOR = new java.awt.Color(102, 102, 102, 178);        
        scaleY = 1.0;
        rangeY = 0;
        filled = false;
        smoothFunction = eu.hansolo.steelseries.tools.SmoothingFunction.COSINUS;
        smoothing = true;
        lineWidth = 1.0f;
        sparkLineColor = eu.hansolo.steelseries.tools.LcdColor.WHITE_LCD;
        customSparkLineColor = java.awt.Color.WHITE;
        lineColor = sparkLineColor.TEXT_COLOR;
        areaFill = eu.hansolo.steelseries.tools.ColorDef.RED;         
        customAreaFillTop = new java.awt.Color(1.0f, 1.0f, 1.0f, 0.4f);
        customAreaFillBottom = new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f);
        lineShadowVisible = false;
        startStopIndicatorVisible = true;
        hiLoIndicatorVisible = false;
        backgroundVisible = true;
        infoLabelsVisible = false;
        INFO_LABEL_FONT = new java.awt.Font("Verdana", 0, 12);
        CLIP_SHAPE = new java.awt.geom.RoundRectangle2D.Double();
        timeFrame = 3600000; // Default to 1 hour == 3600 sec => 3600000 ms  
        baseLineVisible = false;
        averageVisible = false;
        normalAreaVisible = false;
        AVERAGE_LINE = new java.awt.geom.Line2D.Double(INNER_BOUNDS.x, 0, INNER_BOUNDS.x + INNER_BOUNDS.width, 0);
        NORMAL_AREA = new java.awt.geom.Rectangle2D.Double(INNER_BOUNDS.x, 0, INNER_BOUNDS.width, 0);
        averageColor = new java.awt.Color(102, 216, 29);
        normalAreaColor = new java.awt.Color(216, 29, 68, 25);        
        createInitialImages();
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);        
        repaint(INNER_BOUNDS);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Initialization">
    /**
     * Initializes the sparkline component with the given width and height
     * @param WIDTH
     * @param HEIGHT 
     */
    private void init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return;
        }
        
        if (recreateImages)
        {                            
            if (sparkLineBackgroundImage != null)
            {
                sparkLineBackgroundImage.flush();
            }
            sparkLineBackgroundImage = create_SPARK_LINE_BACKGROUND_Image(WIDTH, HEIGHT);

            // set the clip shape
            final double CORNER_RADIUS = WIDTH * 0.09375 - 1;
            CLIP_SHAPE.setRoundRect(1, 1, WIDTH - 2, HEIGHT - 2, CORNER_RADIUS, CORNER_RADIUS);

            if (startIndicatorImage != null)
            {
                startIndicatorImage.flush();
            }
            //startIndicatorImage = createIndicatorImage(WIDTH, HEIGHT, eu.hansolo.steelseries.tools.ColorDef.GRAY);
            startIndicatorImage = create_START_STOP_INDICATOR_Image(WIDTH);

            if (stopIndicatorImage != null)
            {
                stopIndicatorImage.flush();
            }
            //stopIndicatorImage = createIndicatorImage(WIDTH, HEIGHT, eu.hansolo.steelseries.tools.ColorDef.GRAY);
            stopIndicatorImage = create_START_STOP_INDICATOR_Image(WIDTH);

            if (loIndicatorImage != null)
            {
                loIndicatorImage.flush();
            }        
            //loIndicatorImage = createIndicatorImage(WIDTH, HEIGHT, eu.hansolo.steelseries.tools.ColorDef.BLUE);
            loIndicatorImage = create_LO_INDICATOR_Image(WIDTH);                    

            if (hiIndicatorImage != null)
            {
                hiIndicatorImage.flush();
            }
            //hiIndicatorImage = createIndicatorImage(WIDTH, HEIGHT, eu.hansolo.steelseries.tools.ColorDef.RED);
            hiIndicatorImage = create_HI_INDICATOR_Image(WIDTH);                                
        }                
        recreateImages = false;
        
        disabledShape = new java.awt.geom.RoundRectangle2D.Double(0, 0, WIDTH, HEIGHT, WIDTH * 0.09375, WIDTH * 0.09375);
        
        // Calculation and creation of sparkline itself
        pixelResolution = INNER_BOUNDS.getWidth() / (double)timeFrame;        
        //offset = (int)(0.015 * WIDTH) < 4 ? 4 : (int)(0.015 * WIDTH);
        offset = (int)(0.06 * WIDTH) < 8 ? 8 : (int)(0.06 * WIDTH);
        
        baseLineY = INNER_BOUNDS.y + INNER_BOUNDS.height - ((0 - lo) * (1 / scaleY) + offset);
        
        if (!DATA_LIST.isEmpty())
        {
            calculate(WIDTH, HEIGHT);
        }
                
        if (sparkLineImage != null)
        {
            sparkLineImage.flush();
        }
        sparkLineImage = createSparkLineImage(WIDTH, HEIGHT);                
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Visualization">
    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
       
        G2.translate(INNER_BOUNDS.x, INNER_BOUNDS.y);
                                       
        if (backgroundVisible)
        {
            // Draw SparkLineBackground
            G2.drawImage(sparkLineBackgroundImage, 0, 0, null);
                
            // Set the clip
            G2.setClip(CLIP_SHAPE); 
        }
            
        // Draw info label text
        if (infoLabelsVisible && INNER_BOUNDS.height > 40)
        {
            G2.setColor(sparkLineColor.TEXT_COLOR);
            G2.setFont(INFO_LABEL_FONT.deriveFont(0.12f * INNER_BOUNDS.height));
            G2.drawString("hi: " + DF.format(hi), (int) (INNER_BOUNDS.width * 0.0277777778), 2 + G2.getFont().getSize());
            G2.drawString("lo: " + DF.format(lo), (int) (INNER_BOUNDS.width * 0.0277777778), (INNER_BOUNDS.height - 4));            
        }
        
        // Draw the sparkline
        G2.drawImage(sparkLineImage, 0, 0, null);
                
        // Draw baseline
        if (baseLineVisible)
        {
            G2.setColor(java.awt.Color.BLACK);        
            G2.drawLine(INNER_BOUNDS.x, (int)baseLineY, INNER_BOUNDS.x + INNER_BOUNDS.width, (int)baseLineY);
        }
        
        // Draw normal area
        if (normalAreaVisible)
        {
            G2.setColor(normalAreaColor);
            G2.fill(NORMAL_AREA);
        }
        
        // Draw average
        if (averageVisible)
        {
            G2.setColor(averageColor);
            G2.draw(AVERAGE_LINE);
        }
        
        // Draw disabled image if needed
        if (!isEnabled())
        {
            G2.setColor(DISABLED_COLOR);
            G2.fill(disabledShape);
        }
        
        G2.translate(-INNER_BOUNDS.x, -INNER_BOUNDS.y);
        
        G2.dispose();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
     /**
     * Adds a new value to the DATA_LIST of the sparkline
     * @param DATA 
     */
    public void addDataPoint(final double DATA)
    {       
        for (eu.hansolo.steelseries.tools.DataPoint dataPoint : DATA_LIST)
        {
            if (System.currentTimeMillis() - dataPoint.getTimeStamp() > timeFrame)
            {
                trashList.add(dataPoint);
            }
        }
        
        for (eu.hansolo.steelseries.tools.DataPoint dataPoint : trashList)
        {
            DATA_LIST.remove(dataPoint);
        }       
        trashList.clear();        
        DATA_LIST.add(new eu.hansolo.steelseries.tools.DataPoint(System.currentTimeMillis(), DATA));
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the linked list that contains the current data of the sparkline
     * @return the linked list that contains the current data of the sparkline
     */
    public java.util.List<eu.hansolo.steelseries.tools.DataPoint> getDataList()
    {
        java.util.List<eu.hansolo.steelseries.tools.DataPoint> dataListCopy = new java.util.LinkedList<eu.hansolo.steelseries.tools.DataPoint>();
        dataListCopy.addAll(DATA_LIST);                
        return dataListCopy;        
    }
    
    /**
     * Returns the first entry in the sparkline DATA_LIST
     * @return the first entry in the sparkline DATA_LIST
     */
    public double getStart()
    {
        return this.start;
    }

    /**
     * Returns the first last in the sparkline DATA_LIST
     * @return the last entry in the sparkline DATA_LIST
     */
    public double getStop()
    {
        return this.stop;
    }

    /**
     * Returns the entry with the lowest value in the sparkline DATA_LIST
     * @return the entry with the lowest value in the sparkline DATA_LIST
     */
    public double getLo()
    {
        return this.lo;
    }

    /**
     * Returns the entry with the highest value in the sparkline DATA_LIST
     * @return the entry with the highest value in the sparkline DATA_LIST
     */
    public double getHi()
    {
        return this.hi;
    }
                         
    /**
     * Returns the calculated varianz of the current data
     * @return the calculated varianz of the current data
     */
    public double getVariance()
    {
        if (!DATA_LIST.isEmpty())
        {
            double sum = 0;
            double sumOfSquares = 0;
            double average = 0;
            for (eu.hansolo.steelseries.tools.DataPoint dataPoint : DATA_LIST)
            {
                sumOfSquares += (dataPoint.getValue() * dataPoint.getValue());
                sum += dataPoint.getValue();
            }
            average = sum / DATA_LIST.size();
            return (sumOfSquares / DATA_LIST.size()) - average * average;
        }
        return 0;
    }
    
    /**
     * Returns the calculated average of the current data
     * @return the calculated average of the current data
     */
    public double getAverage()
    {
        if (!DATA_LIST.isEmpty())
        {
            double sum = 0;
            for (eu.hansolo.steelseries.tools.DataPoint dataPoint : DATA_LIST)
            {
                sum += dataPoint.getValue();
            }
            return sum / DATA_LIST.size();
        }
        return 0;
    }        
    
    /**
     * Returns the calculated standard deviation of the current data
     * @return the calculated standard deviation of the current data
     */
    public double getStandardDeviation()
    {
        return Math.sqrt(getVariance());
    }
    
    /**
     * Returns the range of values which means hi - low
     * @return the range of values which means hi - low
     */
    public double getRange()
    {
        return this.rangeY;
    }
    
    public double getQ1()
    {
        if (DATA_LIST.size() > 2)
        {
            sortData();
            int stopIndex;    
            if (sortedList.size() % 2 != 0)
            {
                stopIndex = sortedList.size() / 2;                                
            }
            else
            {
                stopIndex = sortedList.size() / 2 - 1;                
            }  
            return (sortedList.subList(0, stopIndex)).get(((sortedList.subList(0, stopIndex)).size() / 2));
        }
        return 0;
    }
    
    public double getQ2()
    {
        return getMedian();
    }
    
    public double getQ3()
    {
        if (DATA_LIST.size() > 2)
        {
            sortData();            
            int startIndex = sortedList.size() / 2;
            return (sortedList.subList(startIndex, sortedList.size() - 1)).get(((sortedList.subList(startIndex, sortedList.size() - 1)).size() / 2));            
        }
        return 0;
    }
    
    /**
     * Returns the median of the measured values
     * @return the median of the measured values
     */
    public double getMedian()
    {
        if (DATA_LIST.size() > 2)
        {
            sortData();
        
            if (sortedList.size() % 2 != 0)
            {
                return sortedList.get((sortedList.size() / 2));
            }
            else
            {
                return (sortedList.get(sortedList.size() / 2 - 1) + sortedList.get(sortedList.size() / 2)) / 2.0;
            }
        }
        return 0;
    }        
    
    /**
     * Returns the current timeframe of the sparkline in milliseconds
     * @return the current timeframe of the sparkline in milliseconds
     */
    public long getTimeFrame()
    {
        return this.timeFrame;
    }
    
    /**
     * Defines the current timeframe of the sparkline in milliseconds
     * @param TIME_FRAME 
     */
    public void setTimeFrame(final long TIME_FRAME)
    {
        this.timeFrame = TIME_FRAME;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the timestamp of the first value in the list of
     * datapoints as a long.
     * @return the timestamp of the first value in the datalist as a long
     */
    public long getStartTimestamp()
    {
        if (DATA_LIST.isEmpty())
        {
            return 0;
        }
        return DATA_LIST.getFirst().getTimeStamp();
    }
    
    /**
     * Returns the timestamp of the the last value in the list of
     * datapoints as a long.
     * @return the timestamp of the last value in the datalist as a long
     */
    public long getStopTimestamp()
    {
        if (DATA_LIST.isEmpty())
        {
            return 0;
        }
        return DATA_LIST.getLast().getTimeStamp();
    }
        
    /**
     * Returns true if the area under the sparkline will be filled with a gradient
     * @return true if the area under the sparkline will be filled with a gradient
     */
    public boolean isFilled()
    {
        return this.filled;
    }

    /**
     * Enables or disables the filling of the area below the sparkline
     * @param FILLED 
     */
    public void setFilled(final boolean FILLED)
    {
        this.filled = FILLED;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns true if the sparkline data will be smoothed
     * @return true if the sparkline data will be smoothed
     */
    public boolean isSmoothing()
    {
        return this.smoothing;
    }

    /**
     * Enables or disables the smoothing of the POINT_LIST and so of the sparkline
     * @param SMOOTHING 
     */
    public void setSmoothing(final boolean SMOOTHING)
    {
        this.smoothing = SMOOTHING;
        if (SMOOTHING)
        {
            hiLoIndicatorVisible = false;
        }
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns the current active color scheme of the sparkline (type eu.hansolo.tools.LcdColor)
     * @return the current active color scheme of the sparkline (type eu.hansolo.tools.LcdColor)
     */
    public eu.hansolo.steelseries.tools.LcdColor getSparkLineColor()
    {
        return this.sparkLineColor;
    }
    
    /**
     * Sets the color theme for the sparkline.
     * @param LCD_COLOR 
     */
    public void setSparkLineColor(final eu.hansolo.steelseries.tools.LcdColor LCD_COLOR)
    {
        this.lineColor = LCD_COLOR.TEXT_COLOR;
        this.sparkLineColor = LCD_COLOR;
        recreateImages = true;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the custom sparkline color of type java.awt.Paint which
     * will be used if the sparkLineColor is set to eu.hansolo.steelseries.tools.LcdColor.CUSTOM     
     * @return the custom sparkline color of type java.awt.Paint.     
     */
    public java.awt.Paint getCustomSparkLineColor()
    {
        return this.customSparkLineColor;
    }
    
    public void setCustomSparkLineColor(final java.awt.Paint CUSTOM_SPARKLINE_COLOR)
    {
        this.customSparkLineColor = CUSTOM_SPARKLINE_COLOR;
        recreateImages = true;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the color of the sparkline itself
     * @return the color of the sparkline itself
     */
    public java.awt.Color getLineColor()
    {
        return this.lineColor;
    }

    /**
     * Sets the color of the sparkline
     * @param LINE_COLOR 
     */
    public void setLineColor(final java.awt.Color LINE_COLOR)
    {
        this.lineColor = LINE_COLOR;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns the colordefinition of the area below the sparkline
     * @return the colordefinition of the area below the sparkline
     */
    public eu.hansolo.steelseries.tools.ColorDef getAreaFill()
    {
        return this.areaFill;
    }

    /**
     * Sets the colordefinition of the area below the sparkline
     * @param AREA_FILL_COLOR 
     */
    public void setAreaFill(final eu.hansolo.steelseries.tools.ColorDef AREA_FILL_COLOR)
    {
        this.areaFill = AREA_FILL_COLOR;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
                
    /**
     * Returns the color that will be used for a custom area gradient at top
     * @return the color that will be used for a custom area gradient at top
     */
    public java.awt.Color getCustomAreaFillTop()
    {
        return this.customAreaFillTop;
    }

    /**
     * Sets the color that will be used for a custom area gradient at top
     * @param CUSTOM_AREA_FILL_COLOR_TOP 
     */
    public void setCustomAreaFillTop(final java.awt.Color CUSTOM_AREA_FILL_COLOR_TOP)
    {
        customAreaFillTop = CUSTOM_AREA_FILL_COLOR_TOP;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns the color that will be used for a custom area gradient at bottom
     * @return the color that will be used for a custom area gradient at bottom
     */
    public java.awt.Color getCustomAreaFillBottom()
    {
        return this.customAreaFillBottom;
    }

    /**
     * Sets the color that will be used for a custom area gradient at bottom
     * @param CUSTOM_AREA_FILL_COLOR_BOTTOM 
     */
    public void setCustomAreaFillBottom(final java.awt.Color CUSTOM_AREA_FILL_COLOR_BOTTOM)
    {
        customAreaFillBottom = CUSTOM_AREA_FILL_COLOR_BOTTOM;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
       
    /**
     * Returns the width of the sparkline
     * @return the width of the sparkline
     */
    public float getLineWidth()
    {
        return this.lineWidth;
    }

    /**
     * Defines the width of the sparkline
     * @param LINE_WIDTH 
     */
    public void setLineWidth(final float LINE_WIDTH)
    {
        lineWidth = LINE_WIDTH;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns true if the sparkline shadow is visible
     * @return true if the sparkline shadow is visible
     */
    public boolean isLineShadowVisible()
    {
        return this.lineShadowVisible;
    }

    /**
     * Enables or disables the visibility of the sparkline shadow
     * @param LINE_SHADOW_VISIBLE 
     */
    public void setLineShadow(final boolean LINE_SHADOW_VISIBLE)
    {
        lineShadowVisible = LINE_SHADOW_VISIBLE;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns true if the start/stop indicators are visible.    
     * @return true if the start/stop indicators are visible
     */
    public boolean isStartStopIndicatorVisible()
    {
        return this.startStopIndicatorVisible;
    }

    /**
     * Defines the visibility of the start/stop indicators
     * @param START_STOP_INDICATOR_VISIBLE 
     */
    public void setStartStopIndicatorVisible(final boolean START_STOP_INDICATOR_VISIBLE)
    {
        startStopIndicatorVisible = START_STOP_INDICATOR_VISIBLE;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns true if the hi/lo indicators are visible.
     * They will be disabled automaticaly if smoothing is applied to 
     * the data.
     * @return true if the hi/lo indicators are visible
     */
    public boolean isHiLoIndicatorVisible()
    {
        return this.hiLoIndicatorVisible;
    }

    /**
     * Defines the visiblity of the hi/lo indicators
     * @param HI_LO_INDICATOR_VISIBLE 
     */
    public void setHiLoIndicatorVisible(final boolean HI_LO_INDICATOR_VISIBLE)
    {
        hiLoIndicatorVisible = HI_LO_INDICATOR_VISIBLE;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    /**
     * Returns true if the backgroundimage of the sparkline is visible
     * @return true if the backgroundimage of the sparkline is visible
     */
    public boolean isBackgroundVisible()
    {
        return this.backgroundVisible;
    }
    
    /**
     * Defines the visibility of the background image
     * @param BACKGROUND_VISIBLE 
     */
    public void setBackgroundVisible(final boolean BACKGROUND_VISIBLE)
    {
        this.backgroundVisible = BACKGROUND_VISIBLE;
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns true if the info labels are visible
     * @return true if the info labels are visible
     */
    public boolean isInfoLabelsVisible()
    {
        return this.infoLabelsVisible;
    }
    
    /**
     * Enables or disables the visibility of the info labels.
     * They won't be drawn if the sparkline is too small.
     * @param INFO_LABELS_VISIBLE 
     */
    public void setInfoLabelsVisible(final boolean INFO_LABELS_VISIBLE)
    {
        this.infoLabelsVisible = INFO_LABELS_VISIBLE;
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the smoothing function that is selected
     * @return ths smoothing function that is selected
     */
    public eu.hansolo.steelseries.tools.SmoothingFunction getSmoothFunction()
    {
        return this.smoothFunction;
    }

    /**
     * Defines the smoothing function that will be applied to the data if selected
     * @param SMOOTHING_FUNCTION 
     */
    public void setSmoothFunction(final eu.hansolo.steelseries.tools.SmoothingFunction SMOOTHING_FUNCTION)
    {
        this.smoothFunction = SMOOTHING_FUNCTION;
        init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns true if the baseline (value = 0) is visible
     * @return true if the baseline (value = 0) is visible
     */
    public boolean isBaseLineVisible()
    {
        return baseLineVisible;
    }
    
    /**
     * Enables or disables the visiblity of the baseline
     * @param BASE_LINE_VISIBLE 
     */
    public void setBaseLineVisible(final boolean BASE_LINE_VISIBLE)
    {
        baseLineVisible = BASE_LINE_VISIBLE;
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns true if the average is visible
     * @return true if the average is visible
     */
    public boolean isAverageVisible()
    {
        return averageVisible;
    }
    
    /**
     * Enables or disables the visibility of the average
     * @param AVERAGE_VISIBLE 
     */
    public void setAverageVisible(final boolean AVERAGE_VISIBLE)
    {
        averageVisible = AVERAGE_VISIBLE;        
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns true if the normal area visible
     * @return true if the normal area visible
     */
    public boolean isNormalAreaVisible()
    {
        return normalAreaVisible;
    }
 
    /**
     * Enables or disables the visibility of the normal area
     * @param NORMAL_AREA_VISIBLE 
     */
    public void setNormalAreaVisible(final boolean NORMAL_AREA_VISIBLE)
    {        
        normalAreaVisible = NORMAL_AREA_VISIBLE;        
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the color of the average
     * @return the color of the average
     */
    public java.awt.Color getAverageColor()
    {
        return averageColor;
    }
    
    /**
     * Defines the color for the average
     * @param AVERAGE_COLOR 
     */
    public void setAverageColor(final java.awt.Color AVERAGE_COLOR)
    {
        averageColor = AVERAGE_COLOR;
        repaint(INNER_BOUNDS);
    }
    
    /**
     * Returns the color that will be used for the normal color
     * @return the color that will be used for the normal color
     */
    public java.awt.Color getNormalAreaColor()
    {
        return normalAreaColor;
    }
    
    /**
     * Defines the color that will be used for the normal area. The given 
     * color will be used with an alpha of 0.2f
     * @param NORMAL_AREA_COLOR 
     */
    public void setNormalAreaColor(final java.awt.Color NORMAL_AREA_COLOR)
    {
        normalAreaColor = UTIL.setAlpha(NORMAL_AREA_COLOR, 0.2f);
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
    
    // <editor-fold defaultstate="collapsed" desc="Calculation methods">
    /**
     * Calculates the sparkline with all it's parameters. This methods
     * will be called everytime a new value was added to the DATA_LIST
     * @param WIDTH
     * @param HEIGHT 
     */
    private void calculate(final int WIDTH, final int HEIGHT)
    {        
        // Set start and stop values        
        start = DATA_LIST.getFirst().getValue();
        stop = DATA_LIST.getLast().getValue();

        // Find min and max values
        lo = DATA_LIST.getFirst().getValue();
        hi = DATA_LIST.getFirst().getValue();        
        loIndex = 0;
        hiIndex = 0;        
        final int SIZE = DATA_LIST.size();
        double y;
        for (int index = 0 ; index < SIZE ; index++)
        {
            y = DATA_LIST.get(index).getValue();                
            calcHiLoValues(y, index);
        }

        // Calculate the range from min to max
        rangeY = hi - lo;
                
        // Calculate the offset between the graph and the border
        //offset = (double) HEIGHT * OFFSET_FACTOR;

        // Calculate the scaling in x- and y-direction        
        scaleY = rangeY / ((double) HEIGHT - (offset * 2));
        
        // Fill the pointlist with smoothing if possible
        pointList.clear();        
        if (DATA_LIST.size() > 5 && smoothing)
        {
            smoothData();  
        }
        else
        {                        
            for (int index = 0 ; index < SIZE ; index++)
            {
                pointList.add(new java.awt.geom.Point2D.Double((DATA_LIST.get(index).getTimeStamp() - DATA_LIST.getFirst().getTimeStamp()) * pixelResolution, ((DATA_LIST.get(index).getValue() - lo) * (1 / scaleY) + offset)));                
            }
        }    
        
        // Calculate average and normal area if one of them is visible
        if (averageVisible || normalAreaVisible)
        {                        
            final double AVERAGE = (getAverage() - lo) * (1 / scaleY) + offset;
            final double STANDARD_DEVIATION = (getStandardDeviation() * (1 / scaleY));
            NORMAL_AREA.setRect(INNER_BOUNDS.x, AVERAGE - STANDARD_DEVIATION, INNER_BOUNDS.width, 2 * STANDARD_DEVIATION);
            AVERAGE_LINE.setLine(INNER_BOUNDS.x, AVERAGE, INNER_BOUNDS.x + INNER_BOUNDS.width, AVERAGE);
        }
    }
        
    /**
     * Calls the selected smoothing functions and fills the POINT_LIST
     * with the smoothed data
     */
    private void smoothData()
    {
        final int SIZE = DATA_LIST.size();
        double y;

        switch(smoothFunction)
        {
            case CONTINUOUS_AVERAGE:
                // Add first point                
                pointList.add(new java.awt.geom.Point2D.Double(0, ((DATA_LIST.getFirst().getValue() - lo) * (1 / scaleY) + offset)));                
                
                // Add the averaged points
                for (int i = 1 ; i < SIZE - 1 ; i++)
                {
                    //System.out.println((a * dataList.get(i - 1) + b * dataList.get(i) + c * (dataList.get( i + 1))) / (a + b + c));
                    y = continuousAverage(DATA_LIST.get(i - 1).getValue(), DATA_LIST.get(i).getValue(), DATA_LIST.get(i + 1).getValue());                    
                    pointList.add(new java.awt.geom.Point2D.Double((DATA_LIST.get(i).getTimeStamp() - DATA_LIST.getFirst().getTimeStamp()) * pixelResolution, ((y - lo) * (1 / scaleY) + offset)));                    
                }

                // Add last point                
                pointList.add(new java.awt.geom.Point2D.Double((DATA_LIST.getLast().getTimeStamp() - DATA_LIST.getFirst().getTimeStamp()) * pixelResolution, ((DATA_LIST.getLast().getValue() - lo) * (1 / scaleY) + offset)));                
                break;

            case CUBIC_SPLINE:
                for (int i = 2 ; i < SIZE - 1 ; i++)
                {
                    y = cubicInterpolate(DATA_LIST.get(i - 2).getValue(), DATA_LIST.get(i - 1).getValue(), DATA_LIST.get(i).getValue(), DATA_LIST.get(i + 1).getValue(), 0.5);                    
                    pointList.add(new java.awt.geom.Point2D.Double((DATA_LIST.get(i).getTimeStamp() - DATA_LIST.getFirst().getTimeStamp()) * pixelResolution, ((y - lo) * (1 / scaleY) + offset)));                    
                }
                break;

            case HERMITE:
                for (int i = 2 ; i < SIZE - 1 ; i++)
                {
                    y = hermiteInterpolate(DATA_LIST.get(i - 2).getValue(), DATA_LIST.get(i - 1).getValue(), DATA_LIST.get(i - 0).getValue(), DATA_LIST.get(i + 1).getValue(), 0.5, 0, 0);                    
                    pointList.add(new java.awt.geom.Point2D.Double((DATA_LIST.get(i).getTimeStamp() - DATA_LIST.getFirst().getTimeStamp()) * pixelResolution, ((y - lo) * (1 / scaleY) + offset)));
                }
                break;

            case COSINUS:                

            default:
                for (int i = 0 ; i < SIZE - 1 ; i++)
                {
                    y = cosInterpolate(DATA_LIST.get(i).getValue(), DATA_LIST.get(i + 1).getValue(), 0.5);                    
                    pointList.add(new java.awt.geom.Point2D.Double((DATA_LIST.get(i).getTimeStamp() - DATA_LIST.getFirst().getTimeStamp()) * pixelResolution, ((y - lo) * (1 / scaleY) + offset)));
                }
                break;
        }        
    }
        
    /**
     * Calculates the max and min measured values and stores the index of the
     * related values in in loIndex and hiIndex.
     * @param y
     * @param index 
     */
    private void calcHiLoValues(double y, int index)
    {
        if (y < lo)
        {
            lo = y;
            loIndex = index;
        }

        if (y > hi)
        {
            hi = y;
            hiIndex = index;
        }                
    }
    
    /**
     * Puts all values in a ArrayList and sorts them
     */
    private void sortData()
    {        
        sortedList.clear();
        for (eu.hansolo.steelseries.tools.DataPoint dataPoint : DATA_LIST)
        {
            sortedList.add(dataPoint.getValue());
        }
        java.util.Collections.sort(sortedList);        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Smoothing functions">
    /**
     * Returns the value smoothed by a continuous average function 
     * @param Y0
     * @param Y1
     * @param Y2
     * @return the value smoothed by a continous average function
     */
    private double continuousAverage(final double Y0, final double Y1, final double Y2)
    {
        final double A = 1;
        final double B = 1;
        final double C = 1;

        return ((A * Y0) + (B * Y1) + (C * Y2)) / (A + B + C);
    }

    /**
     * Returns the value smoothed by a cubic spline interpolation function
     * @param Y0
     * @param Y1
     * @param Y2
     * @param Y3
     * @param MU
     * @return the value smoothed by a cubic spline interpolation function
     */
    private double cubicInterpolate(final double Y0, final double Y1, final double Y2, final double Y3, final double MU)
    {
        final double A0;
        final double A1;
        final double A2;
        final double A3;
        final double MU2;

        MU2 = MU*MU;
        A0 = Y3 - Y2 - Y0 + Y1;
        A1 = Y0 - Y1 - A0;
        A2 = Y2 - Y0;
        A3 = Y1;

        return ((A0 * MU * MU2) + (A1 * MU2) + (A2 * MU) + A3);
    }

    /**
     * Returns the value smoothed by a cosinus interpolation function
     * @param Y1
     * @param Y2
     * @param MU
     * @return the value smoothed by a cosinus interpolation function
     */
    private double cosInterpolate(final double Y1, final double Y2, final double MU)
    {
        final double MU2;

        MU2 = (1 - Math.cos(MU * Math.PI)) / 2;
        return (Y1 * (1 - MU2) + Y2 * MU2);
    }

    /**
     * Returns the value smoothed by a hermite interpolation function
     * @param Y0
     * @param Y1
     * @param Y2
     * @param Y3
     * @param MU
     * @param TENSION
     * @param BIAS
     * @return the value smoothed by a hermite interpolation function
     */
    private double hermiteInterpolate(final double Y0, final double Y1, final double Y2, final double Y3, final double MU, final double TENSION, final double BIAS)
    {
        double m0;
        double m1;
        final double MU2;
        final double Mu3;
        final double A0;
        final double A1;
        final double A2;
        final double A3;

        MU2 = MU * MU;
        Mu3 = MU2 * MU;
        m0  = (Y1-Y0)*(1+BIAS)*(1-TENSION)/2;
        m0 += (Y2-Y1)*(1-BIAS)*(1-TENSION)/2;
        m1  = (Y2-Y1)*(1+BIAS)*(1-TENSION)/2;
        m1 += (Y3-Y2)*(1-BIAS)*(1-TENSION)/2;
        A0 =  (2 * Mu3) - (3 * MU2) + 1;
        A1 =    Mu3 - (2 * MU2) + MU;
        A2 =    Mu3 -   MU2;
        A3 = (-2 * Mu3) + (3 * MU2);

        return ((A0 * Y1) + (A1 * m0) + (A2 * m1) + (A3 * Y2));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Size related methods">
    /**
     * Calculates the area that is available for painting the display
     */
    private void calcInnerBounds()
    {
        final java.awt.Insets INSETS = getInsets();
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, getWidth() - INSETS.left - INSETS.right, getHeight() - INSETS.top - INSETS.bottom);
    }
    
    /**
     * Returns a rectangle2d representing the available space for drawing the
     * component taking the insets into account (e.g. given through borders etc.)
     * @return rectangle2d that represents the area available for rendering the component
     */
    public java.awt.Rectangle getInnerBounds()
    {        
        return INNER_BOUNDS;
    }
    
       @Override
    public java.awt.Dimension getMinimumSize()
    {        
        return new java.awt.Dimension(128, 48);
    }
    
    @Override
    public void setPreferredSize(final java.awt.Dimension DIM)
    {        
        super.setPreferredSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);                
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final int WIDTH, final int HEIGHT)
    {
        super.setSize(WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);                
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final java.awt.Dimension DIM)
    {
        super.setSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);                
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final java.awt.Rectangle BOUNDS)
    {
        super.setBounds(BOUNDS);
        calcInnerBounds();
        init(BOUNDS.width, BOUNDS.height);                
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        super.setBounds(X, Y, WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);                
        revalidate();
        repaint();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Image related methods">
    /**
     * Returns a buffered image that contains the background of the sparkline component
     * @param WIDTH
     * @param HEIGHT
     * @return a buffered image that contains the background of the sparkline component
     */
    private java.awt.image.BufferedImage create_SPARK_LINE_BACKGROUND_Image(final int WIDTH, final int HEIGHT)
    {        
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        // Background rectangle
        final java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Double(0.0, 0.0);
        final java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0.0, IMAGE_HEIGHT);
        if (BACKGROUND_START.equals(BACKGROUND_STOP))
        {
            BACKGROUND_STOP.setLocation(0.0, BACKGROUND_START.getY() + 1);
        } 
        
        final float[] BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.08f,
            0.92f,
            1.0f
        };

        final java.awt.Color[] BACKGROUND_COLORS =
        {
            new java.awt.Color(0.4f, 0.4f, 0.4f, 1.0f),
            new java.awt.Color(0.5f, 0.5f, 0.5f, 1.0f),
            new java.awt.Color(0.5f, 0.5f, 0.5f, 1.0f),
            new java.awt.Color(0.9f, 0.9f, 0.9f, 1.0f)
        };

        final java.awt.LinearGradientPaint BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);
        final double BACKGROUND_CORNER_RADIUS = WIDTH * 0.09375;
        final java.awt.geom.RoundRectangle2D BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, BACKGROUND_CORNER_RADIUS, BACKGROUND_CORNER_RADIUS);
        G2.setPaint(BACKGROUND_GRADIENT);
        G2.fill(BACKGROUND);

        // Foreground rectangle
        final java.awt.geom.Point2D FOREGROUND_START = new java.awt.geom.Point2D.Double(0.0, 1.0);
        final java.awt.geom.Point2D FOREGROUND_STOP = new java.awt.geom.Point2D.Double(0.0, IMAGE_HEIGHT - 1);
        if (FOREGROUND_START.equals(FOREGROUND_STOP))
        {
            FOREGROUND_STOP.setLocation(0.0, FOREGROUND_START.getY() + 1);
        } 
        
        final float[] FOREGROUND_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.49f,
            0.5f,
            1.0f
        };

        final java.awt.Color[] FOREGROUND_COLORS =
        {
            sparkLineColor.GRADIENT_START_COLOR,
            sparkLineColor.GRADIENT_FRACTION1_COLOR,
            sparkLineColor.GRADIENT_FRACTION2_COLOR,
            sparkLineColor.GRADIENT_FRACTION3_COLOR,
            sparkLineColor.GRADIENT_STOP_COLOR
        };
                
        if (customSparkLineColor != null && sparkLineColor == eu.hansolo.steelseries.tools.LcdColor.CUSTOM)
        {
            G2.setPaint(customSparkLineColor);
        }
        else
        {
            final java.awt.LinearGradientPaint FOREGROUND_GRADIENT = new java.awt.LinearGradientPaint(FOREGROUND_START, FOREGROUND_STOP, FOREGROUND_FRACTIONS, FOREGROUND_COLORS);                
            G2.setPaint(FOREGROUND_GRADIENT);
        }
        final double FOREGROUND_CORNER_RADIUS = BACKGROUND.getArcWidth() - 1;
        final java.awt.geom.RoundRectangle2D FOREGROUND = new java.awt.geom.RoundRectangle2D.Double(1, 1, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, FOREGROUND_CORNER_RADIUS, FOREGROUND_CORNER_RADIUS);
        G2.fill(FOREGROUND);

        G2.dispose();

        return IMAGE;
    }
    
    /**
     * Returns a buffered image that contains the sparkline itself. This image will be calculated
     * everytime a new value was added to the DATA_LIST
     * @param WIDTH
     * @param HEIGHT
     * @return a buffered image that contains the sparkline itself
     */
    private java.awt.image.BufferedImage createSparkLineImage(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }
                
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();

        if (!pointList.isEmpty())
        {
            G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);

            // Generate sparkline and filled sparkline
            final java.awt.geom.GeneralPath SPARK_LINE = new java.awt.geom.GeneralPath();
            final java.awt.geom.GeneralPath SPARK_LINE_FILLED = new java.awt.geom.GeneralPath();

            SPARK_LINE_FILLED.moveTo(pointList.get(0).getX(), baseLineY);

            SPARK_LINE.moveTo(pointList.get(0).getX(), HEIGHT - pointList.get(0).getY());
            SPARK_LINE_FILLED.lineTo(pointList.get(0).getX(), HEIGHT - pointList.get(0).getY());

            for (java.awt.geom.Point2D point : pointList)
            {
                SPARK_LINE.lineTo(point.getX(), HEIGHT - point.getY());
                SPARK_LINE_FILLED.lineTo(point.getX(), HEIGHT - point.getY());
            }

            SPARK_LINE_FILLED.lineTo(pointList.get(pointList.size() - 1).getX(), baseLineY);
            SPARK_LINE_FILLED.closePath();

            // Draw sparkline
            G2.setStroke(new java.awt.BasicStroke(this.lineWidth, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_MITER));

            if (filled)
            {
                final java.awt.geom.Point2D START_POINT = new java.awt.geom.Point2D.Double(0, INNER_BOUNDS.y);
                final java.awt.geom.Point2D END_POINT = new java.awt.geom.Point2D.Double(0, INNER_BOUNDS.y + INNER_BOUNDS.height);                                
                if (START_POINT.equals(END_POINT))
                {
                    END_POINT.setLocation(0.0, START_POINT.getY() + 1);
                } 
                float baseLineFraction = (float) (baseLineY / INNER_BOUNDS.height);
                if (baseLineFraction < 0f || baseLineFraction > 1f)
                {
                    baseLineFraction = 0.5f;
                }
                if (START_POINT.distance(END_POINT) != 0)
                {
                    float[] fractions =
                    {
                        0.0f,
                        baseLineFraction,
                        1.0f
                    };

                    // Set appropriate filling gradient
                    final java.awt.Color[] COLORS;
                    if (areaFill == eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                    {
                        COLORS = new java.awt.Color[]
                        {
                            customAreaFillTop,
                            customAreaFillBottom,
                            customAreaFillTop
                        };
                    }
                    else
                    {
                        COLORS = new java.awt.Color[]
                        {
                            UTIL.setAlpha(areaFill.LIGHT, 0.75f),
                            UTIL.setAlpha(areaFill.DARK, 0.0f),
                            UTIL.setAlpha(areaFill.LIGHT, 0.75f)
                        };
                    }
                    final java.awt.LinearGradientPaint SPARK_LINE_GRADIENT = new java.awt.LinearGradientPaint(START_POINT, END_POINT, fractions, COLORS);
                    G2.setPaint(SPARK_LINE_GRADIENT);
                    G2.fill(SPARK_LINE_FILLED);
                }
            }
            else
            {
                // Draw shadow of line which looks good on dark backgrounds
                if (lineShadowVisible)
                {
                    G2.translate(1, 1);
                    G2.setColor(new java.awt.Color(0x000000));
                    G2.draw(SPARK_LINE);
                    G2.translate(-1, -1);
                }
            }

            // Set appropriate line color
            G2.setColor(lineColor);
            G2.draw(SPARK_LINE);

            // Draw indicators
            if (startStopIndicatorVisible)
            {                
                G2.drawImage(startIndicatorImage, (int) pointList.get(0).getX() - startIndicatorImage.getWidth() / 2, HEIGHT - (int) pointList.get(0).getY() - startIndicatorImage.getHeight() / 2, null);
                G2.drawImage(stopIndicatorImage, (int) pointList.get(pointList.size() - 1).getX() - stopIndicatorImage.getWidth() / 2, HEIGHT - (int) pointList.get(pointList.size() - 1).getY() - stopIndicatorImage.getHeight() / 2, null);
            }
            if (hiLoIndicatorVisible)
            {
                if (loIndex < pointList.size())
                {
                    G2.drawImage(loIndicatorImage, (int) pointList.get(loIndex).getX() - loIndicatorImage.getWidth() / 2, HEIGHT - (int) pointList.get(loIndex).getY() - loIndicatorImage.getHeight() / 2, null);
                }   
                if (hiIndex < pointList.size())
                {
                    G2.drawImage(hiIndicatorImage, (int) pointList.get(hiIndex).getX() - hiIndicatorImage.getWidth() / 2, HEIGHT - (int) pointList.get(hiIndex).getY() - hiIndicatorImage.getHeight() / 2, null);
                }                
            }

        }
        G2.dispose();

        return IMAGE;
    }
    
    /**
     * Returns a buffered image that contains the hi value indicator
     * @param WIDTH
     * @return a buffered image that contains the hi value indicator
     */
    private java.awt.image.BufferedImage create_HI_INDICATOR_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        
        // Define the size of the indicator
        int indicatorSize = (int)(0.015 * WIDTH);        
        if (indicatorSize < 4)
        {
            indicatorSize = 4;
        }
        if (indicatorSize > 8)
        {
            indicatorSize = 8;
        }
                
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(indicatorSize, indicatorSize, java.awt.Transparency.TRANSLUCENT);
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

        final java.awt.geom.GeneralPath THRESHOLD_TRIANGLE = new java.awt.geom.GeneralPath();
        THRESHOLD_TRIANGLE.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        THRESHOLD_TRIANGLE.moveTo(IMAGE_WIDTH * 0.5, 0);
        THRESHOLD_TRIANGLE.lineTo(0, IMAGE_HEIGHT);
        THRESHOLD_TRIANGLE.lineTo(IMAGE_WIDTH, IMAGE_HEIGHT);
        THRESHOLD_TRIANGLE.lineTo(IMAGE_WIDTH * 0.5, 0);
        THRESHOLD_TRIANGLE.closePath();
        final java.awt.geom.Point2D THRESHOLD_TRIANGLE_START = new java.awt.geom.Point2D.Double(0, THRESHOLD_TRIANGLE.getBounds2D().getMinY() );
        final java.awt.geom.Point2D THRESHOLD_TRIANGLE_STOP = new java.awt.geom.Point2D.Double(0, THRESHOLD_TRIANGLE.getBounds2D().getMaxY() );
        final float[] THRESHOLD_TRIANGLE_FRACTIONS = 
        {
            0.0f,
            0.3f,
            0.59f,
            1.0f
        };
        final java.awt.Color[] THRESHOLD_TRIANGLE_COLORS = 
        {
            new java.awt.Color(82, 0, 0, 255),
            new java.awt.Color(252, 29, 0, 255),
            new java.awt.Color(252, 29, 0, 255),
            new java.awt.Color(82, 0, 0, 255)
        };
        final java.awt.LinearGradientPaint THRESHOLD_TRIANGLE_GRADIENT = new java.awt.LinearGradientPaint(THRESHOLD_TRIANGLE_START, THRESHOLD_TRIANGLE_STOP, THRESHOLD_TRIANGLE_FRACTIONS, THRESHOLD_TRIANGLE_COLORS);
        G2.setPaint(THRESHOLD_TRIANGLE_GRADIENT);
        G2.fill(THRESHOLD_TRIANGLE);        
        G2.setColor(java.awt.Color.RED);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(THRESHOLD_TRIANGLE);

        G2.dispose();

        return IMAGE;
    }
    
    /**
     * Returns a buffered image that contains the lo value indicator
     * @param WIDTH
     * @return a buffered image that contains the lo valuw indicator
     */
    private java.awt.image.BufferedImage create_LO_INDICATOR_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        
        // Define the size of the indicator
        int indicatorSize = (int)(0.015 * WIDTH);        
        if (indicatorSize < 4)
        {
            indicatorSize = 4;
        }
        if (indicatorSize > 8)
        {
            indicatorSize = 8;
        }
                
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(indicatorSize, indicatorSize, java.awt.Transparency.TRANSLUCENT);
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

        final java.awt.geom.GeneralPath THRESHOLD_TRIANGLE = new java.awt.geom.GeneralPath();
        THRESHOLD_TRIANGLE.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        THRESHOLD_TRIANGLE.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT);
        THRESHOLD_TRIANGLE.lineTo(0, 0);
        THRESHOLD_TRIANGLE.lineTo(IMAGE_WIDTH, 0);
        THRESHOLD_TRIANGLE.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT);
        THRESHOLD_TRIANGLE.closePath();
        final java.awt.geom.Point2D THRESHOLD_TRIANGLE_START = new java.awt.geom.Point2D.Double(0, THRESHOLD_TRIANGLE.getBounds2D().getMaxY() );
        final java.awt.geom.Point2D THRESHOLD_TRIANGLE_STOP = new java.awt.geom.Point2D.Double(0, THRESHOLD_TRIANGLE.getBounds2D().getMinY() );
        final float[] THRESHOLD_TRIANGLE_FRACTIONS = 
        {
            0.0f,
            0.3f,
            0.59f,
            1.0f
        };
        final java.awt.Color[] THRESHOLD_TRIANGLE_COLORS = 
        {
            new java.awt.Color(0, 0, 72, 255),
            new java.awt.Color(0, 29, 255, 255),
            new java.awt.Color(0, 29, 255, 255),
            new java.awt.Color(0, 0, 72, 255)
        };
        final java.awt.LinearGradientPaint THRESHOLD_TRIANGLE_GRADIENT = new java.awt.LinearGradientPaint(THRESHOLD_TRIANGLE_START, THRESHOLD_TRIANGLE_STOP, THRESHOLD_TRIANGLE_FRACTIONS, THRESHOLD_TRIANGLE_COLORS);
        G2.setPaint(THRESHOLD_TRIANGLE_GRADIENT);
        G2.fill(THRESHOLD_TRIANGLE);        
        G2.setColor(new java.awt.Color(0x001DFF));
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(THRESHOLD_TRIANGLE);

        G2.dispose();

        return IMAGE;
    }
    
    /**
     * Returns a buffered image that contains the start/stop indicator
     * @param WIDTH
     * @return a buffered image that contains the start/stop indicator
     */
    private java.awt.image.BufferedImage create_START_STOP_INDICATOR_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        
        // Define the size of the indicator
        int indicatorSize = (int)(0.015 * WIDTH);        
        if (indicatorSize < 4)
        {
            indicatorSize = 4;
        }
        if (indicatorSize > 8)
        {
            indicatorSize = 8;
        }
                
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(indicatorSize, indicatorSize, java.awt.Transparency.TRANSLUCENT);
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

        final java.awt.geom.Ellipse2D ELLIPSE = new java.awt.geom.Ellipse2D.Double(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);        
        final java.awt.geom.Point2D ELLIPSE_CENTER = new java.awt.geom.Point2D.Double( (0.42857142857142855 * IMAGE_WIDTH), (0.2857142857142857 * IMAGE_HEIGHT) );
        final float[] ELLIPSE_FRACTIONS = 
        {
            0.0f,
            0.01f,
            0.99f,
            1.0f
        };
        final java.awt.Color[] ELLIPSE_COLORS = 
        {
            new java.awt.Color(204, 204, 204, 255),
            new java.awt.Color(204, 204, 204, 255),
            new java.awt.Color(51, 51, 51, 255),
            new java.awt.Color(51, 51, 51, 255)
        };
        final java.awt.RadialGradientPaint ELLIPSE_GRADIENT = new java.awt.RadialGradientPaint(ELLIPSE_CENTER, (float)(0.5 * IMAGE_WIDTH), ELLIPSE_FRACTIONS, ELLIPSE_COLORS);
        G2.setPaint(ELLIPSE_GRADIENT);
        G2.fill(ELLIPSE);

        G2.dispose();

        return IMAGE;
    }
    
    /**
     * Calling this method will add all relevant imagetypes to the list of image types
     * that are needed to initialize the component
     */
    private void createInitialImages()
    {
        recreateImages = true;        
    }
    
    /**
     * Calling this method will add all imagetypes to the list of imagetypes
     * so that the next time the init method will be called all images will
     * be recreated
     */
    private void recreateAllImages()
    {
        recreateImages = true;
    }    
    // </editor-fold>
    
    @Override
    public String toString()
    {
        return "Sparkline";
    }
}
