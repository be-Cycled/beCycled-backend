package me.becycled.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author I1yi4
 */
@Configuration
public class CorsConfig {

    private static final String ALL_ALLOWED_VALUE = "*";

    @Bean
    @Primary
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns(ALL_ALLOWED_VALUE)
                    .allowedMethods(ALL_ALLOWED_VALUE)
                    .allowedHeaders(ALL_ALLOWED_VALUE)
                    .allowCredentials(true)
                    .maxAge(Long.MAX_VALUE);
            }
        };
    }
}
