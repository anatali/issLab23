package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public class LinearBargraph extends AbstractLinearBargraph
{        
    // One image to reduce memory consumption
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage thresholdImage;
    private java.awt.image.BufferedImage minMeasuredImage;
    private java.awt.image.BufferedImage maxMeasuredImage;    
    private java.awt.image.BufferedImage disabledImage;
    private eu.hansolo.steelseries.tools.ColorDef barGraphColor;
    private java.awt.geom.Rectangle2D led;
    private java.awt.geom.Point2D ledCenter;
    private java.awt.Color[] ledColors;
    private final float[] LED_FRACTIONS;
    private java.awt.RadialGradientPaint ledGradient;
    private java.awt.RadialGradientPaint ledInactiveGradient;
    private final java.awt.Color[] LED_INACTIVE_COLORS;
    private java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.RadialGradientPaint> sectionGradients;
    private java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.geom.Point2D> sectionOffsets;
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();  
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    // The value background
    private final java.awt.geom.Rectangle2D VALUE_BACKGROUND_TRACK;
    private final java.awt.geom.Point2D VALUE_BACKGROUND_VERTICAL_START;
    private final java.awt.geom.Point2D VALUE_BACKGROUND_VERTICAL_STOP;
    private final float[] VALUE_BACKGROUND_TRACK_FRACTIONS;
    // Value track border
    private final java.awt.geom.Rectangle2D VALUE_LEFT_BORDER;
    private final java.awt.geom.Rectangle2D VALUE_RIGHT_BORDER;
    private final java.awt.geom.Point2D VALUE_BORDER_START;
    private final java.awt.geom.Point2D VALUE_BORDER_STOP;
    private final float[] VALUE_BORDER_FRACTIONS;
    
    
    public LinearBargraph()
    {
        super();        
        barGraphColor = getModel().getValueColor();        
        LED_FRACTIONS = new float[]
        {
            0.0f,
            1.0f
        };        
        LED_INACTIVE_COLORS = new java.awt.Color[]
        {
            new java.awt.Color(60, 60, 60, 255),
            new java.awt.Color(50, 50, 50, 255)
        };
        sectionGradients = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.RadialGradientPaint>(4);
        sectionOffsets = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.geom.Point2D>(4);

        // The value background
        VALUE_BACKGROUND_TRACK = new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10);
        VALUE_BACKGROUND_VERTICAL_START = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BACKGROUND_VERTICAL_STOP = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BACKGROUND_TRACK_FRACTIONS = new float[]
        {
            0.0f,
            0.48f,
            0.49f,
            1.0f
        };
        // Value track border
        VALUE_LEFT_BORDER = new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10);
        VALUE_RIGHT_BORDER = new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10);
        VALUE_BORDER_START = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BORDER_STOP = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BORDER_FRACTIONS = new float[]
        {
            0.0f,
            0.48f,
            0.49f,
            1.0f
        };        
        prepareBargraph(getInnerBounds().width, getInnerBounds().height);               
        init(getInnerBounds().width, getInnerBounds().height);
    }

    public LinearBargraph(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        super();
        setModel(MODEL);        
        barGraphColor = MODEL.getValueColor();        
        LED_FRACTIONS = new float[]
        {
            0.0f,
            1.0f
        };        
        LED_INACTIVE_COLORS = new java.awt.Color[]
        {
            new java.awt.Color(60, 60, 60, 255),
            new java.awt.Color(50, 50, 50, 255)
        };
        sectionGradients = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.RadialGradientPaint>(4);
        sectionOffsets = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.geom.Point2D>(4);

        // The value background
        VALUE_BACKGROUND_TRACK = new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10);
        VALUE_BACKGROUND_VERTICAL_START = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BACKGROUND_VERTICAL_STOP = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BACKGROUND_TRACK_FRACTIONS = new float[]
        {
            0.0f,
            0.48f,
            0.49f,
            1.0f
        };
        // Value track border
        VALUE_LEFT_BORDER = new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10);
        VALUE_RIGHT_BORDER = new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10);
        VALUE_BORDER_START = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BORDER_STOP = new java.awt.geom.Point2D.Double(0, 0);
        VALUE_BORDER_FRACTIONS = new float[]
        {
            0.0f,
            0.48f,
            0.49f,
            1.0f
        };        
        prepareBargraph(getInnerBounds().width, getInnerBounds().height);               
        init(getInnerBounds().width, getInnerBounds().height);
    }
    
    @Override
    public final AbstractGauge init(final int WIDTH, final int HEIGHT)
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
        bImage = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        
        // Create Foreground Image
        if (fImage != null)
        {
            fImage.flush();
        }
        fImage = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        
        prepareBargraph(WIDTH, HEIGHT);

        final float LCD_TEXT_HEIGHT_BASE;
        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.HORIZONTAL)
        {
            LCD_TEXT_HEIGHT_BASE = HEIGHT * 0.15f;
        }
        else
        {
            LCD_TEXT_HEIGHT_BASE = HEIGHT * 0.055f;
        }
        if (isDigitalFont())
        {                            
            setLcdValueFont(LCD_DIGITAL_FONT.deriveFont(0.7f * LCD_TEXT_HEIGHT_BASE));            
        }
        else
        {
            setLcdValueFont(LCD_STANDARD_FONT.deriveFont(0.625f * LCD_TEXT_HEIGHT_BASE));       
        }

        if (isCustomLcdUnitFontEnabled())
        {
            setLcdUnitFont(getCustomLcdUnitFont().deriveFont(0.25f * LCD_TEXT_HEIGHT_BASE));
        }
        else
        {
            setLcdUnitFont(LCD_STANDARD_FONT.deriveFont(0.25f * LCD_TEXT_HEIGHT_BASE));
        }
                        
        setLcdInfoFont(getModel().getStandardInfoFont().deriveFont(0.15f * LCD_TEXT_HEIGHT_BASE));
        
        if (isFrameVisible())
        {
            create_FRAME_Image(WIDTH, HEIGHT, bImage);                    
        }

        if (isBackgroundVisible())
        {
            create_BACKGROUND_Image(WIDTH, HEIGHT, bImage);
        }      

        if (isTrackVisible())
        {
            create_TRACK_Image(WIDTH, HEIGHT, getMinValue(), getMaxValue(), getTrackStart(), getTrackSection(), getTrackStop(), getTrackStartColor(), getTrackSectionColor(), getTrackStopColor(), bImage);
        }        
        
        TICKMARK_FACTORY.create_LINEAR_TICKMARKS_Image(WIDTH, 
                                                HEIGHT,
                                                getModel().getNiceMinValue(), 
                                                getModel().getNiceMaxValue(),                                                                                   
                                                getModel().getMaxNoOfMinorTicks(),
                                                getModel().getMaxNoOfMajorTicks(),
                                                getModel().getMinorTickSpacing(),
                                                getModel().getMajorTickSpacing(),                                                
                                                getMinorTickmarkType(),
                                                getMajorTickmarkType(),
                                                isTickmarksVisible(),
                                                isTicklabelsVisible(),
                                                getModel().isMinorTickmarksVisible(),
                                                getModel().isMajorTickmarksVisible(),
                                                getLabelNumberFormat(),
                                                isTickmarkSectionsVisible(),
                                                getBackgroundColor(),
                                                getTickmarkColor(),
                                                isTickmarkColorFromThemeEnabled(),
                                                getTickmarkSections(),                                                
                                                new java.awt.geom.Point2D.Double(0,0),
                                                getOrientation(),
                                                getModel().isNiceScale(),
                                                bImage);

        if (isTitleVisible())
        {
            create_TITLE_Image(WIDTH, HEIGHT, getModel().isUnitVisible(), bImage);
        } 

        if (isLcdVisible())
        {                                                
            switch(getOrientation())
            {
                case HORIZONTAL:
                    // Horizontal
                    create_LCD_Image(new java.awt.geom.Rectangle2D.Double((WIDTH * 0.695 + getInnerBounds().x), (getHeight() * 0.22 + getInnerBounds().y), (WIDTH * 0.18), (HEIGHT * 0.15)), getLcdColor(), getCustomLcdBackground(), bImage);                
                    LCD.setRect((WIDTH * 0.695 + getInnerBounds().x), (getHeight() * 0.22 + getInnerBounds().y),WIDTH * 0.18, HEIGHT * 0.15);                    
                    break;

                case VERTICAL:
                    create_LCD_Image(new java.awt.geom.Rectangle2D.Double(((WIDTH - (WIDTH * 0.5714285714)) / 2.0 + getInnerBounds().x), (HEIGHT * 0.88 + getInnerBounds().y), (WIDTH * 0.5714285714), (HEIGHT * 0.055)), getLcdColor(), getCustomLcdBackground(), bImage);                
                    LCD.setRect(((WIDTH - (WIDTH * 0.5714285714)) / 2.0 + getInnerBounds().x), (HEIGHT * 0.88 + getInnerBounds().y), WIDTH * 0.5714285714, HEIGHT * 0.055);                    
                    break;
            }
        }   
        
        if (thresholdImage != null)
        {
            thresholdImage.flush();
        }
        thresholdImage = create_THRESHOLD_Image(WIDTH, HEIGHT);

        if (minMeasuredImage != null)
        {
            minMeasuredImage.flush();
        }
        minMeasuredImage = create_MEASURED_VALUE_Image(WIDTH, HEIGHT, new java.awt.Color(0, 23, 252, 255));

        if (maxMeasuredImage != null)
        {
            maxMeasuredImage.flush();
        }
        maxMeasuredImage = create_MEASURED_VALUE_Image(WIDTH, HEIGHT, new java.awt.Color(252, 29, 0, 255));

        if (isForegroundVisible())
        {
            FOREGROUND_FACTORY.createLinearForeground(WIDTH, HEIGHT, false, fImage);            
        } 
        
        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH, HEIGHT);

        setCurrentLedImage(getLedImageOff());

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

        //final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);

        // Draw threshold indicator
        if (isThresholdVisible())
        {            
            final double VALUE_POS;
            final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
            if (getInnerBounds().width < getInnerBounds().height)
            {
                // Vertical orientation
                VALUE_POS = getInnerBounds().height * 0.8567961165048543 - (getInnerBounds().height * 0.7281553398) * (getThreshold() / (getMaxValue() - getMinValue())) + getInnerBounds().y;
                G2.translate(getInnerBounds().width * 0.4357142857142857 - thresholdImage.getWidth() - 2 + getInnerBounds().x, VALUE_POS - thresholdImage.getHeight() / 2.0 + getInnerBounds().y);
            }
            else
            {
                // Horizontal orientation
                VALUE_POS = ((getInnerBounds().width * 0.8567961165048543) - (getInnerBounds().width * 0.12864077669902912)) * getThreshold() / (getMaxValue() - getMinValue()) + getInnerBounds().x;
                G2.translate(getInnerBounds().width * 0.14285714285714285 - thresholdImage.getWidth() / 2.0 + VALUE_POS + getInnerBounds().x, getHeight() * 0.5714285714 + 2 + getInnerBounds().y);
            }
            G2.drawImage(thresholdImage, 0, 0, null);

            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw min measured value indicator
        if (isMinMeasuredValueVisible())
        {
            final double VALUE_POS;
            final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
            if (getInnerBounds().width < getInnerBounds().height)
            {
                // Vertical orientation
                VALUE_POS = getInnerBounds().height * 0.8567961165048543 - (getInnerBounds().height * 0.7281553398) * (getMinMeasuredValue() / (getMaxValue() - getMinValue())) + getInnerBounds().y;
                G2.translate(getInnerBounds().width * 0.37 - minMeasuredImage.getWidth() - 2 + getInnerBounds().x, VALUE_POS - minMeasuredImage.getHeight() / 2.0 + getInnerBounds().y);
            }
            else
            {
                // Horizontal orientation
                VALUE_POS = ((getInnerBounds().width * 0.8567961165048543) - (getInnerBounds().width * 0.12864077669902912)) * getMinMeasuredValue() / (getMaxValue() - getMinValue()) + getInnerBounds().x;
                G2.translate(getInnerBounds().width * 0.14285714285714285 - minMeasuredImage.getWidth() / 2.0 + VALUE_POS + getInnerBounds().x, getInnerBounds().height * 0.63 + 2 + getInnerBounds().y);
            }
            G2.drawImage(minMeasuredImage, 0, 0, null);

            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw max measured value indicator
        if (isMaxMeasuredValueVisible())
        {
            final double VALUE_POS;
            final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
            if (getInnerBounds().width < getInnerBounds().height)
            {
                // Vertical orientation
                VALUE_POS = getInnerBounds().height * 0.8567961165048543 - (getInnerBounds().height * 0.7281553398) * (getMaxMeasuredValue() / (getMaxValue() - getMinValue())) + getInnerBounds().y;
                G2.translate(getInnerBounds().width * 0.37 - maxMeasuredImage.getWidth() - 2 + getInnerBounds().x, VALUE_POS - maxMeasuredImage.getHeight() / 2.0 + getInnerBounds().y);
            }
            else
            {
                // Horizontal orientation
                VALUE_POS = ((getInnerBounds().width * 0.8567961165048543) - (getInnerBounds().width * 0.12864077669902912)) * getMaxMeasuredValue() / (getMaxValue() - getMinValue()) + getInnerBounds().x;
                G2.translate(getInnerBounds().width * 0.14285714285714285 - maxMeasuredImage.getWidth() / 2.0 + VALUE_POS + getInnerBounds().x, getHeight() * 0.63 + 2 + getInnerBounds().y);
            }
            G2.drawImage(maxMeasuredImage, 0, 0, null);

            G2.setTransform(OLD_TRANSFORM);
        }
        
        // Draw LED if enabled
        if (isLedVisible())
        {
            G2.drawImage(getCurrentLedImage(), (int) (getInnerBounds().width * getLedPosition().getX()), (int) (getInnerBounds().height * getLedPosition().getY()), null);
        }

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
                
                G2.drawString(getLcdUnitString(), (int) (LCD.getX() + (LCD.getWidth() - UNIT_BOUNDARY.getWidth()) - LCD.getWidth() * 0.03), (int) (LCD.getY() + LCD.getHeight() * 0.76));
                
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
                    G2.drawString(Integer.toHexString((int) getLcdValue()).toUpperCase(), (int) (LCD.getX() + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76));                        
                    break;
                    
                case OCT:
                    valueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toOctalString((int) getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76));                        
                    break;
                    
                case DEC:
                    
                default:
                    valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(formatLcdValue(getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76));                        
                    break;
            }
            
            if (!getLcdInfoString().isEmpty())
            {
                G2.setFont(getLcdInfoFont());
                infoLayout = new java.awt.font.TextLayout(getLcdInfoString(), G2.getFont(), RENDER_CONTEXT);
                INFO_BOUNDARY.setFrame(infoLayout.getBounds());
                G2.drawString(getLcdInfoString(), LCD.getBounds().x + 5, LCD.getBounds().y + (int) INFO_BOUNDARY.getHeight() + 5);
            }
        }
        
        // Draw the active leds in dependence on the current value and the peak value
        // drawValue(G2, getWidth(), getHeight());
        drawValue(G2, getInnerBounds().width, getInnerBounds().height);
        
        // Draw foreground
        if (isForegroundVisible())
        {
            G2.drawImage(fImage, 0, 0, null);
        }
        
        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }

    @Override
    public void setValue(double value)
    {
        super.setValue(value);
        
        if (isValueCoupled())
        {
            setLcdValue(value);       
        }
        repaint(getInnerBounds());
    }
    
    @Override
    public eu.hansolo.steelseries.tools.ColorDef getBarGraphColor()
    {
        return this.barGraphColor;
    }

    @Override
    public void setBarGraphColor(final eu.hansolo.steelseries.tools.ColorDef BARGRAPH_COLOR)
    {
        this.barGraphColor = BARGRAPH_COLOR;        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    private void prepareBargraph(final int WIDTH, final int HEIGHT)
    {
        // Define colors of aktive leds
        if (barGraphColor != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
        {                    
            ledColors = new java.awt.Color[]
            {
                barGraphColor.LIGHT,
                barGraphColor.DARK
            };
        }
        else
        {
            ledColors = new java.awt.Color[]
            {
                getCustomBarGraphColorObject().LIGHT,
                getCustomBarGraphColorObject().DARK
            };
        }
        
        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.VERTICAL)
        {
            // VERTICAL
            led = new java.awt.geom.Rectangle2D.Double(WIDTH * 0.45, HEIGHT * 0.8519417476, WIDTH * 0.1214285714, HEIGHT * 0.0121359223);
            ledCenter = new java.awt.geom.Point2D.Double(led.getCenterX(), led.getCenterY());

            if (!getSections().isEmpty())
            {
                sectionGradients.clear();
                sectionOffsets.clear();
                
                // Vertical orientation
                double TOP =  HEIGHT * 0.12864077669902912; // position of max value
                double BOTTOM = HEIGHT * 0.8567961165048543; // position of min value
                double FULL_SIZE = BOTTOM - TOP;
                
                for (eu.hansolo.steelseries.tools.Section section : getSections())
                {
                    sectionGradients.put(section, new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * WIDTH), LED_FRACTIONS, new java.awt.Color[]{section.getColor().brighter(), section.getColor().darker()}));
                    sectionOffsets.put(section, new java.awt.geom.Point2D.Double((((section.getStart() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE), (((section.getStop() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE)));
                }
            }            
            ledGradient = new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * WIDTH), LED_FRACTIONS, ledColors);
            ledInactiveGradient = new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * WIDTH), LED_FRACTIONS, LED_INACTIVE_COLORS);
        }
        else
        {
            // HORIZONTAL
            led = new java.awt.geom.Rectangle2D.Double(WIDTH * 0.14285714285714285, HEIGHT * 0.45, WIDTH * 0.0121359223, HEIGHT * 0.1214285714);
            ledCenter = new java.awt.geom.Point2D.Double(led.getCenterX(), led.getCenterY());

            if (!getSections().isEmpty())
            {
                sectionGradients.clear();
                sectionOffsets.clear();

                // Horizontal orientation
                double TOP = WIDTH * 0.8567961165048543; // position of max value
                double BOTTOM =  WIDTH * 0.12864077669902912; // position of min value
                double FULL_SIZE = TOP - BOTTOM;

                for (eu.hansolo.steelseries.tools.Section section : getSections())
                {
                    sectionGradients.put(section, new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * WIDTH), LED_FRACTIONS, new java.awt.Color[]{section.getColor().brighter(), section.getColor().darker()}));
                    sectionOffsets.put(section, new java.awt.geom.Point2D.Double((((section.getStart() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE), (((section.getStop() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE)));
                }
            }           
            ledGradient = new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * HEIGHT), LED_FRACTIONS, ledColors);
            ledInactiveGradient = new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * HEIGHT), LED_FRACTIONS, LED_INACTIVE_COLORS);
        }
    }

    public boolean isTitleVisible()
    {
        return getModel().isTitleVisible();
    }

    public void setTitleVisible(final boolean TITLE_VISIBLE)
    {
        getModel().setTitleVisible(TITLE_VISIBLE);
        repaint(getInnerBounds());
    }

    @Override
    public boolean isTickmarksVisible()
    {
        return getModel().isTickmarksVisible();
    }

    @Override
    public void setTickmarksVisible(final boolean TICKMARKS_VISIBLE)
    {
        getModel().setTickmarksVisible(TICKMARKS_VISIBLE);
        repaint(getInnerBounds());
    }

    private void drawValue(final java.awt.Graphics2D G2, final int WIDTH, final int HEIGHT)
    {
        final boolean VERTICAL = WIDTH < HEIGHT ? true : false;
        final double TOP; // position of max value
        final double BOTTOM; // position of min value
        final double FULL_SIZE;        

        // The value background
        // Orientation dependend definitions
        if (VERTICAL)
        {
            // Vertical orientation
            TOP =  HEIGHT * 0.12864077669902912; // position of max value
            BOTTOM = HEIGHT * 0.8567961165048543; // position of min value
            FULL_SIZE = BOTTOM - TOP;

            VALUE_BACKGROUND_TRACK.setRect(WIDTH * 0.4357142857142857, TOP, WIDTH * 0.14285714285714285, FULL_SIZE);
            VALUE_BACKGROUND_VERTICAL_START.setLocation(0, VALUE_BACKGROUND_TRACK.getBounds2D().getMinY() );
            VALUE_BACKGROUND_VERTICAL_STOP.setLocation(0, VALUE_BACKGROUND_TRACK.getBounds2D().getMaxY() );
        }
        else
        {
            // Horizontal orientation
            TOP = WIDTH * 0.8567961165048543; // position of max value
            BOTTOM =  WIDTH * 0.12864077669902912; // position of min value
            FULL_SIZE = TOP - BOTTOM;

            VALUE_BACKGROUND_TRACK.setRect(WIDTH * 0.14285714285714285, HEIGHT * 0.4357142857142857, FULL_SIZE, HEIGHT * 0.14285714285714285);
            VALUE_BACKGROUND_VERTICAL_START.setLocation(TOP, 0);
            VALUE_BACKGROUND_VERTICAL_STOP.setLocation(BOTTOM, 0);
        }
                        
        final java.awt.Color[] VALUE_BACKGROUND_TRACK_COLORS =
        {
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.0470588235f),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.1450980392f),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.1490196078f),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.0470588235f)
        };
        final java.awt.LinearGradientPaint VALUE_BACKGROUND_TRACK_GRADIENT = new java.awt.LinearGradientPaint(VALUE_BACKGROUND_VERTICAL_START, VALUE_BACKGROUND_VERTICAL_STOP, VALUE_BACKGROUND_TRACK_FRACTIONS, VALUE_BACKGROUND_TRACK_COLORS);
        G2.setPaint(VALUE_BACKGROUND_TRACK_GRADIENT);
        G2.fill(VALUE_BACKGROUND_TRACK);

        // Value track border
        if (VERTICAL)
        {
            // Vertical orientation
            VALUE_LEFT_BORDER.setRect(WIDTH * 0.4357142857142857, TOP - (led.getHeight() / 2), WIDTH * 0.007142857142857143, FULL_SIZE + led.getHeight());
            VALUE_RIGHT_BORDER.setRect(WIDTH * 0.5714285714285714, TOP - (led.getHeight() / 2), WIDTH * 0.007142857142857143, FULL_SIZE + led.getHeight());
            VALUE_BORDER_START.setLocation(0, VALUE_LEFT_BORDER.getBounds2D().getMinY() );
            VALUE_BORDER_STOP.setLocation(0, VALUE_LEFT_BORDER.getBounds2D().getMaxY() );
        }
        else
        {
            // Horizontal orientation
            VALUE_LEFT_BORDER.setRect(WIDTH * 0.14285714285714285 - (led.getWidth() / 2), HEIGHT * 0.4357142857, FULL_SIZE + led.getWidth(), HEIGHT * 0.007142857142857143);
            VALUE_RIGHT_BORDER.setRect(WIDTH * 0.14285714285714285 - (led.getWidth() / 2), HEIGHT * 0.5714285714, FULL_SIZE + led.getWidth(), HEIGHT * 0.007142857142857143);
            VALUE_BORDER_START.setLocation(VALUE_LEFT_BORDER.getBounds2D().getMaxX(), 0 );
            VALUE_BORDER_STOP.setLocation(VALUE_LEFT_BORDER.getBounds2D().getMinX(), 0 );
        }        
        final java.awt.Color[] VALUE_BORDER_COLORS =
        {
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.2980392157f),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.6862745098f),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.6980392157f),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0.4f)
        };
        final java.awt.LinearGradientPaint VALUE_BORDER_GRADIENT = new java.awt.LinearGradientPaint(VALUE_BORDER_START, VALUE_BORDER_STOP, VALUE_BORDER_FRACTIONS, VALUE_BORDER_COLORS);
        G2.setPaint(VALUE_BORDER_GRADIENT);
        G2.fill(VALUE_LEFT_BORDER);
        G2.fill(VALUE_RIGHT_BORDER);

        if (VERTICAL)
        {
            // Vertical orientation

            // Draw the inactive leds
            final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
            G2.setPaint(ledInactiveGradient);
            final double INACTIVE_LEDS = ((getMaxValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE;
            for (double translateY = 0 ; Double.compare(translateY, INACTIVE_LEDS) <= 0 ; translateY += led.getHeight() + 1)
            {
                G2.translate(0, -translateY);
                G2.fill(led);
                G2.setTransform(OLD_TRANSFORM);
            }

            // Draw the active leds in dependence on the current value
            if (!isSectionsVisible())
            {
                G2.setPaint(ledGradient);
            }
            final double ACTIVE_LEDS = ((getValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE;
            if (!getModel().isSingleLedBargraphEnabled())
            {
                for (double translateY = 0 ; Double.compare(translateY, ACTIVE_LEDS) <= 0 ; translateY += led.getHeight() + 1)
                {
                    // If sections visible, color the bargraph with the given section colors
                    if (isSectionsVisible())
                    {
                        for (eu.hansolo.steelseries.tools.Section section : getSections())
                        {
                            if (translateY >= sectionOffsets.get(section).getX() && translateY < sectionOffsets.get(section).getY())
                            {
                                G2.setPaint(sectionGradients.get(section));
                            }
                        }
                    }
                    G2.translate(0, -translateY);
                    G2.fill(led);
                    G2.setTransform(OLD_TRANSFORM);
                }
            }
            else
            { // Draw only single led in bargraph
                final double TRANSLATE_Y = -((getValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue()) * FULL_SIZE);
                if (isSectionsVisible())
                {
                    for (eu.hansolo.steelseries.tools.Section section : getSections())
                    {
                        if (TRANSLATE_Y >= sectionOffsets.get(section).getX() && TRANSLATE_Y < sectionOffsets.get(section).getY())
                        {
                            G2.setPaint(sectionGradients.get(section));
                        }
                    }
                }
                G2.translate(0, -TRANSLATE_Y);
                G2.fill(led);
                G2.setTransform(OLD_TRANSFORM);
            }
            
            // Draw peak value if enabled
            if (isPeakValueEnabled() && isPeakValueVisible())
            {
                G2.translate(0, -((getPeakValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue()) * FULL_SIZE));
                G2.fill(led);
                G2.setTransform(OLD_TRANSFORM);
            }
        }
        else
        {
            // Horizontal orientation

            // Draw the inactive leds
            final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
            G2.setPaint(ledInactiveGradient);
            final double INACTIVE_LEDS = ((getMaxValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE;
            for (double translateX = -(led.getWidth() / 2) ; Double.compare(translateX, INACTIVE_LEDS) <= 0 ; translateX += led.getWidth() + 1)
            {
                G2.translate(translateX, 0);
                G2.fill(led);
                G2.setTransform(OLD_TRANSFORM);
            }

            // Draw the active leds in dependence on the current value
            if (!isSectionsVisible())
            {
                G2.setPaint(ledGradient);
            }
            final double ACTIVE_LEDS = ((getValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE;
            if (!getModel().isSingleLedBargraphEnabled())
            {
                for (double translateX = -(led.getWidth() / 2) ; Double.compare(translateX, ACTIVE_LEDS) <= 0 ; translateX += led.getWidth() + 1)
                {
                    // If sections visible, color the bargraph with the given section colors
                    if (isSectionsVisible())
                    {
                        for (eu.hansolo.steelseries.tools.Section section : getSections())
                        {
                            if (translateX+(led.getWidth() / 2) >= sectionOffsets.get(section).getX() && translateX+(led.getWidth() / 2) < sectionOffsets.get(section).getY())
                            {
                                G2.setPaint(sectionGradients.get(section));
                            }
                        }
                    }

                    G2.translate(translateX, 0);
                    G2.fill(led);
                    G2.setTransform(OLD_TRANSFORM);
                }
            }
            else
            {   // Draw only one led
                final double TRANSLATE_X = (((getValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE);
                if (isSectionsVisible())
                {
                    for (eu.hansolo.steelseries.tools.Section section : getSections())
                    {
                        if (TRANSLATE_X+(led.getWidth() / 2) >= sectionOffsets.get(section).getX() && TRANSLATE_X+(led.getWidth() / 2) < sectionOffsets.get(section).getY())
                        {
                            G2.setPaint(sectionGradients.get(section));
                        }
                    }
                }
                G2.translate(TRANSLATE_X, 0);
                G2.fill(led);
                G2.setTransform(OLD_TRANSFORM);
            }

            // Draw peak value if enabled
            if (isPeakValueEnabled() && isPeakValueVisible())
            {
                G2.translate((((getPeakValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * FULL_SIZE), 0);
                G2.fill(led);
                G2.setTransform(OLD_TRANSFORM);
            }
        }
    }

    @Override
    protected java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(bImage.getWidth() / 2.0 + getInnerBounds().x, bImage.getHeight() / 2.0 + getInnerBounds().y);
    }

    @Override
    protected java.awt.geom.Rectangle2D getBounds2D()
    {
        return new java.awt.geom.Rectangle2D.Double(bImage.getMinX(), bImage.getMinY(), bImage.getWidth(), bImage.getHeight());
    }

    @Override
    public String toString()
    {
        return "LinearBargraph";
    }

}
