package ru.yanchenko.vlad.graphapp.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.yanchenko.vlad.graphapp.AppConfig;
import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexTextSizer;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JSON-based implementation of {@link Persistable} using Jackson.
 * <p>
 * Serializes and deserializes graph data (vertices and edges) to/from
 * a JSON file. Vertex names and coordinates are stored; edges are stored
 * by vertex name and resolved to indices on load.
 *
 * @see Persistable
 * @see ObjectMapper
 */
public class JsonPersistence implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(JsonPersistence.class.getName());

    // JSON field names
    private static final String FIELD_VERTICES = "vertices";
    private static final String FIELD_LINKS = "links";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_X = "x";
    private static final String FIELD_Y = "y";
    private static final String FIELD_VERTEX1 = "vertex1";
    private static final String FIELD_VERTEX2 = "vertex2";

    private final ObjectMapper objectMapper;

    /**
     * Creates a JsonPersistence with a configured ObjectMapper.
     */
    public JsonPersistence() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Loads graph data from a JSON file.
     *
     * @param graphService the graph domain service to populate
     */
    @Override
    public void loadFromFile(GraphDomainService graphService) {
        try {
            String filePath = getJsonFilePath();

            File file = new File(filePath);
            if (!file.exists()) {
                LOGGER.warning("JSON file does not exist: " + filePath);
                return;
            }

            Map<String, Object> jsonData = objectMapper.readValue(file, new TypeReference<>() {});
            graphService.clearVertices();
            graphService.clearEdges();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> verticesList = (List<Map<String, Object>>) jsonData.get(FIELD_VERTICES);
            if (verticesList != null) {
                for (Map<String, Object> vertexData : verticesList) {
                    String name = (String) vertexData.get(FIELD_NAME);
                    Integer x = (Integer) vertexData.get(FIELD_X);
                    Integer y = (Integer) vertexData.get(FIELD_Y);

                    if (name != null && x != null && y != null) {
                        double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, name);
                        graphService.addVertex(new Vertex(x, y, radius, name));
                    }
                }
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> linksList = (List<Map<String, Object>>) jsonData.get(FIELD_LINKS);
            if (linksList != null) {
                for (Map<String, Object> linkData : linksList) {
                    String vertex1Name = (String) linkData.get(FIELD_VERTEX1);
                    String vertex2Name = (String) linkData.get(FIELD_VERTEX2);

                    if (vertex1Name != null && vertex2Name != null) {
                        int vertex1Index = findVertexIndexByName(graphService.getCurrentGraph().getVertices(), vertex1Name);
                        int vertex2Index = findVertexIndexByName(graphService.getCurrentGraph().getVertices(), vertex2Name);

                        if (vertex1Index != -1 && vertex2Index != -1) {
                            graphService.addEdge(new Edge(vertex1Index, vertex2Index));
                        }
                    }
                }
            }

            LOGGER.info("JSON parsing completed successfully");

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading from JSON file", ex);
            throw new RuntimeException("Failed to load JSON file", ex);
        }
    }

    /**
     * Saves graph data to a JSON file.
     *
     * @param graphService the graph domain service to save
     */
    @Override
    public void saveToFile(GraphDomainService graphService) {
        try {
            String filePath = getJsonFilePath();

            Map<String, Object> jsonData = new HashMap<>();

            List<Map<String, Object>> verticesList = new ArrayList<>();
            for (Vertex vertex : graphService.getCurrentGraph().getVertices()) {
                if (!vertex.getVertexName().isEmpty()) {
                    Map<String, Object> vertexData = new HashMap<>();
                    vertexData.put(FIELD_NAME, vertex.getVertexName());
                    vertexData.put(FIELD_X, Math.round(vertex.getX()));
                    vertexData.put(FIELD_Y, Math.round(vertex.getY()));
                    verticesList.add(vertexData);
                }
            }
            jsonData.put(FIELD_VERTICES, verticesList);

            List<Map<String, Object>> linksList = new ArrayList<>();
            for (Edge edge : graphService.getCurrentGraph().getEdges()) {
                Map<String, Object> linkData = new HashMap<>();
                linkData.put(FIELD_VERTEX1, graphService.getCurrentGraph().getVertices().get(edge.from()).getVertexName());
                linkData.put(FIELD_VERTEX2, graphService.getCurrentGraph().getVertices().get(edge.to()).getVertexName());
                linksList.add(linkData);
            }
            jsonData.put("links", linksList);

            objectMapper.writeValue(new File(filePath), jsonData);
            LOGGER.info("JSON file saved successfully: " + filePath);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error saving to JSON file", ex);
            throw new RuntimeException("Failed to save JSON file", ex);
        }
    }

    private String getJsonFilePath() {
        String filePath = AppConfig.getPersistenceFilePath();
        if (filePath.endsWith(".xml")) {
            filePath = filePath.substring(0, filePath.length() - 4) + ".json";
        } else if (!filePath.endsWith(".json")) {
            filePath = filePath + ".json";
        }
        return filePath;
    }

    private int findVertexIndexByName(List<Vertex> vertices, String name) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getVertexName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}