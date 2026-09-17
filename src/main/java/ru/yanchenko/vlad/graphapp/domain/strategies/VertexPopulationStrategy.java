package ru.yanchenko.vlad.graphapp.domain.strategies;

import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;

/**
 * Strategy interface for populating a graph with initial vertices.
 * <p>
 * Implementations define different ways to seed the graph,
 * such as from hardcoded values, files, or other sources.
 * UI state updates are handled by the caller after population.
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
     */
    void populate(GraphDomainService graphService);
}