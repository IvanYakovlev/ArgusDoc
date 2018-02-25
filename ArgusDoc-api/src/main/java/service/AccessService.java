package service;

import javafx.collections.ObservableList;
import entity.Access;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AccessService extends Remote{

    public void addAccess() throws RemoteException;
    public void updateAccess() throws RemoteException;
    public void removeAccess() throws RemoteException;
    public List<Access> listAccess() throws RemoteException;
    public List<String> listAccessName() throws RemoteException;
    public int getIdAccessByName(String value) throws RemoteException;

}
