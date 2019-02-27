package model;

import java.util.List;

public  abstract class Solid {
    protected List<Vertex> vertexBuffer;
    protected List<Part> partBuffer;
    protected List<Integer> indexBuffer;

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public void setVertexBuffer(List<Vertex> vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
    }

    public List<Part> getPartBuffer() {
        return partBuffer;
    }

    public void setPartBuffer(List<Part> partBuffer) {
        this.partBuffer = partBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public void setIndexBuffer(List<Integer> indexBuffer) {
        this.indexBuffer = indexBuffer;
    }
}
