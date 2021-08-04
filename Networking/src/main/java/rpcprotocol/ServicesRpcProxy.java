package rpcprotocol;

import domain.Carte;
import domain.Jucator;
import dto.DTOStart;
import service.Observer;
import service.RosuException;
import service.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements Service {
    private String host;
    private int port;

    private Observer client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws RosuException {
        try {
            output.writeObject(request);
            output.flush();


        } catch (IOException e) {
            throw new RosuException("Error sending object " + e);
        }

    }

    private Response readResponse() throws RosuException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws RosuException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    @Override
    public void login(Jucator user, Observer client) {
        initializeConnection();
        Request req=new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()==ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new RosuException(err);
        }
    }

    @Override
    public void logout(Jucator user, Observer client) {
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new RosuException(err);
        }
    }

    @Override
    public void start(Jucator player) {
        Request req=new Request.Builder().type(RequestType.START).data(player).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new RosuException(err);
        }

    }

    @Override
    public void joacaRunda(Jucator player, String carte) {

    }

    private void handleUpdate(Response response) {
        if (response.type()== ResponseType.START){
//            DTOStart dtoStart= (DTOStart) response.data();
            List<String> players=new ArrayList<>();
            List<Carte> cards=new ArrayList<>();
//
//            for(String player:dtoStart.getReadyToPlay().split(";")){
//                players.add(player);
//            }
//            for(String card:dtoStart.getCards().split(";")){
//                cards.add(card);
//            }
            try {
                System.out.println("IN HANDLE START");
                client.notifyNewGame(players, cards);
            } catch (RosuException e) {
                e.printStackTrace();
            }
        }
   }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.START;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
