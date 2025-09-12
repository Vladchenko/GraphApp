package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

/**
 * Context for graph manipulation actions (add, delete, rotate).
 */
public class GraphActionContext {
    private final VerticesData verticesData;
    private final RefreshService refreshService;
    private final PopulationKind populationKind;

    public GraphActionContext(VerticesData verticesData,
                              RefreshService refreshService,
                              PopulationKind populationKind) {
        this.verticesData = verticesData;
        this.refreshService = refreshService;
        this.populationKind = populationKind;
    }

    public VerticesData getVerticesData() { return verticesData; }
    public RefreshService getRefreshService() { return refreshService; }
    public PopulationKind getPopulationKind() { return populationKind; }
}