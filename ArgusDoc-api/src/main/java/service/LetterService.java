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
    public void updateJuristLetter(Letter letter) throws IOException, RemoteException, SQLException;
    public void updateOripLetter(Letter letter) throws IOException, RemoteException, SQLException;
    public void updateTechnicalLetter(Letter letter) throws IOException, RemoteException, SQLException;
    public void updateBookkeepingLetter(Letter letter) throws IOException, RemoteException, SQLException;


    public void removeLetter(int id, String filePath) throws IOException, SQLException;
    public List<Letter> listLetter() throws RemoteException;
    public Letter getLetterById(int id) throws RemoteException;
    public int getMaxId() throws RemoteException, SQLException;
}
