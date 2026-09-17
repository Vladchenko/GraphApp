package ru.yanchenko.vlad.graphapp.domain.usecases;

import ru.yanchenko.vlad.graphapp.infrastructure.contexts.FileActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Action to save graph data to file.
 * Bound to Ctrl+S
 */
public class SaveAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(SaveAction.class.getName());

    private final FileActionContext context;
    private final GraphUiState uiState;

    /**
     * Creates a SaveAction with the specified context and state.
     *
     * @param context the file action context
     * @param uiState the UI state
     */
    public SaveAction(FileActionContext context, GraphUiState uiState) {
        super("Save Graph", KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        this.context = context;
        this.uiState = uiState;
    }

    /**
     * Handles the save graph action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Save graph data to file using new service architecture
            context.getPersistable().saveToFile(context.getGraphService());

            LOGGER.info("Graph saved successfully to file");
            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to save to file", ex);
            handleSaveError(ex);
        }
    }

    @Override
    public boolean isActionEnabled() {
        return !uiState.isEditingVertex() && !uiState.isAddingVertex();
    }

    /**
     * Handle errors that occur during saving.
     */
    private void handleSaveError(Exception ex) {
        LOGGER.warning("Save operation failed, but UI state remains unchanged");
        // Note: We don't reset UI state on save error since the user might want to retry
    }
}
