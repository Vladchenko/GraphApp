package ru.yanchenko.vlad.graphapp.domain;

/**
 * A way for vertices to be populated
 */
public enum PopulationKind {
    /**
     * Populate vertices in a way so that their names and coordinates are hardcoded
     */
    HARDCODED_SAMPLE_A,
    /**
     * Populate vertices in a way so that their names and coordinates are hardcoded
     */
    HARDCODED_SAMPLE_B,
    /**
     * Populate vertices in a circular manner and get their names and coordinates from file
     */
    CIRCULAR_FILE,
    /**
     * Populate vertices by getting their names and coordinates from file
     */
    FIXED_FILE
}
