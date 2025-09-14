package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.VertexPopulationStrategy;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiStateService;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexPolarCoordinate;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesDataFacade;

public class VertexPopulationService {

    private final VertexPopulationStrategy strategy;

    public VertexPopulationService(VertexPopulationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Populates vertices using the new service architecture.
     *
     * @param graphService the graph domain service
     * @param uiStateService the UI state service
     * @param uiState the UI state
     */
    public void populateVertices(GraphDomainService graphService, 
                                GraphUiStateService uiStateService, 
                                GraphUiState uiState) {
        
        // DIRECT APPROACH: Skip the facade and strategy pattern, use services directly
        if (strategy instanceof ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.FixedFileStrategy) {
            ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.FixedFileStrategy fixedFileStrategy = 
                (ru.yanchenko.vlad.graphapp.domain.verticesops.strategies.FixedFileStrategy) strategy;
            
            try {
                // Get the persistable from the strategy and call the new method
                java.lang.reflect.Field field = fixedFileStrategy.getClass().getDeclaredField("persistable");
                field.setAccessible(true);
                ru.yanchenko.vlad.graphapp.persistence.Persistable persistable = 
                    (ru.yanchenko.vlad.graphapp.persistence.Persistable) field.get(fixedFileStrategy);
                
                // Call the new service-based persistence method directly
                persistable.loadFromFile(graphService);
                uiStateService.updatePolarCoordinates(graphService.getCurrentGraph().getVertices());
                
                
            } catch (Exception ex) {
                System.err.println("Error in direct service-based loading: " + ex.getMessage());
                ex.printStackTrace();
                
                // Fallback to facade approach
                VerticesDataFacade facade = new VerticesDataFacade(graphService, uiStateService);
                strategy.populate(facade, uiState);
            }
        } else {
            // For other strategies, use the facade approach
            VerticesDataFacade facade = new VerticesDataFacade(graphService, uiStateService);
            strategy.populate(facade, uiState);
        }
        
        uiState.setAddingVertex(false);
    }

    /**
     * Populates vertices using the legacy VerticesData approach.
     *
     * @param verticesData the vertices data
     * @param uiState the UI state
     * @deprecated Use populateVertices(GraphDomainService, GraphUiStateService, GraphUiState) instead
     */
    @Deprecated
    public void populateVertices(VerticesData verticesData, GraphUiState uiState) {
        strategy.populate(verticesData, uiState);

        for (int i = 0; i < verticesData.getVertices().size(); i++) {
            verticesData.getVerticesPolarCoordinates().add(new VertexPolarCoordinate());
        }
    }
}