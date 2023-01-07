package br.com.coffeeandit;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.logging.Logger;

@Named("ValidarCPF")
@ApplicationScoped
public class ValidarCPF implements HttpFunction {

    private static final Logger logger = Logger.getLogger(ValidarCPF.class.getName());
    private static final Gson gson = new Gson();


    private CPFService cpfService = new CPFService();

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {

        var cpf = getRequestCPF(httpRequest);
        var cpfDto = new CPFDto();
        cpfDto.setCpf(cpf);
        cpfDto.setValid(cpfService.isCPFValido(cpf));
        var writer = new PrintWriter(httpResponse.getWriter());
        writer.printf(gson.toJson(cpfDto));
        httpResponse.setContentType(MediaType.APPLICATION_JSON);
    }

    private String getRequestCPF(HttpRequest httpRequest) throws IOException {
        var cpf = httpRequest.getFirstQueryParameter("cpf").orElse("0");

        try {
            var requestParsed = gson.fromJson(httpRequest.getReader(), JsonElement.class);
            JsonObject requestJson = null;

            if (requestParsed != null && requestParsed.isJsonObject()) {
                requestJson = requestParsed.getAsJsonObject();
            }

            if (requestJson != null && requestJson.has("cpf")) {
                cpf = requestJson.get("cpf").getAsString();
            }
        } catch (JsonParseException e) {
            logger.severe("Error parsing JSON: " + e.getMessage());
        }
        return cpf;
    }




}