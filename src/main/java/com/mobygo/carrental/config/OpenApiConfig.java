package com.mobygo.carrental.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mobyGoOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("MobyGo Car Rental API")
                .description("REST API for the MobyGo car rental platform. " +
                    "Manage cars, locations, rentals and users. Public endpoints for browsing; " +
                    "admin endpoints require authentication. Log in via POST /api/auth/login to get " +
                    "a JWT, then use 'Authorize' with bearerAuth. HTTP Basic is also supported.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("MobyGo Team")
                    .email("team@mobygo.ch"))
                .license(new License().name("MIT")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth").addList("basicAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecuritySchemes("basicAuth",
                    new SecurityScheme()
                        .name("basicAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")));
    }
}
