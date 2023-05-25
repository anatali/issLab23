package eu.hansolo.steelseries.tools;

/**
 *
 * @author hansolo
 */
public enum PointerImageFactory
{
    INSTANCE;

    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    private int radWidth = 0;
    private eu.hansolo.steelseries.tools.PointerType radPointerType = eu.hansolo.steelseries.tools.PointerType.TYPE1;
    private eu.hansolo.steelseries.tools.ColorDef radPointerColor = null;
    private eu.hansolo.steelseries.tools.CustomColorDef radCustomPointerColor = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.RED);    
    private java.awt.image.BufferedImage radPointerImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);

    private int radWidthShadow = 0;
    private eu.hansolo.steelseries.tools.PointerType radPointerTypeShadow = eu.hansolo.steelseries.tools.PointerType.TYPE1;    
    private java.awt.image.BufferedImage radPointerShadowImage = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);

    /**
     * Creates the pointer image for a centered radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param POINTER_TYPE
     * @param POINTER_COLOR
     * @return a buffered image that contains the pointer image for a centered radial gauge
     */
    public java.awt.image.BufferedImage createStandardPointer(final int WIDTH, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE, final eu.hansolo.steelseries.tools.ColorDef POINTER_COLOR)
    {        
        return createStandardPointer(WIDTH, POINTER_TYPE, POINTER_COLOR, null);
    }

    /**
     * Creates the pointer image for a centered radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param POINTER_TYPE
     * @param POINTER_COLOR
     * @param CUSTOM_POINTER_COLOR
     * @return a buffered image that contains the pointer image for a centered radial gauge
     */
    public java.awt.image.BufferedImage createStandardPointer(final int WIDTH, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE, final eu.hansolo.steelseries.tools.ColorDef POINTER_COLOR, final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_POINTER_COLOR)
    {
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        if (radWidth == WIDTH && radPointerType == POINTER_TYPE && radPointerColor == POINTER_COLOR && radCustomPointerColor == CUSTOM_POINTER_COLOR)
        {
            return radPointerImage;
        }

        radPointerImage.flush();
        radPointerImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = radPointerImage.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = radPointerImage.getWidth();
        final int IMAGE_HEIGHT = radPointerImage.getHeight();

        final java.awt.geom.GeneralPath POINTER;
        final java.awt.geom.Point2D POINTER_START;
        final java.awt.geom.Point2D POINTER_STOP;
        final float[] POINTER_FRACTIONS;
        final java.awt.Color[] POINTER_COLORS;
        final java.awt.Paint POINTER_GRADIENT;

        switch (POINTER_TYPE)
        {            
            case TYPE2:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
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
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                { 
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(0, 0, 0, 255),
                        new java.awt.Color(0, 0, 0, 255),
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT
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
                POINTER = new java.awt.geom.GeneralPath(new java.awt.geom.Rectangle2D.Double(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.009345794392523364, IMAGE_HEIGHT * 0.37383177570093457));                
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(POINTER_COLOR.LIGHT);
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
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
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
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        POINTER_COLOR.DARK,
                        POINTER_COLOR.DARK,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT
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
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
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
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(POINTER_COLOR.DARK);
                }
                else
                {
                    G2.setColor(CUSTOM_POINTER_COLOR.DARK);
                }
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
                
            case TYPE6:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
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
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                { 
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.LIGHT
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {                        
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.LIGHT
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(POINTER_COLOR.DARK);
                }
                else
                {
                    G2.setColor(CUSTOM_POINTER_COLOR.DARK);
                }
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
                
            case TYPE7:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.closePath();
                POINTER_START = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new java.awt.geom.Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]
                {
                    0.0f,
                    1.0f
                };
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                { 
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        POINTER_COLOR.DARK,
                        POINTER_COLOR.MEDIUM
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {                        
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;
                
            case TYPE8:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
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
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                { 
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {                        
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);                
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {
                    G2.setColor(POINTER_COLOR.DARK);
                }
                else
                {
                    G2.setColor(CUSTOM_POINTER_COLOR.DARK);
                }
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
                
            case TYPE9:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.4392523364485981);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.4392523364485981);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5280373831775701);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.6074766355140186);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5280373831775701);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
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

                final java.awt.geom.GeneralPath COLORED_BOX = new java.awt.geom.GeneralPath();
                COLORED_BOX.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                COLORED_BOX.moveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.21962616822429906);
                COLORED_BOX.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.21962616822429906);
                COLORED_BOX.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.13551401869158877);
                COLORED_BOX.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.13551401869158877);
                COLORED_BOX.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.21962616822429906);
                COLORED_BOX.closePath();                
                G2.setColor(POINTER_COLOR.MEDIUM);
                G2.fill(COLORED_BOX);
                break;
                
            case TYPE1:
                                    
            default:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.40186915887850466);
                POINTER.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.397196261682243);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
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
                if (POINTER_COLOR != eu.hansolo.steelseries.tools.ColorDef.CUSTOM)
                {                                    
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        POINTER_COLOR.VERY_DARK,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.VERY_DARK
                    };
                }
                else
                {
                    POINTER_COLORS = new java.awt.Color[]
                    {
                        CUSTOM_POINTER_COLOR.VERY_DARK,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.VERY_DARK
                    };
                }
                POINTER_GRADIENT = new java.awt.LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                final java.awt.Color STROKE_COLOR_POINTER = POINTER_COLOR.LIGHT;
                G2.setColor(STROKE_COLOR_POINTER);
                G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;                    
        }

        G2.dispose();

        // Cache current parameters
        radWidth = WIDTH;
        radPointerType = POINTER_TYPE;
        radPointerColor = POINTER_COLOR;
        radCustomPointerColor = CUSTOM_POINTER_COLOR;

        return radPointerImage;
    }
    
    /**
     * Creates the pointer shadow image for a centered radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param POINTER_TYPE
     * @return a buffered image that contains the pointer shadow image for a centered radial gauge
     */
    public java.awt.image.BufferedImage createStandardPointerShadow(final int WIDTH, final eu.hansolo.steelseries.tools.PointerType POINTER_TYPE)
    {
        if (WIDTH <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        if (radWidthShadow == WIDTH && radPointerTypeShadow == POINTER_TYPE)
        {
            return radPointerShadowImage;
        }

        final java.awt.Color SHADOW_COLOR = new java.awt.Color(0.0f, 0.0f, 0.0f, 0.65f);

        radPointerShadowImage.flush();
        radPointerShadowImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = radPointerShadowImage.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = radPointerShadowImage.getWidth();
        final int IMAGE_HEIGHT = radPointerShadowImage.getHeight();

        final java.awt.geom.GeneralPath POINTER;

        switch(POINTER_TYPE)
        {
            case TYPE1:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.40186915887850466);
                POINTER.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.397196261682243);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE2:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
                
            case TYPE3:
                
                break;
                
            case TYPE4:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);                
                G2.fill(POINTER);
                break;   
                
            case TYPE5:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);                
                G2.fill(POINTER);
                break;
                
            case TYPE6:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);                
                G2.fill(POINTER);
                break;
            
            case TYPE7:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);                
                G2.fill(POINTER);
                break;
                
            case TYPE8:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);                
                G2.fill(POINTER);
                break;
            
            case TYPE9:
                POINTER = new java.awt.geom.GeneralPath();
                POINTER.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.4392523364485981);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.4392523364485981);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5280373831775701);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.6074766355140186);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5280373831775701);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);                
                G2.fill(POINTER);
                break;
                
            default:
                
                break;
        }

        G2.dispose();

        // Cache current parameters
        radWidthShadow = WIDTH;
        radPointerTypeShadow = POINTER_TYPE;

        return radPointerShadowImage;
    }

    @Override
    public String toString()
    {
        return "PointerImageFactory";
    }         
}
