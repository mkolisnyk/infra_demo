package com.sample.it.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientCore {
    public <REQ, RES> RES post(String endpoint, REQ input, Class<RES> outType) throws Exception {
        String json = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(input);

        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return new ObjectMapper().reader().readValue(response.body().toString(), outType);
    }

    public <RES> RES get(String endpoint, Class<RES> outType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return new ObjectMapper().reader().readValue(response.body().toString(), outType);
    }
}
