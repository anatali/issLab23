package eu.hansolo.steelseries.tools;


/**
 * Definition of color combinations for gradients that will be used
 * as backgrounds for the components. So one could choose DARK_GRAY
 * in the ui editor and it will set the gradient with the colors
 * that are defined here for DARK_GRAY.
 * @author hansolo
 */
public enum BackgroundColor
{
    DARK_GRAY(new java.awt.Color(0, 0, 0, 255), new java.awt.Color(51, 51, 51, 255), new java.awt.Color(153, 153, 153, 255), java.awt.Color.WHITE, new java.awt.Color(180, 180, 180)),
    SATIN_GRAY(new java.awt.Color(45, 57, 57, 255), new java.awt.Color(45, 57, 57, 255), new java.awt.Color(45, 57, 57, 255), new java.awt.Color(167, 184, 180, 255), new java.awt.Color(137, 154, 150)),
    LIGHT_GRAY(new java.awt.Color(130, 130, 130, 255), new java.awt.Color(181, 181, 181, 255), new java.awt.Color(253, 253, 253, 255), java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    WHITE(java.awt.Color.WHITE, java.awt.Color.WHITE, java.awt.Color.WHITE, java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    BLACK(java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.WHITE, new java.awt.Color(180, 180, 180)),
    BEIGE(new java.awt.Color(178, 172, 150, 255), new java.awt.Color(204, 205, 184, 255), new java.awt.Color(231, 231, 214, 255), java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    BROWN(new java.awt.Color(245, 225, 193, 255), new java.awt.Color(245, 225, 193, 255), new java.awt.Color(255, 250, 240, 255), new java.awt.Color(109, 73, 47, 255), new java.awt.Color(89, 53, 27)),
    RED(new java.awt.Color(198, 93, 95, 255), new java.awt.Color(212, 132, 134, 255), new java.awt.Color(242, 218, 218, 255), java.awt.Color.BLACK, new java.awt.Color(90, 0, 0)),
    GREEN(new java.awt.Color(65, 120, 40, 255), new java.awt.Color(129, 171, 95, 255), new java.awt.Color(218, 237, 202, 255), java.awt.Color.BLACK, new java.awt.Color(0, 90, 0)),
    BLUE(new java.awt.Color(45, 83, 122, 255), new java.awt.Color(115, 144, 170, 255), new java.awt.Color(227, 234, 238, 255), java.awt.Color.BLACK, new java.awt.Color(0, 0, 90)),
    ANTHRACITE(new java.awt.Color(50, 50, 54, 255), new java.awt.Color(47, 47, 51, 255), new java.awt.Color(69, 69, 74, 255), new java.awt.Color(250, 250, 250, 255), new java.awt.Color(180, 180, 180)),
    MUD(new java.awt.Color(80, 86, 82, 255), new java.awt.Color(70, 76, 72, 255), new java.awt.Color(57, 62, 58, 255), new java.awt.Color(255, 255, 240, 255), new java.awt.Color(225, 225, 210)),    
    CARBON(new java.awt.Color(50, 50, 54, 255), new java.awt.Color(47, 47, 51, 255), new java.awt.Color(69, 69, 74, 255), java.awt.Color.WHITE, new java.awt.Color(180, 180, 180)),
    STAINLESS(java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    STAINLESS_GRINDED(new java.awt.Color(50, 50, 54, 255), new java.awt.Color(47, 47, 51, 255), new java.awt.Color(69, 69, 74, 255), java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    BRUSHED_METAL(new java.awt.Color(50, 50, 54, 255), new java.awt.Color(47, 47, 51, 255), new java.awt.Color(69, 69, 74, 255), java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    PUNCHED_SHEET(new java.awt.Color(50, 50, 54, 255), new java.awt.Color(47, 47, 51, 255), new java.awt.Color(69, 69, 74, 255), java.awt.Color.WHITE, new java.awt.Color(180, 180, 180)),
    TRANSPARENT(new java.awt.Color(0, 0, 0, 0), new java.awt.Color(0, 0, 0, 0), new java.awt.Color(0, 0, 0, 0), java.awt.Color.BLACK, new java.awt.Color(80, 80, 80)),
    CUSTOM(null, null, null, java.awt.Color.BLACK, new java.awt.Color(80, 80, 80));

    public final java.awt.Color GRADIENT_START_COLOR;
    public final java.awt.Color GRADIENT_FRACTION_COLOR;
    public final java.awt.Color GRADIENT_STOP_COLOR;
    public final java.awt.Color LABEL_COLOR;
    public final java.awt.Color SYMBOL_COLOR;

    private BackgroundColor(final java.awt.Color GRADIENT_START_COLOR, final java.awt.Color GRADIENT_FRACTION_COLOR, final java.awt.Color GRADIENT_STOP_COLOR, final java.awt.Color LABEL_COLOR, final java.awt.Color SYMBOL_COLOR)
    {
        this.GRADIENT_START_COLOR = GRADIENT_START_COLOR;
        this.GRADIENT_FRACTION_COLOR = GRADIENT_FRACTION_COLOR;
        this.GRADIENT_STOP_COLOR = GRADIENT_STOP_COLOR;
        this.LABEL_COLOR = LABEL_COLOR;
        this.SYMBOL_COLOR = SYMBOL_COLOR;
    }

}