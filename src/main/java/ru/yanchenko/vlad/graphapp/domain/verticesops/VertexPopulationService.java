package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.FixedFileStrategy;
import ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.VertexPopulationStrategy;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

import java.lang.reflect.Field;

/**
 * Service responsible for populating graph vertices from various sources.
 * <p>
 * Delegates loading to a {@link VertexPopulationStrategy}. Supports two paths:
 * <ul>
 *   <li>{@link ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.FixedFileStrategy} —
 *       loads directly from a {@link Persistable} via reflection</li>
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
     * @param uiStateService the UI state service (used to update polar coordinates)
     * @param uiState        the UI state (reset to adding mode after load)
     */
    public void populateVertices(GraphDomainService graphService,
                                 GraphUiStateService uiStateService,
                                 GraphUiState uiState) {
        if (strategy instanceof FixedFileStrategy) {
            try {
                FixedFileStrategy fixedFileStrategy = (FixedFileStrategy) strategy;
                Field field = fixedFileStrategy.getClass().getDeclaredField("persistable");
                field.setAccessible(true);
                Persistable persistable = (Persistable) field.get(fixedFileStrategy);
                persistable.loadFromFile(graphService);
                uiStateService.updatePolarCoordinates(graphService.getCurrentGraph().getVertices());
            } catch (Exception ex) {
                System.err.println("Error in direct service-based loading: " + ex.getMessage());
                ex.printStackTrace();
                strategy.populate(graphService, uiState);
            }
        } else {
            strategy.populate(graphService, uiState);
        }
        uiState.setAddingVertex(false);
    }
}