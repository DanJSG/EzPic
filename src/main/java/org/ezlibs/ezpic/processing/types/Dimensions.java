package org.ezlibs.ezpic.processing.types;

/**
 * 2D dimensions with an associated label.
 */
public class Dimensions {

    private final int width;
    private final int height;
    private final String label;

    /**
     * Create a set of 2D dimensions of specified width and height with an associated label.
     *
     * @param width the width dimension
     * @param height the height dimension
     * @param label the associated label
     */
    public Dimensions(int width, int height, String label) {
        this.width = width;
        this.height = height;
        this.label = label;
    }

    /**
     * Get the height dimension.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the width dimension.
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the dimension's label.
     *
     * @return label as a String
     */
    public String getLabel() {
        return label;
    }

}
