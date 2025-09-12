package ru.yanchenko.vlad.graphapp.domain.verticesops.strategies;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

import javax.xml.parsers.ParserConfigurationException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixedFileStrategy implements VertexPopulationStrategy {
    private final Persistable persistable;

    public FixedFileStrategy(Persistable persistable) {
        this.persistable = persistable;
    }

    @Override
    public void populate(VerticesData verticesData, GraphUiState uiState) {
        try {
            persistable.loadFromFile(verticesData);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FixedFileStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        uiState.setAddingVertex(false);
    }
}
