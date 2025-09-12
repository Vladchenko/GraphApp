package ru.yanchenko.vlad.graphapp.models.vertex;

public class VertexLink {

    private int link1;
    private int link2;

    public VertexLink() {
        this.link1 = -1;
        this.link2 = -1;
    }

    public VertexLink(int link1, int link2) {
        this.link1 = link1;
        this.link2 = link2;
    }

    public int getLink1() {
        return link1;
    }

    public void setLink1(int link1) {
        this.link1 = link1;
    }

    public int getLink2() {
        return link2;
    }

    public void setLink2(int link2) {
        this.link2 = link2;
    }

}
