package org.microq.dealer.config.impl;

import lombok.extern.slf4j.Slf4j;
import org.microq.dealer.client_test.ClientHandler;
import org.microq.dealer.config.AbstractServer;
import org.microq.dealer.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@Slf4j
public class DefaultMicroQueueServer extends AbstractServer {

    @Autowired
    private ServerConfiguration serverConfiguration;

    @Override
    protected ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

    @PostConstruct
    @Override
    protected ServerSocket serverSocket() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverConfiguration.getServerSocketPort());
            log.info("Socket info : {}",serverSocket.getLocalSocketAddress().toString());
            log.info("Server hostname  :{}",serverSocket);
            while (!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept();
                log.info("new client connected info {}",clientSocket.getLocalSocketAddress().toString());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serverSocket;
    }



}
