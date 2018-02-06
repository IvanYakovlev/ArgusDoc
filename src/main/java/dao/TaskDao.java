package dao;

import javafx.collections.ObservableList;
import model.Task;

import java.util.List;

public interface TaskDao {
    public void addTask(Task task);
    public void updateTask(Task task);
    public void removeTask(int id);
    public ObservableList<Task> listMyTasks(int id);
    public ObservableList<Task> listMyDoneTasks(int id);
    public ObservableList<Task> listFromEmpTasks(String userName);
    public ObservableList<Task> listArchiveTasks(int idStatus);
    public void doneTask(Task task);
    public void performedTask(int id);
    public void overdueTask(int id);
    public void canceledTask(int id);
    public void openTaskAttachment(int id);
    public void downloadAttachmentFile(int id);


}
