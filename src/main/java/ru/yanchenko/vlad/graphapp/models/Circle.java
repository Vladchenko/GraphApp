package ru.yanchenko.vlad.graphapp.models;

/**
 * Geometric shape representing a circle with a center point and radius.
 * <p>
 * Extends {@link Dot} to inherit x/y coordinates, adding a radius property.
 * Used as the base class for {@link ru.yanchenko.vlad.graphapp.models.vertex.Vertex}.
 *
 * @see Dot
 * @see ru.yanchenko.vlad.graphapp.models.vertex.Vertex
 */
public class Circle extends Dot {

    private double radius;

    /**
     * Returns the radius of this circle.
     *
     * @return the radius value
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of this circle.
     *
     * @param radius the new radius value
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
