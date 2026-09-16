package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.List;

/**
 * Strategy interface for applying layout algorithms to a list of vertices.
 * <p>
 * Implementations define how vertices are positioned on the screen
 * (e.g., circular, grid, force-directed).
 *
 * @see VertexLayoutService
 */
public interface LayoutStrategy {
    /**
     * Applies the layout algorithm to position the vertices.
     *
     * @param vertices the list of vertices to layout
     */
    void layout(List<Vertex> vertices);
}