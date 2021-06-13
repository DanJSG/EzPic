package org.ezlibs.imageserver.types;

import org.springframework.http.MediaType;

public interface StorageService {

    void upload(byte[] bytes, String filepath, MediaType mimeType);

    byte[] download(String filepath);

}
