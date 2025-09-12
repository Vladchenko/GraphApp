package ru.yanchenko.vlad.graphapp.models.domain;

public record Edge(int from, int to) {
    public Edge {
        if (from < 0 || to < 0) {
            throw new IllegalArgumentException("Edge indices must be non-negative");
        }
    }
}
