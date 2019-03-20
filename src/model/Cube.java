package model;

import transforms.Point3D;

import java.awt.*;
import java.util.Arrays;

public class Cube extends Solid {

    public Cube() {

        Point3D p0 = new Point3D(-0.5,0.5,0);
        Point3D p1 = new Point3D(0.5,0.5,0);
        Point3D p2 = new Point3D(0.5,-0.5,0);
        Point3D p3 = new Point3D(-0.5,-0.5,0);
        Point3D p4 = new Point3D(-0.5,0.5,1);
        Point3D p5 = new Point3D(0.5,0.5,1);
        Point3D p6 = new Point3D(0.5,-0.5,1);
        Point3D p7 = new Point3D(-0.5,-0.5,1);

        vertices.add(new Vertex(p0, Color.RED));
        vertices.add(new Vertex(p1, Color.GREEN));
        vertices.add(new Vertex(p2, Color.BLUE));
        vertices.add(new Vertex(p3, Color.WHITE));
        vertices.add(new Vertex(p4, Color.RED));
        vertices.add(new Vertex(p5, Color.GREEN));
        vertices.add(new Vertex(p6, Color.BLUE));
        vertices.add(new Vertex(p7, Color.WHITE));

        addTriangle(0,1,3);
        addTriangle(1,2,3);
        addTriangle(0,1,4);
        addTriangle(4,1,5);
        addTriangle(1,2,5);
        addTriangle(5,2,6);
        addTriangle(2,3,6);
        addTriangle(6,3,7);
        addTriangle(3,0,4);
        addTriangle(3,4,7);
        addTriangle(4,5,6);
        addTriangle(6,7,4);

        setTriangle();
        setCount(12);
    }

    private void addTriangle(Integer... nums) {
        indices.addAll(Arrays.asList(nums));
    }

}
