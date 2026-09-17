package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

/**
 * Context for file operations (load, save).
 */
public class FileActionContext {
    private final Persistable persistable;
    private final RefreshService refreshService;
    private final GraphUiState uiState;
    private final GraphDomainService graphService;

    /**
     * Creates a FileActionContext with the specified services and state.
     *
     * @param persistable the persistence service
     * @param refreshService the refresh service
     * @param uiState the UI state
     * @param graphService the graph domain service
     */
    public FileActionContext(Persistable persistable,
                             RefreshService refreshService,
                             GraphUiState uiState,
                             GraphDomainService graphService) {
        this.persistable = persistable;
        this.refreshService = refreshService;
        this.uiState = uiState;
        this.graphService = graphService;
    }

    /**
     * Returns the persistence service.
     *
     * @return the persistence service
     */
    public Persistable getPersistable() { return persistable; }

    /**
     * Returns the refresh service.
     *
     * @return the refresh service
     */
    public RefreshService getRefreshService() { return refreshService; }

    /**
     * Returns the UI state.
     *
     * @return the UI state
     */
    public GraphUiState getUiState() { return uiState; }

    /**
     * Returns the graph domain service.
     *
     * @return the graph domain service
     */
    public GraphDomainService getGraphService() { return graphService; }
}