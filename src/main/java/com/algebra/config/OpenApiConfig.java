package com.algebra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Algebra Solver API",
                version = "1.0",
                description = "REST API for storing, retrieving and evaluating algebraic equations using a postfix expression tree."
        )
)
@Configuration
public class OpenApiConfig {
}
