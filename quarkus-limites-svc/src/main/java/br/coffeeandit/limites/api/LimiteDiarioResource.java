package br.coffeeandit.limites.api;

import br.coffeeandit.limites.domain.LimiteDiarioBusiness;
import br.coffeeandit.limites.model.LimiteDiario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

@Path("/v1/limite")
@Tag(name = "/v1/limite", description = "Grupo de API's para limites financeiros")
public class LimiteDiarioResource {

    @Inject
    private LimiteDiarioBusiness limiteDiarioBusiness;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "API responsável por criar o limite para uma conta")
    @APIResponses(value = {@APIResponse(responseCode = "201", description = "Retorno OK do limite criado."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    public Response inserirLimite(@Context UriInfo uriInfo, final LimiteDiario newLimiteDiario) {

        var limiteDiario = limiteDiarioBusiness.inserirLimiteDiario(newLimiteDiario);
        var uri = uriInfo.getAbsolutePathBuilder()
                .path(LimiteDiarioResource.class, "findById")
                .build(limiteDiario.id);
        return Response.created(uri).build();
    }

    @Operation(description = "API responsável por recuperar um limite de uma conta")
    @Parameters(@Parameter(name = "id", in = ParameterIn.PATH, description = "Id do limite"))
    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Retorno OK com o limite encontrado."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response findById(@NotNull @Context UriInfo uriInfo, @PathParam("id") final Long id) {
        final Optional<LimiteDiario> entity = LimiteDiario.findByIdOptional(id);
        if (entity.isPresent()) {
            return Response.ok(entity).build();
        }
        throw new NotFoundException("Não foi possível encontrar o limite por esse id");
    }

    @Operation(description = "API responsável por recuperar um limite de uma agencia e conta")
    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Retorno OK com o limite encontrado."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters({@Parameter(name = "agencia", in = ParameterIn.PATH, description = "Código da Agência"),
            @Parameter(name = "conta", in = ParameterIn.PATH, description = "Código da Conta")}
    )
    @GET
    @Path("/{agencia}/{conta}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByAgenciaConta(@Context UriInfo uriInfo, @PathParam("agencia") final Long agencia,
                                       @PathParam("conta") final Long conta) {
        var entity = limiteDiarioBusiness.buscarLimiteDiario(agencia, conta);
        if (!entity.isPresent()) {
            throw new NotFoundException("Não foi possível encontrar o limite por esse id");
        }
        return Response.ok(entity.get()).build();
    }

    @Operation(description = "API responsável por remover o limite de uma conta")
    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Retorno OK com a transação encontrada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(@Parameter(name = "id", in = ParameterIn.PATH, description = "Id do limite"))
    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteById(@Context UriInfo uriInfo, @PathParam("id") final Long id) {
        var optional = LimiteDiario.findByIdOptional(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Não foi possível encontrar o limite por esse id");
        }
        final PanacheEntityBase panacheEntityBase = optional.get();
        panacheEntityBase.delete();

        return Response.noContent().build();
    }
}
