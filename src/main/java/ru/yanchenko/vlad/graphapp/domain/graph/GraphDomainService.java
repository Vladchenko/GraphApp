package ru.yanchenko.vlad.graphapp.domain.graph;


import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.domain.Graph;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphDomainService {
    private Graph currentGraph;

    public GraphDomainService() {
        this.currentGraph = new Graph(new ArrayList<>(), new ArrayList<>());
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public void updateGraph(Graph newGraph) {
        this.currentGraph = newGraph;
    }

    public void addVertex(Vertex vertex) {
        List<Vertex> vertices = new ArrayList<>(currentGraph.getVertices());
        vertices.add(vertex);
        currentGraph = new Graph(vertices, currentGraph.getEdges());
    }

    public void removeVertex(int index) {
        List<Vertex> vertices = new ArrayList<>(currentGraph.getVertices());
        List<Edge> edges = new ArrayList<>(currentGraph.getEdges());

        if (index >= 0 && index < vertices.size()) {
            vertices.remove(index);

            // Remove edges connected to this vertex
            edges.removeIf(edge -> edge.from() == index || edge.to() == index);

            // Adjust indices for remaining edges
            edges = edges.stream()
                    .map(edge -> new Edge(
                            edge.from() > index ? edge.from() - 1 : edge.from(),
                            edge.to() > index ? edge.to() - 1 : edge.to()
                    ))
                    .collect(Collectors.toList());

            currentGraph = new Graph(vertices, edges);
        }
    }

    public void addEdge(Edge edge) {
        List<Edge> edges = new ArrayList<>(currentGraph.getEdges());
        edges.add(edge);
        currentGraph = new Graph(currentGraph.getVertices(), edges);
    }

    public void removeEdge(Edge edge) {
        List<Edge> edges = new ArrayList<>(currentGraph.getEdges());
        edges.removeIf(e -> e.from() == edge.from() && e.to() == edge.to());
        currentGraph = new Graph(currentGraph.getVertices(), edges);
    }

    // Conversion utilities for backward compatibility
    public List<VertexLink> convertEdgesToLinks() {
        return currentGraph.getEdges().stream()
                .map(edge -> new VertexLink(edge.from(), edge.to()))
                .collect(Collectors.toList());
    }

    public void updateFromLinks(List<VertexLink> links) {
        List<Edge> edges = links.stream()
                .map(link -> new Edge(link.getLink1(), link.getLink2()))
                .collect(Collectors.toList());
        currentGraph = new Graph(currentGraph.getVertices(), edges);
    }
}