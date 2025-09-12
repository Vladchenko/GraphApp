package ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Base class for all graph-related actions.
 */
public abstract class GraphAction extends AbstractAction {

    public GraphAction(String name) {
        super(name);
    }

    public GraphAction(String name, KeyStroke accelerator) {
        super(name);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    public boolean isActionEnabled() {
        return true;
    }

    public void updateEnabledState() {
        setEnabled(isActionEnabled());
    }
}