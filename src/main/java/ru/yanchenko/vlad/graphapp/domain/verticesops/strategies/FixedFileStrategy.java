package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

import javax.xml.parsers.ParserConfigurationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Strategy that loads vertices from a file without applying any layout transformation.
 * <p>
 * Vertex positions are taken directly from the file data as-is.
 *
 * @see VertexPopulationStrategy
 * @see Persistable
 */
public class FixedFileStrategy implements VertexPopulationStrategy {
    private final Persistable persistable;

    /**
     * Creates a FixedFileStrategy with the specified persistence service.
     *
     * @param persistable the persistence service for loading from file
     */
    public FixedFileStrategy(Persistable persistable) {
        this.persistable = persistable;
    }

    /**
     * Loads vertices from file without applying any layout transformation.
     *
     * @param graphService the graph domain service to populate
     * @param uiState the UI state to update
     */
    @Override
    public void populate(GraphDomainService graphService, GraphUiState uiState) {
        try {
            persistable.loadFromFile(graphService);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FixedFileStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
