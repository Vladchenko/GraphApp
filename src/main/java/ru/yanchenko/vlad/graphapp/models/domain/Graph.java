package ru.yanchenko.vlad.graphapp.models.domain;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.ArrayList;
import java.util.List;

// Pure domain model - just data, no operations
public class Graph {
    private final List<Vertex> vertices;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
    }

    // Only getters - no business logic
    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    // Immutable - no setters
}

