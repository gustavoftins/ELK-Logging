package com.hbsis.logging.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class LogRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRest.class);

    private final LogService logService;

    public LogRest(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/log")
    public String log(HttpServletRequest r){
        LOGGER.info(r.getRequestURI());
        return "Retorno";
    }

    @GetMapping("/teste/{tag}/{match}")
    public int teste(@PathVariable("tag") String tag, @PathVariable("match") String match) throws IOException {
        return this.logService.getLog(tag, match).size();
    }

    @GetMapping("/count/{tag}/{match}")
    public Long count(@PathVariable("tag") String tag, @PathVariable("match") String match) throws IOException {
        return this.logService.count(tag, match);
    }

}
