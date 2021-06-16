package org.ezlibs.imageserver.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ezlibs.imageserver.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ImageController {

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

    @PostMapping(value = "/{bucket}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image, @PathVariable String bucket, @RequestParam(required = false) String preset) {
        boolean isValid = ImageService.checkImageExtension(image);
        if (!isValid) return ResponseEntity.badRequest().body("Wrong file type. Only '.png', '.jpg', and '.jpeg' are accepted.");
        List<String> imageNames = ImageService.storeImage(image, preset, bucket);
        if (imageNames == null) return ResponseEntity.internalServerError().body(null);
        return ResponseEntity.status(HttpStatus.OK).body(new FilenamesJsonResponse(imageNames).writeValueAsString());
    }

    @GetMapping(value = "/{bucket}/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public static @ResponseBody byte[] downloadImage(@PathVariable String bucket, @PathVariable String filename) {
        return ImageService.getImage(filename, bucket);
    }

}
