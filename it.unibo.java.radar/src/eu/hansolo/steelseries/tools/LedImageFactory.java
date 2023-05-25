package eu.hansolo.steelseries.tools;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public enum LedImageFactory
{
    INSTANCE;    
    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    

    private int sizeBuffer = 0;
    private eu.hansolo.steelseries.tools.LedColor ledColorBuffer = eu.hansolo.steelseries.tools.LedColor.RED_LED;
    private eu.hansolo.steelseries.tools.CustomLedColor customLedColorBuffer = new eu.hansolo.steelseries.tools.CustomLedColor(java.awt.Color.RED);
    private java.awt.image.BufferedImage ledOnBuffer = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    private java.awt.image.BufferedImage ledOffBuffer = UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
    
    /**
     * Returns a image of a led with the given size, state and color.
     * If the LED_COLOR parameter equals CUSTOM the customLedColor will be used
     * to calculate the custom led colors
     * @param SIZE
     * @param STATE
     * @param LED_COLOR 
     * @param CUSTOM_LED_COLOR      
     * @return the led image 
     */
    public final java.awt.image.BufferedImage create_LED_Image(final int SIZE, final int STATE, final eu.hansolo.steelseries.tools.LedColor LED_COLOR, final eu.hansolo.steelseries.tools.CustomLedColor CUSTOM_LED_COLOR)
    {
        if (SIZE <= 11) // 11 is needed because otherwise the image size would be smaller than 1 in the worst case
        {
            return UTIL.createImage(1, 1, java.awt.Transparency.TRANSLUCENT);
        }

        if (SIZE == sizeBuffer && LED_COLOR == ledColorBuffer && CUSTOM_LED_COLOR == customLedColorBuffer)
        {
            switch(STATE)
            {
                case 0:
                    return ledOffBuffer;                    
                case 1:
                    return ledOnBuffer;                    
            }
        }
        
        ledOnBuffer.flush();
        ledOffBuffer.flush();
        
        ledOnBuffer = UTIL.createImage((int) (SIZE * 0.0934579439), (int) (SIZE * 0.0934579439), java.awt.Transparency.TRANSLUCENT);
        ledOffBuffer = UTIL.createImage((int) (SIZE * 0.0934579439), (int) (SIZE * 0.0934579439), java.awt.Transparency.TRANSLUCENT);
        
        final java.awt.Graphics2D G2_ON = ledOnBuffer.createGraphics();
        final java.awt.Graphics2D G2_OFF = ledOffBuffer.createGraphics();

        G2_ON.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2_OFF.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = ledOnBuffer.getWidth();
        final int IMAGE_HEIGHT = ledOnBuffer.getHeight();

        // Define led data
        final java.awt.geom.Ellipse2D LED = new java.awt.geom.Ellipse2D.Double(0.25 * IMAGE_WIDTH, 0.25 * IMAGE_HEIGHT, 0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
        final java.awt.geom.Ellipse2D LED_CORONA = new java.awt.geom.Ellipse2D.Double(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        final java.awt.geom.Point2D LED_CENTER = new java.awt.geom.Point2D.Double(LED.getCenterX(), LED.getCenterY());

        final float[] LED_FRACTIONS =
        {
            0.0f,
            0.2f,
            1.0f
        };

        final float[] LED_INNER_SHADOW_FRACTIONS =
        {
            0.0f,
            0.8f,
            1.0f
        };

        final java.awt.Color[] LED_INNER_SHADOW_COLORS =
        {
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.0f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.0f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.4f),
        };

        final float[] LED_ON_CORONA_FRACTIONS =
        {
            0.0f,
            0.6f,
            0.7f,
            0.8f,
            0.85f,
            1.0f
        };
        
        final java.awt.Color[] LED_OFF_COLORS;                
        final java.awt.Color[] LED_ON_COLORS;
        final java.awt.Color[] LED_ON_CORONA_COLORS;        
        
        if (LED_COLOR == eu.hansolo.steelseries.tools.LedColor.CUSTOM)
        {
            LED_OFF_COLORS = new java.awt.Color[]
            {
                CUSTOM_LED_COLOR.INNER_COLOR1_OFF,
                CUSTOM_LED_COLOR.INNER_COLOR2_OFF,
                CUSTOM_LED_COLOR.OUTER_COLOR_OFF
            };
            
            LED_ON_COLORS = new java.awt.Color[]
            {
                CUSTOM_LED_COLOR.INNER_COLOR1_ON,
                CUSTOM_LED_COLOR.INNER_COLOR2_ON,
                CUSTOM_LED_COLOR.OUTER_COLOR_ON
            };
            
            LED_ON_CORONA_COLORS = new java.awt.Color[]
            {
                UTIL.setAlpha(CUSTOM_LED_COLOR.CORONA_COLOR, 0.0f),
                UTIL.setAlpha(CUSTOM_LED_COLOR.CORONA_COLOR, 0.4f),
                UTIL.setAlpha(CUSTOM_LED_COLOR.CORONA_COLOR, 0.25f),
                UTIL.setAlpha(CUSTOM_LED_COLOR.CORONA_COLOR, 0.15f),
                UTIL.setAlpha(CUSTOM_LED_COLOR.CORONA_COLOR, 0.05f),
                UTIL.setAlpha(CUSTOM_LED_COLOR.CORONA_COLOR, 0.0f)
            };
        }
        else
        {
            LED_OFF_COLORS = new java.awt.Color[]
            {
                LED_COLOR.INNER_COLOR1_OFF,
                LED_COLOR.INNER_COLOR2_OFF,
                LED_COLOR.OUTER_COLOR_OFF
            };
            
            LED_ON_COLORS = new java.awt.Color[]
            {
                LED_COLOR.INNER_COLOR1_ON,
                LED_COLOR.INNER_COLOR2_ON,
                LED_COLOR.OUTER_COLOR_ON
            };
            
            LED_ON_CORONA_COLORS = new java.awt.Color[]
            {
                UTIL.setAlpha(LED_COLOR.CORONA_COLOR, 0.0f),
                UTIL.setAlpha(LED_COLOR.CORONA_COLOR, 0.4f),
                UTIL.setAlpha(LED_COLOR.CORONA_COLOR, 0.25f),
                UTIL.setAlpha(LED_COLOR.CORONA_COLOR, 0.15f),
                UTIL.setAlpha(LED_COLOR.CORONA_COLOR, 0.05f),
                UTIL.setAlpha(LED_COLOR.CORONA_COLOR, 0.0f)
            };
        }
                        
        // Define gradients for the lower led
        final java.awt.RadialGradientPaint LED_OFF_GRADIENT = new java.awt.RadialGradientPaint(LED_CENTER, 0.25f * IMAGE_WIDTH, LED_FRACTIONS, LED_OFF_COLORS);
        final java.awt.RadialGradientPaint LED_ON_GRADIENT = new java.awt.RadialGradientPaint(LED_CENTER, 0.25f * IMAGE_WIDTH, LED_FRACTIONS, LED_ON_COLORS);
        final java.awt.RadialGradientPaint LED_INNER_SHADOW_GRADIENT = new java.awt.RadialGradientPaint(LED_CENTER, 0.25f * IMAGE_WIDTH, LED_INNER_SHADOW_FRACTIONS, LED_INNER_SHADOW_COLORS);
        final java.awt.RadialGradientPaint LED_ON_CORONA_GRADIENT = new java.awt.RadialGradientPaint(LED_CENTER, 0.5f * IMAGE_WIDTH, LED_ON_CORONA_FRACTIONS, LED_ON_CORONA_COLORS);


        // Define light reflex data
        final java.awt.geom.Ellipse2D LED_LIGHTREFLEX = new java.awt.geom.Ellipse2D.Double(0.4 * IMAGE_WIDTH, 0.35 * IMAGE_WIDTH, 0.2 * IMAGE_WIDTH, 0.15 * IMAGE_WIDTH);
        final java.awt.geom.Point2D LED_LIGHTREFLEX_START = new java.awt.geom.Point2D.Double(0, LED_LIGHTREFLEX.getMinY());
        final java.awt.geom.Point2D LED_LIGHTREFLEX_STOP = new java.awt.geom.Point2D.Double(0, LED_LIGHTREFLEX.getMaxY());

        final float[] LIGHT_REFLEX_FRACTIONS =
        {
            0.0f,
            1.0f
        };

        final java.awt.Color[] LIGHTREFLEX_COLORS =
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.4f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f)
        };

        // Define light reflex gradients
        final java.awt.LinearGradientPaint LED_LIGHTREFLEX_GRADIENT = new java.awt.LinearGradientPaint(LED_LIGHTREFLEX_START, LED_LIGHTREFLEX_STOP, LIGHT_REFLEX_FRACTIONS, LIGHTREFLEX_COLORS);

        // Draw the led in on state
        // LED ON
        G2_ON.setPaint(LED_ON_CORONA_GRADIENT);
        G2_ON.fill(LED_CORONA);
        G2_ON.setPaint(LED_ON_GRADIENT);
        G2_ON.fill(LED);
        G2_ON.setPaint(LED_INNER_SHADOW_GRADIENT);
        G2_ON.fill(LED);
        G2_ON.setPaint(LED_LIGHTREFLEX_GRADIENT);
        G2_ON.fill(LED_LIGHTREFLEX);
        
        // Draw the led in off state
        // LED OFF
        G2_OFF.setPaint(LED_OFF_GRADIENT);
        G2_OFF.fill(LED);
        G2_OFF.setPaint(LED_INNER_SHADOW_GRADIENT);
        G2_OFF.fill(LED);
        G2_OFF.setPaint(LED_LIGHTREFLEX_GRADIENT);
        G2_OFF.fill(LED_LIGHTREFLEX);
                                
        G2_ON.dispose();
        G2_OFF.dispose();

        // Buffer current values
        sizeBuffer = SIZE;
        ledColorBuffer = LED_COLOR;
        customLedColorBuffer = CUSTOM_LED_COLOR;
        
        switch (STATE)
        {
            case 0:
                // Return LED OFF
                return ledOffBuffer;
            case 1:
                // Return LED ON
                return ledOnBuffer;
            default:
                return ledOffBuffer;
        }
    }
    
}
