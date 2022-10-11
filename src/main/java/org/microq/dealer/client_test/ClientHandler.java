package org.microq.dealer.client_test;

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
            broadcastMessage("Server "+ "new" +" has entered the chat");
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
//        for (ClientHandler clientHandler:clientHandlers){
//            try{
//                if(!clientHandler.clientUsername.equals(clientUsername)){
//                    clientHandler.bufferedWriter.write(messageToSend);
//                    clientHandler.bufferedWriter.newLine();
//                    clientHandler.bufferedWriter.flush();;
//                }
//            }catch (IOException e){
//                closeEverything(socket,bufferedReader,bufferedWriter);
//            }
//        }
    }

    public void removerClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server " + clientUsername + " has left the chat");
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
