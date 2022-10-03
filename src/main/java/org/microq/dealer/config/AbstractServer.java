package org.microq.dealer.config;

import lombok.extern.slf4j.Slf4j;

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
        log.info("Starting ServerSocket on : {}",getServerConfiguration().getServerSocketPort());
        try {
            while (serverSocket().isClosed()){
                Socket socket = serverSocket().accept();
                System.out.println("New Client");
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
