package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;

import java.util.List;

public interface LayoutStrategy {
    void layout(List<Vertex> vertices);
}