package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.EditActionContext;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Action to cancel current operation.
 * Bound to Escape key.
 */
public class CancelAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(CancelAction.class.getName());

    private final EditActionContext editContext;
    private final GraphUiState uiState;

    /**
     * Creates a CancelAction with the specified context and state.
     *
     * @param editContext the edit action context
     * @param uiState the UI state
     */
    public CancelAction(EditActionContext editContext, GraphUiState uiState) {
        super("Cancel");
        this.editContext = editContext;
        this.uiState = uiState; 
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
    }

    /**
     * Handles the cancel action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Cancel all operations
            cancelAllOperations();

            // Clear domain data state
            clearDomainDataState();

            LOGGER.info("Cancelled all operations");
            editContext.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error cancelling operations", ex);
            // Force reset to safe state
            forceResetToSafeState();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return uiState.isInAnyOperation();
    }

    /**
     * Cancel all UI operations.
     */
    private void cancelAllOperations() {
        uiState.setAddingVertex(false);
        uiState.setEditingVertex(false);
        uiState.setDeletingVertex(false);
        uiState.setEditBuffer("");
    }

    /**
     * Clear domain data state.
     */
    private void clearDomainDataState() {
        GraphUiState uiState = editContext.getUiState();

        // Clear any ongoing edge creation
        uiState.setEdgeBeingCreated(new Edge(-1, -1));

        // Clear possible link preview
        uiState.getDragPreview().setX1(-10);
        uiState.getDragPreview().setY1(-10);
        uiState.getDragPreview().setX2(-10);
        uiState.getDragPreview().setY2(-10);
    }

    /**
     * Force reset to safe state in case of error.
     */
    private void forceResetToSafeState() {
        uiState.setAddingVertex(false);
        uiState.setEditingVertex(false);
        uiState.setDeletingVertex(false);
        uiState.setSelectedVertexIndex(-1);
        uiState.setEditBuffer("");

        // Force refresh
        editContext.getRefreshService().refresh();
    }
}