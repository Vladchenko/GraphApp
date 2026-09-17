package ru.yanchenko.vlad.graphapp.models.presentation;

import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.vertex.DragPreview;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Unified UI state data for the graph presentation layer.
 * <p>
 * Combines operation flags, selection state, interaction state,
 * and transient rendering data (edge being created, drag preview, polar coordinates).
 * All fields are mutable and updated by user interactions and actions.
 */
public class GraphUiState {
    // Selection state
    private int selectedVertexIndex = -1;
    private String editBuffer = "";

    // Operation flags
    private boolean isAddingVertex = false;
    private boolean isEditingVertex = false;
    private boolean isDeletingVertex = false;

    // Mouse interaction state
    private Point currentMousePosition;
    private int vertexLinkMargin = 8;

    // Transient rendering data
    private Edge edgeBeingCreated;
    private DragPreview dragPreview;
    private final List<VertexPolarCoordinate> polarCoordinates;

    // Debug mode
    private boolean debugMode = false;

    /**
     * Creates a GraphUiState with default values.
     */
    public GraphUiState() {
        this.edgeBeingCreated = new Edge(-1, -1);
        this.dragPreview = new DragPreview();
        this.polarCoordinates = new ArrayList<>();
    }

    //region Selection state

    /**
     * Returns the index of the selected vertex.
     *
     * @return the selected vertex index, or -1 if none
     */
    public int getSelectedVertexIndex() { return selectedVertexIndex; }

    /**
     * Sets the index of the selected vertex.
     *
     * @param index the selected vertex index
     */
    public void setSelectedVertexIndex(int index) { this.selectedVertexIndex = index; }

    /**
     * Returns the current edit buffer content.
     *
     * @return the edit buffer
     */
    public String getEditBuffer() { return editBuffer; }

    /**
     * Sets the edit buffer content.
     *
     * @param buffer the new edit buffer
     */
    public void setEditBuffer(String buffer) { this.editBuffer = buffer; }

    //endregion

    //region Operation flags

    /**
     * Returns whether vertex addition mode is active.
     *
     * @return true if adding vertex
     */
    public boolean isAddingVertex() { return isAddingVertex; }

    /**
     * Sets the vertex addition mode.
     *
     * @param adding true to enable adding vertex mode
     */
    public void setAddingVertex(boolean adding) { this.isAddingVertex = adding; }

    /**
     * Returns whether vertex editing mode is active.
     *
     * @return true if editing vertex
     */
    public boolean isEditingVertex() { return isEditingVertex; }

    /**
     * Sets the vertex editing mode.
     *
     * @param editing true to enable editing vertex mode
     */
    public void setEditingVertex(boolean editing) { this.isEditingVertex = editing; }

    /**
     * Returns whether vertex deletion mode is active.
     *
     * @return true if deleting vertex
     */
    public boolean isDeletingVertex() { return isDeletingVertex; }

    /**
     * Sets the vertex deletion mode.
     *
     * @param deleting true to enable deleting vertex mode
     */
    public void setDeletingVertex(boolean deleting) { this.isDeletingVertex = deleting; }

    //endregion

    //region Mouse interaction state

    /**
     * Returns the current mouse position.
     *
     * @return the current mouse position
     */
    public Point getCurrentMousePosition() { return currentMousePosition; }

    /**
     * Sets the current mouse position.
     *
     * @param point the current mouse position
     */
    public void setCurrentMousePosition(Point point) { this.currentMousePosition = point; }

    /**
     * Returns the vertex link margin.
     *
     * @return the link margin
     */
    public int getVertexLinkMargin() { return vertexLinkMargin; }

    /**
     * Sets the vertex link margin.
     *
     * @param margin the link margin
     */
    public void setVertexLinkMargin(int margin) { this.vertexLinkMargin = margin; }

    //endregion

    //region Transient rendering data

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
     * Returns the drag preview line being drawn by the user.
     *
     * @return the drag preview coordinates
     */
    public DragPreview getDragPreview() { return dragPreview; }

    /**
     * Sets the drag preview line being drawn by the user.
     *
     * @param dragPreview the drag preview coordinates
     */
    public void setDragPreview(DragPreview dragPreview) { this.dragPreview = dragPreview; }

    /**
     * Returns an unmodifiable copy of the polar coordinates list.
     *
     * @return the polar coordinates
     */
    public List<VertexPolarCoordinate> getPolarCoordinates() {
        return new ArrayList<>(polarCoordinates);
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

    //endregion

    //region Debug mode

    /**
     * Returns whether debug mode is enabled.
     *
     * @return true if debug mode is on
     */
    public boolean isDebugMode() { return debugMode; }

    /**
     * Sets the debug mode.
     *
     * @param debug true to enable debug mode
     */
    public void setDebugMode(boolean debug) { this.debugMode = debug; }

    //endregion

    //region Convenience getters (computed properties)

    /**
     * Returns whether a vertex is currently selected.
     *
     * @return true if a vertex is selected
     */
    public boolean hasSelection() { return selectedVertexIndex >= 0; }

    /**
     * Returns whether any operation (add/edit/delete) is in progress.
     *
     * @return true if any operation is active
     */
    public boolean isInAnyOperation() { return isAddingVertex || isEditingVertex || isDeletingVertex; }

    //endregion
}
