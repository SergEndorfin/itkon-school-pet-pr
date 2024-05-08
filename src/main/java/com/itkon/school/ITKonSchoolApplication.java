package com.itkon.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.itkon.school.repository")
@EntityScan("com.itkon.school.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class ITKonSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ITKonSchoolApplication.class, args);
    }
}
