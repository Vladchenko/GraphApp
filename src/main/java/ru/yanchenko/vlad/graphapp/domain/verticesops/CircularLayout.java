package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.List;
import java.util.function.Consumer;

public class CircularLayout implements Consumer<List<Vertex>> {

    private final ScreenData screenData;
    private final double radius;

    public CircularLayout(ScreenData screenData, double radius) {
        this.screenData = screenData;
        this.radius = radius;
    }

    @Override
    public void accept(List<Vertex> vertices) {
        if (vertices == null || vertices.isEmpty()) return;
        double angleIncrement = 0.0;
        double step = Math.PI * 2.0 / vertices.size();
        for (Vertex vertex : vertices) {
            vertex.setX((int) (radius * Math.cos(angleIncrement))
                    + screenData.getScreenCenterPoint().x);
            vertex.setY((int) (radius * Math.sin(angleIncrement))
                    + screenData.getScreenCenterPoint().y);
            angleIncrement += step;
        }
    }
}