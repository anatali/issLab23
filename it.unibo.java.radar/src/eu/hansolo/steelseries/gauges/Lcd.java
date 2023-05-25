package eu.hansolo.steelseries.gauges;

/**
 *
 * @author hansolo
 */
public interface Lcd
{
    public boolean isValueCoupled();

    public void setValueCoupled(final boolean VALUE_COUPLED);
    
    public double getLcdValue();

    public void setLcdValue(final double VALUE);

    public void setLcdValueAnimated(final double VALUE);

    public int getLcdDecimals();

    public void setLcdDecimals(final int DECIMALS);

    public String getLcdUnitString();

    public void setLcdUnitString(final String UNIT);

    public boolean isLcdUnitStringVisible();

    public void setLcdUnitStringVisible(final boolean UNIT_STRING_VISIBLE);

    public boolean isCustomLcdUnitFontEnabled();

    public void setCustomLcdUnitFontEnabled(final boolean USE_CUSTOM_LCD_UNIT_FONT);

    public java.awt.Font getCustomLcdUnitFont();

    public void setCustomLcdUnitFont(final java.awt.Font CUSTOM_LCD_UNIT_FONT);

    public String getLcdInfoString();
    
    public void setLcdInfoString(final String INFO);
    
    public java.awt.Font getLcdInfoFont();
    
    public void setLcdInfoFont(final java.awt.Font LCD_INFO_FONT);
    
    public boolean isDigitalFont();

    public void setDigitalFont(final boolean DIGITAL_FONT);

    public eu.hansolo.steelseries.tools.LcdColor getLcdColor();

    public void setLcdColor(final eu.hansolo.steelseries.tools.LcdColor COLOR);

    public java.awt.Paint getCustomLcdBackground();
    
    public void setCustomLcdBackground(final java.awt.Paint CUSTOM_LCD_BACKGROUND);
    
    public java.awt.Color getCustomLcdForeground();
    
    public void setCustomLcdForeground(final java.awt.Color CUSTOM_LCD_FOREGROUND);
    
    public String formatLcdValue(final double VALUE);
    
    public boolean isLcdScientificFormat();

    public void setLcdScientificFormat(final boolean LCD_SCIENTIFIC_FORMAT);
    
    public java.awt.Font getLcdValueFont();
    
    public void setLcdValueFont(final java.awt.Font LCD_VALUE_FONT);
    
    public java.awt.Font getLcdUnitFont();
    
    public void setLcdUnitFont(final java.awt.Font LCD_UNIT_FONT);
    
    public eu.hansolo.steelseries.tools.NumberSystem getLcdNumberSystem();
    
    public void setLcdNumberSystem(final eu.hansolo.steelseries.tools.NumberSystem NUMBER_SYSTEM);
}
