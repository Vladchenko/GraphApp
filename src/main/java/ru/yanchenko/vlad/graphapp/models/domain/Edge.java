package ru.yanchenko.vlad.graphapp.models.domain;

/**
 * Represents a directed edge between two vertices in the graph.
 * <p>
 * Stored as indices into the vertices list, not as vertex references,
 * to keep the model lightweight and serializable.
 *
 * @param from source vertex index (non-negative)
 * @param to   target vertex index (non-negative)
 */
public record Edge(int from, int to) {
    /**
     * Validates that both vertex indices are non-negative.
     *
     * @throws IllegalArgumentException if {@code from} or {@code to} is negative
     */
    public Edge {
        if (from < 0 || to < 0) {
            throw new IllegalArgumentException("Edge indices must be non-negative");
        }
    }
}
