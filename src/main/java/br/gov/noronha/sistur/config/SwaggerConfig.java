package br.gov.noronha.sistur.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sisturOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SisTur API - Fernando de Noronha")
                        .description("API da plataforma SisTur para planejamento turístico em Fernando de Noronha.")
                        .version("v1.0.0"));
    }
}
