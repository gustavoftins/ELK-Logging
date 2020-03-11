package com.hbsis.logging.log;

import com.hbsis.logging.socket.SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class LogRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRest.class);

    private final LogService logService;

    private final SocketServer socketServer;

    public LogRest(LogService logService, SocketServer socketServer) {
        this.logService = logService;
        this.socketServer = socketServer;
    }

    @GetMapping("/getLogs")
    public void getLog(){
        this.logService.getLog();
    }

    @GetMapping("/startStreaming")
    public void start() {
        socketServer.streamFile();
    }

    @GetMapping("/getLogs/{scrollId}")
    public void getLog(@PathVariable("scrollId") String scrollId){
        this.logService.getLogFromScrollId(scrollId);
    }
}
