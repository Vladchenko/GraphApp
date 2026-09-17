package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.GraphAction;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;

import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import static ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService.RADIAN_INCREMENT;

/**
 * Handles mouse wheel events to rotate all vertices around the screen center.
 * <p>
 * Scroll up rotates vertices clockwise, scroll down rotates counter-clockwise.
 *
 * @see GraphAction
 * @see VertexRotationService
 */
public class MouseWheelAction extends GraphAction {

    private final GraphActionContext context;
    private final MouseWheelEvent mouseWheelEvent;
    private final VertexRotationService vertexRotationService;

    /**
     * Creates a MouseWheelAction with the specified context and event.
     *
     * @param context the graph action context
     * @param mouseWheelEvent the mouse wheel event
     * @param vertexRotationService the vertex rotation service
     */
    public MouseWheelAction(GraphActionContext context,
                            MouseWheelEvent mouseWheelEvent,
                            VertexRotationService vertexRotationService) {
        super("Mouse Wheel");
        this.context = context;
        this.mouseWheelEvent = mouseWheelEvent;
        this.vertexRotationService = vertexRotationService;
    }

    /**
     * Handles the mouse wheel event.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<VertexPolarCoordinate> polarCoordinates = context.getUiState().getPolarCoordinates();

        if (!polarCoordinates.isEmpty()) {
            double rotation = mouseWheelEvent.getPreciseWheelRotation(); // or getWheelRotation()
            if (rotation == 0.0) {
                return; // no movement
            }
            double increment = (rotation > 0.0) ? -RADIAN_INCREMENT : RADIAN_INCREMENT;

            vertexRotationService.rotateVertices(
                    increment,
                    context.getGraphService().getCurrentGraph().getVertices(),
                    polarCoordinates
            );
        }
        context.getRefreshService().refresh();
    }
}