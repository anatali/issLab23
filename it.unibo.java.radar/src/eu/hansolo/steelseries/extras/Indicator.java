package eu.hansolo.steelseries.extras;


/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class Indicator extends eu.hansolo.steelseries.gauges.AbstractRadial
{
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">   
    private static final eu.hansolo.steelseries.tools.SymbolImageFactory SYMBOL_FACTORY = eu.hansolo.steelseries.tools.SymbolImageFactory.INSTANCE;
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage symbolOnImage;
    private java.awt.image.BufferedImage symbolOffImage;
    private java.awt.image.BufferedImage fImage;    
    private java.awt.image.BufferedImage disabledImage;  
    private eu.hansolo.steelseries.tools.SymbolType symbolType = eu.hansolo.steelseries.tools.SymbolType.HORN;
    private eu.hansolo.steelseries.tools.ColorDef onColor = eu.hansolo.steelseries.tools.ColorDef.RED;
    private eu.hansolo.steelseries.tools.CustomColorDef customOnColor = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.RED);
    private eu.hansolo.steelseries.tools.ColorDef offColor = eu.hansolo.steelseries.tools.ColorDef.GRAY;
    private eu.hansolo.steelseries.tools.CustomColorDef customOffColor = new eu.hansolo.steelseries.tools.CustomColorDef(java.awt.Color.DARK_GRAY);
    private boolean on = false;
    private boolean glow = true;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Initialization">   
    public Indicator()
    {
        super();                
        init(getInnerBounds().width, getInnerBounds().height);
    }
    
    @Override
    public final eu.hansolo.steelseries.gauges.AbstractGauge init(int WIDTH, int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        // Create background image
        if (bImage != null)
        {
            bImage.flush();
        }
        bImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        
        // Create the symbol image
        if (symbolOnImage != null)
        {
            symbolOnImage.flush();
        }
        symbolOnImage = SYMBOL_FACTORY.createSymbol(WIDTH, symbolType, onColor, customOnColor, glow); 
        
        // Create the symbol image
        if (symbolOffImage != null)
        {
            symbolOffImage.flush();
        }
        symbolOffImage = SYMBOL_FACTORY.createSymbol(WIDTH, symbolType, offColor, customOffColor, false); 
        
        // Create foreground image
        if (fImage != null)
        {
            fImage.flush();
        }
        fImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        
        if (isFrameVisible())
        {
            switch (getFrameType())
            {
                case ROUND:
                    FRAME_FACTORY.createRadialFrame(WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);   
                    break;
                case SQUARE:
                    FRAME_FACTORY.createLinearFrame(WIDTH, WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);
                    break;
                default:
                    FRAME_FACTORY.createRadialFrame(WIDTH, getFrameDesign(), getCustomFrameDesign(), getFrameEffect(), bImage);
                    break;
            }
        }                                         

        if (isBackgroundVisible())
        {
            create_BACKGROUND_Image(WIDTH, "", "", bImage);
        }

        //create_TITLE_Image(WIDTH, getTitle(), getUnitString(), bImage);
                         
        if (isForegroundVisible())
        {            
            switch(getFrameType())
            {                
                case SQUARE:                    
                    FOREGROUND_FACTORY.createLinearForeground(WIDTH, WIDTH, false, bImage);
                    break;
                    
                case ROUND:
                    
                default:                    
                    FOREGROUND_FACTORY.createRadialForeground(WIDTH, false, getForegroundType(), fImage);
                    break;
            }
        }

        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH);                    
                        
        return this;
    }
    // </editor-fold>
    
    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Translate the coordinate system related to insets
        G2.translate(getInnerBounds().x, getInnerBounds().y);

        // Draw combined background image
        G2.drawImage(bImage, 0, 0, null);

        // Draw the symbol image
        if (on)
        {
            G2.drawImage(symbolOnImage, 0, 0, null);
        }   
        else
        {
            G2.drawImage(symbolOffImage, 0, 0, null);
        }
        
        // Draw combined foreground image
        G2.drawImage(fImage, 0, 0, null);
        
        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        // Translate the coordinate system back to original
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">   
    /**
     * Returns the type of symbol that will be drawn on the indicator
     * @return the type of symbol that will be drawn on the indicator
     */
    public eu.hansolo.steelseries.tools.SymbolType getSymbolType()
    {
        return symbolType;
    }
    
    /**
     * Sets the type of symbol that will be drawn on the indicator
     * @param SYMBOL_TYPE 
     */
    public void setSymbolType(final eu.hansolo.steelseries.tools.SymbolType SYMBOL_TYPE)
    {
        symbolType = SYMBOL_TYPE;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns true if the symbol is visualized with it's on color
     * @return true if the symbol is visualized with it's on color
     */
    public boolean isOn()
    {
        return on;
    }
    
    /**
     * Sets the symbol to on or off
     * @param ON 
     */
    public void setOn(final boolean ON)
    {
        on = ON;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the color definition that is used to visualize the on state of the symbol
     * @return the color definition that is used to visualize the on state of the symbol
     */
    public eu.hansolo.steelseries.tools.ColorDef getOnColor()
    {
        return onColor;
    }
    
    /**
     * Sets the color definition that is used to visualize the on state of the symbol
     * @param ON_COLOR 
     */
    public void setOnColor(final eu.hansolo.steelseries.tools.ColorDef ON_COLOR)
    {
        onColor = ON_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the custom color definition that is used to visualize the on state of the symbol
     * @return the custom color definition that is used to visualize the on state of the symbol
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomOnColor()
    {
        return customOnColor;
    }
    
    /**
     * Sets the custom color definition that will be used to visualize the on state of the symbol
     * @param CUSTOM_ON_COLOR 
     */
    public void setCustomOnColor(final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_ON_COLOR)
    {
        customOnColor = CUSTOM_ON_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the color definition that is used to visualize the off state of the symbol
     * @return the color definition that is used to visualize the off state of the symbol
     */
    public eu.hansolo.steelseries.tools.ColorDef getOffColor()
    {
        return offColor;
    }
    
    /**
     * Sets the color definition that will be used to visualize the off state of the symbol
     * @param OFF_COLOR 
     */
    public void setOffColor(final eu.hansolo.steelseries.tools.ColorDef OFF_COLOR)
    {
        offColor = OFF_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns the custom color definition that is used to visualize the off state of the symbol
     * @return the custom color definition that is used to visualize the off state of the symbol
     */
    public eu.hansolo.steelseries.tools.CustomColorDef getCustomOffColor()
    {
        return customOffColor;
    }
    
    /**
     * Sets the custom color definition that is used to visualize the off state of the symbol
     * @param CUSTOM_OFF_COLOR 
     */
    public void setCustomOffColor(final eu.hansolo.steelseries.tools.CustomColorDef CUSTOM_OFF_COLOR)
    {
        customOffColor = CUSTOM_OFF_COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    /**
     * Returns true if a glow effect will be applied to the on state of the symbol
     * @return true if a glow effect will be applied to the on state of the symbol
     */
    public boolean isGlow()
    {
        return glow;
    }
    
    /**
     * Enables / disables the glow effect to the on state of the symbol
     * @param GLOW 
     */
    public void setGlow(final boolean GLOW)
    {
        glow = GLOW;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    // </editor-fold>
    
    @Override
    public java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(bImage.getWidth() / 2.0 + getInnerBounds().x, bImage.getHeight() / 2.0 + getInnerBounds().y);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return new java.awt.geom.Rectangle2D.Double(bImage.getMinX(), bImage.getMinY(), bImage.getWidth(), bImage.getHeight());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Size related methods">    
    @Override
    public java.awt.Dimension getMinimumSize()
    {        
        return new java.awt.Dimension(50, 50);
    }        
    // </editor-fold>
        
    @Override
    public String toString()
    {
        return "Indicator";
    }
}
