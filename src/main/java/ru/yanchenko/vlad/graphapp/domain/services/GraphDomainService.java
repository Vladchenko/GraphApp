package ru.yanchenko.vlad.graphapp.domain.services;


import ru.yanchenko.vlad.graphapp.domain.entities.Edge;
import ru.yanchenko.vlad.graphapp.domain.entities.Graph;
import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;

import java.util.Collections;
import java.util.List;

/**
 * Central service for managing graph domain data (vertices and edges).
 * <p>
 * Acts as the single source of truth for the graph model. Mutations
 * modify the internal state in place for performance.
 */
public class GraphDomainService {

    /**
     * The current graph state. Modified in place on every mutation.
     */
    private Graph currentGraph;

    /**
     * Creates an empty graph with no vertices or edges.
     */
    public GraphDomainService() {
        this.currentGraph = new Graph(Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Returns the current graph instance.
     * <p>
     * Use the mutation methods ({@code addVertex}, {@code removeVertex}, etc.)
     * to modify it.
     *
     * @return the current graph
     */
    public Graph getCurrentGraph() {
        return currentGraph;
    }

    /**
     * Replaces the current graph with a new instance.
     * <p>
     * Typically used by facade layers or persistence services to restore
     * a graph loaded from disk.
     *
     * @param newGraph the new graph to set as current
     */
    public void updateGraph(Graph newGraph) {
        this.currentGraph = newGraph;
    }

    /**
     * Adds a vertex to the graph.
     * <p>
     * The vertex index is {@code vertices.size() - 1} after the addition.
     *
     * @param vertex the vertex to add
     */
    public void addVertex(Vertex vertex) {
        currentGraph.getVertices().add(vertex);
    }

    /**
     * Removes a vertex at the specified index and all edges connected to it.
     * <p>
     * After removal, indices of vertices that come after the deleted one are
     * decremented by 1, and all edge endpoints are adjusted accordingly.
     *
     * @param index the index of the vertex to remove
     */
    public void removeVertex(int index) {
        List<Vertex> vertices = currentGraph.getVertices();
        List<Edge> edges = currentGraph.getEdges();

        if (index >= 0 && index < vertices.size()) {
            vertices.remove(index);
            // Remove edges connected to this vertex
            edges.removeIf(edge -> edge.from() == index || edge.to() == index);
            // Adjust indices for remaining edges
            for (int i = edges.size() - 1; i >= 0; i--) {
                Edge edge = edges.get(i);
                int from = edge.from();
                int to = edge.to();
                if (from > index) {
                    from--;
                }
                if (to > index) {
                    to--;
                }
                edges.set(i, new Edge(from, to));
            }
        }
    }

    /**
     * Clears all vertices from the graph.
     */
    public void clearVertices() {
        currentGraph.getVertices().clear();
    }

    /**
     * Clears all edges from the graph.
     */
    public void clearEdges() {
        currentGraph.getEdges().clear();
    }

    /**
     * Adds an edge to the graph.
     * <p>
     * Validates that both vertex indices are non-negative and within the
     * current vertex list bounds.
     *
     * @param edge the edge to add
     * @throws IllegalArgumentException if edge indices are negative
     * @throws IndexOutOfBoundsException if edge indices exceed vertex count
     */
    public void addEdge(Edge edge) {
        if (edge.from() < 0 || edge.to() < 0) {
            throw new IllegalArgumentException("Edge indices must be non-negative: " + edge);
        }
        if (edge.from() >= currentGraph.getVertices().size() || edge.to() >= currentGraph.getVertices().size()) {
            throw new IndexOutOfBoundsException("Edge references non-existent vertex: " + edge);
        }
        currentGraph.getEdges().add(edge);
    }

    /**
     * Removes an edge from the graph.
     * <p>
     * Removes the first edge that matches both {@code from} and {@code to}
     * of the given edge.
     *
     * @param edge the edge to remove
     */
    public void removeEdge(Edge edge) {
        currentGraph.getEdges().removeIf(e -> e.from() == edge.from() && e.to() == edge.to());
    }

    /**
     * Removes an edge at the specified index.
     *
     * @param index the index of the edge to remove
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public void removeEdge(int index) {
        if (index > -1 && index < currentGraph.getEdges().size()) {
            currentGraph.getEdges().remove(index);
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds when removing edge");
        }
    }
}
