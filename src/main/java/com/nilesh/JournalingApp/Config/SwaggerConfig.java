package com.nilesh.JournalingApp.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Journal App API Doc")
                        .description("API documentation for Journal App")
                        .version("1.0.0"))

                .servers(List.of(
                        new Server()
                                .url("https://journalapp-production-1986.up.railway.app/journal")
                                .description("Production Server")
                ))

                
                .tags(List.of(
                        new Tag().name("Public APIs").description("Publicly accessible endpoints, no auth required"),
                        new Tag().name("User APIs").description("User-specific operations"),
                        new Tag().name("Journal Entry APIs").description("CRUD operations on journal entries"),
                        new Tag().name("Admin APIs").description("Admin-only operations")
                ))

                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                );
    }
}