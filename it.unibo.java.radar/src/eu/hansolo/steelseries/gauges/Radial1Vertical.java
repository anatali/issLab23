package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public final class Radial1Vertical extends AbstractRadial
{                       
    private final java.awt.geom.Point2D CENTER;
    private final java.awt.geom.Point2D TRACK_OFFSET;    
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
           
    private java.awt.image.BufferedImage postsImage;
    private java.awt.image.BufferedImage trackImage;
    private java.awt.image.BufferedImage tickmarksImage;
    private java.awt.image.BufferedImage pointerImage;
    private java.awt.image.BufferedImage pointerShadowImage;    
    private java.awt.image.BufferedImage thresholdImage;
    private java.awt.image.BufferedImage minMeasuredImage;
    private java.awt.image.BufferedImage maxMeasuredImage;
    private java.awt.image.BufferedImage disabledImage;        
    private double angle = 0;

    
    public Radial1Vertical()
    {
        super();                                                
        CENTER = new java.awt.geom.Point2D.Double();
        TRACK_OFFSET = new java.awt.geom.Point2D.Double();        
        setLedPosition(0.455, 0.555);
        setOrientation(eu.hansolo.steelseries.tools.Orientation.NORTH);        
        setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE5); 
        init(getInnerBounds().width, getInnerBounds().height);
    }

    public Radial1Vertical(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        super();
        CENTER = new java.awt.geom.Point2D.Double();
        TRACK_OFFSET = new java.awt.geom.Point2D.Double();
        setModel(MODEL);
        setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE5);
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
        
        if (postsImage != null)
        {
            postsImage.flush();
        }
        postsImage = create_POSTS_Image(WIDTH, eu.hansolo.steelseries.tools.PostPosition.LOWER_CENTER, eu.hansolo.steelseries.tools.PostPosition.SMALL_GAUGE_MIN_LEFT, eu.hansolo.steelseries.tools.PostPosition.SMALL_GAUGE_MAX_RIGHT);

        if (trackImage != null)
        {
            trackImage.flush();
        }
        TRACK_OFFSET.setLocation(0, 0);
        CENTER.setLocation(getGaugeBounds().getCenterX(), HEIGHT * 0.73);                    
        trackImage = create_TRACK_Image(WIDTH, Math.PI / 2, getTickmarkOffset() - Math.PI / 4, getMinValue(), getMaxValue(), getAngleStep(), getTrackStart(), getTrackSection(), getTrackStop(), getTrackStartColor(), getTrackSectionColor(), getTrackStopColor(), 0.44f, CENTER, getTickmarkDirection(), TRACK_OFFSET);                            

        if (tickmarksImage != null)
        {
            tickmarksImage.flush();
        }
        
        tickmarksImage = TICKMARK_FACTORY.create_RADIAL_TICKMARKS_Image(WIDTH, 
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
                                                0.44f,
                                                -0.04f,
                                                CENTER,
                                                new java.awt.geom.Point2D.Double(0, 0),
                                                eu.hansolo.steelseries.tools.Orientation.NORTH,
                                                getModel().isNiceScale(),
                                                null);
        
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
        
        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterY());
        if (isAreasVisible())
        {
            createAreas(bImage);
        }        
        
        if (isSectionsVisible())
        {
            createSections(bImage);
        }
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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Translate coordinate system related to insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterY());
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        G2.drawImage(bImage, 0, 0, null);
        
        // Draw title
        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
        if (!getTitle().isEmpty())
        {
            if (isLabelColorFromThemeEnabled())
            {
                G2.setColor(getBackgroundColor().LABEL_COLOR);
            }
            else
            {
                G2.setColor(getLabelColor());
            }
            G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.04672897196261682 * getGaugeBounds().width)));
            final java.awt.font.TextLayout TITLE_LAYOUT = new java.awt.font.TextLayout(getTitle(), G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D TITLE_BOUNDARY = TITLE_LAYOUT.getBounds();
            G2.drawString(getTitle(), (float)((getGaugeBounds().width - TITLE_BOUNDARY.getWidth()) / 2.0), 0.4f * getGaugeBounds().width + TITLE_LAYOUT.getAscent() - TITLE_LAYOUT.getDescent());
        }

        // Draw unit string
        if (!getUnitString().isEmpty())
        {
            if (isLabelColorFromThemeEnabled())
            {
                G2.setColor(getBackgroundColor().LABEL_COLOR);
            }
            else
            {
                G2.setColor(getLabelColor());
            }
            G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.04672897196261682 * getGaugeBounds().width)));
            final java.awt.font.TextLayout UNIT_LAYOUT = new java.awt.font.TextLayout(getUnitString(), G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D UNIT_BOUNDARY = UNIT_LAYOUT.getBounds();
            G2.drawString(getUnitString(), (float)((getGaugeBounds().width - UNIT_BOUNDARY.getWidth()) / 2.0), 0.47f * getGaugeBounds().width + UNIT_LAYOUT.getAscent() - UNIT_LAYOUT.getDescent());
        }
        switch(getOrientation())
        {
            case NORTH:               
                break;
            case EAST:
                break;
            case SOUTH:
                break;
            case WEST:
                G2.rotate(-Math.PI / 2.0, CENTER.getX(), CENTER.getY());
                break;
            default:                
                break;
        }    
        
        final java.awt.geom.AffineTransform FORMER_TRANSFORM = G2.getTransform();

        // Draw the track
        if (isTrackVisible())
        {            
            G2.drawImage(trackImage, 0, 0, null);            
        }

        // Draw the tickmarks       
        G2.drawImage(tickmarksImage, 0, 0, null);
        
        // Draw threshold indicator
        if (isThresholdVisible())
        {
            G2.rotate(getGaugeType().ROTATION_OFFSET + (getThreshold() - getMinValue()) * getAngleStep(), CENTER.getX(), getGaugeBounds().width * 0.7336448598);
            G2.drawImage(thresholdImage, 0, 0, null);
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw min measured value indicator
        if (isMinMeasuredValueVisible())
        {
            G2.rotate(getGaugeType().ROTATION_OFFSET + (getMinMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), getGaugeBounds().width * 0.7336448598);
            G2.drawImage(minMeasuredImage, 0, 0, null);
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw max measured value indicator
        if (isMaxMeasuredValueVisible())
        {
            G2.rotate(getGaugeType().ROTATION_OFFSET + (getMaxMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), getGaugeBounds().width * 0.7336448598);
            G2.drawImage(maxMeasuredImage, 0, 0, null);
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw LED if enabled
        if (isLedVisible())
        {
            G2.setTransform(OLD_TRANSFORM);
            G2.drawImage(getCurrentLedImage(), (int) (getGaugeBounds().width * getLedPosition().getX()), (int) (getGaugeBounds().width * getLedPosition().getY()), null);                        
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw the pointer
        angle = getGaugeType().ROTATION_OFFSET + (getValue() - getMinValue()) * getAngleStep();
        //G2.rotate(ANGLE + (Math.cos(Math.toRadians(ANGLE - ROTATION_OFFSET - 91.5))), CENTER.getX(), backgroundImage.getHeight() * 0.7336448598);
        G2.rotate(angle, CENTER.getX(), getGaugeBounds().height * 0.7336448598 + 2);
        G2.drawImage(pointerShadowImage, 0, 0, null);
        G2.setTransform(FORMER_TRANSFORM);
        G2.rotate(angle, CENTER.getX(), getGaugeBounds().height * 0.7336448598);
        G2.drawImage(pointerImage, 0, 0, null);
        G2.setTransform(FORMER_TRANSFORM);

        // Draw posts
        G2.drawImage(postsImage, 0, 0, null);

        switch(getOrientation())
        {
            case NORTH:                
                break;
            case EAST:
                break;
            case SOUTH:
                break;
            case WEST:
                G2.setTransform(OLD_TRANSFORM);
                break;
            default:                
                break;
        }  

        G2.drawImage(fImage, 0, 0, null);
        
        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate coordinate system back to original
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }
        
    /**
     * Sets the orientation of the gauge as an int value that is defined in javax.swing.SwingUtilities
     * 1 => NORTH => Pointer rotation center placed on the bottom of the gauge
     * 3 => EAST  => Pointer rotation center placed on the left side of the gauge
     * 5 => SOUTH => Pointer rotation center placed on the top of the gauge
     * 7 => WEST  => Pointer rotation center placed on the left side of the gauge
     * @param ORIENTATION 
     */
    @Override
    public void setOrientation(final eu.hansolo.steelseries.tools.Orientation ORIENTATION)
    {
        super.setOrientation(ORIENTATION);
        switch(ORIENTATION)
        {
            case NORTH:
                setLedPosition(0.455, 0.555);
                break;
            case EAST:
                break;
            case SOUTH:
                break;
            case WEST:
                setLedPosition(0.455, 0.555);
                break;
            default:
                setLedPosition(0.455, 0.555);
                break;
        }                
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
               
    @Override
    public java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(bImage.getWidth() / 2.0, bImage.getHeight() / 2.0);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return new java.awt.geom.Rectangle2D.Double(bImage.getMinX(), bImage.getMinY(), bImage.getWidth(), bImage.getHeight());
    }
        
    private void createAreas(final java.awt.image.BufferedImage IMAGE)
    {
        final double ORIGIN_CORRECTION;
        final double ANGLE_STEP;

        switch(getOrientation())
        {
            case NORTH:
                ORIGIN_CORRECTION = 135;
                break;
            case EAST:
                ORIGIN_CORRECTION = 135;
                break;
            case SOUTH:
                ORIGIN_CORRECTION = 135;
                break;
            case WEST:
                ORIGIN_CORRECTION = 225;
                break;
            default:
                ORIGIN_CORRECTION = 135;
                break;
        }                         
        
        if (bImage != null)
        {            
            ANGLE_STEP = Math.toDegrees(getGaugeType().ANGLE_RANGE) / (getMaxValue() - getMinValue());
            
            if (bImage != null && !getAreas().isEmpty())
            {                            
                final double RADIUS = bImage.getWidth() * 0.44f - bImage.getWidth() * 0.04f;            
                final double FREE_AREA = bImage.getWidth() / 2.0 - RADIUS;            
                final java.awt.geom.Rectangle2D AREA_FRAME = new java.awt.geom.Rectangle2D.Double(bImage.getMinX() + FREE_AREA, bImage.getMinY() + FREE_AREA, 2 * RADIUS, 2 * RADIUS);                                 
                
                for (eu.hansolo.steelseries.tools.Section area : getAreas())
                {                                                                              
                    area.setFilledArea(new java.awt.geom.Arc2D.Double(AREA_FRAME, ORIGIN_CORRECTION - (area.getStart() * ANGLE_STEP) + (getMinValue() * ANGLE_STEP), -(area.getStop() - area.getStart()) * ANGLE_STEP, java.awt.geom.Arc2D.PIE));                
                }                
            }              
            // Draw the areas
            if (isAreasVisible() && IMAGE != null)
            {                
                final java.awt.Graphics2D G2 = IMAGE.createGraphics();                           
                G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                switch(getOrientation())
                {                    
                    case EAST:                    
                        break;
                    case SOUTH:                    
                        break;
                    case WEST:
                        G2.translate(CENTER.getX() / 2.2, 0);
                        break;
                    case NORTH:
                        
                    default:
                        G2.translate(0, CENTER.getY() / 2.2);
                        break;
                }         
                
                for (eu.hansolo.steelseries.tools.Section area : getAreas())
                {
                    G2.setColor(area.getColor());
                    G2.fill(area.getFilledArea());
                }
                G2.dispose();
            }                
        }
    }
           
    private void createSections(final java.awt.image.BufferedImage IMAGE)
    {                                                    
        if (bImage != null)
        {
            final double ORIGIN_CORRECTION;            
            switch(getOrientation())
            {
                case NORTH:
                    ORIGIN_CORRECTION = 135;
                    break;
                case EAST:
                    ORIGIN_CORRECTION = 135;
                    break;
                case SOUTH:
                    ORIGIN_CORRECTION = 135;
                    break;
                case WEST:
                    ORIGIN_CORRECTION = 225;
                    break;
                default:
                    ORIGIN_CORRECTION = 135;
                    break;
            }              
                                    
            final double ANGLE_STEP = getGaugeType().APEX_ANGLE / (getMaxValue() - getMinValue());
            final double OUTER_RADIUS = bImage.getWidth() * 0.44f;
            final double INNER_RADIUS = bImage.getWidth() * 0.44f - bImage.getWidth() * 0.04f;
            final double FREE_AREA_OUTER_RADIUS = bImage.getWidth() / 2.0 - OUTER_RADIUS;
            final double FREE_AREA_INNER_RADIUS = bImage.getWidth() / 2.0 - INNER_RADIUS;
            final java.awt.geom.Ellipse2D INNER = new java.awt.geom.Ellipse2D.Double(bImage.getMinX() + FREE_AREA_INNER_RADIUS, bImage.getMinY() + FREE_AREA_INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);
        

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
            
            // Draw the sections
            if (isSectionsVisible() && IMAGE != null)
            {
                final java.awt.Graphics2D G2 = IMAGE.createGraphics();
                G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                switch(getOrientation())
                {                    
                    case EAST:                    
                        break;
                    case SOUTH:                    
                        break;
                    case WEST:
                        G2.translate(CENTER.getX() / 2.2, 0);
                        break;
                    case NORTH:
                        
                    default:
                        G2.translate(0, CENTER.getY() / 2.2);
                        break;
                }   
                
                for (eu.hansolo.steelseries.tools.Section section : getSections())
                {
                    G2.setColor(section.getColor());
                    G2.fill(section.getSectionArea());
//                    if (section3DEffectVisible)
//                    {
//                        G2.setPaint(section3DEffect);
//                        G2.fill(section.getSectionArea());
//                    }
                }
                G2.dispose();
            }
        }
    }
    
    @Override
    protected java.awt.image.BufferedImage create_THRESHOLD_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath THRESHOLD = new java.awt.geom.GeneralPath();
        THRESHOLD.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        THRESHOLD.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3333333333333333);
        THRESHOLD.lineTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.37333333333333335);
        THRESHOLD.lineTo(IMAGE_WIDTH * 0.52, IMAGE_HEIGHT * 0.37333333333333335);
        THRESHOLD.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3333333333333333);
        THRESHOLD.closePath();
        final java.awt.geom.Point2D THRESHOLD_START = new java.awt.geom.Point2D.Double(0, THRESHOLD.getBounds2D().getMinY() );
        final java.awt.geom.Point2D THRESHOLD_STOP = new java.awt.geom.Point2D.Double(0, THRESHOLD.getBounds2D().getMaxY() );
        final float[] THRESHOLD_FRACTIONS =
        {
            0.0f,
            0.3f,
            0.59f,
            1.0f
        };
        final java.awt.Color[] THRESHOLD_COLORS =
        {
            new java.awt.Color(82, 0, 0, 255),
            new java.awt.Color(252, 29, 0, 255),
            new java.awt.Color(252, 29, 0, 255),
            new java.awt.Color(82, 0, 0, 255)
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

    @Override
    protected java.awt.image.BufferedImage create_MEASURED_VALUE_Image(final int WIDTH, final java.awt.Color COLOR)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath MAXMEASURED = new java.awt.geom.GeneralPath();
        MAXMEASURED.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        MAXMEASURED.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3037383177570093);
        MAXMEASURED.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.2803738317757009);
        MAXMEASURED.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.2803738317757009);
        MAXMEASURED.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3037383177570093);
        MAXMEASURED.closePath();
        G2.setColor(COLOR);
        G2.fill(MAXMEASURED);

        G2.dispose();

        return IMAGE;
    }

    @Override
    protected java.awt.image.BufferedImage create_POINTER_Image(final int WIDTH, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
       
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath POINTER;
        final java.awt.geom.Point2D POINTER_START;
        final java.awt.geom.Point2D POINTER_STOP;
        final float[] POINTER_FRACTIONS;
        final java.awt.Color[] POINTER_COLORS;
        final java.awt.Paint POINTER_GRADIENT;
        final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_POINTER_COLOR;
        if (getPointerColor() == eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
        {
            CUSTOM_POINTER_COLOR = new eu.hansolo.steelseries.tools.CustomColorDef(getCustomPointerColor());
        }
        else
        {
            CUSTOM_POINTER_COLOR = null;
        }
        
        switch(POINTER_TYPE)
        {            
            case TYPE2:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6962616822429907);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5747663551401869);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2897196261682243);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5747663551401869);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6962616822429907);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.705607476635514, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7663551401869159);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.36f,
                    0.3601f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        getPointerColor().LIGHT,
                        getPointerColor().LIGHT
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;
                
            case TYPE3:
                POINTER = new java.awt.geom.GeneralPath(new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243, IMAGE_WIDTH * 0.009345794392523364, IMAGE_HEIGHT * 0.4485981308));                
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(getPointerColor().LIGHT);
                }   
                else
                {
                    G2.setColor(CUSTOM_POINTER_COLOR.LIGHT);
                }
                G2.fill(POINTER);
                break;   
                
            case TYPE4:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29439252336448596);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3037383177570093);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3037383177570093);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29439252336448596);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.51f,
                    0.52f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {                    
                        getPointerColor().DARK,
                        getPointerColor().DARK,
                        getPointerColor().LIGHT,
                        getPointerColor().LIGHT                    
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {                    
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT                    
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;
                
            case TYPE5:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.74);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.74);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.74);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.74);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.4999f,
                    0.5f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getPointerColor().LIGHT,
                        getPointerColor().LIGHT,
                        getPointerColor().MEDIUM,
                        getPointerColor().MEDIUM
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().MEDIUM,
                        getCustomPointerColorObject().MEDIUM
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(getPointerColor().DARK);
                }
                else
                {
                    G2.setColor(getCustomPointerColorObject().DARK);
                }
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
                
            case TYPE6:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.6);
                POINTER.lineTo(IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.49333333333333335);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.52, IMAGE_HEIGHT * 0.49333333333333335);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.5933333333333334);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.5933333333333334);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.49333333333333335);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.6);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxY(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinY(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.25f,
                    0.75f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getPointerColor().LIGHT,
                        getPointerColor().MEDIUM,
                        getPointerColor().MEDIUM,
                        getPointerColor().LIGHT
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().MEDIUM,
                        getCustomPointerColorObject().MEDIUM,
                        getCustomPointerColorObject().LIGHT
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(getPointerColor().DARK);
                }
                else
                {
                    G2.setColor(getCustomPointerColorObject().DARK);
                }
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
                
            case TYPE7:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getPointerColor().DARK,
                        getPointerColor().MEDIUM
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getCustomPointerColorObject().DARK,
                        getCustomPointerColorObject().MEDIUM
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;
            
            case TYPE8:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.lineTo(IMAGE_WIDTH * 0.5333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.curveTo(IMAGE_WIDTH * 0.5333333333333333, IMAGE_HEIGHT * 0.7333333333333333, IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.7066666666666667, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.31333333333333335);
                POINTER.curveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.7066666666666667, IMAGE_WIDTH * 0.4666666666666667, IMAGE_HEIGHT * 0.7333333333333333, IMAGE_WIDTH * 0.4666666666666667, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.46f,
                    0.47f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                { 
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getPointerColor().LIGHT,
                        getPointerColor().LIGHT,
                        getPointerColor().MEDIUM,
                        getPointerColor().MEDIUM
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {                        
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().MEDIUM,
                        getCustomPointerColorObject().MEDIUM
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(getPointerColor().DARK);
                }
                else
                {
                    G2.setColor(getCustomPointerColorObject().DARK);
                }
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
                
            case TYPE9:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.42);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.42);
                POINTER.lineTo(IMAGE_WIDTH * 0.5133333333333333, IMAGE_HEIGHT * 0.66);
                POINTER.lineTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.66);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.42);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.3);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.curveTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7666666666666667, IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.8533333333333334, IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.8533333333333334);
                POINTER.curveTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.86);
                POINTER.curveTo(IMAGE_WIDTH * 0.52, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.8533333333333334);
                POINTER.curveTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.8533333333333334, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7666666666666667, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.3);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.3);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.48f,
                    1.0f
                };
                POINTER_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(50, 50, 50, 255),
                    new java.awt.Color(102, 102, 102, 255),
                    new java.awt.Color(50, 50, 50, 255)
                };
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                G2.setColor(new java.awt.Color(0x2E2E2E));
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);

                final java.awt.geom.GeneralPath COLOR_BOX = new java.awt.geom.GeneralPath();
                COLOR_BOX.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                COLOR_BOX.moveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.4066666666666667);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.4066666666666667);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.30666666666666664);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.4066666666666667);
                COLOR_BOX.closePath();                
                G2.setColor(getPointerColor().MEDIUM);
                G2.fill(COLOR_BOX);
                break;
                
            case TYPE1:
                
            default:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.6915887850467289, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6261682242990654, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6121495327102804);
                POINTER.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.5981308411214953, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29906542056074764, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29906542056074764);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29906542056074764, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5981308411214953, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6121495327102804);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6308411214953271, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.6915887850467289, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.7149532710280374, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7663551401869159);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.7149532710280374, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.3f,
                    0.59f,
                    1.0f
                };
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        getPointerColor().DARK,
                        getPointerColor().LIGHT,
                        getPointerColor().LIGHT,
                        getPointerColor().DARK
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.DARK
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);

                final java.awt.Color STROKE_COLOR_POINTER = getPointerColor().LIGHT;
                G2.setColor(STROKE_COLOR_POINTER);
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
        }

        G2.dispose();

        return IMAGE;
    }

    @Override
    protected java.awt.image.BufferedImage create_POINTER_SHADOW_Image(final int WIDTH, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        final java.awt.Color SHADOW_COLOR = new java.awt.Color(0.0f, 0.0f, 0.0f, 0.65f);
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath POINTER;

        switch(POINTER_TYPE)
        {
            case TYPE1:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.6915887850467289, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6261682242990654, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6121495327102804);
                POINTER.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.5981308411214953, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29906542056074764, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29906542056074764);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29906542056074764, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5981308411214953, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6121495327102804);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6308411214953271, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.6915887850467289, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.7149532710280374, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7663551401869159);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.7149532710280374, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE2:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.6962616822429907);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.5747663551401869);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2897196261682243);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2897196261682243);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.5747663551401869);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.6962616822429907);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.705607476635514, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7663551401869159);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.7663551401869159, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7523364485981309, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7242990654205608, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.705607476635514);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE3:                
                break;  
                    
            case TYPE4:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29439252336448596);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3037383177570093);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.7336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3037383177570093);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.29439252336448596);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE5:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.74);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.74);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.74);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.74);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE6:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.6);
                POINTER.lineTo(IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.49333333333333335);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.52, IMAGE_HEIGHT * 0.49333333333333335);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.5933333333333334);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.5933333333333334);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.49333333333333335);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.6);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE7:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.lineTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.30666666666666664);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE8:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.lineTo(IMAGE_WIDTH * 0.5333333333333333, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.curveTo(IMAGE_WIDTH * 0.5333333333333333, IMAGE_HEIGHT * 0.7333333333333333, IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.7066666666666667, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.31333333333333335);
                POINTER.curveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.7066666666666667, IMAGE_WIDTH * 0.4666666666666667, IMAGE_HEIGHT * 0.7333333333333333, IMAGE_WIDTH * 0.4666666666666667, IMAGE_HEIGHT * 0.7333333333333333);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE9:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.42);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.42);
                POINTER.lineTo(IMAGE_WIDTH * 0.5133333333333333, IMAGE_HEIGHT * 0.66);
                POINTER.lineTo(IMAGE_WIDTH * 0.4866666666666667, IMAGE_HEIGHT * 0.66);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.42);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.3);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7);
                POINTER.lineTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.curveTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.7666666666666667, IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.8533333333333334, IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.8533333333333334);
                POINTER.curveTo(IMAGE_WIDTH * 0.47333333333333333, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.86);
                POINTER.curveTo(IMAGE_WIDTH * 0.52, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.86, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.8533333333333334);
                POINTER.curveTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.8533333333333334, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7666666666666667, IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7666666666666667);
                POINTER.lineTo(IMAGE_WIDTH * 0.5266666666666666, IMAGE_HEIGHT * 0.7);
                POINTER.lineTo(IMAGE_WIDTH * 0.5066666666666667, IMAGE_HEIGHT * 0.3);
                POINTER.lineTo(IMAGE_WIDTH * 0.49333333333333335, IMAGE_HEIGHT * 0.3);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
        }

        G2.dispose();

        return IMAGE;
    }

    @Override
    public String toString()
    {
        return "Radial1Vertical";
    }
}
