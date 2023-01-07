package br.com.coffeeandit.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "/v1/transactions", description = "Grupo de API's para manipulação de transações financeiras")
        },
        info = @Info(
                title = "Sistema de exemplo de transações, curso Quarkus CoffeeandIT",
                version = "1.0.0",
                contact = @Contact(
                        name = "Fale com a Coffeeandit",
                        url = "https:www.coffeeandit.com.br/")),
        servers = {
                @Server(url = "http:localhost:8080")
        })
public class OpenApiConfig extends Application {
}
