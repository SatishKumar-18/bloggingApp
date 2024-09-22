package com.project.BloggingApp.config.SwaggerUI;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class SwaggerUiConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info().title("Blogging App APIs")
                                .description("By Satish")
                ).tags(Arrays.asList(
                        new Tag().name("Public APIs"),
                        new Tag().name("User APIs"),
                        new Tag().name("Article APIs"),
                        new Tag().name("Comment APIs")
                )).addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
