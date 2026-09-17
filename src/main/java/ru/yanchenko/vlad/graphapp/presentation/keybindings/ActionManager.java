package ru.yanchenko.vlad.graphapp.presentation.keybindings;

import ru.yanchenko.vlad.graphapp.domain.usecases.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all graph actions and their key bindings.
 * Uses dependency injection instead of ServiceLocator.
 */
public class ActionManager {

    // Action key names for InputMap/ActionMap bindings
    private static final String ADD_VERTEX_KEY = "AddVertexAction";
    private static final String DELETE_VERTEX_KEY = "DeleteVertexAction";
    private static final String EDIT_VERTEX_KEY = "EditVertexAction";
    private static final String ROTATE_CLOCKWISE_KEY = "RotateClockwiseAction";
    private static final String ROTATE_COUNTER_CLOCKWISE_KEY = "RotateCounterClockwiseAction";

    private final List<GraphAction> actions = new ArrayList<>();

    /**
     * Creates an ActionManager with all graph actions.
     *
     * @param loadAction the load action
     * @param saveAction the save action
     * @param resetAction the reset action
     * @param cancelAction the cancel action
     * @param confirmAction the confirmation action
     * @param addVertexAction the add vertex action
     * @param editVertexAction the edit vertex action
     * @param rotateClockwiseAction the clockwise rotate action
     * @param deleteVertexAction the delete vertex action
     * @param rotateCounterClockwiseAction the counter-clockwise rotate action
     */
    public ActionManager(LoadAction loadAction,
                         SaveAction saveAction,
                         ResetAction resetAction,
                         CancelAction cancelAction,
                         ConfirmAction confirmAction,
                         AddVertexAction addVertexAction,
                         EditVertexAction editVertexAction,
                         RotateAction rotateClockwiseAction,
                         DeleteVertexAction deleteVertexAction,
                         RotateAction rotateCounterClockwiseAction) {

        // Add all actions
        actions.add(loadAction);
        actions.add(saveAction);
        actions.add(addVertexAction);
        actions.add(editVertexAction);
        actions.add(deleteVertexAction);
        actions.add(resetAction);
        actions.add(cancelAction);
        actions.add(confirmAction);
        actions.add(rotateClockwiseAction);
        actions.add(rotateCounterClockwiseAction);
    }

    /**
     * Sets up key bindings for a component using InputMap/ActionMap.
     *
     * @param component the component to bind keys to
     */
    public void setupKeyBindings(JComponent component) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        for (GraphAction action : actions) {
            KeyStroke accelerator = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
            if (accelerator != null) {
                String actionKey = action.getClass().getSimpleName();
                inputMap.put(accelerator, actionKey);
                actionMap.put(actionKey, action);
            }
        }

        // Add additional key bindings for multiple keys per action
        setupAdditionalKeyBindings(inputMap, actionMap);
    }

    private void setupAdditionalKeyBindings(InputMap inputMap, ActionMap actionMap) {
        // Add vertex - multiple keys
        AddVertexAction addAction = getAction(AddVertexAction.class);
        if (addAction != null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), ADD_VERTEX_KEY);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), ADD_VERTEX_KEY);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), ADD_VERTEX_KEY);
            actionMap.put(ADD_VERTEX_KEY, addAction);
        }

        // Delete vertex - multiple keys
        DeleteVertexAction deleteAction = getAction(DeleteVertexAction.class);
        if (deleteAction != null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), DELETE_VERTEX_KEY);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), DELETE_VERTEX_KEY);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UNDERSCORE, 0), DELETE_VERTEX_KEY);
            actionMap.put(DELETE_VERTEX_KEY, deleteAction);
        }

        // Edit vertex - multiple keys
        EditVertexAction editAction = getAction(EditVertexAction.class);
        if (editAction != null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), EDIT_VERTEX_KEY);
            actionMap.put(EDIT_VERTEX_KEY, editAction);
        }

        // Rotation - arrow keys
        RotateAction clockwiseAction = getAction(RotateAction.class,
                action -> action.getDirection() == RotateAction.Direction.CLOCKWISE);
        if (clockwiseAction != null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ROTATE_CLOCKWISE_KEY);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ROTATE_CLOCKWISE_KEY);
            actionMap.put(ROTATE_CLOCKWISE_KEY, clockwiseAction);
        }

        RotateAction counterClockwiseAction = getAction(RotateAction.class,
                action -> action.getDirection() == RotateAction.Direction.COUNTER_CLOCKWISE);
        if (counterClockwiseAction != null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), ROTATE_COUNTER_CLOCKWISE_KEY);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), ROTATE_COUNTER_CLOCKWISE_KEY);
            actionMap.put(ROTATE_COUNTER_CLOCKWISE_KEY, counterClockwiseAction);
        }
    }

    /**
     * Updates the enabled state of all actions based on current context.
     */
    public void updateActionStates() {
        for (GraphAction action : actions) {
            action.updateEnabledState();
        }
    }

    /**
     * Gets an action of the specified type.
     *
     * @param actionClass the action class to search for
     * @param <T> the action type
     * @return the action if found, null otherwise
     */
    @SuppressWarnings("unchecked")
    public <T extends GraphAction> T getAction(Class<T> actionClass) {
        for (GraphAction action : actions) {
            if (actionClass.isInstance(action)) {
                return (T) action;
            }
        }
        return null;
    }

    /**
     * Gets an action of the specified type that matches a predicate.
     *
     * @param actionClass the action class to search for
     * @param predicate the predicate to match
     * @param <T> the action type
     * @return the action if found, null otherwise
     */
    @SuppressWarnings("unchecked")
    public <T extends GraphAction> T getAction(Class<T> actionClass, java.util.function.Predicate<T> predicate) {
        for (GraphAction action : actions) {
            if (actionClass.isInstance(action) && predicate.test((T) action)) {
                return (T) action;
            }
        }
        return null;
    }

    /**
     * Gets all actions managed by this manager.
     *
     * @return a copy of the list of all actions
     */
    public List<GraphAction> getAllActions() {
        return new ArrayList<>(actions);
    }
}