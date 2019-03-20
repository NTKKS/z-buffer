package main;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Raster raster;

    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Raster.WIDTH, Raster.HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Image buffer");

        raster = new Raster();
        raster.setFocusable(true);
        raster.grabFocus();
        add(raster);

    }

    public Raster getRaster() { return raster; }

}
