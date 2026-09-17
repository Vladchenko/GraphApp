package ru.yanchenko.vlad.graphapp.domain.usecases;

import ru.yanchenko.vlad.graphapp.domain.services.VertexPopulationService;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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

    /**
     * Creates a ResetAction with the specified context and service.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param vertexPopulationService the vertex population service
     */
    public ResetAction(GraphActionContext context,
                       GraphUiState uiState,
                       VertexPopulationService vertexPopulationService) {
        super("Reset Graph", KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        this.context = context;
        this.uiState = uiState;
        this.vertexPopulationService = vertexPopulationService;
    }

    /**
     * Handles the reset graph action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Clear all domain data
            clearDomainData();

            // Clear all UI state
            clearUiState();

            // Repopulate with initial data using new service architecture
            uiState.setAddingVertex(true);
            vertexPopulationService.populateVertices(context.getGraphService(), uiState);

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
        context.getGraphService().updateGraph(new ru.yanchenko.vlad.graphapp.domain.entities.Graph(new ArrayList<>(), new ArrayList<>()));
        context.getUiState().setPolarCoordinates(new ArrayList<>());
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
