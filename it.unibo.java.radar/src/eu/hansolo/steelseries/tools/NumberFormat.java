/*
 */
package eu.hansolo.steelseries.tools;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public enum NumberFormat
{
    AUTO("0"),
    STANDARD("0"),
    FRACTIONAL("0.0#"),
    SCIENTIFIC("0.##E0"),
    PERCENTAGE("##0.0%");
    
    
    private final java.text.DecimalFormat DF;
    
    private NumberFormat(final String FORMAT_STRING)
    {
        DF = new java.text.DecimalFormat(FORMAT_STRING);
    }
    
    public String format(final Number NUMBER)
    {
        return DF.format(NUMBER);
    }
}
