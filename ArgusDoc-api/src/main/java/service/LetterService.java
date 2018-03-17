package service;

import javafx.collections.ObservableList;
import entity.Letter;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface LetterService extends Remote {

    public void addLetter(Letter letter) throws IOException, RemoteException, SQLException;
    public void updateLetter(Letter letter) throws IOException, RemoteException, SQLException;
    public void removeLetter(int id, String filePath) throws IOException, SQLException;
    public void downloadLetter(Letter letter) throws RemoteException;
    public void openLetter(int id) throws IOException, SQLException;
    public List<Letter> listLetter() throws RemoteException;
    public Letter getLetterById(int id) throws RemoteException;

}
