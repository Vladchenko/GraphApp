package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.VertexPopulationStrategy;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

public class VertexPopulationService {

    private final VertexPopulationStrategy strategy;

    public VertexPopulationService(VertexPopulationStrategy strategy) {
        this.strategy = strategy;
    }

    public void populateVertices(VerticesData verticesData, GraphUiState uiState) {
        strategy.populate(verticesData, uiState);

        for (int i = 0; i < verticesData.getVertices().size(); i++) {
            verticesData.getVerticesPolarCoordinates().add(new VertexPolarCoordinate());
        }
    }
}