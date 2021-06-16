package org.ezlibs.ezpics.storage.config;

import org.ezlibs.ezpics.storage.providers.S3ClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

/**
 * AWS Spring configuration class. Called on startup of Spring Boot application and automatically configures the
 * relevant AWS services.
 */
@Configuration
public class AWSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSConfig.class);

    /**
     * Called during application startup. Configures AWS S3 using the region or endpoint specified in the environment
     * variables, or throughs a RuntimeException.
     *
     * @param endpoint Autowired from application properties or environment variables, represents the AWS S3 endpoint to use
     * @param region Autowired from application properties or environment variables, represents the AWS region to use
     */
    @Autowired
    public AWSConfig(@Value("${CUSTOM_ENDPOINT:#{null}}") String endpoint,
                     @Value("${AWS_REGION:#{null}}") String region) {
        if (endpoint == null && region == null)
            throw new RuntimeException("CUSTOM_ENDPOINT and AWS_REGION environment variables cannot both be null. Please set at least one of these.");
        boolean isConfigured = endpoint != null ? S3ClientProvider.configureS3Client(endpoint) : S3ClientProvider.configureS3Client(Region.of(region));
        if (isConfigured) {
            LOGGER.info("Successfully configured AWS S3 connection.");
        } else {
            LOGGER.error("Failed to configure AWS S3.");
        }
    }

}
