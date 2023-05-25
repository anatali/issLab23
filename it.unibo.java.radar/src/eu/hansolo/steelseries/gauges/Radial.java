package eu.hansolo.steelseries.gauges;


/**
 *
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class Radial extends AbstractRadial
{                        
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage pointerImage;
    private java.awt.image.BufferedImage pointerShadowImage;    
    private java.awt.image.BufferedImage thresholdImage;
    private java.awt.image.BufferedImage minMeasuredImage;
    private java.awt.image.BufferedImage maxMeasuredImage;    
    private java.awt.image.BufferedImage disabledImage;          
    private double angle;
    private final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double();
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();
    private boolean section3DEffectVisible;
    private java.awt.RadialGradientPaint section3DEffect;
    private final java.awt.geom.Point2D TRACK_OFFSET = new java.awt.geom.Point2D.Double();
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private double unitStringWidth;
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
        
    public Radial()
    {
        super();
        angle = 0;
        section3DEffectVisible = false;
        init(getInnerBounds().width, getInnerBounds().height);
    }
    
    public Radial(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        super();
        setModel(MODEL);        
        angle = 0;
        section3DEffectVisible = false;
        init(getInnerBounds().width, getInnerBounds().height);
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
               
        create_POSTS_Image(WIDTH, fImage, getGaugeType().POST_POSITIONS);

        TRACK_OFFSET.setLocation(0, 0);
        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterX());                    
        if (isTrackVisible())
        {
            create_TRACK_Image(WIDTH, getFreeAreaAngle(), getTickmarkOffset(), getMinValue(), getMaxValue(), getAngleStep(), getTrackStart(), getTrackSection(), getTrackStop(), getTrackStartColor(), getTrackSectionColor(), getTrackStopColor(), 0.38f, CENTER, getTickmarkDirection(), TRACK_OFFSET, bImage);
        }        
           
        // Create areas if not empty
        if (!getAreas().isEmpty())
        {
            createAreas(bImage);
        }        
        // Create the sections 3d effect gradient overlay
        if (section3DEffectVisible)
        {
            section3DEffect = create_3D_RADIAL_GRADIENT(WIDTH, 0.38f);
        }
        // Create sections if not empty
        if (!getSections().isEmpty())
        {
            createSections(bImage);
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
                                                0.38f,
                                                0.09f,
                                                CENTER,
                                                new java.awt.geom.Point2D.Double(0, 0),
                                                eu.hansolo.steelseries.tools.Orientation.NORTH,
                                                getModel().isNiceScale(),
                                                bImage);
                       
        create_TITLE_Image(WIDTH, getTitle(), getUnitString(), bImage);
                
        if (isLcdVisible())
        {                        
            create_LCD_Image(new java.awt.geom.Rectangle2D.Double(((getGaugeBounds().width - WIDTH * getGaugeType().LCD_FACTORS.getX()) / 2.0),
                                                                   (getGaugeBounds().height * getGaugeType().LCD_FACTORS.getY()), 
                                                                   (WIDTH * getGaugeType().LCD_FACTORS.getWidth()), 
                                                                   (WIDTH * getGaugeType().LCD_FACTORS.getHeight())), 
                                                                   getLcdColor(), 
                                                                   getCustomLcdBackground(), 
                                                                   bImage);
            LCD.setRect(((getGaugeBounds().width - WIDTH * getGaugeType().LCD_FACTORS.getX()) / 2.0), (getGaugeBounds().height * getGaugeType().LCD_FACTORS.getY()), WIDTH * getGaugeType().LCD_FACTORS.getWidth(), WIDTH * getGaugeType().LCD_FACTORS.getHeight());
        }              
               
        if (pointerImage != null)
        {
            pointerImage.flush();
        }
        pointerImage = create_POINTER_Image(WIDTH, getPointerType());
        
        if (pointerShadowImage != null)
        {
            pointerShadowImage.flush();
        }
        pointerShadowImage = create_POINTER_SHADOW_Image(WIDTH, getPointerType());
        
        if (thresholdImage != null)
        {
            thresholdImage.flush();
        }
        thresholdImage = create_THRESHOLD_Image(WIDTH);

        if (minMeasuredImage != null)
        {
            minMeasuredImage.flush();
        }
        minMeasuredImage = create_MEASURED_VALUE_Image(WIDTH, new java.awt.Color(0, 23, 252, 255));

        if (maxMeasuredImage != null)
        {
            maxMeasuredImage.flush();
        }
        maxMeasuredImage = create_MEASURED_VALUE_Image(WIDTH, new java.awt.Color(252, 29, 0, 255));

        if (isForegroundVisible())
        {            
            switch(getFrameType())
            {
                case SQUARE:
                    FOREGROUND_FACTORY.createLinearForeground(WIDTH, WIDTH, false, fImage);
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
        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterX());

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);        
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

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
        
        // Draw threshold indicator
        if (isThresholdVisible())
        {
            G2.rotate(getRotationOffset() + (getThreshold() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());                        
            G2.drawImage(thresholdImage, (int) (getGaugeBounds().width * 0.4813084112), (int) (getGaugeBounds().height * 0.0841121495), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw min measured value indicator
        if (isMinMeasuredValueVisible())
        {
            G2.rotate(getRotationOffset() + (getMinMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(minMeasuredImage, (int) (getGaugeBounds().width * 0.4865), (int) (getGaugeBounds().height * 0.105), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw max measured value indicator
        if (isMaxMeasuredValueVisible())
        {
            G2.rotate(getRotationOffset() + (getMaxMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(maxMeasuredImage, (int) (getGaugeBounds().width * 0.4865), (int) (getGaugeBounds().height * 0.105), null);
            G2.setTransform(OLD_TRANSFORM);
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
            if (isLcdUnitStringVisible())
            {
                unitLayout = new java.awt.font.TextLayout(getLcdUnitString(), G2.getFont(), RENDER_CONTEXT);
                UNIT_BOUNDARY.setFrame(unitLayout.getBounds());
                G2.drawString(getLcdUnitString(), (int) (LCD.getX() + (LCD.getWidth() - UNIT_BOUNDARY.getWidth()) - LCD.getWidth() * 0.03), (int) (LCD.getY() + LCD.getHeight() * 0.76));                
                unitStringWidth = UNIT_BOUNDARY.getWidth();
            }
            else
            {
                unitStringWidth = 0;
            }            
            G2.setFont(getLcdValueFont());            
            switch(getModel().getNumberSystem())
            {
                case HEX:
                    valueLayout = new java.awt.font.TextLayout(Integer.toHexString((int) getLcdValue()).toUpperCase(), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toHexString((int) getLcdValue()).toUpperCase(), (int) (LCD.getX() + (LCD.getWidth() - unitStringWidth - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76));            
                    break;
                    
                case OCT:
                    valueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toOctalString((int) getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - unitStringWidth - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76));            
                    break;
                    
                case DEC:
                    
                default:
                    valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(formatLcdValue(getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - unitStringWidth - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + LCD.getHeight() * 0.76));            
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
        
        // Draw the pointer
        angle = getRotationOffset() + (getValue() - getMinValue()) * getAngleStep();        
        G2.rotate(angle + (Math.cos(Math.toRadians(angle - getRotationOffset() - 91.5))), CENTER.getX(), CENTER.getY());
        G2.drawImage(pointerShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(angle, CENTER.getX(), CENTER.getY());
        G2.drawImage(pointerImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);

        // Draw combined foreground image
        G2.drawImage(fImage, 0, 0, null);
        
        // Draw disabled image if component isEnabled() == false
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
            
    /**
     * Returns true if the 3d effect gradient overlay for the sections is visible
     * @return true if the 3d effect gradient overlay for the sections is visible
     */
    public boolean isSection3DEffectVisible()
    {
        return this.section3DEffectVisible;
    }
    
    /**
     * Defines the visibility of the 3d effect gradient overlay for the sections
     * @param SECTION_3D_EFFECT_VISIBLE 
     */
    public void setSection3DEffectVisible(final boolean SECTION_3D_EFFECT_VISIBLE)
    {
        this.section3DEffectVisible = SECTION_3D_EFFECT_VISIBLE;
        init(getInnerBounds().width, getInnerBounds().height);
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
                       
    // <editor-fold defaultstate="collapsed" desc="Areas related">    
    private void createAreas(final java.awt.image.BufferedImage IMAGE)
    {                
        if (bImage != null)
        {
            final double ANGLE_STEP = Math.toDegrees(getGaugeType().ANGLE_RANGE) / (getMaxValue() - getMinValue());
            
            if (bImage != null && !getAreas().isEmpty())
            {            
                final double RADIUS = bImage.getWidth() * 0.38f - bImage.getWidth() * 0.04f;
                final double FREE_AREA = bImage.getWidth() / 2.0 - RADIUS;
                final java.awt.geom.Rectangle2D AREA_FRAME = new java.awt.geom.Rectangle2D.Double(bImage.getMinX() + FREE_AREA, bImage.getMinY() + FREE_AREA, 2 * RADIUS, 2 * RADIUS);           
                for (eu.hansolo.steelseries.tools.Section area : getAreas())
                {                
                    area.setFilledArea(new java.awt.geom.Arc2D.Double(AREA_FRAME, getGaugeType().ORIGIN_CORRECTION - (area.getStart() * ANGLE_STEP) + (getMinValue() * ANGLE_STEP), -(area.getStop() - area.getStart()) * ANGLE_STEP, java.awt.geom.Arc2D.PIE));                
                }           
            }  
            
            // Draw the areas
            if (isAreasVisible() && IMAGE != null)
            {                
                final java.awt.Graphics2D G2 = IMAGE.createGraphics();
                G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                for (eu.hansolo.steelseries.tools.Section area : getAreas())
                {
                    G2.setColor(area.getColor());
                    G2.fill(area.getFilledArea());
                }
                G2.dispose();
            }                
        }
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Sections related">        
    private void createSections(final java.awt.image.BufferedImage IMAGE)
    {                                                    
        if (bImage != null)
        {            
            final double ANGLE_STEP = getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue());
            final double OUTER_RADIUS = bImage.getWidth() * 0.38f;
            final double INNER_RADIUS = bImage.getWidth() * 0.38f - bImage.getWidth() * 0.04f;
            final double FREE_AREA_OUTER_RADIUS = bImage.getWidth() / 2.0 - OUTER_RADIUS;
            final double FREE_AREA_INNER_RADIUS = bImage.getWidth() / 2.0 - INNER_RADIUS;
            final java.awt.geom.Ellipse2D INNER = new java.awt.geom.Ellipse2D.Double(bImage.getMinX() + FREE_AREA_INNER_RADIUS, bImage.getMinY() + FREE_AREA_INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);
        

            for (eu.hansolo.steelseries.tools.Section section : getSections())
            {                            
                final double ANGLE_START = getGaugeType().ORIGIN_CORRECTION - (section.getStart() * ANGLE_STEP) + (getMinValue() * ANGLE_STEP);
                final double ANGLE_EXTEND = -(section.getStop() - section.getStart()) * ANGLE_STEP;                

                final java.awt.geom.Arc2D OUTER_ARC = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
                OUTER_ARC.setFrame(bImage.getMinX() + FREE_AREA_OUTER_RADIUS, bImage.getMinY() + FREE_AREA_OUTER_RADIUS, 2 * OUTER_RADIUS, 2 * OUTER_RADIUS);
                OUTER_ARC.setAngleStart(ANGLE_START);
                OUTER_ARC.setAngleExtent(ANGLE_EXTEND);
                final java.awt.geom.Area SECTION = new java.awt.geom.Area(OUTER_ARC);

                SECTION.subtract(new java.awt.geom.Area(INNER));

                section.setSectionArea(SECTION);
            } 
            
            // Draw the sections
            if (isSectionsVisible() && IMAGE != null)
            {
                final java.awt.Graphics2D G2 = IMAGE.createGraphics();
                G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                for (eu.hansolo.steelseries.tools.Section section : getSections())
                {
                    G2.setColor(section.getColor());
                    G2.fill(section.getSectionArea());
                    if (section3DEffectVisible)
                    {
                        G2.setPaint(section3DEffect);
                        G2.fill(section.getSectionArea());
                    }
                }
                G2.dispose();
            }
        }
    }
    // </editor-fold>    
        
    @Override
    public String toString()
    {
        return "Radial " + getGaugeType();
    } 
}