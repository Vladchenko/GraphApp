package ru.yanchenko.vlad.graphapp.di;

import dagger.Module;
import dagger.Provides;
import ru.yanchenko.vlad.graphapp.AppConfig;
import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.key.*;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse.MouseActionManager;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.EditActionContext;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.FileActionContext;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.GraphActionContext;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.RefreshService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.*;
import ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.*;
import ru.yanchenko.vlad.graphapp.listeners.KeyboardState;
import ru.yanchenko.vlad.graphapp.listeners.MouseMotionListenerImpl;
import ru.yanchenko.vlad.graphapp.listeners.TextInputListener;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.persistence.JsonPersistence;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;
import ru.yanchenko.vlad.graphapp.presentation.DrawingPanel;
import ru.yanchenko.vlad.graphapp.presentation.DrawingTimer;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.function.Consumer;

@Module
public class GraphAppModule {

    private static final double GRAPH_INITIAL_RADIUS = 200;
    public static final String ROTATE_CLOCKWISE = "rotateClockwise";
    public static final String COUNTER_ROTATE_CLOCKWISE = "rotateCounterClockwise";

    @Provides
    @Singleton
    public GraphUiState provideGraphUiState() {
        return new GraphUiState();
    }

    @Provides
    @Singleton
    public PopulationKind providePopulationKind() {
        return AppConfig.getPopulationKind();
    }

    @Provides
    @Singleton
    public ScreenData provideScreenData() {
        return new ScreenData();
    }

    @Provides
    public @Singleton
    KeyboardState provideKeyboardManager() {
        return new KeyboardState();
    }

    @Provides
    @Singleton
    public Persistable providePersistable() {
        return new JsonPersistence();   //XMLPersistence();
    }

    @Provides
    @Singleton
    public Consumer<List<Vertex>> provideStrategiesList(ScreenData screenData) {
        return new CircularLayout(screenData, GRAPH_INITIAL_RADIUS);
    }

    @Provides
    @Singleton
    public VertexPopulationStrategy provideVertexPopulationStrategy(Persistable persistable,
                                                                    PopulationKind populationKind,
                                                                    Consumer<List<Vertex>> layoutStrategiesList,
                                                                    VertexCreationService vertexCreationService) {
        VertexPopulationStrategy strategy = null;
        switch (populationKind) {
            case FIXED_FILE: {
                strategy = new FixedFileStrategy(persistable);
                break;
            }
            case CIRCULAR_FILE: {
                strategy = new CircularFileStrategy(persistable, layoutStrategiesList);
                break;
            }
            case HARDCODED_SAMPLE_A: {
                strategy = new HardcodedSampleAStrategy(vertexCreationService);
                break;
            }
            case HARDCODED_SAMPLE_B: {
                strategy = new HardcodedSampleBStrategy(vertexCreationService);
                break;
            }
        }
        return strategy;
    }

    @Provides
    @Singleton
    public VertexValidationService provideVertexValidationService() {
        return new VertexValidationService();
    }

    @Provides
    @Singleton
    public VertexPopulationService provideVertexPopulationService(VertexPopulationStrategy populationStrategy) {
        return new VertexPopulationService(populationStrategy);
    }

    @Provides
    @Singleton
    public VertexRotationService provideVertexRotationService(ScreenData screenData) {
        return new VertexRotationService(screenData);
    }

    @Provides
    @Singleton
    public VertexDeletionService provideVertexDeletionService() {
        return new VertexDeletionService();
    }

    @Provides
    @Singleton
    public VertexCreationService provideVertexCreationService(ScreenData screenData,
                                                              VertexValidationService vertexValidationService) {
        return new VertexCreationService(screenData, vertexValidationService);
    }

    @Provides
    @Singleton
    public VertexLayoutService provideVertexLayoutService(Consumer<List<Vertex>> layoutStrategy) {
        return new VertexLayoutService(layoutStrategy);
    }

    @Provides
    @Singleton
    public DrawingPanel provideDrawingPanel(GraphUiState uiState,
                                            ScreenData screenData,
                                            GraphDomainService graphService,
                                            GraphUiStateService uiStateService,
                                            MouseActionManager mouseActionManager,
                                            RefreshService refreshService) {
        return new DrawingPanel(screenData, mouseActionManager, graphService, uiStateService, uiState, refreshService);
    }

    @Provides
    @Singleton
    public DrawingTimer provideDrawingTimer(DrawingPanel drawingPanel) {
        return new DrawingTimer(drawingPanel);
    }

    @Provides
    @Singleton
    public KeyListener provideTextInputListener(DrawingTimer drawingTimer,
                                                GraphUiState uiState) {
        return new TextInputListener(drawingTimer, uiState);
    }

    @Provides
    @Singleton
    public MouseMotionListener provideMouseMotionListener(DrawingTimer drawingTimer,
                                                          GraphDomainService graphService,
                                                          GraphUiStateService uiStateService,
                                                          KeyboardState keyboardState) {
        return new MouseMotionListenerImpl(drawingTimer, graphService, uiStateService, keyboardState);
    }

    @Provides
    @Singleton
    public JFrame provideDrawingFrame() {
        return new JFrame();
    }

    @Provides
    @Singleton
    public FileActionContext provideFileActionContext(GraphUiState uiState,
                                                      Persistable persistable,
                                                      RefreshService refreshService,
                                                      GraphDomainService graphService,
                                                      GraphUiStateService uiStateService) {
        return new FileActionContext(persistable, refreshService, uiState, graphService, uiStateService);
    }

    @Provides
    @Singleton
    public GraphActionContext provideGraphActionContext(GraphDomainService graphService,
                                                        GraphUiStateService uiStateService,
                                                        RefreshService refreshService,
                                                        PopulationKind populationKind) {
        return new GraphActionContext(graphService, uiStateService, refreshService, populationKind);
    }

    @Provides
    @Singleton
    public EditActionContext provideEditActionContext(GraphUiStateService uiStateService,
                                                      RefreshService refreshService) {
        return new EditActionContext(uiStateService.getUiData(), refreshService);
    }

    // Action providers
    @Provides
    @Singleton
    public AddVertexAction provideAddVertexAction(GraphActionContext graphContext, GraphUiState uiState) {
        return new AddVertexAction(graphContext, uiState);
    }

    @Provides
    @Singleton
    public EditVertexAction provideEditVertexAction(GraphActionContext graphContext, GraphUiState uiState) {
        return new EditVertexAction(graphContext, uiState);
    }

    @Provides
    @Singleton
    public CancelAction provideCancelAction(EditActionContext editContext, GraphUiState uiState) {
        return new CancelAction(editContext, uiState);
    }

    @Provides
    @Singleton
    public ConfirmAction provideConfirmAction(GraphActionContext graphContext,
                                              GraphUiState uiState,
                                              VertexLayoutService vertexLayoutService,
                                              VertexCreationService vertexCreationService) {
        return new ConfirmAction(graphContext, uiState, vertexLayoutService, vertexCreationService);
    }

    @Provides
    @Singleton
    public DeleteVertexAction provideDeleteVertexAction(GraphActionContext graphContext,
                                                        GraphUiState uiState,
                                                        VertexLayoutService vertexLayoutService,
                                                        VertexDeletionService vertexDeletionService) {
        return new DeleteVertexAction(graphContext, uiState, vertexLayoutService, vertexDeletionService);
    }

    @Provides
    @Singleton
    public LoadAction provideLoadAction(FileActionContext fileContext, GraphUiState uiState) {
        return new LoadAction(fileContext, uiState);
    }

    @Provides
    @Singleton
    public ResetAction provideResetAction(GraphActionContext graphContext,
                                          GraphUiState uiState,
                                          VertexPopulationService vertexPopulationService) {
        return new ResetAction(graphContext, uiState, vertexPopulationService);
    }

    @Provides
    @Singleton
    @Named(ROTATE_CLOCKWISE)
    public RotateAction provideRotateClockwiseAction(GraphUiState uiState,
                                                     GraphDomainService graphService,
                                                     GraphUiStateService uiStateService,
                                                     VertexRotationService vertexRotationService) {
        return new RotateAction(RotateAction.Direction.CLOCKWISE, uiStateService.getUiData(), uiState, graphService,
                vertexRotationService);
    }

    @Provides
    @Singleton
    @Named("rotateCounterClockwise")
    public RotateAction provideRotateCounterClockwiseAction(GraphUiState uiState,
                                                            GraphDomainService graphService,
                                                            GraphUiStateService uiStateService,
                                                            VertexRotationService vertexRotationService) {
        return new RotateAction(RotateAction.Direction.COUNTER_CLOCKWISE, uiStateService.getUiData(), uiState,
                graphService, vertexRotationService);
    }

    @Provides
    @Singleton
    public SaveAction provideSaveAction(FileActionContext fileContext, GraphUiState uiState) {
        return new SaveAction(fileContext, uiState);
    }

    @Provides
    @Singleton
    public ActionManager provideActionManager(LoadAction loadAction,
                                              SaveAction saveAction,
                                              ResetAction resetAction,
                                              CancelAction cancelAction,
                                              ConfirmAction confirmAction,
                                              @Named(ROTATE_CLOCKWISE)
                                              RotateAction rotateClockwiseAction,
                                              AddVertexAction addVertexAction,
                                              EditVertexAction editVertexAction,
                                              DeleteVertexAction deleteVertexAction,
                                              @Named(COUNTER_ROTATE_CLOCKWISE)
                                              RotateAction rotateCounterClockwiseAction) {
        return new ActionManager(loadAction, saveAction, resetAction, cancelAction, confirmAction,
                rotateClockwiseAction, addVertexAction, editVertexAction,
                deleteVertexAction, rotateCounterClockwiseAction);
    }

    @Provides
    @Singleton
    public MouseActionManager provideMouseActionManager(GraphActionContext graphContext,
                                                        GraphUiState uiState,
                                                        VertexRotationService vertexRotationService) {
        return new MouseActionManager(graphContext, uiState, vertexRotationService);
    }

    @Provides
    @Singleton
    public RefreshService provideRefreshService() {
        return new RefreshService();
    }

    // Add new domain service providers
    @Provides
    @Singleton
    public GraphDomainService provideGraphDomainService() {
        return new GraphDomainService();
    }

    @Provides
    @Singleton
    public GraphUiStateService provideGraphUiStateService(GraphUiState uiState) {
        return new GraphUiStateService(uiState);
    }
}
