package br.com.coffeeandit.api;

import br.com.coffeeandit.model.FileObject;
import br.com.coffeeandit.business.S3Service;
import br.com.coffeeandit.model.InputData;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/pessoas")
public class DocumentResource {

    @Inject
    S3Service s3Service;

    @APIResponses(value = {
            @APIResponse(responseCode = "204", description ="Retorno para inclusão do documento"),
            @APIResponse(responseCode = "401", description ="Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description ="Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description ="Recurso não encontrado")
    })
    @Operation(description = "API para envio de documentos do correntista")
    @POST
    @Path("/documentos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<Response> uploadFile(@MultipartForm InputData formData) throws Exception {

        if (formData.fileName == null || formData.fileName.isEmpty()) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        if (formData.mimeType == null || formData.mimeType.isEmpty()) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        return Uni.createFrom()
                .completionStage(() -> {
                    return s3Service.putObject(formData);
                })
                .onItem().ignore().andSwitchTo(Uni.createFrom().item(Response.created(null).build()))
                .onFailure().recoverWithItem(th -> {
                    th.printStackTrace();
                    return Response.serverError().build();
                });
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Retorno do documento"),
            @APIResponse(responseCode = "401", description ="Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description ="Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description ="Recurso não encontrado")
    })
    @Operation(description = "API para busca de documentos do correntista")
    @GET
    @Path("/documentos/{objectKey}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> downloadFile(@PathParam("objectKey") String objectKey) throws Exception {
        File tempFile = s3Service.tempFilePath();

        return Uni.createFrom()
                .completionStage(() -> s3Service.getObject(objectKey, tempFile))
                .onItem()
                .transform(object -> Response.ok(tempFile)
                        .header("Content-Disposition", "attachment;filename=" + objectKey)
                        .header("Content-Type", object.contentType()).build());
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "204", description ="Retorno para inclusão do documento"),
            @APIResponse(responseCode = "401", description ="Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description ="Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description ="Recurso não encontrado")
    })
    @Operation(description = "API para envio de documentos do correntista")
    @GET
    @Path("/documentos/")
    public Uni<List<FileObject>> listFiles() {
        return Uni.createFrom().completionStage(() -> s3Service.listObjects())
                .onItem().transform(result -> toFileItems(result));
    }

    private List<FileObject> toFileItems(ListObjectsResponse objects) {
        return objects.contents().stream()
                .sorted(Comparator.comparing(S3Object::lastModified).reversed())
                .map(FileObject::from).collect(Collectors.toList());
    }
}