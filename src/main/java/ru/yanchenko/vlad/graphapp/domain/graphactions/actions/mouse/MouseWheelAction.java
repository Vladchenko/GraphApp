package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import static ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService.RADIAN_INCREMENT;

public class MouseWheelAction extends GraphAction {

    private final GraphActionContext context;
    private final MouseWheelEvent mouseWheelEvent;
    private final VertexRotationService vertexRotationService;

    public MouseWheelAction(GraphActionContext context,
                            MouseWheelEvent mouseWheelEvent,
                            VertexRotationService vertexRotationService) {
        super("Mouse Wheel");
        this.context = context;
        this.mouseWheelEvent = mouseWheelEvent;
        this.vertexRotationService = vertexRotationService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();
        List<VertexPolarCoordinate> polarCoordinates = verticesData.getVerticesPolarCoordinates();

        if (!polarCoordinates.isEmpty()) {
            double rotation = mouseWheelEvent.getPreciseWheelRotation(); // or getWheelRotation()
            if (rotation == 0.0) {
                return; // no movement
            }
            double increment = (rotation > 0.0) ? -RADIAN_INCREMENT : RADIAN_INCREMENT;

            vertexRotationService.rotateVertices(
                    increment,
                    verticesData.getVertices(),
                    polarCoordinates
            );
        }
        context.getRefreshService().refresh();
    }
}