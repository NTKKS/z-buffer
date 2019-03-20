package main;

import javax.swing.*;

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

        JFrame descr = new JFrame("Description:");
        JOptionPane.showMessageDialog(descr,
                "W,A,S,D - movement in XY axis\n" +
                "Q,E - movement in Z axis\n" +
                "Scrolling - zoom\n" +
                "LMB - camera rotation\n" +
                "RMB - model rotation\n" +
                "R - reset view\n" +
                "O/P - Ortho/Persp\n" +
                "I - wireframe mode\n\n" +
                "Jan Janás, PGRF2, Březen 2019",
                "Description:",1);
    }

    public Raster getRaster() { return raster; }

}
