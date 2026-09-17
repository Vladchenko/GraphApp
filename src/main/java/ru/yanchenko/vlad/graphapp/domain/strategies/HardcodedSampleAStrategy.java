package ru.yanchenko.vlad.graphapp.domain.strategies;

import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexCreationService;

/**
 * Strategy that populates the graph with 9 hardcoded vertices at fixed positions.
 * <p>
 * Used for testing and demonstration purposes.
 *
 * @see VertexPopulationStrategy
 */
public class HardcodedSampleAStrategy implements VertexPopulationStrategy {
    private final VertexCreationService vertexCreationService;

    /**
     * Creates a HardcodedSampleAStrategy with the specified vertex creation service.
     *
     * @param vertexCreationService the service for creating vertices
     */
    public HardcodedSampleAStrategy(VertexCreationService vertexCreationService) {
        this.vertexCreationService = vertexCreationService;
    }

    /**
     * Populates the graph with 9 hardcoded vertices at fixed positions.
     *
     * @param graphService the graph domain service to populate
     */
    @Override
    public void populate(GraphDomainService graphService) {
        vertexCreationService.addVertexAtPosition("1", 100, 100, graphService);
        vertexCreationService.addVertexAtPosition("12", 200, 200, graphService);
        vertexCreationService.addVertexAtPosition("123", 300, 300, graphService);
        vertexCreationService.addVertexAtPosition("1234", 400, 400, graphService);
        vertexCreationService.addVertexAtPosition("12345", 500, 500, graphService);
        vertexCreationService.addVertexAtPosition("123456", 600, 600, graphService);
        vertexCreationService.addVertexAtPosition("123456789", 750, 750, graphService);
        vertexCreationService.addVertexAtPosition("1234567890 abcdefghijk",750, 250, graphService);
        vertexCreationService.addVertexAtPosition("1234567890 abcdefghijk lmnopqrstuv", 1150, 650, graphService);
    }
}