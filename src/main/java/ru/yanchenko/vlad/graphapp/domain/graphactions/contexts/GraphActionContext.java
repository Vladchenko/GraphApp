package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

/**
 * Context for graph manipulation actions (add, delete, rotate).
 */
public class GraphActionContext {
    private final GraphDomainService graphService;        // NEW
    private final GraphUiStateService uiStateService;     // NEW
    private final VerticesData verticesData;              // Keep for backward compatibility
    private final RefreshService refreshService;
    private final PopulationKind populationKind;

    public GraphActionContext(GraphDomainService graphService,
                              GraphUiStateService uiStateService,
                              VerticesData verticesData,  // Keep for backward compatibility
                              RefreshService refreshService,
                              PopulationKind populationKind) {
        this.graphService = graphService;
        this.uiStateService = uiStateService;
        this.verticesData = verticesData;
        this.refreshService = refreshService;
        this.populationKind = populationKind;
    }

    // NEW: Direct access to services
    public GraphDomainService getGraphService() { return graphService; }
    public GraphUiStateService getUiStateService() { return uiStateService; }

    // Keep existing methods for backward compatibility
    public VerticesData getVerticesData() { return verticesData; }
    public RefreshService getRefreshService() { return refreshService; }
    public PopulationKind getPopulationKind() { return populationKind; }
}