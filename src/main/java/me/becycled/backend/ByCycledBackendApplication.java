package me.becycled.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author binakot
 */
@SpringBootApplication
@SuppressWarnings({"HideUtilityClassConstructor", "PMD.UseUtilityClass"})
public class ByCycledBackendApplication {

    public static void main(final String... args) {
        SpringApplication.run(ByCycledBackendApplication.class, args);
    }
}
