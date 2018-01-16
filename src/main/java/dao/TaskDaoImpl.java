package dao;


import dbConnection.DBconnection;
import model.Task;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TaskDaoImpl implements TaskDao{
DBconnection dBconnection;
    public void addTask(Task task) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO TASKS(Task_name, Task_text, Task_attachment, Task_from_employee, Employee_id, Task_term) VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setString(4, task.getTaskFromEmployee());
            preparedStatement.setInt(5, task.getEmployeeID());
            preparedStatement.setDate(6, (Date) task.getTaskTerm());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Task_name=?, Task_text=?, Task_attachment=?, Task_from_employee=?, Employee_id=?, Task_term=? WHERE  Task_id =?");
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setString(4, task.getTaskFromEmployee());
            preparedStatement.setInt(5, task.getEmployeeID());
            preparedStatement.setDate(6, (Date) task.getTaskTerm());
            preparedStatement.setInt(7, task.getTaskID());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTask(int id) {
        dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE  FROM  TASKS WHERE Task_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Task getTaskById(int id) {
        return null;
    }

    public List<Task> listTasks() {
        return null;
    }

    @Override
    public void completeTask(int id) {

    }
}
