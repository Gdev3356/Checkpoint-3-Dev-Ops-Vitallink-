package br.com.fiap.vitallink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vitallinkOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VitalLink API")
                        .description("""
                                API RESTful para gerenciamento de consultas médicas.
                                
                                Conecta pacientes e médicos, organizando especialidades,
                                agendamentos e histórico clínico de forma segura e escalável.
                                
                                Banco de dados: Oracle XE 21c (containerizado)
                                Stack: Java 17 + Spring Boot 3 + JPA/Hibernate
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Grupo VitalLink — FIAP")
                                .email("vitallink@fiap.com.br"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}