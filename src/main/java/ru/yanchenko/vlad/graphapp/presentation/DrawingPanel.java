package ru.yanchenko.vlad.graphapp.presentation;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse.MouseActionManager;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.RefreshService;
import ru.yanchenko.vlad.graphapp.geometry.Geometry;
import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.UiColors;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import javax.swing.*;
import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.List;

import static ru.yanchenko.vlad.graphapp.models.UiColors.VERTEX_BACKGROUND_COLORS;
import static ru.yanchenko.vlad.graphapp.models.UiColors.VERTEX_BACKGROUND_COLORS_FRACTIONS;

/**
 * Panel responsible for rendering the graph visualization.
 */
public class DrawingPanel extends JPanel {

    private final MouseActionManager mouseActionManager;
    private final ScreenData screenData;
    private final GraphDomainService graphService; // Domain service
    private final GraphUiStateService uiStateService; // UI state service
    private final GraphUiState uiState; // UI state

    public DrawingPanel(ScreenData screenData,
                        MouseActionManager mouseActionManager,
                        GraphDomainService graphService,
                        GraphUiStateService uiStateService,
                        GraphUiState uiState,
                        RefreshService refreshService) {
        this.uiState = uiState;
        this.screenData = screenData;
        this.graphService = graphService;
        this.uiStateService = uiStateService;
        this.mouseActionManager = mouseActionManager;
        
        // Set up the refresh callback
        refreshService.setRefreshCallback(this::repaint);
    }

    @Override
    public void paintComponent(Graphics g) {
        long beginTime = new Date().getTime();

        // Clear canvas
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        // Configure rendering hints
        configureRenderingHints(g2);

        // Draw all components
        drawCenterAxis(g2);
        drawEdges(g2);
        drawPossibleLink(g2);
        drawVertices(g2);
        drawEditBox(g2);
        drawFrameTime(g2, beginTime);
    }

    /**
     * Configure rendering hints for better visual quality.
     */
    private void configureRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2));
    }

    /**
     * Draw the center axis point of the graph.
     */
    private void drawCenterAxis(Graphics2D g2) {
        g2.setColor(UiColors.CENTER_DOT_COLOR);
        Point center = screenData.getScreenCenterPoint();
        g2.drawOval(center.x - 2, center.y - 2, 4, 4);
    }

    /**
     * Draw all edges between vertices.
     */
    private void drawEdges(Graphics2D g2) {
        g2.setColor(UiColors.VERTEX_LINKS_COLOR);
        List<VertexLink> verticesLinks = graphService.convertEdgesToLinks();
        List<Vertex> vertices = graphService.getCurrentGraph().getVertices();

        for (VertexLink link : verticesLinks) {
            drawEdge(g2, link, vertices);
        }
    }

    /**
     * Draw a single edge between two vertices.
     */
    private void drawEdge(Graphics2D g2, VertexLink link, List<Vertex> vertices) {
        int link1Index = link.getLink1();
        int link2Index = link.getLink2();

        // Validate indices
        if (link1Index >= vertices.size() || link2Index >= vertices.size()) {
            return;
        }

        Vertex vertex1 = vertices.get(link1Index);
        Vertex vertex2 = vertices.get(link2Index);

        // Calculate connection points on vertex boundaries
        Point2D connectionPoint1 = calculateConnectionPoint(vertex1, vertex2);
        Point2D connectionPoint2 = calculateConnectionPoint(vertex2, vertex1);

        // Draw the edge line
        g2.drawLine(
                (int) connectionPoint1.getX(), (int) connectionPoint1.getY(),
                (int) connectionPoint2.getX(), (int) connectionPoint2.getY()
        );

        // Draw connection terminators
        drawConnectionTerminator(g2, connectionPoint1);
        drawConnectionTerminator(g2, connectionPoint2);
    }

    /**
     * Calculate the connection point on a vertex's boundary.
     */
    private Point2D calculateConnectionPoint(Vertex fromVertex, Vertex toVertex) {
        double x1 = fromVertex.getX();
        double y1 = fromVertex.getY();
        double x2 = toVertex.getX();
        double y2 = toVertex.getY();

        // Calculate angle between vertices
        double angle = Geometry.computeAngle(x1, y1, x2, y2);

        // Calculate connection point on vertex boundary
        double connectionX = x1 + (fromVertex.getRadius() + uiState.getVertexLinkMargin()) * Math.cos(angle);
        double connectionY = y1 - (fromVertex.getRadius() + uiState.getVertexLinkMargin()) * Math.sin(angle);

        return new Point2D.Double(connectionX, connectionY);
    }

    /**
     * Draw a small circle at the connection point.
     */
    private void drawConnectionTerminator(Graphics2D g2, Point2D point) {
        g2.drawOval((int) point.getX() - 3, (int) point.getY() - 3, 6, 6);
    }

    /**
     * Draw the possible link being created (UI state).
     */
    private void drawPossibleLink(Graphics g) {
        VertexPossibleLink link = uiStateService.getUiData().getPossibleLink();
        Graphics2D g2 = (Graphics2D) g;
        if (mouseActionManager.isRightMouseButton()) {
            g2.setColor(Color.BLACK);
            float[] dash0 = {5.0f, 15.0f};
            g2.setStroke(new BasicStroke(10,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.CAP_ROUND,
                    2, dash0, 0.0f));
            g2.drawLine(link.getX1(), link.getY1(), link.getX2(), link.getY2());
        } else {
            g2.setColor(UiColors.NEW_LINK_COLOR);
            g2.drawLine(link.getX1(), link.getY1(), link.getX2(), link.getY2());
            g2.drawOval(link.getX2() - 3, link.getY2() - 3, 6, 6);
            g2.drawOval(link.getX1() - 3, link.getY1() - 3, 6, 6);
        }
    }

    /**
     * Draw all vertices.
     */
    private void drawVertices(Graphics2D g2) {
        List<Vertex> vertices = graphService.getCurrentGraph().getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            boolean isSelected = i == uiState.getSelectedVertexIndex();
            drawVertex(g2, vertex, isSelected);
        }
    }

    /**
     * Draw a single vertex with background and text.
     */
    private void drawVertex(Graphics2D g2, Vertex vertex, boolean isSelected) {
        // Draw vertex background with gradient
        drawVertexBackground(g2, vertex);

        // Draw vertex with selection-aware coloring
        drawVertexWithSelection(g2, vertex, isSelected);
    }

    /**
     * Draw vertex with selection-aware coloring.
     */
    private void drawVertexWithSelection(Graphics2D g2, Vertex vertex, boolean isSelected) {
        // Save current color and stroke
        Color originalColor = g2.getColor();
        Stroke originalStroke = g2.getStroke();

        try {
            // Calculate dash pattern (extracted from Vertex logic)
            float[] dash = calculateDashPattern(vertex);

            // Draw outer arc
            g2.setStroke(new BasicStroke(5,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    1.0f, dash, dash[0] / 2));
            g2.setColor(UiColors.ARCS_OUTER_COLOR);
            g2.drawArc((int) (vertex.getX() - vertex.getRadius()),
                    (int) (vertex.getY() - vertex.getRadius()),
                    (int) vertex.getRadius() * 2,
                    (int) vertex.getRadius() * 2, 0, 360);

            // Draw inner arc with selection-aware color
            g2.setStroke(new BasicStroke(2,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    1.0f, dash, dash[0] / 2));

            if (isSelected) {
                g2.setColor(UiColors.ARCS_INNER_SELECTED_COLOR);
            } else {
                g2.setColor(UiColors.ARCS_INNER_COLOR);
            }

            g2.drawArc((int) (vertex.getX() - vertex.getRadius()),
                    (int) (vertex.getY() - vertex.getRadius()),
                    (int) vertex.getRadius() * 2,
                    (int) vertex.getRadius() * 2, 0, 360);

            // Draw vertex text
            g2.setColor(UiColors.VERTEX_NAMES_COLOR);
            g2.setFont(VertexFont.VERTICES_FONT);
            drawVertexText(g2, vertex);

        } finally {
            // Restore original color and stroke
            g2.setColor(originalColor);
            g2.setStroke(originalStroke);
        }
    }

    /**
     * Calculate dash pattern for vertex arcs.
     */
    private float[] calculateDashPattern(Vertex vertex) {
        String vertexName = vertex.getVertexName();
        double circleDiameter = 2 * Math.PI * vertex.getRadius();
        float arcBlank = 16;
        if (vertexName.length() == 1) {
            arcBlank = 0;
        }
        float arc = (float) ((circleDiameter / vertexName.length()) - arcBlank);

        return new float[]{arc, arcBlank};
    }

    /**
     * Draw vertex text.
     */
    private void drawVertexText(Graphics2D g2, Vertex vertex) {
        String vertexName = vertex.getVertexName();

        if (vertexName.length() == 1) {
            g2.drawString(vertexName,
                    (int) (vertex.getX() - vertex.getRadius() / 2 + 1 - vertexName.length() * 1.5 + 3),
                    (int) (vertex.getY() + 10 - 1));
        } else {
            g2.drawString(vertexName,
                    (int) (vertex.getX() - vertex.getRadius() / 2 - vertexName.length() * 1.5),
                    (int) (vertex.getY() + 10 - 1));
        }
    }

    /**
     * Draw the gradient background for a vertex.
     */
    private void drawVertexBackground(Graphics2D g2, Vertex vertex) {
        Point2D center = new Point2D.Float((float) vertex.getX(), (float) vertex.getY());
        RadialGradientPaint gradientPaint = new RadialGradientPaint(
                center,
                (float) vertex.getRadius(),
                VERTEX_BACKGROUND_COLORS_FRACTIONS,
                VERTEX_BACKGROUND_COLORS,
                CycleMethod.REPEAT
        );

        g2.setPaint(gradientPaint);
        g2.fill(new Ellipse2D.Double(
                vertex.getX() - vertex.getRadius(),
                vertex.getY() - vertex.getRadius(),
                vertex.getRadius() * 2,
                vertex.getRadius() * 2
        ));
    }

    /**
     * Draw the edit box when adding or editing a vertex (UI state).
     */
    private void drawEditBox(Graphics2D g2) {
        if (!uiState.isAddingVertex() && !uiState.isEditingVertex()) {
            return;
        }

        String editText = uiState.getEditBuffer();
        if (editText == null || editText.isEmpty()) {
            return;
        }

        // Calculate text dimensions
        FontMetrics fontMetrics = g2.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(editText);
        int textHeight = fontMetrics.getHeight();

        // Calculate edit box position and size
        Point center = screenData.getScreenCenterPoint();
        int boxWidth = Math.max(textWidth + 20, 100); // Minimum width
        int boxHeight = 40;
        int boxX = center.x - boxWidth / 2;
        int boxY = center.y - boxHeight / 2;

        // Draw edit box background
        g2.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 5, 5);

        // Draw edit box border
        g2.setColor(UiColors.VERTEX_NAMES_COLOR);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 5, 5);

        // Draw text
        g2.setColor(UiColors.VERTEX_NAMES_COLOR);
        g2.setFont(VertexFont.VERTICES_FONT);
        int textX = center.x - textWidth / 2;
        int textY = center.y + textHeight / 4; // Center vertically
        g2.drawString(editText, textX, textY);

        // Draw cursor if in edit mode
        if (uiState.isEditingVertex()) {
            drawCursor(g2, textX + textWidth, textY - textHeight);
        }
    }

    /**
     * Draw a blinking cursor in the edit box.
     */
    private void drawCursor(Graphics2D g2, int x, int y) {
        long currentTime = System.currentTimeMillis();
        boolean shouldShow = (currentTime / 500) % 2 == 0; // Blink every 500ms

        if (shouldShow) {
            g2.setColor(UiColors.VERTEX_NAMES_COLOR);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x, y, x, y + 20);
        }
    }

    /**
     * Draw frame time information (UI state).
     */
    private void drawFrameTime(Graphics2D g2, long beginTime) {
        g2.setColor(UiColors.FRAME_TIME_COLOR);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        long spentTime = new Date().getTime() - beginTime;
        String frameTimeText = spentTime + " ms";

        // Draw frame time in top-right corner
        g2.drawString(frameTimeText, screenData.getScreenWidth() - 70, 20);

        // Draw additional debug info if needed
        if (uiState.isDebugMode()) {
            drawDebugInfo(g2);
        }
    }

    /**
     * Draw debug information (UI state).
     */
    private void drawDebugInfo(Graphics2D g2) {
        g2.setColor(UiColors.FRAME_TIME_COLOR);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        int y = 40;
        g2.drawString("Vertices: " + graphService.getCurrentGraph().getVertices().size(), 10, y);
        y += 15;
        g2.drawString("Edges: " + graphService.convertEdgesToLinks().size(), 10, y);
        y += 15;
        g2.drawString("Selected: " + uiState.getSelectedVertexIndex(), 10, y);
        y += 15;
        g2.drawString("Edit Buffer: '" + uiState.getEditBuffer() + "'", 10, y);
    }

    /**
     * Get the preferred size of the panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(screenData.getScreenWidth(), screenData.getScreenHeight());
    }

    /**
     * Get the minimum size of the panel.
     */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Get the maximum size of the panel.
     */
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}