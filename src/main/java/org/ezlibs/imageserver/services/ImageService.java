package org.ezlibs.imageserver.services;

import org.apache.commons.io.FilenameUtils;
import org.ezlibs.imageserver.processing.constants.Presets;
import org.ezlibs.imageserver.processing.processors.JPEGPresetImageProcessor;
import org.ezlibs.imageserver.processing.processors.PresetImageProcessor;
import org.ezlibs.imageserver.processing.types.Dimensions;
import org.ezlibs.imageserver.processing.types.Preset;
import org.ezlibs.imageserver.processing.types.Ratio;
import org.ezlibs.imageserver.types.StorageService;
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

public class ImageService {

    private static final List<String> VALID_EXTENSIONS = List.of("jpg", "jpeg", "png");

    public static boolean checkImageExtension(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        return VALID_EXTENSIONS.stream().anyMatch(currentExtension -> currentExtension.equals(fileExtension));
    }

    public static List<String> storeImage(MultipartFile image, String presetName) {
        PresetImageProcessor imageProcessor = new JPEGPresetImageProcessor();
        BufferedImage bufferedImage = convertMultipartFileToBufferedImage(image);
        Preset preset = getPreset(bufferedImage, presetName);
        if (bufferedImage == null) return null;
        try {
            List<byte[]> processedImagesBytes = imageProcessor.processImage(bufferedImage, preset);
            return uploadImages(processedImagesBytes, preset.getResizeDimensions(), image.getContentType());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    public static byte[] getImage(String filepath) {
        StorageService storageService = new S3Service();
        return storageService.download(filepath);
    }

    private static List<String> uploadImages(List<byte[]> imageBytesList, List<Dimensions> dimensionsList, String contentType) {
        StorageService storageService = new S3Service();
        UUID imageName = UUID.randomUUID();
        List<String> filenames = new ArrayList<>();
        String filename;
        System.out.println(imageBytesList.size());
        for(int i = 0; i < imageBytesList.size(); i++) {
            byte[] imageBytes = imageBytesList.get(i);
            if (dimensionsList != null) {
                filename = imageName + "-" + dimensionsList.get(i).getLabel() + ".jpg";
            } else {
                filename = imageName + ".jpg";
            }
            filenames.add(filename);
            storageService.upload(imageBytes, filename, getMediaType(contentType));
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
