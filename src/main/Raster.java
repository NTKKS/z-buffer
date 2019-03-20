package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Raster extends JPanel {

    public static final int WIDTH = 800, HEIGHT = 600;
    private static final int FPS = 100 / 30;
    private final BufferedImage bi;
    private final Graphics g;

    Raster() {
        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = bi.getGraphics();
        setLoop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bi, 0, 0, null);
    }

    private void setLoop() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public void clear() {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void drawPixel(int x, int y, int color) {
        if (x >= 0 && x <= WIDTH - 1 && y >= 0 && y <= HEIGHT - 1)
            bi.setRGB(x, y, color);
    }
}
