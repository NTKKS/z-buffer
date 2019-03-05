package window;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame frame;
    private JPanel panel;
    private BufferedImage img;

    public Window(int width, int height) {

        frame = new JFrame();
        frame.setTitle("Buffer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(width,height);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        Window window = new Window(800,600);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
