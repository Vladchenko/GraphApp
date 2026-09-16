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

public class CircularFileStrategy implements VertexPopulationStrategy {
    private final Persistable persistable;
    private final Consumer<List<Vertex>> layoutStrategy;

    public CircularFileStrategy(Persistable persistable, Consumer<List<Vertex>> layoutStrategy) {
        this.persistable = persistable;
        this.layoutStrategy = layoutStrategy;
    }

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
