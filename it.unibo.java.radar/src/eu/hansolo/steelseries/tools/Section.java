package eu.hansolo.steelseries.tools;


/**
 *
 * @author hansolo
 */
public class Section
{
    private double start;
    private double stop;
    private java.awt.Color color;
    private java.awt.geom.Area sectionArea;
    private java.awt.geom.Arc2D filledArea;

    public Section()
    {
        this(-1, -1, java.awt.Color.RED, null, null);
    }

    public Section(final double START, final double STOP, final java.awt.Color COLOR)
    {
        this(START, STOP, COLOR, null, null);
    }
        
    public Section(final double START, final double STOP, final java.awt.Color COLOR, final java.awt.geom.Arc2D FILLED_AREA)
    {
       this(START, STOP, COLOR, null, FILLED_AREA);
    }
    
    public Section(final double START, final double STOP, final java.awt.Color COLOR, final java.awt.geom.Area SECTION_AREA, final java.awt.geom.Arc2D FILLED_AREA)
    {
       this.start = START;
       this.stop = STOP;
       this.color = COLOR;
       this.sectionArea = SECTION_AREA;
       this.filledArea = FILLED_AREA;
    }

    public double getStart()
    {
        return this.start;
    }

    public void setStart(final double START)
    {
        this.start = START;
    }

    public double getStop()
    {
        return this.stop;
    }

    public void setStop(final double STOP)
    {
        this.stop = STOP;
    }

    public java.awt.Color getColor()
    {
        return this.color;
    }

    public void setColor(final java.awt.Color COLOR)
    {
        this.color = COLOR;
    }

    public java.awt.geom.Area getSectionArea()
    {
        return this.sectionArea;
    }

    public void setSectionArea(final java.awt.geom.Area SECTION_AREA)
    {
        this.sectionArea = SECTION_AREA;
    }

    public java.awt.geom.Arc2D getFilledArea()
    {
        return this.filledArea;
    }
    
    public void setFilledArea(final java.awt.geom.Arc2D FILLED_AREA)
    {
        this.filledArea = FILLED_AREA;
    }
    
    @Override
    public String toString()
    {
        return "Section";
    }

}
