package ru.yanchenko.vlad.graphapp.persistence;

import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import javax.xml.parsers.ParserConfigurationException;

public interface Persistable {
    void loadFromFile(VerticesData verticesData) throws ParserConfigurationException;
    void saveToFile(VerticesData verticesData);
}
