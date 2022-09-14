package com.rce.reverseadapter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

public class Utils {

    public static ResponseEntity<String> forwardRequest(
            WebClient client,
            HttpMethod method,
            String path,
            Map<String, String> pathParams,
            Map<String,String> queryParams,
            Map<String, String> headerParams,
            String body,
            MediaType sendType,
            MediaType receiveType
    ) {
        String pathWithVariables = path;

        for (Map.Entry<String,String> entry : pathParams.entrySet()) {
            pathWithVariables = pathWithVariables.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        UriComponentsBuilder uri = UriComponentsBuilder
                .newInstance()
                .path(pathWithVariables);

        for (Map.Entry<String,String> entry : queryParams.entrySet()) {
            uri.queryParam(entry.getKey(),entry.getValue());
        }

        WebClient.RequestBodySpec requestBodySpec = client
                .method(method)
                .uri(uri.build().toUri());

        for (Map.Entry<String, String> entry : headerParams.entrySet()) {
            requestBodySpec = requestBodySpec.header(entry.getKey(), entry.getValue());
        }

        if(body != null) {
            return requestBodySpec
                    .contentType(sendType)
                    .accept(receiveType)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } else {
            return requestBodySpec
                    .accept(receiveType)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        }
    }

    public static ResponseEntity<String> forwardResponse(int status, HttpHeaders headers, String body) {
        if(body!=null && !headers.isEmpty()) {
            return ResponseEntity.status(status).headers(headers).body(body);
        } else if(body != null) {
            return ResponseEntity.status(status).body(body);
        } else if(!headers.isEmpty()) {
            return ResponseEntity.status(status).headers(headers).build();
        } else {
            return ResponseEntity.status(status).build();
        }
    }

}
