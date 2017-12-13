package dao;

import model.Task;

import java.util.List;

public interface TaskDao {
    public void addTask(Task task);
    public void updateTask(Task task);
    public void removeTask(int id);
    public Task getTaskById(int id);
    public List<Task> listTasks();

}
