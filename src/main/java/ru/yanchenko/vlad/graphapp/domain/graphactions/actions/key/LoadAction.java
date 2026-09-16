package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.FileActionContext;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Action to load graph data from file.
 * Bound to Ctrl+L
 */
public class LoadAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(LoadAction.class.getName());

    private final FileActionContext context;
    private final GraphUiState uiState;

    /**
     * Creates a LoadAction with the specified context and state.
     *
     * @param context the file action context
     * @param uiState the UI state
     */
    public LoadAction(FileActionContext context, GraphUiState uiState) {
        super("Load Graph", KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        this.context = context;
        this.uiState = uiState;
    }

    /**
     * Handles the load graph action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Clear current state before loading
            clearCurrentState();

            // Load graph data from file using new service architecture
            context.getPersistable().loadFromFile(context.getGraphService());

            // Initialize polar coordinates for loaded vertices
            initializePolarCoordinates();

            // Clear any ongoing operations
            clearAllOperations();

            LOGGER.info("Graph loaded successfully from file");
            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to load graph from file", ex);
            handleLoadError(ex);
        }
    }

    @Override
    public boolean isActionEnabled() {
        return !uiState.isEditingVertex() && !uiState.isAddingVertex();
    }

    /**
     * Clear current state before loading new data.
     */
    private void clearCurrentState() {
        uiState.setSelectedVertexIndex(-1);
        uiState.setEditBuffer("");
    }

    /**
     * Initialize polar coordinates for all loaded vertices.
     */
    private void initializePolarCoordinates() {
        // Update polar coordinates using the UI state service
        context.getUiStateService().updatePolarCoordinates(context.getGraphService().getCurrentGraph().getVertices());
    }

    /**
     * Clear all operations after loading.
     */
    private void clearAllOperations() {
        uiState.setAddingVertex(false);
        uiState.setEditingVertex(false);
        uiState.setDeletingVertex(false);
        uiState.setSelectedVertexIndex(-1);
        uiState.setEditBuffer("");
    }

    /**
     * Handle errors that occur during loading.
     */
    private void handleLoadError(Exception ex) {
        // Reset UI state to a safe state
        clearAllOperations();

        LOGGER.warning("Graph loading failed, UI state has been reset");

        // Refresh display to show empty state
        context.getRefreshService().refresh();
    }
}