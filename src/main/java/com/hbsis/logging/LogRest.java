package com.hbsis.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class LogRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRest.class);

    @GetMapping("/log")
    public String log(HttpServletRequest r){
        LOGGER.info(r.getRequestURI());
        return "Retorno";
    }

    @GetMapping("/teste")
    public void testeElastic(){
        System.out.println("entrou");

        WebClient web = WebClient.create("http://localhost:9200");
        WebClient.RequestBodySpec req = web.method(HttpMethod.GET).uri("/logapp/_search?q=logmessage:log");

        String response2 = req.exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper obm = new ObjectMapper();

        try {
            hits responseDes = obm.readValue(response2, hits.class);

            System.out.println(responseDes.hits.size());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
