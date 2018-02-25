package service;

import javafx.collections.ObservableList;
import entity.Letter;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LetterService extends Remote {

    public void addLetter(Letter letter) throws IOException, RemoteException;
    public void removeLetter(int id, String filePath) throws RemoteException;
    public void downloadLetter(Letter letter) throws RemoteException;
    public void openLetter(int id) throws RemoteException;
    public List<Letter> listLetter() throws RemoteException;

}
