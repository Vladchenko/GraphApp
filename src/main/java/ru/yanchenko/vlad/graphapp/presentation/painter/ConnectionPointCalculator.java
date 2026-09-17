package ru.yanchenko.vlad.graphapp.presentation.painter;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.shared.geometry.Geometry;

import java.awt.geom.Point2D;

/**
 * Utility class for calculating rendering-related geometry for graph vertices.
 * <p>
 * Provides methods to compute connection points on vertex boundaries (where edges should attach)
 * and dash patterns for vertex arc rendering. These calculations depend on UI state
 * (e.g., link margin) and vertex properties (radius, name).
 * <p>
 * This class is a <b>final utility</b> — all public methods are instance-based due to
 * UI state dependency, but the class itself cannot be extended.
 *
 * @see GraphUiState
 * @see VertexPainter
 */
public final class ConnectionPointCalculator {

    private final GraphUiState uiState;

    /**
     * Creates a {@code ConnectionPointCalculator} with the specified UI state.
     * <p>
     * The UI state provides rendering parameters such as the link margin offset
     * used when calculating connection points on vertex boundaries.
     *
     * @param uiState the graph UI state containing rendering parameters
     */
    public ConnectionPointCalculator(GraphUiState uiState) {
        this.uiState = uiState;
    }

    /**
     * Calculates the dash pattern for rendering vertex arcs based on the vertex name length.
     * <p>
     * For single-character vertex names, returns a solid line ({@code [arc, 0]}).
     * For multi-character names, computes a dash pattern proportional to the circle
     * circumference divided by the name length, with a configurable blank space.
     *
     * @param vertex the vertex to calculate the dash pattern for
     * @return a two-element float array where element[0] is the dash length and element[1] is the gap length
     */
    public float[] calculateDashPattern(Vertex vertex) {
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
     * Calculates the point on the boundary of {@code fromVertex}'s circle where an edge line
     * to {@code toVertex} should connect.
     * <p>
     * The calculation uses the angle between the two vertices and adds the configured
     * link margin to ensure the edge starts exactly at the vertex boundary (not the center).
     *
     * @param fromVertex the source vertex whose boundary point is calculated
     * @param toVertex   the target vertex used to determine the connection angle
     * @return a {@link Point2D} representing the connection point on {@code fromVertex}'s boundary
     */
    public Point2D calculateConnectionPoint(Vertex fromVertex, Vertex toVertex) {
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
}
