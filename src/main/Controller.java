package main;

import model.*;
import render.Renderer3D;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller {

    private Raster raster;
    private Renderer3D renderer;
    private Solid axis,arrow,cube,bicubic;
    private JCheckBox boxWireFrame, boxBicubic, boxCube, boxArrow;
    private JRadioButton rPersp, rOrtho;
    private Camera camera;
    private int mx, my;

    public Controller(Raster raster) {
        this.raster = raster;
        initListeners();
        initObjects();

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setFocusable(false);
        raster.add(panel, BorderLayout.SOUTH);
        buttons(panel);
    }

    private void initObjects() {
        renderer = new Renderer3D(raster);
        reset();

        axis = new Axis();
        arrow = new Arrow();
        cube = new Cube();
        //bicubic = new Bicubic();

        renderer.add(arrow);
        renderer.add(axis);
        renderer.draw();


    }

    private void buttons(JPanel panel) {
        rPersp = new JRadioButton("Persp");
        rPersp.setBackground(Color.LIGHT_GRAY);
        rOrtho = new JRadioButton("Ortho");
        rOrtho.setBackground(Color.LIGHT_GRAY);
        boxWireFrame = new JCheckBox("WireFrame");
        boxWireFrame.setBackground(Color.LIGHT_GRAY);
        //boxBicubic = new JCheckBox("Bicubic");
        boxCube = new JCheckBox("Cube");
        boxCube.setBackground(Color.LIGHT_GRAY);
        boxArrow = new JCheckBox("Arrow");
        boxArrow.setBackground(Color.LIGHT_GRAY);


        ButtonGroup group = new ButtonGroup();
        group.add(rPersp);
        group.add(rOrtho);

        rPersp.setSelected(true);
        rPersp.setFocusable(false);
        rOrtho.setFocusable(false);
        boxWireFrame.setFocusable(false);
        boxCube.setFocusable(false);
        boxCube.setSelected(false);
        //boxBicubic.setFocusable(false);
        //boxBicubic.setSelected(false);
        boxArrow.setFocusable(false);
        boxArrow.setSelected(true);

        panel.add(rPersp);
        panel.add(rOrtho);
        panel.add(boxWireFrame);
        //panel.add(boxBicubic);
        panel.add(boxCube);
        panel.add(boxArrow);

        rPersp.addActionListener(e -> persp());
        rOrtho.addActionListener(e -> orth());
        boxWireFrame.addActionListener(e -> setWireFrame());

        boxArrow.addActionListener(e -> addArrow());
        //boxBicubic.addActionListener(e -> addBicubic());
        boxCube.addActionListener(e -> addCube());

    }

    private void reset() {
        camera = new Camera(new Vec3D(0, -5, 4), Math.toRadians(90), Math.toRadians(-40), 1, true);
        renderer.setView(camera.getViewMatrix());
    }

    private void addCube() {
        if (boxCube.isSelected()) renderer.add(cube);
        else renderer.remove(cube);
        renderer.draw();
    }

    private void addBicubic() {
        if (boxBicubic.isSelected()) renderer.add(bicubic);
        else renderer.remove(bicubic);
        renderer.draw();
    }

    private void addArrow() {
        if (boxArrow.isSelected()) renderer.add(arrow);
        else renderer.remove(arrow);
        renderer.draw();
    }

    //pohyb

    private void down() {
        camera = camera.up(0.5);
        renderer.setView(camera.getViewMatrix());
    }

    private void up() {
        camera = camera.down(0.5);
        renderer.setView(camera.getViewMatrix());
    }

    private void right() {
        camera = camera.right(0.5);
        renderer.setView(camera.getViewMatrix());
    }

    private void left() {
        camera = camera.left(0.5);
        renderer.setView(camera.getViewMatrix());
    }

    private void forward() {
        camera = camera.forward(0.5);
        renderer.setView(camera.getViewMatrix());
    }

    private void backward() {
        camera = camera.forward(-0.5);
        renderer.setView(camera.getViewMatrix());
    }

    private void persp() {
        Mat4PerspRH projection = new Mat4PerspRH(Math.PI / 4, Raster.HEIGHT / (float) Raster.WIDTH, 1, 200);
        renderer.setProjection(projection);
        reset();
    }

    private void orth() {
        Mat4OrthoRH projection = new Mat4OrthoRH(Raster.WIDTH / 100.0, Raster.HEIGHT / 100.0, 0.1, 200);
        renderer.setProjection(projection);
        reset();
    }


    private void zoom(int index) {
        if (index > 0) {
            Mat4 zoom = renderer.getModel().mul(new Mat4Scale(1.05, 1.05, 1.05));
            renderer.setModel(zoom);
        } else {
            Mat4 zoom = renderer.getModel().mul(new Mat4Scale(0.95, 0.95, 0.95));
            renderer.setModel(zoom);
        }
    }

    private void setWireFrame() {
        renderer.setWireFrame();
    }


    private void initListeners() {

        raster.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
            }
        });
        raster.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //rozhlizeni se (pohled kamery)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    double diffX = (mx - e.getX()) / 500.0;
                    double diffY = (my - e.getY()) / 500.0;
                    double azimut = camera.getAzimuth() + diffX;
                    double zenith = camera.getZenith() + diffY;
                    if (zenith > Math.PI / -2 && zenith < 0 && azimut > 0.3 && azimut < 3) {
                        camera = camera.withAzimuth(azimut);
                        camera = camera.withZenith(zenith);
                        renderer.setView(camera.getViewMatrix());
                    }
                //rotace modelu
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    double rotX = (mx - e.getX()) / -200.0;
                    double rotY = (my - e.getY()) / -200.0;
                    Mat4 rot = renderer.getModel().mul(new Mat4RotXYZ(rotY, 0, rotX));
                    renderer.setModel(rot);
                }
                mx = e.getX();
                my = e.getY();
            }
        });
        //zoom
        raster.addMouseWheelListener(e -> {
            if (e.getUnitsToScroll() > 0)  zoom(1);
            else zoom(-1);
        });

        raster.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    //pohyb
                    case KeyEvent.VK_Q:
                        up();
                        break;
                    case KeyEvent.VK_E:
                        down();
                        break;
                    case KeyEvent.VK_D:
                        right();
                        break;
                    case KeyEvent.VK_A:
                        left();
                        break;
                    case KeyEvent.VK_W:
                        forward();
                        break;
                    case KeyEvent.VK_S:
                        backward();
                        break;
                    //nastaveni rezimu zobrazeni
                    case KeyEvent.VK_R:
                        reset();
                        break;
                    case KeyEvent.VK_P:
                        persp();
                        rPersp.setSelected(true);
                        break;
                    case KeyEvent.VK_O:
                        orth();
                        rOrtho.setSelected(true);
                        break;
                    case KeyEvent.VK_I:
                        setWireFrame();
                        boxWireFrame.setSelected(!boxWireFrame.isSelected());
                        break;
                }
            }
        });

    }
}
