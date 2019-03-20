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
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Controller {

    private Raster raster;
    private Renderer3D renderer;
    private Solid axis,arrow,cube;
    private JCheckBox boxRotX, boxRotY, boxRotZ, boxWireFrame, boxBicubic, boxCube, boxArrow;
    private boolean isLoopActivated = false;
    private int start = 0;
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

        renderer.add(arrow);
        renderer.add(axis);
        /*
        bicubic = new Bicubics();
        renderer.add(bicubic);
        */
        renderer.draw();


    }

    private void buttons(JPanel panel) {
        rPersp = new JRadioButton("Persp");
        rOrtho = new JRadioButton("Ortho");
        boxRotX = new JCheckBox("Rot X");
        boxRotY = new JCheckBox("Rot Y");
        boxRotZ = new JCheckBox("Rot Z");
        boxWireFrame = new JCheckBox("WireFrame");
        boxBicubic = new JCheckBox("Bic");
        boxCube = new JCheckBox("Cube");
        boxArrow = new JCheckBox("Arrow");


        rPersp.setToolTipText("Perspektivní projekce");
        rOrtho.setToolTipText("Orthogonální projekce");
        boxRotX.setToolTipText("Rotovat kolem osy X");
        boxRotY.setToolTipText("Rotovat kolem osy Y");
        boxRotZ.setToolTipText("Rotovat kolem osy Z");
        boxWireFrame.setToolTipText("Zapnout drátěný model");
        boxBicubic.setToolTipText("Bikubická plocha");
        boxCube.setToolTipText("Krychle");
        boxArrow.setToolTipText("Šipka");

        ButtonGroup group = new ButtonGroup();
        group.add(rPersp);
        group.add(rOrtho);

        rPersp.setSelected(true);
        rPersp.setFocusable(false);
        rOrtho.setFocusable(false);
        boxRotX.setFocusable(false);
        boxRotY.setFocusable(false);
        boxRotZ.setFocusable(false);
        boxWireFrame.setFocusable(false);
        boxCube.setFocusable(false);
        boxCube.setSelected(false);
        boxBicubic.setFocusable(false);
        boxBicubic.setSelected(false);
        boxArrow.setFocusable(false);
        boxArrow.setSelected(true);

        panel.add(rPersp);
        panel.add(rOrtho);
        panel.add(boxWireFrame);
        panel.add(boxRotX);
        panel.add(boxRotY);
        panel.add(boxRotZ);
        panel.add(boxBicubic);
        panel.add(boxCube);
        panel.add(boxArrow);

        rPersp.addActionListener(e -> persp());
        rOrtho.addActionListener(e -> orth());
        boxWireFrame.addActionListener(e -> setWireFrame());

        boxArrow.addActionListener(e -> addArrow());
        //boxBicubic.addActionListener(e -> addBicubic());
        boxCube.addActionListener(e -> addCube());

    }

    private void loop() {
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (isLoopActivated) {
                    rotate();
                }
            }
        }, 1, 100);
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
    /*
    private void addBicubic() {
        if (boxBicubic.isSelected()) renderer.add(bicubic);
        else renderer.remove(bicubic);
        renderer.draw();
    }
    */
    private void addArrow() {
        if (boxArrow.isSelected()) renderer.add(arrow);
        else renderer.remove(arrow);
        renderer.draw();
    }

    private void rotate() {
        if (boxRotX.isSelected()) rotateObject(0);
        if (boxRotY.isSelected()) rotateObject(1);
        if (boxRotZ.isSelected()) rotateObject(2);
    }


    private void rotateObject(int index) {
        for (Solid solid : renderer.getSolids()) {
            if (!solid.isAxis()) {
                List<Vertex> vertices = new ArrayList<>();
                for (int i = 0; i < solid.getVertices().size(); i++) {
                    Vertex ver = solid.getVertices().get(i);

                    if (index == 0) vertices.add(new Vertex(ver.vertex.mul(new Mat4RotX(Math.PI/15)),ver.color));
                    if (index == 1) vertices.add(new Vertex(ver.vertex.mul(new Mat4RotY(Math.PI/15)),ver.color));
                    if (index == 2) vertices.add(new Vertex(ver.vertex.mul(new Mat4RotZ(Math.PI/15)),ver.color));
                }
                solid.setVertices(vertices);
            }
        }
        renderer.draw();
    }

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
                if (SwingUtilities.isLeftMouseButton(e)) {
                    double diffX = (mx - e.getX()) / -500.0;
                    double diffY = (my - e.getY()) / -500.0;
                    double azimut = camera.getAzimuth() + diffX;
                    double zenith = camera.getZenith() + diffY;
                    if (zenith > Math.PI / -2 && zenith < 0 && azimut > 0.3 && azimut < 3) {
                        camera = camera.withAzimuth(azimut);
                        camera = camera.withZenith(zenith);
                        renderer.setView(camera.getViewMatrix());
                    }

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

        raster.addMouseWheelListener(e -> {
            if (e.getUnitsToScroll() > 0)  zoom(1);
            else zoom(-1);
        });

        raster.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
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
                    case KeyEvent.VK_R:
                        reset();
                        break;
                    case KeyEvent.VK_F:
                        rotate();
                        break;
                    case KeyEvent.VK_P:
                        persp();
                        rPersp.setSelected(true);
                        break;
                    case KeyEvent.VK_O:
                        orth();
                        rOrtho.setSelected(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        setWireFrame();
                        boxWireFrame.setSelected(!boxWireFrame.isSelected());
                        break;
                    case KeyEvent.VK_ENTER:
                        isLoopActivated = !isLoopActivated;
                        if (isLoopActivated && start == 0) {
                            loop();
                            start++;
                        }
                        break;
                }
            }
        });

    }

    public boolean isLoopActivated() {
        return isLoopActivated;
    }

    public void setLoopActivated(boolean loopActivated) {
        isLoopActivated = loopActivated;
    }
}
