package org.microq.dealer.config.impl;

import org.microq.dealer.config.AbstractServer;
import org.microq.dealer.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
@Component
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return socket;
    }
}
