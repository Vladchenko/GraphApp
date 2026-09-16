package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexCreationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

public class HardcodedSampleAStrategy implements VertexPopulationStrategy {
    private final VertexCreationService vertexCreationService;

    public HardcodedSampleAStrategy(VertexCreationService vertexCreationService) {
        this.vertexCreationService = vertexCreationService;
    }

    @Override
    public void populate(GraphDomainService graphService, GraphUiState uiState) {
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
