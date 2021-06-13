package org.ezlibs.imageserver.controllers;

import org.ezlibs.imageserver.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
public class ImageController {

    @PostMapping(value = "/image")
    public static ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image, @RequestParam(required = false) String preset) {
        boolean isValid = ImageService.checkImageExtension(image);
        if (!isValid) return ResponseEntity.badRequest().body("Wrong file type. Only '.png', '.jpg', and '.jpeg' are accepted.");
        boolean uploadSuccess = ImageService.storeImage(image, preset);
        return uploadSuccess ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) : ResponseEntity.internalServerError().body(null);
    }

    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public static @ResponseBody byte[] downloadImage(@PathVariable String filename) {
        return ImageService.getImage(filename);
    }

}
