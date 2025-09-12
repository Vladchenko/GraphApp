package ru.yanchenko.vlad.graphapp.geometry;

import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

/**
 * Computation for vertices rotation
 */
public class Geometry {

    /**
     * Compute a distance between 2 dots
     *
     * @param x1 ordinate of a 1st dot
     * @param y1 ordinate of a 1st dot
     * @param x2 ordinate of a 2nd dot
     * @param y2 ordinate of a 2nd dot
     * @return distance between 2 dots
     */
    public static double computeDistance(double x1, double y1, int x2, int y2) {
        return Math.sqrt(
                Math.pow(x1 - x2, 2)
                        + Math.pow(y1 - y2, 2)
        );
    }

    /**
     * Compute angle between 2 dots (coordinates)
     *
     * @param x1 ordinate of first dot
     * @param y1 ordinate of first dot
     * @param x2 ordinate of second dot
     * @param y2 ordinate of second dot
     * @return angle between 2 dots
     */
    public static double computeAngle(double x1, double y1, double x2, double y2) {

        double deltaX;
        double deltaY;
        double resultAngle = 0;

        deltaY = y2 - y1;
        deltaX = x2 - x1;

        // Calculating angle between dots
        if (deltaX != 0) {
            resultAngle = Math.atan(Math.abs(deltaY)
                    / Math.abs(deltaX));
        }

        // Adjusting the angle
        if (deltaX < 0 && deltaY < 0) {
            resultAngle = -resultAngle + Math.PI;
        }
        if (deltaX < 0 && deltaY > 0) {
            resultAngle = resultAngle + Math.PI;
        }
        if (deltaX > 0 && deltaY > 0) {
            resultAngle = -resultAngle + Math.PI * 2;
        }

        if (deltaX == 0 && deltaY > 0) {
            resultAngle = 2 * Math.PI - Math.PI / 2;
        }
        if (deltaX == 0 && deltaY < 0) {
            resultAngle = Math.PI / 2;
        }
        if (deltaX > 0 && deltaY == 0) {
            resultAngle = 0;
        }
        if (deltaX < 0 && deltaY == 0) {
            resultAngle = Math.PI;
        }

        if (deltaX == 0 && deltaY == 0) {
            resultAngle = 0;
        }

        return resultAngle;
    }

    /**
     * Compute a polar coordinate of a vertex, relatively to a center coordinate.
     *
     * @param vertex          to compute a polar coordinate for
     * @param polarCoordinate to keep a polar coordinate computation
     * @param centerX         to compute a polar coordinate relatively to
     * @param centerY         to compute a polar coordinate relatively to
     */
    public static void toPolar(Vertex vertex,
                               VertexPolarCoordinate polarCoordinate,
                               int centerX,
                               int centerY) {
        polarCoordinate.setAngle(
                computeAngle(centerX,
                        centerY,
                        vertex.getX(),
                        vertex.getY())
        );
        // computing the radius between vertex and centerX, centerY coordinate
        polarCoordinate.setRadius(
                Math.sqrt(
                        Math.pow(vertex.getY()
                                - centerY, 2)
                                + Math.pow(vertex.getX()
                                - centerX, 2)));
    }

    /**
     * Convert to cartesian(dekart) coordinate from polar coordinate.
     *
     * @param vertex          to compute a cartesian coordinate for
     * @param polarCoordinate to get a polar coordinate from
     * @param centerX         to compute a cartesian's x ordinate relatively to
     * @param centerY         to compute a cartesian's y ordinate relatively to
     */
    public static void toCartesian(Vertex vertex,
                                   VertexPolarCoordinate polarCoordinate,
                                   int centerX,
                                   int centerY) {
        vertex.setX(
                centerX
                        + Math.cos(polarCoordinate.getAngle())
                        * polarCoordinate.getRadius()
        );
        vertex.setY(
                centerY
                        - Math.sin(polarCoordinate.getAngle())
                        * polarCoordinate.getRadius()
        );
    }
}
