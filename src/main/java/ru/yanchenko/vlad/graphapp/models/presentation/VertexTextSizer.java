package ru.yanchenko.vlad.graphapp.models.presentation;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public final class VertexTextSizer {
	private VertexTextSizer() {}

	public static double computeRadius(Font font, String vertexName) {
		Rectangle2D rectangle = font.getStringBounds(
			vertexName,
			new FontRenderContext(
				null,
				RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
				RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT
			)
		);

		double radius = rectangle.getWidth() - vertexName.length() * 3;
		if (vertexName.length() == 1) {
			radius = rectangle.getWidth() + 5;
		}
		return radius;
	}
}