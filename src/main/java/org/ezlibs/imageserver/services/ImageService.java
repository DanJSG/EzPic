package org.ezlibs.imageserver.services;

import org.apache.commons.io.FilenameUtils;
import org.ezlibs.imageserver.processing.constants.Presets;
import org.ezlibs.imageserver.processing.processors.JPEGPresetImageProcessor;
import org.ezlibs.imageserver.processing.processors.PresetImageProcessor;
import org.ezlibs.imageserver.processing.types.Dimensions;
import org.ezlibs.imageserver.processing.types.Preset;
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
        Preset preset = Presets.getPreset(presetName.toLowerCase());
        PresetImageProcessor imageProcessor = new JPEGPresetImageProcessor();
        BufferedImage bufferedImage = convertMultipartFileToBufferedImage(image);
        if (bufferedImage == null) return null;
        try {
            List<byte[]> processedImagesBytes = imageProcessor.processImage(bufferedImage, Presets.getPreset(presetName));
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
