package org.ezlibs.ezpics.storage.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.net.URISyntaxException;

public class S3ClientProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3ClientProvider.class);
    private static final String NOT_CONFIGURED_ERROR = "AWS S3 Client is not configured. " +
            "Please call the configureClient() method before attempting to use any other methods in this class.";
    private static boolean configured = false;
    private static S3Client s3;

    public static boolean configureS3Client(Region region) {
        s3 = S3Client.builder().region(region).build();
        LOGGER.info("Attempting to connect to Amazon S3...");
        s3.listBuckets();
        configured = true;
        return true;
    }

    public static boolean configureS3Client(String endpoint) {
        try {
            URI endpointUri = new URI(endpoint);
            s3 = S3Client.builder().endpointOverride(endpointUri).build();
            LOGGER.info("Attempting to connect to Amazon S3...");
            s3.listBuckets();
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
