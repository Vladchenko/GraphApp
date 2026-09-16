package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

/**
 * Service responsible for deleting vertices and vertex links from the graph.
 */
public class VertexDeletionService {

    /**
     * Deletes a vertex by its index from the graph.
     * Also removes all edges that reference this vertex.
     *
     * @param index the index of the vertex to delete
     * @param domainService the domain service to interact with the graph
     * @param uiState the UI state to update
     */
    public void deleteVertexByIndex(int index, GraphDomainService domainService, GraphUiState uiState) {
        // Remove the vertex itself
        domainService.removeVertex(index);

        // Update UI state
        uiState.setDeletingVertex(false);
    }
}