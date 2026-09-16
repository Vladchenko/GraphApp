package ru.yanchenko.vlad.graphapp.models;

/**
 * Geometric primitive representing a point with x and y coordinates.
 * <p>
 * Serves as the base class for shapes like {@link Circle}.
 */
public class Dot {

    private double x;
    private double y;

    /**
     * Returns the x-coordinate of this point.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this point.
     *
     * @param x the new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate of this point.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this point.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }
}
