package eu.hansolo.steelseries.tools;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public enum LcdImageFactory
{
    INSTANCE;
    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    private java.awt.geom.Rectangle2D boundsBuffer = new java.awt.geom.Rectangle2D.Double();
    private eu.hansolo.steelseries.tools.LcdColor lcdColorBuffer = eu.hansolo.steelseries.tools.LcdColor.WHITE_LCD;    
    private java.awt.Paint customLcdBackgroundBuffer = java.awt.Color.RED;
    private java.awt.image.BufferedImage lcdImageBuffer = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    
    
    /**
     * Returns the image with the given lcd color.
     * @param BOUNDS 
     * @param LCD_COLOR
     * @param CUSTOM_LCD_BACKGROUND 
     * @param BACKGROUND_IMAGE 
     * @return buffered image containing the lcd with the selected lcd color
     */
    public java.awt.image.BufferedImage create_LCD_Image(final java.awt.geom.Rectangle2D BOUNDS, final eu.hansolo.steelseries.tools.LcdColor LCD_COLOR, final java.awt.Paint CUSTOM_LCD_BACKGROUND, final java.awt.image.BufferedImage BACKGROUND_IMAGE)
    {                        
        if (BOUNDS.getWidth() <= 0 || BOUNDS.getHeight() <= 0)
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }
        
        
        if (BOUNDS.equals(boundsBuffer) && LCD_COLOR == lcdColorBuffer && CUSTOM_LCD_BACKGROUND == customLcdBackgroundBuffer)
        {
            if (BACKGROUND_IMAGE != null && lcdImageBuffer.getWidth() == BACKGROUND_IMAGE.getWidth())
            {                                                    
                final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();
                G.drawImage(lcdImageBuffer, 0, 0, null);
                G.dispose();                 
            }            
            return lcdImageBuffer;
        }
              
        lcdImageBuffer.flush();
        if (BACKGROUND_IMAGE != null)
        {
            lcdImageBuffer = UTIL.createImage(BACKGROUND_IMAGE.getWidth(), BACKGROUND_IMAGE.getHeight(), java.awt.Transparency.TRANSLUCENT);
        }
        else
        {
            lcdImageBuffer = UTIL.createImage((int) BOUNDS.getWidth(), (int) BOUNDS.getHeight(), java.awt.Transparency.TRANSLUCENT);
        }
        
        final java.awt.Graphics2D G2 = lcdImageBuffer.createGraphics();
        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);        
        
        // Background rectangle
        final java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Double(0.0, BOUNDS.getMinY());
        final java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0.0, BOUNDS.getMaxY());
        if (BACKGROUND_START.equals(BACKGROUND_STOP))
        {
            BACKGROUND_STOP.setLocation(0.0, BACKGROUND_START.getY() + 1);
        }
        
        final float[] BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.08f,
            0.92f,
            1.0f
        };

        final java.awt.Color[] BACKGROUND_COLORS =
        {
            new java.awt.Color(0.3f, 0.3f, 0.3f, 1.0f),
            new java.awt.Color(0.4f, 0.4f, 0.4f, 1.0f),
            new java.awt.Color(0.4f, 0.4f, 0.4f, 1.0f),
            new java.awt.Color(0.9f, 0.9f, 0.9f, 1.0f)
        };

        final java.awt.LinearGradientPaint BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);
        final double BACKGROUND_CORNER_RADIUS = BOUNDS.getWidth() * 0.078125f;        
        final java.awt.geom.RoundRectangle2D BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(BOUNDS.getMinX(), BOUNDS.getMinY(), BOUNDS.getWidth(), BOUNDS.getHeight(), BACKGROUND_CORNER_RADIUS, BACKGROUND_CORNER_RADIUS);
        G2.setPaint(BACKGROUND_GRADIENT);
        G2.fill(BACKGROUND);

        // Foreground rectangle
        final java.awt.geom.Point2D FOREGROUND_START = new java.awt.geom.Point2D.Double(0.0, BOUNDS.getMinY() + 1.0);
        final java.awt.geom.Point2D FOREGROUND_STOP = new java.awt.geom.Point2D.Double(0.0, BOUNDS.getMaxY() - 1);
        if (FOREGROUND_START.equals(FOREGROUND_STOP))
        {
            FOREGROUND_STOP.setLocation(0.0, FOREGROUND_START.getY() + 1);
        }
        
        final float[] FOREGROUND_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.49f,
            0.5f,
            1.0f
        };
                
        final java.awt.Color[] FOREGROUND_COLORS =
        {
            LCD_COLOR.GRADIENT_START_COLOR,
            LCD_COLOR.GRADIENT_FRACTION1_COLOR,
            LCD_COLOR.GRADIENT_FRACTION2_COLOR,
            LCD_COLOR.GRADIENT_FRACTION3_COLOR,
            LCD_COLOR.GRADIENT_STOP_COLOR
        };
                
        if (LCD_COLOR == eu.hansolo.steelseries.tools.LcdColor.CUSTOM)
        {
            G2.setPaint(CUSTOM_LCD_BACKGROUND);
        }
        else
        {
            final java.awt.LinearGradientPaint FOREGROUND_GRADIENT = new java.awt.LinearGradientPaint(FOREGROUND_START, FOREGROUND_STOP, FOREGROUND_FRACTIONS, FOREGROUND_COLORS);
            G2.setPaint(FOREGROUND_GRADIENT);
        }
        
        final double FOREGROUND_CORNER_RADIUS = BACKGROUND.getArcWidth() - 1;
        final java.awt.geom.RoundRectangle2D FOREGROUND = new java.awt.geom.RoundRectangle2D.Double(BOUNDS.getMinX() + 1, BOUNDS.getMinY() + 1, BOUNDS.getWidth() - 2, BOUNDS.getHeight() - 2, FOREGROUND_CORNER_RADIUS, FOREGROUND_CORNER_RADIUS);        
        G2.fill(FOREGROUND);

        G2.dispose();

        
        if (BACKGROUND_IMAGE != null)
        {
            final java.awt.Graphics2D G = BACKGROUND_IMAGE.createGraphics();            
            G.drawImage(lcdImageBuffer, 0, 0, null);
            G.dispose();                   
        }
        
        // Buffer current values
        boundsBuffer.setRect(BOUNDS);
        lcdColorBuffer = LCD_COLOR;   
        customLcdBackgroundBuffer = CUSTOM_LCD_BACKGROUND;
        
        return lcdImageBuffer;        
    }
}
