package ru.yanchenko.vlad.graphapp.presentation;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse.MouseActionManager;
import ru.yanchenko.vlad.graphapp.domain.graphactions.contexts.RefreshService;
import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.UiColors;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;
import ru.yanchenko.vlad.graphapp.presentation.painter.ConnectionPointCalculator;
import ru.yanchenko.vlad.graphapp.presentation.painter.VertexPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.List;

/**
 * Swing panel responsible for rendering the entire graph visualization.
 * <p>
 * Acts as a thin orchestrator that delegates rendering of individual graph elements
 * to specialized painter classes ({@link VertexPainter}, etc.). Manages rendering hints,
 * frame timing, and debug information display.
 * <p>
 * The panel is continuously repainted by a {@link DrawingTimer} (3ms interval),
 * ensuring smooth animations for vertex dragging, rotation, and link creation.
 *
 * @see DrawingTimer
 * @see VertexPainter
 * @see ConnectionPointCalculator
 */
public class DrawingPanel extends JPanel {

    private final GraphUiState uiState;
    private final ScreenData screenData;
    private final VertexPainter vertexPainter;
    private final GraphDomainService graphService;
    private final GraphUiStateService uiStateService;
    private final MouseActionManager mouseActionManager;
    private final ConnectionPointCalculator connectionPointCalculator;

    /**
     * Creates a {@code DrawingPanel} with all required dependencies.
     * <p>
     * Registers this panel's {@link #paintComponent(Graphics)} method as the refresh callback
     * on the provided {@link RefreshService}, enabling automatic repaint triggers
     * from domain and action layers.
     *
     * @param uiState                       the current UI state (selection, edit buffer, operation flags)
     * @param screenData                      screen dimensions and center point for positioning
     * @param vertexPainter                   painter for rendering graph vertices
     * @param refreshService                  service that triggers repaint via callback
     * @param graphService                    the graph domain service providing vertex and edge data
     * @param uiStateService                  service managing UI state and data (possible links, polar coords)
     * @param mouseActionManager              manager for mouse event handling
     * @param connectionPointCalculator       calculator for edge connection points on vertex boundaries
     */
    public DrawingPanel(GraphUiState uiState,
                        ScreenData screenData,
                        VertexPainter vertexPainter,
                        RefreshService refreshService,
                        GraphDomainService graphService,
                        GraphUiStateService uiStateService,
                        MouseActionManager mouseActionManager,
                        ConnectionPointCalculator connectionPointCalculator) {
        this.uiState = uiState;
        this.screenData = screenData;
        this.graphService = graphService;
        this.vertexPainter = vertexPainter;
        this.uiStateService = uiStateService;
        this.mouseActionManager = mouseActionManager;
        this.connectionPointCalculator = connectionPointCalculator;

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
        vertexPainter.drawVertices(g2);
        drawEditBox(g2);
        drawFrameTime(g2, beginTime);
    }

    /**
     * Configures antialiasing and rendering quality hints on the graphics context.
     * <p>
     * Enables antialiasing for smooth edges, text antialiasing for readable vertex names,
     * and sets the rendering quality to "quality" mode. Also sets the default stroke width to 2.
     *
     * @param g2 the graphics context to configure
     */
    private void configureRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2));
    }

    /**
     * Draws a small circle at the screen center to mark the graph's origin point.
     * <p>
     * The circle is 4x4 pixels, centered on the screen center point from {@link ScreenData}.
     *
     * @param g2 the graphics context to draw on
     */
    private void drawCenterAxis(Graphics2D g2) {
        g2.setColor(UiColors.CENTER_DOT_COLOR);
        Point center = screenData.getScreenCenterPoint();
        g2.drawOval(center.x - 2, center.y - 2, 4, 4);
    }

    /**
     * Draws all edges between vertices in the current graph.
     * <p>
     * Iterates through all edges (converted from domain {@link Edge} objects to {@link VertexLink} objects)
     * and calls {@link #drawEdge(Graphics2D, VertexLink, List)} for each one.
     *
     * @param g2 the graphics context to draw on
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
     * Draws a single edge between two vertices, including connection terminators.
     * <p>
     * Calculates connection points on each vertex's boundary using
     * {@link ConnectionPointCalculator#calculateConnectionPoint(Vertex, Vertex)},
     * draws the connecting line, and renders small circles at both endpoints.
     *
     * @param g2         the graphics context to draw on
     * @param link       the edge link containing source and target vertex indices
     * @param vertices   the list of all vertices in the graph
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
        Point2D connectionPoint1 = connectionPointCalculator.calculateConnectionPoint(vertex1, vertex2);
        Point2D connectionPoint2 = connectionPointCalculator.calculateConnectionPoint(vertex2, vertex1);

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
     * Draws a small 6x6 pixel circle at the specified connection point.
     * <p>
     * Used as a terminator marker at each end of an edge line to visually indicate
     * the connection point on a vertex boundary.
     *
     * @param g2   the graphics context to draw on
     * @param point the point where the terminator circle should be centered
     */
    private void drawConnectionTerminator(Graphics2D g2, Point2D point) {
        g2.drawOval((int) point.getX() - 3, (int) point.getY() - 3, 6, 6);
    }

    /**
     * Draws the preview line for a link being created by the user.
     * <p>
     * Renders a solid line for left-button drag or a dashed line for right-button drag,
     * with terminator circles at both ends. The preview is read from {@link GraphUiStateService}.
     *
     * @param g the graphics context to draw on
     */
    private void drawPossibleLink(Graphics g) {
        VertexPossibleLink link = uiStateService.getUiData().getPossibleLink();
        Graphics2D g2 = (Graphics2D) g;
        if (mouseActionManager.isRightMouseButton()) {
            g2.setColor(Color.BLACK);
            float[] dash0 = {5.0f, 15.0f};
            g2.setStroke(new BasicStroke(10,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_BEVEL,
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
     * Draws a semi-transparent edit box at the screen center when adding or editing a vertex.
     * <p>
     * Displays the current edit buffer text within a rounded rectangle. When in editing mode,
     * renders a blinking cursor at the end of the text.
     *
     * @param g2 the graphics context to draw on
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
     * Draws a blinking cursor at the specified position.
     * <p>
     * The cursor blinks every 500ms by toggling visibility based on {@link System#currentTimeMillis()}.
     *
     * @param g2 the graphics context to draw on
     * @param x  the x-coordinate of the cursor
     * @param y  the y-coordinate of the cursor top
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
     * Draws the current frame rendering time in the top-right corner.
     * <p>
     * Calculates elapsed time since {@code beginTime} (captured at the start of {@link #paintComponent(Graphics)})
     * and displays it in milliseconds. If debug mode is enabled, also shows additional debug info.
     *
     * @param g2        the graphics context to draw on
     * @param beginTime the time (ms) when painting started
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
     * Draws debug information in the top-left corner when debug mode is enabled.
     * <p>
     * Displays vertex count, edge count, selected vertex index, and current edit buffer content.
     *
     * @param g2 the graphics context to draw on
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