package ru.yanchenko.vlad.graphapp.listeners;

import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.presentation.DrawingTimer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of MouseMotionListener that handles mouse drag and move events.
 * Properly separates UI state from domain data.
 */
public class MouseMotionListenerImpl implements MouseMotionListener {
    private static final Logger LOGGER = Logger.getLogger(MouseMotionListenerImpl.class.getName());

    private final DrawingTimer drawingTimer;
    private final VerticesData verticesData; // Domain data
    private final GraphUiState uiState; // UI state
    private final KeyboardState keyboardState;

    public MouseMotionListenerImpl(DrawingTimer drawingTimer,
                                   VerticesData verticesData,
                                   GraphUiState uiState,
                                   KeyboardState keyboardState) {
        this.drawingTimer = drawingTimer;
        this.verticesData = verticesData;
        this.uiState = uiState;
        this.keyboardState = keyboardState;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            // Update mouse position in UI state
            uiState.setCurrentMousePosition(e.getPoint());

            // Handle vertex dragging or link creation
            if (keyboardState.isKeyCtrl()) {
                handleVertexDragging(e);
            } else {
                handleLinkCreation(e);
            }

            drawingTimer.getCanvasRefreshTimer().start();

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error in mouse drag", ex);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        try {
            // Update mouse position in UI state
            uiState.setCurrentMousePosition(e.getPoint());

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error in mouse move", ex);
        }
    }

    /**
     * Handle vertex dragging when Ctrl key is pressed.
     */
    private void handleVertexDragging(MouseEvent e) {
        int chosenVertex = verticesData.getVertexLink().getLink1();
        List<Vertex> verticesList = verticesData.getVertices();

        if (chosenVertex != -1 && chosenVertex < verticesList.size()) {
            Vertex vertex = verticesList.get(chosenVertex);
            vertex.setX(e.getX());
            vertex.setY(e.getY());

            LOGGER.fine("Dragged vertex " + chosenVertex + " to (" + e.getX() + ", " + e.getY() + ")");
        }
    }

    /**
     * Handle link creation preview when dragging from a vertex.
     */
    private void handleLinkCreation(MouseEvent e) {
        int chosenVertex = verticesData.getVertexLink().getLink1();
        VertexLink vertexLink = verticesData.getVertexLink();
        List<Vertex> verticesList = verticesData.getVertices();

        if (chosenVertex == -1 || chosenVertex >= verticesList.size()) {
            return;
        }

        vertexLink.setLink2(-1);

        Vertex sourceVertex = verticesList.get(vertexLink.getLink1());
        double subjectX1 = sourceVertex.getX();
        double subjectY1 = sourceVertex.getY();

        // Calculate angle between vertex center and mouse cursor
        double angle = Geometry.computeAngle(subjectX1, subjectY1, e.getPoint().x, e.getPoint().y);

        // Set first terminator coordinates
        setFirstTerminator(sourceVertex, angle);

        // Check if mouse is still within vertex area
        if (isMouseWithinVertexArea(subjectX1, subjectY1, e.getX(), e.getY(), sourceVertex)) {
            setSecondTerminatorToFirst();
        } else {
            setSecondTerminatorToMouse(e);
        }

        // Check for intersection with other vertices
        checkVertexIntersections(e, subjectX1, subjectY1, sourceVertex);
    }

    /**
     * Set the first terminator coordinates.
     */
    private void setFirstTerminator(Vertex sourceVertex, double angle) {
        int x1 = (int) (sourceVertex.getX() +
                Math.cos(angle) * (sourceVertex.getRadius() + uiState.getVertexLinkMargin()));
        int y1 = (int) (sourceVertex.getY() -
                Math.sin(angle) * (sourceVertex.getRadius() + uiState.getVertexLinkMargin()));

        verticesData.getVertexPossibleLink().setX1(x1);
        verticesData.getVertexPossibleLink().setY1(y1);
    }

    /**
     * Check if mouse is within vertex area.
     */
    private boolean isMouseWithinVertexArea(double vertexX, double vertexY, int mouseX, int mouseY, Vertex vertex) {
        return Geometry.computeDistance(vertexX, vertexY, mouseX, mouseY) <=
                vertex.getRadius() + uiState.getVertexLinkMargin();
    }

    /**
     * Set second terminator to same as first.
     */
    private void setSecondTerminatorToFirst() {
        verticesData.getVertexPossibleLink().setX2(verticesData.getVertexPossibleLink().getX1());
        verticesData.getVertexPossibleLink().setY2(verticesData.getVertexPossibleLink().getY1());
    }

    /**
     * Set second terminator to mouse position.
     */
    private void setSecondTerminatorToMouse(MouseEvent e) {
        verticesData.getVertexPossibleLink().setX2(e.getX());
        verticesData.getVertexPossibleLink().setY2(e.getY());
    }

    /**
     * Check for intersections with other vertices and adjust terminators.
     */
    private void checkVertexIntersections(MouseEvent e, double subjectX1, double subjectY1, Vertex sourceVertex) {
        List<Vertex> verticesList = verticesData.getVertices();
        int sourceIndex = verticesData.getVertexLink().getLink1();

        for (int i = 0; i < verticesList.size(); i++) {
            Vertex vertex = verticesList.get(i);

            if (Geometry.computeDistance(vertex.getX(), vertex.getY(), e.getX(), e.getY()) <=
                    vertex.getRadius() + uiState.getVertexLinkMargin()) {

                if (i != sourceIndex) {
                    // Calculate angle to target vertex
                    double angle2 = Geometry.computeAngle(subjectX1, subjectY1, vertex.getX(), vertex.getY()) + Math.PI;

                    // Set second terminator to target vertex
                    setSecondTerminatorToVertex(vertex, angle2);

                    // Recalculate first terminator for stability
                    recalculateFirstTerminator(subjectX1, subjectY1, sourceVertex);
                }
            }
        }
    }

    /**
     * Set second terminator to target vertex.
     */
    private void setSecondTerminatorToVertex(Vertex targetVertex, double angle) {
        int x2 = (int) (targetVertex.getX() +
                Math.cos(angle) * (targetVertex.getRadius() + uiState.getVertexLinkMargin()));
        int y2 = (int) (targetVertex.getY() -
                Math.sin(angle) * (targetVertex.getRadius() + uiState.getVertexLinkMargin()));

        verticesData.getVertexPossibleLink().setX2(x2);
        verticesData.getVertexPossibleLink().setY2(y2);
    }

    /**
     * Recalculate first terminator for stability.
     */
    private void recalculateFirstTerminator(double subjectX1, double subjectY1, Vertex sourceVertex) {
        double angle = Geometry.computeAngle(subjectX1, subjectY1,
                verticesData.getVertexPossibleLink().getX2(),
                verticesData.getVertexPossibleLink().getY2());

        setFirstTerminator(sourceVertex, angle);
    }
}