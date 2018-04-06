package service;


import dbConnection.DBconnection;
import dialog.ADInfo;
import entity.TaskEntity;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import entity.StatusTask;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    public void addTask(TaskEntity taskEntity) throws RemoteException {

//Добавляем данные в таблицу
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO TASKS(Task_name, Task_text, Task_attachment, Task_from_employee," +
                " Employee_id, Task_term, Status_task_id, Task_time,Task_is_letter,Letter_id) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, taskEntity.getTaskName());
            preparedStatement.setString(2, taskEntity.getTaskText());
            preparedStatement.setString(3, taskEntity.getTaskAttachment());
            preparedStatement.setString(4, taskEntity.getTaskFromEmployee());
            preparedStatement.setInt(5, taskEntity.getEmployeeId());
            preparedStatement.setDate(6, taskEntity.getTaskTerm());
            preparedStatement.setInt(7, Integer.parseInt(StatusTask.NOT_DONE));
            preparedStatement.setTime(8, taskEntity.getTaskTime());
            preparedStatement.setInt(9, taskEntity.getTaskIsLetter());
            if (taskEntity.getLetterId()==0) {
                preparedStatement.setNull(10, java.sql.Types.INTEGER);
            }else {
                preparedStatement.setInt(10, taskEntity.getLetterId());
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void updateTask(TaskEntity taskEntity) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE TASKS SET Task_name=?, Task_text=?, Task_attachment=?, Task_from_employee=?, " +
                "Employee_id=?, Task_term=?, Status_task_id=?, Task_time=?,Task_is_letter=?,Letter_id=? " +
                "WHERE  Task_id =?";

        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, taskEntity.getTaskName());
            preparedStatement.setString(2, taskEntity.getTaskText());
            preparedStatement.setString(3, taskEntity.getTaskAttachment());
            preparedStatement.setString(4, taskEntity.getTaskFromEmployee());
            preparedStatement.setInt(5, taskEntity.getEmployeeId());
            preparedStatement.setDate(6, taskEntity.getTaskTerm());
            preparedStatement.setString(7, taskEntity.getStatusTaskId());
            preparedStatement.setTime(8, taskEntity.getTaskTime());
            preparedStatement.setInt(9, taskEntity.getTaskIsLetter());
            if (taskEntity.getLetterId()==0) {
                preparedStatement.setNull(10, java.sql.Types.INTEGER);
            }else {
                preparedStatement.setInt(10, taskEntity.getLetterId());
            }
            preparedStatement.setInt(11, taskEntity.getTaskId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void removeTask(TaskEntity taskEntity) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE  FROM  TASKS WHERE Task_id = ?";
        //удаляем данные из таблицы

        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, taskEntity.getTaskId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public List<TaskEntity> listMyTasks(int id) throws RemoteException{
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        Statement statement = null;
        String sql = "SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS " +
                "WHERE TASKS.Employee_id=EMPLOYEES.Employee_id " +
                "AND TASKs.status_task_id=STATUS_TASKS.Status_task_id " +
                "AND TASKS.Employee_id='"+id+"' " +
                "AND STATUS_TASKS.Status_task_id!="+StatusTask.DONE+"" +
                "AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED+" " +
                "AND TASKS.Task_is_letter='0'";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskId(resultSet.getInt("Task_id"));
                taskEntity.setTaskName(resultSet.getString("Task_name"));
                taskEntity.setTaskText(resultSet.getString("Task_text"));
                taskEntity.setTaskAttachment(resultSet.getString("Task_attachment"));
                taskEntity.setEmployeeId(resultSet.getInt("Employee_id"));
                taskEntity.setEmployeeName(resultSet.getString("Employee_name"));
                taskEntity.setTaskTerm(resultSet.getDate("Task_term"));
                taskEntity.setStatusTaskName(resultSet.getString("Status_task_name"));
                taskEntity.setStatusTaskId(resultSet.getString("Status_task_id"));
                taskEntity.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                taskEntity.setTaskTime(resultSet.getTime("Task_time"));
                taskEntity.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                listData.add(taskEntity);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listMyLetterTasks(int id) throws RemoteException {
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        Statement statement = null;
        String sql = "SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS " +
                "WHERE TASKS.Employee_id=EMPLOYEES.Employee_id " +
                "AND TASKS.status_task_id=STATUS_TASKS.Status_task_id  " +
                "AND TASKS.Employee_id='"+id+"' " +
                "AND STATUS_TASKS.Status_task_id!="+StatusTask.DONE+"" +
                "AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED+" " +
                "AND TASKS.Task_is_letter='1'";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskId(resultSet.getInt("Task_id"));
                taskEntity.setTaskName(resultSet.getString("Task_name"));
                taskEntity.setTaskText(resultSet.getString("Task_text"));
                taskEntity.setTaskAttachment(resultSet.getString("Task_attachment"));
                taskEntity.setEmployeeName(resultSet.getString("Employee_name"));
                taskEntity.setEmployeeId(resultSet.getInt("Employee_id"));
                taskEntity.setTaskTerm(resultSet.getDate("Task_term"));
                taskEntity.setStatusTaskName(resultSet.getString("Status_task_name"));
                taskEntity.setStatusTaskId(resultSet.getString("Status_task_id"));
                taskEntity.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                taskEntity.setTaskTime(resultSet.getTime("Task_time"));
                taskEntity.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                taskEntity.setLetterId(resultSet.getInt("Letter_id"));
                listData.add(taskEntity);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listMyDoneTasks(int id) throws RemoteException{
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        Statement statement = null;
        String sql = "SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS " +
                "WHERE TASKS.Employee_id=EMPLOYEES.Employee_id " +
                "AND TASKs.status_task_id=STATUS_TASKS.Status_task_id " +
                "AND TASKS.Employee_id='"+id+"' " +
                "AND STATUS_TASKS.Status_task_id="+StatusTask.DONE;
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskId(resultSet.getInt("Task_id"));
                taskEntity.setTaskName(resultSet.getString("Task_name"));
                taskEntity.setTaskText(resultSet.getString("Task_text"));
                taskEntity.setEmployeeId(resultSet.getInt("Employee_id"));
                taskEntity.setTaskAttachment(resultSet.getString("Task_attachment"));
                taskEntity.setEmployeeName(resultSet.getString("Employee_name"));
                taskEntity.setTaskTerm(resultSet.getDate("Task_term"));
                taskEntity.setStatusTaskName(resultSet.getString("Status_task_name"));
                taskEntity.setStatusTaskId(resultSet.getString("Status_task_id"));
                taskEntity.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                taskEntity.setTaskTime(resultSet.getTime("Task_time"));
                taskEntity.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                listData.add(taskEntity);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listFromEmpTasks(String userName) throws RemoteException{
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        Statement statement = null;
        String sql = "SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS " +
                "WHERE TASKS.Employee_id=EMPLOYEES.Employee_id " +
                "AND TASKs.status_task_id=STATUS_TASKS.Status_task_id " +
                "AND TASKS.Task_from_employee='"+userName+"' " +
                "AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED;
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskId(resultSet.getInt("Task_id"));
                taskEntity.setTaskName(resultSet.getString("Task_name"));
                taskEntity.setTaskText(resultSet.getString("Task_text"));
                taskEntity.setTaskAttachment(resultSet.getString("Task_attachment"));
                taskEntity.setEmployeeId(resultSet.getInt("Employee_id"));
                taskEntity.setEmployeeName(resultSet.getString("Employee_name"));
                taskEntity.setTaskTerm(resultSet.getDate("Task_term"));
                taskEntity.setStatusTaskName(resultSet.getString("Status_task_name"));
                taskEntity.setStatusTaskId(resultSet.getString("Status_task_id"));
                taskEntity.setTaskTime(resultSet.getTime("Task_time"));
                taskEntity.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                taskEntity.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                taskEntity.setLetterId(resultSet.getInt("Letter_id"));
                listData.add(taskEntity);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listArchiveTasks(int idStatus) throws RemoteException{
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        Statement statement = null;
        String sql = "SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS " +
                "WHERE TASKS.Employee_id=EMPLOYEES.Employee_id " +
                "AND TASKs.status_task_id=STATUS_TASKS.Status_task_id " +
                "AND TASKS.Status_task_id='"+idStatus+"'";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskId(resultSet.getInt("Task_id"));
                taskEntity.setTaskName(resultSet.getString("Task_name"));
                taskEntity.setTaskText(resultSet.getString("Task_text"));
                taskEntity.setTaskAttachment(resultSet.getString("Task_attachment"));
                taskEntity.setEmployeeId(resultSet.getInt("Employee_id"));
                taskEntity.setTaskTerm(resultSet.getDate("Task_term"));
                taskEntity.setEmployeeName(resultSet.getString("Employee_name"));
                taskEntity.setStatusTaskName(resultSet.getString("Status_task_name"));
                taskEntity.setStatusTaskId(resultSet.getString("Status_task_id"));
                taskEntity.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                taskEntity.setTaskTime(resultSet.getTime("Task_time"));
                taskEntity.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                listData.add(taskEntity);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public void doneTask(TaskEntity taskEntity) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE TASKS SET Status_task_id=?, Task_text=?, Task_attachment=? " +
                "WHERE  Task_id =?";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(taskEntity.getStatusTaskId()));
            preparedStatement.setString(2, taskEntity.getTaskText());
            preparedStatement.setString(3, taskEntity.getTaskAttachment());
            preparedStatement.setInt(4, taskEntity.getTaskId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    @Override
    public void performedTask(int id) throws RemoteException{
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(StatusTask.PERFORMED));
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void overdueTask(int id) throws RemoteException{
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(StatusTask.OVERDUE));
            preparedStatement.setInt(2, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void canceledTask(int id) throws RemoteException{
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(StatusTask.CANCELED));
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
