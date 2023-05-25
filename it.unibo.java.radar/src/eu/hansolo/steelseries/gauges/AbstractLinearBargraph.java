package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public abstract class AbstractLinearBargraph extends AbstractLinear
{    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public AbstractLinearBargraph()
    {
        super();        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Value related (Getter/Setter)">    
    /**
     * Returns the enum colordef that is defined for the current bargraph
     * @return enum colordef that represents the current bargraph color
     */
    public eu.hansolo.steelseries.tools.ColorDef getBarGraphColor()
    {
        return getModel().getValueColor();
    }

    /**
     * Sets the current bargraph color to the given enum colordef
     * @param BARGRAPH_COLOR
     */
    public void setBarGraphColor(final eu.hansolo.steelseries.tools.ColorDef BARGRAPH_COLOR)
    {
        getModel().setValueColor(BARGRAPH_COLOR);
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    /**
     * Returns the color that will be used to calculate the custom bargraph color
     * @return the color that will be used to calculate the custom bargraph color
     */
    public java.awt.Color getCustomBargraphColor()
    {
        return getModel().getCustomValueColor();
    }
    
    /**
     * Sets the color that will be used to calculate the custom bargraph color
     * @param COLOR 
     */
    public void setCustomBarGraphColor(final java.awt.Color COLOR)
    {
        getModel().setCustomValueColorObject(new eu.hansolo.steelseries.tools.CustomColorDef(COLOR));
        init(getInnerBounds().width, getInnerBounds().width);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the object that represents holds the custom bargraph color
     * @return the object that represents the custom bargraph color
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomBarGraphColorObject()
    {
        return getModel().getCustomValueColorObject();
    }
    
    /**
     * Returns true if the peak value is visible
     * @return true if the park value is visible
     */
    public boolean isPeakValueEnabled()
    {
        return getModel().isPeakValueVisible();
    }

    /**
     * Enables/Disables the visibility of the peak value
     * @param PEAK_VALUE_ENABLED
     */
    public void setPeakValueEnabled(final boolean PEAK_VALUE_ENABLED)
    {
        getModel().setPeakValueVisible(PEAK_VALUE_ENABLED);
    }
    // </editor-fold>    
}
