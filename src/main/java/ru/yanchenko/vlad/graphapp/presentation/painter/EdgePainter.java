package ru.yanchenko.vlad.graphapp.presentation.painter;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.graphactions.actions.mouse.MouseActionManager;
import ru.yanchenko.vlad.graphapp.models.UiColors;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiData;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Painter responsible for rendering edges and edge previews in the graph.
 * <p>
 * Delegates connection point calculation to {@link ConnectionPointCalculator}
 * and draws edges as lines with terminator circles at vertex boundaries.
 * Handles both persistent edges (from {@link GraphDomainService}) and
 * temporary edge previews (user dragging to create a new link).
 *
 * @see ConnectionPointCalculator
 * @see VertexPainter
 */
public class EdgePainter {

    private final GraphUiData graphUiData;
    private final GraphDomainService graphService;
    private final MouseActionManager mouseActionManager;
    private final ConnectionPointCalculator connectionPointCalculator;

    /**
     * Creates an {@code EdgePainter} with the specified dependencies.
     * <p>
     * The painter uses {@code graphService} to fetch persistent edges, {@code graphUiData}
     * to read the current edge preview state, {@code mouseActionManager} to determine
     * which mouse button is active (for solid vs dashed line rendering), and
     * {@code connectionPointCalculator} to compute edge endpoints on vertex boundaries.
     *
     * @param graphUiData               the graph UI data containing edge preview state
     * @param graphService              the graph domain service providing persistent edge data
     * @param mouseActionManager        the mouse action manager for right-button state detection
     * @param connectionPointCalculator the calculator for vertex boundary connection points
     */
    public EdgePainter(GraphUiData graphUiData,
                       GraphDomainService graphService,
                       MouseActionManager mouseActionManager,
                       ConnectionPointCalculator connectionPointCalculator) {
        this.graphUiData = graphUiData;
        this.graphService = graphService;
        this.mouseActionManager = mouseActionManager;
        this.connectionPointCalculator = connectionPointCalculator;
    }

    /**
     * Draws the preview line for an edge being created by the user.
     * <p>
     * Renders a solid line with terminators for left-button drag, or a dashed line
     * (no terminators) for right-button drag. The preview state is read from
     * {@link GraphUiData}. The line style differentiates between adding (solid) and
     * removing (dashed) edges.
     *
     * @param g2 the graphics context to draw on
     */
    public void drawPossibleEdge(Graphics2D g2) {
        VertexPossibleLink link = graphUiData.getPossibleLink();
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
            drawEdgeTerminator(g2, link.getX2(), link.getY2());
            drawEdgeTerminator(g2, link.getX1(), link.getY1());
        }
    }

    /**
     * Draws all edges between vertices in the current graph.
     * <p>
     * Iterates through all domain {@link Edge} objects and calls
     * {@link #drawEdge(Graphics2D, Edge, List)} for each one.
     *
     * @param g2 the graphics context to draw on
     */
    public void drawEdges(Graphics2D g2) {
        g2.setColor(UiColors.VERTEX_LINKS_COLOR);
        List<Vertex> vertices = graphService.getCurrentGraph().getVertices();
        for (Edge edge : graphService.getCurrentGraph().getEdges()) {
            drawEdge(g2, edge, vertices);
        }
    }

    /**
     * Draws a single edge between two vertices, including connection terminators.
     * <p>
     * Calculates connection points on each vertex's boundary using
     * {@link ConnectionPointCalculator#calculateConnectionPoint(Vertex, Vertex)},
     * draws the connecting line, and renders small circles at both endpoints.
     *
     * @param g2       the graphics context to draw on
     * @param edge     the link containing source and target vertex indices
     * @param vertices the list of all vertices in the graph
     */
    private void drawEdge(Graphics2D g2, Edge edge, List<Vertex> vertices) {
        // Validate indices
        if (edge.from() >= vertices.size() || edge.to() >= vertices.size()) {
            return;
        }

        Vertex vertex1 = vertices.get(edge.from());
        Vertex vertex2 = vertices.get(edge.to());

        // Calculate connection points on vertex boundaries
        Point2D connectionPoint1 = connectionPointCalculator.calculateConnectionPoint(vertex1, vertex2);
        Point2D connectionPoint2 = connectionPointCalculator.calculateConnectionPoint(vertex2, vertex1);

        // Draw the edge line
        g2.drawLine(
                (int) connectionPoint1.getX(), (int) connectionPoint1.getY(),
                (int) connectionPoint2.getX(), (int) connectionPoint2.getY()
        );

        // Draw connection terminators
        drawEdgeTerminator(g2, connectionPoint1.getX(), connectionPoint1.getY());
        drawEdgeTerminator(g2, connectionPoint2.getX(), connectionPoint2.getY());
    }

    /**
     * Draws a small 6x6 pixel circle at the specified coordinates.
     * <p>
     * Used as a terminator marker at each end of an edge line to visually indicate
     * the connection point on a vertex boundary. This shared method is called by both
     * {@link #drawEdge(Graphics2D, Edge, List)} and {@link #drawPossibleEdge(Graphics2D)}.
     *
     * @param g2 the graphics context to draw on
     * @param x  the x-coordinate of the terminator center
     * @param y  the y-coordinate of the terminator center
     */
    private void drawEdgeTerminator(Graphics2D g2, double x, double y) {
        g2.drawOval((int) x - 3, (int) y - 3, 6, 6);
    }
}
