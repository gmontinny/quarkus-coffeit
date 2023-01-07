package br.com.coffeeandit.api.v1;

import br.com.coffeeandit.dto.AlteracaoSituacaoDTO;
import br.com.coffeeandit.dto.TransacaoDTO;
import br.com.coffeeandit.model.Transacao;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.smallrye.mutiny.Multi;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/v1/transactions")
public class TransacaoAPI {


    private Logger log = LoggerFactory.getLogger(TransacaoAPI.class);

    @Inject
    @Channel("liquidacao_sse")
    private Multi<TransacaoDTO> transactions;

    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Retorno para alteração de uma transação"),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")
    })
    @Operation(description = "API para alterar a situação de transação financeira")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam(value = "id") String uuid, @RequestBody AlteracaoSituacaoDTO alteracaoSituacaoDTO) {

        var item = Transacao.findByUuid(UUID.fromString(uuid));
        if (Objects.nonNull(item)) {
            log.info("Transação recuperada para atualização %s ", item);
            item.setSituacao(alteracaoSituacaoDTO.getSituacao());
            log.info("Situação da Transação alterada %s ", item);
            item.update();
            return Response.status(204).build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(),
                String.format("Não foi possível alterar a transação %s", uuid)).build();

    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<TransacaoDTO> query(@NotNull @QueryParam("conta") Long codigoConta,
                                     @NotNull @QueryParam("agencia") Long codigoAgencia) {

        return Multi.createBy().repeating()
                .supplier(AtomicInteger::new,
                        atomicInteger -> Transacao.findByAgenciaConta(codigoAgencia, codigoConta)
                                .parallelStream().map(transacao -> transacao.convert())
                                .collect(Collectors.toList())
                ).until(List::isEmpty)
                .onItem()
                .transformToMultiAndConcatenate(transacaoDTOS -> Multi.createFrom().iterable(transacaoDTOS));

    }

    @GET
    @Path("/sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<TransacaoDTO> stream(@NotNull @QueryParam("conta") Long codigoConta,
                                      @NotNull @QueryParam("agencia") Long codigoAgencia
    ) {
        return transactions.filter(transacaoDTO -> codigoConta.equals(transacaoDTO.getConta().getCodigoConta())
                && codigoAgencia.equals(transacaoDTO.getConta().getCodigoAgencia()));
    }

    @DELETE
    @Path(value = "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") String uuid, @HeaderParam(value = "content-type") String contentType) {
        Transacao.deleteById(uuid);
        return Response.status(HttpResponseStatus.NO_CONTENT.code()).build();
    }


    @GET
    @Path(value = "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TransacaoDTO findById(@PathParam("id") String uuid, @HeaderParam(value = "content-type") String contentType) {
        var item = Transacao.findByUuid(UUID.fromString(uuid));
        if (Objects.nonNull(item)) {
            return item.convert();
        }
        throw new ResourceNotFoundException("Item não encontrado");
    }
}
