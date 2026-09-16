package ru.yanchenko.vlad.graphapp.persistence;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Contract for graph persistence operations.
 * <p>
 * Implementations handle loading and saving graph data
 * (vertices and edges) to various file formats.
 *
 * @see JsonPersistence
 * @see XMLPersistence
 */
public interface Persistable {
    /**
     * Loads graph data from a file.
     *
     * @param graphService the graph domain service to populate
     * @throws ParserConfigurationException if XML parser configuration fails
     */
    void loadFromFile(GraphDomainService graphService) throws ParserConfigurationException;

    /**
     * Saves graph data to a file.
     *
     * @param graphService the graph domain service to save
     */
    void saveToFile(GraphDomainService graphService);
}
