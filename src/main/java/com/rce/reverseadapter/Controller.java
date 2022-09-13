package com.rce.reverseadapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rce.reverseadapter.Utils.forwardRequest;
import static com.rce.reverseadapter.Utils.forwardResponse;

@RestController
public class Controller {

    private final ObjectMapper mapper;
    final WebClient target;

    public Controller() {
        mapper = new ObjectMapper();

        String host = System.getenv("TARGET_HOST");
        String port = System.getenv("TARGET_PORT");

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);

        WebClient.Builder builder = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));

        target = builder.baseUrl("http://" + host + ":" + port).build();
    }

    @RequestMapping(
            value = "/v0/demo",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> procedure0(@PathVariable Map<String, String> _pathParams,
                                             @RequestParam Map<String,String> _queryParams,
                                             @RequestHeader Map<String, String> _headerParams,
                                             @RequestBody String _rawBody) {
        try {
            JsonNode _body = mapper.readTree(_rawBody);

            String scheme = "http";

            HttpMethod method = HttpMethod.valueOf("POST");

            String path = "/v1/demo";

            Map<String, String> pathParams = new HashMap<>();


            Map<String,String> queryParams = new HashMap<>();


            Map<String, String> headerParams = new HashMap<>();
            headerParams.put("maxcalls", _headerParams.get("maxcalls"));headerParams.put("calls", _headerParams.get("calls"));headerParams.put("fanout", _headerParams.get("fanout"));

            String body = "{\"p1_v1\":\"" + _body.get("p1_v0").textValue() + "\",\"p2_v1\":\"" + _body.get("p2_v0").textValue() + "\",\"p3_v1\":\"" + _body.get("p3_v0").textValue() + "\",\"p4_v1\":\"" + _body.get("p4_v0").textValue() + "\",\"p5_v1\":\"" + _body.get("p5_v0").textValue() + "\"}";

            MediaType sendType = MediaType.APPLICATION_JSON;

            MediaType receiveType = MediaType.APPLICATION_JSON;

            ResponseEntity<String> responseEntity = forwardRequest(
                    target, method, path, pathParams, queryParams, headerParams, body, sendType, receiveType
            );

            HttpStatus status = responseEntity.getStatusCode();
            _headerParams = responseEntity.getHeaders().toSingleValueMap();
            _body = mapper.readTree(responseEntity.getBody());

            if(status.value() == 200) {
                HttpHeaders responseHeaders = new HttpHeaders();

                String responseBody = "{\"p1_v0\":\"" + _body.get("p1_v1").textValue() + "\",\"p2_v0\":\"" + _body.get("p2_v1").textValue() + "\",\"p3_v0\":\"" + _body.get("p3_v1").textValue() + "\",\"p4_v0\":\"" + _body.get("p4_v1").textValue() + "\",\"p5_v0\":\"" + _body.get("p5_v1").textValue() + "\"}";
                return forwardResponse(200, responseHeaders, responseBody);
            };
            return ResponseEntity.internalServerError().body("UNMAPPED RESPONSE");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }




}
