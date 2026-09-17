package ru.yanchenko.vlad.graphapp.models.presentation;

/**
 * Service that manages UI state and data for the graph presentation layer.
 * <p>
 * Provides access to {@link GraphUiData} (edge state, drag preview, polar coordinates)
 * and {@link GraphUiState} (selection, operation flags, debug options).
 * <p>
 * Use {@link #getUiData()} for direct access to edge state, drag preview, and polar coordinates.
 *
 * @see GraphUiData
 * @see GraphUiState
 */
public class GraphUiStateService {
    private final GraphUiData uiData;
    private final GraphUiState uiState;

    /**
     * Creates a GraphUiStateService with the specified UI state.
     *
     * @param uiState the UI state to manage
     */
    public GraphUiStateService(GraphUiState uiState) {
        this.uiState = uiState;
        this.uiData = new GraphUiData();
    }

    /**
     * Returns the UI data managed by this service.
     * <p>
     * Use this for direct access to edge state, drag preview, and polar coordinates.
     *
     * @return the UI data
     */
    public GraphUiData getUiData() { return uiData; }

    /**
     * Returns the UI state managed by this service.
     *
     * @return the UI state
     */
    public GraphUiState getUiState() { return uiState; }
}