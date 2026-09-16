package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseReleaseAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;
    private final boolean isRightMouseButton;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Vertex> vertices = context.getGraphService().getCurrentGraph().getVertices();
        int noVertexChosen = 0;
        VertexLink currentLink = context.getUiStateService().getUiData().getCurrentLink();

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
                if (currentLink.getLink1() != i) {
                    currentLink.setLink2(i);

                    if (isRightMouseButton) {
                        // Remove link using the graph service
                        context.getGraphService().removeEdge(new Edge(currentLink.getLink1(), currentLink.getLink2()));
                    } else {
                        // Add link using the graph service
                        context.getGraphService().addEdge(new Edge(currentLink.getLink1(), currentLink.getLink2()));
                    }
                } else {
                    // Clear link selection
                    currentLink.setLink1(-1);
                }
            } else {
                noVertexChosen++;
            }

            // Clear link selection if no second vertex chosen
            if (noVertexChosen == vertices.size()) {
                currentLink.setLink1(-1);
                currentLink.setLink2(-1);
            }
        }

        // Reset editing state and clear possible link
        uiState.setEditingVertex(false);
        currentLink.setLink1(-1);
        currentLink.setLink2(-1);
        context.getUiStateService().getUiData().getPossibleLink().setX1(-10);
        context.getUiStateService().getUiData().getPossibleLink().setY1(-10);
        context.getUiStateService().getUiData().getPossibleLink().setX2(-10);
        context.getUiStateService().getUiData().getPossibleLink().setY2(-10);

        context.getRefreshService().refresh();
    }
}