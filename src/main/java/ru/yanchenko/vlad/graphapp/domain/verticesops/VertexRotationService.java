package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.List;

import static ru.yanchenko.vlad.graphapp.geometry.Geometry.toCartesian;
import static ru.yanchenko.vlad.graphapp.geometry.Geometry.toPolar;

public class VertexRotationService {

    public static final double RADIAN_INCREMENT = 0.01;

    private final ScreenData screenData;

    public VertexRotationService(ScreenData screenData) {
        this.screenData = screenData;
    }

    /**
     * Rotate vertices in circle by increasing their polar angle in radianInc
     *
     * @param radianInc increment for vertices to be rotated around center of screen
     */
    public void rotateVertices(double radianInc,
                               List<Vertex> vertices,
                               List<VertexPolarCoordinate> verticesPolarCoordinates) {
        for (int i = 0; i < vertices.size(); i++) {
            toPolar(
                    vertices.get(i),
                    verticesPolarCoordinates.get(i),
                    screenData.getScreenCenterPoint().x,
                    screenData.getScreenCenterPoint().y);
            // increase vertex's angle in radianInc
            verticesPolarCoordinates.get(i).setAngle(
                    verticesPolarCoordinates.get(i).getAngle()
                            + radianInc
            );
            toCartesian(
                    vertices.get(i),
                    verticesPolarCoordinates.get(i),
                    screenData.getScreenCenterPoint().x,
                    screenData.getScreenCenterPoint().y);
        }
    }
}
