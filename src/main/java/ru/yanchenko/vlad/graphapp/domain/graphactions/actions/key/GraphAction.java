package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Abstract base class for all graph-related actions.
 * <p>
 * Implements the Command pattern to encapsulate graph operations as objects.
 * Subclasses define the specific behavior of {@link #actionPerformed(ActionEvent)},
 * while the base class manages action availability state and keyboard accelerators.
 * <p>
 * Actions are registered in {@link javax.swing.JMenuBar} or other Swing components
 * and triggered by mouse clicks or key presses.
 * <p>
 * Examples of subclasses:
 * <ul>
 *   <li>{@link AddVertexAction} — add a vertex</li>
 *   <li>{@link DeleteVertexAction} — delete a vertex</li>
 *   <li>{@link SaveAction} — save the graph</li>
 *   <li>{@link LoadAction} — load the graph</li>
 * </ul>
 *
 * @see javax.swing.AbstractAction
 * @see java.awt.event.ActionEvent
 */
public abstract class GraphAction extends AbstractAction {

    /**
     * Creates an action with the specified name (displayed in menus).
     *
     * @param name the action name, used as text in menus and buttons
     */
    public GraphAction(String name) {
        super(name);
    }

    /**
     * Creates an action with a name and a keyboard accelerator.
     *
     * @param name        the action name, used as text in menus
     * @param accelerator the keyboard accelerator (key combination) to trigger the action
     */
    public GraphAction(String name, KeyStroke accelerator) {
        super(name);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    /**
     * Abstract method defining the action's behavior when invoked.
     * <p>
     * Concrete subclasses implement the graph operation logic here.
     * The {@code e} parameter contains information about the event that triggered
     * the action (source, timestamp, modifiers).
     *
     * @param e the action event that initiated the call
     */
    @Override
    public abstract void actionPerformed(ActionEvent e);

    /**
     * Returns whether this action should be enabled (available for execution).
     * <p>
     * Returns {@code true} by default, meaning the action is always enabled.
     * Subclasses may override this method to implement conditional availability.
     * For example, a "Save" action may be disabled if the graph has not been modified.
     *
     * @return {@code true} if the action is enabled, {@code false} if disabled
     */
    public boolean isActionEnabled() {
        return true;
    }

    /**
     * Updates the action's enabled state based on the result of {@link #isActionEnabled()}.
     * <p>
     * Calls {@code setEnabled(isActionEnabled())}. Should be called when the application
     * state changes in a way that may affect the action's availability
     * (e.g., when loading a new graph or changing selection).
     */
    public void updateEnabledState() {
        setEnabled(isActionEnabled());
    }
}