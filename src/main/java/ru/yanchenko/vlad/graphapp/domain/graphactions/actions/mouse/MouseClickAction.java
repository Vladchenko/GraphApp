package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseClickAction extends GraphAction {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final MouseEvent mouseEvent;

    public MouseClickAction(GraphActionContext context, GraphUiState uiState, MouseEvent mouseEvent) {
        super("Mouse Click");
        this.context = context;
        this.uiState = uiState;
        this.mouseEvent = mouseEvent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();
        List<Vertex> vertices = verticesData.getVertices();

        // Handle vertex selection
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            if (Geometry.computeDistance(vertex.getX(), vertex.getY(),
                    mouseEvent.getX(), mouseEvent.getY()) <= vertex.getRadius()) {
                // Update UI state for selection
                uiState.setSelectedVertexIndex(i);
                verticesData.getVertexLink().setLink1(i);
            }
        }
        context.getRefreshService().refresh();
    }
}