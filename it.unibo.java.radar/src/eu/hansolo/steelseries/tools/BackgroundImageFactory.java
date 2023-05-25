package eu.hansolo.steelseries.tools;

/**
 *
 * @author hansolo
 */
public enum BackgroundImageFactory
{
    INSTANCE;
    
    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    // Variables for caching    
    private int radWidth = 0;    
    private eu.hansolo.steelseries.tools.BackgroundColor radBackgroundColor = eu.hansolo.steelseries.tools.BackgroundColor.DARK_GRAY;    
    private java.awt.Paint radCustomBackground = null;    
    private java.awt.image.BufferedImage radBackgroundImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private int linWidth = 0;
    private int linHeight = 0;    
    private eu.hansolo.steelseries.tools.BackgroundColor linBackgroundColor = eu.hansolo.steelseries.tools.BackgroundColor.DARK_GRAY;        
    private java.awt.Paint linCustomBackground = null;
    private java.awt.image.BufferedImage linBackgroundImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);        
    public final java.awt.image.BufferedImage STAINLESS_GRINDED_TEXTURE = UTIL.create_STAINLESS_STEEL_PLATE_Texture(100);
    public final java.awt.image.BufferedImage CARBON_FIBRE_TEXTURE = UTIL.create_CARBON_Texture(12); 
    //public final java.awt.image.BufferedImage PUNCHED_SHEET_TEXTURE = UTIL.create_PUNCHED_SHEET_Image(15);
    public final java.awt.image.BufferedImage PUNCHED_SHEET_TEXTURE = UTIL.create_PUNCHED_SHEET_Image(12);

    /**
     * Creates the background image for a radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param BACKGROUND_COLOR
     * @return a buffered image that contains the background image of a radial gauge
     */
    public java.awt.image.BufferedImage createRadialBackground(final int WIDTH, final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR)
    {
        return createRadialBackground(WIDTH, BACKGROUND_COLOR, null);
    }

    /**
     * Creates the background image for a radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param BACKGROUND_COLOR
     * @param CUSTOM_BACKGROUND
     * @return a buffered image that contains the background image of a radial gauge
     */
    public java.awt.image.BufferedImage createRadialBackground(final int WIDTH, final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR, final java.awt.Paint CUSTOM_BACKGROUND)
    {        
        return createRadialBackground(WIDTH, BACKGROUND_COLOR, CUSTOM_BACKGROUND, null);
    }

    /**
     * Creates the background image for a radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating.
     * a new image.
     * If an image is passed to the method, it will paint to the image and
     * return this image. This will reduce the memory consumption.
     * @param WIDTH
     * @param BACKGROUND_COLOR
     * @param CUSTOM_BACKGROUND
     * @param BACKGROUND_IMAGE
     * @return a buffered image that contains the background image of a radial gauge
     */
    public java.awt.image.BufferedImage createRadialBackground(final int WIDTH, final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR, final java.awt.Paint CUSTOM_BACKGROUND, final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {        
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        // Take image from cache instead of creating a new one if parameters are the same as last time
        if (radWidth == WIDTH && radBackgroundColor == BACKGROUND_COLOR && radCustomBackground.equals(CUSTOM_BACKGROUND))
        {
            if (BACKGROUND_IMAGE != null)
            {
                final java.awt.Graphics2D G2 = BACKGROUND_IMAGE.createGraphics();
                G2.drawImage(radBackgroundImage, 0, 0, null);
                G2.dispose();                
            }
            return radBackgroundImage;
        }

        radBackgroundImage.flush();
        radBackgroundImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);            
        
        final java.awt.Graphics2D G2 = radBackgroundImage.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = WIDTH;
        final int IMAGE_HEIGHT = WIDTH;

        // Boolean that defines if a overlay gradient will be painted
        boolean fadeInOut = false;
        
        // Background of gauge
        final java.awt.geom.Ellipse2D GAUGE_BACKGROUND = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);
        final java.awt.geom.Point2D GAUGE_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, GAUGE_BACKGROUND.getBounds2D().getMinY());
        final java.awt.geom.Point2D GAUGE_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, GAUGE_BACKGROUND.getBounds2D().getMaxY());
        if (GAUGE_BACKGROUND_START.equals(GAUGE_BACKGROUND_STOP))
        {
            GAUGE_BACKGROUND_STOP.setLocation(0.0, GAUGE_BACKGROUND_START.getY() + 1);
        } 
        
        final float[] GAUGE_BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.40f,
            1.0f
        };        

        // Set custom background paint if selected
        if (CUSTOM_BACKGROUND != null && BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {
            G2.setPaint(CUSTOM_BACKGROUND);
        }
        else
        {
            final java.awt.Color[] GAUGE_BACKGROUND_COLORS =
            {
                BACKGROUND_COLOR.GRADIENT_START_COLOR,
                BACKGROUND_COLOR.GRADIENT_FRACTION_COLOR,
                BACKGROUND_COLOR.GRADIENT_STOP_COLOR
            };

            final java.awt.Paint GAUGE_BACKGROUND_GRADIENT;
            if (BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.BRUSHED_METAL)
            {
                GAUGE_BACKGROUND_GRADIENT = new java.awt.TexturePaint(UTIL.createBrushMetalTexture(null, GAUGE_BACKGROUND.getBounds().width, GAUGE_BACKGROUND.getBounds().height), GAUGE_BACKGROUND.getBounds());
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.STAINLESS)
            {
                final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double(GAUGE_BACKGROUND.getCenterX(), GAUGE_BACKGROUND.getCenterY());
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
                GAUGE_BACKGROUND_GRADIENT = new ConicalGradientPaint(false, CENTER, -0.45f, STAINLESS_FRACTIONS, STAINLESS_COLORS);
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.STAINLESS_GRINDED)
            {                
                GAUGE_BACKGROUND_GRADIENT = new java.awt.TexturePaint(STAINLESS_GRINDED_TEXTURE, new java.awt.Rectangle(0, 0, 100, 100));
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.CARBON)
            {
                GAUGE_BACKGROUND_GRADIENT = new java.awt.TexturePaint(CARBON_FIBRE_TEXTURE, new java.awt.Rectangle(0, 0, 12, 12));
                fadeInOut = true;
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.PUNCHED_SHEET)
            {
                GAUGE_BACKGROUND_GRADIENT = new java.awt.TexturePaint(PUNCHED_SHEET_TEXTURE, new java.awt.Rectangle(0, 0, 12, 12));
                fadeInOut = true;
            }
            else
            {
                GAUGE_BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(GAUGE_BACKGROUND_START, GAUGE_BACKGROUND_STOP, GAUGE_BACKGROUND_FRACTIONS, GAUGE_BACKGROUND_COLORS);
            }
            G2.setPaint(GAUGE_BACKGROUND_GRADIENT);
        }
        G2.fill(GAUGE_BACKGROUND);
        
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
            final java.awt.LinearGradientPaint SHADOW_OVERLAY_GRADIENT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Double(GAUGE_BACKGROUND.getMinX(), 0), new java.awt.geom.Point2D.Double(GAUGE_BACKGROUND.getMaxX(), 0), SHADOW_OVERLAY_FRACTIONS, SHADOW_OVERLAY_COLORS);
            G2.setPaint(SHADOW_OVERLAY_GRADIENT);
            G2.fill(GAUGE_BACKGROUND);
        }        
        
        final java.awt.geom.Ellipse2D GAUGE_INNERSHADOW = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031);
        final java.awt.geom.Point2D GAUGE_INNERSHADOW_CENTER = new java.awt.geom.Point2D.Double( (0.5 * IMAGE_WIDTH), (0.5 * IMAGE_HEIGHT) );
        final float[] GAUGE_INNERSHADOW_FRACTIONS = 
        {
            0.0f,
            0.7f,
            0.71f,
            0.86f,
            0.92f,
            0.97f,
            1.0f
        };
        final java.awt.Color[] GAUGE_INNERSHADOW_COLORS = 
        {
            new java.awt.Color(0f, 0f, 0f, 0f),
            new java.awt.Color(0f, 0f, 0f, 0f),
            new java.awt.Color(0f, 0f, 0f, 0f),
            new java.awt.Color(0f, 0f, 0f, 0.03f),
            new java.awt.Color(0f, 0f, 0f, 0.07f),
            new java.awt.Color(0f, 0f, 0f, 0.15f),
            new java.awt.Color(0f, 0f, 0f, 0.3f)
        };
        final java.awt.RadialGradientPaint GAUGE_INNERSHADOW_GRADIENT = new java.awt.RadialGradientPaint(GAUGE_INNERSHADOW_CENTER, (float)(0.4158878504672897 * IMAGE_WIDTH), GAUGE_INNERSHADOW_FRACTIONS, GAUGE_INNERSHADOW_COLORS);
        G2.setPaint(GAUGE_INNERSHADOW_GRADIENT);
        G2.fill(GAUGE_INNERSHADOW);
        if (BACKGROUND_COLOR != eu.hansolo.steelseries.tools.BackgroundColor.TRANSPARENT)
        {
            G2.fill(GAUGE_INNERSHADOW);
        }

        G2.dispose();
        
        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
            G.drawImage(radBackgroundImage, 0, 0, null);
            G.dispose();        
        }
        
        // Cache current values
        radWidth = WIDTH;
        radBackgroundColor = BACKGROUND_COLOR;        
        radCustomBackground = CUSTOM_BACKGROUND;

        return radBackgroundImage;
    }
    
    /**
     * Creates the background image for a linear gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param HEIGHT
     * @param BACKGROUND_COLOR
     * @return a buffered image that contains the background image of a linear gauge
     */
    public java.awt.image.BufferedImage createLinearBackground(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR)
    {
        return createLinearBackground(WIDTH, HEIGHT, BACKGROUND_COLOR, null);
    }

    /**
     * Creates the background image for a linear gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param HEIGHT
     * @param BACKGROUND_COLOR
     * @param CUSTOM_BACKGROUND
     * @return a buffered image that contains the background image of a linear gauge
     */
    public java.awt.image.BufferedImage createLinearBackground(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR, final java.awt.Paint CUSTOM_BACKGROUND)
    {        
        return createLinearBackground(WIDTH, HEIGHT, BACKGROUND_COLOR, CUSTOM_BACKGROUND, null);
    }

    /**
     * Creates the background image for a linear gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * If an image is passed to the method, it will paint to the image and
     * return this image. This will reduce the memory consumption.
     * @param WIDTH
     * @param HEIGHT
     * @param BACKGROUND_COLOR
     * @param CUSTOM_BACKGROUND
     * @param BACKGROUND_IMAGE
     * @return a buffered image that contains the background image of a linear gauge
     */
    public java.awt.image.BufferedImage createLinearBackground(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.BackgroundColor BACKGROUND_COLOR, final java.awt.Paint CUSTOM_BACKGROUND, final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {                
        if (WIDTH <= 32 || HEIGHT <= 32)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        // Take image from cache instead of creating a new one if parameters are the same as last time
        if (linWidth == WIDTH && linHeight == HEIGHT && linBackgroundColor == BACKGROUND_COLOR && linCustomBackground.equals(CUSTOM_BACKGROUND))
        {
            if (BACKGROUND_IMAGE != null)
            {
                final java.awt.Graphics2D G2 = BACKGROUND_IMAGE.createGraphics();
                G2.drawImage(linBackgroundImage, 0, 0, null);
                G2.dispose();
            }
            return linBackgroundImage;
        }

        linBackgroundImage.flush();
        linBackgroundImage = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);            
        
        final java.awt.Graphics2D G2 = linBackgroundImage.createGraphics();        
        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = WIDTH;
        final int IMAGE_HEIGHT = HEIGHT;

        // Boolean that defines if a gradient overlay will be painted
        boolean fadeInOut = false;
        
        final double OUTER_FRAME_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            OUTER_FRAME_CORNER_RADIUS = IMAGE_HEIGHT * 0.05;
        }
        else
        {
            OUTER_FRAME_CORNER_RADIUS = IMAGE_WIDTH * 0.05;
        }
        final java.awt.geom.RoundRectangle2D OUTER_FRAME = new java.awt.geom.RoundRectangle2D.Double(0.0, 0.0, IMAGE_WIDTH, IMAGE_HEIGHT, OUTER_FRAME_CORNER_RADIUS, OUTER_FRAME_CORNER_RADIUS);
        final double FRAME_MAIN_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            FRAME_MAIN_CORNER_RADIUS = OUTER_FRAME_CORNER_RADIUS - ((OUTER_FRAME.getHeight() - IMAGE_HEIGHT - 2) / 2.0);
        }
        else
        {
            FRAME_MAIN_CORNER_RADIUS = OUTER_FRAME_CORNER_RADIUS - ((OUTER_FRAME.getWidth() - IMAGE_WIDTH - 2) / 2.0);
        }
        final java.awt.geom.RoundRectangle2D FRAME_MAIN = new java.awt.geom.RoundRectangle2D.Double(1.0, 1.0, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, FRAME_MAIN_CORNER_RADIUS, FRAME_MAIN_CORNER_RADIUS);

        final double INNER_FRAME_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            INNER_FRAME_CORNER_RADIUS = IMAGE_HEIGHT * 0.02857143;
        }
        else
        {
            INNER_FRAME_CORNER_RADIUS = IMAGE_WIDTH * 0.02857143;
        }

        final java.awt.geom.RoundRectangle2D INNER_FRAME = new java.awt.geom.RoundRectangle2D.Double(FRAME_MAIN.getX() + 16, FRAME_MAIN.getY() + 16, FRAME_MAIN.getWidth() - 32, FRAME_MAIN.getHeight() - 32, INNER_FRAME_CORNER_RADIUS, INNER_FRAME_CORNER_RADIUS);

        final double BACKGROUND_CORNER_RADIUS = INNER_FRAME_CORNER_RADIUS - 1;
        
        final java.awt.geom.RoundRectangle2D BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(INNER_FRAME.getX() + 1, INNER_FRAME.getY() + 1, INNER_FRAME.getWidth() - 2, INNER_FRAME.getHeight() - 2, BACKGROUND_CORNER_RADIUS, BACKGROUND_CORNER_RADIUS);
        final java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY());
        final java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY());
        if (BACKGROUND_START.equals(BACKGROUND_STOP))
        {
            BACKGROUND_STOP.setLocation(0.0, BACKGROUND_START.getY() + 1);
        } 
        
        final float[] BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.4f,
            1.0f
        };        

        java.awt.Paint gaugeBackgroundGradient = null;
        
        // Set custom background paint if selected
        if (CUSTOM_BACKGROUND != null && BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {
            G2.setPaint(CUSTOM_BACKGROUND);
        }
        else
        {
            final java.awt.Color[] BACKGROUND_COLORS =
            {
                BACKGROUND_COLOR.GRADIENT_START_COLOR,
                BACKGROUND_COLOR.GRADIENT_FRACTION_COLOR,
                BACKGROUND_COLOR.GRADIENT_STOP_COLOR
            };
            
            if (BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.BRUSHED_METAL)
            {
                gaugeBackgroundGradient = new java.awt.TexturePaint(UTIL.createBrushMetalTexture(null, BACKGROUND.getBounds().width, BACKGROUND.getBounds().height), BACKGROUND.getBounds());
            }
            else if (BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.STAINLESS)
            {
                gaugeBackgroundGradient = new java.awt.TexturePaint(UTIL.createBrushMetalTexture(new java.awt.Color(0x6E6E70), BACKGROUND.getBounds().width, BACKGROUND.getBounds().height, 5, 0.03f, true, 0.5f), BACKGROUND.getBounds());
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.STAINLESS_GRINDED)
            {                
                gaugeBackgroundGradient = new java.awt.TexturePaint(STAINLESS_GRINDED_TEXTURE, new java.awt.Rectangle(0, 0, 100, 100));
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.CARBON)
            {
                gaugeBackgroundGradient = new java.awt.TexturePaint(CARBON_FIBRE_TEXTURE, new java.awt.Rectangle(0, 0, 12, 12));
                fadeInOut = true;
            }
            else if(BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.PUNCHED_SHEET)
            {
                gaugeBackgroundGradient = new java.awt.TexturePaint(PUNCHED_SHEET_TEXTURE, new java.awt.Rectangle(0, 0, 12, 12));
                fadeInOut = true;
            }
            else
            {
                gaugeBackgroundGradient = new java.awt.LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);                
            }
            G2.setPaint(gaugeBackgroundGradient);
        }
        G2.fill(BACKGROUND);
                
        // Create inner shadow on background shape
        final java.awt.image.BufferedImage CLP;
        if (CUSTOM_BACKGROUND != null && BACKGROUND_COLOR == eu.hansolo.steelseries.tools.BackgroundColor.CUSTOM)
        {
            CLP = eu.hansolo.steelseries.tools.Shadow.INSTANCE.createInnerShadow((java.awt.Shape) BACKGROUND, CUSTOM_BACKGROUND, 0, 0.65f, java.awt.Color.BLACK, 20, 315);
        }
        else
        {
            CLP = eu.hansolo.steelseries.tools.Shadow.INSTANCE.createInnerShadow((java.awt.Shape) BACKGROUND, gaugeBackgroundGradient, 0, 0.65f, java.awt.Color.BLACK, 20, 315);            
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
                new java.awt.Color(0f, 0f, 0f, 0.5f),
                new java.awt.Color(0f, 0f, 0f, 0.0f),
                new java.awt.Color(0f, 0f, 0f, 0.0f),
                new java.awt.Color(0f, 0f, 0f, 0.5f)
            };
            final java.awt.LinearGradientPaint SHADOW_OVERLAY_GRADIENT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Double(BACKGROUND.getMinX(), 0), new java.awt.geom.Point2D.Double(BACKGROUND.getMaxX(), 0), SHADOW_OVERLAY_FRACTIONS, SHADOW_OVERLAY_COLORS);
            G2.setPaint(SHADOW_OVERLAY_GRADIENT);
            G2.fill(BACKGROUND);
        }
        
        G2.dispose();

        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
            G.drawImage(linBackgroundImage, 0, 0, null);
            G.dispose();            
        }
        // Cache current values
        linWidth = WIDTH;
        linHeight = HEIGHT;
        linBackgroundColor = BACKGROUND_COLOR;        
        linCustomBackground = CUSTOM_BACKGROUND;
        return linBackgroundImage;
    }
            
    @Override
    public String toString()
    {
        return "BackgroundImageFactory";
    }
}
