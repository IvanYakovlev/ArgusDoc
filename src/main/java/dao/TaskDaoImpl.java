package dao;


import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.StatusTask;
import model.Task;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TaskDaoImpl implements TaskDao{
DBconnection dBconnection;
    public void addTask(Task task) {
        dBconnection=new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO TASKS(Task_name, Task_text, Task_attachment, Task_from_employee, Employee_id, Task_term, Status_task_id) VALUES(?,?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setString(4, task.getTaskFromEmployee());
            preparedStatement.setInt(5, task.getEmployeeId());
            preparedStatement.setDate(6, task.getTaskTerm());
            preparedStatement.setInt(7, StatusTask.NOT_DONE);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Task_name=?, Task_text=?, Task_attachment=?, Task_from_employee=?, Employee_id=?, Task_term=?, Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setString(4, task.getTaskFromEmployee());
            preparedStatement.setInt(5, task.getEmployeeId());
            preparedStatement.setDate(6,task.getTaskTerm());
            preparedStatement.setInt(7, task.getStatusTaskId());
            preparedStatement.setInt(7, task.getTaskId());
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

    public ObservableList<Task> listTasks() {
        this.dBconnection = new DBconnection();
        ObservableList<Task> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id");
            while (resultSet.next()){
                Task task= new Task();
                task.setTaskId(resultSet.getInt("Task_id"));
                task.setTaskName(resultSet.getString("Task_name"));
                task.setTaskText(resultSet.getString("Task_text"));
                task.setTaskAttachment(resultSet.getString("Task_attachment"));
                task.setEmployeeId(resultSet.getInt("Employee_id"));
                task.setEmployeeName(resultSet.getString("Employee_name"));
                task.setTaskTerm(resultSet.getDate("Task_term"));
                task.setStatusTaskName(resultSet.getString("Status_task_name"));
                listData.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public void doneTask(int id) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, StatusTask.DONE);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performedTask(int id) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, StatusTask.PERFORMED);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void overdueTask(int id) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, StatusTask.OVERDUE);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void canceledTask(int id) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, StatusTask.CANCELED);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
