package br.com.coffeeandit.api;

import br.com.coffeeandit.business.PeopleService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/v1/pessoas")
public class PeopleResource {

    @Inject
    private PeopleService service;

    @APIResponses(value = {
            @APIResponse(responseCode = "204", description ="Retorno para inclusão do documento"),
            @APIResponse(responseCode = "401", description ="Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description ="Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description ="Recurso não encontrado")
    })
    @Operation(description = "API para envio de documentos do correntista")
    @GET
    @Path("/pessoas/{id}")
    public Response buscarPessoas(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

}
