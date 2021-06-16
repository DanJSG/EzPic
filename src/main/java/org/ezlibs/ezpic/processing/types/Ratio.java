package org.ezlibs.ezpic.processing.types;

/**
 * An aspect ratio.
 */
public class Ratio {

    private final float xRatio;
    private final float yRatio;

    public Ratio(float xRatio, float yRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    /**
     * Get the aspect ratio's horizontal (x) value.
     * @return horizontal value as a float
     */
    public float getRatioX() {
        return xRatio;
    }

    /**
     * Get the aspect ratio's vertical (y) value.
     * @return vertical value as a float
     */
    public float getRatioY() {
        return yRatio;
    }
}
