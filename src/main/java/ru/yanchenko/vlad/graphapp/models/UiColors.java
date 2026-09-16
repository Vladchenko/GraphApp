package ru.yanchenko.vlad.graphapp.models;

import java.awt.*;

/**
 * Constants for all UI colors used in the graph drawing panel.
 * <p>
 * Covers colors for vertex arcs (inner/outer), links, names,
 * selection states, and debug overlays.
 */
public class UiColors {
    public static final Color ARCS_OUTER_COLOR = new Color(150, 255, 150);
    public static final Color ARCS_INNER_COLOR = new Color(0, 0, 0);
    public static final Color CENTER_DOT_COLOR = new Color(100, 100, 100);
    public static final Color ARCS_INNER_SELECTED_COLOR = new Color(255, 255, 255);
    public static final Color ARCS_OUTER_LINKED_VERTEX_COLOR = new Color(150, 150, 255);
    public static final Color ARCS_INNER_LINKED_VERTEX_COLOR = new Color(0, 0, 0);
    public static final Color VERTEX_NAMES_COLOR = new Color(150, 150, 255);//Color(0,0,0);//
    public static final Color VERTEX_LINKS_COLOR = new Color(255, 255, 150);
    public static final Color NEW_LINK_COLOR = new Color(100, 255, 100);
    public static final Color FRAME_TIME_COLOR = new Color(200, 200, 200);
    public static final float[] VERTEX_BACKGROUND_COLORS_FRACTIONS = {0.03f, 0.95f};
    public static final Color[] VERTEX_BACKGROUND_COLORS = {new Color(0.6f, 0.6f, 0.5f, 0.90f),
            new Color(0.0f, 0.0f, 0.0f, 0.9f)};
}
