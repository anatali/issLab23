package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public final class Radial1Square extends AbstractRadial
{        
    private static final double TICKMARK_ROTATION_OFFSET = 0.5 * Math.PI;
    private final double ROTATION_OFFSET; // Offset for the pointer        
    // One image to reduce memory consumption
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage pointerImage;
    private java.awt.image.BufferedImage pointerShadowImage;   
    private java.awt.image.BufferedImage thresholdImage;
    private java.awt.image.BufferedImage minMeasuredImage;
    private java.awt.image.BufferedImage maxMeasuredImage;
    private java.awt.image.BufferedImage disabledImage;
    private final java.awt.geom.Point2D ROTATION_CENTER;
    private final java.awt.geom.Point2D TRACK_OFFSET;
    private final java.awt.geom.Point2D TICKMARKS_OFFSET;
    private final java.awt.geom.Point2D THRESHOLD_OFFSET;
    private final java.awt.geom.Point2D MEASURED_OFFSET;
    private double thresholdRotationOffset;
    private double measuredRotationOffset;        
    private float titleOffsetYFactor;
    private float unitOffsetYFactor;
    private double angle;

    
    public Radial1Square()
    {
        super();
        getModel().setGaugeType(eu.hansolo.steelseries.tools.GaugeType.TYPE1);        
        ROTATION_OFFSET = (1.5 * Math.PI) + (getModel().getFreeAreaAngle() / 2.0);                
        ROTATION_CENTER = new java.awt.geom.Point2D.Double(0, 0);
        TRACK_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        TICKMARKS_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        THRESHOLD_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        MEASURED_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        measuredRotationOffset = 0;
        thresholdRotationOffset = 0;                
        titleOffsetYFactor = 0.6f;
        unitOffsetYFactor = 0.67f;
        angle = 0;           
        setLedPosition(0.45, 0.45);        
        setOrientation(eu.hansolo.steelseries.tools.Orientation.NORTH_WEST);
        init(getInnerBounds().width, getInnerBounds().height);
    }
    
    public Radial1Square(final eu.hansolo.steelseries.tools.Model MODEL)
    {
        super();
        setModel(MODEL);
        ROTATION_OFFSET = (1.5 * Math.PI) + (getModel().getFreeAreaAngle() / 2.0);        
        ROTATION_CENTER = new java.awt.geom.Point2D.Double(0, 0);
        TRACK_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        TICKMARKS_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        THRESHOLD_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        MEASURED_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
        measuredRotationOffset = 0;
        thresholdRotationOffset = 0;                
        titleOffsetYFactor = 0.6f;
        unitOffsetYFactor = 0.67f;
        angle = 0;                   
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
            create_BACKGROUND_Image(WIDTH, bImage);
        }        

        final double TRACK_ORIENTATION_OFFSET;
        switch (getOrientation())
        {
            case NORTH_EAST:
                TRACK_ORIENTATION_OFFSET = Math.PI / 2;
                TRACK_OFFSET.setLocation(-(WIDTH / 1.55), 0);
                break;
            case SOUTH_EAST:
                TRACK_ORIENTATION_OFFSET = Math.PI;
                TRACK_OFFSET.setLocation(-(WIDTH / 1.55), -(WIDTH / 1.55));
                break;
            case SOUTH_WEST:
                TRACK_ORIENTATION_OFFSET = 1.5 * Math.PI;
                TRACK_OFFSET.setLocation(0, -(WIDTH / 1.55));
                break;
            case NORTH_WEST:

            default:
                TRACK_ORIENTATION_OFFSET = 0;
                TRACK_OFFSET.setLocation(0, 0);
                break;
        }
                
        if (isTrackVisible())
        {
            create_TRACK_Image(WIDTH, getModel().getFreeAreaAngle(), TICKMARK_ROTATION_OFFSET + TRACK_ORIENTATION_OFFSET, getMinValue(), getMaxValue(), getAngleStep(), getTrackStart(), getTrackSection(), getTrackStop(), getTrackStartColor(), getTrackSectionColor(), getTrackStopColor(), 0.68f, new java.awt.geom.Point2D.Double(WIDTH * 0.8271028037, WIDTH * 0.8271028037), getTickmarkDirection(), TRACK_OFFSET, bImage);            
        }        
                         
        createAreas(bImage);        
        
        createSections(bImage);
        
        //final double TICKMARKS_ORIENTATION_OFFSET;
        switch (getOrientation())
        {
            case NORTH_EAST:
                //TICKMARKS_ORIENTATION_OFFSET = Math.PI / 2;
                TICKMARKS_OFFSET.setLocation(-(WIDTH / 1.55), 0);
                //tickLabelRotationOffset = 0;
                break;
            case SOUTH_EAST:
                //TICKMARKS_ORIENTATION_OFFSET = Math.PI;
                TICKMARKS_OFFSET.setLocation(-(WIDTH / 1.55), -(WIDTH / 1.55));
                //tickLabelRotationOffset = Math.PI;
                break;
            case SOUTH_WEST:
                //TICKMARKS_ORIENTATION_OFFSET = 1.5 * Math.PI;
                TICKMARKS_OFFSET.setLocation(0, -(WIDTH / 1.55));
                //tickLabelRotationOffset = Math.PI;
                break;
            case NORTH_WEST:

            default:
                //TICKMARKS_ORIENTATION_OFFSET = 0;
                TICKMARKS_OFFSET.setLocation(0, 0);
                //tickLabelRotationOffset = 0;
                break;
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
                                                0.68f,
                                                0.09f,
                                                new java.awt.geom.Point2D.Double(WIDTH * 0.8271028037, WIDTH * 0.8271028037),
                                                new java.awt.geom.Point2D.Double(0, 0),
                                                eu.hansolo.steelseries.tools.Orientation.NORTH_WEST,
                                                getModel().isNiceScale(),
                                                bImage);
        
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
        
        create_POSTS_Image(WIDTH, fImage);
        
        if (isForegroundVisible())
        {
            create_FOREGROUND_Image(WIDTH, fImage);
        }
        
        final double THRESHOLD_ORIENTATION_OFFSET;
        switch (getOrientation())
        {
            case NORTH_EAST:                            
                THRESHOLD_ORIENTATION_OFFSET = Math.PI / 2;
                thresholdRotationOffset = ROTATION_OFFSET + (getMaxValue() - getThreshold() - getMinValue()) * getAngleStep();
                THRESHOLD_OFFSET.setLocation(bImage.getWidth() * 0.775, bImage.getHeight() * 0.81);
                break;
            case SOUTH_EAST:
                THRESHOLD_ORIENTATION_OFFSET = Math.PI / 2;
                THRESHOLD_OFFSET.setLocation((bImage.getWidth() * 0.79), (bImage.getHeight() * 0.16));
                thresholdRotationOffset = Math.PI / 2 + ROTATION_OFFSET + (getMaxValue() - getThreshold() - getMinValue()) * getAngleStep();
                break;
            case SOUTH_WEST:                            
                THRESHOLD_ORIENTATION_OFFSET = -Math.PI / 2;
                THRESHOLD_OFFSET.setLocation((bImage.getWidth() * 0.19), (bImage.getHeight() * 0.16));
                thresholdRotationOffset = ROTATION_OFFSET + (getThreshold() - getMinValue()) * getAngleStep();
                break;
            case NORTH_WEST:

            default:
                THRESHOLD_ORIENTATION_OFFSET = 0;
                THRESHOLD_OFFSET.setLocation((bImage.getWidth() * 0.805), (bImage.getHeight() * 0.19));
                thresholdRotationOffset = ROTATION_OFFSET + (getThreshold() - getMinValue()) * getAngleStep();
                break;
        }

        if (thresholdImage != null)
        {
            thresholdImage.flush();
        }
        thresholdImage = create_THRESHOLD_Image(WIDTH, THRESHOLD_ORIENTATION_OFFSET);

        final double MIN_MEASURED_ORIENTATION_OFFSET;
        switch (getOrientation())
        {
            case NORTH_EAST:
                MIN_MEASURED_ORIENTATION_OFFSET = Math.PI / 2;
                measuredRotationOffset = ROTATION_OFFSET;
                MEASURED_OFFSET.setLocation(bImage.getWidth() * 0.87, bImage.getHeight() * 0.815);
                break;
            case SOUTH_EAST:
                MIN_MEASURED_ORIENTATION_OFFSET = Math.PI / 2;
                MEASURED_OFFSET.setLocation((bImage.getWidth() * 0.87), (bImage.getHeight() * 0.15));
                measuredRotationOffset = Math.PI / 2 + ROTATION_OFFSET;
                break;
            case SOUTH_WEST:
                MIN_MEASURED_ORIENTATION_OFFSET = -Math.PI / 2;
                MEASURED_OFFSET.setLocation((bImage.getWidth() * 0.10), (bImage.getHeight() * 0.16));
                measuredRotationOffset = ROTATION_OFFSET;
                break;
            case NORTH_WEST:

            default:
                MIN_MEASURED_ORIENTATION_OFFSET = 0;
                MEASURED_OFFSET.setLocation((bImage.getWidth() * 0.811), (bImage.getHeight() * 0.11));
                measuredRotationOffset = ROTATION_OFFSET;
                break;
        }
        if (minMeasuredImage != null)
        {
            minMeasuredImage.flush();
        }
        minMeasuredImage = create_MEASURED_VALUE_Image(WIDTH, new java.awt.Color(0, 23, 252, 255), MIN_MEASURED_ORIENTATION_OFFSET);

        final double MAX_MEASURED_ORIENTATION_OFFSET;
        switch (getOrientation())
        {
            case NORTH_EAST:
                MAX_MEASURED_ORIENTATION_OFFSET = Math.PI / 2;
                measuredRotationOffset = ROTATION_OFFSET;
                MEASURED_OFFSET.setLocation(bImage.getWidth() * 0.87, bImage.getHeight() * 0.815);
                break;
            case SOUTH_EAST:
                MAX_MEASURED_ORIENTATION_OFFSET = Math.PI / 2;
                MEASURED_OFFSET.setLocation((bImage.getWidth() * 0.87), (bImage.getHeight() * 0.15));
                measuredRotationOffset = Math.PI / 2 + ROTATION_OFFSET;
                break;
            case SOUTH_WEST:
                MAX_MEASURED_ORIENTATION_OFFSET = -Math.PI / 2;
                MEASURED_OFFSET.setLocation((bImage.getWidth() * 0.10), (bImage.getHeight() * 0.16));
                measuredRotationOffset = ROTATION_OFFSET;
                break;
            case NORTH_WEST:

            default:
                MAX_MEASURED_ORIENTATION_OFFSET = 0;
                MEASURED_OFFSET.setLocation((bImage.getWidth() * 0.811), (bImage.getHeight() * 0.11));
                measuredRotationOffset = ROTATION_OFFSET;
                break;
        }
        if (maxMeasuredImage != null)
        {
            maxMeasuredImage.flush();
        }
        maxMeasuredImage = create_MEASURED_VALUE_Image(WIDTH, new java.awt.Color(252, 29, 0, 255), MAX_MEASURED_ORIENTATION_OFFSET);

        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH);                    

        // Adjust the rotation center of the pointer
        switch (getOrientation())
        {            
            case NORTH_EAST:
                ROTATION_CENTER.setLocation(WIDTH - WIDTH * 0.8271028037, WIDTH * 0.8271028037);
                titleOffsetYFactor = 0.6f;
                unitOffsetYFactor = 0.67f;
                break;
            case SOUTH_EAST:
                ROTATION_CENTER.setLocation(WIDTH - WIDTH * 0.8271028037, WIDTH - WIDTH * 0.8271028037);
                titleOffsetYFactor = 0.3f;
                unitOffsetYFactor = 0.37f;
                break;
            case SOUTH_WEST:
                ROTATION_CENTER.setLocation(WIDTH * 0.8271028037, WIDTH - WIDTH * 0.8271028037);
                titleOffsetYFactor = 0.3f;
                unitOffsetYFactor = 0.37f;
                break;
            case NORTH_WEST:

            default:
                ROTATION_CENTER.setLocation(WIDTH * 0.8271028037, WIDTH * 0.8271028037);
                titleOffsetYFactor = 0.6f;
                unitOffsetYFactor = 0.67f;
                break;
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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        // Draw combined background image
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

            // Use custom font if selected
            if (isTitleAndUnitFontEnabled())
            {
                G2.setFont(new java.awt.Font(getTitleAndUnitFont().getFamily(), 0, (int) (0.04672897196261682 * getGaugeBounds().width)));
            }
            else
            {
                G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.04672897196261682 * getGaugeBounds().width)));
            }
            final java.awt.font.TextLayout TITLE_LAYOUT = new java.awt.font.TextLayout(getTitle(), G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D TITLE_BOUNDARY = TITLE_LAYOUT.getBounds();
            G2.drawString(getTitle(), (float)((getGaugeBounds().width - TITLE_BOUNDARY.getWidth()) / 2), titleOffsetYFactor * getGaugeBounds().height + TITLE_LAYOUT.getAscent() - TITLE_LAYOUT.getDescent());
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

            // Use custom font if selected
            if (isTitleAndUnitFontEnabled())
            {
                G2.setFont(new java.awt.Font(getTitleAndUnitFont().getFamily(), 0, (int) (0.04672897196261682 * getGaugeBounds().width)));
            }
            else
            {
                G2.setFont(new java.awt.Font("Verdana", 0, (int) (0.04672897196261682 * bImage.getWidth())));
            }
            
            final java.awt.font.TextLayout UNIT_LAYOUT = new java.awt.font.TextLayout(getUnitString(), G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D UNIT_BOUNDARY = UNIT_LAYOUT.getBounds();
            G2.drawString(getUnitString(), (float)((getGaugeBounds().width - UNIT_BOUNDARY.getWidth()) / 2), unitOffsetYFactor * getGaugeBounds().width + UNIT_LAYOUT.getAscent() - UNIT_LAYOUT.getDescent());
        }

        // Draw threshold indicator
        if (isThresholdVisible())
        {            
            G2.rotate(thresholdRotationOffset, ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
            G2.drawImage(thresholdImage, (int) THRESHOLD_OFFSET.getX(), (int) THRESHOLD_OFFSET.getY(), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw min measured value indicator
        if (isMinMeasuredValueVisible())
        {
            switch(getOrientation())
            {
                case NORTH_EAST:
                    G2.rotate(measuredRotationOffset + (getMaxValue() - getMinMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
                case SOUTH_EAST:
                    G2.rotate(measuredRotationOffset + (getMaxValue() - getMinMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
                case SOUTH_WEST:
                    G2.rotate(measuredRotationOffset + (getMinMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
                case NORTH_WEST:
                    G2.rotate(measuredRotationOffset +  (getMinMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
            }
            G2.drawImage(minMeasuredImage, (int) MEASURED_OFFSET.getX(), (int) MEASURED_OFFSET.getY(), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw max measured value indicator
        if (isMaxMeasuredValueVisible())
        {
            switch(getOrientation())
            {
                case NORTH_EAST:
                    G2.rotate(measuredRotationOffset + (getMaxValue() - getMaxMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
                case SOUTH_EAST:
                    G2.rotate(measuredRotationOffset + (getMaxValue() - getMaxMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
                case SOUTH_WEST:
                    G2.rotate(measuredRotationOffset + (getMaxMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;
                case NORTH_WEST:
                    G2.rotate(measuredRotationOffset +  (getMaxMeasuredValue() - getMinValue()) * getAngleStep(), ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
                    break;                    
            }
            G2.drawImage(maxMeasuredImage, (int) MEASURED_OFFSET.getX(), (int) MEASURED_OFFSET.getY(), null);
            G2.setTransform(OLD_TRANSFORM);
        }

        // Draw LED if enabled
        if (isLedVisible())
        {
            G2.drawImage(getCurrentLedImage(), (int) (getGaugeBounds().width * getLedPosition().getX()), (int) (getGaugeBounds().width * getLedPosition().getY()), null);
        }

        // Draw the pointer
        switch (getOrientation())
        {
            case SOUTH_EAST:
                angle = (getValue() - getMinValue() - getMaxValue()) * (-getAngleStep());
                break;
            case SOUTH_WEST:
                angle = (getValue() - getMinValue() - getMaxValue()) * getAngleStep();
                break;
            case NORTH_EAST:
                angle = (getValue() - getMinValue()) * (-getAngleStep());
                break;
            case NORTH_WEST:
                
            default:
                 angle = (getValue() - getMinValue()) * getAngleStep();
                 break;   
        }

        //G2.rotate(ANGLE + (Math.cos(Math.toRadians(ANGLE - 91.0))), CENTER.getX(), CENTER.getY());
        G2.rotate(angle, ROTATION_CENTER.getX(), ROTATION_CENTER.getY() + 2);
        G2.drawImage(pointerShadowImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        G2.rotate(angle, ROTATION_CENTER.getX(), ROTATION_CENTER.getY());
        G2.drawImage(pointerImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);

        // Draw the foreground
        G2.drawImage(fImage, 0, 0, null);

        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }
    
    @Override
    public eu.hansolo.steelseries.tools.GaugeType getGaugeType()
    {
        return eu.hansolo.steelseries.tools.GaugeType.TYPE1;
    }
                
    // BECAUSE THERE ARE PROBLEMS WITH NEGATIVE VALUES I TEMPORARLY
    // DEACTIVATED THE ORIENTATION RELATED SETTINGS
    @Override
    public eu.hansolo.steelseries.tools.Orientation getOrientation()
    {
        return eu.hansolo.steelseries.tools.Orientation.NORTH_WEST;
    }
    
    /**
     * Sets the orientation of the gauge.
     * Possible values are:
     * 8 => NORTH_WEST => the upper left area of a circle (default)
     * 2 => NORTH_EAST => upper right area of a circle
     * 4 => SOUTH_EAST => lower right area of a circle
     * 6 => SOUTH_WEST => lower left area of a circle
     * the related int values are defined in javax.swing.SwingUtilities
     * @param ORIENTATION
     */
    @Override
    public void setOrientation(final eu.hansolo.steelseries.tools.Orientation ORIENTATION)
    {
        super.setOrientation(ORIENTATION);

        switch (getOrientation())
        {
            case NORTH_WEST:
                setTickmarkDirection(eu.hansolo.steelseries.tools.Direction.CLOCKWISE);
                break;
            case NORTH_EAST:
                setTickmarkDirection(eu.hansolo.steelseries.tools.Direction.COUNTER_CLOCKWISE);
                break;
            case SOUTH_EAST:
                setTickmarkDirection(eu.hansolo.steelseries.tools.Direction.COUNTER_CLOCKWISE);
                break;
            case SOUTH_WEST:
                setTickmarkDirection(eu.hansolo.steelseries.tools.Direction.CLOCKWISE);
                break;
        }

        init(getGaugeBounds().width, getGaugeBounds().height);
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
        final java.awt.geom.Point2D AREA_OFFSET = new java.awt.geom.Point2D.Double(0, 0);
                                        
        if (bImage != null && !getAreas().isEmpty())
        {                        
            double stopAngle = 0;
            double startAngle = 0;                        
            final double RADIUS = bImage.getWidth() * 0.3177570093f;            
            final double FREE_AREA = bImage.getWidth() / 2.0 - RADIUS;
            final java.awt.geom.Rectangle2D AREA_FRAME = new java.awt.geom.Rectangle2D.Double(bImage.getMinX() + FREE_AREA * 0.9 + AREA_OFFSET.getX(), bImage.getMinY() + FREE_AREA * 0.9 + AREA_OFFSET.getY(), 4 * RADIUS, 4 * RADIUS);                       
            
            for (eu.hansolo.steelseries.tools.Section tmpArea : getAreas())
            {                
                switch (getOrientation())
                {
                    case NORTH_EAST:
                        stopAngle = 90 - Math.toDegrees((getMaxValue() - tmpArea.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 90 - Math.toDegrees((getMaxValue() - tmpArea.getStart() - getMinValue()) * getAngleStep());
                        AREA_OFFSET.setLocation(-bImage.getWidth() * 0.45, bImage.getWidth() * 0.195);
                        break;

                    case SOUTH_EAST:
                        stopAngle = 0 - Math.toDegrees((getMaxValue() - tmpArea.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 0 - Math.toDegrees((getMaxValue() - tmpArea.getStart() - getMinValue()) * getAngleStep());
                        AREA_OFFSET.setLocation(-bImage.getWidth() * 0.45, -bImage.getWidth() * 0.45);
                        break;

                    case SOUTH_WEST:
                        stopAngle = 270 - Math.toDegrees((tmpArea.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 270 - Math.toDegrees((tmpArea.getStart() - getMinValue()) * getAngleStep());
                        AREA_OFFSET.setLocation(bImage.getWidth() * 0.195, -bImage.getWidth() * 0.45);
                        break;

                    case NORTH_WEST:

                    default:
                        stopAngle = 180 - Math.toDegrees((tmpArea.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 180 - Math.toDegrees((tmpArea.getStart() - getMinValue()) * getAngleStep());
                        AREA_OFFSET.setLocation(bImage.getWidth() * 0.195, bImage.getWidth() * 0.195);
                        break;
                }
                final java.awt.geom.Arc2D ARC = new java.awt.geom.Arc2D.Double(AREA_FRAME, 0 - (tmpArea.getStart() * getAngleStep()), -(tmpArea.getStop() - tmpArea.getStart()) * getAngleStep(), java.awt.geom.Arc2D.PIE);                
                ARC.setFrame(AREA_OFFSET.getX(), AREA_OFFSET.getY(), 4 * RADIUS, 4 * RADIUS);                
                ARC.setAngleStart(startAngle);
                ARC.setAngleExtent(stopAngle - startAngle);
                tmpArea.setFilledArea(ARC);                
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
            final double OUTER_RADIUS = bImage.getWidth() * 0.3411214953f;
            final double INNER_RADIUS = bImage.getWidth() * 0.3411214953f - bImage.getWidth() * 0.023364486f;
            final java.awt.geom.Ellipse2D INNER = new java.awt.geom.Ellipse2D.Double(0, 0, 1, 1);

            switch (getOrientation())
            {
                case NORTH_EAST:
                    INNER.setFrame(-bImage.getWidth() * 0.45, bImage.getWidth() * 0.195, 4 * INNER_RADIUS, 4 * INNER_RADIUS);
                    break;

                case SOUTH_EAST:
                    INNER.setFrame(-bImage.getWidth() * 0.45, -bImage.getWidth() * 0.45, 4 * INNER_RADIUS, 4 * INNER_RADIUS);
                    break;

                case SOUTH_WEST:
                    INNER.setFrame(bImage.getWidth() * 0.195, -bImage.getWidth() * 0.45, 4 * INNER_RADIUS, 4 * INNER_RADIUS);
                    break;

                case NORTH_WEST:

                default:
                    INNER.setFrame(bImage.getWidth() * 0.195, bImage.getWidth() * 0.195, 4 * INNER_RADIUS, 4 * INNER_RADIUS);
                    break;
            }

            double stopAngle;
            double startAngle;
            final java.awt.geom.Point2D SECTION_OFFSET = new java.awt.geom.Point2D.Double(0,0);

            for (eu.hansolo.steelseries.tools.Section section : getSections())
            {
                switch (getOrientation())
                {
                    case NORTH_EAST:
                        stopAngle = 90 - Math.toDegrees((getMaxValue() - section.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 90 - Math.toDegrees((getMaxValue() - section.getStart() - getMinValue()) * getAngleStep());
                        SECTION_OFFSET.setLocation(-bImage.getWidth() * 0.403271028, bImage.getWidth() * 0.148271028);
                        break;

                    case SOUTH_EAST:
                        stopAngle = 0 - Math.toDegrees((getMaxValue() - section.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 0 - Math.toDegrees((getMaxValue() - section.getStart() - getMinValue()) * getAngleStep());
                        SECTION_OFFSET.setLocation(-bImage.getWidth() * 0.403271028, -bImage.getWidth() * 0.403271028);
                        break;

                    case SOUTH_WEST:
                        stopAngle = 270 - Math.toDegrees((section.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 270 - Math.toDegrees((section.getStart() - getMinValue()) * getAngleStep());
                        SECTION_OFFSET.setLocation(bImage.getWidth() * 0.148271028, -bImage.getWidth() * 0.403271028);
                        break;

                    case NORTH_WEST:

                    default:
                        stopAngle = 180 - Math.toDegrees((section.getStop() - getMinValue()) * getAngleStep());
                        startAngle = 180 - Math.toDegrees((section.getStart() - getMinValue()) * getAngleStep());
                        SECTION_OFFSET.setLocation(bImage.getWidth() * 0.148271028, bImage.getWidth() * 0.148271028);
                        break;
                }

                final java.awt.geom.Arc2D OUTER_ARC = new java.awt.geom.Arc2D.Double(java.awt.geom.Arc2D.PIE);
                OUTER_ARC.setFrame(SECTION_OFFSET.getX(), SECTION_OFFSET.getY(), 4 * OUTER_RADIUS, 4 * OUTER_RADIUS);
                OUTER_ARC.setAngleStart(startAngle);
                OUTER_ARC.setAngleExtent(stopAngle - startAngle);
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

    private void transformGraphics(final int IMAGE_WIDTH, final int IMAGE_HEIGHT, final java.awt.Graphics2D G2)
    {
        switch(getOrientation())
        {
            // UpperRight
            case NORTH_EAST:
                G2.scale(-1, 1);
                G2.translate(-IMAGE_WIDTH, 0);
                break;
            // LowerRight
            case SOUTH_EAST:
                G2.scale(-1, -1);
                G2.translate(-IMAGE_WIDTH, -IMAGE_HEIGHT);
                break;
            // LowerLeft
            case SOUTH_WEST:
                G2.scale(1, -1);
                G2.translate(0, -IMAGE_HEIGHT);
                break;
            // UpperLeft
            case NORTH_WEST:

            default:
                G2.scale(1, 1);
        }
    }
    
    private java.awt.image.BufferedImage create_FRAME_Image(final int WIDTH, java.awt.image.BufferedImage image)
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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);

        if (getFrameDesign() != eu.hansolo.steelseries.tools.FrameDesign.NO_FRAME)
        {
            // Define shape that will be subtracted from the frame shapes and will be filled by the background later on
            final java.awt.geom.GeneralPath BACKGROUND = new java.awt.geom.GeneralPath();
            BACKGROUND.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            BACKGROUND.moveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
            BACKGROUND.curveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028);
            BACKGROUND.curveTo(IMAGE_WIDTH * 0.6401869158878505, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.46261682242990654, IMAGE_HEIGHT * 0.1588785046728972, IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.32242990654205606);
            BACKGROUND.curveTo(IMAGE_WIDTH * 0.17289719626168223, IMAGE_HEIGHT * 0.4439252336448598, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.6635514018691588, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897);
            BACKGROUND.curveTo(IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
            BACKGROUND.closePath();
            final java.awt.geom.Area SUBTRACT = new java.awt.geom.Area(BACKGROUND);

            final java.awt.geom.GeneralPath FRAME_OUTERFRAME = new java.awt.geom.GeneralPath();
            FRAME_OUTERFRAME.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            FRAME_OUTERFRAME.moveTo(IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 1.0);
            FRAME_OUTERFRAME.curveTo(IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 1.0, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.0, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 0.0);
            FRAME_OUTERFRAME.curveTo(IMAGE_WIDTH * 0.3644859813084112, IMAGE_HEIGHT * 0.0, IMAGE_WIDTH * 0.0, IMAGE_HEIGHT * 0.308411214953271, IMAGE_WIDTH * 0.0, IMAGE_HEIGHT * 1.0);
            FRAME_OUTERFRAME.curveTo(IMAGE_WIDTH * 0.0, IMAGE_HEIGHT * 1.0, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 1.0, IMAGE_WIDTH * 1.0, IMAGE_HEIGHT * 1.0);
            FRAME_OUTERFRAME.closePath();
            final java.awt.Color FILL_COLOR_FRAME_OUTERFRAME = new java.awt.Color(0x848484);
            G2.setColor(FILL_COLOR_FRAME_OUTERFRAME);
            final java.awt.geom.Area FRAME_OUTERFRAME_AREA = new java.awt.geom.Area(FRAME_OUTERFRAME);
            FRAME_OUTERFRAME_AREA.subtract(SUBTRACT);
            G2.fill(FRAME_OUTERFRAME_AREA);

            final java.awt.geom.GeneralPath FRAME_MAIN = new java.awt.geom.GeneralPath();
            FRAME_MAIN.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            FRAME_MAIN.moveTo(IMAGE_WIDTH * 0.9953271028037384, IMAGE_HEIGHT * 0.9953271028037384);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.9953271028037384, IMAGE_HEIGHT * 0.9953271028037384, IMAGE_WIDTH * 0.9953271028037384, IMAGE_HEIGHT * 0.004672897196261682, IMAGE_WIDTH * 0.9953271028037384, IMAGE_HEIGHT * 0.004672897196261682);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.3364485981308411, IMAGE_HEIGHT * 0.004672897196261682, IMAGE_WIDTH * 0.004672897196261682, IMAGE_HEIGHT * 0.35514018691588783, IMAGE_WIDTH * 0.004672897196261682, IMAGE_HEIGHT * 0.9953271028037384);
            FRAME_MAIN.curveTo(IMAGE_WIDTH * 0.004672897196261682, IMAGE_HEIGHT * 0.9953271028037384, IMAGE_WIDTH * 0.9953271028037384, IMAGE_HEIGHT * 0.9953271028037384, IMAGE_WIDTH * 0.9953271028037384, IMAGE_HEIGHT * 0.9953271028037384);
            FRAME_MAIN.closePath();

            final java.awt.geom.Point2D FRAME_MAIN_START;
            final java.awt.geom.Point2D FRAME_MAIN_STOP;
            final java.awt.geom.Point2D FRAME_MAIN_CENTER = new java.awt.geom.Point2D.Double(FRAME_MAIN.getBounds2D().getCenterX(), FRAME_MAIN.getBounds2D().getCenterY());

            switch (getOrientation())
            {
                case NORTH_WEST:
                    FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
                    FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
                    break;
                case NORTH_EAST:
                    FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
                    FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
                    break;
                case SOUTH_EAST:
                    FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
                    FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
                    break;
                case SOUTH_WEST:
                    FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
                    FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
                    break;
                default:
                    FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
                    FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
            }

            final float ANGLE_OFFSET = (float) Math.toDegrees(Math.atan((IMAGE_HEIGHT / 8.0f) / (IMAGE_WIDTH / 2.0f)));
            final float[] FRAME_MAIN_FRACTIONS;
            final java.awt.Color[] FRAME_MAIN_COLORS;
            final java.awt.Paint FRAME_MAIN_GRADIENT;

            switch(getFrameDesign())
            {
                case BLACK_METAL:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        90.0f - 2 * ANGLE_OFFSET,
                        90.0f,
                        90.0f + 3 * ANGLE_OFFSET,
                        180.0f,
                        270.0f - 3 * ANGLE_OFFSET,
                        270.0f,
                        270.0f + 2 * ANGLE_OFFSET,
                        1.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(153, 153, 153, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(153, 153, 153, 255),
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
                        90.0f - 2 * ANGLE_OFFSET,
                        90.0f,
                        90.0f + 3 * ANGLE_OFFSET,
                        180.0f,
                        270.0f - 3 * ANGLE_OFFSET,
                        270.0f,
                        270.0f + 2 * ANGLE_OFFSET,
                        1.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(238, 238, 238, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(238, 238, 238, 255),
                        new java.awt.Color(179, 179, 179, 255),
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
            
            final java.awt.geom.GeneralPath GAUGE_BACKGROUND_MAIN = new java.awt.geom.GeneralPath();
            GAUGE_BACKGROUND_MAIN.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            GAUGE_BACKGROUND_MAIN.moveTo(IMAGE_WIDTH * 0.9205607476635514, IMAGE_HEIGHT * 0.9205607476635514);
            GAUGE_BACKGROUND_MAIN.curveTo(IMAGE_WIDTH * 0.9205607476635514, IMAGE_HEIGHT * 0.9205607476635514, IMAGE_WIDTH * 0.9205607476635514, IMAGE_HEIGHT * 0.0794392523364486, IMAGE_WIDTH * 0.9205607476635514, IMAGE_HEIGHT * 0.0794392523364486);
            GAUGE_BACKGROUND_MAIN.curveTo(IMAGE_WIDTH * 0.6822429906542056, IMAGE_HEIGHT * 0.0794392523364486, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.13551401869158877, IMAGE_WIDTH * 0.3037383177570093, IMAGE_HEIGHT * 0.308411214953271);
            GAUGE_BACKGROUND_MAIN.curveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.4439252336448598, IMAGE_WIDTH * 0.0794392523364486, IMAGE_HEIGHT * 0.6822429906542056, IMAGE_WIDTH * 0.0794392523364486, IMAGE_HEIGHT * 0.9205607476635514);
            GAUGE_BACKGROUND_MAIN.curveTo(IMAGE_WIDTH * 0.0794392523364486, IMAGE_HEIGHT * 0.9205607476635514, IMAGE_WIDTH * 0.9205607476635514, IMAGE_HEIGHT * 0.9205607476635514, IMAGE_WIDTH * 0.9205607476635514, IMAGE_HEIGHT * 0.9205607476635514);
            GAUGE_BACKGROUND_MAIN.closePath();
            G2.setColor(java.awt.Color.WHITE);
            final java.awt.geom.Area GAUGE_BACKGROUND_MAIN_AREA = new java.awt.geom.Area(GAUGE_BACKGROUND_MAIN);
            GAUGE_BACKGROUND_MAIN_AREA.subtract(SUBTRACT);
            G2.fill(GAUGE_BACKGROUND_MAIN_AREA);
        }
        G2.dispose();

        return image;
    }

    private java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH, java.awt.image.BufferedImage image)
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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        boolean fadeInOut = false;
        
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);
        final java.awt.geom.AffineTransform NEW_TRANSFORM = G2.getTransform();

        final java.awt.geom.GeneralPath BACKGROUND = new java.awt.geom.GeneralPath();
        BACKGROUND.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        BACKGROUND.moveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.6401869158878505, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.46261682242990654, IMAGE_HEIGHT * 0.1588785046728972, IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.32242990654205606);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.17289719626168223, IMAGE_HEIGHT * 0.4439252336448598, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.6635514018691588, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
        BACKGROUND.closePath();
        final java.awt.geom.Point2D GAUGE_BACKGROUND_START;
        final java.awt.geom.Point2D GAUGE_BACKGROUND_STOP;
        switch(getOrientation())
        {
            case NORTH_WEST:
                GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY());
                GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY());
                break;
            case NORTH_EAST:
                GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY());
                GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY());
                break;
            case SOUTH_EAST:
                GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY());
                GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY());
                break;
            case SOUTH_WEST:
                GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY());
                GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY());
                break;                
            default:
                GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY());
                GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY());
        }

        final float[] GAUGE_BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.39f,            
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
                backgroundPaint = new java.awt.TexturePaint(UTIL.createBrushMetalTexture(null, BACKGROUND.getBounds().width, BACKGROUND.getBounds().height), BACKGROUND.getBounds());
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
                backgroundPaint = new eu.hansolo.steelseries.tools.ConicalGradientPaint(false, ROTATION_CENTER, -0.45f, STAINLESS_FRACTIONS, STAINLESS_COLORS);
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
        G2.fill(BACKGROUND);

        // Create inner shadow on background shape
        final java.awt.image.BufferedImage CLP;
        if (getCustomBackground() != null && getBackgroundColor() == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {
            CLP = eu.hansolo.steelseries.tools.Shadow.INSTANCE.createInnerShadow((java.awt.Shape) BACKGROUND, getCustomBackground(), 0, 0.65f, java.awt.Color.BLACK, 20, 315);
        }
        else
        {
            CLP = eu.hansolo.steelseries.tools.Shadow.INSTANCE.createInnerShadow((java.awt.Shape) BACKGROUND, backgroundPaint, 0, 0.65f, java.awt.Color.BLACK, 20, 315);
        }
        G2.drawImage(CLP, BACKGROUND.getBounds().x, BACKGROUND.getBounds().y, null);

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
            final java.awt.LinearGradientPaint SHADOW_OVERLAY_GRADIENT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Double(BACKGROUND.getBounds().getMinX(), 0), new java.awt.geom.Point2D.Double(BACKGROUND.getBounds().getMaxX(), 0), SHADOW_OVERLAY_FRACTIONS, SHADOW_OVERLAY_COLORS);
            G2.setPaint(SHADOW_OVERLAY_GRADIENT);
            G2.fill(BACKGROUND);
        }   
        
        // Draw the custom layer if selected
        if (isCustomLayerVisible())
        {
            G2.setTransform(OLD_TRANSFORM);
            G2.drawImage(UTIL.getScaledInstance(getCustomLayer(), IMAGE_WIDTH, IMAGE_HEIGHT, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC), 0, 0, null);
            G2.setTransform(NEW_TRANSFORM);
        }
        
        G2.dispose();

        return image;
    }

    @Override
    protected java.awt.image.BufferedImage create_POINTER_Image(final int WIDTH, eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);       

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);

        final java.awt.geom.GeneralPath POINTER;
        final java.awt.geom.Point2D POINTER_START;
        final java.awt.geom.Point2D POINTER_STOP;
        final float[] POINTER_FRACTIONS;
        final java.awt.Color[] POINTER_COLORS;
        final java.awt.Paint POINTER_GRADIENT;

        switch(POINTER_TYPE)
        {            
            case TYPE2:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.lineTo(IMAGE_WIDTH * 0.7897196261682243, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.6635514018691588, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.6682242990654206, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.7897196261682243, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.curveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.curveTo(IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8084112149532711, IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.curveTo(IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
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
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().LIGHT
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;
                
            case TYPE3:
                POINTER = new java.awt.geom.GeneralPath(new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.822429906542056, IMAGE_WIDTH * 0.6775700935, IMAGE_HEIGHT * 0.009345794392523364));                
                if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(getPointerColor().LIGHT);
                }
                else
                {
                    G2.setColor(getCustomPointerColorObject().LIGHT);
                }
                G2.fill(POINTER);
                break;
                
            case TYPE4:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.17757009345794392, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.lineTo(IMAGE_WIDTH * 0.897196261682243, IMAGE_HEIGHT * 0.8037383177570093);
                POINTER.lineTo(IMAGE_WIDTH * 0.897196261682243, IMAGE_HEIGHT * 0.8504672897196262);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.17757009345794392, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
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
                        getCustomPointerColorObject().DARK,
                        getCustomPointerColorObject().DARK,
                        getCustomPointerColorObject().LIGHT,
                        getCustomPointerColorObject().LIGHT
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;
                
            case TYPE5:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.7990654205607477);
                POINTER.lineTo(IMAGE_WIDTH * 0.16822429906542055, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8551401869158879);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    0.45f,
                    0.46f,
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
                POINTER.moveTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.lineTo(IMAGE_WIDTH * 0.6495327102803738, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.lineTo(IMAGE_WIDTH * 0.6448598130841121, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.6495327102803738, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.6495327102803738, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxX() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinX() );
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
                POINTER.moveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8504672897196262);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
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
                POINTER.moveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.curveTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8177570093457944, IMAGE_WIDTH * 0.16822429906542055, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
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
                POINTER.moveTo(IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.7663551401869159, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.lineTo(IMAGE_WIDTH * 0.7663551401869159, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8551401869158879);
                POINTER.lineTo(IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.8551401869158879);
                POINTER.curveTo(IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.8551401869158879, IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8504672897196262, IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8504672897196262);
                POINTER.curveTo(IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8504672897196262, IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8084112149532711, IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8037383177570093, IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8037383177570093);
                POINTER.curveTo(IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8037383177570093, IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.7990654205607477, IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.7990654205607477);
                POINTER.lineTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.7990654205607477);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
                POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
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
                COLOR_BOX.moveTo(IMAGE_WIDTH * 0.2523364485981308, IMAGE_HEIGHT * 0.8317757009345794);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.2523364485981308, IMAGE_HEIGHT * 0.822429906542056);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.16822429906542055, IMAGE_HEIGHT * 0.822429906542056);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.16822429906542055, IMAGE_HEIGHT * 0.8317757009345794);
                COLOR_BOX.lineTo(IMAGE_WIDTH * 0.2523364485981308, IMAGE_HEIGHT * 0.8317757009345794);
                COLOR_BOX.closePath();                
                G2.setColor(getPointerColor().MEDIUM);
                G2.fill(COLOR_BOX);
                break;
                
            case TYPE1:
                                                
            default:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.curveTo(IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.7429906542056075, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.7289719626168224, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.curveTo(IMAGE_WIDTH * 0.7102803738317757, IMAGE_HEIGHT * 0.822429906542056, IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178, IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178, IMAGE_WIDTH * 0.7149532710280374, IMAGE_HEIGHT * 0.8364485981308412, IMAGE_WIDTH * 0.7242990654205608, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.curveTo(IMAGE_WIDTH * 0.7429906542056075, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.curveTo(IMAGE_WIDTH * 0.8084112149532711, IMAGE_HEIGHT * 0.8551401869158879, IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.curveTo(IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8084112149532711, IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.curveTo(IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8084112149532711, IMAGE_HEIGHT * 0.7990654205607477, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);

        final java.awt.geom.GeneralPath POINTER;

        switch(POINTER_TYPE)
        {
            case TYPE1:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.curveTo(IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.7429906542056075, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.7289719626168224, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.curveTo(IMAGE_WIDTH * 0.7102803738317757, IMAGE_HEIGHT * 0.822429906542056, IMAGE_WIDTH * 0.2102803738317757, IMAGE_HEIGHT * 0.8271028037383178, IMAGE_WIDTH * 0.2102803738317757, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.2102803738317757, IMAGE_HEIGHT * 0.8271028037383178, IMAGE_WIDTH * 0.7149532710280374, IMAGE_HEIGHT * 0.8364485981308412, IMAGE_WIDTH * 0.7242990654205608, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.curveTo(IMAGE_WIDTH * 0.7429906542056075, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.curveTo(IMAGE_WIDTH * 0.8084112149532711, IMAGE_HEIGHT * 0.8551401869158879, IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.curveTo(IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8084112149532711, IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.curveTo(IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8084112149532711, IMAGE_HEIGHT * 0.7990654205607477, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE2:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.lineTo(IMAGE_WIDTH * 0.7897196261682243, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.6635514018691588, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.2102803738317757, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.2102803738317757, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.6682242990654206, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.7897196261682243, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.curveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.curveTo(IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8084112149532711, IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.curveTo(IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
            
            case TYPE3:
                break;
                
            case TYPE4:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.17757009345794392, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.lineTo(IMAGE_WIDTH * 0.897196261682243, IMAGE_HEIGHT * 0.8037383177570093);
                POINTER.lineTo(IMAGE_WIDTH * 0.897196261682243, IMAGE_HEIGHT * 0.8504672897196262);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.17757009345794392, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE5:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.7990654205607477);
                POINTER.lineTo(IMAGE_WIDTH * 0.16822429906542055, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8551401869158879);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE6:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.lineTo(IMAGE_WIDTH * 0.6495327102803738, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.lineTo(IMAGE_WIDTH * 0.6448598130841121, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.6495327102803738, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.6495327102803738, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8457943925233645);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE7:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8504672897196262);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8084112149532711);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE8:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.lineTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486);
                POINTER.curveTo(IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8177570093457944, IMAGE_WIDTH * 0.16822429906542055, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.7850467289719626, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8271028037383178, IMAGE_HEIGHT * 0.8598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE9:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.lineTo(IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.822429906542056);
                POINTER.lineTo(IMAGE_WIDTH * 0.7663551401869159, IMAGE_HEIGHT * 0.8130841121495327);
                POINTER.lineTo(IMAGE_WIDTH * 0.7663551401869159, IMAGE_HEIGHT * 0.8411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.8317757009345794);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.lineTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8551401869158879);
                POINTER.lineTo(IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.8551401869158879);
                POINTER.curveTo(IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.8551401869158879, IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8504672897196262, IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8504672897196262);
                POINTER.curveTo(IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8504672897196262, IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8271028037383178);
                POINTER.curveTo(IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8084112149532711, IMAGE_WIDTH * 0.8925233644859814, IMAGE_HEIGHT * 0.8037383177570093, IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8037383177570093);
                POINTER.curveTo(IMAGE_WIDTH * 0.8878504672897196, IMAGE_HEIGHT * 0.8037383177570093, IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.7990654205607477, IMAGE_WIDTH * 0.8551401869158879, IMAGE_HEIGHT * 0.7990654205607477);
                POINTER.lineTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.7990654205607477);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.16355140186915887, IMAGE_HEIGHT * 0.8364485981308412);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
        }        

        G2.dispose();

        return IMAGE;
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
        final int IMAGE_HEIGHT = image.getHeight();

        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);

        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.NORTH_EAST || getOrientation() == eu.hansolo.steelseries.tools.Orientation.NORTH_WEST)
        {
            final java.awt.geom.Ellipse2D MIN_POST_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.15887850522994995, IMAGE_HEIGHT * 0.836448609828949, IMAGE_WIDTH * 0.03738318383693695, IMAGE_HEIGHT * 0.03738313913345337);
            final java.awt.geom.Point2D MIN_POST_FRAME_START = new java.awt.geom.Point2D.Double(0, MIN_POST_FRAME.getBounds2D().getMinY() );
            final java.awt.geom.Point2D MIN_POST_FRAME_STOP = new java.awt.geom.Point2D.Double(0, MIN_POST_FRAME.getBounds2D().getMaxY() );
            final float[] MIN_POST_FRAME_FRACTIONS =
            {
                0.0f,
                0.46f,
                1.0f
            };
            final java.awt.Color[] MIN_POST_FRAME_COLORS =
            {
                new java.awt.Color(180, 180, 180, 255),
                new java.awt.Color(63, 63, 63, 255),
                new java.awt.Color(40, 40, 40, 255)
            };
            final java.awt.LinearGradientPaint MIN_POST_FRAME_GRADIENT = new java.awt.LinearGradientPaint(MIN_POST_FRAME_START, MIN_POST_FRAME_STOP, MIN_POST_FRAME_FRACTIONS, MIN_POST_FRAME_COLORS);
            G2.setPaint(MIN_POST_FRAME_GRADIENT);
            G2.fill(MIN_POST_FRAME);

            final java.awt.geom.Ellipse2D MIN_POST_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.16355140507221222, IMAGE_HEIGHT * 0.84112149477005, IMAGE_WIDTH * 0.028037384152412415, IMAGE_HEIGHT * 0.02803736925125122);
            final java.awt.geom.Point2D MIN_POST_MAIN_START = new java.awt.geom.Point2D.Double(0, MIN_POST_MAIN.getBounds2D().getMinY() );
            final java.awt.geom.Point2D MIN_POST_MAIN_STOP = new java.awt.geom.Point2D.Double(0, MIN_POST_MAIN.getBounds2D().getMaxY() );
            final float[] MIN_POST_MAIN_FRACTIONS =
            {
                0.0f,
                0.5f,
                1.0f
            };

            final java.awt.Color[] MIN_POST_MAIN_COLORS;
            switch(getModel().getKnobStyle())
            {
                case BLACK:
                    MIN_POST_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0xBFBFBF),
                        new java.awt.Color(0x2B2A2F),
                        new java.awt.Color(0x7D7E80)
                    };
                    break;

                case BRASS:
                    MIN_POST_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0xDFD0AE),
                        new java.awt.Color(0x7A5E3E),
                        new java.awt.Color(0xCFBE9D)
                    };
                    break;

                case SILVER:

                default:
                    MIN_POST_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0xD7D7D7),
                        new java.awt.Color(0x747474),
                        new java.awt.Color(0xD7D7D7)
                    };
                    break;
            }
            final java.awt.LinearGradientPaint MIN_POST_MAIN_GRADIENT = new java.awt.LinearGradientPaint(MIN_POST_MAIN_START, MIN_POST_MAIN_STOP, MIN_POST_MAIN_FRACTIONS, MIN_POST_MAIN_COLORS);
            G2.setPaint(MIN_POST_MAIN_GRADIENT);
            G2.fill(MIN_POST_MAIN);
        }

        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.SOUTH_EAST || getOrientation() == eu.hansolo.steelseries.tools.Orientation.SOUTH_WEST)
        {
            final java.awt.geom.Ellipse2D MIN_POST1_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.8317757248878479, IMAGE_HEIGHT * 0.1682243049144745, IMAGE_WIDTH * 0.03738313913345337, IMAGE_HEIGHT * 0.03738316893577576);
            final java.awt.geom.Point2D MIN_POST1_FRAME_START = new java.awt.geom.Point2D.Double(MIN_POST1_FRAME.getBounds2D().getMaxX(), 0);
            final java.awt.geom.Point2D MIN_POST1_FRAME_STOP = new java.awt.geom.Point2D.Double(MIN_POST1_FRAME.getBounds2D().getMinX(), 0);
            final float[] MIN_POST1_FRAME_FRACTIONS =
            {
                0.0f,
                0.46f,
                1.0f
            };
            final java.awt.Color[] MIN_POST1_FRAME_COLORS =
            {
                new java.awt.Color(180, 180, 180, 255),
                new java.awt.Color(63, 63, 63, 255),
                new java.awt.Color(40, 40, 40, 255)
            };
            final java.awt.LinearGradientPaint MIN_POST1_FRAME_GRADIENT = new java.awt.LinearGradientPaint(MIN_POST1_FRAME_START, MIN_POST1_FRAME_STOP, MIN_POST1_FRAME_FRACTIONS, MIN_POST1_FRAME_COLORS);
            G2.setPaint(MIN_POST1_FRAME_GRADIENT);
            G2.fill(MIN_POST1_FRAME);

            final java.awt.geom.Ellipse2D MIN_POST1_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.836448609828949, IMAGE_HEIGHT * 0.17289718985557556, IMAGE_WIDTH * 0.02803736925125122, IMAGE_HEIGHT * 0.028037384152412415);
            final java.awt.geom.Point2D MIN_POST1_MAIN_START = new java.awt.geom.Point2D.Double(MIN_POST1_MAIN.getBounds2D().getMaxX(), 0);
            final java.awt.geom.Point2D MIN_POST1_MAIN_STOP = new java.awt.geom.Point2D.Double(MIN_POST1_MAIN.getBounds2D().getMinX(), 0);
            final float[] MIN_POST1_MAIN_FRACTIONS =
            {
                0.0f,
                0.5f,
                1.0f
            };

            final java.awt.Color[] MIN_POST1_MAIN_COLORS;
            switch(getModel().getKnobStyle())
            {
                case BLACK:
                    MIN_POST1_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0xBFBFBF),
                        new java.awt.Color(0x2B2A2F),
                        new java.awt.Color(0x7D7E80)
                    };
                    break;

                case BRASS:
                    MIN_POST1_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0xDFD0AE),
                        new java.awt.Color(0x7A5E3E),
                        new java.awt.Color(0xCFBE9D)
                    };
                    break;

                case SILVER:

                default:
                    MIN_POST1_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0xD7D7D7),
                        new java.awt.Color(0x747474),
                        new java.awt.Color(0xD7D7D7)
                    };
                    break;
            }
            final java.awt.LinearGradientPaint MIN_POST1_MAIN_GRADIENT = new java.awt.LinearGradientPaint(MIN_POST1_MAIN_START, MIN_POST1_MAIN_STOP, MIN_POST1_MAIN_FRACTIONS, MIN_POST1_MAIN_COLORS);
            G2.setPaint(MIN_POST1_MAIN_GRADIENT);
            G2.fill(MIN_POST1_MAIN);
        }

        switch(getKnobType())
        {
            case SMALL_STD_KNOB:            
                final java.awt.geom.Ellipse2D CENTER_KNOB_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7850467562675476, IMAGE_HEIGHT * 0.7850467562675476, IMAGE_WIDTH * 0.08411210775375366, IMAGE_HEIGHT * 0.08411210775375366);
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

                final java.awt.geom.Ellipse2D CENTER_KNOB_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7943925261497498, IMAGE_HEIGHT * 0.7943925261497498, IMAGE_WIDTH * 0.06542056798934937, IMAGE_HEIGHT * 0.06542056798934937);
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

                final java.awt.geom.Ellipse2D CENTER_KNOB_INNERSHADOW = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7943925261497498, IMAGE_HEIGHT * 0.7943925261497498, IMAGE_WIDTH * 0.06542056798934937, IMAGE_HEIGHT * 0.06542056798934937);
                final java.awt.geom.Point2D CENTER_KNOB_INNERSHADOW_CENTER = new java.awt.geom.Point2D.Double( (0.822429906542056 * IMAGE_WIDTH), (0.8177570093457944 * IMAGE_HEIGHT) );
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
                final java.awt.RadialGradientPaint CENTER_KNOB_INNERSHADOW_GRADIENT = new java.awt.RadialGradientPaint(CENTER_KNOB_INNERSHADOW_CENTER, (float)(0.03271028037383177 * IMAGE_WIDTH), CENTER_KNOB_INNERSHADOW_FRACTIONS, CENTER_KNOB_INNERSHADOW_COLORS);
                G2.setPaint(CENTER_KNOB_INNERSHADOW_GRADIENT);
                G2.fill(CENTER_KNOB_INNERSHADOW);
                break;
        
            case BIG_STD_KNOB:
                final java.awt.geom.Ellipse2D BIGCENTER_BACKGROUNDFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7663551568984985, IMAGE_HEIGHT * 0.7663551568984985, IMAGE_WIDTH * 0.1214953064918518, IMAGE_HEIGHT * 0.1214953064918518);
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

                final java.awt.geom.Ellipse2D BIGCENTER_BACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7710280418395996, IMAGE_HEIGHT * 0.7710280418395996, IMAGE_WIDTH * 0.11214953660964966, IMAGE_HEIGHT * 0.11214953660964966);
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

                final java.awt.geom.Ellipse2D BIGCENTER_FOREGROUNDFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7803738117218018, IMAGE_HEIGHT * 0.7803738117218018, IMAGE_WIDTH * 0.09345793724060059, IMAGE_HEIGHT * 0.09345793724060059);
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

                final java.awt.geom.Ellipse2D BIGCENTER_FOREGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7850467562675476, IMAGE_HEIGHT * 0.7850467562675476, IMAGE_WIDTH * 0.08411210775375366, IMAGE_HEIGHT * 0.08411210775375366);
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
                final java.awt.geom.Ellipse2D CHROMEKNOB_BACKFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7570093274116516, IMAGE_HEIGHT * 0.7570093274116516, IMAGE_WIDTH * 0.14018690586090088, IMAGE_HEIGHT * 0.14018690586090088);
                final java.awt.geom.Point2D CHROMEKNOB_BACKFRAME_START = new java.awt.geom.Point2D.Double( (0.7897196261682243 * IMAGE_WIDTH), (0.7663551401869159 * IMAGE_HEIGHT) );
                final java.awt.geom.Point2D CHROMEKNOB_BACKFRAME_STOP = new java.awt.geom.Point2D.Double( ((0.7897196261682243 + 0.0718114890783315) * IMAGE_WIDTH), ((0.7663551401869159 + 0.1149224055539082) * IMAGE_HEIGHT) );
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

                final java.awt.geom.Ellipse2D CHROMEKNOB_BACK = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7616822719573975, IMAGE_HEIGHT * 0.7616822719573975, IMAGE_WIDTH * 0.13084107637405396, IMAGE_HEIGHT * 0.13084107637405396);                
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

                final java.awt.geom.Ellipse2D CHROMEKNOB_FOREFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7943925261497498, IMAGE_HEIGHT * 0.7943925261497498, IMAGE_WIDTH * 0.06542056798934937, IMAGE_HEIGHT * 0.06542056798934937);
                final java.awt.geom.Point2D CHROMEKNOB_FOREFRAME_START = new java.awt.geom.Point2D.Double( (0.8084112149532711 * IMAGE_WIDTH), (0.7990654205607477 * IMAGE_HEIGHT) );
                final java.awt.geom.Point2D CHROMEKNOB_FOREFRAME_STOP = new java.awt.geom.Point2D.Double( ((0.8084112149532711 + 0.033969662360372466) * IMAGE_WIDTH), ((0.7990654205607477 + 0.05036209552904459) * IMAGE_HEIGHT) );
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

                final java.awt.geom.Ellipse2D CHROMEKNOB_FORE = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7990654110908508, IMAGE_HEIGHT * 0.7990654110908508, IMAGE_WIDTH * 0.05607479810714722, IMAGE_HEIGHT * 0.05607479810714722);
                final java.awt.geom.Point2D CHROMEKNOB_FORE_START = new java.awt.geom.Point2D.Double( (0.8084112149532711 * IMAGE_WIDTH), (0.8037383177570093 * IMAGE_HEIGHT) );
                final java.awt.geom.Point2D CHROMEKNOB_FORE_STOP = new java.awt.geom.Point2D.Double( ((0.8084112149532711 + 0.03135661140957459) * IMAGE_WIDTH), ((0.8037383177570093 + 0.04648808818065655) * IMAGE_HEIGHT) );
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
                final java.awt.geom.Ellipse2D METALKNOB_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7897196412086487, IMAGE_HEIGHT * 0.7850467562675476, IMAGE_WIDTH * 0.08411210775375366, IMAGE_HEIGHT * 0.08411210775375366);
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

                final java.awt.geom.Ellipse2D METALKNOB_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.7943925261497498, IMAGE_HEIGHT * 0.7897196412086487, IMAGE_WIDTH * 0.07476633787155151, IMAGE_HEIGHT * 0.07476633787155151);
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
                METALKNOB_LOWERHL.moveTo(IMAGE_WIDTH * 0.8504672897196262, IMAGE_HEIGHT * 0.8551401869158879);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.8504672897196262, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8411214953271028, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.8317757009345794, IMAGE_HEIGHT * 0.8411214953271028);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.8177570093457944, IMAGE_HEIGHT * 0.8411214953271028, IMAGE_WIDTH * 0.8084112149532711, IMAGE_HEIGHT * 0.8457943925233645, IMAGE_WIDTH * 0.8084112149532711, IMAGE_HEIGHT * 0.8551401869158879);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.8130841121495327, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.822429906542056, IMAGE_HEIGHT * 0.8644859813084113, IMAGE_WIDTH * 0.8317757009345794, IMAGE_HEIGHT * 0.8644859813084113);
                METALKNOB_LOWERHL.curveTo(IMAGE_WIDTH * 0.8364485981308412, IMAGE_HEIGHT * 0.8644859813084113, IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.8598130841121495, IMAGE_WIDTH * 0.8504672897196262, IMAGE_HEIGHT * 0.8551401869158879);
                METALKNOB_LOWERHL.closePath();
                final java.awt.geom.Point2D METALKNOB_LOWERHL_CENTER = new java.awt.geom.Point2D.Double( (0.8317757009345794 * IMAGE_WIDTH), (0.8644859813084113 * IMAGE_HEIGHT) );
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
                final java.awt.RadialGradientPaint METALKNOB_LOWERHL_GRADIENT = new java.awt.RadialGradientPaint(METALKNOB_LOWERHL_CENTER, (float)(0.03271028037383177 * IMAGE_WIDTH), METALKNOB_LOWERHL_FRACTIONS, METALKNOB_LOWERHL_COLORS);
                G2.setPaint(METALKNOB_LOWERHL_GRADIENT);
                G2.fill(METALKNOB_LOWERHL);

                final java.awt.geom.GeneralPath METALKNOB_UPPERHL = new java.awt.geom.GeneralPath();
                METALKNOB_UPPERHL.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                METALKNOB_UPPERHL.moveTo(IMAGE_WIDTH * 0.8644859813084113, IMAGE_HEIGHT * 0.8084112149532711);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.7850467289719626, IMAGE_WIDTH * 0.8317757009345794, IMAGE_HEIGHT * 0.7850467289719626);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.8130841121495327, IMAGE_HEIGHT * 0.7850467289719626, IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.794392523364486, IMAGE_WIDTH * 0.794392523364486, IMAGE_HEIGHT * 0.8084112149532711);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.7990654205607477, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.8130841121495327, IMAGE_HEIGHT * 0.8177570093457944, IMAGE_WIDTH * 0.8317757009345794, IMAGE_HEIGHT * 0.8177570093457944);
                METALKNOB_UPPERHL.curveTo(IMAGE_WIDTH * 0.8457943925233645, IMAGE_HEIGHT * 0.8177570093457944, IMAGE_WIDTH * 0.8598130841121495, IMAGE_HEIGHT * 0.8130841121495327, IMAGE_WIDTH * 0.8644859813084113, IMAGE_HEIGHT * 0.8084112149532711);
                METALKNOB_UPPERHL.closePath();
                final java.awt.geom.Point2D METALKNOB_UPPERHL_CENTER = new java.awt.geom.Point2D.Double( (0.8271028037383178 * IMAGE_WIDTH), (0.7850467289719626 * IMAGE_HEIGHT) );
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
                final java.awt.RadialGradientPaint METALKNOB_UPPERHL_GRADIENT = new java.awt.RadialGradientPaint(METALKNOB_UPPERHL_CENTER, (float)(0.04906542056074766 * IMAGE_WIDTH), METALKNOB_UPPERHL_FRACTIONS, METALKNOB_UPPERHL_COLORS);
                G2.setPaint(METALKNOB_UPPERHL_GRADIENT);
                G2.fill(METALKNOB_UPPERHL);

                final java.awt.geom.Ellipse2D METALKNOB_INNERFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.8084112405776978, IMAGE_HEIGHT * 0.8084112405776978, IMAGE_WIDTH * 0.04205602407455444, IMAGE_HEIGHT * 0.04205602407455444);
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

                final java.awt.geom.Ellipse2D METALKNOB_INNERBACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.8130841255187988, IMAGE_HEIGHT * 0.8130841255187988, IMAGE_WIDTH * 0.032710254192352295, IMAGE_HEIGHT * 0.032710254192352295);
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
        G2.dispose();

        return image;
    }

    private java.awt.image.BufferedImage create_FOREGROUND_Image(final int WIDTH, java.awt.image.BufferedImage image)
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
        final int IMAGE_HEIGHT = image.getHeight();

        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);

        if (getOrientation() == eu.hansolo.steelseries.tools.Orientation.NORTH_EAST || getOrientation() == eu.hansolo.steelseries.tools.Orientation.NORTH_WEST)
        {
            final java.awt.geom.GeneralPath HIGHLIGHT = new java.awt.geom.GeneralPath();
            HIGHLIGHT.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            HIGHLIGHT.moveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.3925233644859813);
            HIGHLIGHT.curveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.35514018691588783, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028);
            HIGHLIGHT.curveTo(IMAGE_WIDTH * 0.7710280373831776, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.5887850467289719, IMAGE_HEIGHT * 0.102803738317757, IMAGE_WIDTH * 0.4392523364485981, IMAGE_HEIGHT * 0.205607476635514);
            HIGHLIGHT.curveTo(IMAGE_WIDTH * 0.3037383177570093, IMAGE_HEIGHT * 0.29439252336448596, IMAGE_WIDTH * 0.22429906542056074, IMAGE_HEIGHT * 0.37850467289719625, IMAGE_WIDTH * 0.1542056074766355, IMAGE_HEIGHT * 0.5420560747663551);
            HIGHLIGHT.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3691588785046729, IMAGE_WIDTH * 0.794392523364486, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.3925233644859813);
            HIGHLIGHT.closePath();
            final java.awt.geom.Point2D HIGHLIGHT_START = new java.awt.geom.Point2D.Double(0, HIGHLIGHT.getBounds2D().getMinY());
            final java.awt.geom.Point2D HIGHLIGHT_STOP = new java.awt.geom.Point2D.Double(0, HIGHLIGHT.getBounds2D().getMaxY());
            final float[] HIGHLIGHT_FRACTIONS =
            {
                0.0f,
                1.0f
            };
            final java.awt.Color[] HIGHLIGHT_COLORS =
            {
                new java.awt.Color(255, 255, 255, 63),
                new java.awt.Color(255, 255, 255, 12)
            };
            final java.awt.LinearGradientPaint HIGHLIGHT_GRADIENT = new java.awt.LinearGradientPaint(HIGHLIGHT_START, HIGHLIGHT_STOP, HIGHLIGHT_FRACTIONS, HIGHLIGHT_COLORS);
            G2.setPaint(HIGHLIGHT_GRADIENT);
            G2.fill(HIGHLIGHT);
        }
        else
        {
            final java.awt.geom.GeneralPath HIGHLIGHT_FLIPPED = new java.awt.geom.GeneralPath();
            HIGHLIGHT_FLIPPED.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
            HIGHLIGHT_FLIPPED.moveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
            HIGHLIGHT_FLIPPED.curveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.5560747663551402);
            HIGHLIGHT_FLIPPED.curveTo(IMAGE_WIDTH * 0.5841121495327103, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.22897196261682243, IMAGE_HEIGHT * 0.6308411214953271, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897);
            HIGHLIGHT_FLIPPED.curveTo(IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
            HIGHLIGHT_FLIPPED.closePath();
            final java.awt.geom.Point2D HIGHLIGHT_FLIPPED_START = new java.awt.geom.Point2D.Double(0, HIGHLIGHT_FLIPPED.getBounds2D().getMaxY() );
            final java.awt.geom.Point2D HIGHLIGHT_FLIPPED_STOP = new java.awt.geom.Point2D.Double(0, HIGHLIGHT_FLIPPED.getBounds2D().getMinY() );
            final float[] HIGHLIGHT_FLIPPED_FRACTIONS =
            {
                0.0f,
                1.0f
            };
            final java.awt.Color[] HIGHLIGHT_FLIPPED_COLORS =
            {
                new java.awt.Color(255, 255, 255, 63),
                new java.awt.Color(255, 255, 255, 12)
            };
            final java.awt.LinearGradientPaint HIGHLIGHT_FLIPPED_GRADIENT = new java.awt.LinearGradientPaint(HIGHLIGHT_FLIPPED_START, HIGHLIGHT_FLIPPED_STOP, HIGHLIGHT_FLIPPED_FRACTIONS, HIGHLIGHT_FLIPPED_COLORS);
            G2.setPaint(HIGHLIGHT_FLIPPED_GRADIENT);
            G2.fill(HIGHLIGHT_FLIPPED);
        }
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

        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        transformGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, G2);

        final java.awt.geom.GeneralPath BACKGROUND = new java.awt.geom.GeneralPath();
        BACKGROUND.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        BACKGROUND.moveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.08411214953271028);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.6401869158878505, IMAGE_HEIGHT * 0.08411214953271028, IMAGE_WIDTH * 0.46261682242990654, IMAGE_HEIGHT * 0.1588785046728972, IMAGE_WIDTH * 0.29439252336448596, IMAGE_HEIGHT * 0.32242990654205606);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.17289719626168223, IMAGE_HEIGHT * 0.4439252336448598, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.6635514018691588, IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897);
        BACKGROUND.curveTo(IMAGE_WIDTH * 0.08411214953271028, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897, IMAGE_WIDTH * 0.9158878504672897, IMAGE_HEIGHT * 0.9158878504672897);
        BACKGROUND.closePath();

        G2.setColor(new java.awt.Color(102, 102, 102, 178));        
        G2.fill(BACKGROUND);
        
        G2.dispose();

        return IMAGE;
    }
    
    @Override
    public String toString()
    {
        return "Radial1Square";
    }
}
