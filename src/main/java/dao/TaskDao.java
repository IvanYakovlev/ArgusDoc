package dao;

import javafx.collections.ObservableList;
import model.Task;

import java.util.List;

public interface TaskDao {
    public void addTask(Task task);
    public void updateTask(Task task);
    public void removeTask(int id);
    public ObservableList<Task> listTasks(int id);
    public void doneTask(int id);
    public void performedTask(int id);
    public void overdueTask(int id);
    public void canceledTask(int id);


}
