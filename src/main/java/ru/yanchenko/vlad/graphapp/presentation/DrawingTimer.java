package ru.yanchenko.vlad.graphapp.presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Manages the refresh timer for the drawing panel.
 * <p>
 * Creates a non-repeating Swing {@link Timer} that triggers a repaint
 * of the {@link DrawingPanel} at a fixed interval (3ms).
 *
 * @see DrawingPanel
 * @see javax.swing.Timer
 */
public class DrawingTimer {

    private final DrawingPanel drawingPanel;
    private final ActionListener actionListener = new ActionListener();
    private final Timer canvasRefreshTimer = new Timer(3, actionListener);

    /**
     * Creates a DrawingTimer with the specified drawing panel.
     *
     * @param drawingPanel the panel to refresh
     */
    public DrawingTimer(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        canvasRefreshTimer.setRepeats(false);
    }

    private class ActionListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            drawingPanel.repaint();
        }
    }

    /**
     * Returns the canvas refresh timer.
     *
     * @return the timer instance
     */
    public Timer getCanvasRefreshTimer() {
        return canvasRefreshTimer;
    }
}
