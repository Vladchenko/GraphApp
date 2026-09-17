package ru.yanchenko.vlad.graphapp.domain.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable domain model representing a directed graph.
 * <p>
 * Contains only data — vertices and edges — with no business logic.
 * Mutations go through {@link ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService}.
 * <p>
 * Edges reference vertices by index, not by object reference.
 */
public class Graph {
    private final List<Vertex> vertices;
    private final List<Edge> edges;

    /**
     * Creates a new graph with the given vertices and edges.
     * <p>
     * Copies the provided lists to prevent external mutation.
     *
     * @param vertices the list of vertices
     * @param edges    the list of edges
     */
    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
    }

    /**
     * Returns the list of vertices.
     *
     * @return the list of vertices
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Returns the list of edges.
     *
     * @return the list of edges
     */
    public List<Edge> getEdges() {
        return edges;
    }
}
