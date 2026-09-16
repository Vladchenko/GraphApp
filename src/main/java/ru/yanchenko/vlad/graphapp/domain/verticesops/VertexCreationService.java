package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexTextSizer;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;

/**
 * Service responsible for creating and adding vertices to the graph.
 */
public class VertexCreationService {

	private final ScreenData screenData;
	private final VertexValidationService vertexValidationService;

	/**
	 * Creates a VertexCreationService with the specified screen data and validation service.
	 *
	 * @param screenData the screen data
	 * @param vertexValidationService the vertex validation service
	 */
	public VertexCreationService(ScreenData screenData, VertexValidationService vertexValidationService) {
		this.screenData = screenData;
		this.vertexValidationService = vertexValidationService;
	}

	/**
	 * Adds a vertex by name using GraphDomainService.
	 *
	 * @param vertexName the name of the vertex to add
	 * @param graphService the graph domain service
	 */
	public void addVertexByName(String vertexName, GraphDomainService graphService) {
		boolean canAddVertex = vertexValidationService.isVertexExist(vertexName, graphService);
		if (canAddVertex) {
			double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, vertexName);
			Vertex vertex = new Vertex(0, 0, radius, vertexName);
			graphService.addVertex(vertex);
		}
	}

	/**
	 * Adds a vertex at specified coordinates using GraphDomainService.
	 *
	 * @param vertexName the name of the vertex to add
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param graphService the graph domain service
	 */
	public void addVertexAtPosition(String vertexName, int x, int y, GraphDomainService graphService) {
		boolean canAddVertex = vertexValidationService.isVertexExist(vertexName, graphService);
		if (canAddVertex) {
			Vertex vertex = createVertexAtPosition(vertexName, x, y);
			graphService.addVertex(vertex);
		}
	}

	/**
	 * Creates a vertex at the center of the screen.
	 *
	 * @param vertexName the name of the vertex
	 * @return the created vertex
	 */
	private Vertex createVertexAtCenter(String vertexName) {
		double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, vertexName);
		return new Vertex(
				screenData.getScreenCenterPoint().x,
				screenData.getScreenCenterPoint().y,
				radius,
				vertexName
		);
	}

	/**
	 * Creates a vertex at specified coordinates.
	 *
	 * @param vertexName the name of the vertex
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the created vertex
	 */
	private Vertex createVertexAtPosition(String vertexName, int x, int y) {
		double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, vertexName);
		return new Vertex(x, y, radius, vertexName);
	}

	/**
	 * Adds a vertex at the center of the screen using GraphDomainService.
	 *
	 * @param vertexName the name of the vertex to add
	 * @param graphService the graph domain service
	 */
	public void addVertexAtCenter(String vertexName, GraphDomainService graphService) {
		boolean canAddVertex = vertexValidationService.isVertexExist(vertexName, graphService.getCurrentGraph().getVertices());
		if (canAddVertex) {
			Vertex vertex = createVertexAtCenter(vertexName);
			graphService.addVertex(vertex);
		}
	}
}