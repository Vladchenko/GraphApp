package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

/**
 * Context for graph manipulation actions (add, delete, rotate).
 */
public class GraphActionContext {
    private final GraphDomainService graphService;
    private final GraphUiState uiState;
    private final RefreshService refreshService;
    private final PopulationKind populationKind;

    /**
     * Creates a GraphActionContext with the specified services and configuration.
     *
     * @param graphService the graph domain service
     * @param uiState the UI state
     * @param refreshService the refresh service
     * @param populationKind the population kind
     */
    public GraphActionContext(GraphDomainService graphService,
                              GraphUiState uiState,
                              RefreshService refreshService,
                              PopulationKind populationKind) {
        this.graphService = graphService;
        this.uiState = uiState;
        this.refreshService = refreshService;
        this.populationKind = populationKind;
    }

    /**
     * Returns the graph domain service.
     *
     * @return the graph domain service
     */
    public GraphDomainService getGraphService() { return graphService; }

    /**
     * Returns the UI state.
     *
     * @return the UI state
     */
    public GraphUiState getUiState() { return uiState; }

    // Keep existing methods for backward compatibility
    /**
     * Returns the refresh service.
     *
     * @return the refresh service
     */
    public RefreshService getRefreshService() { return refreshService; }

    /**
     * Returns the population kind.
     *
     * @return the population kind
     */
    public PopulationKind getPopulationKind() { return populationKind; }
}