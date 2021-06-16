package org.ezlibs.imageserver.types;

import org.springframework.http.MediaType;

public interface StorageService {

    void upload(byte[] bytes, String filepath, String bucketName, MediaType mimeType);

    byte[] download(String filepath, String bucketName);

}
