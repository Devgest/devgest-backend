package tech.devgest.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@SpringBootApplication
public class DevgestBackendApplication {

    public static void main(String[] args) {
        Dotenv.configure().systemProperties().load();

        SpringApplication.run(DevgestBackendApplication.class, args);
    }
}
