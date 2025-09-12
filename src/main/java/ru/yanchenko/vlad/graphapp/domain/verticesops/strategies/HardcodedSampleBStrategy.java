package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexCreationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

public class HardcodedSampleBStrategy implements VertexPopulationStrategy {
    private final VertexCreationService vertexCreationService;

    public HardcodedSampleBStrategy(VertexCreationService vertexCreationService) {
        this.vertexCreationService = vertexCreationService;
    }

    @Override
    public void populate(VerticesData verticesData, GraphUiState uiState) {
        vertexCreationService.addVertexByName("a", verticesData);
        vertexCreationService.addVertexByName("bc", verticesData);
        vertexCreationService.addVertexByName("def", verticesData);
        vertexCreationService.addVertexByName("ghijkl", verticesData);
        vertexCreationService.addVertexByName("mno", verticesData);
        vertexCreationService.addVertexByName("pq", verticesData);
        vertexCreationService.addVertexByName("r", verticesData);
        vertexCreationService.addVertexByName("12", verticesData);
        vertexCreationService.addVertexByName("345", verticesData);
        vertexCreationService.addVertexByName("67890", verticesData);
        vertexCreationService.addVertexByName("!@#", verticesData);
        uiState.setAddingVertex(false);
    }
}
