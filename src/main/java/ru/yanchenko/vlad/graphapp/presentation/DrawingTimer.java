package ru.yanchenko.vlad.graphapp.presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DrawingTimer {

    private final DrawingPanel drawingPanel;
    private final ActionListener actionListener = new ActionListener();
    private final Timer canvasRefreshTimer = new Timer(3, actionListener);

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

    public Timer getCanvasRefreshTimer() {
        return canvasRefreshTimer;
    }
}
