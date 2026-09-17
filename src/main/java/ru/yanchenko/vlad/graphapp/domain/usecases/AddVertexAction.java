package ru.yanchenko.vlad.graphapp.domain.usecases;

import ru.yanchenko.vlad.graphapp.domain.entities.Edge;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Action to start adding a new vertex.
 * Bound to +, =, and Shift+= keys.
 */
public class AddVertexAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(AddVertexAction.class.getName());

    private final GraphActionContext context;
    private final GraphUiState uiState;

    /**
     * Creates an AddVertexAction with the specified context and state.
     *
     * @param context the graph action context
     * @param uiState the UI state containing editing flags
     */
    public AddVertexAction(GraphActionContext context, GraphUiState uiState) {
        super("Add Vertex");
        this.context = context;
        this.uiState = uiState;
    }

    /**
     * Handles the add vertex action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!uiState.isEditingVertex()) {
            // Clear any existing selection
            clearSelection();

            // Set UI state for vertex addition
            uiState.setAddingVertex(true);
            uiState.setSelectedVertexIndex(-1);
            uiState.setEditBuffer("");

            // Clear any other UI operations
            uiState.setEditingVertex(false);
            uiState.setDeletingVertex(false);

            LOGGER.info("Started vertex addition mode");
            context.getRefreshService().refresh();
        }

        // Consume the event to prevent it from being processed as text input
        if (e.getSource() instanceof KeyEvent) {
            ((KeyEvent) e.getSource()).consume();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return !uiState.isEditingVertex() && !uiState.isAddingVertex();
    }

    /**
     * Clear vertex selection in domain data.
     */
    private void clearSelection() {
        // Clear any ongoing edge creation
        context.getUiState().setEdgeBeingCreated(new Edge(-1, -1));
    }
}