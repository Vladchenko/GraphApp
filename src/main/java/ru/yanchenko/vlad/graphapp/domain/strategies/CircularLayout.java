package ru.yanchenko.vlad.graphapp.domain.strategies;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.shared.ScreenData;

import java.util.List;
import java.util.function.Consumer;

/**
 * Layout strategy that arranges vertices in a circle around the screen center.
 * <p>
 * Distributes vertices evenly along the circumference using equal angular steps.
 *
 * @see Consumer
 */
public class CircularLayout implements Consumer<List<Vertex>> {

    private final ScreenData screenData;
    private final double radius;

    /**
     * Creates a CircularLayout with the specified screen data and radius.
     *
     * @param screenData the screen data containing center point
     * @param radius the radius of the circle for vertex placement
     */
    public CircularLayout(ScreenData screenData, double radius) {
        this.screenData = screenData;
        this.radius = radius;
    }

    /**
     * Arranges vertices in a circle around the screen center.
     *
     * @param vertices the list of vertices to layout
     */
    @Override
    public void accept(List<Vertex> vertices) {
        if (vertices == null || vertices.isEmpty()) return;
        double angleIncrement = 0.0;
        double step = Math.PI * 2.0 / vertices.size();
        for (Vertex vertex : vertices) {
            vertex.setX((int) (radius * Math.cos(angleIncrement))
                    + screenData.getScreenCenterPoint().x);
            vertex.setY((int) (radius * Math.sin(angleIncrement))
                    + screenData.getScreenCenterPoint().y);
            angleIncrement += step;
        }
    }
}