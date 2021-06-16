package org.ezlibs.ezpic.processing.processors;

import org.ezlibs.ezpic.processing.types.Preset;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * An image processor used to process images based on the settings within a preset object.
 */
public interface PresetImageProcessor {

    /**
     * Process an image according to the settings within the provided preset.
     *
     * @param image the image to process
     * @param preset the preset to use
     * @return a list of images as byte arrays
     * @throws IOException if processing the image fails
     */
    List<byte[]> processImage(BufferedImage image, Preset preset) throws IOException;

    /**
     * Compress an image to a specified quality using JPEG compression.
     *
     * @param image the image to compress
     * @param compressionQuality the compression quality between 0 and 1
     * @return the compressed image as a byte array
     * @throws IOException if compressing the image fails
     */
    byte[] compressImage(BufferedImage image, float compressionQuality) throws IOException;

    /**
     * Resize the image according to a scaling factor
     * @param image the image to resize
     * @param scaleFactor the factor to scale the image by
     * @return the resized image
     */
    BufferedImage rescaleImage(BufferedImage image, float scaleFactor);

    /**
     * Resize the image to a specific width and height. This will stretch an image if the width and height are not
     * in sync with the image's aspect ratio.
     *
     * @param image the image
     * @param width the desired width
     * @param height the desired height
     * @return the resized image
     */
    BufferedImage rescaleImage(BufferedImage image, int width, int height);

    /**
     * Crop an image to a specified width and height. This will ensure that the cropping is anchored at the center of
     * the image.
     *
     * @param image the image to crop
     * @param newWidth the height width
     * @param newHeight the desired height
     * @return the cropped image
     * @throws IllegalArgumentException if the desired width or height are bigger than the original image
     */
    BufferedImage cropImage(BufferedImage image, int newWidth, int newHeight) throws IllegalArgumentException;

    /**
     * Crop an image to a specified aspect ratio. The cropping will be anchored at the center of the image and will crop
     * as little of the image as possible to achieve the aspect ratio.
     *
     * @param image the image to crop
     * @param xRatio the horizontal aspect ratio value
     * @param yRatio the vertical aspect ratio value
     * @return the cropped image
     */
    BufferedImage cropImage(BufferedImage image, float xRatio, float yRatio);

}
