package eu.hansolo.steelseries.tools;


/**
 * Color definitions for different LCD designs.
 * Some of the colors are taken from images of
 * real lcd's which really leads to a more realistic
 * look of the lcd display.
 * @author hansolo
 */
public enum LcdColor
{
    BEIGE_LCD(
        new java.awt.Color(200, 200, 177, 255),
        new java.awt.Color(241, 237, 207, 255),
        new java.awt.Color(234, 230, 194, 255),
        new java.awt.Color(225, 220, 183, 255),
        new java.awt.Color(237, 232, 191, 255),
        java.awt.Color.BLACK),
    BLUE_LCD(
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(231, 246, 255, 255),
        new java.awt.Color(170, 224, 255, 255),
        new java.awt.Color(136, 212, 255, 255),
        new java.awt.Color(192, 232, 255, 255),
        new java.awt.Color(0x124564)),
    ORANGE_LCD(
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(255, 245, 225, 255),
        new java.awt.Color(255, 217, 147, 255),
        new java.awt.Color(255, 201, 104, 255),
        new java.awt.Color(255, 227, 173, 255),
        new java.awt.Color(0x503700)),
    RED_LCD(
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(255, 225, 225, 255),
        new java.awt.Color(253, 152, 152, 255),
        new java.awt.Color(252, 114, 115, 255),
        new java.awt.Color(254, 178, 178, 255),
        new java.awt.Color(0x4F0C0E)),
    YELLOW_LCD(
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(245, 255, 186, 255),
        new java.awt.Color(210, 255, 0, 255),
        new java.awt.Color(158, 205, 0, 255),
        new java.awt.Color(210, 255, 0, 255),
        new java.awt.Color(0x405300)),
    WHITE_LCD(
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(241, 246, 242, 255),
        new java.awt.Color(229, 239, 244, 255),
        new java.awt.Color(255, 255, 255, 255),
        java.awt.Color.BLACK),
    GRAY_LCD(
        new java.awt.Color(65, 65, 65, 255),
        new java.awt.Color(117, 117, 117, 255),
        new java.awt.Color(87, 87, 87, 255),
        new java.awt.Color(65, 65, 65, 255),
        new java.awt.Color(81, 81, 81, 255),
        java.awt.Color.WHITE),
    BLACK_LCD(
        new java.awt.Color(65, 65, 65, 255),
        new java.awt.Color(102, 102, 102, 255),
        new java.awt.Color(51, 51, 51, 255),
        new java.awt.Color(0, 0, 0, 255),
        new java.awt.Color(51, 51, 51, 255),
        new java.awt.Color(0xCCCCCC)),
    GREEN_LCD(
        new java.awt.Color(33, 67, 67, 255),
        new java.awt.Color(33, 67, 67, 255),
        new java.awt.Color(29, 58, 58, 255),
        new java.awt.Color(28, 57, 57, 255),
        new java.awt.Color(23, 46, 46, 255),
        new java.awt.Color(0, 185, 165, 255)),
    BLUE2_LCD(
        new java.awt.Color(0, 68, 103, 255),
        new java.awt.Color(8, 109, 165, 255),
        new java.awt.Color(0, 72, 117, 255),
        new java.awt.Color(0, 72, 117, 255),
        new java.awt.Color(0, 68, 103, 255),
        new java.awt.Color(111, 182, 228, 255)),
    BLUEBLACK_LCD(
        new java.awt.Color(22, 125, 212, 255),
        new java.awt.Color(3, 162, 254, 255),
        new java.awt.Color(3, 162, 254, 255),
        new java.awt.Color(3, 162, 254, 255),
        new java.awt.Color(11, 172, 244, 255),
        java.awt.Color.BLACK),
    BLUEDARKBLUE_LCD(
        new java.awt.Color(18, 33, 88, 255),
        new java.awt.Color(18, 33, 88, 255),
        new java.awt.Color(19, 30, 90, 255),
        new java.awt.Color(17, 31, 94, 255),
        new java.awt.Color(21, 25, 90, 255),
        new java.awt.Color(23, 99, 221, 255)),
    BLUELIGHTBLUE_LCD(
        new java.awt.Color(88, 107, 132, 255),
        new java.awt.Color(53, 74, 104, 255),
        new java.awt.Color(27, 37, 65, 255),
        new java.awt.Color(5, 12, 40, 255),
        new java.awt.Color(32, 47, 79, 255),
        new java.awt.Color(71, 178, 254, 255)),
    BLUEGRAY_LCD(
        new java.awt.Color(135, 174, 255, 255),
        new java.awt.Color(101, 159, 255, 255),
        new java.awt.Color(44, 93, 255, 255),
        new java.awt.Color(27, 65, 254, 255),
        new java.awt.Color(12, 50, 255, 255),
        new java.awt.Color(0xB2B4ED)),    
    STANDARD_LCD(
        new java.awt.Color(131, 133, 119, 255),
        new java.awt.Color(176, 183, 167, 255),
        new java.awt.Color(165, 174, 153, 255),
        new java.awt.Color(166, 175, 156, 255),
        new java.awt.Color(175, 184, 165, 255),
        new java.awt.Color(35, 42, 52, 255)),
    STANDARD_GREEN_LCD(
        new java.awt.Color(255, 255, 255, 255),
        new java.awt.Color(219, 230, 220, 255),
        new java.awt.Color(179, 194, 178, 255),
        new java.awt.Color(153, 176, 151, 255),
        new java.awt.Color(114, 138, 109, 255),
        new java.awt.Color(0x080C06)),
    BLUEBLUE_LCD(
        new java.awt.Color(100, 168, 253, 255),
        new java.awt.Color(100, 168, 253, 255),
        new java.awt.Color(95, 160, 250, 255),
        new java.awt.Color(80, 144, 252, 255),
        new java.awt.Color(74, 134, 255, 255),
        new java.awt.Color(0x002CBB)),
    REDDARKRED_LCD(
        new java.awt.Color(72, 36, 50, 255),
        new java.awt.Color(185, 111, 110, 255),
        new java.awt.Color(148, 66, 72, 255),
        new java.awt.Color(83, 19, 20, 255),
        new java.awt.Color(7, 6, 14, 255),
        new java.awt.Color(0xFE8B92)),
    CUSTOM(
        null,
        null,
        null,
        null,
        null,
        null);


    public final java.awt.Color GRADIENT_START_COLOR;
    public final java.awt.Color GRADIENT_FRACTION1_COLOR;
    public final java.awt.Color GRADIENT_FRACTION2_COLOR;
    public final java.awt.Color GRADIENT_FRACTION3_COLOR;
    public final java.awt.Color GRADIENT_STOP_COLOR;
    public final java.awt.Color TEXT_COLOR;

    LcdColor(final java.awt.Color GRADIENT_START_COLOR, final java.awt.Color GRADIENT_FRACTION1_COLOR, final java.awt.Color GRADIENT_FRACTION2_COLOR, final java.awt.Color GRADIENT_FRACTION3_COLOR, final java.awt.Color GRADIENT_STOP_COLOR, final java.awt.Color TEXT_COLOR)
    {
        this.GRADIENT_START_COLOR = GRADIENT_START_COLOR;
        this.GRADIENT_FRACTION1_COLOR = GRADIENT_FRACTION1_COLOR;
        this.GRADIENT_FRACTION2_COLOR = GRADIENT_FRACTION2_COLOR;
        this.GRADIENT_FRACTION3_COLOR = GRADIENT_FRACTION3_COLOR;
        this.GRADIENT_STOP_COLOR = GRADIENT_STOP_COLOR;
        this.TEXT_COLOR = TEXT_COLOR;
    }
}
