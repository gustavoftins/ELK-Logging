package com.hbsis.logging.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class LogRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRest.class);

    @Autowired
    private LogService logservice;

    @GetMapping("/log")
    public String log(HttpServletRequest r){
        LOGGER.info(r.getRequestURI());
        return "Retorno";
    }

    @GetMapping("/teste")
    public int teste() throws IOException, IllegalAccessException {
        return this.logservice.getLog().size();
    }

}
