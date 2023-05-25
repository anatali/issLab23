package eu.hansolo.steelseries.extras;


/**
 *
 * @author hansolo
 */
public class Horizon extends eu.hansolo.steelseries.gauges.AbstractRadial
{    
    private double roll;
    private double oldRoll;    
    private double pitch;
    private double oldPitch;
    private double pitchPixel;
    private boolean upsidedown = false;
    
    private final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double();    
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;     
    
    private java.awt.image.BufferedImage horizonImage;    
    private java.awt.image.BufferedImage horizonForegroundImage;
    private boolean customColors;
    private java.awt.Color customSkyColor;
    private java.awt.Color customGroundColor;
    private java.awt.image.BufferedImage disabledImage;
    private final java.awt.geom.Ellipse2D CLIP = new java.awt.geom.Ellipse2D.Double();     
    private org.pushingpixels.trident.Timeline timelineRoll = new org.pushingpixels.trident.Timeline(this);
    private org.pushingpixels.trident.Timeline timelinePitch = new org.pushingpixels.trident.Timeline(this);
    //private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);    

        
    public Horizon()
    {
        super();        
        customColors = false;
        customSkyColor = new java.awt.Color(127, 213, 240, 255);
        customGroundColor = new java.awt.Color(60, 68, 57, 255);
        init(getInnerBounds().width, getInnerBounds().height);
        pitch = 0;
        roll = 0;              
    }
   
    @Override
    public final eu.hansolo.steelseries.gauges.AbstractGauge init(final int WIDTH, final int HEIGHT)
    {   
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        pitchPixel = (int)(Math.PI * WIDTH) / 360.0;
        
        // Calculate clip area        
        CLIP.setFrame(WIDTH * 0.08411215245723724, WIDTH * 0.08411215245723724, WIDTH * 0.8317756652832031, WIDTH * 0.8317756652832031);
                
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
                        
        if (horizonImage != null)
        {
            horizonImage.flush();
        }
        horizonImage = create_HORIZON_Image(WIDTH);
                                        
        create_INDICATOR_Image(WIDTH, fImage);
        
        if (horizonForegroundImage != null)
        {
            horizonForegroundImage.flush();
        }
        horizonForegroundImage = create_HORIZON_FOREGROUND_Image(WIDTH);
        
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

        // Translate the coordinate system related to the insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        final java.awt.Shape OLD_CLIP = G2.getClip();
        
        // Draw the horizon
        G2.setClip(CLIP);
        
        // Rotate around roll
        G2.rotate(-Math.toRadians(roll), CENTER.getX(), CENTER.getY());
                       
        // Translate about dive             
        G2.translate(0, -(pitch * pitchPixel));
        
        // Draw horizon          
        G2.drawImage(horizonImage, (int) ((getWidth() - horizonImage.getWidth()) / 2.0), (int)((getHeight() - horizonImage.getHeight()) / 2.0), null);        
        
        // Draw the scale and angle indicator
        G2.translate(0, (pitch * pitchPixel));        
        G2.drawImage(horizonForegroundImage, (int) (getWidth() * 0.5 - horizonForegroundImage.getWidth() / 2.0), (int) (getWidth() * 0.10747663551401869), null);
        
        G2.setTransform(OLD_TRANSFORM);
        G2.setClip(OLD_CLIP);
                
        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);
                                                
        // Draw combined foreground image
        G2.drawImage(fImage, 0, 0, null);

        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate the coordinate system back to original
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }

    /**
     * Returns the value of the roll axis (0 - 360°)
     * @return the value of the roll axis (0 - 360°)
     */
    public double getRoll()
    {
        return this.roll;
    }

    /**
     * Sets the value of the roll axis (0 - 360°)
     * @param ROLL 
     */
    public void setRoll(final double ROLL)
    {        
        this.roll = ROLL % 360;  
        this.oldRoll = roll;
        fireStateChanged();
        repaint();
    }
        
    public void setRollAnimated(final double ROLL)
    {
        if (isEnabled())
        {
            if (timelineRoll.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD || timelineRoll.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_REVERSE)
            {
                timelineRoll.abort();
            }
            timelineRoll = new org.pushingpixels.trident.Timeline(this);
            timelineRoll.addPropertyToInterpolate("roll", this.oldRoll, ROLL);
            timelineRoll.setEase(new org.pushingpixels.trident.ease.Spline(0.5f));
            timelineRoll.setDuration(800);
            timelineRoll.play();
        }
    }
    
    /**
     * Returns the value of the current pitch
     * @return the value of the current pitch
     */
    public double getPitch()
    {
        return this.pitch;
    }

    /**
     * Sets the value of the current pitch
     * @param PITCH 
     */
    public void setPitch(final double PITCH)
    {
        this.pitch = PITCH % 180; 

        if (pitch > 90)
        {
            pitch = 90 - (pitch - 90);            
            if (!upsidedown)
            {
                setRoll(roll - 180);
            }
            upsidedown = true;            
        }
        else if (pitch < -90)
        {
            pitch = -90 + (-90 - pitch);            
            if (!upsidedown)
            {
                setRoll(roll + 180);                        
            }
            upsidedown = true;
        }                
        else
        {
            upsidedown = false;                        
            this.oldPitch = pitch;
        }                        
        fireStateChanged();
        repaint();
    }
       
    public void setPitchAnimated(final double PITCH)
    {
        if (isEnabled())
        {
            if (timelinePitch.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD || timelinePitch.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_REVERSE)
            {
                timelinePitch.abort();
            }                              
            timelinePitch = new org.pushingpixels.trident.Timeline(this);
            timelinePitch.addPropertyToInterpolate("pitch", this.oldPitch, PITCH);
            timelinePitch.setEase(new org.pushingpixels.trident.ease.Spline(0.5f));
            timelinePitch.setDuration(800);
            timelinePitch.play();                        
        }
    }
    
    /**
     * Returns true if customized colors will be used for visualization
     * @return true if customized colors will be used for visualization
     */
    public boolean isCustomColors()
    {
        return customColors;
    }
    
    /**
     * Enables / disables the usage of custom colors for visualization
     * @param CUSTOM_COLORS 
     */
    public void setCustomColors(final boolean CUSTOM_COLORS)
    {
        customColors = CUSTOM_COLORS;
        if (customColors)
        {
            init(getInnerBounds().width, getInnerBounds().height);
            repaint(getInnerBounds());
        }
    }
    
    /**
     * Returns the custom color that will be used for visualization of the sky
     * @return the custom color that will be used for visualization of the sky
     */
    public java.awt.Color getCustomSkyColor()
    {
        return customSkyColor;
    }
    
    /**
     * Sets the custom color that will be used for visualization of the sky
     * @param CUSTOM_SKY_COLOR 
     */
    public void setCustomSkyColor(final java.awt.Color CUSTOM_SKY_COLOR)
    {
        customSkyColor = CUSTOM_SKY_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the custom color that will be used for visualization of the ground
     * @return the custom color that will be used for visualization of the ground
     */
    public java.awt.Color getCustomGroundColor()
    {
        return customGroundColor;
    }
    
    /**
     * Sets the custom color that will be used for visualization of the ground
     * @param CUSTOM_GROUND_COLOR 
     */
    public void setCustomGroundColor(final java.awt.Color CUSTOM_GROUND_COLOR)
    {
        customGroundColor = CUSTOM_GROUND_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    @Override
    public void setFrameType(final eu.hansolo.steelseries.tools.FrameType FRAME_TYPE)
    {
        super.setFrameType(eu.hansolo.steelseries.tools.FrameType.ROUND);
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
    
    private java.awt.image.BufferedImage create_HORIZON_Image(final int WIDTH)
    {                                        
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
                
        final int HORIZON_HEIGHT = (int) (Math.PI * WIDTH);
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HORIZON_HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);        
                        
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        
        final java.awt.geom.Rectangle2D HORIZON = new java.awt.geom.Rectangle2D.Double(0, 0, IMAGE_WIDTH , HORIZON_HEIGHT);
        final java.awt.geom.Point2D HORIZON_START = new java.awt.geom.Point2D.Double(0, HORIZON.getBounds2D().getMinY() );
        final java.awt.geom.Point2D HORIZON_STOP = new java.awt.geom.Point2D.Double(0, HORIZON.getBounds2D().getMaxY() );
        final float[] HORIZON_FRACTIONS = 
        {
            0.0f,
            0.49999f,
            0.5f,
            1.0f
        };
        final java.awt.Color[] HORIZON_COLORS;         
        if (customColors)
        {
            HORIZON_COLORS = new java.awt.Color[]
            {
                customSkyColor,
                customSkyColor,
                customGroundColor,
                customGroundColor
            };
        }
        else
        {
            HORIZON_COLORS = new java.awt.Color[]
            {           
                new java.awt.Color(127, 213, 240, 255),
                new java.awt.Color(127, 213, 240, 255),            
                new java.awt.Color(60, 68, 57, 255),
                new java.awt.Color(60, 68, 57, 255)
            };
        }
        
        final java.awt.LinearGradientPaint HORIZON_GRADIENT = new java.awt.LinearGradientPaint(HORIZON_START, HORIZON_STOP, HORIZON_FRACTIONS, HORIZON_COLORS);
        G2.setPaint(HORIZON_GRADIENT);        
        G2.fill(HORIZON);
        
        // Draw horizontal lines                
        G2.setColor(UTIL.setBrightness(HORIZON_COLORS[0], 0.5f));
        final java.awt.geom.Line2D LINE = new java.awt.geom.Line2D.Double();
        final double STEPSIZE_Y = HORIZON_HEIGHT / 360.0 * 5.0;
        boolean stepTen = false;
        int step = 0;
        G2.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, (int) (WIDTH * 0.04)));  
        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
        java.awt.font.TextLayout valueLayout;
        final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();                
        for (double y = IMAGE_HEIGHT / 2.0 - STEPSIZE_Y ; y > 0 ; y -= STEPSIZE_Y)
        {            
            if (step <= 80)
            {    
                if (stepTen)
                {                                
                    LINE.setLine((IMAGE_WIDTH - (IMAGE_WIDTH * 0.2)) / 2, y, IMAGE_WIDTH - (IMAGE_WIDTH - (IMAGE_WIDTH * 0.2)) / 2, y);                
                    step += 10;                  
                    valueLayout = new java.awt.font.TextLayout(Integer.toString(step), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());
                    G2.drawString(Integer.toString(step), (float) (LINE.getX1() - VALUE_BOUNDARY.getWidth() - 5), (float) (y + VALUE_BOUNDARY.getHeight() / 2));
                    G2.drawString(Integer.toString(step), (float) (LINE.getX2() + 5), (float) (y + VALUE_BOUNDARY.getHeight() / 2));                
                }
                else
                {
                    LINE.setLine((IMAGE_WIDTH - (IMAGE_WIDTH * 0.1)) / 2, y, IMAGE_WIDTH - (IMAGE_WIDTH - (IMAGE_WIDTH * 0.1)) / 2, y);
                }
                G2.draw(LINE);   
            }
            stepTen ^= true;
        }        
        stepTen = false;
        step = 0;
        G2.setColor(java.awt.Color.WHITE);
        final java.awt.Stroke FORMER_STROKE = G2.getStroke();
        G2.setStroke(new java.awt.BasicStroke(1.5f));
        LINE.setLine(0, IMAGE_HEIGHT / 2.0, IMAGE_WIDTH, IMAGE_HEIGHT / 2.0);
        G2.draw(LINE);
        G2.setStroke(FORMER_STROKE);
        for (double y = IMAGE_HEIGHT / 2.0 + STEPSIZE_Y ; y <= IMAGE_HEIGHT ; y += STEPSIZE_Y)
        {
            if (step >= -80)
            {
                if (stepTen)
                {                
                    LINE.setLine((IMAGE_WIDTH - (IMAGE_WIDTH * 0.2)) / 2, y, IMAGE_WIDTH - (IMAGE_WIDTH - (IMAGE_WIDTH * 0.2)) / 2, y);
                    step -= 10;
                    valueLayout = new java.awt.font.TextLayout(Integer.toString(step), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());
                    G2.drawString(Integer.toString(step), (float) (LINE.getX1() - VALUE_BOUNDARY.getWidth() - 5), (float) (y + VALUE_BOUNDARY.getHeight() / 2));
                    G2.drawString(Integer.toString(step), (float) (LINE.getX2() + 5), (float) (y + VALUE_BOUNDARY.getHeight() / 2));                
                }
                else
                {
                    LINE.setLine((IMAGE_WIDTH - (IMAGE_WIDTH * 0.1)) / 2, y, IMAGE_WIDTH - (IMAGE_WIDTH - (IMAGE_WIDTH * 0.1)) / 2, y);
                }
                G2.draw(LINE);
            }
            stepTen ^= true;
        }
        
        G2.dispose();

        return IMAGE;
    }
            
    private java.awt.image.BufferedImage create_INDICATOR_Image(final int WIDTH, java.awt.image.BufferedImage image)
    {                
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        if (image == null)
        {
            image = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        }
        final java.awt.Graphics2D G2 = image.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);        
        
        final int IMAGE_WIDTH = image.getWidth();
        final int IMAGE_HEIGHT = image.getHeight();
        final java.awt.geom.Point2D LOCAL_CENTER = new java.awt.geom.Point2D.Double(IMAGE_WIDTH / 2.0, IMAGE_HEIGHT / 2.0);

        G2.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, (int) (IMAGE_WIDTH * 0.035)));          
        //final java.awt.geom.Point2D TEXT_POS = new java.awt.geom.Point2D.Double(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.08878504672897196 + IMAGE_WIDTH * 0.035 / 3);
                                
        final java.awt.geom.Line2D SCALE_MARK_SMALL = new java.awt.geom.Line2D.Double(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.08878504672897196, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.0937850467);                
        final java.awt.geom.Line2D SCALE_MARK = new java.awt.geom.Line2D.Double(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.08878504672897196, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1037850467);                
        final java.awt.geom.Line2D SCALE_MARK_BIG = new java.awt.geom.Line2D.Double(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.08878504672897196, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.113);
        final java.awt.Stroke SMALL_STROKE = new java.awt.BasicStroke(0.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);
        final java.awt.Stroke STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);
        final java.awt.Stroke BIG_STROKE = new java.awt.BasicStroke(2.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);
        
        final int STEP = 5;
        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();
        G2.rotate(-Math.PI / 2, LOCAL_CENTER.getX(), LOCAL_CENTER.getY());
        for (int angle = -90 ; angle <= 90 ; angle += STEP)
        {            
            if (angle % 45 == 0 || angle == 0)
            {
                //G2.fill(UTIL.rotateTextAroundCenter(G2, Integer.toString(angle), (int) TEXT_POS.getX(), (int) TEXT_POS.getY(), 0));
                G2.setColor(getPointerColor().MEDIUM);
                G2.setStroke(BIG_STROKE);
                G2.draw(SCALE_MARK_BIG);
            }            
            else if (angle % 15 == 0)
            {
                G2.setColor(java.awt.Color.WHITE);
                G2.setStroke(STROKE);
                G2.draw(SCALE_MARK);
            }
            else
            {
                G2.setColor(java.awt.Color.WHITE);
                G2.setStroke(SMALL_STROKE);
                G2.draw(SCALE_MARK_SMALL);
            }
                                    
            G2.rotate(Math.toRadians(STEP), LOCAL_CENTER.getX(), LOCAL_CENTER.getY());
        }
        
        G2.setTransform(OLD_TRANSFORM);
        final java.awt.geom.GeneralPath CENTER_PLANE = new java.awt.geom.GeneralPath();
        CENTER_PLANE.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        CENTER_PLANE.moveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.514018691588785, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.5233644859813084, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5233644859813084);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.5233644859813084, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.514018691588785, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.5);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.48598130841121495, IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.4766355140186916, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4766355140186916);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.4766355140186916, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.48598130841121495, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.5);
        CENTER_PLANE.closePath();
        CENTER_PLANE.moveTo(IMAGE_WIDTH * 0.4158878504672897, IMAGE_HEIGHT * 0.5046728971962616);
        CENTER_PLANE.lineTo(IMAGE_WIDTH * 0.4158878504672897, IMAGE_HEIGHT * 0.4953271028037383);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.4158878504672897, IMAGE_HEIGHT * 0.4953271028037383, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.4953271028037383, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.4953271028037383);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.4672897196261682);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.4672897196261682, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.4158878504672897);
        CENTER_PLANE.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.4158878504672897);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.4672897196261682, IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.4672897196261682);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.4953271028037383);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.4953271028037383, IMAGE_WIDTH * 0.5841121495327103, IMAGE_HEIGHT * 0.4953271028037383, IMAGE_WIDTH * 0.5841121495327103, IMAGE_HEIGHT * 0.4953271028037383);
        CENTER_PLANE.lineTo(IMAGE_WIDTH * 0.5841121495327103, IMAGE_HEIGHT * 0.5046728971962616);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.5841121495327103, IMAGE_HEIGHT * 0.5046728971962616, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5046728971962616, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5046728971962616);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5046728971962616);
        CENTER_PLANE.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5046728971962616, IMAGE_WIDTH * 0.4158878504672897, IMAGE_HEIGHT * 0.5046728971962616, IMAGE_WIDTH * 0.4158878504672897, IMAGE_HEIGHT * 0.5046728971962616);
        CENTER_PLANE.closePath();                
        G2.setPaint(getPointerColor().LIGHT);
        G2.fill(CENTER_PLANE);        
        
        G2.dispose();

        return image;
    }
                
    private java.awt.image.BufferedImage create_HORIZON_FOREGROUND_Image(final int WIDTH)
    {                
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage((int) (WIDTH * 0.0373831776), (int) (WIDTH * 0.0560747664), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
                                                      
        // Draw angle indicator        
        final java.awt.geom.GeneralPath TRIANGLE = new java.awt.geom.GeneralPath();
        TRIANGLE.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        TRIANGLE.moveTo(IMAGE_WIDTH * 0.5, 0);
        TRIANGLE.lineTo(0, IMAGE_HEIGHT);
        TRIANGLE.lineTo(IMAGE_WIDTH, IMAGE_HEIGHT);        
        TRIANGLE.closePath();     
        G2.setColor(getPointerColor().LIGHT);
        G2.fill(TRIANGLE);
        G2.setColor(getPointerColor().MEDIUM);
        G2.draw(TRIANGLE);
        
        G2.dispose();

        return IMAGE;
    }
    
    @Override
    public String toString()
    {
        return "Horizon";
    }
}
