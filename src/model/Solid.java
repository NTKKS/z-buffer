package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Solid {

    public List<Integer> indices = new ArrayList<>();
    public List<Vertex> vertices = new ArrayList<>();
    private boolean triangle;
    private boolean axis;
    private boolean bicubic;
    private int count;


    public int getCount()
    {
        return count;
    }

    public boolean isAxis() { return axis; }

    public boolean isBicubic() { return bicubic; }

    public boolean isTriangle() { return triangle; }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() { return indices; }

    void setTriangle() { this.triangle = true; }

    public void setAxis(boolean axis) {
        this.axis = axis;
    }

    void setLine() {
        this.bicubic = true;
    }

    void setCount(int count) {
        this.count = count;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

}
