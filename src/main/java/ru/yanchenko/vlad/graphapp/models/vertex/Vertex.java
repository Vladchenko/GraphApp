package ru.yanchenko.vlad.graphapp.models.vertex;

import ru.yanchenko.vlad.graphapp.models.Circle;

/**
 * A vertex in the graph, represented as a circle with a name.
 * <p>
 * Extends {@link Circle} to inherit position and radius,
 * and adds a {@code vertexName} for identification and display.
 */
public class Vertex extends Circle {

	private String vertexName;

	/**
	 * Creates a vertex with the specified position, radius, and name.
	 *
	 * @param x the x-coordinate of the vertex center
	 * @param y the y-coordinate of the vertex center
	 * @param radius the radius of the vertex circle
	 * @param vertexName the name displayed on the vertex
	 */
	public Vertex(int x, int y, double radius, String vertexName) {
		this.vertexName = vertexName;
		setX(x);
		setY(y);
		setRadius(radius);
	}

	//region "Getters & Setters"
	/**
	 * Returns the name of this vertex.
	 *
	 * @return the vertex name
	 */
	public String getVertexName() {
		return vertexName;
	}

	/**
	 * Sets the name of this vertex.
	 *
	 * @param vertexName the new vertex name
	 */
	public void setVertexName(String vertexName) {
		this.vertexName = vertexName;
	}
	//endregion
}
