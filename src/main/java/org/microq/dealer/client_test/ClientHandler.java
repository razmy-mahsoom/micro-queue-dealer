package org.microq.dealer.client_test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.microq.dealer.componenet.InterchangeCollection;
import org.microq.dealer.queue.InternalInterchange;
import org.microq.dealer.queue.InternalSequence;
import org.microq.support.annotations.ClientType;
import org.microq.support.annotations.MessageType;
import org.microq.support.auditor.Chaining;
import org.microq.support.auditor.MQMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

@Slf4j
public class ClientHandler implements Runnable{

    private Socket socket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
//    @Autowired
//    private ObjectMapper mapper;

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
        String incomingJsonPayload;
        while (socket.isConnected()){
            try{
                incomingJsonPayload = bufferedReader.readLine();
                processIncomingPayload(incomingJsonPayload);
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }

    }

    public void processIncomingPayload(String jsonPayload){
        System.out.println(jsonPayload);

        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MQMessage message = null;
        try {
            message = mapper.readValue(jsonPayload,MQMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(message.getMessageType().equals(MessageType.CHAIN_CONFIG)){
            //TODO: delegate to a chain config handler
            String json = null;
            try {
                json = mapper.writeValueAsString(message.getPayload());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            Chaining chaining = null;
            try {
                 chaining = mapper.readValue(json,Chaining.class);
                System.out.println(chaining.getSequence());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            InternalInterchange internalInterchange = new InternalInterchange(chaining.getInterchange().getInterchangeName());
            InternalSequence internalSequence = new InternalSequence(chaining.getSequence().getSequenceName());
            internalSequence.setPath(chaining.getPath());
            internalInterchange.getInternalSequences().add(internalSequence);
            InterchangeCollection.internalInterchangeSet.add(internalInterchange);
        }else if(message.getMessageType().equals(MessageType.PAYLOAD)){
            //TODO: delegate to a payload handler
            System.out.println("This is a payload from auditor");
        }





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
