package com.mardi2020.votedogapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "vote your dog API 명세서",
                description = "API description",
                version = "v0.0.1"
        )
)
public class SwaggerConfig {
}
