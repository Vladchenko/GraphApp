package ru.yanchenko.vlad.graphapp.persistence;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import javax.xml.parsers.ParserConfigurationException;

public interface Persistable {
    void loadFromFile(VerticesData verticesData) throws ParserConfigurationException;
    void saveToFile(VerticesData verticesData);
    
    // New methods for service-based architecture
    default void loadFromFile(GraphDomainService graphService) throws ParserConfigurationException {
        throw new UnsupportedOperationException("Method not implemented in this persistence class");
    }
    
    default void saveToFile(GraphDomainService graphService) {
        throw new UnsupportedOperationException("Method not implemented in this persistence class");
    }
}
