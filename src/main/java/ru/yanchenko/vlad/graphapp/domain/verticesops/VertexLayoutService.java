package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.List;
import java.util.function.Consumer;

/**
 * Service responsible for applying layout strategies to vertices.
 */
public class VertexLayoutService {

    private final Consumer<List<Vertex>> layoutStrategy;

    /**
     * Creates a VertexLayoutService with the specified layout strategy.
     *
     * @param layoutStrategy the layout strategy consumer
     */
    public VertexLayoutService(Consumer<List<Vertex>> layoutStrategy) {
        this.layoutStrategy = layoutStrategy;
    }

    /**
     * Applies the configured layout strategy to the given vertices.
     *
     * @param vertices the list of vertices to layout
     */
    public void layoutVertices(List<Vertex> vertices) {
        if (layoutStrategy != null) {
            layoutStrategy.accept(vertices);
        }
    }

    /**
     * Applies a custom layout strategy to the given vertices.
     *
     * @param vertices the list of vertices to layout
     * @param customLayoutStrategy the custom layout strategy to apply
     */
    public void layoutVertices(List<Vertex> vertices,
                               Consumer<List<Vertex>> customLayoutStrategy) {
        if (customLayoutStrategy != null) {
            customLayoutStrategy.accept(vertices);
        }
    }
}