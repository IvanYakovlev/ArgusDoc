package service;

import javafx.collections.ObservableList;
import entity.Letter;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface LetterService extends Remote {

    public void addLetter(Letter letter) throws RemoteException;
    public void updateLetter(Letter letter) throws  RemoteException;
    public void updateJuristLetter(Letter letter) throws RemoteException;
    public void updateOripLetter(Letter letter) throws RemoteException;
    public void updateTechnicalLetter(Letter letter) throws RemoteException;
    public void updateBookkeepingLetter(Letter letter) throws RemoteException;


    public void removeLetter(int id) throws RemoteException;
    public List<Letter> listLetter() throws RemoteException;
    public Letter getLetterById(int id) throws RemoteException;
    public int getMaxId() throws RemoteException;
}
