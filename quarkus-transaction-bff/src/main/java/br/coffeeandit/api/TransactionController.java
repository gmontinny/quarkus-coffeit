package br.coffeeandit.api;

import br.coffeeandit.api.dto.Conta;
import br.coffeeandit.api.dto.RequisicaoTransacaoDTO;
import br.coffeeandit.domain.TransactionService;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Objects;
import java.util.Optional;

@Path("/v1/transactions")
@SecurityRequirement(name = "jwt", scopes = {"coffeeandit"})
@Tag(name = "/v1/transactions", description = "Grupo de API's para manipulação de transações financeiras")
@Authenticated
public class TransactionController {

    private static final Logger LOG = Logger.getLogger(TransactionController.class);


    @Claim("conta")
    String conta;

    @Claim("agencia")
    String agencia;

    @Inject
    TransactionService transactionService;

    @Operation(description = "API responsável por criar uma transação financeira")
    @APIResponses(value = {@APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"coffeeandit-transaction"})
    public Response save(@Context UriInfo uriInfo, final RequisicaoTransacaoDTO requisicaoTransacaoDTO, @Context final SecurityContext ctx) {
        LOG.info("Transação enviada pelo usuário " + ctx.getUserPrincipal().getName() + " - " + requisicaoTransacaoDTO);
        final Optional<RequisicaoTransacaoDTO> transacaoDTO = transactionService.save(introspectAccount(requisicaoTransacaoDTO));

        var uri = uriInfo.getAbsolutePathBuilder()
                .path(TransactionController.class, "findById")
                .build(transacaoDTO.orElseThrow(() ->
                        new NotFoundException("Não foi possível processar a transação")).getUuid().toString());

        return Response.created(uri).header("x-signature", transacaoDTO.get().getSignature()).build();


    }
    @DELETE
    @Path("/{uuid}")
    public Response delete(@PathParam("uuid") final String uuid) {
        final boolean delete = transactionService.delete(uuid);
        if (delete) {
            return Response.noContent().build();
        }

        throw new NotFoundException("Não encontrei o recurso " + uuid);
    }

    @Operation(description = "API responsável por procurar uma transação financeira")
    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Retorno OK com a transação encontrada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    @GET
    @Path("/{uuid}")
    @Parameters(@Parameter(name = "uuid", in = ParameterIn.PATH, description = "uuid v4 da transação"))
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RequisicaoTransacaoDTO findById(@PathParam("uuid") final String uuid) {
        LOG.info("Procurando transação pelo uuid " + uuid);
        var transacaoDTO = transactionService.find(uuid);
        return transacaoDTO.orElseThrow(() -> new NotFoundException("Não foi possível encontrar a transação"));


    }

    @Operation(description = "API responsável por procurar uma transação financeira")
    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Retorno OK com a transação encontrada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")})
    @PATCH
    @Path("/{uuid}/aprovar")
    @Parameters({@Parameter(name = "uuid", in = ParameterIn.PATH, description = "uuid v4 da transação"),
            @Parameter(name = "x-signature", in = ParameterIn.HEADER, description = "Assinatura da transação")}
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RequisicaoTransacaoDTO aprovar(@PathParam("uuid") final String uuid, @HeaderParam("x-signature") final String signature) {
        LOG.info("Procurando transação pelo uuid " + uuid);
        var transacaoDTO = transactionService.find(uuid);
        return transactionService.aprovarTransacao (transacaoDTO.orElseThrow(()
                        -> new NotFoundException("Não foi possível encontrar a transação")), signature)
                .orElseThrow(() -> new ServerErrorException("Não foi possível atualizar a transação", Response.status(500).build()));


    }


    private RequisicaoTransacaoDTO introspectAccount(final RequisicaoTransacaoDTO requisicaoTransacaoDTO) {

        if (Objects.isNull(conta) || Objects.isNull(agencia)) {
            throw new NotAuthorizedException("O token de autenticação não possui as claims de conta e/ou agencia");
        }
        requisicaoTransacaoDTO.setConta(Conta.of(Long.valueOf(agencia.toString()), Long.valueOf(conta.toString())));
        return requisicaoTransacaoDTO;
    }

}