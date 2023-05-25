package eu.hansolo.steelseries.gauges;

/**
 *
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class RadialBargraph extends AbstractRadialBargraph
{    
    private double ledTrackStartAngle;
    private double ledTrackAngleExtend;    
    
    // One image to reduce memory consumption
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage disabledImage;
    private eu.hansolo.steelseries.tools.ColorDef barGraphColor;
    private java.awt.geom.Point2D center;
    private java.awt.geom.Rectangle2D led;
    private java.awt.geom.Point2D ledCenter;
    private java.awt.Color[] ledColors;
    private final float[] LED_FRACTIONS;
    private java.awt.RadialGradientPaint ledGradient;
    private java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.RadialGradientPaint> sectionGradients;
    private java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.geom.Point2D> sectionAngles;
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    
    public RadialBargraph()
    {
        super();                                                                         
        barGraphColor = eu.hansolo.steelseries.tools.ColorDef.RED;
        LED_FRACTIONS = new float[]
        {
            0.0f,
            1.0f
        };
        sectionGradients = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.RadialGradientPaint>(4);
        sectionAngles = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.geom.Point2D>(4);
                
        ledTrackStartAngle = getGaugeType().ORIGIN_CORRECTION - (0 * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue())));
        ledTrackAngleExtend = -(getMaxValue() - getMinValue()) * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue()));        
        calcBargraphTrack();
        prepareBargraph(getInnerBounds().width);        
        setLcdVisible(true);
    }
    
    public RadialBargraph(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        super();
        setModel(MODEL);               
                                                        
        barGraphColor = eu.hansolo.steelseries.tools.ColorDef.RED;
        LED_FRACTIONS = new float[]
        {
            0.0f,
            1.0f
        };
        sectionGradients = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.RadialGradientPaint>(4);
        sectionAngles = new java.util.HashMap<eu.hansolo.steelseries.tools.Section, java.awt.geom.Point2D>(4);
                
        ledTrackStartAngle = getGaugeType().ORIGIN_CORRECTION - (0 * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue())));
        ledTrackAngleExtend = -(getMaxValue() - getMinValue()) * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue()));        
        calcBargraphTrack();
        prepareBargraph(getInnerBounds().width);        
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
        
        ledTrackStartAngle = getGaugeType().ORIGIN_CORRECTION - (0 * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue())));
        ledTrackAngleExtend = -(getMaxValue() - getMinValue()) * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue())); 
        
        calcBargraphTrack();
        prepareBargraph(WIDTH);
                
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

        create_BARGRAPH_TRACK_Image(WIDTH, ledTrackStartAngle, ledTrackAngleExtend, getGaugeType().APEX_ANGLE, getGaugeType().BARGRAPH_OFFSET, bImage);
        
        create_TITLE_Image(WIDTH, getTitle(), getUnitString(), bImage);
        
        if (isLcdVisible())
        {            
            create_LCD_Image(new java.awt.geom.Rectangle2D.Double(((getGaugeBounds().width - WIDTH * 0.48) / 2.0), (getGaugeBounds().height * 0.425), (WIDTH * 0.48), (WIDTH * 0.15)), getLcdColor(), getCustomLcdBackground(), bImage);                
            LCD.setRect(((getGaugeBounds().width - WIDTH * 0.48) / 2.0), (getGaugeBounds().height * 0.425), WIDTH * 0.48, WIDTH * 0.15);                     
        } 
          
        TICKMARK_FACTORY.create_RADIAL_TICKMARKS_Image(WIDTH, 
                                                getModel().getNiceMinValue(), 
                                                getModel().getNiceMaxValue(),                                                                                   
                                                getModel().getMaxNoOfMinorTicks(),
                                                getModel().getMaxNoOfMajorTicks(),
                                                getModel().getMinorTickSpacing(),
                                                getModel().getMajorTickSpacing(),
                                                getGaugeType(),
                                                getMinorTickmarkType(),
                                                getMajorTickmarkType(),
                                                false,
                                                isTicklabelsVisible(),
                                                false,
                                                false,
                                                getLabelNumberFormat(),
                                                isTickmarkSectionsVisible(),
                                                getBackgroundColor(),
                                                getTickmarkColor(),
                                                isTickmarkColorFromThemeEnabled(),
                                                getTickmarkSections(),
                                                0.38f,
                                                0.09f,
                                                getCenter(),
                                                new java.awt.geom.Point2D.Double(0, 0),
                                                eu.hansolo.steelseries.tools.Orientation.NORTH,
                                                getModel().isNiceScale(),
                                                bImage);
        
        if (isForegroundVisible())
        {            
            FOREGROUND_FACTORY.createRadialForeground(WIDTH, false, getForegroundType(), fImage);
        }

        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH);
        
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

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Translate coordinate system related to insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);
        
        // Draw an Arc2d object that will visualize the range of measured values
        if (isRangeOfMeasuredValuesVisible())
        {
            G2.setPaint(getModel().getRangeOfMeasuredValuesPaint());                        
            if (getGaugeType() == eu.hansolo.steelseries.tools.GaugeType.TYPE3 || getGaugeType() == eu.hansolo.steelseries.tools.GaugeType.TYPE4)
            {
                final java.awt.geom.Area area = new java.awt.geom.Area(getModel().getRadialRangeOfMeasuredValues());                
                area.subtract(new java.awt.geom.Area(LCD));
                G2.fill(area);
            }                        
            else
            {
                G2.fill(getModel().getRadialRangeOfMeasuredValues());
            }
        }
        
        // Draw LED if enabled
        if (isLedVisible())
        {
            G2.drawImage(getCurrentLedImage(), (int) (getGaugeBounds().width * getLedPosition().getX()), (int) (getGaugeBounds().height * getLedPosition().getY()), null);
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
            // Draw lcd info string
            if (!getLcdInfoString().isEmpty())
            {
                G2.setFont(getLcdInfoFont());
                infoLayout = new java.awt.font.TextLayout(getLcdInfoString(), G2.getFont(), RENDER_CONTEXT);
                INFO_BOUNDARY.setFrame(infoLayout.getBounds());
                G2.drawString(getLcdInfoString(), LCD.getBounds().x + 5, LCD.getBounds().y + (int) INFO_BOUNDARY.getHeight() + 5);
            }      
        }
        
        // Draw the active leds in dependence on the current value        
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        
        final double ACTIVE_LED_ANGLE = ((getValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * getGaugeType().APEX_ANGLE;        

        // Use default bargraph color if sections not visible
        if (!isSectionsVisible())
        {
            G2.setPaint(ledGradient);
        }
        
        if (!getModel().isSingleLedBargraphEnabled())
        {
            for (double angle = 0 ; Double.compare(angle, ACTIVE_LED_ANGLE) <= 0 ; angle += 5.0)
            {
                // If sections visible, color the bargraph with the given section colors
                if (isSectionsVisible())
                {
                    for (eu.hansolo.steelseries.tools.Section section : getSections())
                    {
                        if (Double.compare(angle, sectionAngles.get(section).getX()) >= 0 && angle < sectionAngles.get(section).getY())
                        {
                            G2.setPaint(sectionGradients.get(section));
                        }
                    }
                }            
                G2.rotate(Math.toRadians(angle + getGaugeType().BARGRAPH_OFFSET), center.getX(), center.getY());        
                G2.fill(led);        
                G2.setTransform(OLD_TRANSFORM);
            }
        }
        else
        {   // Draw only one led instead of all active leds
            final double ANGLE = Math.toRadians(((getValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * getGaugeType().APEX_ANGLE);
            if (isSectionsVisible())
            {
                for (eu.hansolo.steelseries.tools.Section section : getSections())
                {
                    if (Double.compare(ANGLE, sectionAngles.get(section).getX()) >= 0 && ANGLE < sectionAngles.get(section).getY())
                    {
                        G2.setPaint(sectionGradients.get(section));
                    }
                }
            }
            G2.rotate(ANGLE, center.getX(), center.getY());
            G2.fill(led);
            G2.setTransform(OLD_TRANSFORM);
        }
        
        // Draw peak value if enabled
        if (isPeakValueEnabled() && isPeakValueVisible())
        {
            G2.rotate(Math.toRadians(((getPeakValue() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * getGaugeType().APEX_ANGLE + getGaugeType().BARGRAPH_OFFSET), center.getX(), center.getY());
            G2.fill(led);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw the foreground
        if (isForegroundVisible())
        {
            G2.drawImage(fImage, 0, 0, null);
        }

        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate coordinate system back to original
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
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

    private void prepareBargraph(final int WIDTH)
    {
        center = new java.awt.geom.Point2D.Double(WIDTH / 2.0 + getInnerBounds().x, WIDTH / 2.0 + getInnerBounds().y);
        led = new java.awt.geom.Rectangle2D.Double(WIDTH * 0.1168224299, WIDTH * 0.4859813084, WIDTH * 0.06074766355140187, WIDTH * 0.023364486);
        ledCenter = new java.awt.geom.Point2D.Double(led.getCenterX(), led.getCenterY());

        if (!getSections().isEmpty())
        {
            sectionGradients.clear();
            sectionAngles.clear();
            for (eu.hansolo.steelseries.tools.Section section : getSections())
            {
                sectionGradients.put(section, new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * WIDTH), LED_FRACTIONS, new java.awt.Color[]{section.getColor().brighter(), section.getColor().darker()}));
                sectionAngles.put(section, new java.awt.geom.Point2D.Double((((section.getStart() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * getGaugeType().APEX_ANGLE), (((section.getStop() + Math.abs(getMinValue())) / (getMaxValue() - getMinValue())) * getGaugeType().APEX_ANGLE)));
            }
        }

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

        ledGradient = new java.awt.RadialGradientPaint(ledCenter, (float)(0.030373831775700934 * WIDTH), LED_FRACTIONS, ledColors);
    }

    private void calcBargraphTrack()
    {
        ledTrackStartAngle = getGaugeType().ORIGIN_CORRECTION;
        ledTrackAngleExtend = -(getMaxValue() - getMinValue()) * (getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue()));                
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
    public java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(getInnerBounds().getCenterX() + getInnerBounds().x, getInnerBounds().getCenterX() + getInnerBounds().y);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return getInnerBounds();
    }
        
    @Override
    public String toString()
    {
        return "RadialBargraph " + getGaugeType();
    }
}
