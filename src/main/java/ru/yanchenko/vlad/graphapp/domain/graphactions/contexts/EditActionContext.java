package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

/**
 * Context for edit operations (cancel, confirm).
 */
public class EditActionContext {
    private final VerticesData verticesData;
    private final RefreshService refreshService;

    public EditActionContext(VerticesData verticesData, RefreshService refreshService) {
        this.verticesData = verticesData;
        this.refreshService = refreshService;
    }

    public VerticesData getVerticesData() { return verticesData; }
    public RefreshService getRefreshService() { return refreshService; }
}