package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexDeletionService;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseReleaseAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;
    private final boolean isRightMouseButton;
    private final VertexDeletionService vertexDeletionService;

    public MouseReleaseAction(GraphActionContext context,
                              GraphUiState uiState,
                              MouseEvent mouseEvent,
                              boolean isRightMouseButton,
                              VertexDeletionService vertexDeletionService) {
        super("Mouse Release");
        this.context = context;
        this.uiState = uiState;
        this.mouseEvent = mouseEvent;
        this.isRightMouseButton = isRightMouseButton;
        this.vertexDeletionService = vertexDeletionService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();
        List<Vertex> vertices = verticesData.getVertices();
        int noVertexChosen = 0;

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
                if (verticesData.getVertexLink().getLink1() != i) {
                    verticesData.getVertexLink().setLink2(i);

                    if (isRightMouseButton) {
                        // Remove link
                        vertexDeletionService.deleteVerticesLink(
                                verticesData.getVertexLink().getLink1(),
                                verticesData.getVertexLink().getLink2(),
                                verticesData.getVerticesLinks()
                        );
                    } else {
                        // Add link
                        verticesData.getVerticesLinks().add(
                                new VertexLink(
                                        verticesData.getVertexLink().getLink1(),
                                        verticesData.getVertexLink().getLink2()
                                )
                        );
                    }
                } else {
                    // Clear link selection
                    verticesData.getVertexLink().setLink1(-1);
                }
            } else {
                noVertexChosen++;
            }

            // Clear link selection if no second vertex chosen
            if (noVertexChosen == vertices.size()) {
                verticesData.getVertexLink().setLink1(-1);
                verticesData.getVertexLink().setLink2(-1);
            }
        }

        // Reset editing state and clear possible link
        uiState.setEditingVertex(false); // Use UI state instead of domain data
        verticesData.getVertexLink().setLink1(-1);
        verticesData.getVertexLink().setLink2(-1);
        verticesData.getVertexPossibleLink().setX1(-10);
        verticesData.getVertexPossibleLink().setY1(-10);
        verticesData.getVertexPossibleLink().setX2(-10);
        verticesData.getVertexPossibleLink().setY2(-10);

        context.getRefreshService().refresh();
    }
}