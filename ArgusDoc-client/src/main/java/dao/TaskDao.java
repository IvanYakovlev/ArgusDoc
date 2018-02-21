package dao;

import javafx.collections.ObservableList;
import model.Task;

import java.io.IOException;
import java.util.List;

public interface TaskDao {
    public void addTask(Task task) throws IOException;
    public void updateTask(Task task) throws IOException;
    public void removeTask(Task task);
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
