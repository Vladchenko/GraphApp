package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

/**
 * Context for file operations (load, save).
 */
public class FileActionContext {
    private final Persistable persistable;
    private final VerticesData verticesData;
    private final RefreshService refreshService;
    private final GraphUiState uiState;
    private final GraphDomainService graphService;
    private final GraphUiStateService uiStateService;

    public FileActionContext(Persistable persistable,
                             VerticesData verticesData,
                             RefreshService refreshService,
                             GraphUiState uiState,
                             GraphDomainService graphService,
                             GraphUiStateService uiStateService) {
        this.persistable = persistable;
        this.verticesData = verticesData;
        this.refreshService = refreshService;
        this.uiState = uiState;
        this.graphService = graphService;
        this.uiStateService = uiStateService;
    }

    public Persistable getPersistable() { return persistable; }
    public VerticesData getVerticesData() { return verticesData; }
    public RefreshService getRefreshService() { return refreshService; }
    public GraphUiState getUiState() { return uiState; }
    public GraphDomainService getGraphService() { return graphService; }
    public GraphUiStateService getUiStateService() { return uiStateService; }
}