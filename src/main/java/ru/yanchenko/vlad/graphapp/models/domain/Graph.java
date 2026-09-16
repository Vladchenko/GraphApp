package ru.yanchenko.vlad.graphapp.models.domain;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

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

    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
    }

    // Only getters - no business logic
    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}

