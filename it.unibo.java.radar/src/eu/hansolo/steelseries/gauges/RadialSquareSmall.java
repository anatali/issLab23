package eu.hansolo.steelseries.gauges;

/**
 *
 * @author hansolo
 */
public final class RadialSquareSmall extends AbstractRadial
{    
    private final java.awt.Rectangle INNER_BOUNDS;            
    private int tickLabelPeriod; // Draw value at every nth tickmark
    private final java.awt.geom.Point2D CENTER;
    private final java.awt.geom.Point2D TRACK_OFFSET;    
    private java.awt.image.BufferedImage frameImage;
    private java.awt.image.BufferedImage backgroundImage;
    private java.awt.image.BufferedImage postsImage;
    private java.awt.image.BufferedImage trackImage;
    private java.awt.image.BufferedImage tickmarksImage;
    private java.awt.image.BufferedImage pointerImage;
    private java.awt.image.BufferedImage pointerShadowImage;
    private java.awt.image.BufferedImage foregroundImage;
    private java.awt.image.BufferedImage thresholdImage;
    private java.awt.image.BufferedImage minMeasuredImage;
    private java.awt.image.BufferedImage maxMeasuredImage;
    private java.awt.image.BufferedImage disabledImage;    
    private double angle;


    // ideal size  212 x 132
    
    public RadialSquareSmall()
    {
        super();          
        INNER_BOUNDS = new java.awt.Rectangle(getPreferredSize());        
        tickLabelPeriod = 20;
        CENTER = new java.awt.geom.Point2D.Double();        
        TRACK_OFFSET = new java.awt.geom.Point2D.Double();        
        angle = 0;        
        setLedPosition(0.65, 0.65);                
        setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE5);        
        init(getInnerBounds().width, (int) (getInnerBounds().width * 0.6226415094339622));
    }

    @Override
    public final AbstractGauge init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        if (frameImage != null)
        {
            frameImage.flush();
        }
        frameImage = FRAME_FACTORY.createLinearFrame(WIDTH, HEIGHT, getFrameDesign(), getCustomFrameDesign(), getFrameEffect());

        if (backgroundImage != null)
        {
            backgroundImage.flush();
        }
        backgroundImage = BACKGROUND_FACTORY.createLinearBackground(WIDTH, HEIGHT, getBackgroundColor(), getCustomBackground());

        if (postsImage != null)
        {
            postsImage.flush();
        }
        postsImage = create_POSTS_Image(WIDTH, eu.hansolo.steelseries.tools.PostPosition.SMALL_GAUGE_MIN_LEFT, eu.hansolo.steelseries.tools.PostPosition.SMALL_GAUGE_MAX_RIGHT);

        if (trackImage != null)
        {
            trackImage.flush();
        }
        TRACK_OFFSET.setLocation(0, 0);
        CENTER.setLocation(getGaugeBounds().getCenterX(), HEIGHT * 1.1287878788);                                        
        trackImage = create_TRACK_Image(WIDTH, getFreeAreaAngle() + Math.PI / 2, getTickmarkOffset(), getMinValue(), getMaxValue(), getAngleStep(), getTrackStart(), getTrackSection(), getTrackStop(), getTrackStartColor(), getTrackSectionColor(), getTrackStopColor(), 0.44f, CENTER, getTickmarkDirection(), TRACK_OFFSET);                    

        if (tickmarksImage != null)
        {
            tickmarksImage.flush();
        }
        //tickmarksImage = create_TICKMARKS_Image(WIDTH, FREE_AREA_ANGLE, TICKMARK_OFFSET, getMinValue(), getMaxValue(), getAngleStep(), tickLabelPeriod, getScaleDividerPower(), isTickmarksVisible(), isTicklabelsVisible(), getTickmarkSections(), 0.44f, -0.04f, new java.awt.geom.Point2D.Double(WIDTH / 2.0, WIDTH * 0.73));

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
                                                new java.awt.geom.Point2D.Double(WIDTH / 2.0, WIDTH * 0.73),
                                                new java.awt.geom.Point2D.Double(0, 0),
                                                eu.hansolo.steelseries.tools.Orientation.NORTH,
                                                getModel().isNiceScale(),
                                                null);
        
        if (backgroundImage != null)
        {
            backgroundImage.flush();
        }
        backgroundImage = BACKGROUND_FACTORY.createLinearBackground(WIDTH, HEIGHT, getBackgroundColor(), getCustomBackground());

        if (pointerImage != null)
        {
            pointerImage.flush();
        }
        pointerImage = create_POINTER_Image(WIDTH, (int) (0.6226415094339622 * WIDTH), getPointerType());

        if (pointerShadowImage != null)
        {
            pointerShadowImage.flush();
        }
        pointerShadowImage = create_POINTER_SHADOW_Image(WIDTH, (int) (0.6226415094339622 * WIDTH), getPointerType());

        if (foregroundImage != null)
        {
            foregroundImage.flush();
        }
        foregroundImage = FOREGROUND_FACTORY.createLinearForeground(WIDTH, HEIGHT);

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
        disabledImage = DISABLED_FACTORY.createLinearDisabled(WIDTH, HEIGHT);                    
        
        create_AREA();
        createSections();
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

        // Translate the coordinate system related to insets
        G2.translate(INNER_BOUNDS.x, INNER_BOUNDS.y);

        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().height * 1.1287878788);        
        final int IMAGE_SHIFT_Y = (int) -(getGaugeBounds().width * 0.0606060606);

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        // Draw the frame
        if (isFrameVisible())
        {
            G2.drawImage(frameImage, 0, 0, null);
        }

        // Draw the background
        if (isBackgroundVisible())
        {
            G2.drawImage(backgroundImage, 0, 0, null);
        }
        
        // Draw the sections
        if (isSectionsVisible())
        {
            
            G2.translate(0, getGaugeBounds().getCenterY());
            
            for (eu.hansolo.steelseries.tools.Section section : getModel().getSections())
            {
                G2.setColor(section.getColor());
                G2.fill(section.getSectionArea());
            }
            G2.setTransform(OLD_TRANSFORM);
        }

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
            G2.drawString(getTitle(), (float)((getGaugeBounds().width - TITLE_BOUNDARY.getWidth()) / 2.0), 0.6f * getGaugeBounds().height + TITLE_LAYOUT.getAscent() - TITLE_LAYOUT.getDescent());
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
            G2.drawString(getUnitString(), (float)((getGaugeBounds().width - UNIT_BOUNDARY.getWidth()) / 2.0), 0.68f * getGaugeBounds().height + UNIT_LAYOUT.getAscent() - UNIT_LAYOUT.getDescent());
        }

        final java.awt.geom.AffineTransform FORMER_TRANSFORM = G2.getTransform();
        
        // Draw the tickmarks
        G2.drawImage(tickmarksImage, 0, IMAGE_SHIFT_Y, null);

        // Draw the track
        if (isTrackVisible())
        {
            G2.drawImage(trackImage, 0, IMAGE_SHIFT_Y, null);
        }

        // Draw threshold indicator
        if (isThresholdVisible())
        {
            G2.rotate(getGaugeType().ROTATION_OFFSET + (getThreshold() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(thresholdImage, 0, IMAGE_SHIFT_Y, null);
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw min measured value indicator
        if (isMinMeasuredValueVisible())
        {
            G2.rotate(getGaugeType().ROTATION_OFFSET + (getMinMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(minMeasuredImage, 0, IMAGE_SHIFT_Y, null);
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw max measured value indicator
        if (isMaxMeasuredValueVisible())
        {
            G2.rotate(getGaugeType().ROTATION_OFFSET + (getMaxMeasuredValue() - getMinValue()) * getAngleStep(), CENTER.getX(), CENTER.getY());
            G2.drawImage(maxMeasuredImage, 0, IMAGE_SHIFT_Y, null);
            G2.setTransform(FORMER_TRANSFORM);
        }

        // Draw led
        if (isLedVisible())
        {
            G2.drawImage(getCurrentLedImage(), (int) (getGaugeBounds().width * getLedPosition().getX()), (int) (getGaugeBounds().height * getLedPosition().getY()), null);
        }
                        
        // Draw the pointer
        angle = getGaugeType().ROTATION_OFFSET + (getValue() - getMinValue()) * getAngleStep();
        //G2.rotate(ANGLE + (Math.cos(Math.toRadians(ANGLE - ROTATION_OFFSET - 91.5))), CENTER.getX(), backgroundImage.getHeight() * 0.7336448598);
        G2.rotate(angle, CENTER.getX(), CENTER.getY() + 2);
        G2.drawImage(pointerShadowImage, 0, 0, null);
        G2.setTransform(FORMER_TRANSFORM);
        G2.rotate(angle, CENTER.getX(), CENTER.getY());
        G2.drawImage(pointerImage, 0, 0, null);
        G2.setTransform(FORMER_TRANSFORM);

        // Draw posts
        G2.drawImage(postsImage, 0, IMAGE_SHIFT_Y, null);

        // Draw the foreground
        if (isForegroundVisible())
        {
            G2.drawImage(foregroundImage, 0, 0, null);
        }

        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate coordinate system back to original
        G2.translate(-INNER_BOUNDS.x, -INNER_BOUNDS.y);

        G2.dispose();
    }
    
    @Override
    public eu.hansolo.steelseries.tools.FrameType getFrameType()
    {
        return eu.hansolo.steelseries.tools.FrameType.SQUARE;
    }

    public int getTickLabelPeriod()
    {
        return this.tickLabelPeriod;
    }

    public void setTickLabelPeriod(final int TICK_LABEL_PERIOD)
    {
        this.tickLabelPeriod = TICK_LABEL_PERIOD;        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
            
    @Override
    public java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(getInnerBounds().getCenterX() + INNER_BOUNDS.x, getInnerBounds().getCenterX() / 2.0 + INNER_BOUNDS.y);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return getInnerBounds();
    }

    private void create_AREA()
    {
        
    }

    private void createSections()
    {
        if (!getSections().isEmpty() && backgroundImage != null)
        {
            final double ORIGIN_CORRECTION = 135;

            final double ANGLE_STEP = 90.0 / (getMaxValue() - getMinValue());
            final double OUTER_RADIUS = backgroundImage.getWidth() * 0.40f;
            final double INNER_RADIUS = backgroundImage.getWidth() * 0.37f;
            final double FREE_AREA_OUTER_RADIUS = backgroundImage.getWidth() / 2.0 - OUTER_RADIUS;
            final double FREE_AREA_INNER_RADIUS = backgroundImage.getWidth() / 2.0 - INNER_RADIUS;
            final java.awt.geom.Ellipse2D INNER = new java.awt.geom.Ellipse2D.Double(backgroundImage.getMinX() + FREE_AREA_INNER_RADIUS, backgroundImage.getMinY() + FREE_AREA_INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);

            for (eu.hansolo.steelseries.tools.Section section : getSections())
            {
                final double ANGLE_START = ORIGIN_CORRECTION - (section.getStart() * ANGLE_STEP);
                final double ANGLE_EXTEND = -(section.getStop() - section.getStart()) * ANGLE_STEP;

                final java.awt.geom.Arc2D OUTER_ARC = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
                OUTER_ARC.setFrame(backgroundImage.getMinX() + FREE_AREA_OUTER_RADIUS, backgroundImage.getMinY() + FREE_AREA_OUTER_RADIUS, 2 * OUTER_RADIUS, 2 * OUTER_RADIUS);
                OUTER_ARC.setAngleStart(ANGLE_START);
                OUTER_ARC.setAngleExtent(ANGLE_EXTEND);
                final java.awt.geom.Area SECTION = new java.awt.geom.Area(OUTER_ARC);

                SECTION.subtract(new java.awt.geom.Area(INNER));

                section.setSectionArea(SECTION);
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
        if (WIDTH <= 0 || HEIGHT <= 0)
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

    private java.awt.image.BufferedImage create_POINTER_Image(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
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
        
        final java.awt.geom.Point2D POINTER_START;
        final java.awt.geom.Point2D POINTER_STOP;
        final float[] POINTER_FRACTIONS;
        final java.awt.Color[] POINTER_COLORS;
        final java.awt.Paint POINTER_GRADIENT;

        switch(POINTER_TYPE)
        {
            case TYPE1:
                final java.awt.geom.GeneralPath POINTER1 = new java.awt.geom.GeneralPath();
                POINTER1.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER1.moveTo(IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.6439393939393939);
                POINTER1.curveTo(IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.49242424242424243, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.42424242424242425, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.42424242424242425);
                POINTER1.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.42424242424242425, IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.49242424242424243, IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.6439393939393939);
                POINTER1.curveTo(IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.6439393939393939, IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.6439393939393939, IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.6439393939393939);
                POINTER1.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER1.getBounds2D().getMinY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER1.getBounds2D().getMaxY() );
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
                        getCustomPointerColorObject().DARK,
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().DARK
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER1);

                final java.awt.Color STROKE_COLOR_POINTER = getPointerColor().LIGHT;
                G2.setColor(STROKE_COLOR_POINTER);
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER1);
                break;

            case TYPE2:                
            
            default:
                final java.awt.geom.Rectangle2D POINTER2 = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.4166666666666667, IMAGE_WIDTH * 0.009433962264150943, IMAGE_HEIGHT * 0.24242424242424243);
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER2.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER2.getBounds2D().getMinY() );
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.25f,
                    0.251f,
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
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().LIGHT
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER2);
                break;
        }

        G2.dispose();

        return IMAGE;
    }
   
    private java.awt.image.BufferedImage create_POINTER_SHADOW_Image(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }
        final java.awt.Color SHADOW_COLOR = new java.awt.Color(0.0f, 0.0f, 0.0f, 0.65f);

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
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
        

        switch(POINTER_TYPE)
        {
            case TYPE1:
                final java.awt.geom.GeneralPath POINTER1 = new java.awt.geom.GeneralPath();
                POINTER1.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER1.moveTo(IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.6439393939393939);
                POINTER1.curveTo(IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.49242424242424243, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.42424242424242425, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.42424242424242425);
                POINTER1.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.42424242424242425, IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.49242424242424243, IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.6439393939393939);
                POINTER1.curveTo(IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.6439393939393939, IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.6439393939393939, IMAGE_WIDTH * 0.5047169811320755, IMAGE_HEIGHT * 0.6439393939393939);
                POINTER1.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER1);
                break;

            case TYPE2:
                final java.awt.geom.Rectangle2D POINTER = new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.49528301886792453, IMAGE_HEIGHT * 0.4166666666666667, IMAGE_WIDTH * 0.009433962264150943, IMAGE_HEIGHT * 0.24242424242424243);
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
        }

        G2.dispose();

        return IMAGE;
    }

    @Override
    public void calcInnerBounds()
    {
        final java.awt.Insets INSETS = getInsets();
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, getWidth() - INSETS.left - INSETS.right, getHeight() - INSETS.top - INSETS.bottom);
    }

    @Override
    public final java.awt.Rectangle getInnerBounds()
    {
        return INNER_BOUNDS;
    }

    @Override
    public java.awt.Dimension getMinimumSize()
    {        
        return new java.awt.Dimension(212, 132);
    }

    @Override
    public void setPreferredSize(final java.awt.Dimension DIM)
    {        
        super.setPreferredSize(new java.awt.Dimension(DIM.width, (int) (0.6226415094339622 * DIM.width)));
        calcInnerBounds();
        init(DIM.width, (int) (0.6226415094339622 * DIM.width));        
        setInitialized(true);
        revalidate();
        repaint();
    }

    @Override
    public void setSize(final int WIDTH, final int HEIGHT)
    {
        super.setSize(WIDTH, (int) (0.6226415094339622 * WIDTH));
        calcInnerBounds();
        init(WIDTH, (int) (0.6226415094339622 * WIDTH));       
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final java.awt.Dimension DIM)
    {
        super.setSize(new java.awt.Dimension(DIM.width, (int) (0.6226415094339622 * DIM.width)));
        calcInnerBounds();
        init(DIM.width, (int) (0.6226415094339622 * DIM.width));        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final java.awt.Rectangle BOUNDS)
    {
        super.setBounds(new java.awt.Rectangle(BOUNDS.x, BOUNDS.y, BOUNDS.width, (int) (0.6226415094339622 * BOUNDS.width)));
        calcInnerBounds();
        init(BOUNDS.width, (int) (0.6226415094339622 * BOUNDS.width));        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        super.setBounds(X, Y, WIDTH, (int) (0.6226415094339622 * WIDTH));
        calcInnerBounds();
        init(WIDTH, (int) (0.6226415094339622 * WIDTH));        
        setInitialized(true);
        revalidate();
        repaint();
    }
    
    @Override
    public void componentResized(java.awt.event.ComponentEvent event)
    {        
        //setPreferredSize(new java.awt.Dimension(getWidth(), (int) (0.6226415094339622 * getWidth())));        
        setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
        calcInnerBounds();
        init(INNER_BOUNDS.width, (int)(INNER_BOUNDS.width * 0.6226415094339622));
        revalidate();
        repaint();
    }

    @Override
    public String toString()
    {
        return "RadialSquareVerticalSmall";
    }
}