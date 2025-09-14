package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexPopulationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Action to reset the graph to initial state.
 * Bound to Ctrl+D
 */
public class ResetAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(ResetAction.class.getName());

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final VertexPopulationService vertexPopulationService;

    public ResetAction(GraphActionContext context,
                       GraphUiState uiState,
                       VertexPopulationService vertexPopulationService) {
        super("Reset Graph", KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        this.context = context;
        this.uiState = uiState;
        this.vertexPopulationService = vertexPopulationService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Clear all domain data
            clearDomainData();

            // Clear all UI state
            clearUiState();

            // Repopulate with initial data using new service architecture
            vertexPopulationService.populateVertices(context.getGraphService(), context.getUiStateService(), uiState);

            LOGGER.info("Graph reset successfully");
            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error resetting graph", ex);
            // Reset to safe state
            resetToSafeState();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return !uiState.isEditingVertex() && !uiState.isAddingVertex();
    }

    private void clearDomainData() {
        // Clear graph data using the new service architecture
        context.getGraphService().updateGraph(new ru.yanchenko.vlad.graphapp.models.domain.Graph(new java.util.ArrayList<>(), new java.util.ArrayList<>()));
        context.getUiStateService().setPolarCoordinates(new java.util.ArrayList<>());
    }

    private void clearUiState() {
        uiState.setSelectedVertexIndex(-1);
        uiState.setEditBuffer("");
        uiState.setAddingVertex(false);
        uiState.setEditingVertex(false);
        uiState.setDeletingVertex(false);
    }

    private void resetToSafeState() {
        clearUiState();
        context.getRefreshService().refresh();
    }
}