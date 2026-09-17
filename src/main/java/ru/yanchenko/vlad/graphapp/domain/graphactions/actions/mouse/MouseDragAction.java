package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.DragPreview;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Handles mouse drag events for vertex repositioning and link preview.
 * <p>
 * When Ctrl is held, draws a preview line from the selected vertex to the cursor.
 * When Ctrl is not held, drags the selected vertex to the new position.
 *
 * @see GraphAction
 * @see MouseClickAction
 * @see MouseReleaseAction
 */
public class MouseDragAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;
    private final DragPreview defaultDragPreview;

    /**
     * Creates a MouseDragAction with the specified context and event.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param mouseEvent the mouse event containing drag coordinates
     */
    public MouseDragAction(GraphActionContext context, GraphUiState uiState, MouseEvent mouseEvent) {
        super("Mouse Drag");
        this.context = context;
        this.uiState = uiState;
        this.mouseEvent = mouseEvent;
        defaultDragPreview = new DragPreview();
    }

    /**
     * Handles the mouse drag event.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // NEW: Use services directly instead of legacy VerticesData
        List<Vertex> vertices = context.getGraphService().getCurrentGraph().getVertices();
        Edge currentEdge = context.getUiStateService().getUiData().getEdgeBeingCreated();
        int chosenVertex = currentEdge.from();

        boolean ctrlDown = mouseEvent.isControlDown() || mouseEvent.isMetaDown();

        if (!ctrlDown && chosenVertex != -1) {
            vertices.get(chosenVertex).setX(mouseEvent.getX());
            vertices.get(chosenVertex).setY(mouseEvent.getY());
            // Reset drag preview to default (negative coordinates) so the previously drawn
            // drag line is not visible when Ctrl is not pressed.
            context.getUiStateService().getUiData().setDragPreview(defaultDragPreview);
        } else if (chosenVertex != -1) {
            // Handle link preview
            context.getUiStateService().getUiData().setEdgeBeingCreated(new Edge(chosenVertex, -1));

            double subjectX1 = vertices.get(currentEdge.from()).getX();
            double subjectY1 = vertices.get(currentEdge.from()).getY();

            // Calculate angle between vertex center and mouse cursor
            double angle = Geometry.computeAngle(subjectX1, subjectY1,
                    mouseEvent.getPoint().x, mouseEvent.getPoint().y);

            // Set first terminator coordinates
            context.getUiStateService().getUiData().getDragPreview().setX1(
                    (int) (vertices.get(currentEdge.from()).getX() +
                            Math.cos(angle) * (vertices.get(currentEdge.from()).getRadius() +
                                    uiState.getVertexLinkMargin())) // Use UI state for margin
            );

            context.getUiStateService().getUiData().getDragPreview().setY1(
                    (int) (vertices.get(currentEdge.from()).getY() -
                            Math.sin(angle) * (vertices.get(currentEdge.from()).getRadius() +
                                    uiState.getVertexLinkMargin())) // Use UI state for margin
            );

            // Check if mouse is still within vertex area
            if (Geometry.computeDistance(subjectX1, subjectY1, mouseEvent.getX(), mouseEvent.getY()) <=
                    vertices.get(currentEdge.from()).getRadius() + uiState.getVertexLinkMargin()) {

                // Set second terminator to same as first
                context.getUiStateService().getUiData().getDragPreview().setX2(
                        context.getUiStateService().getUiData().getDragPreview().getX1()
                );
                context.getUiStateService().getUiData().getDragPreview().setY2(
                        context.getUiStateService().getUiData().getDragPreview().getY1()
                );
            } else {
                // Set second terminator to mouse position
                context.getUiStateService().getUiData().getDragPreview().setX2(mouseEvent.getX());
                context.getUiStateService().getUiData().getDragPreview().setY2(mouseEvent.getY());
            }

            // Check for intersection with other vertices
            for (int i = 0; i < vertices.size(); i++) {
                Vertex vertex = vertices.get(i);
                if (Geometry.computeDistance(vertex.getX(), vertex.getY(),
                        mouseEvent.getX(), mouseEvent.getY()) <=
                        vertex.getRadius() + uiState.getVertexLinkMargin()) {

                    if (i != currentEdge.from()) {
                        // Calculate angle to target vertex
                        double angle2 = Geometry.computeAngle(subjectX1, subjectY1,
                                vertex.getX(), vertex.getY()) + Math.PI;

                        // Set second terminator to target vertex
                        context.getUiStateService().getUiData().getDragPreview().setX2(
                                (int) (vertex.getX() + Math.cos(angle2) *
                                        (vertex.getRadius() + uiState.getVertexLinkMargin()))
                        );

                        context.getUiStateService().getUiData().getDragPreview().setY2(
                                (int) (vertex.getY() - Math.sin(angle2) *
                                        (vertex.getRadius() + uiState.getVertexLinkMargin()))
                        );

                        // Recalculate first terminator for stability
                        angle = Geometry.computeAngle(subjectX1, subjectY1,
                                context.getUiStateService().getUiData().getDragPreview().getX2(),
                                context.getUiStateService().getUiData().getDragPreview().getY2());

                        context.getUiStateService().getUiData().getDragPreview().setX1(
                                (int) (vertices.get(currentEdge.from()).getX() +
                                        Math.cos(angle) * (vertices.get(currentEdge.from()).getRadius() +
                                                uiState.getVertexLinkMargin()))
                        );

                        context.getUiStateService().getUiData().getDragPreview().setY1(
                                (int) (vertices.get(currentEdge.from()).getY() -
                                        Math.sin(angle) * (vertices.get(currentEdge.from()).getRadius() +
                                                uiState.getVertexLinkMargin()))
                        );
                    }
                }
            }
        }

        // Update mouse position in UI state
        uiState.setCurrentMousePosition(mouseEvent.getPoint());
        context.getRefreshService().refresh();
    }
}