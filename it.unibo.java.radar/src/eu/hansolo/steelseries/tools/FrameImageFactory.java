package eu.hansolo.steelseries.tools;

/**
 *
 * @author hansolo
 */
public enum FrameImageFactory
{
    INSTANCE;

    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    // Variables for caching
    private int radWidth = 0;       
    private eu.hansolo.steelseries.tools.FrameDesign radFrameDesign = eu.hansolo.steelseries.tools.FrameDesign.METAL;
    private java.awt.image.BufferedImage radFrameImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private eu.hansolo.steelseries.tools.FrameEffect radFrameEffect = eu.hansolo.steelseries.tools.FrameEffect.NONE;
    private java.awt.Paint radCustomFrame = java.awt.Color.BLACK;
    private int linWidth = 0;
    private int linHeight = 0;
    private eu.hansolo.steelseries.tools.FrameDesign linFrameDesign = eu.hansolo.steelseries.tools.FrameDesign.METAL;
    private java.awt.image.BufferedImage linFrameImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private eu.hansolo.steelseries.tools.FrameEffect linFrameEffect = eu.hansolo.steelseries.tools.FrameEffect.NONE;
    private java.awt.Paint linCustomFrame = java.awt.Color.BLACK;

    /**
     * Creates the frame image for a radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param FRAME_DESIGN
     * @param FRAME_EFFECT 
     * @param CUSTOM_FRAME_DESIGN 
     * @return a buffered image that contains the frame image for a radial gauge
     */
    public java.awt.image.BufferedImage createRadialFrame(final int WIDTH, final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN, final java.awt.Paint CUSTOM_FRAME_DESIGN, final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT)
    {        
        return createRadialFrame(WIDTH, FRAME_DESIGN, CUSTOM_FRAME_DESIGN, FRAME_EFFECT, null);
    }

    /**
     * Creates the frame image for a radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * If an image is passed to the method, it will paint to the image and
     * return this image. This will reduce the memory consumption.
     * @param WIDTH
     * @param FRAME_DESIGN
     * @param CUSTOM_FRAME_DESIGN
     * @param FRAME_EFFECT
     * @param BACKGROUND_IMAGE 
     * @return a buffered image that contains the frame image for a radial gauge
     */
    public java.awt.image.BufferedImage createRadialFrame(final int WIDTH, final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN, final java.awt.Paint CUSTOM_FRAME_DESIGN, final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT, final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {                                
        if (WIDTH <= 0)
        {            
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        // Take image from cache instead of creating a new one if parameters are the same as last time
        if (radWidth == WIDTH && radFrameDesign == FRAME_DESIGN && radFrameEffect == FRAME_EFFECT && radCustomFrame.equals(CUSTOM_FRAME_DESIGN))
        {
            if (BACKGROUND_IMAGE != null)
            {
                final java.awt.Graphics2D G2 = BACKGROUND_IMAGE.createGraphics();
                G2.drawImage(radFrameImage, 0, 0, null);
                G2.dispose();
            }
            return radFrameImage;
        }

        radFrameImage.flush();
        radFrameImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = radFrameImage.createGraphics();        
        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = WIDTH;
        final int IMAGE_HEIGHT = WIDTH;

        // Skip frame if frameDesign == NO_FRAME
        if (FRAME_DESIGN != eu.hansolo.steelseries.tools.FrameDesign.NO_FRAME)
        {
            // Shape that will be subtracted from the ellipse and will be filled by the background image later
            final java.awt.geom.Area SUBTRACT = new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.08411215245723724, IMAGE_HEIGHT * 0.08411215245723724, IMAGE_WIDTH * 0.8317756652832031, IMAGE_HEIGHT * 0.8317756652832031));

            final java.awt.geom.Area FRAME_OUTERFRAME = new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(0.0, 0.0, IMAGE_WIDTH, IMAGE_HEIGHT));
            FRAME_OUTERFRAME.subtract(SUBTRACT);
            final java.awt.Color FILL_COLOR_FRAME_OUTERFRAME = new java.awt.Color(0x848484);
            G2.setColor(FILL_COLOR_FRAME_OUTERFRAME);
            G2.fill(FRAME_OUTERFRAME);
            
            final java.awt.geom.Area FRAME_MAIN = new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.004672897048294544, IMAGE_HEIGHT * 0.004672897048294544, IMAGE_WIDTH * 0.9906542301177979, IMAGE_HEIGHT * 0.9906542301177979));
            FRAME_MAIN.subtract(SUBTRACT);
            final java.awt.geom.Point2D FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY());
            final java.awt.geom.Point2D FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY());
            final java.awt.geom.Point2D FRAME_MAIN_CENTER = new java.awt.geom.Point2D.Double(FRAME_MAIN.getBounds2D().getCenterX(), FRAME_MAIN.getBounds2D().getCenterY());

            final float[] FRAME_MAIN_FRACTIONS;
            final java.awt.Color[] FRAME_MAIN_COLORS;
            final java.awt.Paint FRAME_MAIN_GRADIENT;
           
            switch (FRAME_DESIGN)
            {
                case BLACK_METAL:
                    FRAME_MAIN_FRACTIONS = new float[]
                    {
                        0.0f,
                        45.0f,
                        125.0f,
                        180.0f,
                        245.0f,
                        315.0f,
                        360.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(153, 153, 153, 255),
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
                        45.0f,
                        90.0f,
                        125.0f,
                        180.0f,
                        235.0f,
                        270.0f,
                        315.0f,
                        360.0f
                    };

                    FRAME_MAIN_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(254, 254, 254, 255),
                        new java.awt.Color(210, 210, 210, 255),
                        new java.awt.Color(179, 179, 179, 255),
                        new java.awt.Color(238, 238, 238, 255),
                        new java.awt.Color(160, 160, 160, 255),
                        new java.awt.Color(238, 238, 238, 255),
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

            if (CUSTOM_FRAME_DESIGN != null && FRAME_DESIGN == eu.hansolo.steelseries.tools.FrameDesign.CUSTOM)
            {
                G2.setPaint(CUSTOM_FRAME_DESIGN);
            }
            else
            {
                G2.setPaint(FRAME_MAIN_GRADIENT);
            }
            G2.fill(FRAME_MAIN);

            //final java.awt.geom.Ellipse2D FRAME_INNERFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.07943925261497498, IMAGE_HEIGHT * 0.07943925261497498, IMAGE_WIDTH * 0.8411215543746948, IMAGE_HEIGHT * 0.8411215543746948);
            final java.awt.geom.Area FRAME_INNERFRAME = new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.07943925261497498, IMAGE_HEIGHT * 0.07943925261497498, IMAGE_WIDTH * 0.8411215543746948, IMAGE_HEIGHT * 0.8411215543746948));
            FRAME_INNERFRAME.subtract(SUBTRACT);
            
            // Former white ring
            G2.setColor(new java.awt.Color(0.75f, 0.75f, 0.75f, 1.0f));            
            G2.fill(FRAME_INNERFRAME);
                                                        
            // Frame effect overlay
            final java.awt.geom.Point2D EFFECT_CENTER = new java.awt.geom.Point2D.Double( (0.5 * IMAGE_WIDTH), (0.5 * IMAGE_HEIGHT) );
            final float[] EFFECT_FRACTIONS;
            final java.awt.Color[] EFFECT_COLORS;
            final java.awt.Paint EFFECT_GRADIENT;
            switch(FRAME_EFFECT)
            {
                case NONE:
                    
                default:
                    
                    break;
                    
                case EFFECT_BULGE:                    
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.82f,
                        0.83f,
                        0.86f,
                        0.87f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 0),
                        new java.awt.Color(0, 0, 0, 76),
                        new java.awt.Color(0, 0, 0, 95),
                        new java.awt.Color(219, 219, 219, 153),
                        new java.awt.Color(255, 255, 255, 151),
                        new java.awt.Color(0, 0, 0, 102)
                    };
                    EFFECT_GRADIENT = new java.awt.RadialGradientPaint(EFFECT_CENTER, (0.5f * IMAGE_WIDTH), EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_GRADIENT);
                    G2.fill(FRAME_OUTERFRAME);
                    break;
                    
                case EFFECT_CONE:
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.82f,
                        0.8201f,
                        0.96f,
                        0.9601f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 0),
                        new java.awt.Color(0, 0, 0, 50),
                        new java.awt.Color(9, 9, 9, 51),
                        new java.awt.Color(255, 255, 255, 124),
                        new java.awt.Color(223, 223, 223, 127),
                        new java.awt.Color(0, 0, 0, 76)
                    };
                    EFFECT_GRADIENT = new java.awt.RadialGradientPaint(EFFECT_CENTER, (float)(0.5 * IMAGE_WIDTH), EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_GRADIENT);
                    G2.fill(FRAME_OUTERFRAME);
                    break;
                    
                case EFFECT_TORUS:
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        0.82f,
                        0.8201f,
                        0.92f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 0),
                        new java.awt.Color(0, 0, 0, 50),
                        new java.awt.Color(13, 13, 13, 51),
                        new java.awt.Color(255, 255, 255, 64),
                        new java.awt.Color(0, 0, 0, 76)
                    };
                    EFFECT_GRADIENT = new java.awt.RadialGradientPaint(EFFECT_CENTER, (float)(0.5 * IMAGE_WIDTH), EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_GRADIENT);
                    G2.fill(FRAME_OUTERFRAME);
                    break;
                    
                case EFFECT_INNER_FRAME:
                    final java.awt.geom.Ellipse2D EFFECT_BIGINNERFRAME = new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * 0.0607476644217968, IMAGE_HEIGHT * 0.0607476644217968, IMAGE_WIDTH * 0.8785046339035034, IMAGE_HEIGHT * 0.8785046339035034);
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
            
        }
        
        G2.dispose();

        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
            G.drawImage(radFrameImage, 0, 0, null);
            G.dispose();
        }
        // Cache current parameters
        radWidth = WIDTH;
        radFrameDesign = FRAME_DESIGN;
        radFrameEffect = FRAME_EFFECT;
        radCustomFrame = CUSTOM_FRAME_DESIGN;
        return radFrameImage;
    }
    
    /**
     * Creates the frame image for a linear gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param FRAME_DESIGN
     * @param FRAME_EFFECT 
     * @param CUSTOM_FRAME_DESIGN 
     * @return a buffered image that contains the frame image for a linear gauge
     */
    public java.awt.image.BufferedImage createLinearFrame(final int WIDTH, final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN, final java.awt.Paint CUSTOM_FRAME_DESIGN, final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT)
    {
        return createLinearFrame(WIDTH, WIDTH, FRAME_DESIGN, CUSTOM_FRAME_DESIGN, FRAME_EFFECT);
    }
    
    /**
     * Creates the frame image for a linear gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param HEIGHT
     * @param FRAME_DESIGN
     * @param FRAME_EFFECT 
     * @param CUSTOM_FRAME_DESIGN 
     * @return a buffered image that contains the frame image for a linear gauge
     */
    public java.awt.image.BufferedImage createLinearFrame(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN, final java.awt.Paint CUSTOM_FRAME_DESIGN, final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT)
    {        
        return createLinearFrame(WIDTH, HEIGHT, FRAME_DESIGN, CUSTOM_FRAME_DESIGN, FRAME_EFFECT, null);
    }
    
    /**
     * Creates the frame image for a linear gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * If an image is passed to the method, it will paint to the image and
     * return this image. This will reduce the memory consumption.
     * @param WIDTH
     * @param HEIGHT
     * @param FRAME_DESIGN
     * @param FRAME_EFFECT 
     * @param CUSTOM_FRAME_DESIGN 
     * @param BACKGROUND_IMAGE 
     * @return a buffered image that contains the frame image for a linear gauge
     */
    public java.awt.image.BufferedImage createLinearFrame(final int WIDTH, final int HEIGHT, final eu.hansolo.steelseries.tools.FrameDesign FRAME_DESIGN, final java.awt.Paint CUSTOM_FRAME_DESIGN, final eu.hansolo.steelseries.tools.FrameEffect FRAME_EFFECT, final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {                
        if (WIDTH <= 2 || HEIGHT <= 2)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        // Take image from cache instead of creating a new one if parameters are the same as last time
        if (linWidth == WIDTH && linHeight == HEIGHT && linFrameDesign == FRAME_DESIGN && linFrameEffect == FRAME_EFFECT && linCustomFrame.equals(CUSTOM_FRAME_DESIGN))
        {
            if (BACKGROUND_IMAGE != null)
            {
                final java.awt.Graphics2D G2 = BACKGROUND_IMAGE.createGraphics();
                G2.drawImage(linFrameImage, 0, 0, null);
                G2.dispose();
            }
            return linFrameImage;
        }

        linFrameImage.flush();
        linFrameImage = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);            
        final java.awt.Graphics2D G2 = linFrameImage.createGraphics();        
                
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = WIDTH;
        final int IMAGE_HEIGHT = HEIGHT;

        final double OUTER_FRAME_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            OUTER_FRAME_CORNER_RADIUS = IMAGE_HEIGHT * 0.05;
        }
        else
        {
            OUTER_FRAME_CORNER_RADIUS = IMAGE_WIDTH * 0.05;
        }

        final java.awt.geom.Area OUTER_FRAME = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(0.0, 0.0, IMAGE_WIDTH, IMAGE_HEIGHT, OUTER_FRAME_CORNER_RADIUS, OUTER_FRAME_CORNER_RADIUS));
        final java.awt.Color COLOR_OUTER_FRAME = new java.awt.Color(0x848484);
        G2.setColor(COLOR_OUTER_FRAME);
        // The outer frame will be painted later because first we have to subtract the inner background

        final double FRAME_MAIN_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            FRAME_MAIN_CORNER_RADIUS = OUTER_FRAME_CORNER_RADIUS - ((OUTER_FRAME.getBounds2D().getHeight() - IMAGE_HEIGHT - 2) / 2.0);
        }
        else
        {
            FRAME_MAIN_CORNER_RADIUS = OUTER_FRAME_CORNER_RADIUS - ((OUTER_FRAME.getBounds2D().getWidth() - IMAGE_WIDTH - 2) / 2.0);
        }
        final java.awt.geom.Area FRAME_MAIN = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(1.0, 1.0, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, FRAME_MAIN_CORNER_RADIUS, FRAME_MAIN_CORNER_RADIUS));
        final java.awt.geom.Point2D FRAME_MAIN_START = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMinY() );
        final java.awt.geom.Point2D FRAME_MAIN_STOP = new java.awt.geom.Point2D.Double(0, FRAME_MAIN.getBounds2D().getMaxY() );
        final java.awt.geom.Point2D FRAME_MAIN_CENTER = new java.awt.geom.Point2D.Double(FRAME_MAIN.getBounds2D().getCenterX(), FRAME_MAIN.getBounds2D().getCenterY());

        // Create shape that needs to be subtracted from rectangles
        final double SUBTRACT_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            SUBTRACT_CORNER_RADIUS = IMAGE_HEIGHT * 0.02857143;
        }
        else
        {
            SUBTRACT_CORNER_RADIUS = IMAGE_WIDTH * 0.02857143;
        }
        final java.awt.geom.Area SUBTRACT = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(FRAME_MAIN.getBounds2D().getX() + 16, FRAME_MAIN.getBounds2D().getY() + 16, FRAME_MAIN.getBounds2D().getWidth() - 32, FRAME_MAIN.getBounds2D().getHeight() - 32, SUBTRACT_CORNER_RADIUS, SUBTRACT_CORNER_RADIUS));

        // Paint outer frame after we subtracted the inner background shape
        if (FRAME_DESIGN != eu.hansolo.steelseries.tools.FrameDesign.NO_FRAME)
        {
            OUTER_FRAME.subtract(SUBTRACT);
            G2.fill(OUTER_FRAME);
        }

        final float ANGLE_OFFSET = (float) Math.toDegrees(Math.atan((IMAGE_HEIGHT / 8.0f) / (IMAGE_WIDTH / 2.0f)));
        final float[] FRAME_MAIN_FRACTIONS;
        final java.awt.Color[] FRAME_MAIN_COLORS;
        final java.awt.Paint FRAME_MAIN_GRADIENT;

        switch(FRAME_DESIGN)
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
                FRAME_MAIN_START.setLocation( (0.08571428571428572 * IMAGE_WIDTH), (0.0 * IMAGE_HEIGHT) );
                FRAME_MAIN_STOP.setLocation( ((0.08571428571428572 + 0.7436714214664596) * IMAGE_WIDTH), ((0.0 + 0.9868853088441548) * IMAGE_HEIGHT) );
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
                FRAME_MAIN_START.setLocation((0.08571428571428572 * IMAGE_WIDTH), (0.014285714285714285 * IMAGE_HEIGHT));
                FRAME_MAIN_STOP.setLocation(((0.08571428571428572 + 0.6496765631964967) * IMAGE_WIDTH), ((0.014285714285714285 + 1.0004141774777557) * IMAGE_HEIGHT));
                FRAME_MAIN_FRACTIONS = new float[]
                {
                    0.0f,
                    0.21f,
                    0.47f,
                    1.0f
                };
                FRAME_MAIN_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(102, 102, 102, 255),
                    new java.awt.Color(0, 0, 0, 255),
                    new java.awt.Color(102, 102, 102, 255),
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
        if (CUSTOM_FRAME_DESIGN != null && FRAME_DESIGN == eu.hansolo.steelseries.tools.FrameDesign.CUSTOM)
        {
            G2.setPaint(CUSTOM_FRAME_DESIGN);
        }
        else
        {
            G2.setPaint(FRAME_MAIN_GRADIENT);
        }
                
        if (FRAME_DESIGN != eu.hansolo.steelseries.tools.FrameDesign.NO_FRAME)
        {
            FRAME_MAIN.subtract(SUBTRACT);
            G2.fill(FRAME_MAIN);
            
            final float[] EFFECT_FRACTIONS;
            final java.awt.Color[] EFFECT_COLORS;
            final java.awt.Paint EFFECT_GRADIENT;
            switch (FRAME_EFFECT)
            {
                case NONE:
                    
                default:
                    break;
                    
                case EFFECT_BULGE:                           
                    final float relFrameSize;
                    // The smaller side is important for the contour gradient
                    if (WIDTH >= HEIGHT)
                    {
                        relFrameSize = (32f / HEIGHT);                    
                    }
                    else
                    {
                        relFrameSize = (32f / WIDTH);                    
                    }
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        relFrameSize * 0.87f,
                        relFrameSize * 0.86f,
                        relFrameSize * 0.83f,                    
                        relFrameSize,
                        relFrameSize * 1.01f,
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {                    
                        new java.awt.Color(0, 0, 0, 102),
                        new java.awt.Color(255, 255, 255, 151),
                        new java.awt.Color(219, 219, 219, 153),
                        new java.awt.Color(219, 219, 219, 153),
                        new java.awt.Color(36, 36, 36, 76),
                        new java.awt.Color(0, 0, 0, 95),                    
                        new java.awt.Color(0, 0, 0, 0)
                    };                
                    EFFECT_GRADIENT = new eu.hansolo.steelseries.tools.ContourGradientPaint(OUTER_FRAME.getBounds2D(), EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_GRADIENT);
                    G2.fill(OUTER_FRAME);
                    break;
                
                case EFFECT_CONE:
                    // The smaller side is important for the contour gradient
                    
                    if (WIDTH >= HEIGHT)
                    {
                        relFrameSize = (32f / HEIGHT);                    
                    }
                    else
                    {
                        relFrameSize = (32f / WIDTH);                    
                    }                    
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        relFrameSize * 0.1f,
                        //relFrameSize * 0.2f,
                        relFrameSize * 0.3f,
                        //relFrameSize * 0.4f,
                        //relFrameSize * 0.5f,
                        //relFrameSize * 0.6f,
                        //relFrameSize * 0.7f,
                        //relFrameSize * 0.8f,
                        //relFrameSize * 0.9f,
                        relFrameSize,                        
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {                        
                        //java.awt.Color.BLUE,     // 0.0f                   Outer border of frame
                        //java.awt.Color.RED,      // 0.1f * relFrameSize    
                        //java.awt.Color.WHITE,    // 0.2f * relFrameSize
                        //java.awt.Color.BLACK,    // 0.3f * relFrameSize
                        //java.awt.Color.YELLOW,   // 0.4f * relFrameSize
                        //java.awt.Color.MAGENTA,  // 0.5f * relFrameSize
                        //java.awt.Color.CYAN,     // 0.6f * relFrameSize
                        //java.awt.Color.GREEN,    // 0.7f * relFrameSize
                        //java.awt.Color.GRAY,     // 0.8f * relFrameSize
                        //java.awt.Color.BLUE,     // 0.9f * relFrameSize
                        //java.awt.Color.RED,      // 1.0f * relFrameSize    Inner border of frame
                        //java.awt.Color.WHITE,    // 1.0f
                        
                        new java.awt.Color(0f, 0f, 0f, 0.3f),
                        new java.awt.Color(0f, 0f, 0f, 0.3f),
                        new java.awt.Color(1f, 1f, 1f, 0.5f),
                        new java.awt.Color(0f, 0f, 0f, 0.2f),                        
                        new java.awt.Color(0f, 0f, 0f, 0f)
                    };
                    EFFECT_GRADIENT = new eu.hansolo.steelseries.tools.ContourGradientPaint(OUTER_FRAME.getBounds2D(), EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_GRADIENT);
                    G2.fill(OUTER_FRAME);
                    break;
                    
                case EFFECT_TORUS:
                    // The smaller side is important for the contour gradient
                    if (WIDTH >= HEIGHT)
                    {
                        relFrameSize = (32f / HEIGHT);                    
                    }
                    else
                    {
                        relFrameSize = (32f / WIDTH);                    
                    }
                    EFFECT_FRACTIONS = new float[]
                    {
                        0.0f,
                        relFrameSize * 0.1f,
                        //relFrameSize * 0.2f,
                        //relFrameSize * 0.3f,
                        //relFrameSize * 0.4f,
                        relFrameSize * 0.5f,
                        //relFrameSize * 0.6f,
                        //relFrameSize * 0.7f,
                        //relFrameSize * 0.8f,
                        //relFrameSize * 0.9f,
                        relFrameSize,                        
                        1.0f
                    };
                    EFFECT_COLORS = new java.awt.Color[]
                    {                        
                        //java.awt.Color.BLUE,     // 0.0f
                        //java.awt.Color.RED,      // 0.1f * relFrameSize
                        //java.awt.Color.WHITE,    // 0.2f * relFrameSize
                        //java.awt.Color.BLACK,    // 0.3f * relFrameSize
                        //java.awt.Color.YELLOW,   // 0.4f * relFrameSize
                        //java.awt.Color.MAGENTA,  // 0.5f * relFrameSize
                        //java.awt.Color.CYAN,     // 0.6f * relFrameSize
                        //java.awt.Color.GREEN,    // 0.7f * relFrameSize
                        //java.awt.Color.GRAY,     // 0.8f * relFrameSize
                        //java.awt.Color.BLUE,     // 0.9f * relFrameSize
                        //java.awt.Color.RED,      // 1.0f * relFrameSize
                        //java.awt.Color.WHITE,    // 1.0f
                        
                        new java.awt.Color(0f, 0f, 0f, 0.3f),
                        new java.awt.Color(0f, 0f, 0f, 0.3f),
                        new java.awt.Color(1f, 1f, 1f, 0.5f),
                        new java.awt.Color(0f, 0f, 0f, 0.2f),                        
                        new java.awt.Color(0f, 0f, 0f, 0f)
                    };                                                                
                    EFFECT_GRADIENT = new eu.hansolo.steelseries.tools.ContourGradientPaint(OUTER_FRAME.getBounds2D(), EFFECT_FRACTIONS, EFFECT_COLORS);
                    G2.setPaint(EFFECT_GRADIENT);
                    G2.fill(OUTER_FRAME);
                    break;
                    
                case EFFECT_INNER_FRAME:
                    final java.awt.geom.RoundRectangle2D EFFECT_BIGINNERFRAME = new java.awt.geom.RoundRectangle2D.Double(10, 10, IMAGE_WIDTH -20, IMAGE_HEIGHT - 20, 10.0, 10.0);
                    final java.awt.geom.Point2D EFFECT_BIGINNERFRAME_START = new java.awt.geom.Point2D.Double(0, EFFECT_BIGINNERFRAME.getBounds2D().getMinY() );
                    final java.awt.geom.Point2D EFFECT_BIGINNERFRAME_STOP = new java.awt.geom.Point2D.Double(0, EFFECT_BIGINNERFRAME.getBounds2D().getMaxY() );
                    final float[] EFFECT_BIGINNERFRAME_FRACTIONS = 
                    {
                        0.0f,                        
                        0.13f,                        
                        0.45f,
                        0.92f,                        
                        1.0f
                    };
                    final java.awt.Color[] EFFECT_BIGINNERFRAME_COLORS = 
                    {
                        new java.awt.Color(0, 0, 0, 183),                        
                        new java.awt.Color(0, 0, 0, 25),
                        new java.awt.Color(0, 0, 0, 160),                        
                        new java.awt.Color(0, 0, 0, 80),                        
                        new java.awt.Color(255, 255, 255, 158)
                    };
                    final java.awt.LinearGradientPaint EFFECT_BIGINNERFRAME_GRADIENT = new java.awt.LinearGradientPaint(EFFECT_BIGINNERFRAME_START, EFFECT_BIGINNERFRAME_STOP, EFFECT_BIGINNERFRAME_FRACTIONS, EFFECT_BIGINNERFRAME_COLORS);
                    G2.setPaint(EFFECT_BIGINNERFRAME_GRADIENT);
                    G2.fill(EFFECT_BIGINNERFRAME);
                    break;
            }                        
        }

        final double INNER_FRAME_CORNER_RADIUS;
        if (IMAGE_WIDTH >= IMAGE_HEIGHT)
        {
            INNER_FRAME_CORNER_RADIUS = IMAGE_HEIGHT * 0.02857143;
        }
        else
        {
            INNER_FRAME_CORNER_RADIUS = IMAGE_WIDTH * 0.02857143;
        }

        final java.awt.geom.Area INNER_FRAME = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(FRAME_MAIN.getBounds2D().getX() + 16, FRAME_MAIN.getBounds2D().getY() + 16, FRAME_MAIN.getBounds2D().getWidth() - 32, FRAME_MAIN.getBounds2D().getHeight() - 32, INNER_FRAME_CORNER_RADIUS, INNER_FRAME_CORNER_RADIUS));
        G2.setColor(new java.awt.Color(0.75f, 0.75f, 0.75f, 1.0f));            
        if (FRAME_DESIGN != eu.hansolo.steelseries.tools.FrameDesign.NO_FRAME)
        {
            INNER_FRAME.subtract(SUBTRACT);
            G2.fill(INNER_FRAME);
        }

        G2.dispose();

        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
            G.drawImage(linFrameImage, 0, 0, null);
            G.dispose();            
        }
        
        // Cache current parameters
        linWidth = WIDTH;
        linHeight = HEIGHT;
        linFrameDesign = FRAME_DESIGN;
        linFrameEffect = FRAME_EFFECT;
        linCustomFrame = CUSTOM_FRAME_DESIGN;

        return linFrameImage;
    }
    
    @Override
    public String toString()
    {
        return "FrameImageFactory";
    }
}
