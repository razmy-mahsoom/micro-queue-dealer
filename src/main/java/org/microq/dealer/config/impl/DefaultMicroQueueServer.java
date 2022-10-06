package org.microq.dealer.config.impl;

import lombok.extern.slf4j.Slf4j;
import org.microq.dealer.config.AbstractServer;
import org.microq.dealer.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
@Component
@Slf4j
public class DefaultMicroQueueServer extends AbstractServer {

    @Autowired
    private ServerConfiguration serverConfiguration;

    @Override
    protected ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

    @Override
    protected ServerSocket serverSocket() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(serverConfiguration.getServerSocketPort());
            log.info("Socket info : {}",socket.getLocalSocketAddress().toString());
            log.info("Server hostname  :{}",socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return socket;
    }


}
