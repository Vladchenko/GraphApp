package ru.yanchenko.vlad.graphapp.models.presentation;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable data container for the presentation layer's graph state.
 * <p>
 * Holds the current link being created, a preview of possible links,
 * and polar coordinates for each vertex used in circular layout.
 */
public class GraphUiData {
    private VertexLink currentLink;
    private VertexPossibleLink possibleLink;
    private final List<VertexPolarCoordinate> polarCoordinates;

    /**
     * Creates a GraphUiData with default values.
     */
    public GraphUiData() {
        this.currentLink = new VertexLink();
        this.possibleLink = new VertexPossibleLink();
        this.polarCoordinates = new ArrayList<>();
    }

    /**
     * Creates a GraphUiData with the specified values.
     *
     * @param currentLink the current link being created
     * @param possibleLink the possible link preview
     * @param polarCoordinates the list of polar coordinates for vertices
     */
    public GraphUiData(VertexLink currentLink,
                       VertexPossibleLink possibleLink,
                       List<VertexPolarCoordinate> polarCoordinates) {
        this.currentLink = currentLink;
        this.possibleLink = possibleLink;
        this.polarCoordinates = new ArrayList<>(polarCoordinates);
    }

    // Getters
    /**
     * Returns the current link being created.
     *
     * @return the current link
     */
    public VertexLink getCurrentLink() { return currentLink; }

    /**
     * Returns the possible link preview.
     *
     * @return the possible link
     */
    public VertexPossibleLink getPossibleLink() { return possibleLink; }

    /**
     * Returns an unmodifiable copy of the polar coordinates list.
     *
     * @return the polar coordinates
     */
    public List<VertexPolarCoordinate> getPolarCoordinates() {
        return new ArrayList<>(polarCoordinates);
    }

    // Setters for mutable approach
    /**
     * Sets the current link being created.
     *
     * @param currentLink the current link
     */
    public void setCurrentLink(VertexLink currentLink) {
        this.currentLink = currentLink;
    }

    /**
     * Sets the possible link preview.
     *
     * @param possibleLink the possible link
     */
    public void setPossibleLink(VertexPossibleLink possibleLink) {
        this.possibleLink = possibleLink;
    }

    /**
     * Replaces the polar coordinates with the specified list.
     *
     * @param polarCoordinates the new polar coordinates
     */
    public void setPolarCoordinates(List<VertexPolarCoordinate> polarCoordinates) {
        this.polarCoordinates.clear();
        this.polarCoordinates.addAll(polarCoordinates);
    }

    /**
     * Updates the polar coordinates list to match the number of vertices.
     *
     * @param vertices the list of vertices
     */
    public void updatePolarCoordinates(List<Vertex> vertices) {
        this.polarCoordinates.clear();
        for (int i = 0; i < vertices.size(); i++) {
            this.polarCoordinates.add(new VertexPolarCoordinate());
        }
    }
}