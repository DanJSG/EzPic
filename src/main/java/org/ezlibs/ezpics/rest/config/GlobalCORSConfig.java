package org.ezlibs.ezpics.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Spring Boot CORS configuration used for the REST API.
 */
@Configuration
public class GlobalCORSConfig implements WebMvcConfigurer {

    private static String[] origins;

    /**
     * Automatically sets the application's allowed CORS origins based on the environment variable or application
     * properties value.
     * @param corsOrigins Autowired by Spring, the allowed CORS origins
     */
    @Autowired
    public GlobalCORSConfig(@Value("${CORS_ORIGINS}") String[] corsOrigins) {
        origins = corsOrigins;
    }

    /**
     * Adds the CORS origins to the CORS registry mapping for the REST API endpoints and allows the relevant methods.
     * @param registry Autowired by Spring, the CORS registry list
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.HEAD.name())
                .allowedOrigins(origins);
    }

}
