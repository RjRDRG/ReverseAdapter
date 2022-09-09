package com.rce.reverseadapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import static com.rce.reverseadapter.Utils.forwardRequest;
import static com.rce.reverseadapter.Utils.forwardResponse;

@RestController
public class Controller {

    private final String HOST;
    private final int PORT;

    private final ObjectMapper mapper;

    public Controller() {
        mapper = new ObjectMapper();

        HOST = System.getenv("TARGET_HOST");
        PORT = Integer.parseInt(System.getenv("TARGET_PORT"));
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
                    scheme, HOST, PORT, method, path, pathParams, queryParams, headerParams, body, sendType, receiveType
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
