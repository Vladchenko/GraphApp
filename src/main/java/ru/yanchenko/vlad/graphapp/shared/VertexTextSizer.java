package ru.yanchenko.vlad.graphapp.shared;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * Utility for computing the optimal radius of a vertex based on its name text.
 * <p>
 * Uses {@link FontRenderContext} to measure the text bounds and calculates
 * a radius that ensures the vertex name fits inside the circle.
 *
 * @see FontRenderContext
 */
public final class VertexTextSizer {
	private VertexTextSizer() {}

	/**
	 * Computes the optimal radius for a vertex based on its name text.
	 *
	 * @param font the font used to render the vertex name
	 * @param vertexName the name of the vertex
	 * @return the computed radius value
	 */
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
