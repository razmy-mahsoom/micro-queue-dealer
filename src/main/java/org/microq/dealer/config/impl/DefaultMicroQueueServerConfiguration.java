package org.microq.dealer.config.impl;

import org.microq.dealer.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultMicroQueueServerConfiguration implements ServerConfiguration {

    @Value("${app.configurations.server-configurations.server-socket-port}")
    private int serverSocketPort;

    @Override
    public int getServerSocketPort() {
        return serverSocketPort;
    }
}
