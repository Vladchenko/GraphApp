package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Logger;

/**
 * Action to start editing a selected vertex.
 * Bound to Ctrl+R key.
 */
public class EditVertexAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(EditVertexAction.class.getName());

    private final GraphActionContext context;
    private final GraphUiState uiState;

    public EditVertexAction(GraphActionContext context, GraphUiState uiState) {
        super("Edit Vertex");
        this.context = context;
        this.uiState = uiState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!canStartEditing()) {
            LOGGER.warning("Cannot start vertex editing - no selection or operation in progress");
            return;
        }

        try {
            // Clear any other operations
            clearOtherOperations();

            // Start editing mode
            startEditingMode();

            LOGGER.info("Started vertex editing mode for vertex at index: " + uiState.getSelectedVertexIndex());
            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error starting vertex editing", ex);
            resetToSafeState();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return canStartEditing();
    }

    /**
     * Check if vertex editing can be started.
     */
    private boolean canStartEditing() {
        return uiState.hasSelection() &&
                !uiState.isEditingVertex() &&
                !uiState.isAddingVertex() &&
                !uiState.isDeletingVertex();
    }

    /**
     * Clear other operations before starting editing.
     */
    private void clearOtherOperations() {
        uiState.setAddingVertex(false);
        uiState.setDeletingVertex(false);
    }

    /**
     * Start editing mode for the selected vertex.
     */
    private void startEditingMode() {
        List<Vertex> vertices = context.getGraphService().getCurrentGraph().getVertices();
        int index = uiState.getSelectedVertexIndex();

        if (index >= 0 && index < vertices.size()) {
            // Set UI state for editing
            uiState.setEditingVertex(true);

            // Pre-populate edit buffer with current name
            String currentName = vertices.get(index).getVertexName();
            uiState.setEditBuffer(currentName);
        }
    }

    /**
     * Reset to a safe state in case of error.
     */
    private void resetToSafeState() {
        uiState.setEditingVertex(false);
        uiState.setAddingVertex(false);
        uiState.setDeletingVertex(false);
        uiState.setEditBuffer("");
        context.getRefreshService().refresh();
    }
}