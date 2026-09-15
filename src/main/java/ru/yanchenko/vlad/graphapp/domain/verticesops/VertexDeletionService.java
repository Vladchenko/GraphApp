package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;

import java.util.List;

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
        // Remove all links that reference this vertex
        removeLinksForVertex(index, domainService.getCurrentGraph().getEdges());

        // Remove the vertex itself
        domainService.removeVertex(index);

        // Update UI state
        uiState.setDeletingVertex(false);
    }

    /**
     * Removes all links that reference a specific vertex.
     *
     * @param vertexIndex the index of the vertex whose links should be removed
     * @param edges the list of vertex links
     */
    private void removeLinksForVertex(int vertexIndex, List<Edge> edges) {
        int k = 0;
        int initialSize = edges.size();

        while (k < initialSize) {
            Edge link = edges.get(k);
            if (link.from() == vertexIndex || link.to() == vertexIndex) {
                edges.remove(k);
                initialSize--;
                // Don't increment k — the next element shifted into position k
                // needs to be checked in the next iteration
                k--;
            }
            k++;
        }
    }
}