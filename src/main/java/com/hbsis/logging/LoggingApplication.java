package com.hbsis.logging;

import com.hbsis.logging.socket.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class LoggingApplication {

	public static void main(String[] args) {

		SpringApplication.run(LoggingApplication.class, args);
	}

}
