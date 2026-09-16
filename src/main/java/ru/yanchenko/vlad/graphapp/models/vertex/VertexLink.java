package ru.yanchenko.vlad.graphapp.models.vertex;

/**
 * Represents a link (edge) between two vertices by their indices.
 * <p>
 * Used as a legacy data container during link creation.
 */
public class VertexLink {

    private int link1;
    private int link2;

    /**
     * Creates a VertexLink with default values (-1, -1).
     */
    public VertexLink() {
        this.link1 = -1;
        this.link2 = -1;
    }

    /**
     * Creates a VertexLink with the specified vertex indices.
     *
     * @param link1 the index of the first vertex
     * @param link2 the index of the second vertex
     */
    public VertexLink(int link1, int link2) {
        this.link1 = link1;
        this.link2 = link2;
    }

    /**
     * Returns the index of the first vertex in this link.
     *
     * @return the first vertex index
     */
    public int getLink1() {
        return link1;
    }

    /**
     * Sets the index of the first vertex in this link.
     *
     * @param link1 the first vertex index
     */
    public void setLink1(int link1) {
        this.link1 = link1;
    }

    /**
     * Returns the index of the second vertex in this link.
     *
     * @return the second vertex index
     */
    public int getLink2() {
        return link2;
    }

    /**
     * Sets the index of the second vertex in this link.
     *
     * @param link2 the second vertex index
     */
    public void setLink2(int link2) {
        this.link2 = link2;
    }

}
