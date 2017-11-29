package ca.ubc.cs.cpsc210.translink.util;



/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param point             the point in question
     * @return                  true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {
        return (northWest.getLongitude()<=point.getLongitude()&&point.getLongitude()<=southEast.getLongitude()
                &&southEast.getLatitude()<=point.getLatitude()&&point.getLatitude()<=northWest.getLatitude());

    }

    /**
     * Return true if the rectangle intersects the line
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param src               one end of the line in question
     * @param dst               the other end of the line in question
     * @return                  true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        if ((src.getLongitude()<northWest.getLongitude()&&dst.getLongitude()<northWest.getLongitude())|
                (src.getLongitude()>southEast.getLongitude()&&dst.getLongitude()>southEast.getLongitude())|
                (src.getLatitude()>northWest.getLatitude()&&dst.getLatitude()>northWest.getLatitude())|
                (src.getLatitude()<southEast.getLatitude()&&dst.getLatitude()<southEast.getLatitude()))
        {
            return false;
        }

        else {
            if (src.getLongitude()==dst.getLongitude()){
                return between(northWest.getLongitude(),southEast.getLongitude(),src.getLongitude());

            }
            else {
                double k=(src.getLatitude()-dst.getLatitude())/(src.getLongitude()-dst.getLongitude());
                double b=dst.getLatitude()-(src.getLatitude()-dst.getLatitude())*dst.getLongitude()/(src.getLongitude()-dst.getLongitude());
                if ((k*northWest.getLongitude()+b>northWest.getLatitude()&&k*southEast.getLongitude()+b>northWest.getLatitude()
                        &&k*southEast.getLongitude()+b>southEast.getLatitude()&&k*northWest.getLongitude()+b>southEast.getLatitude())|
                        ((k*northWest.getLongitude()+b<northWest.getLatitude()&&k*southEast.getLongitude()+b<northWest.getLatitude()
                                &&k*southEast.getLongitude()+b<southEast.getLatitude()&&k*northWest.getLongitude()+b<southEast.getLatitude())))
                {return false;}
                else
                {return true;}
            }
        }

    }

    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     * @param lwb      the lower boundary
     * @param upb      the upper boundary
     * @param x         the value in question
     * @return          true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
