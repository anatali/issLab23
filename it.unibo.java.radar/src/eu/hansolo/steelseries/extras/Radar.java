package eu.hansolo.steelseries.extras;


/**
 *
 * @author hansolo
 */
public final class Radar extends eu.hansolo.steelseries.gauges.AbstractRadial implements java.awt.event.ActionListener
{    
    private final eu.hansolo.steelseries.extras.Poi MY_LOCATION = new eu.hansolo.steelseries.extras.Poi("Home", 0, 0);
    private double range = 100000;
    private static final int INITIAL_WIDTH = 200;
    private double pixelScaleX = this.range / (0.4 * INITIAL_WIDTH) / 620;
    private double pixelScaleY = this.range / (0.4 * INITIAL_WIDTH) / 1000;
    private java.awt.geom.Point2D CENTER_XY = MY_LOCATION.getLocationXY();
    private final java.util.concurrent.ConcurrentHashMap<String, eu.hansolo.steelseries.extras.Poi> POIS = new java.util.concurrent.ConcurrentHashMap<String, eu.hansolo.steelseries.extras.Poi>(64);
    private final java.util.concurrent.ConcurrentHashMap<String, eu.hansolo.steelseries.extras.Poi> BLIPS = new java.util.concurrent.ConcurrentHashMap<String, eu.hansolo.steelseries.extras.Poi>(64);
    private final java.awt.Color BLIP_TEXT_COLOR = new java.awt.Color(0x619E65);
    private final java.awt.Font BLIP_FONT = new java.awt.Font("Verdana", 0, 6);
    private final java.awt.geom.Line2D BEAM = new java.awt.geom.Line2D.Double(INITIAL_WIDTH / 2.0, INITIAL_WIDTH / 2.0, INITIAL_WIDTH * 0.79, INITIAL_WIDTH * 0.79);
    private final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double();    
    
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;    
    private java.awt.image.BufferedImage beamImage;
    private java.awt.image.BufferedImage disabledImage;
    private final java.awt.Color BEAM_COLOR = new java.awt.Color(130, 230, 150, 180);    
    private double rotationAngle = 0;
    private org.pushingpixels.trident.Timeline timeline = new org.pushingpixels.trident.Timeline(this);

    
    public Radar()
    {
        super();                
        init(getInnerBounds().width, getInnerBounds().height);
    }

    @Override
    public final eu.hansolo.steelseries.gauges.AbstractGauge init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        pixelScaleX = this.range / (0.4 * WIDTH) / 620; 
        pixelScaleY = this.range / (0.4 * WIDTH) / 1000;
        
        // Create Background Image
        if (bImage != null)
        {
            bImage.flush();
        }
        bImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
                
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
            create_BACKGROUND_Image(WIDTH, bImage);
        }
                
        create_TICKMARKS_Image(WIDTH, bImage);
        
        if (beamImage != null)
        {
            beamImage.flush();
        }
        beamImage = create_BEAM_Image(WIDTH);
        
        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH);
        
        BEAM.setLine(getInnerBounds().width / 2.0, getInnerBounds().width / 2.0, getInnerBounds().width * 0.79, getInnerBounds().width * 0.79);

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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Translate the coordinate system related to insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);

        // Draw blips
        G2.setColor(BLIP_TEXT_COLOR);
        G2.setFont(BLIP_FONT);
        for (eu.hansolo.steelseries.extras.Poi poi : BLIPS.values())
        {
            if (poi.distanceTo(MY_LOCATION) < this.range)
            {                
                G2.drawImage(poi.getPoiImage(), (int) (CENTER.getX() - poi.getPoiImage().getWidth() / 2.0 + (poi.getLocationXY().getX() - CENTER_XY.getX()) / pixelScaleX),  (int)(CENTER.getY() - poi.getPoiImage().getWidth() / 2.0 + (poi.getLocationXY().getY() - CENTER_XY.getY()) / pixelScaleY), null);
                G2.drawString(poi.getName(), (int) (CENTER.getX() - poi.getPoiImage().getWidth() + (poi.getLocationXY().getX() - CENTER_XY.getX()) / pixelScaleX),  (int)(CENTER.getY() - poi.getPoiImage().getWidth() + (poi.getLocationXY().getY() - CENTER_XY.getY()) / pixelScaleY));
            }
        }

        // Draw the beam
        G2.rotate(rotationAngle, CENTER.getX(), CENTER.getY());
        G2.drawImage(beamImage, 0, 0, null);
        G2.setTransform(OLD_TRANSFORM);
        
        G2.rotate(Math.toRadians(-135) + rotationAngle, CENTER.getX(), CENTER.getY());
        G2.setColor(BEAM_COLOR);
        G2.draw(BEAM);
        G2.setTransform(OLD_TRANSFORM);

        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate the coordinate system back to original
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }

    /**
     * Returns the rotation angle of the radar beam
     * @return the rotation angle of the radar beam
     */
    public double getRotationAngle()
    {
        return this.rotationAngle;
    }

    /**
     * Sets the rotation angle of the radar beam
     * @param ROTATION_ANGLE
     */
    public void setRotationAngle(final double ROTATION_ANGLE)
    {
        this.rotationAngle = ROTATION_ANGLE;

        repaint();
    }

    /**
     * Returns the range of the radar in meters which means
     * the distance from the center of the radar to it's
     * outer circle
     * @return the range of the radar in meters
     */
    public double getRange()
    {
        return this.range;
    }

    /**
     * Sets the range of the radar in meters which means
     * the distance from the center of the radar to it's
     * outer circle
     * @param RANGE
     */
    public void setRange(final double RANGE)
    {
        this.range = RANGE;
        checkForBlips();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint();
    }

    /**
     * Returns the position of the center of the radar as
     * point of interest (poi) object with it's coordinates
     * as latitude and longitude
     * @return poi that contains the latitude and longitude of
     * the center of the radar
     */
    public eu.hansolo.steelseries.extras.Poi getMyLocation()
    {
        return this.MY_LOCATION;
    }

    /**
     * Defines the position of the center of the radar by the
     * coordinates of the given point of interest (poi) object
     * @param NEW_LOCATION
     */
    public void setMyLocation(final eu.hansolo.steelseries.extras.Poi NEW_LOCATION)
    {
        this.MY_LOCATION.setLocation(NEW_LOCATION.getLocation());
        checkForBlips();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint();
    }

    /**
     * Defines the position of the center of the radar by the
     * given coordinates as latitude and longitude
     * @param LON
     * @param LAT
     */
    public void setMyLocation(final double LON, final double LAT)
    {
        this.MY_LOCATION.setLocation(LON, LAT);
        checkForBlips();
        init(getInnerBounds().width, getInnerBounds().height);
        repaint();
    }

    /**
     * Adds a new point of interest to the list of poi's of the radar
     * Keep in mind that only the poi's are visible as blips that are
     * in the range of the radar.
     * @param BLIP
     */
    public void addPoi(final eu.hansolo.steelseries.extras.Poi BLIP)
    {
        if (POIS.keySet().contains(BLIP.getName()))
        {
            updatePoi(BLIP.getName(), BLIP.getLocation());
        }
        else
        {
            POIS.put(BLIP.getName(), BLIP);
        }
        checkForBlips();
    }

    /**
     * Updates the position of the given poi by it's name (BLIP_NAME) on
     * the radar screen. This could be useful to visualize moving points.
     * Keep in mind that only the poi's are visible as blips that are
     * in the range of the radar.
     * @param BLIP_NAME
     * @param LOCATION
     */
    public void updatePoi(final String BLIP_NAME, final java.awt.geom.Point2D LOCATION)
    {
        if (POIS.keySet().contains(BLIP_NAME))
        {
            POIS.get(BLIP_NAME).setLocation(LOCATION);
            checkForBlips();
        }
    }

    /**
     * Removes a point of interest from the radar
     * Keep in mind that only the poi's are visible as blips that are
     * in the range of the radar.
     * @param BLIP
     */
    public void removePoi(eu.hansolo.steelseries.extras.Poi BLIP)
    {
        if (POIS.keySet().contains(BLIP.getName()))
        {
            POIS.remove(BLIP.getName());
            checkForBlips();
        }
    }

    /**
     * Returns the point of interest given by it's name
     * Keep in mind that only the poi's are visible as blips that are
     * in the range of the radar.
     * @param NAME
     * @return the point of interest given by it's name
     */
    public eu.hansolo.steelseries.extras.Poi getPoi(final String NAME)
    {
        final eu.hansolo.steelseries.extras.Poi POINT_OF_INTEREST;
        if (POIS.keySet().contains(NAME))
        {
            POINT_OF_INTEREST = POIS.get(NAME);
        }
        else
        {
            POINT_OF_INTEREST = null;
        }

        return POINT_OF_INTEREST;
    }

    /**
     * Animates the radar beam of the component. This has no effect
     * on the functionality but is only eye candy.
     * @param RUN enables/disables the animation of the beam
     */
    public void animate(final boolean RUN)
    {
        if (isEnabled())
        {
            if (RUN)
            {
                if (timeline.getState() != org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD && timeline.getState() != org.pushingpixels.trident.Timeline.TimelineState.SUSPENDED)
                {
                    timeline = new org.pushingpixels.trident.Timeline(this);
                    timeline.addPropertyToInterpolate("rotationAngle", this.rotationAngle, 2 * Math.PI);
                    timeline.setEase(new org.pushingpixels.trident.ease.Linear());
                    timeline.setDuration((long) (5000));
                    timeline.playLoop(org.pushingpixels.trident.Timeline.RepeatBehavior.LOOP);
                }
                else if (timeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.SUSPENDED)
                {
                    timeline.resume();
                }
            }
            else
            {
                timeline.suspend();
            }
        }
    }

    /**
     * Checks for poi's in the range of the radar
     */
    private void checkForBlips()
    {
        BLIPS.clear();
        for (eu.hansolo.steelseries.extras.Poi poi : POIS.values())
        {
            if (poi.distanceTo(MY_LOCATION) < this.range)
            {
                if (!BLIPS.keySet().contains(poi.getName()))
                {
                    BLIPS.put(poi.getName(), poi);
                }
            }
        }
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
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();

        final java.awt.geom.Ellipse2D E_GAUGE_BACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);
        final java.awt.geom.Point2D E_GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, E_GAUGE_BACKGROUND.getBounds2D().getMinY() );
        final java.awt.geom.Point2D E_GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, E_GAUGE_BACKGROUND.getBounds2D().getMaxY() );
        final float[] E_GAUGE_BACKGROUND_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] E_GAUGE_BACKGROUND_COLORS =
        {
            new java.awt.Color(0x001F04),
            new java.awt.Color(0x013505)
        };
        final java.awt.LinearGradientPaint E_GAUGE_BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(E_GAUGE_BACKGROUND_START, E_GAUGE_BACKGROUND_STOP, E_GAUGE_BACKGROUND_FRACTIONS, E_GAUGE_BACKGROUND_COLORS);
        G2.setPaint(E_GAUGE_BACKGROUND_GRADIENT);
        G2.fill(E_GAUGE_BACKGROUND);

        final java.awt.geom.Ellipse2D E_GAUGE_INNERSHADOW = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);
        final java.awt.geom.Point2D E_GAUGE_INNERSHADOW_CENTER = new java.awt.geom.Point2D.Double( (0.5 * IMAGE_WIDTH), (0.5 * IMAGE_HEIGHT) );
        final float[] E_GAUGE_INNERSHADOW_FRACTIONS =
        {
            0.0f,
            0.7f,
            0.71f,
            1.0f
        };
        final java.awt.Color[] E_GAUGE_INNERSHADOW_COLORS =
        {
            new java.awt.Color(0, 90, 40, 0),
            new java.awt.Color(0, 90, 40, 0),
            new java.awt.Color(0, 90, 40, 0),
            new java.awt.Color(0, 90, 40, 76)
        };
        final java.awt.RadialGradientPaint E_GAUGE_INNERSHADOW_GRADIENT = new java.awt.RadialGradientPaint(E_GAUGE_INNERSHADOW_CENTER, (float)(0.4158878504672897 * IMAGE_WIDTH), E_GAUGE_INNERSHADOW_FRACTIONS, E_GAUGE_INNERSHADOW_COLORS);
        G2.setPaint(E_GAUGE_INNERSHADOW_GRADIENT);
        G2.fill(E_GAUGE_INNERSHADOW);

        G2.dispose();

        return image;
    }
   
    private java.awt.image.BufferedImage create_TICKMARKS_Image(final int WIDTH, java.awt.image.BufferedImage image)
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
        G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        final int IMAGE_WIDTH = image.getWidth();

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        
        final java.awt.BasicStroke THIN_STROKE = new java.awt.BasicStroke(0.00390625f * IMAGE_WIDTH, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);        
        final java.awt.Font SMALL_FONT = new java.awt.Font("Verdana", 0, (int) (0.02f * IMAGE_WIDTH));
        final float TEXT_DISTANCE = 0.040f * IMAGE_WIDTH;
        final float MIN_LENGTH = 0.015625f * IMAGE_WIDTH;
        final float MED_LENGTH = 0.0234375f * IMAGE_WIDTH;
        final float MAX_LENGTH = 0.03125f * IMAGE_WIDTH;

        final java.awt.Color TEXT_COLOR = new java.awt.Color(0x619E65);
        final java.awt.Color TICK_COLOR = new java.awt.Color(0x619E65);
        final java.awt.Color TICK_30_COLOR = new java.awt.Color(86, 119, 92, 100);

        // Create the watch itself
        final float RADIUS = IMAGE_WIDTH * 0.4f;
        final java.awt.geom.Point2D TICKMARKS_CENTER = new java.awt.geom.Point2D.Double(IMAGE_WIDTH / 2.0f, IMAGE_WIDTH / 2.0f);
        
        // Draw ticks
        java.awt.geom.Point2D innerPoint;
        java.awt.geom.Point2D outerPoint;
        java.awt.geom.Point2D textPoint = null;
        java.awt.geom.Line2D tick;
        int tickCounter90 = 0;
        int tickCounter30 = 0;
        int tickCounter15 = 0;
        int tickCounter5 = 0;
        int counter = 0;

        double sinValue = 0;
        double cosValue = 0;

        final double STEP = (2.0d * Math.PI) / (360.0d);

        for (double alpha = 2 * Math.PI; alpha >= 0; alpha -= STEP)
        {
            G2.setStroke(THIN_STROKE);
            sinValue = Math.sin(alpha);
            cosValue = Math.cos(alpha);

            G2.setColor(TICK_COLOR);

            if (tickCounter5 == 5)
            {
                G2.setStroke(THIN_STROKE);
                innerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + (RADIUS - MIN_LENGTH) * sinValue, TICKMARKS_CENTER.getY() + (RADIUS - MIN_LENGTH) * cosValue);
                outerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + RADIUS * sinValue, TICKMARKS_CENTER.getY() + RADIUS * cosValue);
                // Draw ticks
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);

                tickCounter5 = 0;
            }

            // Different tickmark every 15 units
            if (tickCounter15 == 15)
            {
                G2.setStroke(THIN_STROKE);
                G2.setColor(TICK_COLOR);
                innerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + (RADIUS - MED_LENGTH) * sinValue, TICKMARKS_CENTER.getY() + (RADIUS - MED_LENGTH) * cosValue);
                outerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + RADIUS * sinValue, TICKMARKS_CENTER.getY() + RADIUS * cosValue);

                // Draw ticks
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);

                tickCounter15 = 0;                
                tickCounter90 += 15;
            }

            // Different tickmark every 30 units
            if (tickCounter30 == 30)
            {
                G2.setStroke(THIN_STROKE);
                G2.setColor(TICK_30_COLOR);
                innerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX(), TICKMARKS_CENTER.getY());
                outerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + (RADIUS - TEXT_DISTANCE * 1.5f) * sinValue, TICKMARKS_CENTER.getY() + (RADIUS - TEXT_DISTANCE * 1.5f) * cosValue);

                // Draw ticks
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);

                tickCounter30 = 0;
                tickCounter90 += 30;
            }

            // Different tickmark every 90 units plus text
            if (tickCounter90 == 90)
            {
                G2.setStroke(THIN_STROKE);
                innerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + (RADIUS - MAX_LENGTH) * sinValue, TICKMARKS_CENTER.getY() + (RADIUS - MAX_LENGTH) * cosValue);
                outerPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + RADIUS * sinValue, TICKMARKS_CENTER.getY() + RADIUS * cosValue);

                // Draw ticks
                G2.setColor(TICK_COLOR);
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);

                tickCounter90 = 0;
            }

            // Draw text            
            G2.setFont(SMALL_FONT);
            G2.setColor(TEXT_COLOR);
            textPoint = new java.awt.geom.Point2D.Double(TICKMARKS_CENTER.getX() + (RADIUS - TEXT_DISTANCE) * sinValue, TICKMARKS_CENTER.getY() + (RADIUS - TEXT_DISTANCE) * cosValue);
            if (counter != 360 && counter % 30 == 0)
            {
                G2.rotate(Math.toRadians(180), TICKMARKS_CENTER.getX(), TICKMARKS_CENTER.getY());
                G2.fill(UTIL.rotateTextAroundCenter(G2, String.valueOf(counter), (int) textPoint.getX(), (int) textPoint.getY(), Math.toDegrees(Math.PI - alpha)));
            }

            G2.setTransform(OLD_TRANSFORM);

            tickCounter5++;
            tickCounter15++;
            tickCounter30++;

            counter ++;
        }

        // Draw distance rings        
        final double RADIUS_STEP = RADIUS / 5.0;
        for (int i = 1 ; i < 6 ; i++)
        {
            G2.setColor(TICK_30_COLOR);
            G2.draw(new java.awt.geom.Ellipse2D.Double(TICKMARKS_CENTER.getX() - (i * RADIUS_STEP), TICKMARKS_CENTER.getY() - (i * RADIUS_STEP), i * 2 * RADIUS_STEP, i * 2 * RADIUS_STEP));
            if (i < 5)
            {
                G2.setColor(TICK_COLOR);
                G2.drawString(String.valueOf((int) (this.range / 5000 * i)), (int) TICKMARKS_CENTER.getX() + 2, (int) (TICKMARKS_CENTER.getY() - RADIUS_STEP * i - 1));
            }
        }

        G2.dispose();

        return image;
    }

    private java.awt.image.BufferedImage create_BEAM_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.Point2D BEAM_CENTER = new java.awt.geom.Point2D.Double(IMAGE_WIDTH / 2.0, IMAGE_HEIGHT / 2.0);

        final float[] BEAMAREA_FRACTIONS =
        {
            0.0f,
            0.001f,
            180.0f,
            360.0f
        };
        final java.awt.Color[] BEAMAREA_COLORS =
        {
            new java.awt.Color(55, 178,72, 100),
            new java.awt.Color(0.0f, 0.5f, 0.0f, 0.0f),
            new java.awt.Color(0.0f, 0.5f, 0.0f, 0.0f),
            new java.awt.Color(55, 178,72, 100)
        };

        final java.awt.geom.Ellipse2D BEAM_AREA = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);

        final eu.hansolo.steelseries.tools.ConicalGradientPaint BEAM_GRADIENT = new eu.hansolo.steelseries.tools.ConicalGradientPaint(true, BEAM_CENTER, 0,  BEAMAREA_FRACTIONS, BEAMAREA_COLORS);
        G2.setPaint(BEAM_GRADIENT);
        G2.fill(BEAM_AREA);

        G2.dispose();

        return IMAGE;
    }
       
    @Override
    public void actionPerformed(java.awt.event.ActionEvent event)
    {

    }

    @Override
    public String toString()
    {
        return "Radar";
    }
}
