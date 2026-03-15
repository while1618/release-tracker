package com.neon.releasetracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Release Tracker API",
            version = "v1",
            description = "API for managing software release lifecycle and status transitions"))
public class OpenApiConfig {}
