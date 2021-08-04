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
import java.rmi.RemoteException;
import java.util.List;

public class ClientRpcWorker implements Runnable, Observer {
    private Service server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientRpcWorker(Service server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            Jucator jucator= (Jucator) request.data();
            try {
                server.login(jucator, this);
                return okResponse;
            } catch (RosuException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            Jucator jucator= (Jucator) request.data();
            try {
                server.logout(jucator, this);
                connected=false;
                return okResponse;
            } catch (RosuException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.START){
            Jucator jucator= (Jucator) request.data();
            System.out.println("StartRequest ...");
            try {
                server.start(jucator);
                return okResponse;
            } catch (RosuException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void notifyNewGame(List<String> readyToPlay, List<Carte> cards) {
//        String ready="";
//        String card="";
//        for(String s:readyToPlay){
//            ready+=s+";";
//        }
////        for(String s:cards){
////            card+=s+";";
////        }
//        DTOStart dtoStart=new DTOStart(ready,card);
//        Response resp=new Response.Builder().type(ResponseType.START).data(dtoStart).build();
//        try{
//            sendResponse(resp);
//        }catch (IOException e){
//            throw new RosuException("Sending error: "+e);
//        }


    }

    @Override
    public void notifyWinner(String a) throws RemoteException {

    }

    @Override
    public void notifyEndGame(String a) throws RemoteException {

    }

}
