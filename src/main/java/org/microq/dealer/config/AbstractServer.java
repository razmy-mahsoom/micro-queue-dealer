package org.microq.dealer.config;

import lombok.extern.slf4j.Slf4j;
import org.microq.dealer.client_test.ClientHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
@Slf4j
public abstract class AbstractServer {

    protected abstract ServerConfiguration getServerConfiguration();
    protected abstract ServerSocket serverSocket();

    @PostConstruct
    public void startServer(){
        log.info("MicroQueue started on port : {}",getServerConfiguration().getServerSocketPort());
        try {
            while (serverSocket().isClosed()){
                System.out.println("waiting for connection");
                Socket socket = serverSocket().accept();
                System.out.println("New Client");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeServerSocket(){
        try{
            if (serverSocket()!=null){
                serverSocket().close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
