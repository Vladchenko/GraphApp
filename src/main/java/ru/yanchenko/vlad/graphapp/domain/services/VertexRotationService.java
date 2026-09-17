package ru.yanchenko.vlad.graphapp.domain.services;

import ru.yanchenko.vlad.graphapp.domain.entities.Vertex;
import ru.yanchenko.vlad.graphapp.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.shared.ScreenData;

import java.util.List;

import static ru.yanchenko.vlad.graphapp.shared.geometry.Geometry.toCartesian;
import static ru.yanchenko.vlad.graphapp.shared.geometry.Geometry.toPolar;

/**
 * Service for rotating vertices around the screen center using polar coordinates.
 * <p>
 * Converts vertex positions to polar coordinates, increments their angle,
 * and converts back to Cartesian coordinates to achieve circular rotation.
 */
public class VertexRotationService {

    public static final double RADIAN_INCREMENT = 0.01;

    private final ScreenData screenData;

    /**
     * Creates a VertexRotationService with the specified screen data.
     *
     * @param screenData the screen data containing center point
     */
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
