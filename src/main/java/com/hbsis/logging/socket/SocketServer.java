package com.hbsis.logging.socket;

import com.hbsis.logging.log.LogStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.Socket;

@Service
public abstract class SocketServer {

    @Value("${receiver.host:127.0.0.1}")
    public static final String SOCKET_HOST = "";
    @Value("${receveir.port:5501}")
    public static final int SOCKET_PORT = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    public static void streamFile() {
        while (LogStack.stack.size() > 10) {
            try (Socket sock = new Socket(SOCKET_HOST, SOCKET_PORT); OutputStream os = sock.getOutputStream()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    sb.append(LogStack.stack.pop());
                }
                byte[] logBytes = sb.toString().getBytes();
                os.write(logBytes, 0, logBytes.length);
                os.flush();
            } catch (Exception e) {
                continue;
            }
        }
    }
}