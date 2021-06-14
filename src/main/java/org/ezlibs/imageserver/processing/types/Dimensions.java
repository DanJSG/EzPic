package org.ezlibs.imageserver.processing.types;

public class Dimensions {

    private final int width;
    private final int height;
    private final String label;

    public Dimensions(int width, int height, String label) {
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getLabel() {
        return label;
    }

}
