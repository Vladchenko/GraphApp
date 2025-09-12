package ru.yanchenko.vlad.graphapp.models.vertex;

import ru.yanchenko.vlad.graphapp.models.Circle;

public class Vertex extends Circle {

	private String vertexName;

	public Vertex(int x, int y, double radius, String vertexName) {
		this.vertexName = vertexName;
		setX(x);
		setY(y);
		setRadius(radius);
	}

	//region "Getters & Setters"
	public String getVertexName() {
		return vertexName;
	}

	public void setVertexName(String vertexName) {
		this.vertexName = vertexName;
	}
	//endregion
}
