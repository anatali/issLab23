package eu.hansolo.steelseries.tools;

/**
 *
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public enum GaugeType
{
    TYPE1(0, (1.5 * Math.PI), (0.5 * Math.PI), (Math.PI / 2.0), 180, 90, 0, new java.awt.geom.Rectangle2D.Double(0.55, 0.55, 0.55, 0.15), eu.hansolo.steelseries.tools.PostPosition.CENTER, eu.hansolo.steelseries.tools.PostPosition.MAX_CENTER_TOP, eu.hansolo.steelseries.tools.PostPosition.MIN_LEFT),
    TYPE2(0, (1.5 * Math.PI), (0.5 * Math.PI), Math.PI, 180, 180, 0, new java.awt.geom.Rectangle2D.Double(0.55, 0.55, 0.55, 0.15), eu.hansolo.steelseries.tools.PostPosition.CENTER, eu.hansolo.steelseries.tools.PostPosition.MIN_LEFT, eu.hansolo.steelseries.tools.PostPosition.MAX_RIGHT),
    TYPE3(0, Math.PI, 0, (1.5 * Math.PI), 270, 270, -90, new java.awt.geom.Rectangle2D.Double(0.4, 0.55, 0.4, 0.15), eu.hansolo.steelseries.tools.PostPosition.CENTER, eu.hansolo.steelseries.tools.PostPosition.MAX_CENTER_BOTTOM, eu.hansolo.steelseries.tools.PostPosition.MAX_RIGHT),
    TYPE4((Math.toRadians(60)), (Math.PI + Math.toRadians(30)), 0, Math.toRadians(300), 240, 300, -60, new java.awt.geom.Rectangle2D.Double(0.4, 0.55, 0.4, 0.15), eu.hansolo.steelseries.tools.PostPosition.CENTER, eu.hansolo.steelseries.tools.PostPosition.MIN_BOTTOM, eu.hansolo.steelseries.tools.PostPosition.MAX_BOTTOM),
    TYPE5(0, (1.75 * Math.PI), (0.75 * Math.PI), (Math.PI / 2.0), 180, 90, 0, new java.awt.geom.Rectangle2D.Double(0.55, 0.55, 0.55, 0.15), eu.hansolo.steelseries.tools.PostPosition.LOWER_CENTER, eu.hansolo.steelseries.tools.PostPosition.SMALL_GAUGE_MIN_LEFT, eu.hansolo.steelseries.tools.PostPosition.SMALL_GAUGE_MAX_RIGHT);
    
    final public double FREE_AREA_ANGLE;
    final public double ROTATION_OFFSET;
    final public double TICKMARK_OFFSET;
    final public double ANGLE_RANGE;
    final public double ORIGIN_CORRECTION;
    final public double APEX_ANGLE;
    final public double BARGRAPH_OFFSET;
    final public eu.hansolo.steelseries.tools.PostPosition[] POST_POSITIONS;
    final public java.awt.geom.Rectangle2D LCD_FACTORS;

    private GaugeType(final double FREE_AREA_ANGLE, final double ROTATION_OFFSET, final double TICKMARK_OFFSET, final double ANGLE_RANGE, final double ORIGIN_CORRECTION, final double APEX_ANGLE, final double BARGRAPH_OFFSET, final java.awt.geom.Rectangle2D LCD_FACTORS, final eu.hansolo.steelseries.tools.PostPosition... POST_POSITIONS)
    {
        this.FREE_AREA_ANGLE = FREE_AREA_ANGLE;
        this.ROTATION_OFFSET = ROTATION_OFFSET;
        this.TICKMARK_OFFSET = TICKMARK_OFFSET;
        this.ANGLE_RANGE = ANGLE_RANGE;
        this.ORIGIN_CORRECTION = ORIGIN_CORRECTION;
        this.APEX_ANGLE = APEX_ANGLE;
        this.BARGRAPH_OFFSET = BARGRAPH_OFFSET;
        this.POST_POSITIONS = POST_POSITIONS;
        this.LCD_FACTORS = LCD_FACTORS;
    }
}
