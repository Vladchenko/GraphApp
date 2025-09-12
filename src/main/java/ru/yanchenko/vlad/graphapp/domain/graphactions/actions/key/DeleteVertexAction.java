package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexDeletionService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexLayoutService;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();

        if (!canDeleteVertex()) {
            LOGGER.warning("Cannot delete vertex - no selection or operation in progress");
            return;
        }

        try {
            int selectedIndex = uiState.getSelectedVertexIndex();
            String vertexName = getVertexName(verticesData, selectedIndex);

            // Delete the vertex
            vertexDeletionService.deleteVertexByIndex(selectedIndex, verticesData, uiState);

            // Relayout if needed
            maybeRelayout(verticesData);

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
     * Get vertex name for logging.
     */
    private String getVertexName(VerticesData verticesData, int index) {
        if (index >= 0 && index < verticesData.getVertices().size()) {
            return verticesData.getVertices().get(index).getVertexName();
        }
        return "Unknown";
    }

    /**
     * Relayout vertices if needed based on population kind.
     */
    private void maybeRelayout(VerticesData verticesData) {
        PopulationKind populationKind = context.getPopulationKind();
        if (populationKind == PopulationKind.CIRCULAR_FILE ||
                populationKind == PopulationKind.HARDCODED_SAMPLE_B) {
            vertexLayoutService.layoutVertices(verticesData.getVertices());
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