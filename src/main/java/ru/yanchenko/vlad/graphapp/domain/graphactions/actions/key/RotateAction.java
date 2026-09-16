package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiData;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;

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
    private final GraphUiData uiData;
    private final GraphUiState uiState;
    private final GraphDomainService graphService;
    private final VertexRotationService vertexRotationService;

    public RotateAction(Direction direction,
                        GraphUiData uiData,
                        GraphUiState uiState,
                        GraphDomainService graphService,
                        VertexRotationService vertexRotationService) {
        super("Rotate " + direction.name().toLowerCase());
        this.uiData = uiData;
        this.uiState = uiState;
        this.direction = direction;
        this.graphService = graphService;
        this.vertexRotationService = vertexRotationService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<VertexPolarCoordinate> polarCoordinates = uiData.getPolarCoordinates();

        if (!canRotate()) {
            LOGGER.warning("Cannot rotate - no vertices or operation in progress");
            return;
        }

        try {
            vertexRotationService.rotateVertices(
                    direction.getIncrement(),
                    graphService.getCurrentGraph().getVertices(),
                    polarCoordinates
            );

            LOGGER.info("Rotated graph " + direction.name().toLowerCase());

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
        return !graphService.getCurrentGraph().getVertices().isEmpty()
                && !uiState.isEditingVertex()
                && !uiState.isAddingVertex()
                && !uiState.isDeletingVertex();
    }

    public Direction getDirection() {
        return direction;
    }
}