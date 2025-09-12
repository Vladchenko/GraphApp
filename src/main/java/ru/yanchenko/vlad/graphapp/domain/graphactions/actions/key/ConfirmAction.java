package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexCreationService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexLayoutService;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Action to confirm current operation (add/edit vertex).
 * Bound to Enter key.
 */
public class ConfirmAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(ConfirmAction.class.getName());

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final VertexLayoutService vertexLayoutService;
    private final VertexCreationService vertexCreationService;

    public ConfirmAction(GraphActionContext context,
                         GraphUiState uiState,
                         VertexLayoutService vertexLayoutService,
                         VertexCreationService vertexCreationService) {
        super("Confirm", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        this.context = context;
        this.uiState = uiState;
        this.vertexLayoutService = vertexLayoutService;
        this.vertexCreationService = vertexCreationService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();

        try {
            // Handle vertex editing
            if (uiState.isEditingVertex() && !uiState.getEditBuffer().isEmpty()) {
                confirmVertexEditing(verticesData);
            }

            // Handle vertex addition
            if (uiState.isAddingVertex() && !uiState.getEditBuffer().isEmpty()) {
                confirmVertexAddition(verticesData);
            }

            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error confirming operation", ex);
            // Reset to safe state
            resetToSafeState();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return (uiState.isEditingVertex() || uiState.isAddingVertex()) &&
                !uiState.getEditBuffer().isEmpty();
    }

    /**
     * Confirm vertex editing operation.
     */
    private void confirmVertexEditing(VerticesData verticesData) {
        String name = sanitize(uiState.getEditBuffer());
        int index = uiState.getSelectedVertexIndex();

        if (index >= 0 && index < verticesData.getVertices().size()) {
            verticesData.getVertices().get(index).setVertexName(name);
            uiState.setEditingVertex(false);
            uiState.setEditBuffer("");
            maybeRelayout();

            LOGGER.info("Confirmed vertex editing: " + name);
        }
    }

    /**
     * Confirm vertex addition operation.
     */
    private void confirmVertexAddition(VerticesData verticesData) {
        String name = sanitize(uiState.getEditBuffer());

        vertexCreationService.addVertexAtCenter(name, verticesData);
        verticesData.getVerticesPolarCoordinates().add(new VertexPolarCoordinate());
        uiState.setAddingVertex(false);
        uiState.setEditBuffer("");
        maybeRelayout();

        LOGGER.info("Confirmed vertex addition: " + name);
    }

    /**
     * Relayout vertices if needed based on population kind.
     */
    private void maybeRelayout() {
        PopulationKind kind = context.getPopulationKind();
        if (kind == PopulationKind.CIRCULAR_FILE || kind == PopulationKind.HARDCODED_SAMPLE_B) {
            vertexLayoutService.layoutVertices(context.getVerticesData().getVertices());
        }
    }

    /**
     * Sanitize input string by removing line endings.
     */
    private String sanitize(String s) {
        if (s.endsWith("\n")) return s.substring(0, s.length() - 1);
        if (s.endsWith("\r")) return s.substring(0, s.length() - 1);
        return s;
    }

    /**
     * Reset to safe state in case of error.
     */
    private void resetToSafeState() {
        uiState.setAddingVertex(false);
        uiState.setEditingVertex(false);
        uiState.setEditBuffer("");
        context.getRefreshService().refresh();
    }
}