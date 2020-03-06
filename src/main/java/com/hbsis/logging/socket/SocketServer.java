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

    public static void streamFile() {
        int ioExceptionErrorCount = 0;

        try (Socket sock = new Socket("127.0.0.1", SOCKET_PORT)) {
            LOGGER.info("Iniciando socket.");
            while (true) {
                try (OutputStream os = sock.getOutputStream()) {
                    byte[] logBytes = LogStack.stack.pop().getBytes();
                    os.write(logBytes, 0, logBytes.length);
                    os.flush();
                } catch (EmptyStackException e) {
                    continue;
                }catch (IOException io){
                    if(ioExceptionErrorCount == 3){LOGGER.error("Erro ao pegar OutputStream da socket. \n{}", io); break;}
                    ioExceptionErrorCount++;
                    Thread.sleep(1000);
                    continue;
                }
            }
        }catch (Exception e){
            LOGGER.error("Erro de socket {}", e);
        }
    }
}