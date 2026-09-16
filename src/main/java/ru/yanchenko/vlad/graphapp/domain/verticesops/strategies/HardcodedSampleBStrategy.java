package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexCreationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

public class HardcodedSampleBStrategy implements VertexPopulationStrategy {
    private final VertexCreationService vertexCreationService;

    public HardcodedSampleBStrategy(VertexCreationService vertexCreationService) {
        this.vertexCreationService = vertexCreationService;
    }

    @Override
    public void populate(GraphDomainService graphService, GraphUiState uiState) {
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
        uiState.setAddingVertex(false);
    }
}
