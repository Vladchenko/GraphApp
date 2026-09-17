package ru.yanchenko.vlad.graphapp.models.presentation;

import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.vertex.DragPreview;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable data container for the presentation layer's graph state.
 * <p>
 * Holds the current edge being created (as an immutable {@link Edge} that is
 * replaced on each modification), a drag preview for the link line being drawn
 * by the user, and polar coordinates for each vertex used in circular layout.
 */
public class GraphUiData {

    private Edge edgeBeingCreated;
    private DragPreview dragPreview;
    private final List<VertexPolarCoordinate> polarCoordinates;

    /**
     * Creates a GraphUiData with default values.
     */
    public GraphUiData() {
        this.edgeBeingCreated = new Edge(-1, -1);
        this.dragPreview = new DragPreview();
        this.polarCoordinates = new ArrayList<>();
    }

    /**
     * Creates a GraphUiData with the specified values.
     *
     * @param edgeBeingCreated the current edge being created
     * @param dragPreview the drag preview line coordinates
     * @param polarCoordinates the list of polar coordinates for vertices
     */
    public GraphUiData(Edge edgeBeingCreated,
                       DragPreview dragPreview,
                       List<VertexPolarCoordinate> polarCoordinates) {
        this.edgeBeingCreated = edgeBeingCreated;
        this.dragPreview = dragPreview;
        this.polarCoordinates = new ArrayList<>(polarCoordinates);
    }

    /**
     * Returns the edge being created by the user (e.g., via mouse drag).
     * <p>
     * The returned {@link Edge} is immutable — to modify it, call
     * {@link #setEdgeBeingCreated(Edge)} with a new {@code Edge} instance.
     * A value of {@code Edge(-1, -1)} means no edge is being created.
     *
     * @return the edge being created, or {@code Edge(-1, -1)} if none
     */
    public Edge getEdgeBeingCreated() { return edgeBeingCreated; }

    /**
     * Returns the drag preview line being drawn by the user.
     *
     * @return the drag preview coordinates
     */
    public DragPreview getDragPreview() { return dragPreview; }

    /**
     * Returns an unmodifiable copy of the polar coordinates list.
     *
     * @return the polar coordinates
     */
    public List<VertexPolarCoordinate> getPolarCoordinates() {
        return new ArrayList<>(polarCoordinates);
    }

    /**
     * Sets the edge being created by the user (e.g., via mouse drag).
     * <p>
     * Replaces the previous edge with a new {@link Edge} instance.
     *
     * @param edge the edge being created
     */
    public void setEdgeBeingCreated(Edge edge) {
        this.edgeBeingCreated = edge;
    }

    /**
     * Sets the drag preview line being drawn by the user.
     *
     * @param dragPreview the drag preview coordinates
     */
    public void setDragPreview(DragPreview dragPreview) {
        this.dragPreview = dragPreview;
    }

    /**
     * Replaces the polar coordinates with the specified list.
     *
     * @param polarCoordinates the new polar coordinates
     */
    public void setPolarCoordinates(List<VertexPolarCoordinate> polarCoordinates) {
        this.polarCoordinates.clear();
        this.polarCoordinates.addAll(polarCoordinates);
    }

    /**
     * Updates the polar coordinates list to match the number of vertices.
     *
     * @param vertices the list of vertices
     */
    public void updatePolarCoordinates(List<Vertex> vertices) {
        this.polarCoordinates.clear();
        for (int i = 0; i < vertices.size(); i++) {
            this.polarCoordinates.add(new VertexPolarCoordinate());
        }
    }
}