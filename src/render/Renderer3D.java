package render;


import model.Solid;
import model.Vertex;
import transforms.*;
import main.Raster;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Renderer3D {
    private final Raster raster;
    private List<Solid> solids = new ArrayList<>();
    private Mat4 model, view, projection;
    private boolean wireframe = false;
    private double[][] zBuffer;

    public Renderer3D(Raster raster) {
        this.raster = raster;
        this.model = new Mat4Identity();
        this.view = new Mat4Identity();
        this.projection = new Mat4PerspRH(Math.PI / 4, Raster.HEIGHT / (float) Raster.WIDTH, 1, 200);
        this.zBuffer = new double[Raster.WIDTH][Raster.HEIGHT];
    }


    private void clear() {
        raster.clear();
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[i].length; j++) {
                zBuffer[i][j] = 100;
            }
        }
    }

    public void remove(Solid object) {
        if (solids.contains(object)) {
            solids.remove(object);
            draw();
        }
    }


    public void add(Solid... solids) {
        this.solids.addAll(Arrays.asList(solids));
    }

    //transform to window
    private void transDraw(Color color, Vec3D vec1, Vec3D vec2) {
        vec1 = transformToWindow(vec1);
        vec2 = transformToWindow(vec2);
        if (vec1.getY() > vec2.getY()) {
            Vec3D q = vec1; vec1 = vec2; vec2 = q;
        }
        drawLine(vec1, vec2, color);
    }


    public void draw() {
        clear();
        for (Solid solid : solids) {

            if (solid.isTriangle()) {
                for (int i = 0; i < solid.getCount() * 3; i += 3) {
                    Vertex ver1 = solid.getVertices().get(solid.getIndices().get(i));
                    Vertex ver2 = solid.getVertices().get(solid.getIndices().get(i + 1));
                    Vertex ver3 = solid.getVertices().get(solid.getIndices().get(i + 2));
                    setTriangle(ver1, ver2, ver3);
                }

            } else if (solid.isBicubic()) {
                for (int i = 0; i < solid.getCount(); i += 2) {
                    Vertex ver1 = solid.getVertices().get(solid.getIndices().get(i));
                    Vertex ver2 = solid.getVertices().get(solid.getIndices().get(i + 1));
                    setBicubic(ver1, ver2);
                }

            } else if (solid.isAxis()) {
                for (int i = 0; i < solid.getCount(); i += 2) {
                    Vertex a = solid.getVertices().get(solid.getIndices().get(i));
                    Vertex b = solid.getVertices().get(solid.getIndices().get(i + 1));
                    setAxis(a, b);
                }
            }
        }
    }

    private void setAxis(Vertex ver1, Vertex ver2) {
        Color color = ver1.color;
        Point3D point1 = ver1.vertex;
        Point3D point2 = ver2.vertex;
        point1 = point1.mul(this.view).mul(this.projection);
        point2 = point2.mul(this.view).mul(this.projection);
        Vec3D vec1 = point1.dehomog().get();
        Vec3D vec2 = point2.dehomog().get();
        transDraw(color, vec1, vec2);

    }

    private void setBicubic(Vertex ver1, Vertex ver2) {
        Color color = ver1.color;
        ver1 = ver1.transform(this.model, this.view, this.projection);
        ver2 = ver2.transform(this.model, this.view, this.projection);
        if (ver1.x > ver1.w && ver2.x > ver2.w) return;
        if (ver1.x < -ver1.w && ver2.x < -ver2.w) return;
        if (ver1.y > ver1.w && ver2.y > ver2.w) return;
        if (ver1.y < -ver1.w && ver2.y < -ver2.w) return;
        if (ver1.z > ver1.w && ver2.z > ver2.w) return;
        if (ver1.z < ver2.z) { Vertex q = ver1; ver1 = ver2; ver2 = q; }
        if (ver1.z < 0 && ver2.z < 0) return;
        Optional<Vec3D> opt1 = ver1.vertex.dehomog();
        Optional<Vec3D> opt2 = ver2.vertex.dehomog();
        if (opt1.isPresent() && opt2.isPresent()) {
            Vec3D vec1 = opt1.get();
            Vec3D vec2 = opt2.get();
            transDraw(color, vec1, vec2);

        }
    }

    private void setTriangle(Vertex ver1, Vertex ver2, Vertex ver3) {
        //MVP transforms
        ver1 = ver1.transform(this.model, this.view, this.projection);
        ver2 = ver2.transform(this.model, this.view, this.projection);
        ver3 = ver3.transform(this.model, this.view, this.projection);
        //usporadani
        if (ver1.x > ver1.w && ver2.x > ver2.w && ver3.x > ver3.w) return;
        if (ver1.x < -ver1.w && ver2.x < -ver2.w && ver3.x < -ver3.w) return;
        if (ver1.y > ver1.w && ver2.y > ver2.w && ver3.y > ver3.w) return;
        if (ver1.y < -ver1.w && ver2.y < -ver2.w && ver3.y < -ver3.w) return;
        if (ver1.z > ver1.w && ver2.z > ver2.w && ver3.z > ver3.w) return;
        if (ver1.z < 0 && ver2.z < 0 && ver3.z < 0) return;
        if (ver1.z < ver2.z) {
            Vertex q = ver1; ver1 = ver2; ver2 = q;
        }
        if (ver2.z < ver3.z) {
            Vertex q = ver2; ver2 = ver3; ver3 = q;
        }
        if (ver1.z < ver2.z) {
            Vertex q = ver1; ver1 = ver2; ver2 = q;
        }
        //rychly orez;
        if (ver1.z < 0) return;
        if (ver2.z < 0) {
            Vertex v12 = trim(ver1, ver2);
            Vertex v13 = trim(ver1, ver3);
            drawTriangle(ver1, v12, v13);
        } else if (ver3.z < 0) {
            Vertex v23 = trim(ver2, ver3);
            drawTriangle(ver1, ver2, v23);
            Vertex v13 = trim(ver1, ver3);
            drawTriangle(ver1, v23, v13);
        } else {
            drawTriangle(ver1, ver2, ver3);
        }
    }


    private void drawLine(Vec3D vec1, Vec3D vec2, Color c) {
        if (vec1.getX() > vec2.getY()) {
            Vec3D q = vec1; vec1 = vec2; vec2 = q;
        }
        Vec3D vec = vec2.sub(vec1);
        Optional<Vec3D> vecOpt = vec.normalized();
        if (vecOpt.isPresent()) {
            Vec3D ve = vecOpt.get();
            Vec3D v = vec1;

            double max;
            if (ve.getY() == 0) max = vec.getX() / ve.getX();
            else max = vec.getY() / ve.getY();

            for (int i = 0; i <= max; i++) {
                if (v.getY() >= 0 && v.getY() < Raster.HEIGHT && v.getX() >= 0 && v.getX() < Raster.WIDTH){
                    drawPixel((int) v.getX(), (int) v.getY(), v.getZ(), c);
                }
                v = v.add(ve);
            }
        }
    }

    private void drawTriangle(Vertex ver1, Vertex ver2, Vertex c) {
        Color c1 = ver1.color;
        Color c2 = ver2.color;
        Color c3 = c.color;

        Optional<Vec3D> o1 = ver1.vertex.dehomog();
        Optional<Vec3D> o2 = ver2.vertex.dehomog();
        Optional<Vec3D> o3 = c.vertex.dehomog();

        if (!o1.isPresent() || !o2.isPresent() || !o3.isPresent()) return;

        Vec3D v1 = o1.get();
        Vec3D v2 = o2.get();
        Vec3D v3 = o3.get();

        v1 = transformToWindow(v1);
        v2 = transformToWindow(v2);
        v3 = transformToWindow(v3);

        if (v1.getY() > v2.getY()) {
            Vec3D q = v1; v1 = v2; v2 = q;
            Color p = c1; c1 = c2; c2 = p;
        }

        if (v2.getY() > v3.getY()) {
            Vec3D q = v2; v2 = v3; v3 = q;
            Color p = c2; c2 = c3; c3 = p;
        }

        if (v1.getY() > v2.getY()) {
            Vec3D q = v1; v1 = v2; v2 = q;
            Color p = c1; c1 = c2; c2 = p;
        }
        if (wireframe) {
            drawTriangle(v1, v2, v3, c1);
        } else {
            for (int y = (int) Math.max(v1.getY() + 1, 0); y <= Math.min(v2.getY(), Raster.HEIGHT - 1); y++) {
                double num1 = (y - v1.getY()) / (v2.getY() - v1.getY());
                double num2 = (y - v1.getY()) / (v3.getY() - v1.getY());
                double x1 = (1 - num1) * v1.getX() + num1 * v2.getX();
                double x2 = (1 - num2) * v1.getX() + num2 * v3.getX();
                double z1 = (1 - num1) * v1.getZ() + num1 * v2.getZ();
                double z2 = (1 - num2) * v1.getZ() + num2 * v3.getZ();

                Color color1 = colorInterpolation(c1, c2, num1);
                Color color2 = colorInterpolation(c1, c3, num2);
                fill(y, x1, x2, z1, z2, color1, color2);
            }

            for (int y = (int) Math.max(0, v2.getY() + 1); y <= Math.min(v3.getY(), Raster.HEIGHT - 1); y++) {
                double num1 = (y - v3.getY()) / (v2.getY() - v3.getY());
                double num2 = (y - v3.getY()) / (v1.getY() - v3.getY());
                double x1 = num1 * v2.getX() + (1 - num1) * v3.getX();
                double x2 = (1 - num2) * v3.getX() + num2 * v1.getX();
                double z1 = (1 - num1) * v3.getZ() + num1 * v2.getZ();
                double z2 = (1 - num2) * v3.getZ() + num2 * v1.getZ();

                Color color1 = colorInterpolation(c3, c2, num1);
                Color color2 = colorInterpolation(c3, c1, num2);
                fill(y, x1, x2, z1, z2, color1, color2);
            }
        }
    }

    private void drawTriangle(Vec3D v1, Vec3D v2, Vec3D v3, Color c) {
        drawLine(v1, v2, c);
        drawLine(v2, v3, c);
        drawLine(v1, v3, c);
    }

    private Vertex trim(Vertex v1, Vertex v2) {
        double q = v1.z / (v1.z - v2.z);
        return new Vertex(v1.vertex.mul(1 - q).add(v2.vertex.mul(q)), v1.color);
    }

    private void fill(int y, double x1, double x2, double z1, double z2, Color c1, Color c2) {
        if (x1 > x2) {
            double p = x1; x1 = x2; x2 = p;
            double q = z1; z1 = z2; z2 = q;
            Color c = c1; c1 = c2; c2 = c;
        }
        for (int x = (int) Math.max(x1 + 1, 0); x <= Math.min(x2, Raster.WIDTH - 1); x++) {
            double g = (x - x1) / (x2 - x1);
            double z = (1 - g) * z1 + g * z2;
            drawPixel(x, y, z, colorInterpolation(c1, c2, g));
        }
    }

    private Color colorInterpolation(Color c1, Color c2, double g) {
        return new Color(
                Math.max(Math.min((int) (c1.getRed() * (1 - g) + c2.getRed() * g), 255), 0),
                Math.max(Math.min((int) (c1.getGreen() * (1 - g) + c2.getGreen() * g), 255), 0),
                Math.max(Math.min((int) (c1.getBlue() * (1 - g) + c2.getBlue() * g), 255), 0)
        );
    }

    private Vec3D transformToWindow(Vec3D v) {
        return v.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D(Raster.WIDTH / 2f, Raster.HEIGHT / 2f, 1)
                );
    }

    private void drawPixel(int x, int y, double z, Color color) {
        if (z < this.zBuffer[x][y]) {
            this.zBuffer[x][y] = z;
            this.raster.drawPixel(x, y, color.getRGB());
        }
    }

    public void setWireFrame() {
        this.wireframe = !wireframe;
        draw();
    }

    public void setView(Mat4 view) {
        this.view = view;
        draw();
    }

    public Mat4 getModel() {
        return this.model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
        draw();
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
        draw();
    }

    public List<Solid> getSolids() {
        return solids;
    }
}

