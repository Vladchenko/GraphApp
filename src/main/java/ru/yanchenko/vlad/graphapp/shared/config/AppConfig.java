package ru.yanchenko.vlad.graphapp.shared.config;

import ru.yanchenko.vlad.graphapp.domain.PopulationKind;

import java.util.logging.Logger;

/**
 * Application configuration that can be set via system properties,
 * environment variables, or default values.
 * One can assign populationKind with app parameter, say -Dgraphapp.population.kind=HARDCODED_SAMPLE_A
 */
public class AppConfig {
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());

    // System property keys
    private static final String POPULATION_KIND_PROPERTY = "graphapp.population.kind";
    private static final String PERSISTENCE_FILE_PROPERTY = "graphapp.persistence.file";

    // Environment variable keys
    private static final String POPULATION_KIND_ENV = "GRAPHAPP_POPULATION_KIND";
    private static final String PERSISTENCE_FILE_ENV = "GRAPHAPP_PERSISTENCE_FILE";

    // Default values
    private static final PopulationKind DEFAULT_POPULATION_KIND = PopulationKind.FIXED_FILE;
    private static final String DEFAULT_PERSISTENCE_FILE = "GraphLayout.xml";

    /**
     * Gets the population kind from configuration.
     * Priority: System property > Environment variable > Default value
     */
    public static PopulationKind getPopulationKind() {
        // Try system property first
        String systemProp = System.getProperty(POPULATION_KIND_PROPERTY);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            try {
                PopulationKind kind = PopulationKind.valueOf(systemProp.toUpperCase());
                LOGGER.info("Using population kind from system property: " + kind);
                return kind;
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Invalid population kind in system property: " + systemProp +
                        ". Using default: " + DEFAULT_POPULATION_KIND);
            }
        }

        // Try environment variable
        String envVar = System.getenv(POPULATION_KIND_ENV);
        if (envVar != null && !envVar.trim().isEmpty()) {
            try {
                PopulationKind kind = PopulationKind.valueOf(envVar.toUpperCase());
                LOGGER.info("Using population kind from environment variable: " + kind);
                return kind;
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Invalid population kind in environment variable: " + envVar +
                        ". Using default: " + DEFAULT_POPULATION_KIND);
            }
        }

        // Use default
        LOGGER.info("Using default population kind: " + DEFAULT_POPULATION_KIND);
        return DEFAULT_POPULATION_KIND;
    }

    /**
     * Gets the persistence file path from configuration.
     * Priority: System property > Environment variable > Default value
     */
    public static String getPersistenceFilePath() {
        // Try system property first
        String systemProp = System.getProperty(PERSISTENCE_FILE_PROPERTY);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            LOGGER.info("Using persistence file from system property: " + systemProp);
            return systemProp;
        }

        // Try environment variable
        String envVar = System.getenv(PERSISTENCE_FILE_ENV);
        if (envVar != null && !envVar.trim().isEmpty()) {
            LOGGER.info("Using persistence file from environment variable: " + envVar);
            return envVar;
        }

        // Use default
        LOGGER.info("Using default persistence file: " + DEFAULT_PERSISTENCE_FILE);
        return DEFAULT_PERSISTENCE_FILE;
    }

    /**
     * Gets all available population kinds for reference
     */
    public static PopulationKind[] getAvailablePopulationKinds() {
        return PopulationKind.values();
    }

    /**
     * Validates if a population kind string is valid
     */
    public static boolean isValidPopulationKind(String kind) {
        if (kind == null || kind.trim().isEmpty()) {
            return false;
        }
        try {
            PopulationKind.valueOf(kind.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
