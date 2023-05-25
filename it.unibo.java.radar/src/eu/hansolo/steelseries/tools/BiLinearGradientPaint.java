package eu.hansolo.steelseries.tools;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public final class BiLinearGradientPaint implements java.awt.Paint
{
    private static final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;    
    private final java.awt.Rectangle BOUNDS;
    private final java.awt.Color COLOR_00;
    private final java.awt.Color COLOR_10;
    private final java.awt.Color COLOR_01;
    private final java.awt.Color COLOR_11;  
    private final float FRACTION_X_STEPSIZE;
    private final float FRACTION_Y_STEPSIZE;
    private int titleBarHeight;    


    /**
     * Enhanced constructor which takes bounds of the objects SHAPE to fill and the four
     * colors we need to create the bilinear interpolated gradient
     * @param SHAPE      
     * @param COLOR_00      
     * @param COLOR_10 
     * @param COLOR_01 
     * @param COLOR_11 
     * @throws IllegalArgumentException
     */
    public BiLinearGradientPaint(final java.awt.Shape SHAPE, final java.awt.Color COLOR_00, final java.awt.Color COLOR_10, final java.awt.Color COLOR_01, final java.awt.Color COLOR_11) throws IllegalArgumentException
    {
        // Set the values
        this.BOUNDS = SHAPE.getBounds();
        this.COLOR_00 = COLOR_00;
        this.COLOR_10 = COLOR_10;
        this.COLOR_01 = COLOR_01;
        this.COLOR_11 = COLOR_11;                
        this.FRACTION_X_STEPSIZE = 1.0f / (BOUNDS.getBounds().width);
        this.FRACTION_Y_STEPSIZE = 1.0f / (BOUNDS.getBounds().height);          
        this.titleBarHeight = -1;
    }
    
    @Override
    public java.awt.PaintContext createContext(final java.awt.image.ColorModel COLOR_MODEL, final java.awt.Rectangle DEVICE_BOUNDS, final java.awt.geom.Rectangle2D USER_BOUNDS, final java.awt.geom.AffineTransform TRANSFORM, final java.awt.RenderingHints HINTS)
    {
        return new BiLinearGradientPaintContext();
    }
       
    @Override
    public int getTransparency()
    {
        return java.awt.Transparency.TRANSLUCENT;
    }

    private final class BiLinearGradientPaintContext implements java.awt.PaintContext
    {        
        public BiLinearGradientPaintContext()
        {          
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public java.awt.image.ColorModel getColorModel()
        {
            return java.awt.image.ColorModel.getRGBdefault();
        }
        
        @Override
        public java.awt.image.Raster getRaster(final int X, final int Y, final int TILE_WIDTH, final int TILE_HEIGHT)
        {
            // Get the offset given by the height of the titlebar
            if (titleBarHeight == -1)
            {
                titleBarHeight = Y;                
            }
            
            // Create raster for given colormodel
            final java.awt.image.WritableRaster RASTER = getColorModel().createCompatibleWritableRaster(TILE_WIDTH, TILE_HEIGHT);
            
            // Create data array with place for red, green, blue and alpha values
            final int[] DATA = new int[(TILE_WIDTH * TILE_HEIGHT * 4)];                                 
            java.awt.Color currentColor;                                                                       
                                                           
            float fraction_x = (X - BOUNDS.x) * FRACTION_X_STEPSIZE;
            float fraction_y = (Y - BOUNDS.y - titleBarHeight) * FRACTION_Y_STEPSIZE;
            
            fraction_x = fraction_x > 1f ? 1f : fraction_x;
            fraction_y = fraction_y > 1f ? 1f : fraction_y;
            
            for (int tileY = 0; tileY < TILE_HEIGHT; tileY++)
            {            
                for (int tileX = 0; tileX < TILE_WIDTH; tileX++)
                {                                                                                                                        
                    currentColor = UTIL.bilinearInterpolateColor(COLOR_00, COLOR_10, COLOR_01, COLOR_11, fraction_x, fraction_y);
                    
                    fraction_x += FRACTION_X_STEPSIZE;
                    fraction_x = fraction_x > 1f ? 1f : fraction_x;
                    
                    // Fill data array with calculated color values
                    final int BASE = (tileY * TILE_WIDTH + tileX) * 4;
                    DATA[BASE + 0] = currentColor.getRed();
                    DATA[BASE + 1] = currentColor.getGreen();
                    DATA[BASE + 2] = currentColor.getBlue();
                    DATA[BASE + 3] = currentColor.getAlpha();                                                                           
                }                
                fraction_x = (X - BOUNDS.x) * FRACTION_X_STEPSIZE;
                fraction_y += FRACTION_Y_STEPSIZE;                                               
                fraction_y = fraction_y > 1f ? 1f : fraction_y;
            }            
                        
            // Fill the raster with the data
            RASTER.setPixels(0, 0, TILE_WIDTH, TILE_HEIGHT, DATA);

            return RASTER;
        }
    }
    
    @Override
    public String toString()
    {
        return "BiLinearGradientPaint";
    }
}