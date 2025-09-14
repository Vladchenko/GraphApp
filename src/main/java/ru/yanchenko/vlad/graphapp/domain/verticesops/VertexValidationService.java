package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VertexValidationService {

    /**
     * Checks if a vertex with the given name can be added (i.e., doesn't already exist).
     * 
     * @param vertexName the name to check
     * @param graphService the graph domain service to check against
     * @return true if vertex can be added (doesn't exist), false if it already exists
     */
    public boolean isVertexExist(String vertexName, GraphDomainService graphService) {
        return isVertexCanBeAdded(vertexName, graphService.getCurrentGraph().getVertices());
    }

    /**
     * Checks if a vertex with the given name can be added (i.e., doesn't already exist).
     * 
     * @param vertexName the name to check
     * @param verticesData the vertices data to check against
     * @return true if vertex can be added (doesn't exist), false if it already exists
     * @deprecated Use isVertexExist(String, GraphDomainService) instead
     */
    @Deprecated
    public boolean isVertexExist(String vertexName, VerticesData verticesData) {
        return isVertexCanBeAdded(vertexName, verticesData.getVertices());
    }

    /**
     * Checks if a vertex with the given name can be added (i.e., doesn't already exist).
     * 
     * @param vertexName the name to check
     * @param vertices the list of vertices to check against
     * @return true if vertex can be added (doesn't exist), false if it already exists
     */
    public boolean isVertexExist(String vertexName, List<ru.yanchenko.vlad.graphapp.models.vertex.Vertex> vertices) {
        return isVertexCanBeAdded(vertexName, vertices);
    }

    /**
     * Internal method to check if a vertex can be added.
     * 
     * @param vertexName the name to check
     * @param vertices the list of vertices to check against
     * @return true if vertex can be added (doesn't exist), false if it already exists
     */
    private boolean isVertexCanBeAdded(String vertexName, List<ru.yanchenko.vlad.graphapp.models.vertex.Vertex> vertices) {
        for (ru.yanchenko.vlad.graphapp.models.vertex.Vertex vertex : vertices) {
            if (vertex.getVertexName().equals(vertexName)) {
                Logger.getLogger(VertexValidationService.class.getName())
                        .log(Level.INFO, "Vertex not added, since already exists: " + vertexName);
                return false; // Vertex already exists, cannot add
            }
        }
        return true; // Vertex doesn't exist, can add
    }
}