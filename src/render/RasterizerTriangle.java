package render;

import imageData.TestVisibility;
import model.Vertex;
import transforms.Col;

import java.util.function.Function;

public class RasterizerTriangle {
    private TestVisibility tv;
    private Function<Vertex, Col> shader;

    public RasterizerTriangle(TestVisibility tv,
                              Function<Vertex, Col> shader) {
        this.tv = tv;
        this.shader = shader;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {
        a = a.dehomog();
        b = b.dehomog();
        c = c.dehomog();

        double xa = ((a.getPosition().getX()+1)/2*(tv.getWidth()-1));
        double ya,xb,yb;

        //usporada podle y tak ze ay<by<cy
        for (int y = Math.max(0,(int)(ya+1)); y < yb; y++) {
            double s1 = (y-ya)/(yb-ya);
            double x1 = xa*(1-s1)+xb*s2;
            double z1,z2;

            for (int x = x1; x < x2; x++) {
                double t = (x-x1)/(x2-x1);
                double z =z1*(1-t)+z2*t;
                Vertex abc;
                tv.putPixel(x,y,z,shader.apply(abc));
            }
        }
    }

}
