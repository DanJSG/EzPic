package org.ezlibs.ezpics.storage.config;

import org.ezlibs.ezpics.storage.providers.S3ClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AWSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSConfig.class);

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
