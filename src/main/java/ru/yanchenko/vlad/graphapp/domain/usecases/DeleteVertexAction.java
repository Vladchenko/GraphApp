package ru.yanchenko.vlad.graphapp.domain.usecases;

import ru.yanchenko.vlad.graphapp.domain.PopulationKind;
import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.domain.services.VertexDeletionService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexLayoutService;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Logger;

/**
 * Action to delete the selected vertex.
 * Bound to Delete and Cmd+Backspace keys.
 */
public class DeleteVertexAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(DeleteVertexAction.class.getName());

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final VertexLayoutService vertexLayoutService;
    private final VertexDeletionService vertexDeletionService;

    /**
     * Creates a DeleteVertexAction with the specified context and services.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param vertexLayoutService the vertex layout service
     * @param vertexDeletionService the vertex deletion service
     */
    public DeleteVertexAction(GraphActionContext context,
                              GraphUiState uiState,
                              VertexLayoutService vertexLayoutService,
                              VertexDeletionService vertexDeletionService) {
        super("Delete Vertex");
        this.context = context;
        this.uiState = uiState;
        this.vertexLayoutService = vertexLayoutService;
        this.vertexDeletionService = vertexDeletionService;
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.META_DOWN_MASK));
    }

    /**
     * Handles the delete vertex action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!canDeleteVertex()) {
            LOGGER.warning("Cannot delete vertex - no selection or operation in progress");
            return;
        }

        try {
            int selectedIndex = uiState.getSelectedVertexIndex();
            String vertexName = getVertexName(
                    context.getGraphService().getCurrentGraph().getVertices(),
                    selectedIndex);

            // Delete the vertex
            vertexDeletionService.deleteVertexByIndex(selectedIndex, context.getGraphService(), uiState);

            // Relayout if needed
            maybeRelayout(context.getGraphService().getCurrentGraph().getVertices());

            // Clear selection
            uiState.setSelectedVertexIndex(-1);

            LOGGER.info("Deleted vertex: " + vertexName + " at index: " + selectedIndex);
            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error deleting vertex", ex);
            // Reset to safe state
            resetToSafeState();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return canDeleteVertex();
    }

    /**
     * Check if vertex can be deleted.
     */
    private boolean canDeleteVertex() {
        return uiState.hasSelection() &&
                !uiState.isEditingVertex() &&
                !uiState.isAddingVertex() &&
                !uiState.isDeletingVertex();
    }

    /**
     * Get vertex name.
     */
    private String getVertexName(List<Vertex> vertices, int index) {
        if (index >= 0 && index < vertices.size()) {
            return vertices.get(index).getVertexName();
        }
        return "Unknown";
    }

    /**
     * Relayout vertices if needed based on population kind.
     */
    private void maybeRelayout(List<Vertex> vertices) {
        PopulationKind populationKind = context.getPopulationKind();
        if (populationKind == PopulationKind.CIRCULAR_FILE ||
                populationKind == PopulationKind.HARDCODED_SAMPLE_B) {
            vertexLayoutService.layoutVertices(vertices);
        }
    }

    /**
     * Reset to safe state in case of error.
     */
    private void resetToSafeState() {
        uiState.setSelectedVertexIndex(-1);
        uiState.setDeletingVertex(false);
        context.getRefreshService().refresh();
    }
}