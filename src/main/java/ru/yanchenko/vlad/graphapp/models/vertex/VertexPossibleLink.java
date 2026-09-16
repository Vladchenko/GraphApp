package ru.yanchenko.vlad.graphapp.models.vertex;

/**
 * Represents a preview of a possible link being drawn by the user.
 * <p>
 * Stores the start and end coordinates of the link preview line
 * during mouse drag operations.
 */
public class VertexPossibleLink {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Creates a VertexPossibleLink with default values (-10, -10, -10, -10).
     */
    public VertexPossibleLink() {
        this.x1 = -10;
        this.y1 = -10;
        this.x2 = -10;
        this.y2 = -10;
    }

    /**
     * Creates a VertexPossibleLink with the specified coordinates.
     *
     * @param x1 the x-coordinate of the start point
     * @param y1 the y-coordinate of the start point
     * @param x2 the x-coordinate of the end point
     * @param y2 the y-coordinate of the end point
     */
    public VertexPossibleLink(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    //region "Getters & Setters"
    /**
     * Returns the x-coordinate of the start point.
     *
     * @return the start x-coordinate
     */
    public int getX1() {
        return x1;
    }

    /**
     * Sets the x-coordinate of the start point.
     *
     * @param x1 the start x-coordinate
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * Returns the y-coordinate of the start point.
     *
     * @return the start y-coordinate
     */
    public int getY1() {
        return y1;
    }

    /**
     * Sets the y-coordinate of the start point.
     *
     * @param y1 the start y-coordinate
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * Returns the x-coordinate of the end point.
     *
     * @return the end x-coordinate
     */
    public int getX2() {
        return x2;
    }

    /**
     * Sets the x-coordinate of the end point.
     *
     * @param x2 the end x-coordinate
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * Returns the y-coordinate of the end point.
     *
     * @return the end y-coordinate
     */
    public int getY2() {
        return y2;
    }

    /**
     * Sets the y-coordinate of the end point.
     *
     * @param y2 the end y-coordinate
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }
    //endregion
}
