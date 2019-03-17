package render;

import imageData.DepthBuffer;
import imageData.ImageBuffer;
import imageData.TestVisibility;
import model.Part;
import model.Solid;
import model.Vertex;

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

    public Renderer(RasterizerTriangle rt, Solid solid, BufferedImage img) {

        imageBuffer = new ImageBuffer(img);
        depthBuffer = new DepthBuffer(800, 600);
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
                        RenderLine(a, b);
                    }
                    break;
                case Triangles:
                    List<Vertex> triangle;
                    for (int i = 0; i < part.getCount(); i++) {
                        System.out.println("Got Triangle");
                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart()));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart() + 1));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart() + 2));
                        RenderTriangle(a, b, c);
                    }
                    break;
            }
        }
    }

    private void RenderLine(Vertex a, Vertex b) {
        System.out.println("_Render Line");
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

    }

    private void RenderPoint(Vertex a) {
        System.out.println("draw pixel");
        int x = (int) a.getX();
        int y = (int) a.getY();
        imageBuffer.setValue(x, y, a.getColor().getRGB());

    }
}

