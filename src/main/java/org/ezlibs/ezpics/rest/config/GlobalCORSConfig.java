package org.ezlibs.ezpics.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCORSConfig implements WebMvcConfigurer {

    private static String[] origins;

    @Autowired
    public GlobalCORSConfig() {
        origins = new String[] {"http://localhost:3010", "http://localhost:3000"};
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.HEAD.name())
                .allowedOrigins(origins);
    }

}
