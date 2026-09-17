package ru.yanchenko.vlad.graphapp.presentation;

import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexPopulationService;
import ru.yanchenko.vlad.graphapp.presentation.keybindings.ActionManager;
import ru.yanchenko.vlad.graphapp.presentation.keybindings.MouseActionManager;
import ru.yanchenko.vlad.graphapp.shared.ScreenData;
import ru.yanchenko.vlad.graphapp.shared.di.DaggerGraphAppComponent;
import ru.yanchenko.vlad.graphapp.shared.di.GraphAppComponent;

import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * Entry point for the Graph Application.
 * <p>
 * Initializes the Dagger dependency injection component, populates
 * the graph based on {@link AppConfig#getPopulationKind()}, and sets up
 * the Swing UI with key and mouse bindings.
 *
 * @see AppConfig
 * @see GraphAppComponent
 * @see DrawingPanel
 */
public class GraphApp {
    public static void main(String[] args) {
        GraphAppComponent daggerComponent = DaggerGraphAppComponent.create();
        ScreenData screenData = daggerComponent.getScreenData();
        JFrame drawingFrame = daggerComponent.getDrawingFrame();
        GraphUiState uiState = daggerComponent.getGraphUiState();
        GraphDomainService graphService = daggerComponent.getGraphDomainService();
        DrawingPanel drawingPanel = daggerComponent.getDrawingPanel();
        ActionManager actionManager = daggerComponent.getActionManager();
        KeyListener textInputListener = daggerComponent.getTextInputListener();
        MouseActionManager mouseActionManager = daggerComponent.getMouseActionManager();
        VertexPopulationService vertexPopulationService = daggerComponent.getVertexPopulationService();

        // Use new service architecture for vertex population
        vertexPopulationService.populateVertices(graphService, uiState);
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
