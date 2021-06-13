package org.ezlibs.imageserver.services;

import org.apache.commons.io.FilenameUtils;
import org.ezlibs.imageserver.imageprocessing.PresetImageProcessor;
import org.ezlibs.imageserver.imageprocessing.JPEGPresetImageProcessor;
import org.ezlibs.imageserver.imageprocessing.Presets;
import org.ezlibs.imageserver.types.StorageService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ImageService {

    private static final List<String> VALID_EXTENSIONS = List.of("jpg", "jpeg", "png");

    public static boolean checkImageExtension(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        return VALID_EXTENSIONS.stream().anyMatch(currentExtension -> currentExtension.equals(fileExtension));
    }

    public static boolean storeImage(MultipartFile image, String preset) {
        StorageService storageService = new S3Service();
        Path path = Paths.get(Objects.requireNonNull(image.getOriginalFilename()));
        PresetImageProcessor imageProcessor = new JPEGPresetImageProcessor();
        BufferedImage bufferedImage = convertMultipartFileToBufferedImage(image);
        if (bufferedImage == null) return false;
        try {
            List<byte[]> processedImagesBytes = imageProcessor.processImage(bufferedImage, Presets.getPreset(preset));
            for (byte[] processedImageBytes : processedImagesBytes) {
                storageService.upload(processedImageBytes, path.toString(), getMediaType(Objects.requireNonNull(image.getContentType())));
            }
            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    public static byte[] getImage(String filepath) {
        StorageService storageService = new S3Service();
        return storageService.download(filepath);
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
