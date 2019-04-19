package com.blah.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class UUIDConfig {

    @Bean
    public Supplier<UUID> uuidSupplier() {
        return () -> UUID.randomUUID();
    }
}
