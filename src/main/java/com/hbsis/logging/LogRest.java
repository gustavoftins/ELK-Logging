package com.hbsis.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRest.class);

    @GetMapping("/log")
    public String log(){
        LOGGER.info("este log é legal");
        return "Retorno";
    }
}
