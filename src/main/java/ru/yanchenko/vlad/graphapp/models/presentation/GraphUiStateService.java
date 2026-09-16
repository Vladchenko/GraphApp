package ru.yanchenko.vlad.graphapp.models.presentation;

import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import java.util.List;

/**
 * Service that manages UI state and data for the graph presentation layer.
 * <p>
 * Provides a unified interface for accessing and updating
 * {@link GraphUiData} (links, polar coordinates) and
 * {@link GraphUiState} (selection, operation flags, debug options).
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

    /**
     * Updates the polar coordinates to match the number of vertices.
     *
     * @param vertices the list of vertices
     */
    public void updatePolarCoordinates(List<Vertex> vertices) {
        uiData.updatePolarCoordinates(vertices);
    }

    /**
     * Sets the polar coordinates for vertices.
     *
     * @param coords the new polar coordinates
     */
    public void setPolarCoordinates(List<VertexPolarCoordinate> coords) {
        uiData.setPolarCoordinates(coords);
    }

    /**
     * Sets the edge being created by the user (e.g., via mouse drag).
     * <p>
     * Replaces the previous edge with a new {@link Edge} instance.
     *
     * @param edge the edge being created
     */
    public void setEdgeBeingCreated(Edge edge) {
        uiData.setEdgeBeingCreated(edge);
    }

    /**
     * Sets the possible link preview.
     *
     * @param link the possible link
     */
    public void setPossibleLink(VertexPossibleLink link) {
        uiData.setPossibleLink(link);
    }
}