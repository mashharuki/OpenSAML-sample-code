package no.steras.opensamlSamples.opensaml4WebprofileDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Spring Boot Application
 * OpenSAML 5 Web Profile Demo
 */
@SpringBootApplication
@ServletComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
