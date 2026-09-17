package ru.yanchenko.vlad.graphapp.infrastructure.contexts;

import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

/**
 * Context for edit operations (cancel, confirm).
 */
public class EditActionContext {
    private final GraphUiState uiState;
    private final RefreshService refreshService;

    /**
     * Creates an EditActionContext with the specified state and service.
     *
     * @param uiState the graph UI state
     * @param refreshService the refresh service
     */
    public EditActionContext(GraphUiState uiState, RefreshService refreshService) {
        this.uiState = uiState;
        this.refreshService = refreshService;
    }

    /**
     * Returns the graph UI state.
     *
     * @return the graph UI state
     */
    public GraphUiState getUiState() { return uiState; }

    /**
     * Returns the refresh service.
     *
     * @return the refresh service
     */
    public RefreshService getRefreshService() { return refreshService; }
}