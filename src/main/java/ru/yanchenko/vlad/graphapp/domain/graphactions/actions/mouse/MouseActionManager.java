package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse;

import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexDeletionService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexRotationService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

import java.awt.event.*;

/**
 * Manages mouse interactions using the Action system.
 * Converts mouse events into actions that can be executed.
 */
public class MouseActionManager implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private boolean isRightMouseButton = false;
    private final VertexDeletionService vertexDeletionService;
    private final VertexRotationService vertexRotationService;

    public MouseActionManager(GraphActionContext context,
                              GraphUiState uiState,
                              VertexDeletionService vertexDeletionService,
                              VertexRotationService vertexRotationService) {
        this.context = context;
        this.uiState = uiState;
        this.vertexDeletionService = vertexDeletionService;
        this.vertexRotationService = vertexRotationService;
    }

    // MouseListener implementation
    @Override
    public void mouseClicked(MouseEvent e) {
        // No action needed for click
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Handle right mouse button state
        isRightMouseButton = e.getButton() == MouseEvent.BUTTON3;

        // Create and execute click action
        MouseClickAction clickAction = new MouseClickAction(context, uiState, e);
        clickAction.actionPerformed(null);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Create and execute release action
        MouseReleaseAction releaseAction = new MouseReleaseAction(
                context,
                uiState,
                e,
                isRightMouseButton,
                vertexDeletionService);
        releaseAction.actionPerformed(null);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // No action needed
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // No action needed
    }

    // MouseMotionListener implementation
    @Override
    public void mouseDragged(MouseEvent e) {
        // Create and execute drag action
        MouseDragAction dragAction = new MouseDragAction(context, uiState, e);
        dragAction.actionPerformed(null);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update mouse position in UI state
        uiState.setCurrentMousePosition(e.getPoint());
    }

    // MouseWheelListener implementation
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Create and execute wheel action
        MouseWheelAction wheelAction = new MouseWheelAction(context, e, vertexRotationService);
        wheelAction.actionPerformed(null);
    }

    public boolean isRightMouseButton() {
        return isRightMouseButton;
    }
}