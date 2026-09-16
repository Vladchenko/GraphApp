package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

/**
 * Strategy interface for populating a graph with initial vertices.
 * <p>
 * Implementations define different ways to seed the graph,
 * such as from hardcoded values, files, or other sources.
 *
 * @see GraphDomainService
 * @see FixedFileStrategy
 * @see CircularFileStrategy
 * @see HardcodedSampleAStrategy
 * @see HardcodedSampleBStrategy
 */
public interface VertexPopulationStrategy {
    /**
     * Populates the graph with initial vertices using this strategy.
     *
     * @param graphService the graph domain service to populate
     * @param uiState the UI state to update
     */
    void populate(GraphDomainService graphService, GraphUiState uiState);
}
