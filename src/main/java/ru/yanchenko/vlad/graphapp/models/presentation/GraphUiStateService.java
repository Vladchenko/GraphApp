package ru.yanchenko.vlad.graphapp.models.presentation;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexPossibleLink;

import java.util.List;

public class GraphUiStateService {
    private final GraphUiData uiData;
    private final GraphUiState uiState;

    public GraphUiStateService(GraphUiState uiState) {
        this.uiState = uiState;
        this.uiData = new GraphUiData();
    }

    public GraphUiData getUiData() { return uiData; }
    public GraphUiState getUiState() { return uiState; }

    public void updatePolarCoordinates(List<Vertex> vertices) {
        uiData.updatePolarCoordinates(vertices);
    }

    public void setPolarCoordinates(List<VertexPolarCoordinate> coords) {
        uiData.setPolarCoordinates(coords);
    }

    public void setCurrentLink(VertexLink link) {
        uiData.setCurrentLink(link);
    }

    public void setPossibleLink(VertexPossibleLink link) {
        uiData.setPossibleLink(link);
    }
}