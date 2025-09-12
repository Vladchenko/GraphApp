package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.util.List;

/**
 * Service responsible for deleting vertices and vertex links from the graph.
 */
public class VertexDeletionService {

    /**
     * Deletes a vertex by its name from the vertices data.
     * Also removes all links that reference this vertex.
     *
     * @param vertexName the name of the vertex to delete
     * @param verticesData the vertices data containing vertices and links
     * @param uiState the UI state to update
     */
    public void deleteVertexByName(String vertexName, VerticesData verticesData, GraphUiState uiState) {
        uiState.setDeletingVertex(true);

        // Find and delete the vertex by name
        for (int i = 0; i < verticesData.getVertices().size(); i++) {
            if (verticesData.getVertices().get(i).getVertexName().equals(vertexName)) {
                // Remove all links that reference this vertex
                removeLinksForVertex(i, verticesData.getVerticesLinks());
                // Remove the vertex itself
                verticesData.getVertices().remove(i);
                break;
            }
        }
    }

    /**
     * Deletes a vertex by its index from the vertices data.
     * Also removes all links that reference this vertex and adjusts remaining link indices.
     *
     * @param index the index of the vertex to delete
     * @param verticesData the vertices data containing vertices and links
     * @param uiState the UI state to update
     */
    public void deleteVertexByIndex(int index, VerticesData verticesData, GraphUiState uiState) {
        // Remove all links that reference this vertex
        removeLinksForVertex(index, verticesData.getVerticesLinks());

        // Adjust indices of remaining links
        adjustLinkIndicesAfterDeletion(index, verticesData.getVerticesLinks());

        // Remove the vertex itself
        verticesData.getVertices().remove(index);

        // Update UI state
        uiState.setDeletingVertex(false);
    }

    /**
     * Deletes a specific link between two vertices.
     *
     * @param vertex1 the first vertex index
     * @param vertex2 the second vertex index
     * @param verticesLinks the list of vertex links
     */
    public void deleteVerticesLink(int vertex1, int vertex2, List<VertexLink> verticesLinks) {
        for (int k = 0; k < verticesLinks.size(); k++) {
            VertexLink link = verticesLinks.get(k);
            if ((link.getLink1() == vertex1 && link.getLink2() == vertex2) ||
                    (link.getLink1() == vertex2 && link.getLink2() == vertex1)) {
                verticesLinks.remove(k);
                break; // Exit after removing the first matching link
            }
        }
    }

    /**
     * Removes all links that reference a specific vertex.
     *
     * @param vertexIndex the index of the vertex whose links should be removed
     * @param verticesLinks the list of vertex links
     */
    private void removeLinksForVertex(int vertexIndex, List<VertexLink> verticesLinks) {
        int k = 0;
        int initialSize = verticesLinks.size();

        while (k < initialSize) {
            VertexLink link = verticesLinks.get(k);
            if (link.getLink1() == vertexIndex || link.getLink2() == vertexIndex) {
                verticesLinks.remove(k);
                initialSize--;
                // When a vertex is removed, the following vertices are shifted back
                // by 1 position, so the vertex index has to be shifted back also
                k--;
            }
            k++;
        }
    }

    /**
     * Adjusts link indices after a vertex deletion.
     * Links with indices >= deletedVertexIndex need to be decremented by 1.
     *
     * @param deletedVertexIndex the index of the deleted vertex
     * @param verticesLinks the list of vertex links to adjust
     */
    private void adjustLinkIndicesAfterDeletion(int deletedVertexIndex, List<VertexLink> verticesLinks) {
        for (VertexLink link : verticesLinks) {
            if (link.getLink1() >= deletedVertexIndex) {
                link.setLink1(link.getLink1() - 1);
            }
            if (link.getLink2() >= deletedVertexIndex) {
                link.setLink2(link.getLink2() - 1);
            }
        }
    }
}