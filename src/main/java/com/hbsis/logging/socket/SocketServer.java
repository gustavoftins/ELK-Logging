package com.hbsis.logging.socket;

import com.hbsis.logging.log.LogStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.Socket;

@Service
public class SocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);
    @Value("${receiver.host:127.0.0.1}")
    public String SOCKET_HOST;
    @Value("${receveir.port:5501}")
    public int SOCKET_PORT;

    public void streamFile() {
        while (true) {
            if(LogStack.stack.isEmpty() || LogStack.stack.size() < 10000){
                continue;
            }
            try (Socket sock = new Socket(SOCKET_HOST, SOCKET_PORT); OutputStream os = sock.getOutputStream()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 10000; i++) {
                    sb.append(LogStack.stack.pop()).append("\n");
                }
                byte[] logBytes = sb.toString().getBytes();

                LOGGER.info("enviando pacote de {} bytes", logBytes.length);

                os.write(logBytes, 0, logBytes.length);
                os.flush();
            } catch (Exception e) {
                continue;
            }
        }
    }
}