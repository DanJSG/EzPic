package org.ezlibs.ezpics.storage.config;

import org.ezlibs.ezpics.storage.providers.S3ClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSConfig.class);

    @Autowired
    public AWSConfig() {
        S3ClientProvider.configureS3Client("http://localhost:4566");
        LOGGER.info("Successfully configured AWS S3.");
    }

}
