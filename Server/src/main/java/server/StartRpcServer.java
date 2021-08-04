package server;

import domain.Jucator;
import rosu.persistence.JocRepository;
import rosu.persistence.RundaRepository;
import rosu.persistence.JucatorRepository;
import rosu.persistence.repository.JucatorRepositoryJDBC;
import rosu.persistence.repository.JocRepositoryJDBC;
import rosu.persistence.repository.RundaRepositoryJDBC;
import service.Service;
//import utils.AbstractServer;
//import utils.RpcConcurrentServer;
//import utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
//    private static int defaultPort=55555;
//
//    public static void main(String[] args) {
//        Properties serverProps=new Properties();
//        try {
//            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
//            System.out.println("Server properties set. ");
//            serverProps.list(System.out);
//        } catch (IOException e) {
//            System.err.println("Cannot find server.properties "+e);
//            return;
//        }
//        JucatorRepository jucatorRepository=new JucatorRepositoryJDBC(serverProps);
//        JocRepository jocRepository=new JocRepositoryJDBC(serverProps);
//        RundaRepository rundaRepository =new RundaRepositoryJDBC(serverProps);
//        Service serverImpl=new ServicesImpl(jocRepository,rundaRepository,jucatorRepository);
//
//        int serverPort=defaultPort;
//        try {
//            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
//        }catch (NumberFormatException nef){
//            System.err.println("Wrong  Port Number"+nef.getMessage());
//            System.err.println("Using default port "+defaultPort);
//        }
//        System.out.println("Starting server on port: "+serverPort);
//        AbstractServer server = new RpcConcurrentServer(serverPort, serverImpl);
//        try {
//            server.start();
//        } catch (ServerException e) {
//            System.err.println("Error starting the server" + e.getMessage());
//        }finally {
//            try {
//                server.stop();
//            }catch(ServerException e){
//                System.err.println("Error stopping server "+e.getMessage());
//            }
//        }
//    }
}
