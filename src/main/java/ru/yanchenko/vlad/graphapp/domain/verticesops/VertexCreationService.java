package ru.yanchenko.vlad.graphapp.domain.verticesops;

import ru.yanchenko.vlad.graphapp.models.ScreenData;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexTextSizer;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;
import ru.yanchenko.vlad.graphapp.models.vertex.VerticesData;

/**
 * Service responsible for creating and adding vertices to the graph.
 */
public class VertexCreationService {

	private final ScreenData screenData;
	private final VertexValidationService vertexValidationService;

	public VertexCreationService(ScreenData screenData, VertexValidationService vertexValidationService) {
		this.screenData = screenData;
		this.vertexValidationService = vertexValidationService;
	}

	public void addVertexByName(String vertexName, VerticesData verticesData) {
		boolean vertexAddition = vertexValidationService.isVertexExist(vertexName, verticesData);
		if (vertexAddition) {
			double radius = VertexTextSizer.computeRadius(VertexFont.VERTICES_FONT, vertexName);
			Vertex vertex = new Vertex(0, 0, radius, vertexName);
			verticesData.getVertices().add(vertex);
		}
	}

	/**
	 * Adds a vertex at the center of the screen.
	 *
	 * @param vertexName the name of the vertex to add
	 * @param verticesData the vertices data to add the vertex to
	 */
	public void addVertexAtCenter(String vertexName, VerticesData verticesData) {
		boolean canAddVertex = vertexValidationService.isVertexExist(vertexName, verticesData);
		if (canAddVertex) {
			Vertex vertex = createVertexAtCenter(vertexName);
			verticesData.getVertices().add(vertex);
		}
	}

	/**
	 * Adds a vertex at specified coordinates.
	 *
	 * @param vertexName the name of the vertex to add
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param verticesData the vertices data to add the vertex to
	 */
	public void addVertexAtPosition(String vertexName, int x, int y, VerticesData verticesData) {
		boolean canAddVertex = vertexValidationService.isVertexExist(vertexName, verticesData);
		if (canAddVertex) {
			Vertex vertex = createVertexAtPosition(vertexName, x, y);
			verticesData.getVertices().add(vertex);
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
}