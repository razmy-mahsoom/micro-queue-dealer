package org.microq.dealer.client_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.microq.support.auditor.Chaining;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    private Socket socket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            //broadcastMessage("Server "+ "new" +" has entered the chat");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {

        String messageFromClient;
        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }

    }

    public void broadcastMessage(String messageToSend){
        //System.out.println("Message From Builder - "+messageToSend);
        ObjectMapper mapper = new ObjectMapper();
        Chaining chaining = null;
        try {
            chaining = mapper.readValue(messageToSend, Chaining.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(chaining.getInterchange().getInterchangeName());
        System.out.println(chaining.getSequence().getSequenceName());
        System.out.println(chaining.getPath());
        System.out.println(chaining.getClientType());
    }

    public void removerClientHandler(){
        clientHandlers.remove(this);
        //broadcastMessage("Server " + clientUsername + " has left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removerClientHandler();
        try {

            if(bufferedReader !=null){
                bufferedReader.close();
            }
            if(bufferedWriter !=null){
                bufferedWriter.close();
            }
            if(socket !=null){
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
