package model;

import transforms.*;

public class Vertex {

    public Point3D getPosition() {
        return position;
    }

    private double x;
    private double y;
    private double z;
    private double w;

    private Integer color;
    private Vec3D normal;
    private double one;

    private Point3D position;

    public Vertex(double x, double y, double z, Integer color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color=color;
        this.position = new Point3D(x,y,z);
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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
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

    public boolean isPresent() {
        return position.dehomog().isPresent();
    }

    public Vec3D get() {
        return position.dehomog().get();
    }

    public Vertex mul(Mat4 mat) {
        return new Vertex(position.mul(mat));
    }

}
