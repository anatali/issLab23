package eu.hansolo.steelseries.tools;


/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class GradientWrapper implements java.awt.Paint
{
    private final java.awt.LinearGradientPaint GRADIENT;
    private float[] fractions;
    private java.awt.Color[] colors;
    
    public GradientWrapper(float startX, float startY, float endX, float endY, float[] fractions, java.awt.Color[] colors)
    {
        GRADIENT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(startX, startY), new java.awt.geom.Point2D.Float(endX, endY), fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
        copyArrays(fractions, colors);
    }
    
    public GradientWrapper(float startX, float startY, float endX, float endY, float[] fractions, java.awt.Color[] colors, java.awt.MultipleGradientPaint.CycleMethod cycleMethod)
    {
        GRADIENT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(startX, startY), new java.awt.geom.Point2D.Float(endX, endY), fractions, colors, cycleMethod);
        copyArrays(fractions, colors);
    }

    public GradientWrapper(java.awt.geom.Point2D start, java.awt.geom.Point2D end, float[] fractions, java.awt.Color[] colors)
    {
        GRADIENT = new java.awt.LinearGradientPaint(start, end, fractions, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
        copyArrays(fractions, colors);
    }
    
    public GradientWrapper(java.awt.geom.Point2D start, java.awt.geom.Point2D end, float[] fractions, java.awt.Color[] colors, java.awt.MultipleGradientPaint.CycleMethod cycleMethod)
    {
        GRADIENT = new java.awt.LinearGradientPaint(start, end, fractions, colors, cycleMethod, java.awt.MultipleGradientPaint.ColorSpaceType.SRGB, new java.awt.geom.AffineTransform());
        copyArrays(fractions, colors);
    }
       
    public GradientWrapper(java.awt.geom.Point2D start, java.awt.geom.Point2D end, float[] fractions, java.awt.Color[] colors, java.awt.MultipleGradientPaint.CycleMethod cycleMethod,  java.awt.MultipleGradientPaint.ColorSpaceType colorSpace, java.awt.geom.AffineTransform gradientTransform)
    {
        GRADIENT = new java.awt.LinearGradientPaint(start, end, fractions, colors, cycleMethod, colorSpace, gradientTransform);
        copyArrays(fractions, colors);
    }
        
    /**
     * Returns the point where the wrapped java.awt.LinearGradientPaint will start
     * @return the point where the wrapped java.awt.LinearGradientPaint will start
     */
    public java.awt.geom.Point2D getStartPoint()
    {
        return GRADIENT.getStartPoint();
    }
    
    /**
     * Returns the point where the wrapped java.awt.LinearGradientPaint will stop
     * @return the point where the wrapped java.awt.LinearGradientPaint will stop
     */
    public java.awt.geom.Point2D getEndPoint()
    {
        return GRADIENT.getEndPoint();
    }
            
    /**
     * Returns the color that is defined by the given fraction in the linear gradient paint
     * @param FRACTION
     * @return the color that is defined by the given fraction in the linear gradient paint
     */
    public java.awt.Color getColorAt(final float FRACTION)
    {
        float fraction = FRACTION < 0f ? 0f : (FRACTION > 1f ? 1f : FRACTION);        
        float lowerLimit = 0f;        
        int lowerIndex = 0;
        float upperLimit = 1f;
        int upperIndex = 1;
        int index = 0;
        
        for (float currentFraction : fractions)
        {
            if (Float.compare(currentFraction, fraction) < 0)
            {
                lowerLimit = currentFraction;
                lowerIndex = index;
            }
            if (Float.compare(currentFraction, fraction) == 0)
            {
                return colors[index];
            }
            if (Float.compare(currentFraction, fraction) > 0)
            {
                upperLimit = currentFraction;
                upperIndex = index;
                break;
            }
            index++;
        }
        
        float interpolationFraction = (fraction - lowerLimit) / (upperLimit - lowerLimit);
        
        return interpolateColor(colors[lowerIndex], colors[upperIndex], interpolationFraction);
    }
    
    /**
     * Returns the wrapped java.awt.LinearGradientPaint
     * @return the wrapped java.awt.LinearGradientPaint
     */
    public java.awt.LinearGradientPaint getGradient()
    {
        return GRADIENT;
    }
    
    @Override
    public java.awt.PaintContext createContext(final java.awt.image.ColorModel COLOR_MODEL, final java.awt.Rectangle DEVICE_BOUNDS, java.awt.geom.Rectangle2D USER_BOUNDS, java.awt.geom.AffineTransform X_FORM, java.awt.RenderingHints HINTS)
    {
        return GRADIENT.createContext(COLOR_MODEL, DEVICE_BOUNDS, USER_BOUNDS, X_FORM, HINTS);
    }

    @Override
    public int getTransparency()
    {
        return GRADIENT.getTransparency();
    }
    
    /**
     * Just create a local copy of the fractions and colors array
     * @param fractions
     * @param colors 
     */
    private void copyArrays(final float[] fractions, final java.awt.Color[] colors)
    {
        this.fractions = new float[fractions.length]; 
        System.arraycopy( fractions, 0, this.fractions, 0, fractions.length );
        this.colors = colors.clone();
    }
    
    /**
     * Returns the interpolated color that you get if you multiply the delta between
     * color2 and color1 with the given fraction (for each channel) and interpolation. The fraction should
     * be a value between 0 and 1.
     * @param COLOR1 The first color as integer in the hex format 0xALPHA RED GREEN BLUE, e.g. 0xFF00FF00 for a pure green
     * @param COLOR2 The second color as integer in the hex format 0xALPHA RED GREEN BLUE e.g. 0xFFFF0000 for a pure red
     * @param FRACTION The fraction between those two colors that we would like to get e.g. 0.5f will result in the color 0xFF808000     
     * @return the interpolated color between color1 and color2 calculated by the given fraction and interpolation
     */
    private java.awt.Color interpolateColor(final java.awt.Color COLOR1, final java.awt.Color COLOR2, final float FRACTION)
    {
        assert(Float.compare(FRACTION, 0f) >= 0 && Float.compare(FRACTION, 1f) <= 0);
        
        final float INT_TO_FLOAT_CONST = 1f / 255f;
        
        final float RED1 = COLOR1.getRed() * INT_TO_FLOAT_CONST;
        final float GREEN1 = COLOR1.getGreen() * INT_TO_FLOAT_CONST;
        final float BLUE1 = COLOR1.getBlue() * INT_TO_FLOAT_CONST;
        final float ALPHA1 = COLOR1.getAlpha() * INT_TO_FLOAT_CONST;
        
        final float RED2 = COLOR2.getRed() * INT_TO_FLOAT_CONST;
        final float GREEN2 = COLOR2.getGreen() * INT_TO_FLOAT_CONST;
        final float BLUE2 = COLOR2.getBlue() * INT_TO_FLOAT_CONST;
        final float ALPHA2 = COLOR2.getAlpha() * INT_TO_FLOAT_CONST;
        
        final float DELTA_RED = RED2 - RED1;
        final float DELTA_GREEN = GREEN2 - GREEN1;
        final float DELTA_BLUE = BLUE2 - BLUE1;
        final float DELTA_ALPHA = ALPHA2 - ALPHA1;
        
        float red = RED1 + (DELTA_RED * FRACTION);
        float green = GREEN1 + (DELTA_GREEN * FRACTION);
        float blue = BLUE1 + (DELTA_BLUE * FRACTION);
        float alpha = ALPHA1 + (DELTA_ALPHA * FRACTION);
                
        red = red < 0f ? 0f : (red > 1f ? 1f : red);        
        green = green < 0f ? 0f : (green > 1f ? 1f : green);
        blue = blue < 0f ? 0f : (blue > 1f ? 1f : blue);       
        alpha = alpha < 0f ? 0f : (alpha > 1f ? 1f : alpha);   
        
        return new java.awt.Color(red, green, blue, alpha);        
    }    
}
