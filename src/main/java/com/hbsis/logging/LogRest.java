package com.hbsis.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogRest {
    Logger logger = LoggerFactory.getLogger(LogRest.class);

    public String log(){
        logger.info("este log é legal");
        return "Retorno";
    }
}
