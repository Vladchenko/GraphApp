package ru.yanchenko.vlad.graphapp.persistence;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;

import javax.xml.parsers.ParserConfigurationException;

public interface Persistable {
    void loadFromFile(GraphDomainService graphService) throws ParserConfigurationException;
    void saveToFile(GraphDomainService graphService);
}
