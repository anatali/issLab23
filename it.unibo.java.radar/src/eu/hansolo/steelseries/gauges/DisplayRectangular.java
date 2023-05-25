package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public final class DisplayRectangular extends AbstractLinear
{        
    private java.awt.image.BufferedImage frameImage;
    private java.awt.image.BufferedImage backgroundImage;
    private java.awt.image.BufferedImage titleImage;
    private java.awt.image.BufferedImage lcdImage;
    private java.awt.image.BufferedImage foregroundImage;
    private java.awt.image.BufferedImage disabledImage;
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();

    
    public DisplayRectangular()
    {
        super();                 
        init(getInnerBounds().width, getInnerBounds().height);
    }

    @Override
    public final AbstractGauge init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return this;
        }
        
        if (isDigitalFont())
        {                            
            setLcdValueFont(LCD_DIGITAL_FONT.deriveFont(0.7f * (HEIGHT - 36)));            
        }
        else
        {
            setLcdValueFont(LCD_STANDARD_FONT.deriveFont(0.625f * (HEIGHT - 36)));       
        }

        if (isCustomLcdUnitFontEnabled())
        {
            setLcdUnitFont(getCustomLcdUnitFont().deriveFont(0.25f * (HEIGHT - 36)));
        }
        else
        {
            setLcdUnitFont(LCD_STANDARD_FONT.deriveFont(0.25f * (HEIGHT - 36)));
        }
        
        if (frameImage != null)
        {
            frameImage.flush();
        }
        frameImage = create_FRAME_Image(WIDTH, HEIGHT);

        if (backgroundImage != null)
        {
            backgroundImage.flush();
        }
        backgroundImage = create_BACKGROUND_Image(WIDTH, HEIGHT);

        if (titleImage != null)
        {
            titleImage.flush();
        }
        titleImage = create_TITLE_Image(WIDTH, HEIGHT, false);

        if (lcdImage != null)
        {
            lcdImage.flush();
        }                                                            
        lcdImage = create_LCD_Image(WIDTH - 36, HEIGHT - 36, getLcdColor(), getCustomLcdBackground());

        if (foregroundImage != null)
        {
            foregroundImage.flush();
        }
        foregroundImage = create_FOREGROUND_Image(WIDTH, HEIGHT);

        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH, HEIGHT);                    

        if (backgroundImage != null)
        {    
            backgroundImage.flush();
        }
        backgroundImage = create_BACKGROUND_Image(WIDTH, HEIGHT);   
        if (foregroundImage != null)
        {
            foregroundImage.flush();
        }                
        foregroundImage = create_FOREGROUND_Image(WIDTH, HEIGHT);
        if (disabledImage != null)
        {
            disabledImage.flush();
        }
        disabledImage = create_DISABLED_Image(WIDTH, HEIGHT);

        return this;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        if (!isInitialized())
        {
            return;
        }
        
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);

        // Draw the frame
        if (isFrameVisible())
        {
            G2.drawImage(frameImage, 0, 0, null);
        }
        
        // Draw the background
        if (isBackgroundVisible())
        {
            G2.drawImage(backgroundImage, 0, 0, null);
        }

        // Draw LCD display
        if (isLcdVisible())
        {
            G2.drawImage(lcdImage, 18, 18, null);
                        
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
            if (isLcdUnitStringVisible())
            {
                unitLayout = new java.awt.font.TextLayout(getLcdUnitString(), G2.getFont(), RENDER_CONTEXT);
                UNIT_BOUNDARY.setFrame(unitLayout.getBounds());
                G2.drawString(getLcdUnitString(), (int) ((lcdImage.getWidth() - UNIT_BOUNDARY.getWidth()) - lcdImage.getWidth() * 0.03f) + 18, (int) (lcdImage.getHeight() * 0.76f) + 18);                
                UNIT_STRING_WIDTH = UNIT_BOUNDARY.getWidth();
            }
            else
            {
                UNIT_STRING_WIDTH = 0;
            }
            G2.setFont(getLcdValueFont());
            valueLayout = new java.awt.font.TextLayout(formatLcdValue(getLcdValue()), G2.getFont(), RENDER_CONTEXT);
            VALUE_BOUNDARY.setFrame(valueLayout.getBounds());        
            G2.drawString(formatLcdValue(getLcdValue()), (int) ((lcdImage.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - lcdImage.getWidth() * 0.09) + 18, (int) (lcdImage.getHeight() * 0.76f) + 18);
        }
        
        // Draw the foreground
        if (isForegroundVisible())
        {
            G2.drawImage(foregroundImage, 0, 0, null);
        }
        
        if (!isEnabled())
        {
            G2.drawImage(disabledImage, 0, 0, null);
        }
        
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }

    @Override
    public boolean isLcdVisible()
    {
        return true;
    }
    
    @Override
    public void setValue(final double VALUE)
    {
        setLcdValue(VALUE);
    }

    @Override
    public double getValue()
    {
        return getLcdValue();
    }

    @Override
    public boolean isValueCoupled()
    {
        return false;
    }
    
    @Override
    public java.awt.geom.Point2D getCenter()
    {
        return new java.awt.geom.Point2D.Double(backgroundImage.getWidth() / 2.0 + getInnerBounds().x, backgroundImage.getHeight() / 2.0 + getInnerBounds().y);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return new java.awt.geom.Rectangle2D.Double(backgroundImage.getMinX(), backgroundImage.getMinY(), backgroundImage.getWidth(), backgroundImage.getHeight());
    }
    
    @Override
    public String toString()
    {
        return "DisplayRectangular";
    }
}
