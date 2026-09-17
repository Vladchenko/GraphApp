package ru.yanchenko.vlad.graphapp.domain.services;

import ru.yanchenko.vlad.graphapp.domain.strategies.VertexPopulationStrategy;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

/**
 * Service responsible for populating graph vertices from various sources.
 * <p>
 * Delegates loading to a {@link VertexPopulationStrategy}. Supports two paths:
 * <ul>
 *   <li>{@link ru.yanchenko.vlad.graphapp.domain.strategies.FixedFileStrategy} —
 *       loads directly from a {@link ru.yanchenko.vlad.graphapp.data.persistence.Persistable} via reflection</li>
 *   <li>Other strategies — fall back to {@code strategy.populate()}</li>
 * </ul>
 */
public class VertexPopulationService {

    private final VertexPopulationStrategy strategy;

    /**
     * Creates a new instance with the specified population strategy.
     *
     * @param strategy the strategy used to populate vertices
     */
    public VertexPopulationService(VertexPopulationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Populates vertices using the configured strategy.
     *
     * @param graphService   the graph domain service
     * @param uiState        the UI state (used to update polar coordinates and reset to adding mode after load)
     */
    public void populateVertices(GraphDomainService graphService,
                                 GraphUiState uiState) {
        strategy.populate(graphService);
        uiState.updatePolarCoordinates(graphService.getCurrentGraph().getVertices());
        uiState.setAddingVertex(false);
    }
}