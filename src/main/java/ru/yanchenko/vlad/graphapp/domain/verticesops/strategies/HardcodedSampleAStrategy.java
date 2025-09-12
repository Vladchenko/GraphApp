package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexCreationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

public class HardcodedSampleAStrategy implements VertexPopulationStrategy {
    private final VertexCreationService vertexCreationService;

    public HardcodedSampleAStrategy(VertexCreationService vertexCreationService) {
        this.vertexCreationService = vertexCreationService;
    }

    @Override
    public void populate(VerticesData verticesData, GraphUiState uiState) {
        vertexCreationService.addVertexAtPosition("1", 100, 100, verticesData);
        vertexCreationService.addVertexAtPosition("12", 200, 200, verticesData);
        vertexCreationService.addVertexAtPosition("123", 300, 300, verticesData);
        vertexCreationService.addVertexAtPosition("1234", 400, 400, verticesData);
        vertexCreationService.addVertexAtPosition("12345", 500, 500, verticesData);
        vertexCreationService.addVertexAtPosition("123456", 600, 600, verticesData);
        vertexCreationService.addVertexAtPosition("123456789", 750, 750, verticesData);
        vertexCreationService.addVertexAtPosition("1234567890 abcdefghijk",750, 250, verticesData);
        vertexCreationService.addVertexAtPosition("1234567890 abcdefghijk lmnopqrstuv", 1150, 650, verticesData);
    }
}
