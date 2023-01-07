package br.coffeeandit.api;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/coffee")
@Tag(name = "/v1/coffee", description = "API de Hello World"
)
public class HelloWorldController {

    @ConfigProperty(name = "greeting.message", defaultValue = "Não achamos")
    private String message;

    @GET
    @Operation(description = "API responsável por retornar o nome para Hello")
    @APIResponses(value = {@APIResponse(description = "Retorna 200 com o nome de Hello",
            responseCode = "200"), @APIResponse(description = "Retorna 400 no caso de parametros errados",
            responseCode = "400"), @APIResponse(description = "Retorna 500 no caso de erro inexperado",
            responseCode = "500"),
            @APIResponse(description = "Retorna 401 no caso token não informado ou expirado",
                    responseCode = "401")
    })
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.TEXT_HTML)
    public String coffee() {
        return " Oi " + message;
    }
}
