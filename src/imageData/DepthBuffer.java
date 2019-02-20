package imageData;

public class DepthBuffer implements Image<Float> {

    private int width;
    private int height;

    private float depthBuffer[][];

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        depthBuffer = new float[height][width];
    }

    public void clear() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                depthBuffer[i][j]=1;
            }

        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setValue(int x, int y, Float value) {
        depthBuffer[y][x] = value.floatValue();
    }

    @Override
    public Float getValue(int x, int y) {
        return new Float(depthBuffer[y][x]);
    }
}
