package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;

/**
 * Context for graph manipulation actions (add, delete, rotate).
 */
public class GraphActionContext {
    private final GraphDomainService graphService;        // NEW
    private final GraphUiStateService uiStateService;     // NEW
    private final RefreshService refreshService;
    private final PopulationKind populationKind;

    /**
     * Creates a GraphActionContext with the specified services and configuration.
     *
     * @param graphService the graph domain service
     * @param uiStateService the UI state service
     * @param refreshService the refresh service
     * @param populationKind the population kind
     */
    public GraphActionContext(GraphDomainService graphService,
                              GraphUiStateService uiStateService,
                              RefreshService refreshService,
                              PopulationKind populationKind) {
        this.graphService = graphService;
        this.uiStateService = uiStateService;
        this.refreshService = refreshService;
        this.populationKind = populationKind;
    }

    // NEW: Direct access to services
    /**
     * Returns the graph domain service.
     *
     * @return the graph domain service
     */
    public GraphDomainService getGraphService() { return graphService; }

    /**
     * Returns the UI state service.
     *
     * @return the UI state service
     */
    public GraphUiStateService getUiStateService() { return uiStateService; }

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