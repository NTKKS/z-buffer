package model;

import transforms.Point3D;

public class Vertex {

    public Point3D getPosition() {
        return getPosition();
    }

    private double x;
    private double y;
    private double z;
    private double w;

    private int color;

    private Point3D position;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex dehomog(){
        this.mul(1/position.getW());
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Vertex(Point3D position) {
        this.position = position;
    }

    public Vertex mul (double k){
        return new Vertex(position.mul(k));
    }
    public Vertex add (Point3D k){
        return new Vertex(position.add(k));
    }
}
