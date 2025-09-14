package ru.yanchenko.vlad.graphapp.models.vertex;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.domain.Graph;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiData;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;

import java.util.List;

public class VerticesDataFacade extends VerticesData {
    public final GraphDomainService graphService;
    public final GraphUiStateService uiStateService;

    public VerticesDataFacade(GraphDomainService graphService,
                              GraphUiStateService uiStateService) {
        // Initialize with empty data - we'll override all methods to delegate to services
        super(new java.util.ArrayList<>(), new VertexLink(), new VertexPossibleLink(), 
              new java.util.ArrayList<>(), new java.util.ArrayList<>());
        this.graphService = graphService;
        this.uiStateService = uiStateService;
    }

    // Provide VerticesData interface for backward compatibility
    public VerticesData asVerticesData() {
        Graph graph = graphService.getCurrentGraph();
        GraphUiData uiData = uiStateService.getUiData();

        return new VerticesData(
                graph.getVertices(),
                uiData.getCurrentLink(),
                uiData.getPossibleLink(),
                graphService.convertEdgesToLinks(),
                uiData.getPolarCoordinates()
        );
    }

    // Override parent methods to delegate to services
    @Override
    public List<Vertex> getVertices() {
        List<Vertex> vertices = graphService.getCurrentGraph().getVertices();
        return vertices;
    }

    @Override
    public void setVertices(List<Vertex> vertices) {
        Graph currentGraph = graphService.getCurrentGraph();
        graphService.updateGraph(new Graph(vertices, currentGraph.getEdges()));
    }

    @Override
    public List<VertexLink> getVerticesLinks() {
        return graphService.convertEdgesToLinks();
    }

    @Override
    public void setVerticesLinks(List<VertexLink> links) {
        graphService.updateFromLinks(links);
    }

    @Override
    public List<VertexPolarCoordinate> getVerticesPolarCoordinates() {
        return uiStateService.getUiData().getPolarCoordinates();
    }

    @Override
    public void setVerticesPolarCoordinates(List<VertexPolarCoordinate> coords) {
        uiStateService.setPolarCoordinates(coords);
    }

    @Override
    public VertexLink getVertexLink() {
        return uiStateService.getUiData().getCurrentLink();
    }

    @Override
    public void setVertexLink(VertexLink link) {
        uiStateService.setCurrentLink(link);
    }

    @Override
    public VertexPossibleLink getVertexPossibleLink() {
        return uiStateService.getUiData().getPossibleLink();
    }

    @Override
    public void setVertexPossibleLink(VertexPossibleLink link) {
        uiStateService.setPossibleLink(link);
    }
}