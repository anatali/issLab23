package eu.hansolo.steelseries.tools;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public enum TickmarkImageFactory
{
    INSTANCE;
    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;    
    private static final java.awt.BasicStroke MAJOR_TICKMARK_STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);
    private static final java.awt.BasicStroke MEDIUM_TICKMARK_STROKE = new java.awt.BasicStroke(0.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);
    private static final java.awt.BasicStroke MINOR_TICKMARK_STROKE = new java.awt.BasicStroke(0.3f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);    
    private eu.hansolo.steelseries.tools.NumberFormat numberFormat = eu.hansolo.steelseries.tools.NumberFormat.STANDARD;
    
    // Buffer variables of radial gauges
    private java.awt.image.BufferedImage imageBufferRad = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private int widthBufferRad = 200;
    private double minValueBufferRad = 0;
    private double maxValueBufferRad = 100; 
    private int noOfMinorTicksBufferRad = 0;
    private int noOfMajorTicksBufferRad = 0;
    private double minorTickSpacingBufferRad = 10;
    private double majorTickSpacingBufferRad = 10;
    private eu.hansolo.steelseries.tools.GaugeType gaugeTypeBufferRad = eu.hansolo.steelseries.tools.GaugeType.TYPE4;
    private eu.hansolo.steelseries.tools.TickmarkType minorTickmarkTypeBufferRad = eu.hansolo.steelseries.tools.TickmarkType.LINE;
    private eu.hansolo.steelseries.tools.TickmarkType majorTickmarkTypeBufferRad = eu.hansolo.steelseries.tools.TickmarkType.LINE;
    private boolean ticksVisibleBufferRad = true;
    private boolean ticklabelsVisibleBufferRad = true;
    private boolean tickmarkSectionsVisibleBufferRad = false;
    private boolean minorTicksVisibleBufferRad = true;
    private boolean majorTicksVisibleBufferRad = true;
    private eu.hansolo.steelseries.tools.NumberFormat numberFormatBufferRad = eu.hansolo.steelseries.tools.NumberFormat.AUTO;
    private eu.hansolo.steelseries.tools.BackgroundColor backgroundColorBufferRad = eu.hansolo.steelseries.tools.BackgroundColor.DARK_GRAY;
    private java.awt.Color tickmarkColorBufferRad = backgroundColorBufferRad.LABEL_COLOR;
    private boolean tickmarkColorFromThemeBufferRad = true;
    private java.util.List<eu.hansolo.steelseries.tools.Section> tickmarkSectionsBufferRad = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
    private float radiusFactorBufferRad = 0.38f;
    private float textDistanceFactorBufferRad = 0.09f;
    private java.awt.geom.Point2D centerBufferRad = new java.awt.geom.Point2D.Double();
    private java.awt.geom.Point2D offsetBufferRad = new java.awt.geom.Point2D.Double();
    private eu.hansolo.steelseries.tools.Orientation orientationBufferRad = eu.hansolo.steelseries.tools.Orientation.NORTH;    
    private boolean niceScaleRad = true;
    
    // Buffer variables of linear gauges
    private java.awt.image.BufferedImage imageBufferLin = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private int widthBufferLin = 140; 
    private int heightBufferLin = 0; 
    private double minValueBufferLin = 0; 
    private double maxValueBufferLin = 100; 
    private int noOfMinorTicksBufferLin = 0;
    private int noOfMajorTicksBufferLin = 0;
    private double minorTickSpacingBufferLin = 10;
    private double majorTickSpacingBufferLin = 10;
    private eu.hansolo.steelseries.tools.TickmarkType minorTickmarkTypeBufferLin = eu.hansolo.steelseries.tools.TickmarkType.LINE;
    private eu.hansolo.steelseries.tools.TickmarkType majorTickmarkTypeBufferLin = eu.hansolo.steelseries.tools.TickmarkType.LINE;        
    private boolean ticksVisibleBufferLin = true; 
    private boolean ticklabelsVisibleBufferLin = true;
    private boolean minorTicksVisibleBufferLin = true;
    private boolean majorTicksVisibleBufferLin = true;
    private eu.hansolo.steelseries.tools.NumberFormat numberFormatBufferLin = eu.hansolo.steelseries.tools.NumberFormat.AUTO;
    private boolean tickmarkSectionsVisibleBufferLin = false;
    private eu.hansolo.steelseries.tools.BackgroundColor backgroundColorBufferLin = eu.hansolo.steelseries.tools.BackgroundColor.DARK_GRAY;
    private java.awt.Color tickmarkColorBufferLin = backgroundColorBufferLin.LABEL_COLOR;
    private boolean tickmarkColorFromThemeBufferLin = true;
    private java.util.List<eu.hansolo.steelseries.tools.Section> tickmarkSectionsBufferLin = new java.util.ArrayList<eu.hansolo.steelseries.tools.Section>(10);
    private java.awt.geom.Point2D offsetBufferLin = new java.awt.geom.Point2D.Double();
    private eu.hansolo.steelseries.tools.Orientation orientationBufferLin = eu.hansolo.steelseries.tools.Orientation.VERTICAL;    
    private boolean niceScaleLin = true;
    
    public java.awt.image.BufferedImage create_RADIAL_TICKMARKS_Image(final int WIDTH, 
                                                               final double MIN_VALUE,
                                                               final double MAX_VALUE, 
                                                               final int NO_OF_MINOR_TICKS,
                                                               final int NO_OF_MAJOR_TICKS,
                                                               final double MINOR_TICK_SPACING,
                                                               final double MAJOR_TICK_SPACING,
                                                               final eu.hansolo.steelseries.tools.GaugeType GAUGE_TYPE,
                                                               final eu.hansolo.steelseries.tools.TickmarkType MINOR_TICKMARK_TYPE,
                                                               final eu.hansolo.steelseries.tools.TickmarkType MAJOR_TICKMARK_TYPE,
                                                               final boolean TICKS_VISIBLE,
                                                               final boolean TICKLABELS_VISIBLE,
                                                               final boolean MINOR_TICKS_VISIBLE,
                                                               final boolean MAJOR_TICKS_VISIBLE,
                                                               final eu.hansolo.steelseries.tools.NumberFormat NUMBER_FORMAT,
                                                               final boolean TICKMARK_SECTIONS_VISIBLE,
                                                               final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR,
                                                               final java.awt.Color TICKMARK_COLOR,
                                                               final boolean TICKMARK_COLOR_FROM_THEME,
                                                               java.util.List<eu.hansolo.steelseries.tools.Section> tickmarkSections,
                                                               final float RADIUS_FACTOR,
                                                               final float TEXT_DISTANCE_FACTOR,
                                                               final java.awt.geom.Point2D CENTER,
                                                               final java.awt.geom.Point2D OFFSET,
                                                               final eu.hansolo.steelseries.tools.Orientation ORIENTATION,
                                                               final boolean NICE_SCALE,
                                                               final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
                        
        // Buffer check
        if (WIDTH == widthBufferRad &&
            Double.compare(MIN_VALUE, minValueBufferRad) == 0 &&
            Double.compare(MAX_VALUE, maxValueBufferRad) == 0 &&
            NO_OF_MINOR_TICKS == noOfMinorTicksBufferRad &&
            NO_OF_MAJOR_TICKS == noOfMajorTicksBufferRad &&
            Double.compare(MINOR_TICK_SPACING, minorTickSpacingBufferRad) == 0 &&
            Double.compare(MAJOR_TICK_SPACING, majorTickSpacingBufferRad) == 0 &&
            GAUGE_TYPE == gaugeTypeBufferRad &&
            MINOR_TICKMARK_TYPE == minorTickmarkTypeBufferRad &&
            MAJOR_TICKMARK_TYPE == majorTickmarkTypeBufferRad &&
            TICKS_VISIBLE == ticksVisibleBufferRad &&
            TICKLABELS_VISIBLE == ticklabelsVisibleBufferRad &&
            MINOR_TICKS_VISIBLE == minorTicksVisibleBufferRad &&
            MAJOR_TICKS_VISIBLE == majorTicksVisibleBufferRad &&
            TICKMARK_SECTIONS_VISIBLE == tickmarkSectionsVisibleBufferRad &&
            NUMBER_FORMAT == numberFormatBufferRad &&
            BACKGROUND_COLOR == backgroundColorBufferRad &&
            TICKMARK_COLOR.equals(tickmarkColorBufferRad) &&
            TICKMARK_COLOR_FROM_THEME == tickmarkColorFromThemeBufferRad &&
            tickmarkSections.containsAll(tickmarkSectionsBufferRad) &&
            Float.compare(RADIUS_FACTOR, radiusFactorBufferRad) == 0 &&
            Float.compare(TEXT_DISTANCE_FACTOR, textDistanceFactorBufferRad) == 0 &&
            CENTER.equals(centerBufferRad) &&            
            OFFSET.equals(offsetBufferRad) &&
            orientationBufferRad == ORIENTATION &&
            niceScaleRad == NICE_SCALE)
        {            
            if (BACKGROUND_IMAGE != null)
            {
                final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
                G.drawImage(imageBufferRad, 0, 0, null);
                G.dispose();                       
                return imageBufferRad;
            }
        }
                        
        // Create image if it equals null        
        if (imageBufferRad != null)
        {
            imageBufferRad.flush();
        }
        imageBufferRad = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);                                                             
        
        // Adjust the number format of the ticklabels
        if (NUMBER_FORMAT == eu.hansolo.steelseries.tools.NumberFormat.AUTO)
        {
            if (Math.abs(MAJOR_TICK_SPACING) > 1000)
            {
                numberFormat = eu.hansolo.steelseries.tools.NumberFormat.SCIENTIFIC;
            }
            else if (MAJOR_TICK_SPACING % 1.0 != 0)
            {
                numberFormat = eu.hansolo.steelseries.tools.NumberFormat.FRACTIONAL;
            }
            else
            {
                numberFormat = eu.hansolo.steelseries.tools.NumberFormat.STANDARD;
            }
        }
        else
        {
            numberFormat = NUMBER_FORMAT;
        }

        // Definitions   
        final java.awt.Font STD_FONT = new java.awt.Font("Verdana", 0, (int) (0.04 * WIDTH));
        final int TEXT_DISTANCE = (int) (TEXT_DISTANCE_FACTOR * WIDTH);
        final double TICKLABEL_ROTATION_OFFSET = 0;        
        final int MINOR_TICK_LENGTH = (int) (0.0133333333 * WIDTH);
        final int MEDIUM_TICK_LENGTH = (int) (0.02 * WIDTH);
        final int MAJOR_TICK_LENGTH = (int) (0.03 * WIDTH); 
        final int MINOR_DIAMETER = (int) (0.0093457944 * WIDTH);
        //final int MEDIUM_DIAMETER = (int) (0.0186915888 * WIDTH);
        final int MAJOR_DIAMETER = (int) (0.03 * WIDTH);
        final java.awt.geom.Point2D TEXT_POINT = new java.awt.geom.Point2D.Double(0, 0);
        final java.awt.geom.Point2D INNER_POINT = new java.awt.geom.Point2D.Double(0, 0);
        final java.awt.geom.Point2D OUTER_POINT = new java.awt.geom.Point2D.Double(0, 0);
        final java.awt.geom.Point2D OUTER_POINT_LEFT = new java.awt.geom.Point2D.Double(0,0);
        final java.awt.geom.Point2D OUTER_POINT_RIGHT = new java.awt.geom.Point2D.Double(0,0);        
        final java.awt.geom.Line2D TICK_LINE = new java.awt.geom.Line2D.Double(0, 0, 1, 1);
        final java.awt.geom.Ellipse2D TICK_CIRCLE = new java.awt.geom.Ellipse2D.Double(0, 0, 1, 1);               
        final java.awt.geom.GeneralPath TICK_TRIANGLE = new java.awt.geom.GeneralPath();    
        final double ROTATION_OFFSET = GAUGE_TYPE.ROTATION_OFFSET; // Depends on GaugeType
        final float RADIUS = WIDTH * RADIUS_FACTOR;
        final double ANGLE_STEP = (GAUGE_TYPE.ANGLE_RANGE / ((MAX_VALUE - MIN_VALUE) / MINOR_TICK_SPACING));
        double sinValue = 0;
        double cosValue = 0;
        double valueCounter = MIN_VALUE;
        int majorTickCounter = NO_OF_MINOR_TICKS - 1; // Indicator when to draw the major tickmark
        
        
        // Create the image        
        final java.awt.Graphics2D G2 = imageBufferRad.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Set some default parameters for the graphics object
        if (OFFSET != null)
        {
            G2.translate(OFFSET.getX(), OFFSET.getY());
        }                                
        G2.setFont(STD_FONT);                    
                                
        G2.rotate(ROTATION_OFFSET - Math.PI, CENTER.getX(), CENTER.getY());
        if (TICKMARK_COLOR_FROM_THEME)
        {
            G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
        }
        else
        {
            G2.setColor(TICKMARK_COLOR);
        }
        
        for (double alpha = 0, counter = MIN_VALUE; Float.compare((float) counter, (float) MAX_VALUE) <= 0; alpha -= ANGLE_STEP, counter += MINOR_TICK_SPACING)
        {
            // Set the color
            if (tickmarkSections != null && !tickmarkSections.isEmpty())
            {
                if (TICKMARK_SECTIONS_VISIBLE)
                {
                    for (eu.hansolo.steelseries.tools.Section section : tickmarkSections)
                    {
                        if (Double.compare(valueCounter, section.getStart()) >= 0 && Double.compare(valueCounter, section.getStop()) <= 0)
                        {
                            G2.setColor(section.getColor());
                            break;
                        }
                        else if (TICKMARK_COLOR_FROM_THEME)
                        {
                            G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
                        }
                        else
                        {
                            G2.setColor(TICKMARK_COLOR);
                        }
                    }
                }
                else
                {
                    if (TICKMARK_COLOR_FROM_THEME)
                    {
                        G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
                    }
                    else
                    {
                        G2.setColor(TICKMARK_COLOR);
                    }
                }
            }                        
            
            sinValue = Math.sin(alpha);
            cosValue = Math.cos(alpha);            
            majorTickCounter++;

            // Draw tickmark every major tickmark spacing                                    
            if (majorTickCounter == NO_OF_MINOR_TICKS)
            {
                G2.setStroke(MAJOR_TICKMARK_STROKE);
                INNER_POINT.setLocation(CENTER.getX() + (RADIUS - MAJOR_TICK_LENGTH) * sinValue, CENTER.getY() + (RADIUS - MAJOR_TICK_LENGTH) * cosValue);
                OUTER_POINT.setLocation(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
                TEXT_POINT.setLocation(CENTER.getX() + (RADIUS - TEXT_DISTANCE) * sinValue, CENTER.getY() + (RADIUS - TEXT_DISTANCE) * cosValue);

                // Draw the major tickmarks
                if (TICKS_VISIBLE && MAJOR_TICKS_VISIBLE)
                {
                    drawRadialTicks(G2, INNER_POINT, OUTER_POINT, CENTER, RADIUS, MAJOR_TICKMARK_TYPE, TICK_LINE, TICK_CIRCLE, TICK_TRIANGLE, MAJOR_TICK_LENGTH, MAJOR_DIAMETER, OUTER_POINT_LEFT, OUTER_POINT_RIGHT, alpha);
                }

                // Draw the standard tickmark labels                
                if (TICKLABELS_VISIBLE)
                {
                    G2.fill(UTIL.rotateTextAroundCenter(G2, numberFormat.format(valueCounter), (int) TEXT_POINT.getX(), (int) TEXT_POINT.getY(), Math.toDegrees(Math.PI - alpha + TICKLABEL_ROTATION_OFFSET)));
                }

                valueCounter += MAJOR_TICK_SPACING;
                majorTickCounter = 0;
                continue;
            }
            
            // Draw tickmark every minor tickmark spacing              
            {
                INNER_POINT.setLocation(CENTER.getX() + (RADIUS - MINOR_TICK_LENGTH) * sinValue, CENTER.getY() + (RADIUS - MINOR_TICK_LENGTH) * cosValue);
                OUTER_POINT.setLocation(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
                G2.setStroke(MINOR_TICKMARK_STROKE);
                if (NO_OF_MINOR_TICKS % 2 == 0 && majorTickCounter == (NO_OF_MINOR_TICKS / 2))
                {
                    G2.setStroke(MEDIUM_TICKMARK_STROKE);
                    INNER_POINT.setLocation(CENTER.getX() + (RADIUS - MEDIUM_TICK_LENGTH) * sinValue,
                                            CENTER.getY() + (RADIUS - MEDIUM_TICK_LENGTH) * cosValue);
                    OUTER_POINT.setLocation(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
                    
                }
                
                // Draw the minor tickmarks
                if (TICKS_VISIBLE && MINOR_TICKS_VISIBLE)
                {
                    drawRadialTicks(G2, INNER_POINT, OUTER_POINT, CENTER, RADIUS, MINOR_TICKMARK_TYPE, TICK_LINE, TICK_CIRCLE, TICK_TRIANGLE, MINOR_TICK_LENGTH, MINOR_DIAMETER, OUTER_POINT_LEFT, OUTER_POINT_RIGHT, alpha);
                }
            }
        }        
        G2.dispose();

        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();       
            G.drawImage(imageBufferRad, 0, 0, null);
            G.dispose();        
        }
        
        // Buffer the current parameters
        widthBufferRad = WIDTH;        
        minValueBufferRad = MIN_VALUE;
        maxValueBufferRad = MAX_VALUE; 
        noOfMinorTicksBufferRad = NO_OF_MINOR_TICKS;
        noOfMajorTicksBufferRad = NO_OF_MAJOR_TICKS;
        minorTickSpacingBufferRad = MINOR_TICK_SPACING;
        majorTickSpacingBufferRad = MAJOR_TICK_SPACING;
        gaugeTypeBufferRad = GAUGE_TYPE;
        minorTickmarkTypeBufferRad = MINOR_TICKMARK_TYPE;
        majorTickmarkTypeBufferRad = MAJOR_TICKMARK_TYPE;
        ticksVisibleBufferRad = TICKS_VISIBLE;
        ticklabelsVisibleBufferRad = TICKLABELS_VISIBLE;
        minorTicksVisibleBufferRad = MINOR_TICKS_VISIBLE;
        majorTicksVisibleBufferRad = MAJOR_TICKS_VISIBLE;
        tickmarkSectionsVisibleBufferRad = TICKMARK_SECTIONS_VISIBLE;
        numberFormatBufferRad = NUMBER_FORMAT;
        backgroundColorBufferRad = BACKGROUND_COLOR;
        tickmarkColorBufferRad = TICKMARK_COLOR;
        tickmarkColorFromThemeBufferRad = TICKMARK_COLOR_FROM_THEME;
        if (tickmarkSections != null)
        {
            tickmarkSectionsBufferRad.clear();
            tickmarkSectionsBufferRad.addAll(tickmarkSections);
        }
        radiusFactorBufferRad = RADIUS_FACTOR;
        textDistanceFactorBufferRad = TEXT_DISTANCE_FACTOR;
        centerBufferRad.setLocation(CENTER);
        if (OFFSET != null)
        {
            offsetBufferRad.setLocation(OFFSET);
        }
        orientationBufferRad = ORIENTATION;
        niceScaleRad = NICE_SCALE;
        
        return imageBufferRad;
    }
        
    public java.awt.image.BufferedImage create_LINEAR_TICKMARKS_Image(final int WIDTH, 
                                                               final int HEIGHT, 
                                                               final double MIN_VALUE, 
                                                               final double MAX_VALUE, 
                                                               final int NO_OF_MINOR_TICKS,
                                                               final int NO_OF_MAJOR_TICKS,
                                                               final double MINOR_TICK_SPACING,
                                                               final double MAJOR_TICK_SPACING,
                                                               final eu.hansolo.steelseries.tools.TickmarkType MINOR_TICKMARK_TYPE,
                                                               final eu.hansolo.steelseries.tools.TickmarkType MAJOR_TICKMARK_TYPE,                                                                                                                              
                                                               final boolean TICKS_VISIBLE, 
                                                               final boolean TICKLABELS_VISIBLE, 
                                                               final boolean MINOR_TICKS_VISIBLE,
                                                               final boolean MAJOR_TICKS_VISIBLE,
                                                               final eu.hansolo.steelseries.tools.NumberFormat NUMBER_FORMAT,
                                                               final boolean TICKMARK_SECTIONS_VISIBLE,
                                                               final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR,
                                                               final java.awt.Color TICKMARK_COLOR,
                                                               final boolean TICKMARK_COLOR_FROM_THEME,
                                                               java.util.List<eu.hansolo.steelseries.tools.Section> tickmarkSections,
                                                               final java.awt.geom.Point2D OFFSET,
                                                               final eu.hansolo.steelseries.tools.Orientation ORIENTATION,
                                                               final boolean NICE_SCALE,
                                                               final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
                
        // Buffer check
        if (WIDTH == widthBufferLin && 
            HEIGHT == heightBufferLin && 
            Double.compare(MIN_VALUE, minValueBufferLin) == 0 && 
            Double.compare(MAX_VALUE, maxValueBufferLin) == 0 && 
            NO_OF_MINOR_TICKS == noOfMinorTicksBufferLin &&
            NO_OF_MAJOR_TICKS == noOfMajorTicksBufferLin &&
            Double.compare(MINOR_TICK_SPACING, minorTickSpacingBufferLin) == 0 &&
            Double.compare(MAJOR_TICK_SPACING, majorTickSpacingBufferLin) == 0 &&
            MINOR_TICKMARK_TYPE == minorTickmarkTypeBufferLin &&
            MAJOR_TICKMARK_TYPE == majorTickmarkTypeBufferLin &&
            TICKS_VISIBLE == ticksVisibleBufferLin &&
            MINOR_TICKS_VISIBLE == minorTicksVisibleBufferLin &&
            MAJOR_TICKS_VISIBLE == majorTicksVisibleBufferLin &&
            TICKLABELS_VISIBLE == ticklabelsVisibleBufferLin &&
            NUMBER_FORMAT == numberFormatBufferLin &&
            TICKMARK_SECTIONS_VISIBLE == tickmarkSectionsVisibleBufferLin &&
            BACKGROUND_COLOR == backgroundColorBufferLin &&
            TICKMARK_COLOR.equals(tickmarkColorBufferLin) &&
            TICKMARK_COLOR_FROM_THEME == tickmarkColorFromThemeBufferLin &&
            tickmarkSections.containsAll(tickmarkSectionsBufferLin) &&
            OFFSET.equals(offsetBufferLin) &&
            ORIENTATION == orientationBufferLin &&
            NICE_SCALE == niceScaleLin)
        {            
            if (BACKGROUND_IMAGE != null)
            {
                final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
                G.drawImage(imageBufferLin, 0, 0, null);                
                G.dispose();        
                
                return imageBufferLin;
            }
        }    
    
        // Create image if it equals null        
        if (imageBufferLin != null)
        {
            imageBufferLin.flush();            
        }
        imageBufferLin = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);        
            
        // Adjust the number format of the ticklabels
        if (NUMBER_FORMAT == eu.hansolo.steelseries.tools.NumberFormat.AUTO)
        {
            if (Math.abs(MAJOR_TICK_SPACING) > 1000)
            {
                numberFormat = eu.hansolo.steelseries.tools.NumberFormat.SCIENTIFIC;
            }
            else if (MAJOR_TICK_SPACING % 1.0 != 0)
            {
                numberFormat = eu.hansolo.steelseries.tools.NumberFormat.FRACTIONAL;
            }
        }
        else
        {
            numberFormat = NUMBER_FORMAT;
        }
                                
        // Definitions
        final java.awt.Font STD_FONT;
        final java.awt.geom.Rectangle2D SCALE_BOUNDS;
        
        if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {            
            // Vertical orientation
            STD_FONT = new java.awt.Font("Verdana", 0, (int) (0.062 * WIDTH));
            SCALE_BOUNDS = new java.awt.geom.Rectangle2D.Double(0, HEIGHT * 0.12864077669902912, 0, (HEIGHT * 0.8567961165048543 - HEIGHT * 0.12864077669902912));
        }
        else
        {
            // Horizontal orientation
            STD_FONT = new java.awt.Font("Verdana", 0, (int) (0.062 * HEIGHT));
            SCALE_BOUNDS = new java.awt.geom.Rectangle2D.Double(WIDTH * 0.14285714285714285, 0, (WIDTH * 0.8710124827 - WIDTH * 0.14285714285714285), 0);
        } 
                
        final int MINOR_DIAMETER;
        final int MAJOR_DIAMETER;
        final int MINOR_TICK_START;
        final int MINOR_TICK_STOP;
        final int MEDIUM_TICK_START;
        final int MEDIUM_TICK_STOP;
        final int MAJOR_TICK_START;
        final int MAJOR_TICK_STOP;
        if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {
            MINOR_DIAMETER = (int) (0.0186915888 * WIDTH);
            MAJOR_DIAMETER = (int) (0.0280373832 * WIDTH);  
            MINOR_TICK_START = (int) (0.34 * WIDTH);
            MINOR_TICK_STOP = (int) (0.36 * WIDTH);
            MEDIUM_TICK_START = (int) (0.33 * WIDTH);
            MEDIUM_TICK_STOP = (int) (0.36 * WIDTH);
            MAJOR_TICK_START = (int) (0.32 * WIDTH);
            MAJOR_TICK_STOP = (int) (0.36 * WIDTH);
        }
        else
        {         
            MINOR_DIAMETER = (int) (0.0186915888 * HEIGHT);
            MAJOR_DIAMETER = (int) (0.0280373832 * HEIGHT);  
            MINOR_TICK_START = (int) (0.65 * HEIGHT);
            MINOR_TICK_STOP = (int) (0.63 * HEIGHT);
            MEDIUM_TICK_START = (int) (0.66 * HEIGHT);
            MEDIUM_TICK_STOP = (int) (0.63 * HEIGHT);
            MAJOR_TICK_START = (int) (0.67 * HEIGHT);
            MAJOR_TICK_STOP = (int) (0.63 * HEIGHT);
        }
                                      
        final java.awt.Graphics2D G2 = imageBufferLin.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Set some default parameters for the graphics object
        if (OFFSET != null)
        {
            G2.translate(OFFSET.getX(), OFFSET.getY());            
        }

        G2.setFont(STD_FONT);                            
        
        if (TICKMARK_COLOR_FROM_THEME)
        {
            G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
        }
        else
        {
            G2.setColor(TICKMARK_COLOR);           
        }
        
        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
        final java.awt.font.TextLayout TEXT_LAYOUT = new java.awt.font.TextLayout(numberFormat.format(MAX_VALUE), G2.getFont(), RENDER_CONTEXT);
        final java.awt.geom.Rectangle2D MAX_BOUNDS = TEXT_LAYOUT.getBounds(); // needed to align the numbers on the right (in vertical layout)
        final java.awt.geom.Line2D TICK_LINE = new java.awt.geom.Line2D.Double(0, 0, 1, 1);
        final java.awt.geom.Ellipse2D TICK_CIRCLE = new java.awt.geom.Ellipse2D.Double(0, 0, 1, 1);   
        final java.awt.geom.GeneralPath TICK_TRIANGLE = new java.awt.geom.GeneralPath();
                
        java.awt.font.TextLayout currentLayout;
        java.awt.geom.Rectangle2D currentBounds;        
        float textOffset = 0;
        double currentPos = 0;        
        double valueCounter = MIN_VALUE;        
        int majorTickCounter = NO_OF_MINOR_TICKS - 1; // Indicator when to draw the major tickmark
        double tickSpaceScaling = 1.0;
        switch (ORIENTATION)
        {
            case VERTICAL:
                tickSpaceScaling = SCALE_BOUNDS.getHeight() / (MAX_VALUE - MIN_VALUE);                
                break;
                
            case HORIZONTAL:
                tickSpaceScaling = SCALE_BOUNDS.getWidth() / (MAX_VALUE - MIN_VALUE);                
                break;
        }
                                
        for (double labelCounter = MIN_VALUE, tickCounter = 0; Float.compare((float) labelCounter, (float) MAX_VALUE) <= 0; labelCounter += MINOR_TICK_SPACING, tickCounter += MINOR_TICK_SPACING)        
        {
            // Adjust the color for the tickmark and labels
            if (tickmarkSections != null && !tickmarkSections.isEmpty())
            {
                if (TICKMARK_SECTIONS_VISIBLE)
                {                    
                    for (eu.hansolo.steelseries.tools.Section section : tickmarkSections)
                    {                        
                        if ((Double.compare(tickCounter, section.getStart()) >= 0) && (Double.compare(tickCounter, section.getStop()) <= 0))
                        {
                            G2.setColor(section.getColor());                            
                            break;
                        }
                        else if(TICKMARK_COLOR_FROM_THEME)
                        {
                            G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
                        }
                        else
                        {
                            G2.setColor(TICKMARK_COLOR);
                        }                                                
                    }
                }
                else
                {
                    if(TICKMARK_COLOR_FROM_THEME)
                    {
                        G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
                    }
                    else
                    {
                        G2.setColor(TICKMARK_COLOR);
                    }
                }
            }
            else
            {
                if(TICKMARK_COLOR_FROM_THEME)
                {
                    G2.setColor(BACKGROUND_COLOR.LABEL_COLOR);
                }
                else
                {
                    G2.setColor(TICKMARK_COLOR);                   
                }
            }
                    
            // Calculate the bounds of the scaling
            if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
            {
                currentPos = SCALE_BOUNDS.getMaxY() - tickCounter * tickSpaceScaling;                   
            }
            else
            {
                currentPos = SCALE_BOUNDS.getX() + tickCounter * tickSpaceScaling;                
            }
                        
            majorTickCounter++;                        
            
            // Draw tickmark every major tickmark spacing                                    
            if (majorTickCounter == NO_OF_MINOR_TICKS)
            {
                G2.setStroke(MAJOR_TICKMARK_STROKE);

                // Draw the major tickmarks                
                if (TICKS_VISIBLE && MAJOR_TICKS_VISIBLE)
                {                                        
                    drawLinearTicks(G2, WIDTH, HEIGHT, ORIENTATION, currentPos, MAJOR_TICKMARK_TYPE, TICK_LINE, TICK_CIRCLE, TICK_TRIANGLE, MAJOR_TICK_START, MAJOR_TICK_STOP, MAJOR_DIAMETER);                                       
                }
                
                // Draw the standard tickmark labels                
                if (TICKLABELS_VISIBLE)
                {                                              
                    currentLayout = new java.awt.font.TextLayout(numberFormat.format(valueCounter), G2.getFont(), RENDER_CONTEXT);
                    currentBounds = currentLayout.getBounds(); 
                    if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
                    {
                        // Vertical orientation
                        textOffset = (float) (MAX_BOUNDS.getWidth() - currentBounds.getWidth());                        
                        G2.drawString(numberFormat.format(valueCounter), 0.18f * WIDTH + textOffset, (float)(currentPos - currentBounds.getHeight() / 2.0 + currentBounds.getHeight()));
                    }
                    else
                    {
                        // Horizontal orientation
                        G2.drawString(numberFormat.format(valueCounter), (float) (tickCounter * tickSpaceScaling - currentBounds.getWidth() / 3.0 + SCALE_BOUNDS.getX()), (float) (HEIGHT * 0.68 + 1.5 * currentBounds.getHeight()));
                    }
                }

                valueCounter += MAJOR_TICK_SPACING;                
                majorTickCounter = 0;                
                continue;
            }
            
            // Draw tickmark every minor tickmark spacing  
            if (TICKS_VISIBLE && MINOR_TICKS_VISIBLE)
            {
                G2.setStroke(MINOR_TICKMARK_STROKE);
                if (NO_OF_MINOR_TICKS % 2 == 0 && majorTickCounter == (NO_OF_MINOR_TICKS / 2))
                {
                    G2.setStroke(MEDIUM_TICKMARK_STROKE);
                    drawLinearTicks(G2, WIDTH, HEIGHT, ORIENTATION, currentPos, MINOR_TICKMARK_TYPE, TICK_LINE, TICK_CIRCLE, TICK_TRIANGLE, MEDIUM_TICK_START, MEDIUM_TICK_STOP, MINOR_DIAMETER);                   

                }                                
                else
                {                    
                    drawLinearTicks(G2, WIDTH, HEIGHT, ORIENTATION, currentPos, MINOR_TICKMARK_TYPE, TICK_LINE, TICK_CIRCLE, TICK_TRIANGLE, MINOR_TICK_START, MINOR_TICK_STOP, MINOR_DIAMETER);                   
                }   
            }
        }            
        G2.dispose();

        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
            G.drawImage(imageBufferLin, 0, 0, null);                       
            G.dispose();        
        }
        
        // Buffer the current parameters
        widthBufferLin = WIDTH; 
        heightBufferLin = HEIGHT; 
        minValueBufferLin = MIN_VALUE; 
        maxValueBufferLin = MAX_VALUE; 
        noOfMinorTicksBufferLin = NO_OF_MINOR_TICKS;
        noOfMajorTicksBufferLin = NO_OF_MAJOR_TICKS;
        minorTickSpacingBufferLin = MINOR_TICK_SPACING;
        majorTickSpacingBufferLin = MAJOR_TICK_SPACING;
        minorTickmarkTypeBufferLin = MINOR_TICKMARK_TYPE;
        majorTickmarkTypeBufferLin = MAJOR_TICKMARK_TYPE;        
        ticksVisibleBufferLin = TICKS_VISIBLE; 
        ticklabelsVisibleBufferLin = TICKLABELS_VISIBLE;
        minorTicksVisibleBufferLin = MINOR_TICKS_VISIBLE;
        majorTicksVisibleBufferLin = MAJOR_TICKS_VISIBLE;
        numberFormatBufferLin = NUMBER_FORMAT;
        tickmarkSectionsVisibleBufferLin = TICKMARK_SECTIONS_VISIBLE;
        backgroundColorBufferLin = BACKGROUND_COLOR;
        tickmarkColorBufferLin = TICKMARK_COLOR;
        tickmarkColorFromThemeBufferLin = TICKMARK_COLOR_FROM_THEME;
        if (tickmarkSections != null)
        {
            tickmarkSectionsBufferLin.clear();
            tickmarkSectionsBufferLin.addAll(tickmarkSections);
        }                        
        if (OFFSET != null)
        {
            offsetBufferLin.setLocation(OFFSET);
        }
        orientationBufferLin = ORIENTATION;
        niceScaleLin = NICE_SCALE;

        return imageBufferLin;
    }
    
    
    private void drawRadialTicks(final java.awt.Graphics2D G2, 
                                 final java.awt.geom.Point2D INNER_POINT, 
                                 final java.awt.geom.Point2D OUTER_POINT, 
                                 final java.awt.geom.Point2D CENTER,
                                 final double RADIUS,
                                 final eu.hansolo.steelseries.tools.TickmarkType TICKMARK_TYPE,
                                 final java.awt.geom.Line2D TICK_LINE,
                                 final java.awt.geom.Ellipse2D TICK_CIRCLE,
                                 final java.awt.geom.GeneralPath TICK_TRIANGLE,
                                 final double TICK_LENGTH,
                                 final double DIAMETER,
                                 final java.awt.geom.Point2D OUTER_POINT_LEFT,
                                 final java.awt.geom.Point2D OUTER_POINT_RIGHT,
                                 final double ALPHA)
    {
        // Draw tickmark every major tickmark spacing                                                   
        switch(TICKMARK_TYPE)
        {            
            case CIRCLE:
                TICK_CIRCLE.setFrame(OUTER_POINT.getX() - DIAMETER / 2.0, OUTER_POINT.getY() - DIAMETER / 2.0, DIAMETER, DIAMETER);
                G2.fill(TICK_CIRCLE);
                break;    
            case TRIANGLE:
                OUTER_POINT_LEFT.setLocation(CENTER.getX() + RADIUS * Math.sin(ALPHA - Math.toRadians(Math.asin(TICK_LENGTH / 16.0))), CENTER.getY() + RADIUS * Math.cos(ALPHA - Math.toRadians(Math.asin(TICK_LENGTH / 16.0))));
                OUTER_POINT_RIGHT.setLocation(CENTER.getX() + RADIUS * Math.sin(ALPHA + Math.toRadians(Math.asin(TICK_LENGTH / 16.0))), CENTER.getY() + RADIUS * Math.cos(ALPHA + Math.toRadians(Math.asin(TICK_LENGTH / 16.0))));
                TICK_TRIANGLE.reset();
                TICK_TRIANGLE.moveTo(INNER_POINT.getX(), INNER_POINT.getY());
                TICK_TRIANGLE.lineTo(OUTER_POINT_LEFT.getX(), OUTER_POINT_LEFT.getY());
                TICK_TRIANGLE.lineTo(OUTER_POINT_RIGHT.getX(), OUTER_POINT_RIGHT.getY());
                TICK_TRIANGLE.closePath();
                G2.fill(TICK_TRIANGLE);
                break;
            case LINE:                
                
            default:
                TICK_LINE.setLine(INNER_POINT, OUTER_POINT);
                G2.draw(TICK_LINE);
                break;
        }        
    }    

    private void drawLinearTicks(final java.awt.Graphics2D G2, 
                                 final int WIDTH,
                                 final int HEIGHT,
                                 final eu.hansolo.steelseries.tools.Orientation ORIENTATION,
                                 final double CURRENT_POS,
                                 final eu.hansolo.steelseries.tools.TickmarkType TICKMARK_TYPE,
                                 final java.awt.geom.Line2D TICK_LINE,
                                 final java.awt.geom.Ellipse2D TICK_CIRCLE,
                                 final java.awt.geom.GeneralPath TICK_TRIANGLE,
                                 final double TICK_START,
                                 final double TICK_STOP,
                                 final double DIAMETER)
    {                        
        switch(TICKMARK_TYPE)
        {            
            case CIRCLE:
                if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
                {
                    TICK_CIRCLE.setFrame(TICK_START, CURRENT_POS - DIAMETER / 2.0, DIAMETER, DIAMETER);
                }
                else
                {
                    TICK_CIRCLE.setFrame(CURRENT_POS - DIAMETER / 2.0, TICK_STOP, DIAMETER, DIAMETER);
                }
                G2.fill(TICK_CIRCLE);
                break;   
            case TRIANGLE:    
                TICK_TRIANGLE.reset();
                if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
                {
                    // Vertical orientation                                
                    TICK_TRIANGLE.moveTo(TICK_START, CURRENT_POS + WIDTH * 0.005);
                    TICK_TRIANGLE.lineTo(TICK_START, CURRENT_POS - WIDTH * 0.005);
                    TICK_TRIANGLE.lineTo(TICK_STOP, CURRENT_POS);
                    TICK_TRIANGLE.closePath();
                }
                else
                {
                    // Horizontal orientation                                
                    TICK_TRIANGLE.moveTo(CURRENT_POS - HEIGHT * 0.005, TICK_START);
                    TICK_TRIANGLE.lineTo(CURRENT_POS + HEIGHT * 0.005, TICK_START);
                    TICK_TRIANGLE.lineTo(CURRENT_POS, TICK_STOP);
                    TICK_TRIANGLE.closePath();
                }
                G2.fill(TICK_TRIANGLE);
                break;    
                
            case LINE:    
                
            default:
                if (ORIENTATION == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
                {
                    // Vertical orientation
                    TICK_LINE.setLine(TICK_START, CURRENT_POS, TICK_STOP, CURRENT_POS);                                        
                }
                else
                {
                    // Horizontal orientation
                    TICK_LINE.setLine(CURRENT_POS, TICK_START, CURRENT_POS, TICK_STOP);                    
                }                
                G2.draw(TICK_LINE);
                break;    
        }
    }
}