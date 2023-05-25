package eu.hansolo.steelseries.gauges;


public class DigitalRadial extends AbstractRadial
{            
    private int noOfActiveLeds = 0;    
    // One image to reduce memory consumption
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
        
    private java.awt.image.BufferedImage disabledImage;
    private java.awt.image.BufferedImage ledGreenOff = UTIL.createImage(24, 24, java.awt.Transparency.TRANSLUCENT);
    private java.awt.image.BufferedImage ledYellowOff = UTIL.createImage(24, 24, java.awt.Transparency.TRANSLUCENT);
    private java.awt.image.BufferedImage ledRedOff = UTIL.createImage(24, 24, java.awt.Transparency.TRANSLUCENT);
    private java.awt.image.BufferedImage ledGreenOn = UTIL.createImage(24, 24, java.awt.Transparency.TRANSLUCENT);
    private java.awt.image.BufferedImage ledYellowOn = UTIL.createImage(24, 24, java.awt.Transparency.TRANSLUCENT);
    private java.awt.image.BufferedImage ledRedOn = UTIL.createImage(24, 24, java.awt.Transparency.TRANSLUCENT);    
    private java.awt.Color valueColor = new java.awt.Color(255, 0, 0, 255);
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();    
    private java.awt.Point[] ledPosition;            
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();    
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();


    public DigitalRadial()
    {
        super();
        setUnitString("");                  
    }
        
    @Override
    public final AbstractGauge init(final int WIDTH, final int HEIGHT)
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
           
        if (isLcdVisible())
        {
            create_LCD_Image(new java.awt.geom.Rectangle2D.Double(((getGaugeBounds().width - WIDTH * 0.48) / 2.0), (getGaugeBounds().height * 0.425), (WIDTH * 0.48), (WIDTH * 0.15)), getLcdColor(), getCustomLcdBackground(), bImage);
            LCD.setRect(((getGaugeBounds().width - WIDTH * 0.4) / 2.0), (getGaugeBounds().height * 0.55), WIDTH * 0.48, WIDTH * 0.15);
        }
        
        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH);
        
        ledPosition = new java.awt.Point[]
        {
            // LED 1
            new java.awt.Point((int) (WIDTH * 0.186915887850467), (int)(WIDTH *0.649532710280374)),
            // LED 2
            new java.awt.Point((int) (WIDTH * 0.116822429906542), (int)(WIDTH *0.546728971962617)),
            // LED 3
            new java.awt.Point((int) (WIDTH * 0.088785046728972), (int)(WIDTH *0.41588785046729)),
            // LED 4
            new java.awt.Point((int) (WIDTH * 0.116822429906542), (int)(WIDTH *0.285046728971963)),
            // LED 5
            new java.awt.Point((int) (WIDTH * 0.177570093457944), (int)(WIDTH *0.182242990654206)),
            // LED 6
            new java.awt.Point((int) (WIDTH * 0.280373831775701), (int)(WIDTH *0.117222429906542)),
            // LED 7
            new java.awt.Point((int) (WIDTH * 0.411214953271028), (int)(WIDTH *0.0794392523364486)),
            // LED 8
            new java.awt.Point((int) (WIDTH * 0.542056074766355), (int)(WIDTH *0.117222429906542)),
            // LED 9
            new java.awt.Point((int) (WIDTH * 0.649532710280374), (int)(WIDTH *0.182242990654206)),
            // LED 10
            new java.awt.Point((int) (WIDTH * 0.719626168224299), (int)(WIDTH *0.285046728971963)),
            // LED 11
            new java.awt.Point((int) (WIDTH * 0.738317757009346), (int)(WIDTH *0.41588785046729)),
            // LED 12
            new java.awt.Point((int) (WIDTH * 0.710280373831776), (int)(WIDTH *0.546728971962617)),
            // LED 13
            new java.awt.Point((int) (WIDTH * 0.64018691588785), (int)(WIDTH *0.649532710280374))
        };
        ledGreenOff.flush();
        ledGreenOff = create_LED_OFF_Image(WIDTH, eu.hansolo.steelseries.tools.LedColor.GREEN);
        ledYellowOff.flush();
        ledYellowOff = create_LED_OFF_Image(WIDTH, eu.hansolo.steelseries.tools.LedColor.YELLOW);
        ledRedOff.flush();
        ledRedOff = create_LED_OFF_Image(WIDTH, eu.hansolo.steelseries.tools.LedColor.RED);
        ledGreenOn.flush();
        ledGreenOn = create_LED_ON_Image(WIDTH, eu.hansolo.steelseries.tools.LedColor.GREEN);
        ledYellowOn.flush();
        ledYellowOn = create_LED_ON_Image(WIDTH, eu.hansolo.steelseries.tools.LedColor.YELLOW);
        ledRedOn.flush();
        ledRedOn = create_LED_ON_Image(WIDTH, eu.hansolo.steelseries.tools.LedColor.RED);

        return this;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        if (!isInitialized())
        {            
            return;
        }
        
        super.paintComponent(g);
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);

        for (int i = 0 ; i < 13 ; i++)
        {
            if (i < 7)
            {
                if (i < noOfActiveLeds)
                {
                    G2.drawImage(ledGreenOn, ledPosition[i].x, ledPosition[i].y, null);
                }
                else
                {
                    G2.drawImage(ledGreenOff, ledPosition[i].x, ledPosition[i].y, null);
                }
            }

            if (i >= 7 && i < 12)
            {
                if (i < noOfActiveLeds)
                {
                    G2.drawImage(ledYellowOn, ledPosition[i].x, ledPosition[i].y, null);
                }
                else
                {
                    G2.drawImage(ledYellowOff, ledPosition[i].x, ledPosition[i].y, null);
                }
            }

            if (i == 12)
            {
                if (i < noOfActiveLeds)
                {
                    G2.drawImage(ledRedOn, ledPosition[i].x, ledPosition[i].y, null);
                }
                else
                {
                    G2.drawImage(ledRedOff, ledPosition[i].x, ledPosition[i].y, null);
                }
            }
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
                G2.drawString(getLcdUnitString(), (int) (((getGaugeBounds().width - LCD.getWidth()) / 2.0) + (LCD.getWidth() - UNIT_BOUNDARY.getWidth()) - LCD.getWidth() * 0.03), (int) ((getGaugeBounds().height * 0.425) + LCD.getHeight() * 0.76));
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
                    G2.drawString(Integer.toHexString((int) getLcdValue()).toUpperCase(), (int) (((getGaugeBounds().width - LCD.getWidth()) / 2.0) + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) ((getGaugeBounds().height * 0.425) + LCD.getHeight() * 0.76));
                    break;
                    
                case OCT:
                    valueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toOctalString((int) getLcdValue()), (int) (((getGaugeBounds().width - LCD.getWidth()) / 2.0) + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) ((getGaugeBounds().height * 0.425) + LCD.getHeight() * 0.76));
                    break;
                    
                case DEC:
                    
                default:
                    valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(formatLcdValue(getLcdValue()), (int) (((getGaugeBounds().width - LCD.getWidth()) / 2.0) + (LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) ((getGaugeBounds().height * 0.425) + LCD.getHeight() * 0.76));
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
        
        // Draw combined foreground image
        G2.drawImage(fImage, 0, 0, null);

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

        // Set active leds relating to the new value
        calcNoOfActiveLed();
                
        if (isValueCoupled())
        {
            setLcdValue(value);
        }
        repaint(getInnerBounds());
    }

    @Override
    public void setMinValue(final double MIN_VALUE)
    {
        super.setMinValue(MIN_VALUE);
        calcNoOfActiveLed();
        repaint(getInnerBounds());
    }

    @Override
    public void setMaxValue(final double MAX_VALUE)
    {
        super.setMaxValue(MAX_VALUE);
        calcNoOfActiveLed();
        repaint(getInnerBounds());
    }

    private void calcNoOfActiveLed()
    {
        noOfActiveLeds = (int) (13 / (getMaxValue() - getMinValue()) * getValue());
    }

    public java.awt.Color getValueColor()
    {
        return this.valueColor;
    }

    public void setValueColor(final java.awt.Color VALUE_COLOR)
    {
        this.valueColor = VALUE_COLOR;
        repaint(getInnerBounds());
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

    private java.awt.image.BufferedImage create_LED_OFF_Image(final int WIDTH, final eu.hansolo.steelseries.tools.LedColor LED_COLOR)
    {
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage((int) (WIDTH * 0.1775700935), (int) (WIDTH * 0.1775700935), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        // Led background
        final java.awt.geom.Ellipse2D E_LED1_BG = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.21052631735801697, IMAGE_HEIGHT * 0.21052631735801697, IMAGE_WIDTH * 0.5526316165924072, IMAGE_HEIGHT * 0.5526316165924072);
        final java.awt.geom.Point2D E_LED1_BG_START = new java.awt.geom.Point2D.Double(0, E_LED1_BG.getBounds2D().getMinY() );
        final java.awt.geom.Point2D E_LED1_BG_STOP = new java.awt.geom.Point2D.Double(0, E_LED1_BG.getBounds2D().getMaxY() );
        final float[] E_LED1_BG_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] E_LED1_BG_COLORS =
        {
            new java.awt.Color(0, 0, 0, 229),
            new java.awt.Color(153, 153, 153, 255)
        };        
        final java.awt.LinearGradientPaint E_LED1_BG_GRADIENT = new java.awt.LinearGradientPaint(E_LED1_BG_START, E_LED1_BG_STOP, E_LED1_BG_FRACTIONS, E_LED1_BG_COLORS);
        G2.setPaint(E_LED1_BG_GRADIENT);
        G2.fill(E_LED1_BG);

        // Led foreground
        final java.awt.geom.Ellipse2D LED_FG = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.2368421107530594, IMAGE_HEIGHT * 0.2368421107530594, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5);
        final java.awt.geom.Point2D LED_FG_CENTER = new java.awt.geom.Point2D.Double(LED_FG.getCenterX(), LED_FG.getCenterY());
        final float[] LED_FG_FRACTIONS =
        {
            0.0f,
            0.14f,
            0.15f,
            1.0f
        };
        java.awt.Color[] ledFgColors;

        switch(LED_COLOR)
        {
            case GREEN:              
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(28, 126, 0, 255),
                    new java.awt.Color(28, 126, 0, 255),
                    new java.awt.Color(28, 126, 0, 255),
                    new java.awt.Color(27, 100, 0, 255)
                };
                break;

            case YELLOW:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(164, 128, 8, 255),
                    new java.awt.Color(158, 125, 10, 255),
                    new java.awt.Color(158, 125, 10, 255),
                    new java.awt.Color(130, 96, 25, 255)
                };
                break;

            case RED:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(248, 0, 0, 255),
                    new java.awt.Color(248, 0, 0, 255),
                    new java.awt.Color(248, 0, 0, 255),
                    new java.awt.Color(63, 0, 0, 255)
                };
                break;

            default:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(28, 126, 0, 255),
                    new java.awt.Color(28, 126, 0, 255),
                    new java.awt.Color(28, 126, 0, 255),
                    new java.awt.Color(27, 100, 0, 255)
                };
                break;
        }

        final java.awt.RadialGradientPaint LED_FG_GRADIENT = new java.awt.RadialGradientPaint(LED_FG_CENTER, 0.25f * IMAGE_WIDTH, LED_FG_FRACTIONS, ledFgColors);
        G2.setPaint(LED_FG_GRADIENT);
        G2.fill(LED_FG);

        // Led inner shadow
        final java.awt.geom.Ellipse2D E_LED1_INNERSHADOW = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.2368421107530594, IMAGE_HEIGHT * 0.2368421107530594, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5);
        final java.awt.geom.Point2D E_LED1_INNERSHADOW_CENTER = new java.awt.geom.Point2D.Double( (0.47368421052631576 * IMAGE_WIDTH), (0.47368421052631576 * IMAGE_HEIGHT) );
        final float[] E_LED1_INNERSHADOW_FRACTIONS =
        {
            0.0f,
            0.86f,
            1.0f
        };
        final java.awt.Color[] E_LED1_INNERSHADOW_COLORS =
        {
            new java.awt.Color(0, 0, 0, 0),
            new java.awt.Color(0, 0, 0, 88),
            new java.awt.Color(0, 0, 0, 102)
        };
        final java.awt.RadialGradientPaint E_LED1_INNERSHADOW_GRADIENT = new java.awt.RadialGradientPaint(E_LED1_INNERSHADOW_CENTER, (float)(0.25 * IMAGE_WIDTH), E_LED1_INNERSHADOW_FRACTIONS, E_LED1_INNERSHADOW_COLORS);
        G2.setPaint(E_LED1_INNERSHADOW_GRADIENT);
        G2.fill(E_LED1_INNERSHADOW);

        // Led highlight
        final java.awt.geom.Ellipse2D E_LED1_HL = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.3947368562221527, IMAGE_HEIGHT * 0.31578946113586426, IMAGE_WIDTH * 0.21052631735801697, IMAGE_HEIGHT * 0.1315789520740509);
        final java.awt.geom.Point2D E_LED1_HL_START = new java.awt.geom.Point2D.Double(0, E_LED1_HL.getBounds2D().getMinY() );
        final java.awt.geom.Point2D E_LED1_HL_STOP = new java.awt.geom.Point2D.Double(0, E_LED1_HL.getBounds2D().getMaxY() );
        final float[] E_LED1_HL_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] E_LED1_HL_COLORS =
        {
            new java.awt.Color(255, 255, 255, 102),
            new java.awt.Color(255, 255, 255, 0)
        };
        final java.awt.LinearGradientPaint E_LED1_HL_GRADIENT = new java.awt.LinearGradientPaint(E_LED1_HL_START, E_LED1_HL_STOP, E_LED1_HL_FRACTIONS, E_LED1_HL_COLORS);
        G2.setPaint(E_LED1_HL_GRADIENT);
        G2.fill(E_LED1_HL);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_LED_ON_Image(final int WIDTH, final eu.hansolo.steelseries.tools.LedColor LED_COLOR)
    {        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage((int) (WIDTH * 0.1775700935), (int) (WIDTH * 0.1775700935), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        // Led background
        final java.awt.geom.Ellipse2D E_LED1_BG = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.21052631735801697, IMAGE_HEIGHT * 0.21052631735801697, IMAGE_WIDTH * 0.5526316165924072, IMAGE_HEIGHT * 0.5526316165924072);
        final java.awt.geom.Point2D E_LED1_BG_START = new java.awt.geom.Point2D.Double(0, E_LED1_BG.getBounds2D().getMinY() );
        final java.awt.geom.Point2D E_LED1_BG_STOP = new java.awt.geom.Point2D.Double(0, E_LED1_BG.getBounds2D().getMaxY() );
        final float[] E_LED1_BG_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] E_LED1_BG_COLORS =
        {
            new java.awt.Color(0, 0, 0, 229),
            new java.awt.Color(153, 153, 153, 255)
        };
        final java.awt.LinearGradientPaint E_LED1_BG_GRADIENT = new java.awt.LinearGradientPaint(E_LED1_BG_START, E_LED1_BG_STOP, E_LED1_BG_FRACTIONS, E_LED1_BG_COLORS);
        G2.setPaint(E_LED1_BG_GRADIENT);
        G2.fill(E_LED1_BG);

        // Led glow
        final java.awt.geom.Ellipse2D LED_GLOW = new java.awt.geom.Ellipse2D.Double(0.0, 0.0, IMAGE_WIDTH, IMAGE_HEIGHT);
        final java.awt.geom.Point2D LED_GLOW_CENTER = new java.awt.geom.Point2D.Double(LED_GLOW.getCenterX(), LED_GLOW.getCenterY());
        final float[] LED_GLOW_FRACTIONS =
        {
            0.0f,
            0.57f,
            0.71f,
            0.72f,
            0.85f,
            0.93f,
            0.9301f,
            0.99f
        };
        java.awt.Color[] ledGlowColors;

        switch(LED_COLOR)
        {
            case GREEN:
                ledGlowColors = new java.awt.Color[]
                {
                    new java.awt.Color(165, 255, 0, 255),
                    new java.awt.Color(165, 255, 0, 101),
                    new java.awt.Color(165, 255, 0, 63),
                    new java.awt.Color(165, 255, 0, 62),
                    new java.awt.Color(165, 255, 0, 31),
                    new java.awt.Color(165, 255, 0, 13),
                    new java.awt.Color(165, 255, 0, 12),
                    new java.awt.Color(165, 255, 0, 0)
                };
                break;

            case YELLOW:
                ledGlowColors = new java.awt.Color[]
                {
                    new java.awt.Color(255, 102, 0, 255),
                    new java.awt.Color(255, 102, 0, 101),
                    new java.awt.Color(255, 102, 0, 63),
                    new java.awt.Color(255, 102, 0, 62),
                    new java.awt.Color(255, 102, 0, 31),
                    new java.awt.Color(255, 102, 0, 13),
                    new java.awt.Color(255, 102, 0, 12),
                    new java.awt.Color(255, 102, 0, 0)
                };
                break;

            case RED:
                ledGlowColors = new java.awt.Color[]
                {
                    new java.awt.Color(255, 0, 0, 255),
                    new java.awt.Color(255, 0, 0, 101),
                    new java.awt.Color(255, 0, 0, 63),
                    new java.awt.Color(255, 0, 0, 62),
                    new java.awt.Color(255, 0, 0, 31),
                    new java.awt.Color(255, 0, 0, 13),
                    new java.awt.Color(255, 0, 0, 12),
                    new java.awt.Color(255, 0, 0, 0)
                };
                break;

            default:
                ledGlowColors = new java.awt.Color[]
                {
                    new java.awt.Color(165, 255, 0, 255),
                    new java.awt.Color(165, 255, 0, 101),
                    new java.awt.Color(165, 255, 0, 63),
                    new java.awt.Color(165, 255, 0, 62),
                    new java.awt.Color(165, 255, 0, 31),
                    new java.awt.Color(165, 255, 0, 13),
                    new java.awt.Color(165, 255, 0, 12),
                    new java.awt.Color(165, 255, 0, 0)
                };
                break;
        }
        final java.awt.RadialGradientPaint LED_GLOW_GRADIENT = new java.awt.RadialGradientPaint(LED_GLOW_CENTER, 0.5f * IMAGE_WIDTH, LED_GLOW_FRACTIONS, ledGlowColors);
        G2.setPaint(LED_GLOW_GRADIENT);
        G2.fill(LED_GLOW);

        // Led foreground
        final java.awt.geom.Ellipse2D LED_FG = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.2368421107530594, IMAGE_HEIGHT * 0.2368421107530594, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5);
        final java.awt.geom.Point2D LED_FG_CENTER = new java.awt.geom.Point2D.Double(LED_FG.getCenterX(), LED_FG.getCenterY());
        final float[] LED_FG_FRACTIONS =
        {
            0.0f,
            0.14f,
            0.15f,
            1.0f
        };
        java.awt.Color[] ledFgColors;

        switch(LED_COLOR)
        {
            case GREEN:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(154, 255, 137, 255),
                    new java.awt.Color(154, 255, 137, 255),
                    new java.awt.Color(154, 255, 137, 255),
                    new java.awt.Color(89, 255, 42, 255)
                };
                break;

            case YELLOW:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(251, 255, 140, 255),
                    new java.awt.Color(251, 255, 140, 255),
                    new java.awt.Color(251, 255, 140, 255),
                    new java.awt.Color(250, 249, 60, 255)
                };
                break;

            case RED:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(252, 53, 55, 255),
                    new java.awt.Color(252, 53, 55, 255),
                    new java.awt.Color(252, 53, 55, 255),
                    new java.awt.Color(255, 0, 0, 255)
                };
                break;

            default:
                ledFgColors = new java.awt.Color[]
                {
                    new java.awt.Color(154, 255, 137, 255),
                    new java.awt.Color(154, 255, 137, 255),
                    new java.awt.Color(154, 255, 137, 255),
                    new java.awt.Color(89, 255, 42, 255)
                };
                break;
        }
        
        final java.awt.RadialGradientPaint LED_FG_GRADIENT = new java.awt.RadialGradientPaint(LED_FG_CENTER, 0.25f * IMAGE_WIDTH, LED_FG_FRACTIONS, ledFgColors);
        G2.setPaint(LED_FG_GRADIENT);
        G2.fill(LED_FG);

        // Led inner shadow
        final java.awt.geom.Ellipse2D E_LED1_INNERSHADOW = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.2368421107530594, IMAGE_HEIGHT * 0.2368421107530594, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5);
        final java.awt.geom.Point2D E_LED1_INNERSHADOW_CENTER = new java.awt.geom.Point2D.Double( (0.47368421052631576 * IMAGE_WIDTH), (0.47368421052631576 * IMAGE_HEIGHT) );
        final float[] E_LED1_INNERSHADOW_FRACTIONS =
        {
            0.0f,
            0.86f,
            1.0f
        };
        final java.awt.Color[] E_LED1_INNERSHADOW_COLORS =
        {
            new java.awt.Color(0, 0, 0, 0),
            new java.awt.Color(0, 0, 0, 88),
            new java.awt.Color(0, 0, 0, 102)
        };
        final java.awt.RadialGradientPaint E_LED1_INNERSHADOW_GRADIENT = new java.awt.RadialGradientPaint(E_LED1_INNERSHADOW_CENTER, (float)(0.25 * IMAGE_WIDTH), E_LED1_INNERSHADOW_FRACTIONS, E_LED1_INNERSHADOW_COLORS);
        G2.setPaint(E_LED1_INNERSHADOW_GRADIENT);
        G2.fill(E_LED1_INNERSHADOW);

        // Led highlight
        final java.awt.geom.Ellipse2D E_LED1_HL = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.3947368562221527, IMAGE_HEIGHT * 0.31578946113586426, IMAGE_WIDTH * 0.21052631735801697, IMAGE_HEIGHT * 0.1315789520740509);
        final java.awt.geom.Point2D E_LED1_HL_START = new java.awt.geom.Point2D.Double(0, E_LED1_HL.getBounds2D().getMinY() );
        final java.awt.geom.Point2D E_LED1_HL_STOP = new java.awt.geom.Point2D.Double(0, E_LED1_HL.getBounds2D().getMaxY() );
        final float[] E_LED1_HL_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] E_LED1_HL_COLORS =
        {
            new java.awt.Color(255, 255, 255, 102),
            new java.awt.Color(255, 255, 255, 0)
        };
        final java.awt.LinearGradientPaint E_LED1_HL_GRADIENT = new java.awt.LinearGradientPaint(E_LED1_HL_START, E_LED1_HL_STOP, E_LED1_HL_FRACTIONS, E_LED1_HL_COLORS);
        G2.setPaint(E_LED1_HL_GRADIENT);
        G2.fill(E_LED1_HL);

        G2.dispose();

        return IMAGE;
    }

    @Override
    public String toString()
    {
        return "DigitalRadial";
    }
}