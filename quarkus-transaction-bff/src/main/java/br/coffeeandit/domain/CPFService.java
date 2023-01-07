package br.coffeeandit.domain;

import br.coffeeandit.api.dto.CpfDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/httpFunction")
@RegisterRestClient
public interface CPFService {

    @GET
    CpfDto validarCPF(@QueryParam("cpf") final String cpf);
}
