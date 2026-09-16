package ru.yanchenko.vlad.graphapp.models.presentation;

import java.awt.*;

/**
 * Pure UI state data - no operations, only getters and setters.
 * Represents the current state of user interactions and display preferences.
 */
public class GraphUiState {
    private int selectedVertexIndex = -1;
    private String editBuffer = "";
    private boolean isAddingVertex = false;
    private boolean isEditingVertex = false;
    private boolean isDeletingVertex = false;
    private Point dragStartPoint;
    private Point currentMousePosition;
    private int vertexLinkMargin = 8;

    // Debug mode options
    private boolean debugMode = false;
    private boolean showFrameTime = true;
    private boolean showVertexInfo = false;
    private boolean showEdgeInfo = false;
    private boolean showMousePosition = false;

    // Constructor
    /**
     * Creates a GraphUiState with default values.
     */
    public GraphUiState() {
        // Default initialization
    }

    // Selection state
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

    // Edit buffer state
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

    // Operation flags
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

    // Mouse interaction state
    /**
     * Returns the starting point of a drag operation.
     *
     * @return the drag start point
     */
    public Point getDragStartPoint() { return dragStartPoint; }

    /**
     * Sets the starting point of a drag operation.
     *
     * @param point the drag start point
     */
    public void setDragStartPoint(Point point) { this.dragStartPoint = point; }

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

    // Display preferences
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

    // Debug mode
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

    // Debug display options
    /**
     * Returns whether frame time display is enabled.
     *
     * @return true if frame time is shown
     */
    public boolean isShowFrameTime() { return showFrameTime; }

    /**
     * Sets the frame time display option.
     *
     * @param show true to show frame time
     */
    public void setShowFrameTime(boolean show) { this.showFrameTime = show; }

    /**
     * Returns whether vertex info display is enabled.
     *
     * @return true if vertex info is shown
     */
    public boolean isShowVertexInfo() { return showVertexInfo; }

    /**
     * Sets the vertex info display option.
     *
     * @param show true to show vertex info
     */
    public void setShowVertexInfo(boolean show) { this.showVertexInfo = show; }

    /**
     * Returns whether edge info display is enabled.
     *
     * @return true if edge info is shown
     */
    public boolean isShowEdgeInfo() { return showEdgeInfo; }

    /**
     * Sets the edge info display option.
     *
     * @param show true to show edge info
     */
    public void setShowEdgeInfo(boolean show) { this.showEdgeInfo = show; }

    /**
     * Returns whether mouse position display is enabled.
     *
     * @return true if mouse position is shown
     */
    public boolean isShowMousePosition() { return showMousePosition; }

    /**
     * Sets the mouse position display option.
     *
     * @param show true to show mouse position
     */
    public void setShowMousePosition(boolean show) { this.showMousePosition = show; }

    // Convenience getters (computed properties, still no business logic)
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
}