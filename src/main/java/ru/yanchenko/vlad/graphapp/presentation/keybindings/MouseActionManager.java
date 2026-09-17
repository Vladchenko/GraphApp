package ru.yanchenko.vlad.graphapp.presentation.keybindings;

import ru.yanchenko.vlad.graphapp.domain.services.VertexRotationService;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;

import java.awt.event.*;

/**
 * Manages mouse interactions using the Action system.
 * Converts mouse events into actions that can be executed.
 */
public class MouseActionManager implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final GraphActionContext context;
    private final GraphUiState uiState;
    private boolean isRightMouseButton = false;
    private final VertexRotationService vertexRotationService;

    /**
     * Creates a MouseActionManager with the specified context and services.
     *
     * @param context the graph action context
     * @param uiState the UI state
     * @param vertexRotationService the vertex rotation service
     */
    public MouseActionManager(GraphActionContext context,
                              GraphUiState uiState,
                              VertexRotationService vertexRotationService) {
        this.context = context;
        this.uiState = uiState;
        this.vertexRotationService = vertexRotationService;
    }

    // MouseListener implementation
    /**
     * Handles mouse click events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // No action needed for click
    }

    /**
     * Handles mouse pressed events.
     *
     * @param e the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Handle right mouse button state
        isRightMouseButton = e.getButton() == MouseEvent.BUTTON3;

        // Create and execute click action
        MouseClickAction clickAction = new MouseClickAction(context, uiState, e);
        clickAction.actionPerformed(null);
    }

    /**
     * Handles mouse released events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // Create and execute release action
        MouseReleaseAction releaseAction = new MouseReleaseAction(
                context,
                uiState,
                e,
                isRightMouseButton);
        releaseAction.actionPerformed(null);
    }

    /**
     * Handles mouse entered events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // No action needed
    }

    /**
     * Handles mouse exited events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // No action needed
    }

    // MouseMotionListener implementation
    /**
     * Handles mouse dragged events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // Create and execute drag action
        MouseDragAction dragAction = new MouseDragAction(context, uiState, e);
        dragAction.actionPerformed(null);
    }

    /**
     * Handles mouse moved events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Update mouse position in UI state
        uiState.setCurrentMousePosition(e.getPoint());
    }

    // MouseWheelListener implementation
    /**
     * Handles mouse wheel moved events.
     *
     * @param e the mouse wheel event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Create and execute wheel action
        MouseWheelAction wheelAction = new MouseWheelAction(context, uiState, e, vertexRotationService);
        wheelAction.actionPerformed(null);
    }

    /**
     * Returns the state of the right mouse button.
     *
     * @return true if the right mouse button is pressed
     */
    public boolean isRightMouseButton() {
        return isRightMouseButton;
    }
}