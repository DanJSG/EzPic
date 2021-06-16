package org.ezlibs.ezpics.rest.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ezlibs.ezpics.rest.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST API controller for uploading and downloading images. All API endpoints are mapped underneath the path:
 * {@code /api/v1/}
 */
@RestController
@RequestMapping("/api/v1/")
public class ImageController {

    /**
     * A JSON response containing a list of filenames for uploaded files.
     */
    private static final class FilenamesJsonResponse {
        @JsonProperty
        final List<String> filenames;

        public FilenamesJsonResponse(List<String> filenames) {
            this.filenames = filenames;
        }

        public String writeValueAsString() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * REST API method for processing & uploading an image. Mapped to {@code /api/v1/{bucket}/image} where {bucket} is
     * the name of the bucket to upload the image to. A parameter can be passed specifying a preset to process the image
     * with. Allowed presets are:
     * <ul>
     *  <li>{@code square}</li>
     *  <li>{@code tall}</li>
     *  <li>{@code wide}</li>
     * </ul>
     *
     * @param image the image to upload
     * @param bucket the bucket to upload the image to
     * @param preset the image processing preset
     * @return HTTP response entity with relevant code and body data
     */
    @PostMapping(value = "/{bucket}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image,
                                                     @PathVariable String bucket,
                                                     @RequestParam(required = false) String preset) {
        boolean isValid = ImageService.checkImageExtension(image);
        if (!isValid)
            return ResponseEntity.badRequest().body("Wrong file type. Only '.png', '.jpg', and '.jpeg' are accepted.");
        List<String> imageNames = ImageService.storeImage(image, preset, bucket);
        if (imageNames == null) return ResponseEntity.internalServerError().body(null);
        return ResponseEntity.status(HttpStatus.OK).body(new FilenamesJsonResponse(imageNames).writeValueAsString());
    }

    /**
     * REST API method for downloading an image. Mapped to {@code /api/v1/{bucket}/image/{filename}} where {bucket} is
     * the name of the bucket to download the image from and {filename} is the name of the file.
     *
     * @param bucket the name of the bucket to download the image from
     * @param filename the name of the file to download
     * @return the file
     */
    @GetMapping(value = "/{bucket}/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public static @ResponseBody
    byte[] downloadImage(@PathVariable String bucket, @PathVariable String filename) {
        return ImageService.getImage(filename, bucket);
    }

}
