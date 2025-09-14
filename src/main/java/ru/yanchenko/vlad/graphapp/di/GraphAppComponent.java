package ru.yanchenko.vlad.graphapp.di;

import dagger.Component;
import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.ActionManager;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse.MouseActionManager;
import ru.yanchenko.vlad.graphapp.domain.verticesops.VertexPopulationService;
import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.presentation.DrawingPanel;
import ru.yanchenko.vlad.graphapp.presentation.DrawingTimer;

import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * Dagger component
 */
@Singleton
@Component(modules = {GraphAppModule.class})
public interface GraphAppComponent {
    JFrame getDrawingFrame();
    ScreenData getScreenData();
    DrawingPanel getDrawingPanel();
    DrawingTimer getDrawingTimer();
    GraphUiState getGraphUiState();
    GraphDomainService getGraphDomainService();
    GraphUiStateService getGraphUiStateService();
    VerticesData getVerticesData();
    ActionManager getActionManager();
    KeyListener getTextInputListener();
    MouseActionManager getMouseActionManager();
    VertexPopulationService getVertexPopulationService();
}
