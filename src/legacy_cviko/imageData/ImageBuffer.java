package legacy_cviko.imageData;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageBuffer implements Image<Integer> {
    private Integer value;
    private BufferedImage img;

    public ImageBuffer(BufferedImage img) {
        this.img = img;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    @Override
    public void setValue(int x, int y, Integer value) {
        img.setRGB(x, y, value);
    }

    @Override
    public Integer getValue(int x, int y) {
        return img.getRGB(x, y);
    }

    public void clear(Color color){
        Graphics g = img.getGraphics();
        g.setColor(color);
        g.fillRect(0,0,img.getWidth(),img.getHeight());
    }
}
