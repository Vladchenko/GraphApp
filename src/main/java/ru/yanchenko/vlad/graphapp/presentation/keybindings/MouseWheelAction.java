package ru.yanchenko.vlad.graphapp.presentation.keybindings;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.domain.services.VertexRotationService;
import ru.yanchenko.vlad.graphapp.domain.usecases.GraphAction;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import static ru.yanchenko.vlad.graphapp.domain.services.VertexRotationService.RADIAN_INCREMENT;

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
    private final GraphUiState uiState;
    private final MouseWheelEvent mouseWheelEvent;
    private final VertexRotationService vertexRotationService;

    /**
     * Creates a MouseWheelAction with the specified context and event.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param mouseWheelEvent the mouse wheel event
     * @param vertexRotationService the vertex rotation service
     */
    public MouseWheelAction(GraphActionContext context,
                            GraphUiState uiState,
                            MouseWheelEvent mouseWheelEvent,
                            VertexRotationService vertexRotationService) {
        super("Mouse Wheel");
        this.context = context;
        this.uiState = uiState;
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
        List<Vertex> vertices = context.getGraphService().getCurrentGraph().getVertices();

        if (!vertices.isEmpty()) {
            double rotation = mouseWheelEvent.getPreciseWheelRotation();
            if (rotation == 0.0) {
                return; // no movement
            }
            double increment = (rotation > 0.0) ? -RADIAN_INCREMENT : RADIAN_INCREMENT;

            vertexRotationService.rotateVertices(
                    increment,
                    vertices,
                    uiState.getPolarCoordinates()
            );
        }
        context.getRefreshService().refresh();
    }
}
