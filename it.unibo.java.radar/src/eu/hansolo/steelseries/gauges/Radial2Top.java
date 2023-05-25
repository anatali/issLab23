package eu.hansolo.steelseries.gauges;

/**
 *
 * @author hansolo
 */
public final class Radial2Top extends AbstractRadial
{
    private final double FREE_AREA_ANGLE = Math.toRadians(0); // area where no tickmarks will be painted   
    private final double ROTATION_OFFSET = (1.5 * Math.PI) + (FREE_AREA_ANGLE / 2.0); // Offset for the pointer    
    private final java.awt.geom.Point2D CENTER;
    private final java.awt.geom.Point2D TRACK_OFFSET;    
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage pointerImage;
    private java.awt.image.BufferedImage pointerShadowImage;    
    private java.awt.image.BufferedImage thresholdImage;
    private java.awt.image.BufferedImage minMeasuredImage;
    private java.awt.image.BufferedImage maxMeasuredImage;
    private java.awt.image.BufferedImage disabledImage;
    private double angle = 0;


    public Radial2Top()
    {
        super();     
        CENTER = new java.awt.geom.Point2D.Double();
        TRACK_OFFSET = new java.awt.geom.Point2D.Double();        
        setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE2);    
        init(getInnerBounds().width, getInnerBounds().height);
    }
    
    public Radial2Top(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        super();     
        setModel(MODEL);
        CENTER = new java.awt.geom.Point2D.Double();
        TRACK_OFFSET = new java.awt.geom.Point2D.Double();        
        setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE2);    
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
            create_FRAME_Image(WIDTH, bImage);                  
        }        
                
        if (isBackgroundVisible())
        {
            create_BACKGROUND_Image(WIDTH, "", "", bImage);
        } 
        
        create_POSTS_Image(WIDTH, fImage);

        TRACK_OFFSET.setLocation(0, 0);
        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterY());
        if (isTrackVisible())
        {
            create_TRACK_Image(WIDTH, getFreeAreaAngle(), getTickmarkOffset(), getMinValue(), getMaxValue(), getAngleStep(), getTrackStart(), getTrackSection(), getTrackStop(), getTrackStartColor(), getTrackSectionColor(), getTrackStopColor(), 0.38f, CENTER, getTickmarkDirection(), TRACK_OFFSET, bImage);
        }
       
        createAreas(bImage);        
        
        createSections(bImage);
                 
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

        if (isForegroundVisible())
        {            
            FOREGROUND_FACTORY.createRadialForeground(WIDTH, false, getForegroundType(), fImage);
        }

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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);
        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterY());
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);
              
        // Draw threshold indicator
        if (isThresholdVisible())
        {
            G2.rotate(ROTATION_OFFSET + (getThreshold() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(thresholdImage, (int) (getGaugeBounds().width * 0.480369999), (int) (getGaugeBounds().height * 0.13), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw min measured value indicator
        if (isMinMeasuredValueVisible())
        {
            G2.rotate(ROTATION_OFFSET + (getMinMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(minMeasuredImage, (int) (getGaugeBounds().width * 0.4865), (int) (getGaugeBounds().height * 0.105), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw max measured value indicator
        if (isMaxMeasuredValueVisible())
        {
            G2.rotate(ROTATION_OFFSET + (getMaxMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(maxMeasuredImage, (int) (getGaugeBounds().width * 0.4865), (int) (getGaugeBounds().height * 0.105), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw LED if enabled
        if (isLedVisible())
        {
            G2.drawImage(getCurrentLedImage(), (int) (getGaugeBounds().width * getLedPosition().getX()), (int) (getGaugeBounds().height * getLedPosition().getY()), null);
        }

        // Draw the pointer
        angle = ROTATION_OFFSET + (getValue() - getMinValue()) * getAngleStep();
        G2.rotate(angle, CENTER.getX(), CENTER.getY() + 2);
        G2.drawImage(pointerShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(angle, CENTER.getX(), CENTER.getY());
        G2.drawImage(pointerImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        
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
    public eu.hansolo.steelseries.tools.FrameType getFrameType()
    {
        return eu.hansolo.steelseries.tools.FrameType.ROUND;
    }
    
    @Override
    public eu.hansolo.steelseries.tools.GaugeType getGaugeType()
    {
        return eu.hansolo.steelseries.tools.GaugeType.TYPE2;
    }
    
    @Override
    public void setGaugeType(final eu.hansolo.steelseries.tools.GaugeType GAUGE_TYPE)
    {
        super.setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE2);
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

    private void createAreas(final java.awt.image.BufferedImage IMAGE)
    {                
        if (!getAreas().isEmpty() && bImage != null)
        {       
            final double ORIGIN_CORRECTION = 180.0;                    
            final double RADIUS = bImage.getWidth() * 0.38f - bImage.getWidth() * 0.04f;
            final double FREE_AREA = bImage.getWidth() / 2.0 - RADIUS;                        

            for (eu.hansolo.steelseries.tools.Section area : getAreas())
            {
                final double ANGLE_START = ORIGIN_CORRECTION - (area.getStart() * Math.toDegrees(getAngleStep()));
                final double ANGLE_EXTEND = -(area.getStop() - area.getStart()) * Math.toDegrees(getAngleStep());

                final java.awt.geom.Arc2D AREA = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
                AREA.setFrame(bImage.getMinX() + FREE_AREA, bImage.getMinY() + FREE_AREA, 2 * RADIUS, 2 * RADIUS);
                AREA.setAngleStart(ANGLE_START);
                AREA.setAngleExtent(ANGLE_EXTEND);                

                area.setFilledArea(AREA);
            }
            
            // Draw the area
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
           
    private void createSections(final java.awt.image.BufferedImage IMAGE)
    {
        if (!getSections().isEmpty() && bImage != null)
        {       
            final double ORIGIN_CORRECTION = 180.0;
            //final double ANGLE_STEP = 180.0 / (getMaxValue() - getMinValue());
            final double OUTER_RADIUS = bImage.getWidth() * 0.38f;
            final double INNER_RADIUS = bImage.getWidth() * 0.38f - bImage.getWidth() * 0.04f;
            final double FREE_AREA_OUTER_RADIUS = bImage.getWidth() / 2.0 - OUTER_RADIUS;
            final double FREE_AREA_INNER_RADIUS = bImage.getWidth() / 2.0 - INNER_RADIUS;
            final java.awt.geom.Ellipse2D INNER = new java.awt.geom.Ellipse2D.Double(bImage.getMinX() + FREE_AREA_INNER_RADIUS, bImage.getMinY() + FREE_AREA_INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);

            for (eu.hansolo.steelseries.tools.Section section : getSections())
            {
                final double ANGLE_START = ORIGIN_CORRECTION - (section.getStart() * Math.toDegrees(getAngleStep()));
                final double ANGLE_EXTEND = -(section.getStop() - section.getStart()) * Math.toDegrees(getAngleStep());

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
                }
                G2.dispose();
            }
        }
    }

    private java.awt.image.BufferedImage create_FRAME_Image(final int WIDTH, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        final double VERTICAL_SCALE;
        
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, (int) (0.641860465116279 * WIDTH), java.awt.Transparency.TRANSLUCENT);
            VERTICAL_SCALE = 1.0;
        }
        else
        {
            VERTICAL_SCALE = 0.641860465116279;
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        if (getFrameDesign() != eu.hansolo.steelseries.tools.FrameDesign.NO_FRAME)
        {
            // Define shape that will be subtracted from frame shapes
            final java.awt.geom.GeneralPath SUBTRACT_PATH = new java.awt.geom.GeneralPath();
            SUBTRACT_PATH.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            SUBTRACT_PATH.moveTo(IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            SUBTRACT_PATH.curveTo(IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.42028985507246375 * VERTICAL_SCALE, IMAGE_WIDTH * 0.26976744186046514, IMAGE_HEIGHT * 0.13043478260869565 * VERTICAL_SCALE, IMAGE_WIDTH * 0.49767441860465117, IMAGE_HEIGHT * 0.13043478260869565 * VERTICAL_SCALE);
            SUBTRACT_PATH.curveTo(IMAGE_WIDTH * 0.7255813953488373, IMAGE_HEIGHT * 0.13043478260869565 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.42028985507246375 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            SUBTRACT_PATH.curveTo(IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.8188405797101449 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE);
            SUBTRACT_PATH.lineTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE);
            SUBTRACT_PATH.curveTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE, IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.8115942028985508 * VERTICAL_SCALE, IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            SUBTRACT_PATH.closePath();
            final java.awt.geom.Area SUBTRACT = new java.awt.geom.Area(SUBTRACT_PATH);

            final java.awt.geom.GeneralPath FRAME_OUTERFRAME = new java.awt.geom.GeneralPath();
            FRAME_OUTERFRAME.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            FRAME_OUTERFRAME.moveTo(0.0, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_OUTERFRAME.curveTo(0.0, IMAGE_HEIGHT * 0.34782608695652173 * VERTICAL_SCALE, IMAGE_WIDTH * 0.22325581395348837, 0.0, IMAGE_WIDTH * 0.49767441860465117, 0.0);
            FRAME_OUTERFRAME.curveTo(IMAGE_WIDTH * 0.772093023255814, 0.0, IMAGE_WIDTH, IMAGE_HEIGHT * 0.34782608695652173 * VERTICAL_SCALE, IMAGE_WIDTH, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_OUTERFRAME.curveTo(IMAGE_WIDTH, IMAGE_HEIGHT * 0.9057971014492754 * VERTICAL_SCALE, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT * VERTICAL_SCALE);
            FRAME_OUTERFRAME.lineTo(0.0, IMAGE_HEIGHT * VERTICAL_SCALE);
            FRAME_OUTERFRAME.curveTo(0.0, IMAGE_HEIGHT * VERTICAL_SCALE, 0.0, IMAGE_HEIGHT * 0.8985507246376812 * VERTICAL_SCALE, 0.0, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_OUTERFRAME.closePath();
            final java.awt.Color FILL_COLOR_FRAME_OUTERFRAME = new java.awt.Color(0x848484);
            G2.setColor(FILL_COLOR_FRAME_OUTERFRAME);
            final java.awt.geom.Area FRAME_OUTERFRAME_AREA = new java.awt.geom.Area(FRAME_OUTERFRAME);
            FRAME_OUTERFRAME_AREA.subtract(SUBTRACT);
            G2.fill(FRAME_OUTERFRAME_AREA);

            final java.awt.geom.GeneralPath FRAME_MAIN = new java.awt.geom.GeneralPath();
            FRAME_MAIN.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            FRAME_MAIN.moveTo(IMAGE_WIDTH * 0.004651162790697674, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.004651162790697674, IMAGE_HEIGHT * 0.34782608695652173 * VERTICAL_SCALE, IMAGE_WIDTH * 0.22325581395348837, IMAGE_HEIGHT * 0.007246376811594203 * VERTICAL_SCALE, IMAGE_WIDTH * 0.49767441860465117, IMAGE_HEIGHT * 0.007246376811594203 * VERTICAL_SCALE);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.772093023255814, IMAGE_HEIGHT * 0.007246376811594203 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9953488372093023, IMAGE_HEIGHT * 0.35507246376811596 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9953488372093023, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.9953488372093023, IMAGE_HEIGHT * 0.8840579710144928 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9953488372093023, IMAGE_HEIGHT * 0.9927536231884058 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9953488372093023, IMAGE_HEIGHT * 0.9927536231884058 * VERTICAL_SCALE);
            FRAME_MAIN.lineTo(IMAGE_WIDTH * 0.004651162790697674, IMAGE_HEIGHT * 0.9927536231884058 * VERTICAL_SCALE);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.004651162790697674, IMAGE_HEIGHT * 0.9927536231884058 * VERTICAL_SCALE, IMAGE_WIDTH * 0.004651162790697674, IMAGE_HEIGHT * 0.8840579710144928 * VERTICAL_SCALE, IMAGE_WIDTH * 0.004651162790697674, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_MAIN.closePath();

            final java.awt.geom.Point2D FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
            final java.awt.geom.Point2D FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
            final java.awt.geom.Point2D FRAME_MAIN_CENTER = new java.awt.geom.Point2D.Double(FRAME_MAIN.getBounds2D().getCenterX(), FRAME_MAIN.getBounds2D().getHeight() * 0.7753623188 * VERTICAL_SCALE);

            final float[] FRAME_MAIN_FRACTIONS;
            final java.awt.Color[] FRAME_MAIN_COLORS;
            final java.awt.Paint FRAME_MAIN_GRADIENT;

            switch(getFrameDesign())
            {
                case BLACK_METAL:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        45.0f,
                        85.0f,
                        180.0f,
                        275.0f,
                        315.0f,
                        360.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(254, 254, 254, 255)
                    };

                    FRAME_MAIN_GRADIENT = new eu.hansolo.steelseries.tools.ConicalGradientPaint(true, FRAME_MAIN_CENTER, 0, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;

                case METAL:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.07f,
                        0.12f,
                        1.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(210, 210, 210, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(213, 213, 213, 255)
                    };

                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;

                case SHINY_METAL:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        45.0f,
                        90.0f,
                        95.0f,
                        180.0f,
                        265.0f,
                        270.0f,
                        315.0f,
                        360.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(210, 210, 210, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(160, 160, 160, 255),
                        new java.awt.Color(160, 160, 160, 255),
                        new java.awt.Color(160, 160, 160, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(210, 210, 210, 255),
                        new java.awt.Color(254, 254, 254, 255)
                    };

                    FRAME_MAIN_GRADIENT = new eu.hansolo.steelseries.tools.ConicalGradientPaint(true, FRAME_MAIN_CENTER, 0, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;

                case BRASS:
                    FRAME_MAIN_FRACTIONS = new float[]
                        {
                            0.0f,
                            0.05f,
                            0.10f,
                            0.50f,
                            0.90f,
                            0.95f,
                            1.0f
                        };
                    
                    FRAME_MAIN_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(249, 243, 155, 255),
                            new java.awt.Color(246, 226, 101, 255),
                            new java.awt.Color(240, 225, 132, 255),
                            new java.awt.Color(90, 57, 22, 255),
                            new java.awt.Color(249, 237, 139, 255),
                            new java.awt.Color(243, 226, 108, 255),
                            new java.awt.Color(202, 182, 113, 255)
                        };
                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;
                
                case STEEL:
                    FRAME_MAIN_FRACTIONS = new float[]
                        {
                            0.0f,
                            0.05f,
                            0.10f,
                            0.50f,
                            0.90f,
                            0.95f,
                            1.0f
                        };
                    
                    FRAME_MAIN_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(231, 237, 237, 255),
                            new java.awt.Color(189, 199, 198, 255),
                            new java.awt.Color(192, 201, 200, 255),
                            new java.awt.Color(23, 31, 33, 255),
                            new java.awt.Color(196, 205, 204, 255),
                            new java.awt.Color(194, 204, 203, 255),
                            new java.awt.Color(189, 201, 199, 255)
                        };
                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;     
                
                case CHROME:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.09f,
                        0.12f,
                        0.16f,
                        0.25f,
                        0.29f,
                        0.33f,
                        0.38f,
                        0.48f,
                        0.52f,
                        0.63f,
                        0.68f,
                        0.8f,
                        0.83f,
                        0.87f,
                        0.97f,
                        1.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(255, 255, 255, 255),
                        new java.awt.Color(255, 255, 255, 255),
                        new java.awt.Color(136, 136, 138, 255),
                        new java.awt.Color(164, 185, 190, 255),
                        new java.awt.Color(158, 179, 182, 255),
                        new java.awt.Color(112, 112, 112, 255),
                        new java.awt.Color(221, 227, 227, 255),
                        new java.awt.Color(155, 176, 179, 255),
                        new java.awt.Color(156, 176, 177, 255),
                        new java.awt.Color(254, 255, 255, 255),
                        new java.awt.Color(255, 255, 255, 255),
                        new java.awt.Color(156, 180, 180, 255),
                        new java.awt.Color(198, 209, 211, 255),
                        new java.awt.Color(246, 248, 247, 255),
                        new java.awt.Color(204, 216, 216, 255),
                        new java.awt.Color(164, 188, 190, 255),
                        new java.awt.Color(255, 255, 255, 255)
                    };

                    FRAME_MAIN_GRADIENT = new eu.hansolo.steelseries.tools.ConicalGradientPaint(false, FRAME_MAIN_CENTER, 0, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;    
                    
                case GOLD:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.15f,
                        0.22f,
                        0.3f,
                        0.38f,
                        0.44f,
                        0.51f,
                        0.6f,
                        0.68f,
                        0.75f,
                        1.0f
                    };
                    
                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(255, 255, 207, 255),
                        new java.awt.Color(255, 237, 96, 255),
                        new java.awt.Color(254, 199, 57, 255),
                        new java.awt.Color(255, 249, 203, 255),
                        new java.awt.Color(255, 199, 64, 255),
                        new java.awt.Color(252, 194, 60, 255),
                        new java.awt.Color(255, 204, 59, 255),
                        new java.awt.Color(213, 134, 29, 255),
                        new java.awt.Color(255, 201, 56, 255),
                        new java.awt.Color(212, 135, 29, 255),
                        new java.awt.Color(247, 238, 101, 255)
                    };
                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;    
                
                case ANTHRACITE:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.06f,
                        0.12f,
                        1.0f
                    };
                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(118, 117, 135, 255),
                        new java.awt.Color(74, 74, 82, 255),
                        new java.awt.Color(50, 50, 54, 255),
                        new java.awt.Color(97, 97, 108, 255)
                    };
                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;    
                    
                case TILTED_GRAY:
                    FRAME_MAIN_START.setLocation((0.2336448598130841 * IMAGE_WIDTH), (0.08411214953271028 * IMAGE_HEIGHT));
                    FRAME_MAIN_STOP.setLocation(((0.2336448598130841 + 0.5789369637935792) * IMAGE_WIDTH), ((0.08411214953271028 + 0.8268076708711319) * IMAGE_HEIGHT));
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.07f,
                        0.16f,
                        0.33f,
                        0.55f,
                        0.79f,
                        1.0f
                    };
                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(255, 255, 255, 255),
                        new java.awt.Color(210, 210, 210, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(255, 255, 255, 255),
                        new java.awt.Color(197, 197, 197, 255),
                        new java.awt.Color(255, 255, 255, 255),
                        new java.awt.Color(102, 102, 102, 255)
                    };
                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;
                    
                case TILTED_BLACK:
                    FRAME_MAIN_START.setLocation( (0.22897196261682243 * IMAGE_WIDTH), (0.0794392523364486 * IMAGE_HEIGHT) );
                    FRAME_MAIN_STOP.setLocation( ((0.22897196261682243 + 0.573576436351046) * IMAGE_WIDTH), ((0.0794392523364486 + 0.8191520442889918) * IMAGE_HEIGHT) );
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.21f,
                        0.47f,
                        0.99f,
                        1.0f
                    };
                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(102, 102, 102, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(102, 102, 102, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255)
                    };
                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;
                    
                default:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.07f,
                        0.12f,
                        1.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(210, 210, 210, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(213, 213, 213, 255)
                    };

                    FRAME_MAIN_GRADIENT = new java.awt.LinearGradientPaint(FRAME_MAIN_START, FRAME_MAIN_STOP, FRAME_MAIN_FRACTIONS, FRAME_MAIN_COLORS);
                    break;
            }

            G2.setPaint(FRAME_MAIN_GRADIENT);
            final java.awt.geom.Area FRAME_MAIN_AREA = new java.awt.geom.Area(FRAME_MAIN);
            FRAME_MAIN_AREA.subtract(SUBTRACT);
            G2.fill(FRAME_MAIN_AREA);

            // Apply frame effects            
            final float[] EFFECT_FRACTIONS;
            final java.awt.Color[] EFFECT_COLORS;
            final eu.hansolo.steelseries.tools.GradientWrapper EFFECT_GRADIENT;
            float scale = 1.0f;
            final java.awt.Shape[] EFFECT = new java.awt.Shape[100];
            switch (getFrameEffect())
            {
                case EFFECT_BULGE:                    
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.13f,
                        0.14f,
                        0.17f,
                        0.18f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[] 
                    {
                        new java.awt.Color(0, 0, 0, 102),            // Outside
                        new java.awt.Color(255, 255, 255, 151),                                                
                        new java.awt.Color(219, 219, 219, 153),
                        new java.awt.Color(0, 0, 0, 95),                        
                        new java.awt.Color(0, 0, 0, 76),       // Inside
                        new java.awt.Color(0, 0, 0, 0)
                    };        
                    EFFECT_GRADIENT = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(100,0), new java.awt.geom.Point2D.Double(0, 0), EFFECT_FRACTIONS, EFFECT_COLORS);                                        
                    for (int i = 0 ; i < 100 ; i++)
                    {                                          
                        EFFECT[i] = eu.hansolo.steelseries.tools.Scaler.INSTANCE.scale(FRAME_MAIN_AREA, scale);            
                        scale -= 0.01f;            
                    } 
                    G2.setStroke(new java.awt.BasicStroke(1.5f));                                
                    for (int i = 0 ; i < EFFECT.length ; i++)
                    {            
                        G2.setPaint(EFFECT_GRADIENT.getColorAt(i / 100f));         
                        G2.draw(EFFECT[i]);
                    }   
                    break;
                    
                case EFFECT_CONE:
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.0399f,
                        0.04f,
                        0.1799f,
                        0.18f,                        
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 76),
                        new java.awt.Color(223, 223, 223, 127),
                        new java.awt.Color(255, 255, 255, 124),
                        new java.awt.Color(9, 9, 9, 51),
                        new java.awt.Color(0, 0, 0, 50),                        
                        new java.awt.Color(0, 0, 0, 0)                        
                    };
                    EFFECT_GRADIENT = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(100,0), new java.awt.geom.Point2D.Double(0, 0), EFFECT_FRACTIONS, EFFECT_COLORS);                                        
                    for (int i = 0 ; i < 100 ; i++)
                    {                                          
                        EFFECT[i] = eu.hansolo.steelseries.tools.Scaler.INSTANCE.scale(FRAME_MAIN_AREA, scale);            
                        scale -= 0.01f;            
                    } 
                    G2.setStroke(new java.awt.BasicStroke(1.5f));                                
                    for (int i = 0 ; i < EFFECT.length ; i++)
                    {            
                        G2.setPaint(EFFECT_GRADIENT.getColorAt(i / 100f));         
                        G2.draw(EFFECT[i]);
                    }   
                    break;
                    
                case EFFECT_TORUS:
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.08f,
                        0.1799f,
                        0.18f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 76),
                        new java.awt.Color(255, 255, 255, 64),
                        new java.awt.Color(13, 13, 13, 51),
                        new java.awt.Color(0, 0, 0, 50),                        
                        new java.awt.Color(0, 0, 0, 0)                        
                    };
                    EFFECT_GRADIENT = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(100,0), new java.awt.geom.Point2D.Double(0, 0), EFFECT_FRACTIONS, EFFECT_COLORS);                                        
                    for (int i = 0 ; i < 100 ; i++)
                    {                                          
                        EFFECT[i] = eu.hansolo.steelseries.tools.Scaler.INSTANCE.scale(FRAME_MAIN_AREA, scale);            
                        scale -= 0.01f;            
                    } 
                    G2.setStroke(new java.awt.BasicStroke(1.5f));                                
                    for (int i = 0 ; i < EFFECT.length ; i++)
                    {            
                        G2.setPaint(EFFECT_GRADIENT.getColorAt(i / 100f));         
                        G2.draw(EFFECT[i]);
                    }   
                    break;
                    
                case EFFECT_INNER_FRAME:
                    final java.awt.Shape EFFECT_BIGINNERFRAME = eu.hansolo.steelseries.tools.Scaler.INSTANCE.scale(FRAME_MAIN_AREA, 0.8785046339035034);
                    final java.awt.geom.Point2D EFFECT_BIGINNERFRAME_START = new java.awt.geom.Point2D.Double(0, EFFECT_BIGINNERFRAME.getBounds2D().getMinY() );
                    final java.awt.geom.Point2D EFFECT_BIGINNERFRAME_STOP = new java.awt.geom.Point2D.Double(0, EFFECT_BIGINNERFRAME.getBounds2D().getMaxY() );
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.3f,                        
                        0.5f,                        
                        0.71f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 183),
                        new java.awt.Color(148, 148, 148, 25),                                                
                        new java.awt.Color(0, 0, 0, 159),
                        new java.awt.Color(0, 0, 0, 81),                        
                        new java.awt.Color(255, 255, 255, 158)
                    };
                    final java.awt.LinearGradientPaint EFFECT_BIGINNERFRAME_GRADIENT = new java.awt.LinearGradientPaint(EFFECT_BIGINNERFRAME_START, EFFECT_BIGINNERFRAME_STOP, EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_BIGINNERFRAME_GRADIENT);
                    G2.fill(EFFECT_BIGINNERFRAME);
                    break;
            }
            
            final java.awt.geom.GeneralPath FRAME_INNERFRAME = new java.awt.geom.GeneralPath();
            FRAME_INNERFRAME.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            FRAME_INNERFRAME.moveTo(IMAGE_WIDTH * 0.07906976744186046, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_INNERFRAME.curveTo(IMAGE_WIDTH * 0.07906976744186046, IMAGE_HEIGHT * 0.41304347826086957 * VERTICAL_SCALE, IMAGE_WIDTH * 0.2651162790697674, IMAGE_HEIGHT * 0.12318840579710146 * VERTICAL_SCALE, IMAGE_WIDTH * 0.49767441860465117, IMAGE_HEIGHT * 0.12318840579710146 * VERTICAL_SCALE);
            FRAME_INNERFRAME.curveTo(IMAGE_WIDTH * 0.7302325581395349, IMAGE_HEIGHT * 0.12318840579710146 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9162790697674419, IMAGE_HEIGHT * 0.41304347826086957 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9162790697674419, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_INNERFRAME.curveTo(IMAGE_WIDTH * 0.9162790697674419, IMAGE_HEIGHT * 0.8115942028985508 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8768115942028986 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8768115942028986 * VERTICAL_SCALE);
            FRAME_INNERFRAME.lineTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8768115942028986 * VERTICAL_SCALE);
            FRAME_INNERFRAME.curveTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8768115942028986 * VERTICAL_SCALE, IMAGE_WIDTH * 0.07906976744186046, IMAGE_HEIGHT * 0.8115942028985508 * VERTICAL_SCALE, IMAGE_WIDTH * 0.07906976744186046, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
            FRAME_INNERFRAME.closePath();
            G2.setColor(java.awt.Color.WHITE);
            final java.awt.geom.Area FRAME_INNERFRAME_AREA = new java.awt.geom.Area(FRAME_INNERFRAME);
            FRAME_INNERFRAME_AREA.subtract(SUBTRACT);
            G2.fill(FRAME_INNERFRAME_AREA);
        }

        G2.dispose();

        return image;
    }

    @Override
    protected java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH)
    {
        return create_BACKGROUND_Image(WIDTH, "", "", null);
    }

    @Override
    protected java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH, final String TITLE, final String UNIT_STRING, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        final double VERTICAL_SCALE;
        
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, (int) (0.641860465116279 * WIDTH), java.awt.Transparency.TRANSLUCENT);
            VERTICAL_SCALE = 1.0;
        }
        else
        {
            VERTICAL_SCALE = 0.641860465116279;
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();
        
        boolean fadeInOut = false;
        
        final java.awt.geom.GeneralPath GAUGE_BACKGROUND = new java.awt.geom.GeneralPath();
        GAUGE_BACKGROUND.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        GAUGE_BACKGROUND.moveTo(IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.42028985507246375 * VERTICAL_SCALE, IMAGE_WIDTH * 0.26976744186046514, IMAGE_HEIGHT * 0.13043478260869565 * VERTICAL_SCALE, IMAGE_WIDTH * 0.49767441860465117, IMAGE_HEIGHT * 0.13043478260869565 * VERTICAL_SCALE);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.7255813953488373, IMAGE_HEIGHT * 0.13043478260869565 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.42028985507246375 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.8188405797101449 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE);
        GAUGE_BACKGROUND.lineTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8695652173913043 * VERTICAL_SCALE, IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.8115942028985508 * VERTICAL_SCALE, IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.7753623188405797 * VERTICAL_SCALE);
        GAUGE_BACKGROUND.closePath();
        final java.awt.geom.Point2D GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, GAUGE_BACKGROUND.getBounds2D().getMinY() );
        final java.awt.geom.Point2D GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, GAUGE_BACKGROUND.getBounds2D().getMaxY() );
        final float[] GAUGE_BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.4f,
            1.0f
        };
        
        java.awt.Paint backgroundPaint = null;
        
        // Set custom background paint if selected        
        if (getCustomBackground() != null && getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {            
            G2.setPaint(getCustomBackground());
        }
        else
        {
            final java.awt.Color[] GAUGE_BACKGROUND_COLORS =
            {
                getBackgroundColor().GRADIENT_START_COLOR,
                getBackgroundColor().GRADIENT_FRACTION_COLOR,
                getBackgroundColor().GRADIENT_STOP_COLOR
            };
            
            if (getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.BRUSHED_METAL)
            {
                backgroundPaint = new java.awt.TexturePaint(UTIL.createBrushMetalTexture(null, GAUGE_BACKGROUND.getBounds().width, GAUGE_BACKGROUND.getBounds().height), GAUGE_BACKGROUND.getBounds());
            }
            else if(getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.STAINLESS)
            {                
                final float[] STAINLESS_FRACTIONS =  
                {
                    0f,
                    0.03f,
                    0.10f,
                    0.14f,
                    0.24f,
                    0.33f,
                    0.38f,
                    0.5f,
                    0.62f,
                    0.67f,
                    0.76f,
                    0.81f,
                    0.85f,
                    0.97f,
                    1.0f 
                };

                // Define the colors of the conical gradient paint 
                final java.awt.Color[] STAINLESS_COLORS = 
                { 
                    new java.awt.Color(0xFDFDFD),
                    new java.awt.Color(0xFDFDFD),
                    new java.awt.Color(0xB2B2B4),
                    new java.awt.Color(0xACACAE),
                    new java.awt.Color(0xFDFDFD),
                    new java.awt.Color(0x6E6E70),
                    new java.awt.Color(0x6E6E70),
                    new java.awt.Color(0xFDFDFD),
                    new java.awt.Color(0x6E6E70),
                    new java.awt.Color(0x6E6E70),
                    new java.awt.Color(0xFDFDFD),
                    new java.awt.Color(0xACACAE),
                    new java.awt.Color(0xB2B2B4),
                    new java.awt.Color(0xFDFDFD),
                    new java.awt.Color(0xFDFDFD) 
                };

                // Define the conical gradient paint 
                backgroundPaint = new eu.hansolo.steelseries.tools.ConicalGradientPaint(false, getCenter(), -0.45f, STAINLESS_FRACTIONS, STAINLESS_COLORS);
            }    
            else if(getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.STAINLESS_GRINDED)
            {                
                backgroundPaint = new java.awt.TexturePaint(BACKGROUND_FACTORY.STAINLESS_GRINDED_TEXTURE, new java.awt.Rectangle(0, 0, 100, 100));
            }
            else if(getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.CARBON)
            {
                backgroundPaint = new java.awt.TexturePaint(BACKGROUND_FACTORY.CARBON_FIBRE_TEXTURE, new java.awt.Rectangle(0, 0, 12, 12));
                fadeInOut = true;
            }
            else if(getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.PUNCHED_SHEET)
            {
                backgroundPaint = new java.awt.TexturePaint(BACKGROUND_FACTORY.PUNCHED_SHEET_TEXTURE, new java.awt.Rectangle(0, 0, 12, 12));
                fadeInOut = true;
            }
            else
            {
                backgroundPaint = new java.awt.LinearGradientPaint(GAUGE_BACKGROUND_START, GAUGE_BACKGROUND_STOP, GAUGE_BACKGROUND_FRACTIONS, GAUGE_BACKGROUND_COLORS);
            }
            G2.setPaint(backgroundPaint);
        }
        G2.fill(GAUGE_BACKGROUND);
        
        // Create inner shadow on background shape
        final java.awt.image.BufferedImage CLP;
        if (getCustomBackground() != null && getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {
            CLP = eu.hansolo.steelseries.tools.Shadow.INSTANCE.createInnerShadow((java.awt.Shape) GAUGE_BACKGROUND, getCustomBackground(), 0, 0.65f, java.awt.Color.BLACK, 20, 315);
        }
        else
        {
            CLP = eu.hansolo.steelseries.tools.Shadow.INSTANCE.createInnerShadow((java.awt.Shape) GAUGE_BACKGROUND, backgroundPaint, 0, 0.65f, java.awt.Color.BLACK, 20, 315);
        }
        G2.drawImage(CLP, GAUGE_BACKGROUND.getBounds().x, GAUGE_BACKGROUND.getBounds().y, null);

        // Draw an overlay gradient that gives the carbon fibre a more realistic look
        if (fadeInOut)
        {
            final float[] SHADOW_OVERLAY_FRACTIONS =
            {
                0.0f,
                0.4f,
                0.6f,
                1.0f
            };            
            final java.awt.Color[] SHADOW_OVERLAY_COLORS =
            {
                new java.awt.Color(0f, 0f, 0f, 0.6f),
                new java.awt.Color(0f, 0f, 0f, 0.0f),
                new java.awt.Color(0f, 0f, 0f, 0.0f),
                new java.awt.Color(0f, 0f, 0f, 0.6f)
            };
            final java.awt.LinearGradientPaint SHADOW_OVERLAY_GRADIENT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Double(GAUGE_BACKGROUND.getBounds().getMinX(), 0), new java.awt.geom.Point2D.Double(GAUGE_BACKGROUND.getBounds().getMaxX(), 0), SHADOW_OVERLAY_FRACTIONS, SHADOW_OVERLAY_COLORS);
            G2.setPaint(SHADOW_OVERLAY_GRADIENT);
            G2.fill(GAUGE_BACKGROUND);
        }   
        
        // Draw the custom layer if selected
        if (isCustomLayerVisible())
        {
            G2.drawImage(UTIL.getScaledInstance(getCustomLayer(), IMAGE_WIDTH, (int)(IMAGE_HEIGHT * VERTICAL_SCALE), java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC), 0, 0, null);
        }

        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);

        if (!TITLE.isEmpty())
        {
            if (isLabelColorFromThemeEnabled())
            {
                G2.setColor(getBackgroundColor().LABEL_COLOR);
            }
            else
            {
                G2.setColor(getLabelColor());
            }
            G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.04672897196261682 * IMAGE_WIDTH)));
            final java.awt.font.TextLayout TITLE_LAYOUT = new java.awt.font.TextLayout(TITLE, G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D TITLE_BOUNDARY = TITLE_LAYOUT.getBounds();
            G2.drawString(TITLE, (float)((IMAGE_WIDTH - TITLE_BOUNDARY.getWidth()) / 2.0), (float)(0.44f * IMAGE_HEIGHT * VERTICAL_SCALE) + TITLE_LAYOUT.getAscent() - TITLE_LAYOUT.getDescent());
        }

        if (!UNIT_STRING.isEmpty())
        {
            if (isLabelColorFromThemeEnabled())
            {
                G2.setColor(getBackgroundColor().LABEL_COLOR);
            }
            else
            {
                G2.setColor(getLabelColor());
            }
            G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.04672897196261682 * IMAGE_WIDTH)));
            final java.awt.font.TextLayout UNIT_LAYOUT = new java.awt.font.TextLayout(UNIT_STRING, G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D UNIT_BOUNDARY = UNIT_LAYOUT.getBounds();
            G2.drawString(UNIT_STRING, (float)((IMAGE_WIDTH - UNIT_BOUNDARY.getWidth()) / 2.0), 0.52f * IMAGE_HEIGHT + UNIT_LAYOUT.getAscent() - UNIT_LAYOUT.getDescent());
        }

        G2.dispose();

        return image;
    }
    
    private java.awt.image.BufferedImage create_POSTS_Image(final int WIDTH, java.awt.image.BufferedImage image)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = image.getWidth();
        int IMAGE_HEIGHT = (int) (image.getHeight() * 0.6418604651);
                        
        switch(getKnobType())
        {
            case SMALL_STD_KNOB:                
                final java.awt.geom.Ellipse2D CENTER_KNOB_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.46261683106422424, IMAGE_HEIGHT * 0.7153284549713135, IMAGE_WIDTH * 0.07943925261497498, IMAGE_HEIGHT * 0.1313868761062622);
                final java.awt.geom.Point2D CENTER_KNOB_FRAME_START = new java.awt.geom.Point2D.Double(0, CENTER_KNOB_FRAME.getBounds2D().getMinY() );
                final java.awt.geom.Point2D CENTER_KNOB_FRAME_STOP = new java.awt.geom.Point2D.Double(0, CENTER_KNOB_FRAME.getBounds2D().getMaxY() );
                final float[] CENTER_KNOB_FRAME_FRACTIONS = 
                {
                    0.0f,
                    0.46f,
                    1.0f
                };
                final java.awt.Color[] CENTER_KNOB_FRAME_COLORS = 
                {
                    new java.awt.Color(180, 180, 180, 255),
                    new java.awt.Color(63, 63, 63, 255),
                    new java.awt.Color(40, 40, 40, 255)
                };
                final java.awt.LinearGradientPaint CENTER_KNOB_FRAME_GRADIENT = new java.awt.LinearGradientPaint(CENTER_KNOB_FRAME_START, CENTER_KNOB_FRAME_STOP, CENTER_KNOB_FRAME_FRACTIONS, CENTER_KNOB_FRAME_COLORS);
                G2.setPaint(CENTER_KNOB_FRAME_GRADIENT);
                G2.fill(CENTER_KNOB_FRAME);

                final java.awt.geom.Ellipse2D CENTER_KNOB_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4719626307487488, IMAGE_HEIGHT * 0.7299270033836365, IMAGE_WIDTH * 0.060747623443603516, IMAGE_HEIGHT * 0.10218977928161621);
                final java.awt.geom.Point2D CENTER_KNOB_MAIN_START = new java.awt.geom.Point2D.Double(0, CENTER_KNOB_MAIN.getBounds2D().getMinY() );
                final java.awt.geom.Point2D CENTER_KNOB_MAIN_STOP = new java.awt.geom.Point2D.Double(0, CENTER_KNOB_MAIN.getBounds2D().getMaxY() );
                final float[] CENTER_KNOB_MAIN_FRACTIONS = 
                {
                    0.0f,
                    0.5f,
                    1.0f
                };

                final java.awt.Color[] CENTER_KNOB_MAIN_COLORS;
                switch(getModel().getKnobStyle())
                {
                    case BLACK:
                        CENTER_KNOB_MAIN_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(0xBFBFBF),
                            new java.awt.Color(0x2B2A2F),
                            new java.awt.Color(0x7D7E80)
                        };
                        break;

                    case BRASS:
                        CENTER_KNOB_MAIN_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(0xDFD0AE),
                            new java.awt.Color(0x7A5E3E),
                            new java.awt.Color(0xCFBE9D)
                        };
                        break;

                    case SILVER:

                    default:
                        CENTER_KNOB_MAIN_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(0xD7D7D7),
                            new java.awt.Color(0x747474),
                            new java.awt.Color(0xD7D7D7)
                        };
                        break;
                }
                final java.awt.LinearGradientPaint CENTER_KNOB_MAIN_GRADIENT = new java.awt.LinearGradientPaint(CENTER_KNOB_MAIN_START, CENTER_KNOB_MAIN_STOP, CENTER_KNOB_MAIN_FRACTIONS, CENTER_KNOB_MAIN_COLORS);
                G2.setPaint(CENTER_KNOB_MAIN_GRADIENT);
                G2.fill(CENTER_KNOB_MAIN);

                final java.awt.geom.Ellipse2D CENTER_KNOB_INNERSHADOW = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4719626307487488, IMAGE_HEIGHT * 0.7299270033836365, IMAGE_WIDTH * 0.060747623443603516, IMAGE_HEIGHT * 0.10218977928161621);
                final java.awt.geom.Point2D CENTER_KNOB_INNERSHADOW_CENTER = new java.awt.geom.Point2D.Double( (0.4930232558139535 * IMAGE_WIDTH), (0.7608695652173914 * IMAGE_HEIGHT) );
                final float[] CENTER_KNOB_INNERSHADOW_FRACTIONS = 
                {
                    0.0f,
                    0.75f,
                    0.76f,
                    1.0f
                };
                final java.awt.Color[] CENTER_KNOB_INNERSHADOW_COLORS = 
                {
                    new java.awt.Color(0, 0, 0, 0),
                    new java.awt.Color(0, 0, 0, 0),
                    new java.awt.Color(0, 0, 0, 1),
                    new java.awt.Color(0, 0, 0, 51)
                };
                final java.awt.RadialGradientPaint CENTER_KNOB_INNERSHADOW_GRADIENT = new java.awt.RadialGradientPaint(CENTER_KNOB_INNERSHADOW_CENTER, (float)(0.03255813953488372 * IMAGE_WIDTH), CENTER_KNOB_INNERSHADOW_FRACTIONS, CENTER_KNOB_INNERSHADOW_COLORS);
                G2.setPaint(CENTER_KNOB_INNERSHADOW_GRADIENT);
                G2.fill(CENTER_KNOB_INNERSHADOW);
                break;
            
            case BIG_STD_KNOB:                
                final java.awt.geom.Ellipse2D BIGCENTER_BACKGROUNDFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.44859811663627625, IMAGE_HEIGHT * 0.6934306621551514, IMAGE_WIDTH * 0.1074766218662262, IMAGE_HEIGHT * 0.17518246173858643);
                final java.awt.geom.Point2D BIGCENTER_BACKGROUNDFRAME_START = new java.awt.geom.Point2D.Double(0, BIGCENTER_BACKGROUNDFRAME.getBounds2D().getMinY() );
                final java.awt.geom.Point2D BIGCENTER_BACKGROUNDFRAME_STOP = new java.awt.geom.Point2D.Double(0, BIGCENTER_BACKGROUNDFRAME.getBounds2D().getMaxY() );
                final float[] BIGCENTER_BACKGROUNDFRAME_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] BIGCENTER_BACKGROUNDFRAME_COLORS;                    
                switch (getModel().getKnobStyle())
                {
                    case BLACK:
                        BIGCENTER_BACKGROUNDFRAME_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(129, 133, 136, 255),
                            new java.awt.Color(61, 61, 73, 255)
                        };
                        break;

                    case BRASS:
                        BIGCENTER_BACKGROUNDFRAME_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(143, 117, 80, 255),
                            new java.awt.Color(100, 76, 49, 255)
                        };
                        break;

                    case SILVER:

                    default:
                        BIGCENTER_BACKGROUNDFRAME_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(152, 152, 152, 255),
                            new java.awt.Color(118, 121, 126, 255)
                        };
                        break;
                }
                final java.awt.LinearGradientPaint BIGCENTER_BACKGROUNDFRAME_GRADIENT = new java.awt.LinearGradientPaint(BIGCENTER_BACKGROUNDFRAME_START, BIGCENTER_BACKGROUNDFRAME_STOP, BIGCENTER_BACKGROUNDFRAME_FRACTIONS, BIGCENTER_BACKGROUNDFRAME_COLORS);
                G2.setPaint(BIGCENTER_BACKGROUNDFRAME_GRADIENT);
                G2.fill(BIGCENTER_BACKGROUNDFRAME);

                final java.awt.geom.Ellipse2D BIGCENTER_BACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.44859811663627625, IMAGE_HEIGHT * 0.7007299065589905, IMAGE_WIDTH * 0.1074766218662262, IMAGE_HEIGHT * 0.1605839729309082);
                final java.awt.geom.Point2D BIGCENTER_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BIGCENTER_BACKGROUND.getBounds2D().getMinY() );
                final java.awt.geom.Point2D BIGCENTER_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BIGCENTER_BACKGROUND.getBounds2D().getMaxY() );
                final float[] BIGCENTER_BACKGROUND_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] BIGCENTER_BACKGROUND_COLORS;
                switch(getModel().getKnobStyle())
                {
                    case BLACK:
                        BIGCENTER_BACKGROUND_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(26, 27, 32, 255),
                            new java.awt.Color(96, 97, 102, 255)
                        };
                        break;

                    case BRASS:
                        BIGCENTER_BACKGROUND_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(98, 75, 49, 255),
                            new java.awt.Color(149, 109, 54, 255)
                        };
                        break;

                    case SILVER:

                    default:
                        BIGCENTER_BACKGROUND_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(118, 121, 126, 255),
                            new java.awt.Color(191, 191, 191, 255)
                        };
                        break;
                }
                final java.awt.LinearGradientPaint BIGCENTER_BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(BIGCENTER_BACKGROUND_START, BIGCENTER_BACKGROUND_STOP, BIGCENTER_BACKGROUND_FRACTIONS, BIGCENTER_BACKGROUND_COLORS);
                G2.setPaint(BIGCENTER_BACKGROUND_GRADIENT);
                G2.fill(BIGCENTER_BACKGROUND);
               
                final java.awt.geom.Ellipse2D BIGCENTER_FOREGROUNDFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.46261683106422424, IMAGE_HEIGHT * 0.7153284549713135, IMAGE_WIDTH * 0.07943925261497498, IMAGE_HEIGHT * 0.1313868761062622);
                final java.awt.geom.Point2D BIGCENTER_FOREGROUNDFRAME_START = new java.awt.geom.Point2D.Double(0, BIGCENTER_FOREGROUNDFRAME.getBounds2D().getMinY() );
                final java.awt.geom.Point2D BIGCENTER_FOREGROUNDFRAME_STOP = new java.awt.geom.Point2D.Double(0, BIGCENTER_FOREGROUNDFRAME.getBounds2D().getMaxY() );
                final float[] BIGCENTER_FOREGROUNDFRAME_FRACTIONS = 
                {
                    0.0f,
                    0.47f,
                    1.0f
                };
                final java.awt.Color[] BIGCENTER_FOREGROUNDFRAME_COLORS;
                switch(getModel().getKnobStyle())
                {
                    case BLACK:
                        BIGCENTER_FOREGROUNDFRAME_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(191, 191, 191, 255),
                            new java.awt.Color(56, 57, 61, 255),
                            new java.awt.Color(143, 144, 146, 255)
                        };
                        break;

                    case BRASS:
                        BIGCENTER_FOREGROUNDFRAME_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(147, 108, 54, 255),
                            new java.awt.Color(82, 66, 50, 255),
                            new java.awt.Color(147, 108, 54, 255)
                        };
                        break;

                    case SILVER:

                    default:
                        BIGCENTER_FOREGROUNDFRAME_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(191, 191, 191, 255),
                            new java.awt.Color(116, 116, 116, 255),
                            new java.awt.Color(143, 144, 146, 255)
                        };
                        break;
                }
                final java.awt.LinearGradientPaint BIGCENTER_FOREGROUNDFRAME_GRADIENT = new java.awt.LinearGradientPaint(BIGCENTER_FOREGROUNDFRAME_START, BIGCENTER_FOREGROUNDFRAME_STOP, BIGCENTER_FOREGROUNDFRAME_FRACTIONS, BIGCENTER_FOREGROUNDFRAME_COLORS);
                G2.setPaint(BIGCENTER_FOREGROUNDFRAME_GRADIENT);
                G2.fill(BIGCENTER_FOREGROUNDFRAME);

                final java.awt.geom.Ellipse2D BIGCENTER_FOREGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.46261683106422424, IMAGE_HEIGHT * 0.7226277589797974, IMAGE_WIDTH * 0.07943925261497498, IMAGE_HEIGHT * 0.11678832769393921);
                final java.awt.geom.Point2D BIGCENTER_FOREGROUND_START = new java.awt.geom.Point2D.Double(0, BIGCENTER_FOREGROUND.getBounds2D().getMinY() );
                final java.awt.geom.Point2D BIGCENTER_FOREGROUND_STOP = new java.awt.geom.Point2D.Double(0, BIGCENTER_FOREGROUND.getBounds2D().getMaxY() );
                final float[] BIGCENTER_FOREGROUND_FRACTIONS = 
                {
                    0.0f,
                    0.21f,
                    0.5f,
                    0.78f,
                    1.0f
                };
                final java.awt.Color[] BIGCENTER_FOREGROUND_COLORS;
                switch(getModel().getKnobStyle())
                {
                    case BLACK:
                        BIGCENTER_FOREGROUND_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(191, 191, 191, 255),
                            new java.awt.Color(94, 93, 99, 255),
                            new java.awt.Color(43, 42, 47, 255),
                            new java.awt.Color(78, 79, 81, 255),
                            new java.awt.Color(143, 144, 146, 255)
                        };
                        break;

                    case BRASS:
                        BIGCENTER_FOREGROUND_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(223, 208, 174, 255),
                            new java.awt.Color(159, 136, 104, 255),
                            new java.awt.Color(122, 94, 62, 255),
                            new java.awt.Color(159, 136, 104, 255),
                            new java.awt.Color(223, 208, 174, 255)
                        };
                        break;

                    case SILVER:

                    default:
                        BIGCENTER_FOREGROUND_COLORS = new java.awt.Color[]
                        {
                            new java.awt.Color(215, 215, 215, 255),
                            new java.awt.Color(139, 142, 145, 255),
                            new java.awt.Color(100, 100, 100, 255),
                            new java.awt.Color(139, 142, 145, 255),
                            new java.awt.Color(215, 215, 215, 255)
                        };
                        break;
                }
                final java.awt.LinearGradientPaint BIGCENTER_FOREGROUND_GRADIENT = new java.awt.LinearGradientPaint(BIGCENTER_FOREGROUND_START, BIGCENTER_FOREGROUND_STOP, BIGCENTER_FOREGROUND_FRACTIONS, BIGCENTER_FOREGROUND_COLORS);
                G2.setPaint(BIGCENTER_FOREGROUND_GRADIENT);
                G2.fill(BIGCENTER_FOREGROUND);
                break;
                
            case BIG_CHROME_KNOB:
                final java.awt.geom.Ellipse2D CHROMEKNOB_BACKFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.44859811663627625, IMAGE_HEIGHT * 0.6934306621551514, IMAGE_WIDTH * 0.1074766218662262, IMAGE_HEIGHT * 0.17518246173858643);
                final java.awt.geom.Point2D CHROMEKNOB_BACKFRAME_START = new java.awt.geom.Point2D.Double( (0.4697674418604651 * IMAGE_WIDTH), (0.7028985507246377 * IMAGE_HEIGHT) );
                final java.awt.geom.Point2D CHROMEKNOB_BACKFRAME_STOP = new java.awt.geom.Point2D.Double( ((0.4697674418604651 + 0.056689037569133544) * IMAGE_WIDTH), ((0.7028985507246377 + 0.14134134935940434) * IMAGE_HEIGHT) );
                final float[] CHROMEKNOB_BACKFRAME_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] CHROMEKNOB_BACKFRAME_COLORS = 
                {
                    new java.awt.Color(129, 139, 140, 255),
                    new java.awt.Color(166, 171, 175, 255)
                };
                final java.awt.LinearGradientPaint CHROMEKNOB_BACKFRAME_GRADIENT = new java.awt.LinearGradientPaint(CHROMEKNOB_BACKFRAME_START, CHROMEKNOB_BACKFRAME_STOP, CHROMEKNOB_BACKFRAME_FRACTIONS, CHROMEKNOB_BACKFRAME_COLORS);
                G2.setPaint(CHROMEKNOB_BACKFRAME_GRADIENT);
                G2.fill(CHROMEKNOB_BACKFRAME);

                final java.awt.geom.Ellipse2D CHROMEKNOB_BACK = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.44859811663627625, IMAGE_HEIGHT * 0.7007299065589905, IMAGE_WIDTH * 0.1074766218662262, IMAGE_HEIGHT * 0.16788321733474731);
                final java.awt.geom.Point2D CHROMEKNOB_BACK_CENTER = new java.awt.geom.Point2D.Double(CHROMEKNOB_BACK.getCenterX(), CHROMEKNOB_BACK.getCenterY());
                final float[] CHROMEKNOB_BACK_FRACTIONS = 
                {
                    0.0f,
                    0.09f,
                    0.12f,
                    0.16f,
                    0.25f,
                    0.29f,
                    0.33f,
                    0.38f,
                    0.48f,
                    0.52f,
                    0.65f,
                    0.69f,
                    0.8f,
                    0.83f,
                    0.87f,
                    0.97f,
                    1.0f
                };
                final java.awt.Color[] CHROMEKNOB_BACK_COLORS = 
                {
                    new java.awt.Color(255, 255, 255, 255),
                    new java.awt.Color(255, 255, 255, 255),
                    new java.awt.Color(136, 136, 138, 255),
                    new java.awt.Color(164, 185, 190, 255),
                    new java.awt.Color(158, 179, 182, 255),
                    new java.awt.Color(112, 112, 112, 255),
                    new java.awt.Color(221, 227, 227, 255),
                    new java.awt.Color(155, 176, 179, 255),
                    new java.awt.Color(156, 176, 177, 255),
                    new java.awt.Color(254, 255, 255, 255),
                    new java.awt.Color(255, 255, 255, 255),
                    new java.awt.Color(156, 180, 180, 255),
                    new java.awt.Color(198, 209, 211, 255),
                    new java.awt.Color(246, 248, 247, 255),
                    new java.awt.Color(204, 216, 216, 255),
                    new java.awt.Color(164, 188, 190, 255),
                    new java.awt.Color(255, 255, 255, 255)
                };
                final eu.hansolo.steelseries.tools.ConicalGradientPaint CHROMEKNOB_BACK_GRADIENT = new eu.hansolo.steelseries.tools.ConicalGradientPaint(false, CHROMEKNOB_BACK_CENTER, 0, CHROMEKNOB_BACK_FRACTIONS, CHROMEKNOB_BACK_COLORS);
                G2.setPaint(CHROMEKNOB_BACK_GRADIENT);
                G2.fill(CHROMEKNOB_BACK);

                final java.awt.geom.Ellipse2D CHROMEKNOB_FOREFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.47663551568984985, IMAGE_HEIGHT * 0.7445255517959595, IMAGE_WIDTH * 0.05140185356140137, IMAGE_HEIGHT * 0.0802919864654541);
                final java.awt.geom.Point2D CHROMEKNOB_FOREFRAME_START = new java.awt.geom.Point2D.Double( (0.48372093023255813 * IMAGE_WIDTH), (0.7463768115942029 * IMAGE_HEIGHT) );
                final java.awt.geom.Point2D CHROMEKNOB_FOREFRAME_STOP = new java.awt.geom.Point2D.Double( ((0.48372093023255813 + 0.028609869479898676) * IMAGE_WIDTH), ((0.7463768115942029 + 0.0660827050587352) * IMAGE_HEIGHT) );
                final float[] CHROMEKNOB_FOREFRAME_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] CHROMEKNOB_FOREFRAME_COLORS = 
                {
                    new java.awt.Color(225, 235, 232, 255),
                    new java.awt.Color(196, 207, 207, 255)
                };
                final java.awt.LinearGradientPaint CHROMEKNOB_FOREFRAME_GRADIENT = new java.awt.LinearGradientPaint(CHROMEKNOB_FOREFRAME_START, CHROMEKNOB_FOREFRAME_STOP, CHROMEKNOB_FOREFRAME_FRACTIONS, CHROMEKNOB_FOREFRAME_COLORS);
                G2.setPaint(CHROMEKNOB_FOREFRAME_GRADIENT);
                G2.fill(CHROMEKNOB_FOREFRAME);
                
                final java.awt.geom.Ellipse2D CHROMEKNOB_FORE = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.47663551568984985, IMAGE_HEIGHT * 0.7445255517959595, IMAGE_WIDTH * 0.05140185356140137, IMAGE_HEIGHT * 0.07299268245697021);
                final java.awt.geom.Point2D CHROMEKNOB_FORE_START = new java.awt.geom.Point2D.Double( (0.48372093023255813 * IMAGE_WIDTH), (0.7463768115942029 * IMAGE_HEIGHT) );
                final java.awt.geom.Point2D CHROMEKNOB_FORE_STOP = new java.awt.geom.Point2D.Double( ((0.48372093023255813 + 0.023408075029008005) * IMAGE_WIDTH), ((0.7463768115942029 + 0.05406766777532881) * IMAGE_HEIGHT) );
                final float[] CHROMEKNOB_FORE_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] CHROMEKNOB_FORE_COLORS = 
                {
                    new java.awt.Color(237, 239, 237, 255),
                    new java.awt.Color(148, 161, 161, 255)
                };
                final java.awt.LinearGradientPaint CHROMEKNOB_FORE_GRADIENT = new java.awt.LinearGradientPaint(CHROMEKNOB_FORE_START, CHROMEKNOB_FORE_STOP, CHROMEKNOB_FORE_FRACTIONS, CHROMEKNOB_FORE_COLORS);
                G2.setPaint(CHROMEKNOB_FORE_GRADIENT);
                G2.fill(CHROMEKNOB_FORE);
                break;
                
            case METAL_KNOB:
                final java.awt.geom.Ellipse2D METALKNOB_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4579439163208008, IMAGE_HEIGHT * 0.7153284549713135, IMAGE_WIDTH * 0.08411216735839844, IMAGE_HEIGHT * 0.1313868761062622);
                final java.awt.geom.Point2D METALKNOB_FRAME_START = new java.awt.geom.Point2D.Double(0, METALKNOB_FRAME.getBounds2D().getMinY() );
                final java.awt.geom.Point2D METALKNOB_FRAME_STOP = new java.awt.geom.Point2D.Double(0, METALKNOB_FRAME.getBounds2D().getMaxY() );
                final float[] METALKNOB_FRAME_FRACTIONS = 
                {
                    0.0f,
                    0.47f,
                    1.0f
                };
                final java.awt.Color[] METALKNOB_FRAME_COLORS = 
                {
                    new java.awt.Color(92, 95, 101, 255),
                    new java.awt.Color(46, 49, 53, 255),
                    new java.awt.Color(22, 23, 26, 255)
                };
                final java.awt.LinearGradientPaint METALKNOB_FRAME_GRADIENT = new java.awt.LinearGradientPaint(METALKNOB_FRAME_START, METALKNOB_FRAME_STOP, METALKNOB_FRAME_FRACTIONS, METALKNOB_FRAME_COLORS);
                G2.setPaint(METALKNOB_FRAME_GRADIENT);
                G2.fill(METALKNOB_FRAME);
                
                final java.awt.geom.Ellipse2D METALKNOB_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.46261683106422424, IMAGE_HEIGHT * 0.7226277589797974, IMAGE_WIDTH * 0.0747663676738739, IMAGE_HEIGHT * 0.11678832769393921);
                final java.awt.geom.Point2D METALKNOB_MAIN_START = new java.awt.geom.Point2D.Double(0, METALKNOB_MAIN.getBounds2D().getMinY() );
                final java.awt.geom.Point2D METALKNOB_MAIN_STOP = new java.awt.geom.Point2D.Double(0, METALKNOB_MAIN.getBounds2D().getMaxY() );
                final float[] METALKNOB_MAIN_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] METALKNOB_MAIN_COLORS;
                switch (getModel().getKnobStyle())
                {
                    case BLACK:
                        METALKNOB_MAIN_COLORS = new java.awt.Color[] 
                        {
                            new java.awt.Color(0x2B2A2F),
                            new java.awt.Color(0x1A1B20)
                        };
                        break;

                    case BRASS:
                        METALKNOB_MAIN_COLORS = new java.awt.Color[] 
                        {
                            new java.awt.Color(0x966E36),
                            new java.awt.Color(0x7C5F3D)
                        };
                        break;

                    case SILVER:

                    default:
                        METALKNOB_MAIN_COLORS = new java.awt.Color[] 
                        {
                            new java.awt.Color(204, 204, 204, 255),
                            new java.awt.Color(87, 92, 98, 255)
                        };
                        break;
                }
                final java.awt.LinearGradientPaint METALKNOB_MAIN_GRADIENT = new java.awt.LinearGradientPaint(METALKNOB_MAIN_START, METALKNOB_MAIN_STOP, METALKNOB_MAIN_FRACTIONS, METALKNOB_MAIN_COLORS);
                G2.setPaint(METALKNOB_MAIN_GRADIENT);
                G2.fill(METALKNOB_MAIN);

                final java.awt.geom.GeneralPath METALKNOB_LOWERHL = new java.awt.geom.GeneralPath();
                METALKNOB_LOWERHL.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                METALKNOB_LOWERHL.moveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.8321167883211679);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.8102189781021898, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.8029197080291971, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.8029197080291971);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.8029197080291971, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.8102189781021898, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.8321167883211679);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.8394160583941606, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.8394160583941606, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.8394160583941606);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.8394160583941606, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.8394160583941606, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.8321167883211679);
                METALKNOB_LOWERHL.closePath();
                final java.awt.geom.Point2D METALKNOB_LOWERHL_CENTER = new java.awt.geom.Point2D.Double( (0.49767441860465117 * IMAGE_WIDTH), (0.8333333333333334 * IMAGE_HEIGHT) );
                final float[] METALKNOB_LOWERHL_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] METALKNOB_LOWERHL_COLORS = 
                {
                    new java.awt.Color(255, 255, 255, 153),
                    new java.awt.Color(255, 255, 255, 0)
                };
                final java.awt.RadialGradientPaint METALKNOB_LOWERHL_GRADIENT = new java.awt.RadialGradientPaint(METALKNOB_LOWERHL_CENTER, (float)(0.03255813953488372 * IMAGE_WIDTH), METALKNOB_LOWERHL_FRACTIONS, METALKNOB_LOWERHL_COLORS);
                G2.setPaint(METALKNOB_LOWERHL_GRADIENT);
                G2.fill(METALKNOB_LOWERHL);

                final java.awt.geom.GeneralPath METALKNOB_UPPERHL = new java.awt.geom.GeneralPath();
                METALKNOB_UPPERHL.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                METALKNOB_UPPERHL.moveTo(IMAGE_WIDTH * 0.5373831775700935, IMAGE_HEIGHT * 0.7518248175182481);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.7299270072992701, IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.7153284671532847, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7153284671532847);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.7153284671532847, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.7299270072992701, IMAGE_WIDTH * 0.46261682242990654, IMAGE_HEIGHT * 0.7518248175182481);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.7591240875912408, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.7664233576642335, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.7664233576642335);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.7664233576642335, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.7591240875912408, IMAGE_WIDTH * 0.5373831775700935, IMAGE_HEIGHT * 0.7518248175182481);
                METALKNOB_UPPERHL.closePath();
                final java.awt.geom.Point2D METALKNOB_UPPERHL_CENTER = new java.awt.geom.Point2D.Double( (0.4930232558139535 * IMAGE_WIDTH), (0.7101449275362319 * IMAGE_HEIGHT) );
                final float[] METALKNOB_UPPERHL_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] METALKNOB_UPPERHL_COLORS = 
                {
                    new java.awt.Color(255, 255, 255, 191),
                    new java.awt.Color(255, 255, 255, 0)
                };
                final java.awt.RadialGradientPaint METALKNOB_UPPERHL_GRADIENT = new java.awt.RadialGradientPaint(METALKNOB_UPPERHL_CENTER, (float)(0.04883720930232558 * IMAGE_WIDTH), METALKNOB_UPPERHL_FRACTIONS, METALKNOB_UPPERHL_COLORS);
                G2.setPaint(METALKNOB_UPPERHL_GRADIENT);
                G2.fill(METALKNOB_UPPERHL);

                final java.awt.geom.Ellipse2D METALKNOB_INNERFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4813084006309509, IMAGE_HEIGHT * 0.7518247961997986, IMAGE_WIDTH * 0.037383198738098145, IMAGE_HEIGHT * 0.0656934380531311);
                final java.awt.geom.Point2D METALKNOB_INNERFRAME_START = new java.awt.geom.Point2D.Double(0, METALKNOB_INNERFRAME.getBounds2D().getMinY() );
                final java.awt.geom.Point2D METALKNOB_INNERFRAME_STOP = new java.awt.geom.Point2D.Double(0, METALKNOB_INNERFRAME.getBounds2D().getMaxY() );
                final float[] METALKNOB_INNERFRAME_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] METALKNOB_INNERFRAME_COLORS = 
                {
                    new java.awt.Color(0, 0, 0, 255),
                    new java.awt.Color(204, 204, 204, 255)
                };
                final java.awt.LinearGradientPaint METALKNOB_INNERFRAME_GRADIENT = new java.awt.LinearGradientPaint(METALKNOB_INNERFRAME_START, METALKNOB_INNERFRAME_STOP, METALKNOB_INNERFRAME_FRACTIONS, METALKNOB_INNERFRAME_COLORS);
                G2.setPaint(METALKNOB_INNERFRAME_GRADIENT);
                G2.fill(METALKNOB_INNERFRAME);
                
                final java.awt.geom.Ellipse2D METALKNOB_INNERBACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.4859813153743744, IMAGE_HEIGHT * 0.7591241002082825, IMAGE_WIDTH * 0.02803739905357361, IMAGE_HEIGHT * 0.051094889640808105);
                final java.awt.geom.Point2D METALKNOB_INNERBACKGROUND_START = new java.awt.geom.Point2D.Double(0, METALKNOB_INNERBACKGROUND.getBounds2D().getMinY() );
                final java.awt.geom.Point2D METALKNOB_INNERBACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, METALKNOB_INNERBACKGROUND.getBounds2D().getMaxY() );
                final float[] METALKNOB_INNERBACKGROUND_FRACTIONS = 
                {
                    0.0f,
                    1.0f
                };
                final java.awt.Color[] METALKNOB_INNERBACKGROUND_COLORS = 
                {
                    new java.awt.Color(1, 6, 11, 255),
                    new java.awt.Color(50, 52, 56, 255)
                };
                final java.awt.LinearGradientPaint METALKNOB_INNERBACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(METALKNOB_INNERBACKGROUND_START, METALKNOB_INNERBACKGROUND_STOP, METALKNOB_INNERBACKGROUND_FRACTIONS, METALKNOB_INNERBACKGROUND_COLORS);
                G2.setPaint(METALKNOB_INNERBACKGROUND_GRADIENT);
                G2.fill(METALKNOB_INNERBACKGROUND);
                break;
        }
              
        IMAGE_HEIGHT = image.getHeight();
                        
        final java.awt.geom.Ellipse2D MAX_POST_FRAME_RIGHT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.8317757248878479, IMAGE_HEIGHT * 0.514018714427948, IMAGE_WIDTH * 0.03738313913345337, IMAGE_HEIGHT * 0.03738313913345337);
        final java.awt.geom.Point2D MAX_POST_FRAME_RIGHT_START = new java.awt.geom.Point2D.Double(0, MAX_POST_FRAME_RIGHT.getBounds2D().getMinY() );
        final java.awt.geom.Point2D MAX_POST_FRAME_RIGHT_STOP = new java.awt.geom.Point2D.Double(0, MAX_POST_FRAME_RIGHT.getBounds2D().getMaxY() );
        final float[] E_MAX_POST_FRAME_RIGHT_FRACTIONS =
        {
            0.0f,
            0.46f,
            1.0f
        };
        final java.awt.Color[] MAX_POST_FRAME_RIGHT_COLORS =
        {
            new java.awt.Color(180, 180, 180, 255),
            new java.awt.Color(63, 63, 63, 255),
            new java.awt.Color(40, 40, 40, 255)
        };
        final java.awt.LinearGradientPaint MAX_POST_FRAME_RIGHT_GRADIENT = new java.awt.LinearGradientPaint(MAX_POST_FRAME_RIGHT_START, MAX_POST_FRAME_RIGHT_STOP, E_MAX_POST_FRAME_RIGHT_FRACTIONS, MAX_POST_FRAME_RIGHT_COLORS);
        G2.setPaint(MAX_POST_FRAME_RIGHT_GRADIENT);
        G2.fill(MAX_POST_FRAME_RIGHT);

        final java.awt.geom.Ellipse2D MAX_POST_MAIN_RIGHT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.836448609828949, IMAGE_HEIGHT * 0.5186915993690491, IMAGE_WIDTH * 0.02803736925125122, IMAGE_HEIGHT * 0.02803736925125122);
        final java.awt.geom.Point2D MAX_POST_MAIN_RIGHT_START = new java.awt.geom.Point2D.Double(0, MAX_POST_MAIN_RIGHT.getBounds2D().getMinY() );
        final java.awt.geom.Point2D MAX_POST_MAIN_RIGHT_STOP = new java.awt.geom.Point2D.Double(0, MAX_POST_MAIN_RIGHT.getBounds2D().getMaxY() );
        final float[] MAX_POST_MAIN_RIGHT_FRACTIONS =
        {
            0.0f,
            0.5f,
            1.0f
        };

        final java.awt.Color[] MAX_POST_MAIN_RIGHT_COLORS;
        switch(getModel().getKnobStyle())
        {
            case BLACK:
                MAX_POST_MAIN_RIGHT_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(0xBFBFBF),
                    new java.awt.Color(0x2B2A2F),
                    new java.awt.Color(0x7D7E80)
                };
                break;

            case BRASS:
                MAX_POST_MAIN_RIGHT_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(0xDFD0AE),
                    new java.awt.Color(0x7A5E3E),
                    new java.awt.Color(0xCFBE9D)
                };
                break;

            case SILVER:

            default:
                MAX_POST_MAIN_RIGHT_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(0xD7D7D7),
                    new java.awt.Color(0x747474),
                    new java.awt.Color(0xD7D7D7)
                };
                break;
        }
        final java.awt.LinearGradientPaint MAX_POST_MAIN_RIGHT_GRADIENT = new java.awt.LinearGradientPaint(MAX_POST_MAIN_RIGHT_START, MAX_POST_MAIN_RIGHT_STOP, MAX_POST_MAIN_RIGHT_FRACTIONS, MAX_POST_MAIN_RIGHT_COLORS);
        G2.setPaint(MAX_POST_MAIN_RIGHT_GRADIENT);
        G2.fill(MAX_POST_MAIN_RIGHT);

        final java.awt.geom.Ellipse2D MAX_POST_INNERSHADOW_RIGHT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.836448609828949, IMAGE_HEIGHT * 0.5186915993690491, IMAGE_WIDTH * 0.02803736925125122, IMAGE_HEIGHT * 0.02803736925125122);
        final java.awt.geom.Point2D MAX_POST_INNERSHADOW_RIGHT_CENTER = new java.awt.geom.Point2D.Double( (0.8504672897196262 * IMAGE_WIDTH), (0.5280373831775701 * IMAGE_HEIGHT) );
        final float[] MAX_POST_INNERSHADOW_RIGHT_FRACTIONS =
        {
            0.0f,
            0.75f,
            0.76f,
            1.0f
        };
        final java.awt.Color[] MAX_POST_INNERSHADOW_RIGHT_COLORS =
        {
            new java.awt.Color(0, 0, 0, 0),
            new java.awt.Color(0, 0, 0, 0),
            new java.awt.Color(0, 0, 0, 1),
            new java.awt.Color(0, 0, 0, 51)
        };
        final java.awt.RadialGradientPaint MAX_POST_INNERSHADOW_RIGHT_GRADIENT = new java.awt.RadialGradientPaint(MAX_POST_INNERSHADOW_RIGHT_CENTER, (float)(0.014018691588785047 * IMAGE_WIDTH), MAX_POST_INNERSHADOW_RIGHT_FRACTIONS, MAX_POST_INNERSHADOW_RIGHT_COLORS);
        G2.setPaint(MAX_POST_INNERSHADOW_RIGHT_GRADIENT);
        G2.fill(MAX_POST_INNERSHADOW_RIGHT);

        final java.awt.geom.Ellipse2D MIN_POST_FRAME_LEFT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.13084112107753754, IMAGE_HEIGHT * 0.514018714427948, IMAGE_WIDTH * 0.03738318383693695, IMAGE_HEIGHT * 0.03738313913345337);
        final java.awt.geom.Point2D MIN_POST_FRAME_LEFT_START = new java.awt.geom.Point2D.Double(0, MIN_POST_FRAME_LEFT.getBounds2D().getMinY() );
        final java.awt.geom.Point2D MIN_POST_FRAME_LEFT_STOP = new java.awt.geom.Point2D.Double(0, MIN_POST_FRAME_LEFT.getBounds2D().getMaxY() );
        final float[] E_MIN_POST_FRAME_LEFT_FRACTIONS =
        {
            0.0f,
            0.46f,
            1.0f
        };
        final java.awt.Color[] MIN_POST_FRAME_LEFT_COLORS =
        {
            new java.awt.Color(180, 180, 180, 255),
            new java.awt.Color(63, 63, 63, 255),
            new java.awt.Color(40, 40, 40, 255)
        };
        final java.awt.LinearGradientPaint MIN_POST_FRAME_LEFT_GRADIENT = new java.awt.LinearGradientPaint(MIN_POST_FRAME_LEFT_START, MIN_POST_FRAME_LEFT_STOP, E_MIN_POST_FRAME_LEFT_FRACTIONS, MIN_POST_FRAME_LEFT_COLORS);
        G2.setPaint(MIN_POST_FRAME_LEFT_GRADIENT);
        G2.fill(MIN_POST_FRAME_LEFT);

        final java.awt.geom.Ellipse2D MIN_POST_MAIN_LEFT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.1355140209197998, IMAGE_HEIGHT * 0.5186915993690491, IMAGE_WIDTH * 0.028037384152412415, IMAGE_HEIGHT * 0.02803736925125122);
        final java.awt.geom.Point2D MIN_POST_MAIN_LEFT_START = new java.awt.geom.Point2D.Double(0, MIN_POST_MAIN_LEFT.getBounds2D().getMinY() );
        final java.awt.geom.Point2D MIN_POST_MAIN_LEFT_STOP = new java.awt.geom.Point2D.Double(0, MIN_POST_MAIN_LEFT.getBounds2D().getMaxY() );
        final float[] MIN_POST_MAIN_LEFT_FRACTIONS =
        {
            0.0f,
            0.5f,
            1.0f
        };

        final java.awt.Color[] MIN_POST_MAIN_LEFT_COLORS;
        switch(getModel().getKnobStyle())
        {
            case BLACK:
                MIN_POST_MAIN_LEFT_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(0xBFBFBF),
                    new java.awt.Color(0x2B2A2F),
                    new java.awt.Color(0x7D7E80)
                };
                break;

            case BRASS:
                MIN_POST_MAIN_LEFT_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(0xDFD0AE),
                    new java.awt.Color(0x7A5E3E),
                    new java.awt.Color(0xCFBE9D)
                };
                break;

            case SILVER:

            default:
                MIN_POST_MAIN_LEFT_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(0xD7D7D7),
                    new java.awt.Color(0x747474),
                    new java.awt.Color(0xD7D7D7)
                };
                break;
        }
        final java.awt.LinearGradientPaint MIN_POST_MAIN_LEFT_GRADIENT = new java.awt.LinearGradientPaint(MIN_POST_MAIN_LEFT_START, MIN_POST_MAIN_LEFT_STOP, MIN_POST_MAIN_LEFT_FRACTIONS, MIN_POST_MAIN_LEFT_COLORS);
        G2.setPaint(MIN_POST_MAIN_LEFT_GRADIENT);
        G2.fill(MIN_POST_MAIN_LEFT);

        final java.awt.geom.Ellipse2D MIN_POST_INNERSHADOW_LEFT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.1355140209197998, IMAGE_HEIGHT * 0.5186915993690491, IMAGE_WIDTH * 0.028037384152412415, IMAGE_HEIGHT * 0.02803736925125122);
        final java.awt.geom.Point2D MIN_POST_INNERSHADOW_LEFT_CENTER = new java.awt.geom.Point2D.Double( (0.14953271028037382 * IMAGE_WIDTH), (0.5280373831775701 * IMAGE_HEIGHT) );
        final float[] MIN_POST_INNERSHADOW_LEFT_FRACTIONS =
        {
            0.0f,
            0.75f,
            0.76f,
            1.0f
        };
        final java.awt.Color[] MIN_POST_INNERSHADOW_LEFT_COLORS =
        {
            new java.awt.Color(0, 0, 0, 0),
            new java.awt.Color(0, 0, 0, 0),
            new java.awt.Color(0, 0, 0, 1),
            new java.awt.Color(0, 0, 0, 51)
        };
        final java.awt.RadialGradientPaint MIN_POST_INNERSHADOW_LEFT_GRADIENT = new java.awt.RadialGradientPaint(MIN_POST_INNERSHADOW_LEFT_CENTER, (float)(0.014018691588785047 * IMAGE_WIDTH), MIN_POST_INNERSHADOW_LEFT_FRACTIONS, MIN_POST_INNERSHADOW_LEFT_COLORS);
        G2.setPaint(MIN_POST_INNERSHADOW_LEFT_GRADIENT);
        G2.fill(MIN_POST_INNERSHADOW_LEFT);

        G2.dispose();

        return image;
    }

    @Override
    protected java.awt.image.BufferedImage create_DISABLED_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, (int) (0.641860465116279 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        
        final java.awt.geom.GeneralPath GAUGE_BACKGROUND = new java.awt.geom.GeneralPath();
        GAUGE_BACKGROUND.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        GAUGE_BACKGROUND.moveTo(IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.7753623188405797);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.42028985507246375, IMAGE_WIDTH * 0.26976744186046514, IMAGE_HEIGHT * 0.13043478260869565, IMAGE_WIDTH * 0.49767441860465117, IMAGE_HEIGHT * 0.13043478260869565);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.7255813953488373, IMAGE_HEIGHT * 0.13043478260869565, IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.42028985507246375, IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.7753623188405797);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.9116279069767442, IMAGE_HEIGHT * 0.8188405797101449, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8695652173913043, IMAGE_WIDTH * 0.9069767441860465, IMAGE_HEIGHT * 0.8695652173913043);
        GAUGE_BACKGROUND.lineTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8695652173913043);
        GAUGE_BACKGROUND.curveTo(IMAGE_WIDTH * 0.08837209302325581, IMAGE_HEIGHT * 0.8695652173913043, IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.8115942028985508, IMAGE_WIDTH * 0.08372093023255814, IMAGE_HEIGHT * 0.7753623188405797);
        GAUGE_BACKGROUND.closePath();
        
        G2.setColor(new java.awt.Color(102, 102, 102, 178));
        G2.fill(GAUGE_BACKGROUND);
        
        G2.dispose();

        return IMAGE;
    }
    
    @Override
    public String toString()
    {
        return "Radial2Top";
    }
}
