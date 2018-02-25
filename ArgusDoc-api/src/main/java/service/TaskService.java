package service;

import javafx.collections.ObservableList;
import entity.Task;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaskService extends Remote {

    public void addTask(Task task) throws IOException, RemoteException;
    public void updateTask(Task task) throws IOException, RemoteException;
    public void removeTask(Task task) throws RemoteException;
    public List<Task> listMyTasks(int id) throws RemoteException;
    public List<Task> listMyDoneTasks(int id) throws RemoteException;
    public List<Task> listFromEmpTasks(String userName) throws RemoteException;
    public List<Task> listArchiveTasks(int idStatus) throws RemoteException;
    public void doneTask(Task task) throws RemoteException;
    public void performedTask(int id) throws RemoteException;
    public void overdueTask(int id) throws RemoteException;
    public void canceledTask(int id) throws RemoteException;
    public void openTaskAttachment(int id) throws RemoteException;
    public void downloadAttachmentFile(int id) throws RemoteException;


}
