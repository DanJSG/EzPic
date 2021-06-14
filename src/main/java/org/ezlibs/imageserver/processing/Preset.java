package org.ezlibs.imageserver.processing;

import java.util.List;

public class Preset {

    private final boolean rescale;
    private final boolean crop;
    private final float compressionQuality;
    private final List<Dimensions> resizeDimensions;
    private final List<Ratio> croppingRatios;
    private final int numImages;

    public Preset(boolean shouldRescale, boolean shouldCrop, float compressionQuality, List<Dimensions> resizeDimensions,
                  List<Ratio> croppingRatios) {
        if ((resizeDimensions != null && croppingRatios != null) && resizeDimensions.size() != croppingRatios.size()) {
            throw new IllegalArgumentException("There must be the same number of cropping ratios and resize dimensions");
        }
        this.rescale = shouldRescale;
        this.crop = shouldCrop;
        this.compressionQuality = compressionQuality;
        this.resizeDimensions = resizeDimensions;
        this.croppingRatios = croppingRatios;
        this.numImages = resizeDimensions != null ? resizeDimensions.size() : 1;
    }

    public boolean shouldRescale() {
        return rescale;
    }

    public boolean shouldCrop() {
        return crop;
    }

    public List<Dimensions> getResizeDimensions() {
        return resizeDimensions;
    }

    public List<Ratio> getCroppingRatios() {
        return croppingRatios;
    }

    public float getCompressionQuality() {
        return compressionQuality;
    }

    public int getNumImages() {
        return numImages;
    }
}
