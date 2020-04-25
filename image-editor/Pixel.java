package imageEditor;

public class Pixel {
    private int red;
    private int green;
    private int blue;

    protected static final int MAX_COLOR_VALUE = 255;
    protected static final int PIXEL_COMPONENTS = 3;

    public Pixel(int r, int g, int b) {
        red = r;
        green = g;
        blue = b;
    }

    public void invertPixel() {
        red = MAX_COLOR_VALUE - red;
        green = MAX_COLOR_VALUE - green;
        blue = MAX_COLOR_VALUE - blue;
    }

    public void grayscalePixel() {
        int avg = (red + green + blue) / PIXEL_COMPONENTS;
        red = avg;
        green = avg;
        blue = avg;
    }

    public Pixel emboss(int v) {
        Pixel p = new Pixel(v, v, v);
        return p;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

}
