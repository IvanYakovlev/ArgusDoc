package service;

import entity.TaskEntity;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface TaskService extends Remote {

    public void addTask(TaskEntity taskEntity) throws RemoteException;
    public void updateTask(TaskEntity taskEntity) throws RemoteException;
    public void removeTask(TaskEntity taskEntity) throws RemoteException;
    public List<TaskEntity> listMyTasks(int id) throws RemoteException;
    public List<TaskEntity> listMyLetterTasks(int id) throws RemoteException;
    public List<TaskEntity> listMyDoneTasks(int id) throws RemoteException;
    public List<TaskEntity> listFromEmpTasks(String userName) throws RemoteException;
    public List<TaskEntity> listArchiveTasks(int idStatus) throws RemoteException;
    public void doneTask(TaskEntity taskEntity) throws RemoteException;
    public void performedTask(int id) throws RemoteException;
    public void overdueTask(int id) throws RemoteException;
    public void canceledTask(int id) throws RemoteException;



}
