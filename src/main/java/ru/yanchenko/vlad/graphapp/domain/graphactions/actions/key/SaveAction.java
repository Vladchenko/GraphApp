package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.FileActionContext;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

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

    public SaveAction(FileActionContext context, GraphUiState uiState) {
        super("Save Graph", KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        this.context = context;
        this.uiState = uiState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Save graph data to file
            context.getPersistable().saveToFile(context.getVerticesData());

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