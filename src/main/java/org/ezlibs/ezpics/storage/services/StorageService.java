package org.ezlibs.ezpics.storage.services;

import org.springframework.http.MediaType;

/**
 * Service for uploading and downloading files from a particular bucket.
 */
public interface StorageService {

    /**
     * Upload a byte array to a bucket with a given filepath. The MIME type of the media is used for setting metadata
     * so that the file can be downloaded in the correct format.
     *
     * @param bytes the data as a byte array
     * @param filepath the filepath to upload the file to
     * @param bucketName the name of the bucket to upload the file to
     * @param mimeType the MIME type of the data
     */
    void upload(byte[] bytes, String filepath, String bucketName, MediaType mimeType);

    /**
     * Download a file with a given filepath from a bucket.
     *
     * @param filepath the path of the file
     * @param bucketName the name of the bucket to download the file from
     * @return array of bytes for the file
     */
    byte[] download(String filepath, String bucketName);

}
