package ru.yanchenko.vlad.graphapp.domain.usecases;

import ru.yanchenko.vlad.graphapp.domain.PopulationKind;
import ru.yanchenko.vlad.graphapp.domain.entities.Graph;
import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexCreationService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexLayoutService;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Creates a ConfirmAction with the specified context and services.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param vertexLayoutService the vertex layout service
     * @param vertexCreationService the vertex creation service
     */
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

    /**
     * Handles the confirm action.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // NEW: Use services directly
        GraphDomainService graphService = context.getGraphService();

        try {
            // Handle vertex editing
            if (uiState.isEditingVertex() && !uiState.getEditBuffer().isEmpty()) {
                confirmVertexEditing(graphService);
            }

            // Handle vertex addition
            if (uiState.isAddingVertex() && !uiState.getEditBuffer().isEmpty()) {
                confirmVertexAddition(graphService);
            }

            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error confirming operation", ex);
            resetToSafeState();
        }
    }

    @Override
    public boolean isActionEnabled() {
        return (uiState.isEditingVertex() || uiState.isAddingVertex()) &&
                !uiState.getEditBuffer().isEmpty();
    }

    /**
     * NEW: Confirm vertex editing using services directly.
     */
    private void confirmVertexEditing(GraphDomainService graphService) {
        String name = sanitize(uiState.getEditBuffer());
        int index = uiState.getSelectedVertexIndex();
        
        List<Vertex> vertices = graphService.getCurrentGraph().getVertices();
        if (index >= 0 && index < vertices.size()) {
            // Create new vertex with updated name
            Vertex oldVertex = vertices.get(index);
            Vertex newVertex = new Vertex((int) oldVertex.getX(), (int) oldVertex.getY(), oldVertex.getRadius(), name);
            
            // Update the graph
            List<Vertex> newVertices = new ArrayList<>(vertices);
            newVertices.set(index, newVertex);
            graphService.updateGraph(new Graph(newVertices, graphService.getCurrentGraph().getEdges()));
            
            uiState.setEditingVertex(false);
            uiState.setEditBuffer("");
            maybeRelayout();

            LOGGER.info("Confirmed vertex editing: " + name);
        }
    }

    /**
     * NEW: Confirm vertex addition using services directly.
     */
    private void confirmVertexAddition(GraphDomainService graphService) {
        String name = sanitize(uiState.getEditBuffer());

        vertexCreationService.addVertexAtCenter(name, graphService);
        context.getUiState().updatePolarCoordinates(graphService.getCurrentGraph().getVertices());
        uiState.setAddingVertex(false);
        uiState.setEditBuffer("");
        maybeRelayout();

        LOGGER.info("Confirmed vertex addition: " + name);
    }

    /**
     * NEW: Relayout using services directly.
     */
    private void maybeRelayout() {
        PopulationKind kind = context.getPopulationKind();
        if (kind == PopulationKind.CIRCULAR_FILE || kind == PopulationKind.HARDCODED_SAMPLE_B) {
            vertexLayoutService.layoutVertices(context.getGraphService().getCurrentGraph().getVertices());
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