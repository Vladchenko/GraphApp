package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.EditActionContext;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * Action to cancel current operation.
 * Bound to Escape key.
 */
public class CancelAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(CancelAction.class.getName());

    private final EditActionContext editContext;
    private final GraphUiState uiState;

    public CancelAction(EditActionContext editContext, GraphUiState uiState) {
        super("Cancel");
        this.editContext = editContext;
        this.uiState = uiState;
    }

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
        VerticesData verticesData = editContext.getVerticesData();

        // Clear any ongoing link creation
        verticesData.getVertexLink().setLink1(-1);
        verticesData.getVertexLink().setLink2(-1);

        // Clear possible link preview
        verticesData.getVertexPossibleLink().setX1(-10);
        verticesData.getVertexPossibleLink().setY1(-10);
        verticesData.getVertexPossibleLink().setX2(-10);
        verticesData.getVertexPossibleLink().setY2(-10);
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