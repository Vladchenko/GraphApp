package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Strategy that loads vertices from a file and applies a circular layout.
 * <p>
 * Uses {@link Persistable} to read vertex data, then positions them
 * evenly around a circle via the provided layout strategy.
 *
 * @see VertexPopulationStrategy
 * @see Persistable
 */
public class CircularFileStrategy implements VertexPopulationStrategy {
    private final Persistable persistable;
    private final Consumer<List<Vertex>> layoutStrategy;

    /**
     * Creates a CircularFileStrategy with the specified persistence and layout strategy.
     *
     * @param persistable the persistence service for loading from file
     * @param layoutStrategy the layout strategy to apply after loading
     */
    public CircularFileStrategy(Persistable persistable, Consumer<List<Vertex>> layoutStrategy) {
        this.persistable = persistable;
        this.layoutStrategy = layoutStrategy;
    }

    /**
     * Loads vertices from file and applies circular layout.
     *
     * @param graphService the graph domain service to populate
     * @param uiState the UI state to update
     */
    @Override
    public void populate(GraphDomainService graphService, GraphUiState uiState) {
        try {
            persistable.loadFromFile(graphService);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CircularFileStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (layoutStrategy != null) {
            layoutStrategy.accept(graphService.getCurrentGraph().getVertices());
        }
        uiState.setAddingVertex(false);
    }
}
