package ru.yanchenko.vlad.graphapp.domain.services;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for validating vertex operations, primarily duplicate name checks.
 * <p>
 * Ensures that no two vertices share the same name before allowing insertion.
 */
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
     * @param vertices the list of vertices to check against
     * @return true if vertex can be added (doesn't exist), false if it already exists
     */
    public boolean isVertexExist(String vertexName, List<Vertex> vertices) {
        return isVertexCanBeAdded(vertexName, vertices);
    }

    /**
     * Internal method to check if a vertex can be added.
     * 
     * @param vertexName the name to check
     * @param vertices the list of vertices to check against
     * @return true if vertex can be added (doesn't exist), false if it already exists
     */
    private boolean isVertexCanBeAdded(String vertexName, List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            if (vertex.getVertexName().equals(vertexName)) {
                Logger.getLogger(VertexValidationService.class.getName())
                        .log(Level.INFO, "Vertex not added, since already exists: " + vertexName);
                return false; // Vertex already exists, cannot add
            }
        }
        return true; // Vertex doesn't exist, can add
    }
}
