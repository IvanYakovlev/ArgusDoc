package service;

import entity.TaskEntity;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface TaskService extends Remote {

    public void addTask(TaskEntity taskEntity) throws IOException, RemoteException, SQLException;
    public void updateTask(TaskEntity taskEntity) throws IOException, RemoteException, SQLException;
    public void removeTask(TaskEntity taskEntity) throws RemoteException, SQLException;
    public List<TaskEntity> listMyTasks(int id) throws RemoteException;
    public List<TaskEntity> listMyLetterTasks(int id) throws RemoteException;
    public List<TaskEntity> listMyDoneTasks(int id) throws RemoteException;
    public List<TaskEntity> listFromEmpTasks(String userName) throws RemoteException;
    public List<TaskEntity> listArchiveTasks(int idStatus) throws RemoteException;
    public void doneTask(TaskEntity taskEntity) throws RemoteException, SQLException;
    public void performedTask(int id) throws RemoteException;
    public void overdueTask(int id) throws RemoteException;
    public void canceledTask(int id) throws RemoteException;
    public void openTaskAttachment(int id) throws IOException;
    public void downloadAttachmentFile(int id) throws RemoteException;


}
