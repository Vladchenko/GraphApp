package ru.yanchenko.vlad.graphapp.shared.di;

import dagger.Component;
import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.services.VertexPopulationService;
import ru.yanchenko.vlad.graphapp.presentation.DrawingPanel;
import ru.yanchenko.vlad.graphapp.presentation.DrawingTimer;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.presentation.keybindings.ActionManager;
import ru.yanchenko.vlad.graphapp.presentation.keybindings.MouseActionManager;
import ru.yanchenko.vlad.graphapp.shared.ScreenData;

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
    ActionManager getActionManager();
    KeyListener getTextInputListener();
    MouseActionManager getMouseActionManager();
    VertexPopulationService getVertexPopulationService();
}
