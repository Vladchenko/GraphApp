package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiData;

/**
 * Context for edit operations (cancel, confirm).
 */
public class EditActionContext {
    private final GraphUiData graphUiData;
    private final RefreshService refreshService;

    public EditActionContext(GraphUiData graphUiData, RefreshService refreshService) {
        this.graphUiData = graphUiData;
        this.refreshService = refreshService;
    }

    public GraphUiData getGraphUiData() { return graphUiData; }
    public RefreshService getRefreshService() { return refreshService; }
}