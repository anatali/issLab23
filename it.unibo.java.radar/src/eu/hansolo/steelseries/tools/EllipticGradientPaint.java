package eu.hansolo.steelseries.tools;

/**
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
class EllipticGradientPaint implements java.awt.Paint
{
    private final java.awt.geom.Point2D CENTER;
    private final java.awt.geom.Point2D RADIUS_X_Y;
    private final float[] FRACTIONS;
    private final java.awt.Color[] COLORS;
    private final eu.hansolo.steelseries.tools.GradientWrapper COLOR_LOOKUP;    

    public EllipticGradientPaint(final java.awt.geom.Point2D CENTER, java.awt.geom.Point2D RADIUS_X_Y, final float[] GIVEN_FRACTIONS, final java.awt.Color[] GIVEN_COLORS)
    {
        if (RADIUS_X_Y.distance(0, 0) <= 0)
        {
            throw new IllegalArgumentException("Radius must be greater than 0.");
        }
                
        // Check that fractions and colors are of the same size
        if (GIVEN_FRACTIONS.length != GIVEN_COLORS.length)
        {
            throw new IllegalArgumentException("Fractions and colors must be equal in size");
        }

        this.CENTER = CENTER;
        this.RADIUS_X_Y = RADIUS_X_Y;
        this.FRACTIONS = GIVEN_FRACTIONS;
        this.COLORS = GIVEN_COLORS;
               
        COLOR_LOOKUP = new eu.hansolo.steelseries.tools.GradientWrapper(new java.awt.geom.Point2D.Double(0,0), new java.awt.geom.Point2D.Double(100, 0), FRACTIONS, COLORS);
    }
        
    @Override
    public java.awt.PaintContext createContext(final java.awt.image.ColorModel COLOR_MODEL, final java.awt.Rectangle DEVICE_BOUNDS, final java.awt.geom.Rectangle2D USER_BOUNDS, final java.awt.geom.AffineTransform TRANSFORM, final java.awt.RenderingHints RENDERING_HINTS)
    {
        final java.awt.geom.Point2D TRANSFORMED_CENTER = TRANSFORM.transform(CENTER, null);
        final java.awt.geom.Point2D TRANSFORMED_RADIUS_XY = TRANSFORM.deltaTransform(RADIUS_X_Y, null);
        return new OvalGradientContext(TRANSFORMED_CENTER, TRANSFORMED_RADIUS_XY, FRACTIONS, COLORS);
    }

    @Override
    public int getTransparency()
    {
        return java.awt.Transparency.TRANSLUCENT;
    }

    private final class OvalGradientContext implements java.awt.PaintContext
    {
        private final java.awt.geom.Point2D CENTER;        
        private final java.awt.geom.Ellipse2D.Double ELLIPSE;
        private final java.awt.geom.Line2D.Double LINE;
        private java.util.Map<Double, Double> lookup;
        private double R;

        public OvalGradientContext(final java.awt.geom.Point2D CENTER, final java.awt.geom.Point2D RADIUS_X_Y, final float[] FRACTIONS, final java.awt.Color[] COLORS)
        {
            this.CENTER = CENTER;
            final double X = CENTER.getX() - RADIUS_X_Y.getX();
            final double Y = CENTER.getY() - RADIUS_X_Y.getY();
            final double WIDTH = 2 * RADIUS_X_Y.getX();
            final double HEIGHT = 2 * RADIUS_X_Y.getY();
            ELLIPSE = new java.awt.geom.Ellipse2D.Double(X, Y, WIDTH, HEIGHT);
            LINE = new java.awt.geom.Line2D.Double();
            R = java.awt.geom.Point2D.distance(0, 0, RADIUS_X_Y.getX(), RADIUS_X_Y.getY());
            initLookup();
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public java.awt.image.ColorModel getColorModel()
        {
            return java.awt.image.ColorModel.getRGBdefault();
        }

        @Override
        public java.awt.image.Raster getRaster(final int X, final int Y, final int TILE_WIDTH, final int TILE_HEIGHT)
        {
            final java.awt.image.WritableRaster RASTER = getColorModel().createCompatibleWritableRaster(TILE_WIDTH, TILE_HEIGHT);
            int[] data = new int[TILE_WIDTH * TILE_HEIGHT * 4];            
            double distance;
            double dx;
            double dy;
            double alpha;
            double roundDegrees;
            double radius;
            float ratio;
            
            for (int tileY = 0; tileY < TILE_HEIGHT; tileY++)
            {
                for (int tileX = 0; tileX < TILE_WIDTH; tileX++)
                {
                    distance = CENTER.distance(X + tileX, Y + tileY);
                    dy = Y + tileY - CENTER.getY();
                    dx = X + tileX - CENTER.getX();
                    alpha = Math.atan2(dy, dx);                    
                    roundDegrees = Math.round(Math.toDegrees(alpha));
                    radius = lookup.get(roundDegrees);
                    ratio = (float) (distance / radius);
                    if (Float.compare(ratio, 1.0f) > 0)
                    {
                        ratio = 1.0f;
                    }
                                                            
                    final int BASE = (tileY * TILE_WIDTH + tileX) * 4;
                    data[BASE + 0] = (COLOR_LOOKUP.getColorAt(ratio).getRed());
                    data[BASE + 1] = (COLOR_LOOKUP.getColorAt(ratio).getGreen());
                    data[BASE + 2] = (COLOR_LOOKUP.getColorAt(ratio).getBlue());
                    data[BASE + 3] = (COLOR_LOOKUP.getColorAt(ratio).getAlpha());
                }
            }
            RASTER.setPixels(0, 0, TILE_WIDTH, TILE_HEIGHT, data);
            return RASTER;
        }
        
        private void initLookup()
        {
            lookup = new java.util.HashMap<Double, Double>(360);
            double alpha;
            double xp;
            double yp;
            for (int angle = -180; angle <= 180; angle++)
            {
                Double key = Double.valueOf(angle);
                alpha = Math.toRadians(angle);
                xp = CENTER.getX() + R * Math.cos(alpha);
                yp = CENTER.getY() + R * Math.sin(alpha);
                LINE.setLine(CENTER.getX(), CENTER.getY(), xp, yp);
                Double value = Double.valueOf(getRadius());
                lookup.put(key, value);
            }                                        
            lookup.put(0.0, getRadius());
        }
        
        private double getRadius()
        {
            final double[] COORDINATES = new double[6];
            final java.awt.geom.Point2D.Double P = new java.awt.geom.Point2D.Double();
            double minDistance = Double.MAX_VALUE;
            final double FLATNESS = 0.005;
            final java.awt.geom.PathIterator PATH_ITERATOR = ELLIPSE.getPathIterator(null, FLATNESS);
            while (!PATH_ITERATOR.isDone())
            {
                int segment = PATH_ITERATOR.currentSegment(COORDINATES);
                switch (segment)
                {
                    case java.awt.geom.PathIterator.SEG_CLOSE:
                        
                    case java.awt.geom.PathIterator.SEG_MOVETO:
                        
                    case java.awt.geom.PathIterator.SEG_LINETO:
                        break;
                    
                    default:                        
                        break;
                }
                final double DISTANCE = LINE.ptSegDist(COORDINATES[0], COORDINATES[1]);
                
                if (DISTANCE < minDistance)
                {
                    minDistance = DISTANCE;
                    P.x = COORDINATES[0];
                    P.y = COORDINATES[1];
                }
                
                PATH_ITERATOR.next();
            }
            return CENTER.distance(P);
        }
    }
}