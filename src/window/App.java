package window;

import imageData.TestVisibility;
import model.Solid;
import model.Vertex;
import objs.Arrow;
import render.RasterizerTriangle;
import render.Renderer;
import render.Shader;
import transforms.Col;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class App {

    private JFrame frame;
    private JPanel panel;
    private BufferedImage img;

    public App(int width, int height) {

        frame = new JFrame();
        frame.setTitle("Buffer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(width,height);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        frame.setVisible(true);

    }

    public void start(){
        Solid s = new Arrow();
        TestVisibility tv = new TestVisibility(img);
        Shader shader = new Shader() {
            @Override
            public Col shade(Vertex vertex) {
                return new Col(0xffffff);
            }
        };

        RasterizerTriangle rt;// = new RasterizerTriangle(tv, shader);

        rt = new RasterizerTriangle(tv,
                (Vertex vertex) -> {return new Col(0xff);});

        rt = new RasterizerTriangle(tv,
                (vertex) -> {return vertex.getColor()
                        .mul(1/vertex.getOne());});

        Renderer renderer = new Renderer(rt);
    }

    public static void main(String[] args) {
        App window = new App(800,600);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
