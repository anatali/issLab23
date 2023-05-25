package eu.hansolo.steelseries.gauges;


/**
 *
 * @author hansolo
 */
public final class DisplayMulti extends javax.swing.JComponent implements Lcd
{
    // <editor-fold defaultstate="collapsed" desc="Variable declaration">
    private final eu.hansolo.steelseries.tools.Util UTIL = eu.hansolo.steelseries.tools.Util.INSTANCE;
    private eu.hansolo.steelseries.tools.LcdColor lcdColor = eu.hansolo.steelseries.tools.LcdColor.WHITE_LCD;
    private java.awt.Paint customLcdBackground = java.awt.Color.BLACK;
    private java.awt.Color customLcdForeground = java.awt.Color.WHITE;
    private final java.awt.Rectangle INNER_BOUNDS = new java.awt.Rectangle(0, 0, 128, 64);
    private double value;
    private double oldValue;
    private static final String VALUE_PROPERTY = "value";
    private int lcdDecimals;
    private String lcdUnitString;
    private boolean lcdUnitStringVisible;
    private boolean lcdScientificFormat;
    private boolean digitalFont;
    private boolean useCustomLcdUnitFont;
    private java.awt.Font customLcdUnitFont;
    private java.awt.Font lcdValueFont;
    private java.awt.Font lcdFormerValueFont;
    private java.awt.Font lcdUnitFont;
    private java.awt.Font lcdInfoFont;
    private String lcdInfoString;
    private final java.awt.Font LCD_STANDARD_FONT;
    private final java.awt.Font LCD_DIGITAL_FONT;    
    private java.awt.image.BufferedImage lcdImage;
    private eu.hansolo.steelseries.tools.NumberSystem numberSystem;
    private java.awt.Shape disabledShape;
    private final java.awt.Color DISABLED_COLOR;
    private org.pushingpixels.trident.Timeline timeline;
    private final transient org.pushingpixels.trident.ease.TimelineEase EASING;
    private final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
    private java.awt.font.TextLayout unitLayout;
    private final java.awt.geom.Rectangle2D UNIT_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout valueLayout;
    private final java.awt.geom.Rectangle2D VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout oldValueLayout;
    private final java.awt.geom.Rectangle2D OLD_VALUE_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private java.awt.font.TextLayout infoLayout;
    private final java.awt.geom.Rectangle2D INFO_BOUNDARY = new java.awt.geom.Rectangle2D.Double();
    private final transient java.awt.event.ComponentListener COMPONENT_LISTENER = new java.awt.event.ComponentAdapter() 
    {
        @Override
        public void componentResized(java.awt.event.ComponentEvent event)
        {
            java.awt.Container parent = getParent();            
            if (getWidth() < getMinimumSize().width && getHeight() < getMinimumSize().height)
            {
                if (parent != null && getParent().getLayout() == null)
                {
                    setSize(getMinimumSize());                
                }
                else
                {
                    setPreferredSize(getMinimumSize());
                }            
            }

            if (parent != null && getParent().getLayout() == null)
            {
                setSize(getWidth(), getHeight());
            }
            else
            {
                setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
            }

            calcInnerBounds();
            init(getInnerBounds().width, getInnerBounds().height);     
            revalidate();   
            repaint();
        }
    };
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public DisplayMulti()
    {
        super();
        addComponentListener(COMPONENT_LISTENER); 
        value = 0.0;
        oldValue = 0.0;
        lcdDecimals = 1;
        lcdUnitString = "unit";
        lcdUnitStringVisible =  true;
        lcdScientificFormat = false;
        digitalFont = false;
        useCustomLcdUnitFont = false;
        customLcdUnitFont = new java.awt.Font("Verdana", 1, 24);
        LCD_STANDARD_FONT = new java.awt.Font("Verdana", 1, 30);
        LCD_DIGITAL_FONT = eu.hansolo.steelseries.tools.Util.INSTANCE.getDigitalFont().deriveFont(24).deriveFont(java.awt.Font.PLAIN);
        lcdInfoFont = new java.awt.Font("Verdana", 0, 24);
        lcdInfoString = "";
        numberSystem = eu.hansolo.steelseries.tools.NumberSystem.DEC;
        DISABLED_COLOR = new java.awt.Color(102, 102, 102, 178);
        timeline = new org.pushingpixels.trident.Timeline(this);
        EASING = new org.pushingpixels.trident.ease.Linear();
        init(128, 64);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Initialization">
    public final void init(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 1 || HEIGHT <= 1)
        {
            return;
        }
        
        if (lcdImage != null)
        {
            lcdImage.flush();
        }
        lcdImage = create_LCD_Image(WIDTH, HEIGHT);                
        disabledShape = new java.awt.geom.RoundRectangle2D.Double(0, 0, WIDTH, HEIGHT, WIDTH * 0.09375, WIDTH * 0.09375);
        if (isDigitalFont())
        {
            lcdValueFont = LCD_DIGITAL_FONT.deriveFont(0.5f * getInnerBounds().height);
            lcdFormerValueFont = LCD_DIGITAL_FONT.deriveFont(0.2f * getInnerBounds().height);
            if (useCustomLcdUnitFont)
            {
                lcdUnitFont = customLcdUnitFont.deriveFont(0.1875f * getInnerBounds().height);
            }
            else
            {
                lcdUnitFont = LCD_STANDARD_FONT.deriveFont(0.1875f * getInnerBounds().height);
            }
        }
        else
        {
            lcdValueFont = LCD_STANDARD_FONT.deriveFont(0.46875f * getInnerBounds().height);
            lcdFormerValueFont = LCD_STANDARD_FONT.deriveFont(0.1875f * getInnerBounds().height);
            if (useCustomLcdUnitFont)
            {
                lcdUnitFont = customLcdUnitFont.deriveFont(0.1875f * getInnerBounds().height);
            }
            else
            {
                lcdUnitFont = LCD_STANDARD_FONT.deriveFont(0.1875f * getInnerBounds().height);
            }
        }
        lcdInfoFont = LCD_STANDARD_FONT.deriveFont(0.15f * getInnerBounds().height);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Visualization">
    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        G2.translate(getInnerBounds().x, getInnerBounds().y);

        G2.drawImage(lcdImage, 0, 0, null);

        // Draw lcd text
        if (lcdColor == eu.hansolo.steelseries.tools.LcdColor.CUSTOM)
        {
            G2.setColor(customLcdForeground);
        }
        else
        {
            G2.setColor(lcdColor.TEXT_COLOR);
        }
        G2.setFont(lcdUnitFont);        
        final double UNIT_STRING_WIDTH;
        if (lcdUnitStringVisible && !lcdUnitString.isEmpty())
        {
            unitLayout = new java.awt.font.TextLayout(lcdUnitString, G2.getFont(), RENDER_CONTEXT);
            UNIT_BOUNDARY.setFrame(unitLayout.getBounds());
            G2.drawString(lcdUnitString, (int) ((lcdImage.getWidth() - UNIT_BOUNDARY.getWidth()) - lcdImage.getWidth() * 0.03f), (int) (lcdImage.getHeight() * 0.6f));
            UNIT_STRING_WIDTH = UNIT_BOUNDARY.getWidth();
        }
        else
        {
            UNIT_STRING_WIDTH = 0;
        }

        // Draw value and oldValue        
        switch(numberSystem)
        {
            case HEX:
                G2.setFont(lcdValueFont);
                valueLayout = new java.awt.font.TextLayout(Integer.toHexString((int) value).toUpperCase(), G2.getFont(), RENDER_CONTEXT);
                VALUE_BOUNDARY.setFrame(valueLayout.getBounds());
                G2.drawString(Integer.toHexString((int) value).toUpperCase(), (int) ((lcdImage.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - lcdImage.getWidth() * 0.09), (int) (lcdImage.getHeight() * 0.6f));        
                
                G2.setFont(lcdFormerValueFont);        
                oldValueLayout = new java.awt.font.TextLayout(Integer.toHexString((int) oldValue).toUpperCase(), G2.getFont(), RENDER_CONTEXT);
                OLD_VALUE_BOUNDARY.setFrame(oldValueLayout.getBounds());
                G2.drawString(Integer.toHexString((int) oldValue).toUpperCase(), (int) ((lcdImage.getWidth() - OLD_VALUE_BOUNDARY.getWidth()) / 2f), (int) (lcdImage.getHeight() * 0.9f));
                break;
                
            case OCT:
                G2.setFont(lcdValueFont);
                valueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) value), G2.getFont(), RENDER_CONTEXT);
                VALUE_BOUNDARY.setFrame(valueLayout.getBounds());
                G2.drawString(Integer.toOctalString((int) value), (int) ((lcdImage.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - lcdImage.getWidth() * 0.09), (int) (lcdImage.getHeight() * 0.6f));        
                
                G2.setFont(lcdFormerValueFont);        
                oldValueLayout = new java.awt.font.TextLayout(Integer.toOctalString((int) oldValue), G2.getFont(), RENDER_CONTEXT);
                OLD_VALUE_BOUNDARY.setFrame(oldValueLayout.getBounds());
                G2.drawString(Integer.toOctalString((int) oldValue), (int) ((lcdImage.getWidth() - OLD_VALUE_BOUNDARY.getWidth()) / 2f), (int) (lcdImage.getHeight() * 0.9f));                
                break;
                
            case DEC:
                
            default:
                G2.setFont(lcdValueFont);
                valueLayout = new java.awt.font.TextLayout(formatLcdValue(value), G2.getFont(), RENDER_CONTEXT);
                VALUE_BOUNDARY.setFrame(valueLayout.getBounds());
                G2.drawString(formatLcdValue(value), (int) ((lcdImage.getWidth() - UNIT_STRING_WIDTH - VALUE_BOUNDARY.getWidth()) - lcdImage.getWidth() * 0.09), (int) (lcdImage.getHeight() * 0.6f));        
                
                G2.setFont(lcdFormerValueFont);        
                oldValueLayout = new java.awt.font.TextLayout(formatLcdValue(oldValue), G2.getFont(), RENDER_CONTEXT);
                OLD_VALUE_BOUNDARY.setFrame(oldValueLayout.getBounds());
                G2.drawString(formatLcdValue(oldValue), (int) ((lcdImage.getWidth() - OLD_VALUE_BOUNDARY.getWidth()) / 2f), (int) (lcdImage.getHeight() * 0.9f));
                break;
        }                
        
        // Draw lcd info string
        if (!lcdInfoString.isEmpty())
        {
            G2.setFont(lcdInfoFont);
            infoLayout = new java.awt.font.TextLayout(lcdInfoString, G2.getFont(), RENDER_CONTEXT);
            INFO_BOUNDARY.setFrame(infoLayout.getBounds());
            G2.drawString(lcdInfoString, 5, (int) INFO_BOUNDARY.getHeight() + 5);
        }
        
        if (!isEnabled())
        {
            G2.setColor(DISABLED_COLOR);
            G2.fill(disabledShape);
        }
        
        G2.translate(-getInnerBounds().x, -getInnerBounds().y);

        G2.dispose();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Returns the current component as buffered image.
     * To save this buffered image as png you could use for example:
     * File file = new File("image.png");
     * ImageIO.write(Image, "png", file);
     * @return the current component as buffered image
     */
    public java.awt.image.BufferedImage getAsImage()
    {
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(getWidth(), getHeight(), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        paintAll(G2);
        G2.dispose();
        return IMAGE;
    }
    
    @Override
    public double getLcdValue()
    {
        return this.value;
    }

    @Override
    public void setLcdValue(final double VALUE)
    {
        oldValue = this.value;
        this.value = VALUE;
        firePropertyChange(VALUE_PROPERTY, oldValue, VALUE);
        repaint(getInnerBounds());
    }

    @Override
    public void setLcdValueAnimated(final double VALUE)
    {        
        if (isEnabled())
        {
            if (timeline.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
            {
                timeline.abort();
            }
            timeline = new org.pushingpixels.trident.Timeline(this);
            timeline.addPropertyToInterpolate("value", this.value, VALUE);
            timeline.setEase(EASING);
            timeline.setDuration((long) (2000));
            timeline.play();     
        }
    }

    @Override
    public int getLcdDecimals()
    {
        return this.lcdDecimals;
    }

    @Override
    public void setLcdDecimals(final int DECIMALS)
    {
        this.lcdDecimals = DECIMALS;
        repaint(getInnerBounds());
    }

    @Override
    public String getLcdUnitString()
    {
        return this.lcdUnitString;
    }

    @Override
    public void setLcdUnitString(final String LCD_UNIT_STRING)
    {
        this.lcdUnitString = LCD_UNIT_STRING;
        repaint(getInnerBounds());
    }

    @Override
    public boolean isLcdUnitStringVisible()
    {
        return this.lcdUnitStringVisible;
    }

    @Override
    public void setLcdUnitStringVisible(final boolean LCD_UNIT_STRING_VISIBLE)
    {
        this.lcdUnitStringVisible = LCD_UNIT_STRING_VISIBLE;
        repaint(getInnerBounds());
    }

    @Override
    public boolean isCustomLcdUnitFontEnabled()
    {
        return this.useCustomLcdUnitFont;
    }

    @Override
    public void setCustomLcdUnitFontEnabled(final boolean USE_CUSTOM_LCD_UNIT_FONT)
    {
        this.useCustomLcdUnitFont = USE_CUSTOM_LCD_UNIT_FONT;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public java.awt.Font getCustomLcdUnitFont()
    {
        return this.customLcdUnitFont;
    }

    @Override
    public void setCustomLcdUnitFont(final java.awt.Font CUSTOM_LCD_UNIT_FONT)
    {
        this.customLcdUnitFont = CUSTOM_LCD_UNIT_FONT;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public void setLcdScientificFormat(final boolean LCD_SCIENTIFIC_FORMAT)
    {
        this.lcdScientificFormat = LCD_SCIENTIFIC_FORMAT;
    }

    @Override
    public boolean isLcdScientificFormat()
    {
        return lcdScientificFormat;
    }

    @Override
    public boolean isDigitalFont()
    {
        return this.digitalFont;
    }

    @Override
    public void setDigitalFont(final boolean DIGITAL_FONT)
    {
        this.digitalFont = DIGITAL_FONT;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public eu.hansolo.steelseries.tools.LcdColor getLcdColor()
    {
        return this.lcdColor;
    }

    @Override
    public void setLcdColor(final eu.hansolo.steelseries.tools.LcdColor COLOR)
    {
        this.lcdColor = COLOR;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }

    @Override
    public java.awt.Paint getCustomLcdBackground()
    {
        return this.customLcdBackground;
    }
    
    @Override
    public void setCustomLcdBackground(final java.awt.Paint CUSTOM_LCD_BACKGROUND)
    {
        this.customLcdBackground = CUSTOM_LCD_BACKGROUND;        
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    @Override
    public java.awt.Color getCustomLcdForeground()
    {
        return this.customLcdForeground;
    }
    
    @Override
    public void setCustomLcdForeground(final java.awt.Color CUSTOM_LCD_FOREGROUND)
    {
        this.customLcdForeground = CUSTOM_LCD_FOREGROUND;
        init(getInnerBounds().width, getInnerBounds().height);
        repaint(getInnerBounds());
    }
    
    @Override
    public String formatLcdValue(final double VALUE)
    {
        final StringBuilder DEC_BUFFER = new StringBuilder(16);
        DEC_BUFFER.append("0");

        if (this.lcdDecimals > 0)
        {
            DEC_BUFFER.append(".");
        }

        for (int i = 0; i < this.lcdDecimals; i++)
        {
            DEC_BUFFER.append("0");
        }

        if(lcdScientificFormat)
        {
            DEC_BUFFER.append("E0");
        }
        DEC_BUFFER.trimToSize();
        
        final java.text.DecimalFormat DEC_FORMAT = new java.text.DecimalFormat(DEC_BUFFER.toString(), new java.text.DecimalFormatSymbols(java.util.Locale.US));

        return DEC_FORMAT.format(VALUE);
    }

    @Override
    public boolean isValueCoupled()
    {
        return false;
    }

    @Override
    public void setValueCoupled(boolean VALUE_COUPLED)
    {
        
    }

    @Override
    public java.awt.Font getLcdValueFont()
    {
        return this.lcdValueFont;
    }

    @Override
    public void setLcdValueFont(java.awt.Font LCD_VALUE_FONT)
    {
        this.lcdValueFont = LCD_VALUE_FONT;
        repaint(getInnerBounds());
    }

    @Override
    public java.awt.Font getLcdUnitFont()
    {
        return this.lcdUnitFont;
    }

    @Override
    public void setLcdUnitFont(java.awt.Font LCD_UNIT_FONT)
    {
        this.lcdUnitFont = LCD_UNIT_FONT;
        repaint(getInnerBounds());
    }
    
    @Override
    public String getLcdInfoString()
    {
        return lcdInfoString;
    }
    
    @Override
    public void setLcdInfoString(final String LCD_INFO_STRING)
    {
        lcdInfoString = LCD_INFO_STRING;
        repaint(getInnerBounds());
    }
    
    @Override
    public java.awt.Font getLcdInfoFont()
    {
        return lcdInfoFont;
    }
    
    @Override
    public void setLcdInfoFont(final java.awt.Font LCD_INFO_FONT)
    {
        lcdInfoFont = LCD_INFO_FONT;
        repaint(getInnerBounds());
    }
    
    @Override
    public eu.hansolo.steelseries.tools.NumberSystem getLcdNumberSystem()
    {
        return numberSystem;
    }
    
    @Override
    public void setLcdNumberSystem(final eu.hansolo.steelseries.tools.NumberSystem NUMBER_SYSTEM)
    {
        numberSystem = NUMBER_SYSTEM;
        switch (NUMBER_SYSTEM)
        {
            case HEX:
                lcdInfoString = "hex";
                break;
            case OCT:
                lcdInfoString = "oct";
                break;
            case DEC:
                
            default:
                lcdInfoString = "";
                break;
        }
        repaint(getInnerBounds());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Image related">
    private java.awt.image.BufferedImage create_LCD_Image(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }
        
        final java.awt.image.BufferedImage IMAGE = UTIL.createImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        // Background rectangle
        final java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Double(0.0, 0.0);
        final java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0.0, IMAGE_HEIGHT);
        if (BACKGROUND_START.equals(BACKGROUND_STOP))
        {
            BACKGROUND_STOP.setLocation(0.0, BACKGROUND_START.getY() + 1);
        } 
        
        final float[] BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.08f,
            0.92f,
            1.0f
        };

        final java.awt.Color[] BACKGROUND_COLORS =
        {
            new java.awt.Color(0.4f, 0.4f, 0.4f, 1.0f),
            new java.awt.Color(0.5f, 0.5f, 0.5f, 1.0f),
            new java.awt.Color(0.5f, 0.5f, 0.5f, 1.0f),
            new java.awt.Color(0.9f, 0.9f, 0.9f, 1.0f)
        };

        final java.awt.LinearGradientPaint BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);
        final double BACKGROUND_CORNER_RADIUS = WIDTH * 0.09375;
        final java.awt.geom.RoundRectangle2D BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, BACKGROUND_CORNER_RADIUS, BACKGROUND_CORNER_RADIUS);
        G2.setPaint(BACKGROUND_GRADIENT);
        G2.fill(BACKGROUND);

        // Foreground rectangle
        final java.awt.geom.Point2D FOREGROUND_START = new java.awt.geom.Point2D.Double(0.0, 1.0);
        final java.awt.geom.Point2D FOREGROUND_STOP = new java.awt.geom.Point2D.Double(0.0, IMAGE_HEIGHT - 1);
        if (FOREGROUND_START.equals(FOREGROUND_STOP))
        {
            FOREGROUND_STOP.setLocation(0.0, FOREGROUND_START.getY() + 1);
        } 
        
        final float[] FOREGROUND_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.49f,
            0.5f,
            1.0f
        };

        final java.awt.Color[] FOREGROUND_COLORS =
        {
            lcdColor.GRADIENT_START_COLOR,
            lcdColor.GRADIENT_FRACTION1_COLOR,
            lcdColor.GRADIENT_FRACTION2_COLOR,
            lcdColor.GRADIENT_FRACTION3_COLOR,
            lcdColor.GRADIENT_STOP_COLOR
        };
        
        if (lcdColor == eu.hansolo.steelseries.tools.LcdColor.CUSTOM)
        {
            G2.setPaint(customLcdBackground);
        }
        else
        {
            final java.awt.LinearGradientPaint FOREGROUND_GRADIENT = new java.awt.LinearGradientPaint(FOREGROUND_START, FOREGROUND_STOP, FOREGROUND_FRACTIONS, FOREGROUND_COLORS);        
            G2.setPaint(FOREGROUND_GRADIENT);
        }
        final double FOREGROUND_CORNER_RADIUS = BACKGROUND.getArcWidth() - 1;
        final java.awt.geom.RoundRectangle2D FOREGROUND = new java.awt.geom.RoundRectangle2D.Double(1, 1, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, FOREGROUND_CORNER_RADIUS, FOREGROUND_CORNER_RADIUS);
        G2.fill(FOREGROUND);

        G2.dispose();

        return IMAGE;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Size related">
    /**
     * Calculates the area that is available for painting the display
     */
    private void calcInnerBounds()
    {
        final java.awt.Insets INSETS = getInsets();
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, getWidth() - INSETS.left - INSETS.right, getHeight() - INSETS.top - INSETS.bottom);
    }

    /**
     * Returns a rectangle2d representing the available space for drawing the
     * component taking the insets into account (e.g. given through borders etc.)
     * @return rectangle2d that represents the area available for rendering the component
     */
    private java.awt.Rectangle getInnerBounds()
    {               
        return INNER_BOUNDS;
    }

    @Override
    public java.awt.Dimension getMinimumSize()
    {        
        return new java.awt.Dimension(128, 64);
    }
    
    @Override
    public void setPreferredSize(final java.awt.Dimension DIM)
    {        
        super.setPreferredSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);        
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final int WIDTH, final int HEIGHT)
    {
        super.setSize(WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);        
        revalidate();
        repaint();
    }
    
    @Override
    public void setSize(final java.awt.Dimension DIM)
    {
        super.setSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);        
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final java.awt.Rectangle BOUNDS)
    {
        super.setBounds(BOUNDS);
        calcInnerBounds();
        init(BOUNDS.width, BOUNDS.height);        
        revalidate();
        repaint();
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT)
    {
        super.setBounds(X, Y, WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);
        revalidate();
        repaint();
    }
    // </editor-fold>
    
    @Override
    public String toString()
    {
        return "DisplayMulti";
    }
}
