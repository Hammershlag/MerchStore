package com.example.merchstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * The MerchStoreApplication class is the main entry point for the Spring Boot application.
 *
 * It has one main method:
 * <ul>
 *     <li>main(String[] args): This method starts the Spring Boot application.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 29.05.2024
 */
@SpringBootApplication
public class MerchStoreApplication {

    /**
     * This method starts the Spring Boot application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(MerchStoreApplication.class, args);
    }

}
