package com.hbsis.logging.socket;

import com.hbsis.logging.log.LogStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.EmptyStackException;

@Service
public abstract class SocketServer {

    public static final int SOCKET_PORT = 5501;

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    public static void streamFile() throws IOException, InterruptedException {
        int ioExceptionErrorCount = 0;
        LOGGER.info("Iniciando socket.");
        while (true) {
            try(Socket sock = new Socket("127.0.0.1", SOCKET_PORT)) {
                OutputStream os = sock.getOutputStream();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    sb.append(LogStack.stack.pop());
                }

                LOGGER.info(sb.toString());

                byte[] logBytes = sb.toString().getBytes();
                os.write(logBytes, 0, logBytes.length);
                os.flush();
            } catch (EmptyStackException e) {
                continue;
            } catch (IOException io) {
                if (ioExceptionErrorCount == 3) {
                    LOGGER.error("Erro ao pegar OutputStream da socket. \n{}", io);
                }
                ioExceptionErrorCount++;
                continue;
            }
        }
    }
}