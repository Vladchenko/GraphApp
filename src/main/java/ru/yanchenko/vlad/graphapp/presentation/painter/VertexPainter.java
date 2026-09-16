package ru.yanchenko.vlad.graphapp.presentation.painter;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.UiColors;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

import static ru.yanchenko.vlad.graphapp.models.UiColors.VERTEX_BACKGROUND_COLORS;
import static ru.yanchenko.vlad.graphapp.models.UiColors.VERTEX_BACKGROUND_COLORS_FRACTIONS;

/**
 * Renders graph vertices to a {@link Graphics2D} context.
 * <p>
 * Handles all visual aspects of vertex rendering: radial gradient background,
 * selection-aware arc styling with dash patterns, and vertex name text placement.
 * <p>
 * Each vertex is drawn independently, with selection state determined by
 * comparing the vertex index against the current selection in {@link GraphUiState}.
 *
 * @see Vertex
 * @see GraphUiState
 * @see ConnectionPointCalculator
 */
public class VertexPainter {

    private final GraphUiState uiState;
    private final GraphDomainService graphService;
    private final ConnectionPointCalculator connectionPointCalculator;

    /**
     * Creates a {@code VertexPainter} with the specified dependencies.
     * <p>
     * The painter uses {@code graphService} to fetch the vertex list for rendering,
     * {@code uiState} to determine selection state and rendering parameters,
     * and {@code connectionPointCalculator} to compute dash patterns for vertex arcs.
     *
     * @param connectionPointCalculator calculator for vertex arc dash patterns
     * @param graphService                the graph domain service providing vertex data
     * @param uiState                       the UI state containing selection and rendering parameters
     */
    public VertexPainter(ConnectionPointCalculator connectionPointCalculator,
                         GraphDomainService graphService,
                         GraphUiState uiState) {
        this.uiState = uiState;
        this.graphService = graphService;
        this.connectionPointCalculator = connectionPointCalculator;
    }

    /**
     * Draws all vertices from the current graph, applying selection styling
     * to the currently selected vertex.
     * <p>
     * Iterates through all vertices in the graph and calls {@link #drawVertex(Graphics2D, Vertex, boolean)}
     * for each one, passing {@code true} for {@code isSelected} if the vertex index
     * matches the selected vertex index in {@link GraphUiState}.
     *
     * @param g2 the graphics context to draw on
     */
    public void drawVertices(Graphics2D g2) {
        List<Vertex> vertices = graphService.getCurrentGraph().getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            boolean isSelected = i == uiState.getSelectedVertexIndex();
            drawVertex(g2, vertex, isSelected);
        }
    }

    /**
     * Draws a single vertex with its gradient background and selection-aware arc styling.
     * <p>
     * This is the main entry point for vertex rendering, composed of two steps:
     * 1. Drawing the radial gradient background
     * 2. Drawing the selection-aware arcs and vertex name text
     *
     * @param g2        the graphics context to draw on
     * @param vertex    the vertex to draw
     * @param isSelected {@code true} if this vertex is currently selected
     */
    private void drawVertex(Graphics2D g2, Vertex vertex, boolean isSelected) {
        // Draw vertex background with gradient
        drawVertexBackground(g2, vertex);

        // Draw vertex with selection-aware coloring
        drawVertexWithSelection(g2, vertex, isSelected);
    }

    /**
     * Draws the vertex arcs with selection-aware coloring and the vertex name text.
     * <p>
     * Renders an outer arc (always) and an inner arc (styled based on selection state).
     * Saves and restores the original {@link Color} and {@link Stroke} to avoid
     * side effects on subsequent rendering operations.
     *
     * @param g2        the graphics context to draw on
     * @param vertex    the vertex to draw
     * @param isSelected {@code true} if this vertex is currently selected (uses selected color)
     */
    private void drawVertexWithSelection(Graphics2D g2, Vertex vertex, boolean isSelected) {
        // Save current color and stroke
        Color originalColor = g2.getColor();
        Stroke originalStroke = g2.getStroke();

        try {
            // Calculate dash pattern (extracted from Vertex logic)
            float[] dash = connectionPointCalculator.calculateDashPattern(vertex);

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
     * Draws the radial gradient background fill for a vertex circle.
     * <p>
     * Uses a {@link RadialGradientPaint} centered on the vertex with a radius
     * matching the vertex size. The gradient uses predefined color fractions
     * from {@link UiColors}.
     *
     * @param g2     the graphics context to draw on
     * @param vertex the vertex to draw the background for
     */
    private void drawVertexBackground(Graphics2D g2, Vertex vertex) {
        Point2D center = new Point2D.Float((float) vertex.getX(), (float) vertex.getY());
        RadialGradientPaint gradientPaint = new RadialGradientPaint(
                center,
                (float) vertex.getRadius(),
                VERTEX_BACKGROUND_COLORS_FRACTIONS,
                VERTEX_BACKGROUND_COLORS,
                MultipleGradientPaint.CycleMethod.REPEAT
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
     * Draws the vertex name text centered within the vertex circle.
     * <p>
     * For single-character names, applies a slight right offset for visual centering.
     * For multi-character names, centers the text horizontally within the vertex.
     *
     * @param g2     the graphics context to draw on
     * @param vertex the vertex whose name to draw
     */
    public void drawVertexText(Graphics2D g2, Vertex vertex) {
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
}
