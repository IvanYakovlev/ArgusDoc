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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {
DBconnection dBconnection;
    public void addTask(TaskEntity taskEntity) throws IOException, RemoteException {
        dBconnection=new DBconnection();
//Добавляем данные в таблицу
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO TASKS(Task_name, Task_text, Task_attachment, Task_from_employee, Employee_id, Task_term, Status_task_id, Task_time,Task_is_letter) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, taskEntity.getTaskName());
            preparedStatement.setString(2, taskEntity.getTaskText());
            preparedStatement.setString(3, taskEntity.getTaskAttachment());
            preparedStatement.setString(4, taskEntity.getTaskFromEmployee());
            preparedStatement.setInt(5, taskEntity.getEmployeeId());
            preparedStatement.setDate(6, taskEntity.getTaskTerm());
            preparedStatement.setInt(7, Integer.parseInt(StatusTask.NOT_DONE));
            preparedStatement.setTime(8, taskEntity.getTaskTime());
            preparedStatement.setInt(9, taskEntity.getTaskIsLetter());
            preparedStatement.execute();


//Копируем файл если он прикреплен или задача не сформирована из вкладки Письма
            if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                File destFile = new File(taskEntity.getTaskAttachment());
                Files.copy(taskEntity.getTaskAttachmentFile().toPath(), destFile.toPath());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            //alert.setTitle("Delete File");
            alert.setHeaderText("Файл с таким именем уже существует! Хотите заменить?");


            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                Path path = Paths.get(taskEntity.getTaskAttachment());
                Files.delete(path);

                if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                    File destFile = new File(taskEntity.getTaskAttachment());
                    Files.copy(taskEntity.getTaskAttachmentFile().toPath(), destFile.toPath());

                }

            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }
        }
    }

    public void updateTask(TaskEntity taskEntity) throws IOException, RemoteException{
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Task_name=?, Task_text=?, Task_attachment=?, Task_from_employee=?, Employee_id=?, Task_term=?, Status_task_id=?, Task_time=?,Task_is_letter=? WHERE  Task_id =?");
            preparedStatement.setString(1, taskEntity.getTaskName());
            preparedStatement.setString(2, taskEntity.getTaskText());
            preparedStatement.setString(3, taskEntity.getTaskAttachment());
            preparedStatement.setString(4, taskEntity.getTaskFromEmployee());
            preparedStatement.setInt(5, taskEntity.getEmployeeId());
            preparedStatement.setDate(6, taskEntity.getTaskTerm());
            preparedStatement.setString(7, taskEntity.getStatusTaskId());
            preparedStatement.setTime(8, taskEntity.getTaskTime());
            preparedStatement.setInt(9, taskEntity.getTaskIsLetter());
            preparedStatement.setInt(10, taskEntity.getTaskId());
            preparedStatement.execute();


            //Копируем файл если он прикреплен или задача не сформирована из вкладки Письма
            if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                try {
                    Path path = Paths.get(taskEntity.getOldFile());
                    Files.delete(path);
                }catch (NoSuchFileException e){

                }


                if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                    File destFile = new File(taskEntity.getTaskAttachment());
                    Files.copy(taskEntity.getTaskAttachmentFile().toPath(), destFile.toPath());

                }
                ADInfo.getAdInfo().dialog(Alert.AlertType.CONFIRMATION, "Файл обновлен!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTask(TaskEntity taskEntity)throws RemoteException {
        dBconnection = new DBconnection();
        try {
            //удаляем данные из таблицы

            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE  FROM  TASKS WHERE Task_id = ?");
            preparedStatement.setInt(1, taskEntity.getTaskId());
            preparedStatement.execute();


            //удаляем файл с сервера, если это не письмо
            if (taskEntity.getTaskAttachment()!=null&& taskEntity.getTaskIsLetter()==0) {
                Path path = Paths.get(taskEntity.getTaskAttachment());
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    System.out.println("файл уже удален");
                   // ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<TaskEntity> listMyTasks(int id) throws RemoteException{
        this.dBconnection = new DBconnection();
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Employee_id='"+id+"' AND STATUS_TASKS.Status_task_id!="+StatusTask.DONE+"AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listMyDoneTasks(int id) throws RemoteException{
        this.dBconnection = new DBconnection();
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Employee_id='"+id+"' AND STATUS_TASKS.Status_task_id="+StatusTask.DONE);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listFromEmpTasks(String userName) throws RemoteException{
        this.dBconnection = new DBconnection();
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Task_from_employee='"+userName+"' AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED);
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

                listData.add(taskEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public List<TaskEntity> listArchiveTasks(int idStatus) throws RemoteException{
        this.dBconnection = new DBconnection();
        List<TaskEntity> listData = new ArrayList<TaskEntity>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Status_task_id='"+idStatus+"'");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public void doneTask(TaskEntity taskEntity) throws RemoteException{
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=?, Task_text=?, Task_attachment=? WHERE  Task_id =?");
            preparedStatement.setInt(1, Integer.parseInt(taskEntity.getStatusTaskId()));
            preparedStatement.setString(2, taskEntity.getTaskText());
            preparedStatement.setString(3, taskEntity.getTaskAttachment());
            preparedStatement.setInt(4, taskEntity.getTaskId());

            preparedStatement.execute();

            //Загружаем файл на сервер
            if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                if (taskEntity.getOldFile()!=null){
                try {
                    Path path = Paths.get(taskEntity.getOldFile());

                    Files.delete(path);
                }catch (IOException e){

                }



                if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                    File destFile = new File(taskEntity.getTaskAttachment());
                    Files.copy(taskEntity.getTaskAttachmentFile().toPath(), destFile.toPath());

                }
                ADInfo.getAdInfo().dialog(Alert.AlertType.CONFIRMATION, "Файл обновлен!");
                }
            }





        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performedTask(int id) throws RemoteException{
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, Integer.parseInt(StatusTask.PERFORMED));
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void overdueTask(int id) throws RemoteException{
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, Integer.parseInt(StatusTask.OVERDUE));
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void canceledTask(int id) throws RemoteException{
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=? WHERE  Task_id =?");
            preparedStatement.setInt(1, Integer.parseInt(StatusTask.CANCELED));
            preparedStatement.setInt(2, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openTaskAttachment(int id) throws RemoteException{
        dBconnection = new DBconnection();
        try {
            String sql = "SELECT Task_attachment FROM TASKS WHERE Task_id=" + id;
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            if (resultSet.next()) {

                String filepath = resultSet.getString("Task_attachment");

                File file = new File(filepath);
                try {
                    java.awt.Desktop.getDesktop().open(file);
                }catch (IllegalArgumentException e){
                    ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не найден!");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void downloadAttachmentFile(int id)throws RemoteException {

    }
}
