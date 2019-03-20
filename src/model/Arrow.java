package model;

import model.Solid;
import model.Vertex;
import transforms.Point3D;

import java.awt.*;
import java.util.Arrays;

public class Arrow extends Solid {

    public Arrow() {

        Point3D p0 = new Point3D(0,0,0);
        Point3D p1 = new Point3D(0.75,0,0.5);
        Point3D p2 = new Point3D(0,0,1);
        Point3D p3 = new Point3D(-1,0,0.25);
        Point3D p4 = new Point3D(0,0,0.25);
        Point3D p5 = new Point3D(0,0,0.75);
        Point3D p6 = new Point3D(-1,0,0.75);

        vertices.add(new Vertex(p0, Color.RED));
        vertices.add(new Vertex(p1, Color.RED));
        vertices.add(new Vertex(p2, Color.RED));
        vertices.add(new Vertex(p3, Color.BLUE));
        vertices.add(new Vertex(p4, Color.BLUE));
        vertices.add(new Vertex(p5, Color.BLUE));
        vertices.add(new Vertex(p6, Color.BLUE));

        addTriangle(0,1,2);
        addTriangle(3,4,5);
        addTriangle(3,5,6);

        setTriangle();
        setCount(3);

    }
    private void addTriangle(Integer... nums) {
        indices.addAll(Arrays.asList(nums));
    }
}