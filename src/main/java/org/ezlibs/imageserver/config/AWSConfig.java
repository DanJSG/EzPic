package org.ezlibs.imageserver.config;

import org.ezlibs.imageserver.providers.S3ClientProvider;
import org.ezlibs.imageserver.services.S3Service;
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
