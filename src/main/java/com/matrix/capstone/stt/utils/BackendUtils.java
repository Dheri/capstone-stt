package com.matrix.capstone.stt.utils;

import com.matrix.capstone.stt.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

public class BackendUtils {
    private Environment environment;
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    public void callback(String convertedText, String narrationId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("narrationId", narrationId);
        body.add("convertedText", convertedText);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String  callbackUrl = environment.getProperty("spring.application.backend-url")+"/validationCallback";
        log.debug(String.format("callback address -> %s", callbackUrl));
        log.debug(String.format("requestEntity  -> %s", requestEntity.toString() ));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(callbackUrl, requestEntity, String.class);
        log.debug(String.format("response StatusCode -> %s", response.getStatusCode().toString() ));
    }

    public BackendUtils(Environment environment) {
        this.environment = environment;
    }
}
