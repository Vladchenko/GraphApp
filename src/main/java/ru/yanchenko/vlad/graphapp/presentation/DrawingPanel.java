package ru.yanchenko.vlad.graphapp.presentation;

import ru.yanchenko.vlad.graphapp.domain.services.GraphDomainService;
import ru.yanchenko.vlad.graphapp.infrastructure.contexts.RefreshService;
import ru.yanchenko.vlad.graphapp.presentation.painter.EdgePainter;
import ru.yanchenko.vlad.graphapp.presentation.painter.VertexPainter;
import ru.yanchenko.vlad.graphapp.shared.ScreenData;
import ru.yanchenko.vlad.graphapp.shared.UiColors;
import ru.yanchenko.vlad.graphapp.shared.VertexFont;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

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
 * @see EdgePainter
 * @see VertexPainter
 */
public class DrawingPanel extends JPanel {

    private final GraphUiState uiState;
    private final ScreenData screenData;
    private final EdgePainter edgePainter;
    private final VertexPainter vertexPainter;
    private final GraphDomainService graphService;

    /**
     * Creates a {@code DrawingPanel} with all required dependencies.
     * <p>
     * Registers this panel's {@link #paintComponent(Graphics)} method as the refresh callback
     * on the provided {@link RefreshService}, enabling automatic repaint triggers
     * from domain and action layers. Delegates vertex and edge rendering to
     * {@link VertexPainter} and {@link EdgePainter} respectively.
     *
     * @param uiState        the current UI state (selection, edit buffer, operation flags)
     * @param screenData     screen dimensions and center point for positioning
     * @param edgePainter    painter for rendering graph edges and edge previews
     * @param vertexPainter  painter for rendering graph vertices
     * @param refreshService service that triggers repaint via callback
     * @param graphService   the graph domain service providing vertex and edge data for debug info
     */
    public DrawingPanel(GraphUiState uiState,
                        ScreenData screenData,
                        EdgePainter edgePainter,
                        VertexPainter vertexPainter,
                        RefreshService refreshService,
                        GraphDomainService graphService) {
        this.uiState = uiState;
        this.screenData = screenData;
        this.edgePainter = edgePainter;
        this.graphService = graphService;
        this.vertexPainter = vertexPainter;

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
        edgePainter.drawEdges(g2);
        edgePainter.drawPossibleEdge(g2);
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
        g2.drawString("Edges: " + graphService.getCurrentGraph().getEdges().size(), 10, y);
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
