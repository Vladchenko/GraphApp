package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

public interface VertexPopulationStrategy {
    void populate(VerticesData verticesData, GraphUiState uiState);
}
