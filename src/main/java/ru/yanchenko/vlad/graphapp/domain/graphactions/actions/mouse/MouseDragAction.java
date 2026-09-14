package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseDragAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;
    private final VertexPossibleLink defaultPossibleLink;

    public MouseDragAction(GraphActionContext context, GraphUiState uiState, MouseEvent mouseEvent) {
        super("Mouse Drag");
        this.context = context;
        this.uiState = uiState;
        this.mouseEvent = mouseEvent;
        defaultPossibleLink = new VertexPossibleLink();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // NEW: Use services directly instead of legacy VerticesData
        List<Vertex> vertices = context.getGraphService().getCurrentGraph().getVertices();
        VertexLink currentLink = context.getUiStateService().getUiData().getCurrentLink();
        int chosenVertex = currentLink.getLink1();

        boolean ctrlDown = mouseEvent.isControlDown() || mouseEvent.isMetaDown();

        if (!ctrlDown && chosenVertex != -1) {
            vertices.get(chosenVertex).setX(mouseEvent.getX());
            vertices.get(chosenVertex).setY(mouseEvent.getY());
            // Sets to default possible link, i.e. the one with negative coordinates, for when Ctrl is not pressed,
            // the previously drawn possible link not be visible.
            context.getUiStateService().getUiData().setPossibleLink(defaultPossibleLink);
        } else if (chosenVertex != -1) {
            // Handle link preview
            currentLink.setLink2(-1);

            double subjectX1 = vertices.get(currentLink.getLink1()).getX();
            double subjectY1 = vertices.get(currentLink.getLink1()).getY();

            // Calculate angle between vertex center and mouse cursor
            double angle = Geometry.computeAngle(subjectX1, subjectY1,
                    mouseEvent.getPoint().x, mouseEvent.getPoint().y);

            // Set first terminator coordinates
            context.getUiStateService().getUiData().getPossibleLink().setX1(
                    (int) (vertices.get(currentLink.getLink1()).getX() +
                            Math.cos(angle) * (vertices.get(currentLink.getLink1()).getRadius() +
                                    uiState.getVertexLinkMargin())) // Use UI state for margin
            );

            context.getUiStateService().getUiData().getPossibleLink().setY1(
                    (int) (vertices.get(currentLink.getLink1()).getY() -
                            Math.sin(angle) * (vertices.get(currentLink.getLink1()).getRadius() +
                                    uiState.getVertexLinkMargin())) // Use UI state for margin
            );

            // Check if mouse is still within vertex area
            if (Geometry.computeDistance(subjectX1, subjectY1, mouseEvent.getX(), mouseEvent.getY()) <=
                    vertices.get(currentLink.getLink1()).getRadius() + uiState.getVertexLinkMargin()) {

                // Set second terminator to same as first
                context.getUiStateService().getUiData().getPossibleLink().setX2(
                        context.getUiStateService().getUiData().getPossibleLink().getX1()
                );
                context.getUiStateService().getUiData().getPossibleLink().setY2(
                        context.getUiStateService().getUiData().getPossibleLink().getY1()
                );
            } else {
                // Set second terminator to mouse position
                context.getUiStateService().getUiData().getPossibleLink().setX2(mouseEvent.getX());
                context.getUiStateService().getUiData().getPossibleLink().setY2(mouseEvent.getY());
            }

            // Check for intersection with other vertices
            for (int i = 0; i < vertices.size(); i++) {
                Vertex vertex = vertices.get(i);
                if (Geometry.computeDistance(vertex.getX(), vertex.getY(),
                        mouseEvent.getX(), mouseEvent.getY()) <=
                        vertex.getRadius() + uiState.getVertexLinkMargin()) {

                    if (i != currentLink.getLink1()) {
                        // Calculate angle to target vertex
                        double angle2 = Geometry.computeAngle(subjectX1, subjectY1,
                                vertex.getX(), vertex.getY()) + Math.PI;

                        // Set second terminator to target vertex
                        context.getUiStateService().getUiData().getPossibleLink().setX2(
                                (int) (vertex.getX() + Math.cos(angle2) *
                                        (vertex.getRadius() + uiState.getVertexLinkMargin()))
                        );

                        context.getUiStateService().getUiData().getPossibleLink().setY2(
                                (int) (vertex.getY() - Math.sin(angle2) *
                                        (vertex.getRadius() + uiState.getVertexLinkMargin()))
                        );

                        // Recalculate first terminator for stability
                        angle = Geometry.computeAngle(subjectX1, subjectY1,
                                context.getUiStateService().getUiData().getPossibleLink().getX2(),
                                context.getUiStateService().getUiData().getPossibleLink().getY2());

                        context.getUiStateService().getUiData().getPossibleLink().setX1(
                                (int) (vertices.get(currentLink.getLink1()).getX() +
                                        Math.cos(angle) * (vertices.get(currentLink.getLink1()).getRadius() +
                                                uiState.getVertexLinkMargin()))
                        );

                        context.getUiStateService().getUiData().getPossibleLink().setY1(
                                (int) (vertices.get(currentLink.getLink1()).getY() -
                                        Math.sin(angle) * (vertices.get(currentLink.getLink1()).getRadius() +
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