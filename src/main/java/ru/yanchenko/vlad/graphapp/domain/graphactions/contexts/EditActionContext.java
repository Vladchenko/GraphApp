package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiData;

/**
 * Context for edit operations (cancel, confirm).
 */
public class EditActionContext {
    private final GraphUiData graphUiData;
    private final RefreshService refreshService;

    /**
     * Creates an EditActionContext with the specified data and service.
     *
     * @param graphUiData the graph UI data
     * @param refreshService the refresh service
     */
    public EditActionContext(GraphUiData graphUiData, RefreshService refreshService) {
        this.graphUiData = graphUiData;
        this.refreshService = refreshService;
    }

    /**
     * Returns the graph UI data.
     *
     * @return the graph UI data
     */
    public GraphUiData getGraphUiData() { return graphUiData; }

    /**
     * Returns the refresh service.
     *
     * @return the refresh service
     */
    public RefreshService getRefreshService() { return refreshService; }
}