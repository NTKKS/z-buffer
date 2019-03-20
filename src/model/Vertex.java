package model;

import transforms.Mat4;
import transforms.Point3D;

import java.awt.*;

public class Vertex {

    public final Point3D vertex;
    public final double x, y, z, w;
    public Color color;

    public Vertex(Point3D vertex, Color color) {
        this.vertex = vertex;
        this.color = color;
        x = vertex.getX();
        y = vertex.getY();
        z = vertex.getZ();
        w = vertex.getW();
    }

    public Vertex transform(Mat4 model, Mat4 view, Mat4 projection) {
        return new Vertex(vertex.mul(model).mul(view).mul(projection), color);
    }
}
