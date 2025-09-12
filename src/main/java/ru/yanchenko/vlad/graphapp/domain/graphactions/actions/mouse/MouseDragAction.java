package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseDragAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;

    public MouseDragAction(GraphActionContext context, GraphUiState uiState, MouseEvent mouseEvent) {
        super("Mouse Drag");
        this.context = context;
        this.uiState = uiState;
        this.mouseEvent = mouseEvent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();
        List<Vertex> vertices = verticesData.getVertices();
        int chosenVertex = verticesData.getVertexLink().getLink1();
        VertexLink vertexLink = verticesData.getVertexLink();

        boolean ctrlDown = mouseEvent.isControlDown() || mouseEvent.isMetaDown();

        if (ctrlDown && chosenVertex != -1) {
            vertices.get(chosenVertex).setX(mouseEvent.getX());
            vertices.get(chosenVertex).setY(mouseEvent.getY());
        } else if (chosenVertex != -1) {
            // Handle link preview
            vertexLink.setLink2(-1);

            double subjectX1 = vertices.get(vertexLink.getLink1()).getX();
            double subjectY1 = vertices.get(vertexLink.getLink1()).getY();

            // Calculate angle between vertex center and mouse cursor
            double angle = Geometry.computeAngle(subjectX1, subjectY1,
                    mouseEvent.getPoint().x, mouseEvent.getPoint().y);

            // Set first terminator coordinates
            verticesData.getVertexPossibleLink().setX1(
                    (int) (vertices.get(vertexLink.getLink1()).getX() +
                            Math.cos(angle) * (vertices.get(vertexLink.getLink1()).getRadius() +
                                    uiState.getVertexLinkMargin())) // Use UI state for margin
            );

            verticesData.getVertexPossibleLink().setY1(
                    (int) (vertices.get(vertexLink.getLink1()).getY() -
                            Math.sin(angle) * (vertices.get(vertexLink.getLink1()).getRadius() +
                                    uiState.getVertexLinkMargin())) // Use UI state for margin
            );

            // Check if mouse is still within vertex area
            if (Geometry.computeDistance(subjectX1, subjectY1, mouseEvent.getX(), mouseEvent.getY()) <=
                    vertices.get(vertexLink.getLink1()).getRadius() + uiState.getVertexLinkMargin()) {

                // Set second terminator to same as first
                verticesData.getVertexPossibleLink().setX2(
                        verticesData.getVertexPossibleLink().getX1()
                );
                verticesData.getVertexPossibleLink().setY2(
                        verticesData.getVertexPossibleLink().getY1()
                );
            } else {
                // Set second terminator to mouse position
                verticesData.getVertexPossibleLink().setX2(mouseEvent.getX());
                verticesData.getVertexPossibleLink().setY2(mouseEvent.getY());
            }

            // Check for intersection with other vertices
            for (int i = 0; i < vertices.size(); i++) {
                Vertex vertex = vertices.get(i);
                if (Geometry.computeDistance(vertex.getX(), vertex.getY(),
                        mouseEvent.getX(), mouseEvent.getY()) <=
                        vertex.getRadius() + uiState.getVertexLinkMargin()) {

                    if (i != verticesData.getVertexLink().getLink1()) {
                        // Calculate angle to target vertex
                        double angle2 = Geometry.computeAngle(subjectX1, subjectY1,
                                vertex.getX(), vertex.getY()) + Math.PI;

                        // Set second terminator to target vertex
                        verticesData.getVertexPossibleLink().setX2(
                                (int) (vertex.getX() + Math.cos(angle2) *
                                        (vertex.getRadius() + uiState.getVertexLinkMargin()))
                        );

                        verticesData.getVertexPossibleLink().setY2(
                                (int) (vertex.getY() - Math.sin(angle2) *
                                        (vertex.getRadius() + uiState.getVertexLinkMargin()))
                        );

                        // Recalculate first terminator for stability
                        angle = Geometry.computeAngle(subjectX1, subjectY1,
                                verticesData.getVertexPossibleLink().getX2(),
                                verticesData.getVertexPossibleLink().getY2());

                        verticesData.getVertexPossibleLink().setX1(
                                (int) (vertices.get(vertexLink.getLink1()).getX() +
                                        Math.cos(angle) * (vertices.get(vertexLink.getLink1()).getRadius() +
                                                uiState.getVertexLinkMargin()))
                        );

                        verticesData.getVertexPossibleLink().setY1(
                                (int) (vertices.get(vertexLink.getLink1()).getY() -
                                        Math.sin(angle) * (vertices.get(vertexLink.getLink1()).getRadius() +
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