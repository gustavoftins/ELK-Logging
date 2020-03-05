package com.hbsis.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

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
        WebClient web = WebClient.create("http://localhost:9200");
        WebClient.RequestBodySpec req = web.method(HttpMethod.GET).uri("/logapp/_search?q=tag:log");

        String response2 = req.exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        System.out.println(response2);
    }

}
