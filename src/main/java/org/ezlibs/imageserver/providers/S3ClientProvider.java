package org.ezlibs.imageserver.providers;

import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.net.URISyntaxException;

public class S3ClientProvider {

    private static final String NOT_CONFIGURED_ERROR = "AWS S3 Client is not configured. " +
            "Please call the configureClient() method before attempting to use any other methods in this class.";
    private static boolean configured = false;
    private static S3Client s3;

    public static boolean configureS3Client(String endpoint) {
        try {
            URI endpointUri = new URI(endpoint);
            s3 = S3Client.builder().endpointOverride(endpointUri).build();
            configured = true;
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static S3Client getS3Client() throws IllegalStateException {
        if (!configured) throw new IllegalStateException(NOT_CONFIGURED_ERROR);
        return s3;
    }

}
