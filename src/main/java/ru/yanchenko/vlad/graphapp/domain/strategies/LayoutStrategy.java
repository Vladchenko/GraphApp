package ru.yanchenko.vlad.graphapp.domain.strategies;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;

import java.util.List;

/**
 * Strategy interface for applying layout algorithms to a list of vertices.
 * <p>
 * Implementations define how vertices are positioned on the screen
 * (e.g., circular, grid, force-directed).
 */
public interface LayoutStrategy {
    /**
     * Applies the layout algorithm to position the vertices.
     *
     * @param vertices the list of vertices to layout
     */
    void layout(List<Vertex> vertices);
}
