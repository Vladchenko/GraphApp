package ru.yanchenko.vlad.graphapp.models.presentation;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import java.util.ArrayList;
import java.util.List;

public class GraphUiData {
    private VertexLink currentLink;
    private VertexPossibleLink possibleLink;
    private final List<VertexPolarCoordinate> polarCoordinates;

    public GraphUiData() {
        this.currentLink = new VertexLink();
        this.possibleLink = new VertexPossibleLink();
        this.polarCoordinates = new ArrayList<>();
    }

    public GraphUiData(VertexLink currentLink,
                       VertexPossibleLink possibleLink,
                       List<VertexPolarCoordinate> polarCoordinates) {
        this.currentLink = currentLink;
        this.possibleLink = possibleLink;
        this.polarCoordinates = new ArrayList<>(polarCoordinates);
    }

    // Getters
    public VertexLink getCurrentLink() { return currentLink; }
    public VertexPossibleLink getPossibleLink() { return possibleLink; }
    public List<VertexPolarCoordinate> getPolarCoordinates() {
        return new ArrayList<>(polarCoordinates);
    }

    // Setters for mutable approach
    public void setCurrentLink(VertexLink currentLink) {
        this.currentLink = currentLink;
    }

    public void setPossibleLink(VertexPossibleLink possibleLink) {
        this.possibleLink = possibleLink;
    }

    public void setPolarCoordinates(List<VertexPolarCoordinate> polarCoordinates) {
        this.polarCoordinates.clear();
        this.polarCoordinates.addAll(polarCoordinates);
    }

    public void updatePolarCoordinates(List<Vertex> vertices) {
        this.polarCoordinates.clear();
        for (int i = 0; i < vertices.size(); i++) {
            this.polarCoordinates.add(new VertexPolarCoordinate());
        }
    }
}