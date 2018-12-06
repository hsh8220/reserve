package com.exhibition.reserve;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReserveApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/app/config/reserve/real-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ReserveApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
