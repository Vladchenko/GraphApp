package ru.yanchenko.vlad.graphapp.domain.graphactions.contexts;

/**
 * Callback interface for triggering UI refresh operations.
 * This breaks the dependency cycle between GraphActionContext and DrawingTimer.
 */
@FunctionalInterface
public interface RefreshCallback {
    /**
     * Triggers a refresh of the drawing panel.
     */
    void refresh();
}