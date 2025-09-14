package ru.yanchenko.vlad.graphapp.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.yanchenko.vlad.graphapp.AppConfig;
import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.domain.Graph;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexTextSizer;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexLink;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonPersistence implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(JsonPersistence.class.getName());
    private final ObjectMapper objectMapper;

    public JsonPersistence() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void loadFromFile(VerticesData verticesData) {
        try {
            String filePath = getJsonFilePath();

            File file = new File(filePath);
            if (!file.exists()) {
                LOGGER.warning("JSON file does not exist: " + filePath);
                return;
            }

            Map<String, Object> jsonData = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
            verticesData.getVertices().clear();
            verticesData.getVerticesLinks().clear();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> verticesList = (List<Map<String, Object>>) jsonData.get("vertices");
            if (verticesList != null) {
                for (Map<String, Object> vertexData : verticesList) {
                    String name = (String) vertexData.get("name");
                    Integer x = (Integer) vertexData.get("x");
                    Integer y = (Integer) vertexData.get("y");

                    if (name != null && x != null && y != null) {
                        double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, name);
                        verticesData.getVertices().add(new Vertex(x, y, radius, name));
                    }
                }
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> linksList = (List<Map<String, Object>>) jsonData.get("links");
            if (linksList != null) {
                for (Map<String, Object> linkData : linksList) {
                    String vertex1Name = (String) linkData.get("vertex1");
                    String vertex2Name = (String) linkData.get("vertex2");

                    if (vertex1Name != null && vertex2Name != null) {
                        int vertex1Index = findVertexIndexByName(verticesData.getVertices(), vertex1Name);
                        int vertex2Index = findVertexIndexByName(verticesData.getVertices(), vertex2Name);

                        if (vertex1Index != -1 && vertex2Index != -1) {
                            verticesData.getVerticesLinks().add(new VertexLink(vertex1Index, vertex2Index));
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

    @Override
    public void saveToFile(VerticesData verticesData) {
        try {
            String filePath = getJsonFilePath();

            Map<String, Object> jsonData = new HashMap<>();

            List<Map<String, Object>> verticesList = new ArrayList<>();
            for (Vertex vertex : verticesData.getVertices()) {
                if (!vertex.getVertexName().isEmpty()) {
                    Map<String, Object> vertexData = new HashMap<>();
                    vertexData.put("name", vertex.getVertexName());
                    vertexData.put("x", Math.round(vertex.getX()));
                    vertexData.put("y", Math.round(vertex.getY()));
                    verticesList.add(vertexData);
                }
            }
            jsonData.put("vertices", verticesList);

            List<Map<String, Object>> linksList = new ArrayList<>();
            for (VertexLink link : verticesData.getVerticesLinks()) {
                Map<String, Object> linkData = new HashMap<>();
                linkData.put("vertex1", verticesData.getVertices().get(link.getLink1()).getVertexName());
                linkData.put("vertex2", verticesData.getVertices().get(link.getLink2()).getVertexName());
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

    /**
     * Loads graph data from JSON file using GraphDomainService.
     *
     * @param graphService the graph domain service to load data into
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

            Map<String, Object> jsonData = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
            List<Vertex> vertices = new ArrayList<>();
            List<Edge> edges = new ArrayList<>();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> verticesList = (List<Map<String, Object>>) jsonData.get("vertices");
            if (verticesList != null) {
                for (Map<String, Object> vertexData : verticesList) {
                    String name = (String) vertexData.get("name");
                    Integer x = (Integer) vertexData.get("x");
                    Integer y = (Integer) vertexData.get("y");

                    if (name != null && x != null && y != null) {
                        double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, name);
                        vertices.add(new Vertex(x, y, radius, name));
                    }
                }
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> linksList = (List<Map<String, Object>>) jsonData.get("links");
            if (linksList != null) {
                for (Map<String, Object> linkData : linksList) {
                    String vertex1Name = (String) linkData.get("vertex1");
                    String vertex2Name = (String) linkData.get("vertex2");

                    if (vertex1Name != null && vertex2Name != null) {
                        int vertex1Index = findVertexIndexByName(vertices, vertex1Name);
                        int vertex2Index = findVertexIndexByName(vertices, vertex2Name);

                        if (vertex1Index != -1 && vertex2Index != -1) {
                            edges.add(new Edge(vertex1Index, vertex2Index));
                        }
                    }
                }
            }

            // Update the graph service with loaded data
            graphService.updateGraph(new Graph(vertices, edges));

            LOGGER.info("JSON parsing completed successfully");

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading from JSON file", ex);
            throw new RuntimeException("Failed to load JSON file", ex);
        }
    }

    /**
     * Saves graph data to JSON file using GraphDomainService.
     *
     * @param graphService the graph domain service to save data from
     */
    @Override
    public void saveToFile(GraphDomainService graphService) {
        try {
            String filePath = getJsonFilePath();
            Graph graph = graphService.getCurrentGraph();

            Map<String, Object> jsonData = new HashMap<>();

            List<Map<String, Object>> verticesList = new ArrayList<>();
            for (Vertex vertex : graph.getVertices()) {
                if (!vertex.getVertexName().isEmpty()) {
                    Map<String, Object> vertexData = new HashMap<>();
                    vertexData.put("name", vertex.getVertexName());
                    vertexData.put("x", Math.round(vertex.getX()));
                    vertexData.put("y", Math.round(vertex.getY()));
                    verticesList.add(vertexData);
                }
            }
            jsonData.put("vertices", verticesList);

            List<Map<String, Object>> linksList = new ArrayList<>();
            for (Edge edge : graph.getEdges()) {
                Map<String, Object> linkData = new HashMap<>();
                linkData.put("vertex1", graph.getVertices().get(edge.from()).getVertexName());
                linkData.put("vertex2", graph.getVertices().get(edge.to()).getVertexName());
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

    private int findVertexIndexByName(List<Vertex> vertices, String name) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getVertexName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}