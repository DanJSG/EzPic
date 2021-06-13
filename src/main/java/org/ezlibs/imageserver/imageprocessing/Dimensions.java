package org.ezlibs.imageserver.imageprocessing;

public class Dimensions {

    private final int width;
    private final int height;

    public Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSmallestDimension() {
        return Math.min(width, height);
    }

    public int getLargestDimension() {
        return Math.max(width, height);
    }
}
