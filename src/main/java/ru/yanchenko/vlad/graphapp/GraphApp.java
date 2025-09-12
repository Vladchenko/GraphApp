package ru.yanchenko.vlad.graphapp;

import ru.yanchenko.vlad.graphapp.di.DaggerGraphAppComponent;
import ru.yanchenko.vlad.graphapp.di.GraphAppComponent;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.ActionManager;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse.MouseActionManager;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexPopulationService;
import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.presentation.DrawingPanel;

import javax.swing.*;
import java.awt.event.KeyListener;

public class GraphApp {
    public static void main(String[] args) {
        GraphAppComponent daggerComponent = DaggerGraphAppComponent.create();
        ScreenData screenData = daggerComponent.getScreenData();
        JFrame drawingFrame = daggerComponent.getDrawingFrame();
        GraphUiState uiState = daggerComponent.getGraphUiState();
        DrawingPanel drawingPanel = daggerComponent.getDrawingPanel();
        VerticesData verticesData = daggerComponent.getVerticesData();
        ActionManager actionManager = daggerComponent.getActionManager();
        KeyListener textInputListener = daggerComponent.getTextInputListener();
        MouseActionManager mouseActionManager = daggerComponent.getMouseActionManager();
        VertexPopulationService vertexPopulationService = daggerComponent.getVertexPopulationService();

        vertexPopulationService.populateVertices(verticesData, uiState);
        drawingPanel.setBackground(screenData.getWindowBackgroundColor());
        drawingPanel.setFocusable(true);
        drawingPanel.addKeyListener(textInputListener);

        // Set up key bindings using the action manager
        actionManager.setupKeyBindings(drawingFrame.getRootPane());

        // Set up mouse bindings
        drawingFrame.addMouseListener(mouseActionManager);
        drawingFrame.addMouseMotionListener(mouseActionManager);
        drawingFrame.addMouseWheelListener(mouseActionManager);

        drawingFrame.setBackground(screenData.getWindowBackgroundColor());
        drawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Removes status bar of JFrame
        drawingFrame.setUndecorated(true);

        drawingFrame.setSize(screenData.getScreenWidth(), screenData.getScreenHeight());
        drawingFrame.setLocationRelativeTo(null);
        drawingFrame.setContentPane(drawingPanel);
        daggerComponent.getDrawingFrame().setVisible(true);
    }
}
