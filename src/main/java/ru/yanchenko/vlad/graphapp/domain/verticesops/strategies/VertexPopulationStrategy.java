package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

public interface VertexPopulationStrategy {
    void populate(GraphDomainService graphService, GraphUiState uiState);
}
