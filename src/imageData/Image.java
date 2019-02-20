package imageData;

public interface Image<T> {

    public int getWidth();

    public int getHeight();

    public void setValue(int x, int y, T value);

    public T getValue(int x, int y);
}
