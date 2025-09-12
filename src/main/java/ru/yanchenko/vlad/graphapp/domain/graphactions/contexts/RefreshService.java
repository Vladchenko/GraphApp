package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

/**
 * Service for triggering UI refresh operations.
 * This breaks the dependency cycle between GraphActionContext and DrawingTimer.
 */
public class RefreshService {
    private Runnable refreshCallback;

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void refresh() {
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }
}
