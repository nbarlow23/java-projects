package imageEditor;

public class Image {

    private Pixel[][] image;
    private int width;
    private int height;
    private int curSize;
    private int blur;
    private static final int DIFF_OFFSET = 128;

    /**
     *
     * @param w width of image
     * @param h height of image
     */
    public Image(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
        this.setImage(new Pixel[h][w]);
        this.setCurSize(0);
        this.setBlur(0);
    }

    /**
     *
     * @return new copied Image
     */
    private Pixel[][] copyImage() {
        Pixel[][] dest = new Pixel[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                dest[row][col] = image[row][col];
            }
        }
        return dest;
    }

    /**
     * Prints out the image
     */
    public void printImage() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(image[row][col].getRed() + " ");
                System.out.print(image[row][col].getGreen() + " ");
                System.out.println(image[row][col].getBlue());
            }
        }
    }

    /**
     * Inverts each color component of each pixel
     */
    public void invert() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel p = image[row][col];
                p.invertPixel();
                image[row][col] = p;
            }
        }
    }

    /**
     * averages each color component of each pixel
     */
    public void grayScale() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel p = image[row][col];
                p.grayscalePixel();
                image[row][col] = p;
            }
        }
    }

    /**
     *
     * @param p pixel
     * @param row current row
     * @param col current column
     * @return maxDifference, favoring red over green over blue
     */
    private int findMaxDiff(Pixel p, int row, int col) {
        if (row == 0 || col == 0) {
            return 0;
        }
        int redDiff = p.getRed() - image[row - 1][col - 1].getRed();
        int greenDiff = p.getGreen() - image[row - 1][col - 1].getGreen();
        int blueDiff = p.getBlue() - image[row - 1][col - 1].getBlue();
        int maxDiff = redDiff;
        if (Math.abs(greenDiff) > Math.abs(redDiff) || Math.abs(blueDiff) > Math.abs(redDiff)) {
            if (Math.abs(blueDiff) > Math.abs(greenDiff)) {
                maxDiff = blueDiff;
            }
            else {
                maxDiff = greenDiff;
            }
        }
        return maxDiff;
    }

    private int scaleV(int v) {
        if (v < 0) {
            return 0;
        }
        if (v > Pixel.MAX_COLOR_VALUE) {
            return Pixel.MAX_COLOR_VALUE;
        }
        return v;
    }

    public void emboss() {
        Pixel[][] newImage = copyImage();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel p = image[row][col];
                int maxDiff = findMaxDiff(p, row, col);
                int v = maxDiff + DIFF_OFFSET;
                v = scaleV(v);
                Pixel newP = p.emboss(v);
                newImage[row][col] = newP;
            }
        }
        image = newImage;
    }

    private int getTempBlur(int col) {
        int tempBlur = blur;
        if (col + tempBlur >= width) {
            tempBlur = width - col;
        }
        return tempBlur;
    }

    private Pixel getBlurredPixel(int tempBlur, int row, int col) {
        int r = 0, g = 0, b = 0;

        for (int i = 0; i < tempBlur; i++) {
            r += image[row][col + i].getRed();
            g += image[row][col + i].getGreen();
            b += image[row][col + i].getBlue();
        }

        if (tempBlur != 0) {
            r /= tempBlur;
            g /= tempBlur;
            b /= tempBlur;
        }

        return new Pixel(r, g, b);
    }

    public void motionBlur() {
        Pixel[][] newImage = copyImage();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int tempBlur = getTempBlur(col);
                Pixel newP = getBlurredPixel(tempBlur, row, col);
                newImage[row][col] = newP;
            }
        }

        image = newImage;
    }

    public void addToArray(Pixel p) {
        int row = curSize / width;
        int col = curSize % width;
        image[row][col] = p;
        curSize++;
    }

    public Pixel[][] getImage() {
        return image;
    }

    public void setImage(Pixel[][] image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCurSize() {
        return curSize;
    }

    public void setCurSize(int curSize) {
        this.curSize = curSize;
    }

    public int getBlur() {
        return blur;
    }

    public void setBlur(int blur) {
        this.blur = blur;
    }

}
