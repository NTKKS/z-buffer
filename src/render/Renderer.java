package render;

import imageData.DepthBuffer;
import imageData.ImageBuffer;
import imageData.TestVisibility;
import model.Part;
import model.Solid;
import model.Vertex;
import transforms.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Renderer {

    RasterizerTriangle rt;
    Solid solid;
    ImageBuffer imageBuffer;
    DepthBuffer depthBuffer;
    TestVisibility tv;
    Graphics g;

    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;

    public Renderer(RasterizerTriangle rt, Solid solid, BufferedImage img) {

        init(img);

        imageBuffer = new ImageBuffer(img);
        depthBuffer = new DepthBuffer(img.getWidth(), img.getHeight());
        g = img.getGraphics();

        for (Part part : solid.getPartBuffer()
        ) {
            switch (part.getTopology()) {
                case Points:
                    for (int i = 0; i < part.getCount(); i++) {
                        System.out.println("Got Point");
                        int index = part.getStart() + i;
                        int vert = solid.getIndexBuffer().get(index);
                        Vertex a = solid.getVertexBuffer().get(vert);
                        RenderPoint(a);
                    }
                    break;
                case Lines:
                    for (int i = 0; i < part.getCount(); i++) {
                        System.out.println("Got Line");
                        int ai = part.getStart() + i;
                        int bi = ai + 1;
                        Vertex a = solid.getVertexBuffer().get(ai);
                        Vertex b = solid.getVertexBuffer().get(bi);
                        RenderLine(a, b, a.getColor());
                    }
                    break;
                case Triangles:
                    /*
                    List<Vertex> triangle;
                    for (int i = 0; i < part.getCount(); i++) {
                        System.out.println("Got Triangle");
                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart()));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart() + 1));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart() + 2));
                        RenderTriangle(a, b, c);
                    }*/
                    break;
            }
        }
    }

    public void init(BufferedImage img){

        model = new Mat4Identity();

        Vec3D e = new Vec3D(0, -5, 4);
        Vec3D v = new Vec3D(0, 5, -4);
        Vec3D u = new Vec3D(0, 0, 1);

        view = new Mat4ViewRH(e, v, u);

        projection = new Mat4PerspRH(
                Math.PI / 4,
                img.getHeight()/ (double) img.getWidth(),
                0.1,
                200);
    }

    private Vec3D transformToWindow(Vec3D v){
        return v.mul(new Vec3D(1,-1,1)) // Y jde nahoru, chceme dolu
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed, chceme v rohu
                // máme <0, 2> -> vynásobíme polovinou velikosti plátna
                .mul(new Vec3D(imageBuffer.getWidth()/ 2f, imageBuffer.getHeight()/ 2f, 1));
    }

    private void RenderLine(Vertex a, Vertex b, int color) {
        System.out.println("_Render Line");

        int x1 = (int) a.getPosition().getX();
        int y1 = (int) a.getPosition().getY();
        int x2 = (int) b.getPosition().getX();
        int y2 = (int) b.getPosition().getY();

        float x, y, g, h;
        int dx = x2 - x1;
        int dy = y2 - y1;
        x = x1;
        y = y1;
        float k = dy / (float) dx;

        if (Math.abs(k) < 1) {
            if (dx > 0) {
                g = 1;
                h = k;
            } else {
                g = -1;
                h = -k;
            }
        } else {
            if (dy > 0) {
                g = 1 / k;
                h = 1;
            } else {
                g = -(1 / k);
                h = -1;
            }
        }

        int length;
        if (Math.abs(dx) > Math.abs(dy)) {
            length = Math.abs(dx);
        } else {
            length = Math.abs(dy);
        }

        for (int i = 0; i <= length; i++) {
            imageBuffer.setValue(Math.round(x),Math.round(y),color);
            x = x + g;
            y = y + h;
        }

    }

    private void RenderTriangle(Vertex a, Vertex b, Vertex c) {

        //tranformace MVP
        //rychle orezani

        /*
        w<ax && bx && cx
        -w>ax, bx, cx
        w<ay, by, cy
        -w>ay, by, cy
        w<az, bz, cz
        -w>az, bz, cz
        */
        //orez pro z==0

/*
        for (int y = Math.max(0, (int) (ya + 1)); y < yb; y++) { // mantra
            double s1 = (y - ya) / (yb - ya); // t
            double s2 = (y - ya) / (yc - ya); // t

            double x1 = xa * (1 - s1) + xb * s1;
            double x2 = xa * (1 - s2) + xc * s2;

            double z1 = a.getPosition().getZ() * (1 - s1) + b.getPosition().getZ() * s1;
            double z2 = a.getPosition().getZ() * (1 - s2) + b.getPosition().getZ() * s2;

            Vertex ab = a.mul(1 - s1).add(b.mul(s1));
            Vertex ac = a.mul(1 - s2).add(b.mul(s2));

            if (x1 > x2) {
                double pom = x1;
                x1 = x2;
                x2 = pom;

                pom = z1;
                z1 = z2;
                z2 = pom;

                Vertex pomV = ab;
                ab = ac;
                ac = pomV;
            }
        }


        // setrid vrcholy abc podle z, pote nastanou 4 moznosti
        if (a.getPosition().getZ() < b.getPosition().getZ()) {
            Vertex t = a;
            a = b;
            b = t;
        }
        if (b.getPosition().getZ() < c.getPosition().getZ()) {
            Vertex t = b;
            b = c;
            c = t;
        }
        if (a.getPosition().getZ() < c.getPosition().getZ()) {
            Vertex t = a;
            a = c;
            c = t;
        }

        if (a.getPosition().getZ() < 0) {
            return;
        }
        if (b.getPosition().getZ() < 0) {
            double t;//=
            Vertex ab = b.mul(1 - t).add(a.mul(t));
            rt.rasterize(a, ab, ac);
            return;
        }
        if (c.getPosition().getZ() < 0) {
            rt.rasterize(a, b, bc);
            rt.rasterize(a, ac, bc);
            return;
        }

        rt.rasterize(a, b, c);
*/
    }

    private void RenderPoint(Vertex a) {
        System.out.println("draw pixel");
        int x = (int) a.getX();
        int y = (int) a.getY();
        imageBuffer.setValue(x, y, a.getColor());

    }
}

