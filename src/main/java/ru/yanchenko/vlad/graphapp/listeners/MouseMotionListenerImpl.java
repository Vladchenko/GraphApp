package ru.yanchenko.vlad.graphapp.listeners;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
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
    private final GraphDomainService graphService; // Domain data
    private final GraphUiStateService uiStateService; // UI state
    private final KeyboardState keyboardState;

    public MouseMotionListenerImpl(DrawingTimer drawingTimer,
                                   GraphDomainService graphService,
                                   GraphUiStateService uiStateService,
                                   KeyboardState keyboardState) {
        this.drawingTimer = drawingTimer;
        this.graphService = graphService;
        this.uiStateService = uiStateService;
        this.keyboardState = keyboardState;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            // Update mouse position in UI state
            uiStateService.getUiState().setCurrentMousePosition(e.getPoint());

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
            uiStateService.getUiState().setCurrentMousePosition(e.getPoint());

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error in mouse move", ex);
        }
    }

    /**
     * Handle vertex dragging when Ctrl key is pressed.
     */
    private void handleVertexDragging(MouseEvent e) {
        int chosenVertex = uiStateService.getUiData().getCurrentLink().getLink1();
        List<Vertex> verticesList = graphService.getCurrentGraph().getVertices();

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
        int chosenVertex = uiStateService.getUiData().getCurrentLink().getLink1();
        VertexLink vertexLink = uiStateService.getUiData().getCurrentLink();
        List<Vertex> verticesList = graphService.getCurrentGraph().getVertices();

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
                Math.cos(angle) * (sourceVertex.getRadius() + uiStateService.getUiState().getVertexLinkMargin()));
        int y1 = (int) (sourceVertex.getY() -
                Math.sin(angle) * (sourceVertex.getRadius() + uiStateService.getUiState().getVertexLinkMargin()));

        uiStateService.getUiData().getPossibleLink().setX1(x1);
        uiStateService.getUiData().getPossibleLink().setY1(y1);
    }

    /**
     * Check if mouse is within vertex area.
     */
    private boolean isMouseWithinVertexArea(double vertexX, double vertexY, int mouseX, int mouseY, Vertex vertex) {
        return Geometry.computeDistance(vertexX, vertexY, mouseX, mouseY) <=
                vertex.getRadius() + uiStateService.getUiState().getVertexLinkMargin();
    }

    /**
     * Set second terminator to same as first.
     */
    private void setSecondTerminatorToFirst() {
        uiStateService.getUiData().getPossibleLink().setX2(
                uiStateService.getUiData().getPossibleLink().getX1());
        uiStateService.getUiData().getPossibleLink().setY2(
                uiStateService.getUiData().getPossibleLink().getY1());
    }

    /**
     * Set second terminator to mouse position.
     */
    private void setSecondTerminatorToMouse(MouseEvent e) {
        uiStateService.getUiData().getPossibleLink().setX2(e.getX());
        uiStateService.getUiData().getPossibleLink().setY2(e.getY());
    }

    /**
     * Check for intersections with other vertices and adjust terminators.
     */
    private void checkVertexIntersections(MouseEvent e, double subjectX1, double subjectY1, Vertex sourceVertex) {
        List<Vertex> verticesList = graphService.getCurrentGraph().getVertices();
        int sourceIndex = uiStateService.getUiData().getCurrentLink().getLink1();

        for (int i = 0; i < verticesList.size(); i++) {
            Vertex vertex = verticesList.get(i);

            if (Geometry.computeDistance(vertex.getX(), vertex.getY(), e.getX(), e.getY()) <=
                    vertex.getRadius() + uiStateService.getUiState().getVertexLinkMargin()) {

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
                Math.cos(angle) * (targetVertex.getRadius() + uiStateService.getUiState().getVertexLinkMargin()));
        int y2 = (int) (targetVertex.getY() -
                Math.sin(angle) * (targetVertex.getRadius() + uiStateService.getUiState().getVertexLinkMargin()));

        uiStateService.getUiData().getPossibleLink().setX2(x2);
        uiStateService.getUiData().getPossibleLink().setY2(y2);
    }

    /**
     * Recalculate first terminator for stability.
     */
    private void recalculateFirstTerminator(double subjectX1, double subjectY1, Vertex sourceVertex) {
        double angle = Geometry.computeAngle(subjectX1, subjectY1,
                uiStateService.getUiData().getPossibleLink().getX2(),
                uiStateService.getUiData().getPossibleLink().getY2());

        setFirstTerminator(sourceVertex, angle);
    }
}