package service;

import javafx.collections.ObservableList;
import entity.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface EventService extends Remote {

    public void addEvent(Event event) throws RemoteException, SQLException;
    public void removeEvent(Event event) throws RemoteException, SQLException;
    public List<Event> listSelectedDayEvent(int authUserId, java.sql.Date selectedDate) throws RemoteException;
    public List<Event> listAllEvent(int authUserId) throws RemoteException;

}
