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
        final String securitySchemeName = "basicAuth";
        return new OpenAPI()
            .info(new Info()
                .title("MobyGo Car Rental API")
                .description("REST API for the MobyGo car rental platform. " +
                    "Manage cars, locations, rentals and users. " +
                    "Public endpoints for browsing; admin endpoints require Basic Auth.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("MobyGo Team")
                    .email("team@mobygo.ch"))
                .license(new License().name("MIT")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")));
    }
}
