package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VertexPopulationServiceOld {

    private final Persistable persistable;
    private final Consumer<List<Vertex>> layoutStrategy;
    private final VertexCreationService vertexCreationService;

    public VertexPopulationServiceOld(Persistable persistable,
                                      Consumer<List<Vertex>> layoutStrategy,
                                      VertexCreationService vertexCreationService) {
        this.persistable = persistable;
        this.layoutStrategy = layoutStrategy;
        this.vertexCreationService = vertexCreationService;
    }

    public void populateVertices(PopulationKind populationKind, VerticesData verticesData, GraphUiState uiState) {
        switch (populationKind) {
            case HARDCODED_SAMPLE_A: {
                vertexCreationService.addVertexAtPosition("1", 100, 100, verticesData);
                vertexCreationService.addVertexAtPosition("12", 200, 200, verticesData);
                vertexCreationService.addVertexAtPosition("123", 300, 300, verticesData);
                vertexCreationService.addVertexAtPosition("1234", 400, 400, verticesData);
                vertexCreationService.addVertexAtPosition("12345", 500, 500, verticesData);
                vertexCreationService.addVertexAtPosition("123456", 600, 600, verticesData);
                vertexCreationService.addVertexAtPosition("123456789", 750, 750, verticesData);
                vertexCreationService.addVertexAtPosition("1234567890 abcdefghijk",750, 250, verticesData);
                vertexCreationService.addVertexAtPosition("1234567890 abcdefghijk lmnopqrstuv", 1150, 650, verticesData);
                break;
            }
            case HARDCODED_SAMPLE_B: {
                vertexCreationService.addVertexByName("a", verticesData);
                vertexCreationService.addVertexByName("bc", verticesData);
                vertexCreationService.addVertexByName("def", verticesData);
                vertexCreationService.addVertexByName("ghijkl", verticesData);
                vertexCreationService.addVertexByName("mno", verticesData);
                vertexCreationService.addVertexByName("pq", verticesData);
                vertexCreationService.addVertexByName("r", verticesData);
                vertexCreationService.addVertexByName("12", verticesData);
                vertexCreationService.addVertexByName("345", verticesData);
                vertexCreationService.addVertexByName("67890", verticesData);
                vertexCreationService.addVertexByName("!@#", verticesData);
                uiState.setAddingVertex(false);
                if (layoutStrategy != null) {
                    layoutStrategy.accept(verticesData.getVertices());
                }
                break;
            }
            case CIRCULAR_FILE: {
                try {
                    persistable.loadFromFile(verticesData);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(VertexPopulationServiceOld.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (layoutStrategy != null) {
                    layoutStrategy.accept(verticesData.getVertices());
                }
                uiState.setAddingVertex(false);
                break;
            }
            case FIXED_FILE: {
                try {
                    persistable.loadFromFile(verticesData);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(VertexPopulationServiceOld.class.getName()).log(Level.SEVERE, null, ex);
                }
                uiState.setAddingVertex(false);
                break;
            }
        }
        for (int i = 0; i < verticesData.getVertices().size(); i++) {
            verticesData.getVerticesPolarCoordinates().add(new VertexPolarCoordinate());
        }
    }
}