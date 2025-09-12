package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Logger;

import static ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService.RADIAN_INCREMENT;

/**
 * Action to rotate the graph.
 * Bound to arrow keys.
 */
public class RotateAction extends GraphAction {
    private static final Logger LOGGER = Logger.getLogger(RotateAction.class.getName());

    public enum Direction {
        CLOCKWISE(-RADIAN_INCREMENT),
        COUNTER_CLOCKWISE(RADIAN_INCREMENT);

        private final double increment;

        Direction(double increment) {
            this.increment = increment;
        }

        public double getIncrement() {
            return increment;
        }
    }

    private final Direction direction;
    private final GraphActionContext context;
    private final GraphUiState uiState;
    private final VertexRotationService vertexRotationService;

    public RotateAction(Direction direction,
                        GraphActionContext context,
                        GraphUiState uiState,
                        VertexRotationService vertexRotationService) {
        super("Rotate " + direction.name().toLowerCase());
        this.context = context;
        this.direction = direction;
        this.uiState = uiState;
        this.vertexRotationService = vertexRotationService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VerticesData verticesData = context.getVerticesData();
        List<VertexPolarCoordinate> polarCoordinates = verticesData.getVerticesPolarCoordinates();

        if (!canRotate()) {
            LOGGER.warning("Cannot rotate - no vertices or operation in progress");
            return;
        }

        try {
            vertexRotationService.rotateVertices(
                    direction.getIncrement(),
                    verticesData.getVertices(),
                    polarCoordinates
            );

            LOGGER.info("Rotated graph " + direction.name().toLowerCase());
            context.getRefreshService().refresh();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error rotating graph", ex);
        }
    }

    @Override
    public boolean isActionEnabled() {
        return canRotate();
    }

    /**
     * Check if rotation can be performed.
     */
    private boolean canRotate() {
        return !context.getVerticesData().getVertices().isEmpty() &&
                !uiState.isEditingVertex() &&
                !uiState.isAddingVertex() &&
                !uiState.isDeletingVertex();
    }

    public Direction getDirection() {
        return direction;
    }
}