package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.*;
import ru.yanchenko.vlad.graphapp.models.PopulationKind;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.persistence.Persistable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Resolves the appropriate {@link VertexPopulationStrategy} based on the population kind.
 * <p>
 * Encapsulates the factory logic for creating population strategies,
 * keeping Dagger modules free of business logic.
 *
 * @see PopulationKind
 * @see VertexPopulationStrategy
 */
public class VertexPopulationStrategyResolver {

    private final Persistable persistable;
    private final Consumer<List<Vertex>> layoutStrategy;
    private final VertexCreationService vertexCreationService;

    /**
     * Creates a resolver with the specified dependencies.
     *
     * @param persistable the persistence service for file-based strategies
     * @param layoutStrategy the layout strategy for circular population
     * @param vertexCreationService the vertex creation service for hardcoded strategies
     */
    public VertexPopulationStrategyResolver(Persistable persistable,
                                            Consumer<List<Vertex>> layoutStrategy,
                                            VertexCreationService vertexCreationService) {
        this.persistable = persistable;
        this.layoutStrategy = layoutStrategy;
        this.vertexCreationService = vertexCreationService;
    }

    /**
     * Resolves the strategy for the given population kind.
     *
     * @param populationKind the kind of population to resolve
     * @return the appropriate strategy
     */
    public VertexPopulationStrategy resolve(PopulationKind populationKind) {
        return switch (populationKind) {
            case FIXED_FILE -> new FixedFileStrategy(persistable);
            case CIRCULAR_FILE -> new CircularFileStrategy(persistable, layoutStrategy);
            case HARDCODED_SAMPLE_A -> new HardcodedSampleAStrategy(vertexCreationService);
            case HARDCODED_SAMPLE_B -> new HardcodedSampleBStrategy(vertexCreationService, layoutStrategy);
        };
    }
}
