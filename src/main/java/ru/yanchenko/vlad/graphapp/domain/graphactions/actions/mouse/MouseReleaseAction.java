package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Handles mouse release events to finalize vertex selection and link creation/deletion.
 * <p>
 * Left-click adds a link between two vertices; right-click removes it.
 *
 * @see GraphAction
 * @see MouseClickAction
 * @see MouseDragAction
 */
public class MouseReleaseAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;
    private final boolean isRightMouseButton;

    /**
     * Creates a MouseReleaseAction with the specified context and event.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param mouseEvent the mouse event
     * @param isRightMouseButton true if the right mouse button was released
     */
    public MouseReleaseAction(GraphActionContext context,
                              GraphUiState uiState,
                              MouseEvent mouseEvent,
                              boolean isRightMouseButton) {
        super("Mouse Release");
        this.context = context;
        this.uiState = uiState;
        this.mouseEvent = mouseEvent;
        this.isRightMouseButton = isRightMouseButton;
    }

    /**
     * Handles the mouse release event.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Vertex> vertices = context.getGraphService().getCurrentGraph().getVertices();
        int noVertexChosen = 0;
        Edge currentEdge = context.getUiState().getEdgeBeingCreated();

        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);

            if (Geometry.computeDistance(
                    vertex.getX(),
                    vertex.getY(),
                    mouseEvent.getX(),
                    mouseEvent.getY()) <= vertex.getRadius()) {

                // Mark vertex as clicked in UI state
                uiState.setSelectedVertexIndex(i);

                // Handle link creation/deletion
                if (currentEdge.from() != i) {
                    context.getUiState().setEdgeBeingCreated(new Edge(currentEdge.from(), i));

                    if (isRightMouseButton) {
                        // Remove link using the graph service
                        context.getGraphService().removeEdge(new Edge(currentEdge.from(), i));
                    } else {
                        // Add link using the graph service
                        context.getGraphService().addEdge(new Edge(currentEdge.from(), i));
                    }
                } else {
                    // Clear link selection
                    context.getUiState().setEdgeBeingCreated(new Edge(-1, -1));
                }
            } else {
                noVertexChosen++;
            }

            // Clear link selection if no second vertex chosen
            if (noVertexChosen == vertices.size()) {
                context.getUiState().setEdgeBeingCreated(new Edge(-1, -1));
            }
        }

        // Reset editing state and clear drag preview
        uiState.setEditingVertex(false);
        context.getUiState().setEdgeBeingCreated(new Edge(-1, -1));
        context.getUiState().getDragPreview().setX1(-10);
        context.getUiState().getDragPreview().setY1(-10);
        context.getUiState().getDragPreview().setX2(-10);
        context.getUiState().getDragPreview().setY2(-10);

        context.getRefreshService().refresh();
    }
}