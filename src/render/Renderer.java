package render;

import model.Part;
import model.Solid;
import model.Vertex;

import java.util.List;

public class Renderer {

    RasterizerTriangle rt;

    public Renderer(Solid solid) {

        for (Part part : solid.getPartBuffer()
        ) {
            switch (part.getTopology()) {
                case Points:
                    for (int i = 0; i < part.getCount(); i++) {
                        int index = part.getStart() + i;
                        int vert = solid.getIndexBuffer().get(index);
                        Vertex a = solid.getVertexBuffer().get(vert);
                        RenderPoint(a);
                    }
                    break;
                case Lines:
                    break;
                case Triangles:
                    List<Vertex> triangle;
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart()));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart() + 1));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + part.getStart() + 2));
                        RenderTriangle(a, b, c);
                    }
                    break;
            }
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
        /*
        if (a.getPosition().getZ() < 0) {
            return;
        }
        if (b.getPosition().getZ() < 0) {
            double t;//=
            Vertex ab =b.mul(1-t).add(a.mul(t));
            rt.rasterize(a, ab, ac);
            return;
        }
        if (c.getPosition().getZ() < 0) {
            rt.rasterize(a,b,bc);
            rt.rasterize(a,ac,bc);
            return;
        }

        rt.rasterize(a,b,c);
    */
    }

    private void RenderPoint(Vertex a) {
    }
}

