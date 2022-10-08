package org.microq.dealer;

import org.microq.dealer.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class MicroQueueDealerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MicroQueueDealerApplication.class, args);
    }
}
