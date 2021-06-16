package org.ezlibs.ezpics.processing.processors;

import org.ezlibs.ezpics.processing.types.Preset;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface PresetImageProcessor {

    List<byte[]> processImage(BufferedImage image, Preset preset) throws IOException;

    byte[] compressImage(BufferedImage image, float compressionQuality) throws IOException;

    BufferedImage rescaleImage(BufferedImage image, float scaleFactor);

    BufferedImage rescaleImage(BufferedImage image, int width, int height);

    BufferedImage cropImage(BufferedImage image, int newWidth, int newHeight) throws IllegalArgumentException;

    BufferedImage cropImage(BufferedImage image, float xRatio, float yRatio) throws IllegalArgumentException;

}
