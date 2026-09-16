package ru.yanchenko.vlad.graphapp.domain.graph;


import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.domain.Graph;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GraphDomainService {
    private Graph currentGraph;

    public GraphDomainService() {
        this.currentGraph = new Graph(Collections.emptyList(), Collections.emptyList());
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public void updateGraph(Graph newGraph) {
        this.currentGraph = newGraph;
    }

    public void addVertex(Vertex vertex) {
        currentGraph.getVertices().add(vertex);
    }

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

    public void clearVertices() {
        currentGraph.getVertices().clear();
    }

    public void clearEdges() {
        currentGraph.getEdges().clear();
    }

    public void addEdge(Edge edge) {
        currentGraph.getEdges().add(edge);
    }

    public void removeEdge(Edge edge) {
        currentGraph.getEdges().removeIf(e -> e.from() == edge.from() && e.to() == edge.to());
    }

    public void removeEdge(int index) {
        if (index > -1 && index < currentGraph.getEdges().size()) {
            currentGraph.getEdges().remove(index);
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds when removing edge");
        }
    }

    /**
     * Converts the current graph's edges to a list of legacy {@link VertexLink} objects.
     * <p>
     * Provided for backward compatibility with code that still uses {@code VertexLink}.
     *
     * @return a list of VertexLink instances representing the current edges
     */
    public List<VertexLink> convertEdgesToLinks() {
        return currentGraph.getEdges().stream()
                .map(edge -> new VertexLink(edge.from(), edge.to()))
                .collect(Collectors.toList());
    }

    public void updateFromLinks(List<VertexLink> links) {
        List<Edge> edges = links.stream()
                .map(link -> new Edge(link.getLink1(), link.getLink2()))
                .toList();
        currentGraph.getEdges().clear();
        currentGraph.getEdges().addAll(edges);
    }
}