package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex {

    public Point3D getPosition() {
        return position;
    }

    private double x;
    private double y;
    private double z;
    private double w;

    private Col color;
    private Vec3D normal;
    private double one;

    private Point3D position;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(double x, double y, double z, Col color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color=color;
    }

    public Vertex dehomog(){
        return this.mul(1/position.getW());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public Col getColor() {
        return color;
    }

    public void setColor(Col color) {
        this.color = color;
    }

    public Vertex(Point3D position) {
        this.position = position;
    }

    public Vec3D getNormal() {
        return normal;
    }

    public double getOne() {
        return one;
    }

    public Vertex mul (double k){
        return new Vertex(position.mul(k));
    }
    public Vertex add (Point3D k){
        return new Vertex(position.add(k));
    }
}
