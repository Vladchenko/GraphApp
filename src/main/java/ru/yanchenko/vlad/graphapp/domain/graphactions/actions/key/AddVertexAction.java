package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

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

    public AddVertexAction(GraphActionContext context, GraphUiState uiState) {
        super("Add Vertex");
        this.context = context;
        this.uiState = uiState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();

        if (!uiState.isEditingVertex()) {
            // Clear any existing selection
            clearSelection(verticesData);

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
    private void clearSelection(VerticesData verticesData) {
        // Clear any ongoing link creation
        verticesData.getVertexLink().setLink1(-1);
        verticesData.getVertexLink().setLink2(-1);
    }
}