package eu.hansolo.steelseries.tools;

/**
 *
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class CustomColorDef 
{    
    public final java.awt.Color COLOR;
    public final java.awt.Color VERY_DARK;
    public final java.awt.Color DARK;
    public final java.awt.Color MEDIUM;
    public final java.awt.Color LIGHT;
    public final java.awt.Color LIGHTER;
    public final java.awt.Color VERY_LIGHT;
    
    public CustomColorDef(final java.awt.Color COLOR)
    {        
        this.COLOR = COLOR;
        final float HUE = java.awt.Color.RGBtoHSB(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), null)[0];
        if (COLOR.getRed() == COLOR.getGreen() && COLOR.getRed() == COLOR.getBlue())
        {            
            VERY_DARK = java.awt.Color.getHSBColor(HUE, 0.0f, 0.32f);
            DARK = java.awt.Color.getHSBColor(HUE, 0.0f, 0.62f);
            MEDIUM = java.awt.Color.getHSBColor(HUE, 0.0f, 0.84f);
            LIGHT = java.awt.Color.getHSBColor(HUE, 0.0f, 0.94f);
            LIGHTER = java.awt.Color.getHSBColor(HUE, 0.0f, 1.0f);
            VERY_LIGHT = java.awt.Color.getHSBColor(HUE, 0.0f, 1.0f);
        }
        else
        {
            VERY_DARK = java.awt.Color.getHSBColor(HUE, 1.0f, 0.32f);
            DARK = java.awt.Color.getHSBColor(HUE, 1.0f, 0.62f);
            MEDIUM = java.awt.Color.getHSBColor(HUE, 1.0f, 0.84f);
            LIGHT = java.awt.Color.getHSBColor(HUE, 0.65f, 0.94f);
            LIGHTER = java.awt.Color.getHSBColor(HUE, 0.33f, 1.0f);
            VERY_LIGHT = java.awt.Color.getHSBColor(HUE, 0.15f, 1.0f);
        }
    }
}
