package model;

import transforms.Point3D;

import java.awt.*;
import java.util.Arrays;

public class Pyramid extends Solid {

    public Pyramid() {

        Point3D p0 = new Point3D(-0.75, 0.75, 0.2);
        Point3D p1 = new Point3D(0.75, 0.75, 0.2);
        Point3D p2 = new Point3D(0.75, -0.75, 0.2);
        Point3D p3 = new Point3D(-0.75, -0.75, 0.2);
        Point3D p4 = new Point3D(0, 0, 2);

        vertices.add(new Vertex(p0, Color.RED));
        vertices.add(new Vertex(p1, Color.RED));
        vertices.add(new Vertex(p2, Color.RED));
        vertices.add(new Vertex(p3, Color.RED));
        vertices.add(new Vertex(p4, Color.WHITE));

        addTriangle(0,1,2);
        addTriangle(0,2,3);
        addTriangle(0,1,4);
        addTriangle(1,2,4);
        addTriangle(2,3,4);
        addTriangle(0,3,4);

        setTriangle();
        setCount(6);

        }
    private void addTriangle(Integer... nums) {
        indices.addAll(Arrays.asList(nums));}
}
