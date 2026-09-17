package ru.yanchenko.vlad.graphapp.domain.strategies;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexCreationService;

import java.util.List;
import java.util.function.Consumer;

/**
 * Strategy that populates the graph with 11 hardcoded vertices by name only.
 * <p>
 * Vertices are placed at the screen center; positions are later adjusted by layout.
 * Used for testing and demonstration purposes.
 *
 * @see VertexPopulationStrategy
 */
public class HardcodedSampleBStrategy implements VertexPopulationStrategy {
    private final VertexCreationService vertexCreationService;
    private final Consumer<List<Vertex>> layoutStrategy;

    /**
     * Creates a HardcodedSampleBStrategy with the specified vertex creation service.
     *
     * @param vertexCreationService the service for creating vertices
     * @param layoutStrategy the layout strategy to apply after population
     */
    public HardcodedSampleBStrategy(VertexCreationService vertexCreationService, Consumer<List<Vertex>> layoutStrategy) {
        this.vertexCreationService = vertexCreationService;
        this.layoutStrategy = layoutStrategy;
    }

    /**
     * Populates the graph with 11 hardcoded vertices by name only.
     *
     * @param graphService the graph domain service to populate
     */
    @Override
    public void populate(GraphDomainService graphService) {
        vertexCreationService.addVertexByName("a", graphService);
        vertexCreationService.addVertexByName("bc", graphService);
        vertexCreationService.addVertexByName("def", graphService);
        vertexCreationService.addVertexByName("ghijkl", graphService);
        vertexCreationService.addVertexByName("mno", graphService);
        vertexCreationService.addVertexByName("pq", graphService);
        vertexCreationService.addVertexByName("r", graphService);
        vertexCreationService.addVertexByName("12", graphService);
        vertexCreationService.addVertexByName("345", graphService);
        vertexCreationService.addVertexByName("67890", graphService);
        vertexCreationService.addVertexByName("!@#", graphService);
        if (layoutStrategy != null) {
            layoutStrategy.accept(graphService.getCurrentGraph().getVertices());
        }
    }
}