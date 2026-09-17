package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexCreationService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexLayoutService;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.domain.Graph;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

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
        GraphUiStateService uiStateService = context.getUiStateService();

        try {
            // Handle vertex editing
            if (uiState.isEditingVertex() && !uiState.getEditBuffer().isEmpty()) {
                confirmVertexEditing(graphService);
            }

            // Handle vertex addition
            if (uiState.isAddingVertex() && !uiState.getEditBuffer().isEmpty()) {
                confirmVertexAddition(graphService, uiStateService);
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
    private void confirmVertexAddition(GraphDomainService graphService, GraphUiStateService uiStateService) {
        String name = sanitize(uiState.getEditBuffer());

        vertexCreationService.addVertexAtCenter(name, graphService);
        uiStateService.getUiData().updatePolarCoordinates(graphService.getCurrentGraph().getVertices());
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