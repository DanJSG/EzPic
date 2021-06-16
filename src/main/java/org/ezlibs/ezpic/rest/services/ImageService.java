package org.ezlibs.ezpic.rest.services;

import org.apache.commons.io.FilenameUtils;
import org.ezlibs.ezpic.processing.constants.Presets;
import org.ezlibs.ezpic.processing.processors.JPEGPresetImageProcessor;
import org.ezlibs.ezpic.processing.processors.PresetImageProcessor;
import org.ezlibs.ezpic.processing.types.Dimensions;
import org.ezlibs.ezpic.processing.types.Preset;
import org.ezlibs.ezpic.processing.types.Ratio;
import org.ezlibs.ezpic.storage.services.S3Service;
import org.ezlibs.ezpic.storage.services.StorageService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for handling image processing, uploading, and downloading, as well as file type verification.
 */
public class ImageService {

    private static final List<String> VALID_EXTENSIONS = List.of("jpg", "jpeg", "png");

    public static boolean checkImageExtension(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        return VALID_EXTENSIONS.stream().anyMatch(currentExtension -> currentExtension.equals(fileExtension));
    }

    /**
     * Process the image using the image processor according to a specified preset, then upload the image using the
     * storage service.
     *
     * @param image the image to process and store
     * @param presetName the name of the preset to use for the image processing
     * @param bucketName the name of the bucket to upload the image to
     * @return a list of filenames if successful, {@code null} otherwise
     */
    public static List<String> storeImage(MultipartFile image, String presetName, String bucketName) {
        PresetImageProcessor imageProcessor = new JPEGPresetImageProcessor();
        BufferedImage bufferedImage = convertMultipartFileToBufferedImage(image);
        Preset preset = getPreset(bufferedImage, presetName);
        if (bufferedImage == null) return null;
        try {
            List<byte[]> processedImagesBytes = imageProcessor.processImage(bufferedImage, preset);
            return uploadImages(processedImagesBytes, preset.getResizeDimensions(), bucketName, image.getContentType());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    /**
     * Download an image from the storage service.
     *
     * @param filepath the images filepath
     * @param bucketName the name of the bucket to download the file from
     * @return a byte array representing the image
     */
    public static byte[] getImage(String filepath, String bucketName) {
        StorageService storageService = new S3Service();
        return storageService.download(filepath, bucketName);
    }

    /**
     * Upload a list of images in the form of byte arrays.
     *
     * @param imageBytesList the list of byte arrays to upload
     * @param dimensionsList the dimensions of each image
     * @param bucketName the bucket to upload the image to
     * @param contentType the MIME type of the image
     * @return
     */
    private static List<String> uploadImages(List<byte[]> imageBytesList, List<Dimensions> dimensionsList, String bucketName, String contentType) {
        StorageService storageService = new S3Service();
        UUID imageName = UUID.randomUUID();
        List<String> filenames = new ArrayList<>();
        String filename;
        for(int i = 0; i < imageBytesList.size(); i++) {
            byte[] imageBytes = imageBytesList.get(i);
            // generate the file name
            if (dimensionsList != null) {
                filename = imageName + "-" + dimensionsList.get(i).getLabel() + ".jpg";
            } else {
                filename = imageName + ".jpg";
            }
            filenames.add(filename);
            storageService.upload(imageBytes, filename, bucketName, getMediaType(contentType));
        }
        return filenames;
    }

    private static MediaType getMediaType(String contentType) {
        switch (contentType) {
            case "image/jpeg":
            case "image/jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.IMAGE_PNG;
        }
    }

    /**
     * Get the preset specified by name. If no preset is specified, use a default which stores the uploaded image at
     * full res with JPEG compression, along with 3 thumbnails at half, a quarter, and an eighth the size of the
     * original.
     *
     * @param image the image
     * @param name the preset name
     * @return the preset specified by name, or the default
     */
    private static Preset getPreset(BufferedImage image, String name) {
        if (name != null) return Presets.getPreset(name.toLowerCase());
        Dimensions originalDimensions = new Dimensions(image.getWidth(), image.getHeight(), "full");
        Dimensions halvedDimensions = new Dimensions(image.getWidth() / 2, image.getHeight() / 2, "lg");
        Dimensions quarteredDimensions = new Dimensions(image.getWidth() / 4, image.getHeight() / 4, "md");
        Dimensions eightDimensions = new Dimensions(image.getWidth() / 8, image.getHeight() / 8, "sm");
        Ratio aspectRatio = new Ratio(image.getWidth(), image.getHeight());
        List<Dimensions> dimensionsList = List.of(originalDimensions, halvedDimensions, quarteredDimensions, eightDimensions);
        List<Ratio> aspectRatioList = List.of(aspectRatio, aspectRatio, aspectRatio, aspectRatio);
        return new Preset(true, true, 0.75f, dimensionsList, aspectRatioList);
    }

    /**
     * Converts a MultipartFile to a BufferedImage without creating a file on disk
     * @param image the MultipartFile to convert
     * @return a BufferedImage if successful, {@code null} otherwise
     */
    private static BufferedImage convertMultipartFileToBufferedImage(MultipartFile image) {
        try {
            InputStream inputStream = new ByteArrayInputStream(image.getBytes());
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
