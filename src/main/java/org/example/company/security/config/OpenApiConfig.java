package org.example.company.security.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

@Configuration
public class OpenApiConfig {

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(
                new Parameter()
                    .in("header")
                    .name("X-User-Email")
                    .required(false)
                    .schema(new StringSchema().description("Email for authentication")));
            operation.addParametersItem(
                new Parameter()
                    .in("header")
                    .name("X-User-Role")
                    .required(false)
                    .schema(new StringSchema().description("SUPER_ADMIN for bootstrap")));
            return operation;
        };
    }
}
