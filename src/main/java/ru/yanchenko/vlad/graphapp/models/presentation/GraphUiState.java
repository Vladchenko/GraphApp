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
    public GraphUiState() {
        // Default initialization
    }

    // Selection state
    public int getSelectedVertexIndex() { return selectedVertexIndex; }
    public void setSelectedVertexIndex(int index) { this.selectedVertexIndex = index; }

    // Edit buffer state
    public String getEditBuffer() { return editBuffer; }
    public void setEditBuffer(String buffer) { this.editBuffer = buffer; }

    // Operation flags
    public boolean isAddingVertex() { return isAddingVertex; }
    public void setAddingVertex(boolean adding) { this.isAddingVertex = adding; }

    public boolean isEditingVertex() { return isEditingVertex; }
    public void setEditingVertex(boolean editing) { this.isEditingVertex = editing; }

    public boolean isDeletingVertex() { return isDeletingVertex; }
    public void setDeletingVertex(boolean deleting) { this.isDeletingVertex = deleting; }

    // Mouse interaction state
    public Point getDragStartPoint() { return dragStartPoint; }
    public void setDragStartPoint(Point point) { this.dragStartPoint = point; }

    public Point getCurrentMousePosition() { return currentMousePosition; }
    public void setCurrentMousePosition(Point point) { this.currentMousePosition = point; }

    // Display preferences
    public int getVertexLinkMargin() { return vertexLinkMargin; }
    public void setVertexLinkMargin(int margin) { this.vertexLinkMargin = margin; }

    // Debug mode
    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debug) { this.debugMode = debug; }

    // Debug display options
    public boolean isShowFrameTime() { return showFrameTime; }
    public void setShowFrameTime(boolean show) { this.showFrameTime = show; }

    public boolean isShowVertexInfo() { return showVertexInfo; }
    public void setShowVertexInfo(boolean show) { this.showVertexInfo = show; }

    public boolean isShowEdgeInfo() { return showEdgeInfo; }
    public void setShowEdgeInfo(boolean show) { this.showEdgeInfo = show; }

    public boolean isShowMousePosition() { return showMousePosition; }
    public void setShowMousePosition(boolean show) { this.showMousePosition = show; }

    // Convenience getters (computed properties, still no business logic)
    public boolean hasSelection() { return selectedVertexIndex >= 0; }
    public boolean isInAnyOperation() { return isAddingVertex || isEditingVertex || isDeletingVertex; }
}