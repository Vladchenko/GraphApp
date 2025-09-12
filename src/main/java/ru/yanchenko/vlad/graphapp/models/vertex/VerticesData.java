package ru.yanchenko.vlad.graphapp.models.vertex;

import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;

import java.util.List;

// Domain data only - no UI state
public class VerticesData {
    private final List<Vertex> vertices;
    private final List<VertexLink> verticesLinks;
    private final List<VertexPolarCoordinate> verticesPolarCoordinates;
    private VertexLink vertexLink; // Current link being created
    private VertexPossibleLink vertexPossibleLink; // UI preview state

    public VerticesData(List<Vertex> vertices,
                        VertexLink vertexLink,
                        VertexPossibleLink vertexPossibleLink,
                        List<VertexLink> verticesLinks,
                        List<VertexPolarCoordinate> verticesPolarCoordinates) {
        this.vertices = vertices;
        this.vertexLink = vertexLink;
        this.verticesLinks = verticesLinks;
        this.vertexPossibleLink = vertexPossibleLink;
        this.verticesPolarCoordinates = verticesPolarCoordinates;
    }

    // Domain data getters/setters
    public List<Vertex> getVertices() { return vertices; }
    public void setVertices(List<Vertex> vertices) { this.vertices.clear(); this.vertices.addAll(vertices); }

    public List<VertexLink> getVerticesLinks() { return verticesLinks; }
    public void setVerticesLinks(List<VertexLink> verticesLinks) { this.verticesLinks.clear(); this.verticesLinks.addAll(verticesLinks); }

    public List<VertexPolarCoordinate> getVerticesPolarCoordinates() { return verticesPolarCoordinates; }
    public void setVerticesPolarCoordinates(List<VertexPolarCoordinate> coords) { this.verticesPolarCoordinates.clear(); this.verticesPolarCoordinates.addAll(coords); }

    public VertexLink getVertexLink() { return vertexLink; }
    public void setVertexLink(VertexLink vertexLink) { this.vertexLink = vertexLink; }

    public VertexPossibleLink getVertexPossibleLink() { return vertexPossibleLink; }
    public void setVertexPossibleLink(VertexPossibleLink vertexPossibleLink) { this.vertexPossibleLink = vertexPossibleLink; }
}