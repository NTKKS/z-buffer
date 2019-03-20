package model;

import transforms.Point3D;

import java.awt.*;

public class Axis extends Solid {

    public Axis() {
        vertices.add(new Vertex(new Point3D(3, 0, 0), Color.RED));
        vertices.add(new Vertex(new Point3D(0, 3, 0), Color.GREEN));
        vertices.add(new Vertex(new Point3D(0, 0, 3), Color.BLUE));
        vertices.add(new Vertex(new Point3D(0, 0, 0), Color.WHITE));
        indices.add(0);
        indices.add(3);
        indices.add(1);
        indices.add(3);
        indices.add(2);
        indices.add(3);

        setAxis(true);
        setCount(6);
    }
}
