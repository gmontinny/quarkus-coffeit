package br.coffeeandit.domain;

import br.coffeeandit.api.dto.LimiteDiario;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/v1/limite")
@RegisterRestClient
public interface LimiteService {

    @GET
    @Path("/{agencia}/{conta}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LimiteDiario findByAgenciaConta(@PathParam("agencia") final Long agencia,
                                           @PathParam("conta") final Long conta);
}
