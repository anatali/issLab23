package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public final class RadialCounter extends AbstractRadial
{
    private boolean rotateTickmarks;
    private double oldValue;    
    private double rotationAngle;
    private final java.awt.geom.Point2D CENTER;                
    private java.awt.image.BufferedImage frameImage;
    private java.awt.image.BufferedImage backgroundImage;
    private java.awt.image.BufferedImage lcdImage;
    private java.awt.image.BufferedImage tickmarksImage;
    private java.awt.image.BufferedImage overlayImage;
    private java.awt.image.BufferedImage pointerImage;
    private java.awt.image.BufferedImage foregroundImage;
    private java.awt.image.BufferedImage disabledImage;
    private org.pushingpixels.trident.Timeline timeline;
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();    
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private double unitStringWidth;
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();

    
    public RadialCounter()
    {
        super();
        rotateTickmarks = true;
        oldValue = 0;        
        rotationAngle = 0;
        CENTER = new java.awt.geom.Point2D.Double();        
        timeline = new org.pushingpixels.trident.Timeline(this);               
        init(getInnerBounds().width, getInnerBounds().height);
        setLcdVisible(true);
    }

    @Override
    public final AbstractGauge init(int WIDTH, int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        if (isDigitalFont())
        {
            setLcdValueFont(getModel().getDigitalBaseFont().deriveFont(0.7f * WIDTH * 0.10f));            
        }
        else
        {
            setLcdValueFont(getModel().getStandardBaseFont().deriveFont(0.625f * WIDTH * 0.10f));       
        }

        if (isCustomLcdUnitFontEnabled())
        {
            setLcdUnitFont(getCustomLcdUnitFont().deriveFont(0.25f * WIDTH * 0.10f));
        }
        else
        {
            setLcdUnitFont(getModel().getStandardBaseFont().deriveFont(0.25f * WIDTH * 0.10f));
        }
        
        setLcdInfoFont(getModel().getStandardInfoFont().deriveFont(0.15f * WIDTH * 0.10f));
        
        if (frameImage != null)
        {
            frameImage.flush();
        }
        frameImage = create_FRAME_Image(WIDTH);

        if (backgroundImage != null)
        {
            backgroundImage.flush();
        }
        backgroundImage = create_BACKGROUND_Image(WIDTH);

        if (lcdImage != null)
        {
            lcdImage.flush();
        }                 
        lcdImage = create_LCD_Image((int) (WIDTH * 0.32), (int) (WIDTH * 0.10), getLcdColor(), getCustomLcdBackground());                
        LCD.setRect(((getGaugeBounds().width - lcdImage.getWidth()) / 2.0), ((getGaugeBounds().height - lcdImage.getHeight()) / 2.0), WIDTH * 0.32, WIDTH * 0.10);

        if (tickmarksImage != null)
        {
            tickmarksImage.flush();
        }
        tickmarksImage = create_TICKMARKS_Image(WIDTH);

        if (overlayImage != null)
        {
            overlayImage.flush();
        }
        overlayImage = create_OVERLAY_Image(WIDTH);
        
        if (pointerImage != null)
        {
            pointerImage.flush();
        }
        pointerImage = create_POINTER_Image(WIDTH);

        if (foregroundImage != null)
        {
            foregroundImage.flush();
        }
        foregroundImage = create_FOREGROUND_Image(WIDTH, false, getForegroundType());

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

        CENTER.setLocation(getGaugeBounds().getCenterX(), getGaugeBounds().getCenterY());

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
                
        // Draw the tickmarks
        if (rotateTickmarks)
        {
            G2.rotate(-rotationAngle, CENTER.getX(), CENTER.getY());
            G2.drawImage(tickmarksImage, 0, 0, null);
            G2.setTransform(OLD_TRANSFORM);
        }
        else
        {
            G2.drawImage(tickmarksImage, 0, 0, null);
        }

        // Draw pointer
        if (!rotateTickmarks)
        {
            G2.rotate(rotationAngle, CENTER.getX(), CENTER.getY());
            G2.drawImage(pointerImage, 0, 0, null);
            G2.setTransform(OLD_TRANSFORM);
        }
        else
        {
            G2.drawImage(pointerImage, 0, 0, null);
        }

        // Draw the overlay
        G2.drawImage(overlayImage, 0, 0, null);
        
        // Draw LCD display
        if (isLcdVisible())
        {
            //G2.drawImage(lcdImage, (int) ((getGaugeBounds().width - lcdImage.getWidth()) / 2.0), (int) (getGaugeBounds().height * 0.55), null);
            G2.drawImage(lcdImage, (int) (LCD.getX()), (int) (LCD.getY()), null);
            
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
                    G2.drawString(Integer.toHexString((int) getLcdValue()).toUpperCase(), (int) (LCD.getX() + (LCD.getWidth() - unitStringWidth - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + lcdImage.getHeight() * 0.76));
                    break;
                    
                case OCT:
                    valueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(Integer.toOctalString((int) getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - unitStringWidth - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + lcdImage.getHeight() * 0.76));
                    break;
                    
                case DEC:
                    
                default:
                    valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
                    VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
                    G2.drawString(formatLcdValue(getLcdValue()), (int) (LCD.getX() + (LCD.getWidth() - unitStringWidth - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09), (int) (LCD.getY() + lcdImage.getHeight() * 0.76));
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
                
        // Draw foreground
        if (isForegroundVisible())
        {
            G2.drawImage(foregroundImage, 0, 0, null);
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
    public double getValue()
    {
        return this.oldValue;
    }
    
    @Override
    public void setValue(final double VALUE)
    {
        rotationAngle = (2.0 * Math.PI / 10.0) * (VALUE % 10);
        fireStateChanged();
        this.oldValue = VALUE;        
        if (isValueCoupled())
        {
            setLcdValue(VALUE);
        }
        repaint(getInnerBounds());        
    }

    @Override
    public void setValueAnimated(final double VALUE)
    {                
        if (timeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_FORWARD || timeline.getState() == org.pushingpixels.trident.Timeline.TimelineState.PLAYING_REVERSE)
        {
            timeline.abort();
        }
        timeline = new org.pushingpixels.trident.Timeline(this);
        timeline.addPropertyToInterpolate("value", this.oldValue, VALUE);
        timeline.setEase(new org.pushingpixels.trident.ease.Spline(0.5f));

        timeline.setDuration(1500);
        timeline.play();
    }    
    
    @Override
    public double getMinValue()
    {
        return 0;
    }

    @Override
    public double getMaxValue()
    {
        return 10;
    }

    public boolean isRotateTickmarks()
    {
        return this.rotateTickmarks;
    }

    public void setRotateTickmarks(final boolean ROTATE_TICKMARKS)
    {
        this.rotateTickmarks = ROTATE_TICKMARKS;
        setValue(0);
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

    private java.awt.image.BufferedImage create_TICKMARKS_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        final int IMAGE_WIDTH = IMAGE.getWidth();

        final java.awt.geom.AffineTransform OLD_TRANSFORM = G2.getTransform();

        final java.awt.BasicStroke THIN_STROKE = new java.awt.BasicStroke(0.01f * IMAGE_WIDTH, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_BEVEL);
        final java.awt.Font FONT = new java.awt.Font("Verdana", 0, (int) (0.0747663551f * IMAGE_WIDTH));
        final float TEXT_DISTANCE = 0.1f * IMAGE_WIDTH;
        final float MIN_LENGTH = 0.02f * IMAGE_WIDTH;
        final float MED_LENGTH = 0.04f * IMAGE_WIDTH;

        // Create the watch itself
        final float RADIUS = IMAGE_WIDTH * 0.37f;
        CENTER.setLocation(IMAGE_WIDTH / 2.0f, IMAGE_WIDTH / 2.0f);

        // Draw ticks
        java.awt.geom.Point2D innerPoint = new java.awt.geom.Point2D.Double();
        java.awt.geom.Point2D outerPoint = new java.awt.geom.Point2D.Double();
        java.awt.geom.Point2D textPoint = new java.awt.geom.Point2D.Double();
        java.awt.geom.Line2D tick;
        int tickCounterFull = 0;
        int tickCounterHalf = 0;
        int counter = 0;

        double sinValue = 0;
        double cosValue = 0;

        final double STEP = (2.0 * Math.PI) / (20.0);
        
        if (isTickmarkColorFromThemeEnabled())
        {
            G2.setColor(getBackgroundColor().LABEL_COLOR);
        }
        else
        {
            G2.setColor(getTickmarkColor());
        }

        for (double alpha = 2.0 * Math.PI; alpha >= 0; alpha -= STEP)
        {
            G2.setStroke(THIN_STROKE);
            sinValue = Math.sin(alpha);
            cosValue = Math.cos(alpha);
           
            if (tickCounterHalf == 1)
            {
                G2.setStroke(THIN_STROKE);
                innerPoint.setLocation(CENTER.getX() + (RADIUS - MIN_LENGTH) * sinValue, CENTER.getY() + (RADIUS - MIN_LENGTH) * cosValue);
                outerPoint.setLocation(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
                // Draw ticks
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);

                tickCounterHalf = 0;
            }

            // Different tickmark every 15 units
            if (tickCounterFull == 2)
            {
                G2.setStroke(THIN_STROKE);
                innerPoint.setLocation(CENTER.getX() + (RADIUS - MED_LENGTH) * sinValue, CENTER.getY() + (RADIUS - MED_LENGTH) * cosValue);
                outerPoint.setLocation(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);

                // Draw ticks
                tick = new java.awt.geom.Line2D.Double(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
                G2.draw(tick);

                tickCounterFull = 0;
            }

            // Draw text
            G2.setFont(FONT);
            textPoint.setLocation(CENTER.getX() + (RADIUS - TEXT_DISTANCE) * sinValue, CENTER.getY() + (RADIUS - TEXT_DISTANCE) * cosValue);
            if (counter != 20 && counter % 2 == 0)
            {
                G2.rotate(Math.toRadians(0), CENTER.getX(), CENTER.getY());
                G2.fill(UTIL.rotateTextAroundCenter(G2, String.valueOf(counter/2), (int) textPoint.getX(), (int) textPoint.getY(), Math.toDegrees(2*Math.PI - alpha)));
            }

            G2.setTransform(OLD_TRANSFORM);

            tickCounterHalf++;
            tickCounterFull++;

            counter ++;
        }

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_OVERLAY_Image(final int WIDTH)
    {                
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.Ellipse2D OVERLAY_FRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.31308412551879883, IMAGE_HEIGHT * 0.31308412551879883, IMAGE_WIDTH * 0.37383174896240234, IMAGE_HEIGHT * 0.37383174896240234);
        final java.awt.geom.Point2D OVERLAY_FRAME_START = new java.awt.geom.Point2D.Double(0, OVERLAY_FRAME.getBounds2D().getMinY() );
        final java.awt.geom.Point2D OVERLAY_FRAME_STOP = new java.awt.geom.Point2D.Double(0, OVERLAY_FRAME.getBounds2D().getMaxY() );
        final float[] OVERLAY_FRAME_FRACTIONS = 
        {
            0.0f,
            0.46f,
            1.0f
        };
        final java.awt.Color[] OVERLAY_FRAME_COLORS = 
        {
            new java.awt.Color(180, 180, 180, 255),
            new java.awt.Color(63, 63, 63, 255),
            new java.awt.Color(40, 40, 40, 255)
        };
        final java.awt.LinearGradientPaint OVERLAY_FRAME_GRADIENT = new java.awt.LinearGradientPaint(OVERLAY_FRAME_START, OVERLAY_FRAME_STOP, OVERLAY_FRAME_FRACTIONS, OVERLAY_FRAME_COLORS);
        G2.setPaint(OVERLAY_FRAME_GRADIENT);
        G2.fill(OVERLAY_FRAME);

        final java.awt.geom.Ellipse2D GAUGE_BACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);        
        final java.awt.geom.Ellipse2D OVERLAY_MAIN = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.3177570104598999, IMAGE_HEIGHT * 0.3177570104598999, IMAGE_WIDTH * 0.3644859790802002, IMAGE_HEIGHT * 0.3644859790802002);
        final java.awt.geom.Point2D OVERLAY_MAIN_START = new java.awt.geom.Point2D.Double(0, GAUGE_BACKGROUND.getBounds2D().getMinY() );
        final java.awt.geom.Point2D OVERLAY_MAIN_STOP = new java.awt.geom.Point2D.Double(0, GAUGE_BACKGROUND.getBounds2D().getMaxY() );
        final float[] OVERLAY_MAIN_FRACTIONS = 
        {
            0.0f,
            0.35f,
            1.0f
        };
        final java.awt.Color[] OVERLAY_MAIN_COLORS = 
        {
            getBackgroundColor().GRADIENT_START_COLOR,
            getBackgroundColor().GRADIENT_FRACTION_COLOR,
            getBackgroundColor().GRADIENT_STOP_COLOR
        };
        final java.awt.LinearGradientPaint OVERLAY_MAIN_GRADIENT = new java.awt.LinearGradientPaint(OVERLAY_MAIN_START, OVERLAY_MAIN_STOP, OVERLAY_MAIN_FRACTIONS, OVERLAY_MAIN_COLORS);
        G2.setPaint(OVERLAY_MAIN_GRADIENT);
        G2.fill(OVERLAY_MAIN);

        final java.awt.geom.Ellipse2D OVERLAY_EFFECT = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);
        final java.awt.geom.Point2D OVERLAY_EFFECT_CENTER = new java.awt.geom.Point2D.Double( (0.5 * IMAGE_WIDTH), (0.5 * IMAGE_HEIGHT) );
        final float[] OVERLAY_EFFECT_FRACTIONS = 
        {
            0.0f,
            0.41f,            
            0.705f,            
            1.0f
        };
        final java.awt.Color[] OVERLAY_EFFECT_COLORS = 
        {
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0),
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0),            
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 30),            
            UTIL.setAlpha(getBackgroundColor().LABEL_COLOR, 0)            
        };
        final java.awt.RadialGradientPaint OVERLAY_EFFECT_GRADIENT = new java.awt.RadialGradientPaint(OVERLAY_EFFECT_CENTER, (float)(0.4158878326 * IMAGE_WIDTH), OVERLAY_EFFECT_FRACTIONS, OVERLAY_EFFECT_COLORS);
        G2.setPaint(OVERLAY_EFFECT_GRADIENT);
        G2.fill(OVERLAY_EFFECT);
                
        G2.dispose();

        return IMAGE;
    }    
        
    @Override            
    protected java.awt.image.BufferedImage create_POINTER_Image(final int WIDTH)
    {                
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath POINTERBACK = new java.awt.geom.GeneralPath();
        POINTERBACK.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        POINTERBACK.moveTo(IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.9112149532710281);
        POINTERBACK.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.8271028037383178);
        POINTERBACK.lineTo(IMAGE_WIDTH * 0.5420560747663551, IMAGE_HEIGHT * 0.9112149532710281);
        POINTERBACK.lineTo(IMAGE_WIDTH * 0.45794392523364486, IMAGE_HEIGHT * 0.9112149532710281);
        POINTERBACK.closePath();
        
        final java.awt.geom.GeneralPath POINTERFRONT = new java.awt.geom.GeneralPath();
        POINTERFRONT.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        POINTERFRONT.moveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.9018691588785047);
        POINTERFRONT.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.8457943925233645);
        POINTERFRONT.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.9018691588785047);
        POINTERFRONT.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.9018691588785047);
        POINTERFRONT.closePath();
        
        java.awt.geom.Area POINTER = new java.awt.geom.Area(POINTERBACK);
        POINTER.subtract(new java.awt.geom.Area(POINTERFRONT));
        
        final java.awt.geom.Point2D POINTER_START = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMinY() );
        final java.awt.geom.Point2D POINTER_STOP = new java.awt.geom.Point2D.Double(0, POINTER.getBounds2D().getMaxY() );
        final float[] POINTER_FRACTIONS =
        {
            0.0f,
            0.3f,
            0.59f,
            1.0f
        };
        final java.awt.Color[] POINTER_COLORS;
        if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
        {
            POINTER_COLORS = new java.awt.Color[]
            {
                getPointerColor().DARK,
                getPointerColor().MEDIUM,
                getPointerColor().MEDIUM,
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
        final java.awt.LinearGradientPaint POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
        
        // PointerShadow
        G2.translate(1 ,0);
        G2.setColor(new java.awt.Color(0.0f, 0.0f, 0.0f, 0.55f));
        G2.fill(POINTER);
        G2.translate(-1, 0);
                
        // Pointer
        G2.setPaint(POINTER_GRADIENT);
        G2.fill(POINTER);

        if (getPointerColor() != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
        {
            G2.setColor(getPointerColor().LIGHT);
        }
        else
        {
            G2.setColor(getCustomPointerColorObject().LIGHT);
        }
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(POINTER);
        
        G2.dispose();

        return IMAGE;
    }
   
    @Override
    public String toString()
    {
        return "RadialCounter";
    }
}
