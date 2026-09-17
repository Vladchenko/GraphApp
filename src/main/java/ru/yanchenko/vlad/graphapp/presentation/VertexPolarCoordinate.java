package ru.yanchenko.vlad.graphapp.presentation;

/**
 * Polar coordinate for a vertex
 */
public class VertexPolarCoordinate {

    private double angle;
    private double radius;

    /**
     * Returns the angle of this polar coordinate.
     *
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the angle of this polar coordinate.
     *
     * @param angle the angle
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Returns the radius of this polar coordinate.
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of this polar coordinate.
     *
     * @param radius the radius
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
