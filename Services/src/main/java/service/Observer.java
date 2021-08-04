package service;

import domain.Carte;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Observer extends Remote {
    void notifyNewGame(List<String> readyToPlay, List<Carte> cards) throws RemoteException;

    void notifyWinner(String a) throws RemoteException;

    void notifyEndGame(String a) throws RemoteException;

}
