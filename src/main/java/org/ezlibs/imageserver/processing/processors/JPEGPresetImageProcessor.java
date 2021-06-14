package org.ezlibs.imageserver.processing.processors;

import org.ezlibs.imageserver.processing.types.Dimensions;
import org.ezlibs.imageserver.processing.types.Preset;
import org.ezlibs.imageserver.processing.types.Ratio;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JPEGPresetImageProcessor implements PresetImageProcessor {

    @Override
    public List<byte[]> processImage(BufferedImage image, Preset preset) throws IOException {
        int numImages = preset.getNumImages();
        List<byte[]> outputImages = new ArrayList<>(numImages);
        for (int i = 0; i < numImages; i++) {
            BufferedImage processedImage = image;
            if (preset.shouldCrop()) {
                Ratio croppingRatio = preset.getCroppingRatios().get(i);
                processedImage = cropImage(processedImage, croppingRatio.getRatioX(), croppingRatio.getRatioY());
            }
            if (preset.shouldRescale()) {
                Dimensions rescaleDimensions = preset.getResizeDimensions().get(i);
                processedImage = rescaleImage(processedImage, rescaleDimensions.getWidth(), rescaleDimensions.getHeight());
            }
            byte[] outputImageBytes = compressImage(processedImage, preset.getCompressionQuality());
            outputImages.add(outputImageBytes);
        }
        return outputImages;
    }

    @Override
    public byte[] compressImage(BufferedImage image, float compressionQuality) throws IOException {
        ByteArrayOutputStream compressedImage = new ByteArrayOutputStream();
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressedImage);
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam imageWriteParameters = new JPEGImageWriteParam(null);//imageWriter.getDefaultWriteParam();
        imageWriteParameters.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParameters.setCompressionQuality(compressionQuality);
        imageWriter.setOutput(outputStream);
        imageWriter.write(null, new IIOImage(image, null, null), imageWriteParameters);
        imageWriter.dispose();
        byte[] compressedImageBytes = compressedImage.toByteArray();
        compressedImage.close();
        outputStream.close();
        return compressedImageBytes;
    }

    @Override
    public BufferedImage rescaleImage(BufferedImage image, int width, int height) {
        int imageType = BufferedImage.TYPE_INT_RGB;
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), imageType);
        Graphics2D drawer = bufferedImage.createGraphics();
        drawer.drawImage(scaledImage, 0, 0, null);
        drawer.dispose();
        return bufferedImage;
    }

    @Override
    public BufferedImage rescaleImage(BufferedImage image, float scaleFactor) {
        int height = (int) Math.floor(image.getHeight() * scaleFactor);
        int width = (int) Math.floor(image.getWidth() * scaleFactor);
        return rescaleImage(image, width, height);
    }

    @Override
    public BufferedImage cropImage(BufferedImage image, int newWidth, int newHeight) throws IllegalArgumentException {
        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();
        if (newWidth > oldWidth || newHeight > oldHeight) {
            throw new IllegalArgumentException("The new width and height of the cropped image must be less than or equal to the input image.");
        }
        int xOffset = (oldWidth - newWidth) / 2;
        int yOffset = (oldHeight - newHeight) / 2;
        return image.getSubimage(xOffset, yOffset, newWidth, newHeight);
    }

    @Override
    public BufferedImage cropImage(BufferedImage image, float xRatio, float yRatio) throws IllegalArgumentException {
        int width = image.getWidth();
        int height = image.getHeight();
        float aspectRatioQuotient = xRatio / yRatio;
        if (aspectRatioQuotient > 1 || (aspectRatioQuotient == 1 && width < height)) {
            int newHeight = (int)(width / aspectRatioQuotient);
            return cropImage(image, width, newHeight);
        } else {
            int newWidth = (int)(height * aspectRatioQuotient);
            return cropImage(image, newWidth, height);
        }
    }

}
