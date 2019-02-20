package imageData;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TestVisibility {

    private ImageBuffer imageBuffer;
    private DepthBuffer depthBuffer;

    public TestVisibility(BufferedImage img) {
        this.imageBuffer = new ImageBuffer(img);
        depthBuffer = new DepthBuffer(img.getWidth(),img.getHeight());
    }
    public void clear(Color color){
        depthBuffer.clear();
        imageBuffer.clear(color);
    }
    public boolean isVisible(int x,int y,float z){
        return depthBuffer.getValue(x,y) >= z;
    }

    public void putPixel(int x, int y,float z,int color){
        if (isVisible(x,y,z)){
            depthBuffer.setValue(x,y,z);
            imageBuffer.setValue(x,y,color);
        }
    }
}
