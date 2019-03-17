package window;

import imageData.ImageBuffer;
import imageData.TestVisibility;
import model.Solid;
import model.Vertex;
import objs.Arrow;
import render.RasterizerTriangle;
import render.Renderer;
import render.Shader;
import transforms.Col;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class App extends JFrame{

    private JFrame frame;
    private JPanel panel;
    private BufferedImage img;
    private ImageBuffer imageBuffer;

    public App(int width, int height) {

        setTitle("Buffer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width,height);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        setVisible(true);

        start(width,height);

    }

    public void start(int width, int height){

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageBuffer = new ImageBuffer(img);
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

        Renderer renderer = new Renderer(rt,s,img);

    }

    public void paint(Graphics g){
        g.drawImage(img,0,0,null);
    }

    public static void main(String[] args) {
        App app = new App(800,600);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
