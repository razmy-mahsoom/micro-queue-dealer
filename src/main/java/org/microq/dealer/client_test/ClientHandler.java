package org.microq.dealer.client_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.source.doctree.SeeTree;
import org.microq.dealer.queue.InternalInterchange;
import org.microq.dealer.queue.InternalSequence;
import org.microq.support.auditor.Chaining;
import org.microq.support.auditor.Interchange;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable{

    private Socket socket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private Set<InternalInterchange> internalInterchanges = new HashSet<>();


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);
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
        System.out.println(messageToSend);
        ObjectMapper mapper = new ObjectMapper();
        Chaining chaining = null;
        try {
            chaining = mapper.readValue(messageToSend, Chaining.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        InternalInterchange internalInterchange = new InternalInterchange(chaining.getInterchange().getInterchangeName());
        InternalSequence internalSequence = new InternalSequence(chaining.getSequence().getSequenceName());
        internalSequence.setPath(chaining.getPath());
        internalInterchange.getInternalSequences().add(internalSequence);

        internalInterchanges.add(internalInterchange);
        internalInterchanges.forEach(System.out::println);

    }

    public void removerClientHandler(){
        //clientHandlers.remove(this);
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
