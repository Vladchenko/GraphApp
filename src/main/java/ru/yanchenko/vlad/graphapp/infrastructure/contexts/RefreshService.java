package ru.yanchenko.vlad.graphapp.infrastructure.contexts;

/**
 * Service for triggering UI refresh operations.
 * This breaks the dependency cycle between GraphActionContext and DrawingTimer.
 */
public class RefreshService {
    private Runnable refreshCallback;

    /**
     * Sets the refresh callback.
     *
     * @param refreshCallback the callback to invoke on refresh
     */
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    /**
     * Triggers a refresh by invoking the callback.
     */
    public void refresh() {
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }
}
