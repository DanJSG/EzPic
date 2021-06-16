package org.ezlibs.ezpic.processing.types;

import java.util.List;

/**
 * An image processing preset used to define how an image should be processed and the settings for doing so.
 */
public class Preset {

    private final boolean rescale;
    private final boolean crop;
    private final float compressionQuality;
    private final List<Dimensions> resizeDimensions;
    private final List<Ratio> croppingRatios;
    private final int numImages;

    /**
     * Create a preset and specify whether it should be resized and/or cropped, the compression quality, and the
     * resizing dimensions and cropping aspect ratio.
     *
     * @param shouldRescale should the image be rescaled or not?
     * @param shouldCrop should the image be cropped or not?
     * @param compressionQuality the compression quality, between 0 and 1
     * @param resizeDimensions the list of dimensions to resize the image to
     * @param croppingRatios the list of aspect ratios to resize the image to
     */
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

    /**
     * Should the image be rescaled?
     *
     * @return {@code true} if it should be rescaled, {@code false} if it should not
     */
    public boolean shouldRescale() {
        return rescale;
    }

    /**
     * Should the image be cropped?
     *
     * @return {@code true} if it should be rescaled, {@code false} if it should not
     */
    public boolean shouldCrop() {
        return crop;
    }

    /**
     * Get the list of dimensions to resize the image to.
     * @return the list of dimensions
     */
    public List<Dimensions> getResizeDimensions() {
        return resizeDimensions;
    }

    /**
     * Get the list of aspect ratios to crop the image to
     * @return the list of aspect ratios
     */
    public List<Ratio> getCroppingRatios() {
        return croppingRatios;
    }

    /**
     * Get the compression quality to compress the image(s) to.
     *
     * @return the compression quality value between 0 and 1
     */
    public float getCompressionQuality() {
        return compressionQuality;
    }

    /**
     * Get the number of images that will be generated, based on the number of dimensions and aspect ratios provided.
     * @return the number of images
     */
    public int getNumImages() {
        return numImages;
    }
}
