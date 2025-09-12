package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VertexValidationService {

    public boolean isVertexExist(String vertexName, VerticesData verticesData) {
        boolean vertexAddition = true;
        for (int i = 0; i < verticesData.getVertices().size(); i++) {
            if (verticesData.getVertices().get(i).getVertexName().equals(vertexName)) {
                Logger.getLogger(VertexValidationService.class.getName())
                        .log(Level.INFO, "Vertex not added, since already exist" + vertexName);
                vertexAddition = false;
                break;
            }
        }
        return vertexAddition;
    }
}