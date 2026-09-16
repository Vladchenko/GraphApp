package ru.yanchenko.vlad.graphapp.models.domain;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiData;

/**
 * Represents a directed edge between two vertices in the graph.
 * <p>
 * Stored as indices into the vertices list, not as vertex references,
 * to keep the model lightweight and serializable.
 * <p>
 * When used as a domain model, both {@code from} and {@code to} are
 * non-negative indices. When used in UI state (e.g., {@link GraphUiData}),
 * a value of {@code -1} indicates "no edge selected".
 *
 * @param from source vertex index (non-negative in domain, -1 for UI sentinel)
 * @param to   target vertex index (non-negative in domain, -1 for UI sentinel)
 */
public record Edge(int from, int to) {
}
