package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public final class DisplayCircular extends AbstractRadial
{
    // Images used to combine layers for background and foreground
    private java.awt.image.BufferedImage bImage;
    private java.awt.image.BufferedImage fImage;
    
    private java.awt.image.BufferedImage disabledImage;
    private java.awt.Font lcdFormerValueFont;
    private double oldValue;    
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout oldValueLayout;
    private final java.awt.geom.Rectangle2D OLD_VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private final java.awt.geom.Rectangle2D LCD = new java.awt.geom.Rectangle2D.Double();    
                

    public DisplayCircular()
    {
        super();
        setLcdVisible(true);        
        oldValue = 0;        
        init(getInnerBounds().width, getInnerBounds().height);
    }

    @Override
    public final AbstractGauge init(final int WIDTH, final int HEIGHT)
    {   
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        // Create Background Image
        if (bImage != null)
        {
            bImage.flush();
        }
        bImage = UTIL.createImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        
        // Create Foreground Image
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

        create_TITLE_Image(WIDTH, getTitle(), getUnitString(), bImage);
                   
        create_LCD_Image(new java.awt.geom.Rectangle2D.Double(((getGaugeBounds().width - WIDTH * 0.6542056075) / 2.0), (getGaugeBounds().height * 0.425), (WIDTH * 0.6542056075), (WIDTH * 0.2990654206)), getLcdColor(), getCustomLcdBackground(), bImage);
        LCD.setRect(((getGaugeBounds().width - WIDTH * 0.4) / 2.0), (getGaugeBounds().height - (WIDTH * 0.2990654206)) / 2.0, WIDTH * 0.6542056075, WIDTH * 0.2990654206);
        
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
                
        if (bImage != null)
        {
            if (isDigitalFont())
            {
                setLcdValueFont(getModel().getDigitalBaseFont().deriveFont(0.5f * (WIDTH * 0.2990654206f)));    
                lcdFormerValueFont = getModel().getDigitalBaseFont().deriveFont(0.2f * (WIDTH * 0.2990654206f));
            }
            else
            {
                setLcdValueFont(getModel().getStandardBaseFont().deriveFont(0.46875f * (WIDTH * 0.2990654206f)));     
                lcdFormerValueFont = getModel().getStandardBaseFont().deriveFont(0.1875f * (WIDTH * 0.2990654206f));
            }

            if (isCustomLcdUnitFontEnabled())
            {
                setLcdUnitFont(getCustomLcdUnitFont().deriveFont(0.1875f * (WIDTH * 0.2990654206f)));
            }
            else
            {
                setLcdUnitFont(getModel().getStandardBaseFont().deriveFont(0.1875f * (WIDTH * 0.2990654206f)));
            }
        }

        return this;
    }

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

        // Draw LCD display
        if (isLcdVisible() && bImage != null)
        {            
            // Draw lcd text
            if (getLcdColor() == eu.hansolo.steelseries.tools.LcdColor.CUSTOM)
            {
                G2.setColor(getCustomLcdForeground());
            }
            else
            {
                G2.setColor(getLcdColor().TEXT_COLOR);
            }
            G2.setFont(getLcdUnitFont());            
            final double UNIT_STRING_WIDTH;
            if (isLcdUnitStringVisible() && !getLcdUnitString().isEmpty())
            {
                unitLayout = new java.awt.font.TextLayout(getLcdUnitString(), G2.getFont(), RENDER_CONTEXT);
                UNIT_BOUNDARY.setFrame(unitLayout.getBounds());
                //G2.drawString(getLcdUnitString(), (int) ((LCD.getWidth() - UNIT_BOUNDARY.getWidth()) - LCD.getWidth() * 0.03 + (getGaugeBounds().width - LCD.getWidth()) / 2.0), (int) (LCD.getHeight() * 0.6 + (getGaugeBounds().height - LCD.getHeight()) / 2.0));
                G2.drawString(getLcdUnitString(), (int) ((LCD.getWidth() - UNIT_BOUNDARY.getWidth()) - LCD.getWidth() * 0.03 + (getGaugeBounds().width - LCD.getWidth()) / 2.0), (int) (LCD.getHeight() * 0.6 + getGaugeBounds().height * 0.425));
                UNIT_STRING_WIDTH = UNIT_BOUNDARY.getWidth();
            }
            else
            {
                UNIT_STRING_WIDTH = 0;
            }

            // Draw value
            G2.setFont(getLcdValueFont());
            valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
            VALUE_BOUNDARY.setFrame(valueLayout.getBounds());
            //G2.drawString(formatLcdValue(getLcdValue()), (int) ((LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09 + ((getGaugeBounds().width - LCD.getWidth()) / 2.0)), (int) (LCD.getHeight() * 0.6 + (getGaugeBounds().height - LCD.getHeight()) / 2.0));
            G2.drawString(formatLcdValue(getLcdValue()), (int) ((LCD.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - LCD.getWidth() * 0.09 + ((getGaugeBounds().width - LCD.getWidth()) / 2.0)), (int) (LCD.getHeight() * 0.6 + getGaugeBounds().height * 0.425));

            // Draw oldValue
            G2.setFont(lcdFormerValueFont);
            oldValueLayout = new java.awt.font.TextLayout(formatLcdValue(oldValue), G2.getFont(), RENDER_CONTEXT);
            OLD_VALUE_BOUNDARY.setFrame(oldValueLayout.getBounds());
            //G2.drawString(formatLcdValue(oldValue), (int) ((LCD.getWidth() - OLD_VALUE_BOUNDARY.getWidth()) / 2.0 + (getGaugeBounds().width - LCD.getWidth()) / 2.0), (int) (LCD.getHeight() * 0.9 + (getGaugeBounds().height - LCD.getHeight()) / 2.0));
            G2.drawString(formatLcdValue(oldValue), (int) ((LCD.getWidth() - OLD_VALUE_BOUNDARY.getWidth()) / 2.0 + (getGaugeBounds().width - LCD.getWidth()) / 2.0), (int) (LCD.getHeight() * 0.9 + getGaugeBounds().height * 0.425));
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
    
    @Override
    public void setLcdValue(final double VALUE)
    {
        oldValue = getLcdValue();
        super.setLcdValue(VALUE);                        
    }
    
    @Override
    public void setLcdVisible(final boolean LCD_VISIBLE)
    {
        super.setLcdVisible(true);
    }
    
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

    @Override
    public String toString()
    {
        return "DisplayCircular";
    }
}
