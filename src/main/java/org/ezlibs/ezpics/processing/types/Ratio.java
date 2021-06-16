package org.ezlibs.ezpics.processing.types;

public class Ratio {

    private final float xRatio;
    private final float yRatio;

    public Ratio(float xRatio, float yRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public float getRatioX() {
        return xRatio;
    }

    public float getRatioY() {
        return yRatio;
    }
}
