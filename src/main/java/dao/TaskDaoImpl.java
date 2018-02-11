package dao;


import dbConnection.DBconnection;
import dialog.ADInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.StatusTask;
import model.Task;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class TaskDaoImpl implements TaskDao{
DBconnection dBconnection;
    public void addTask(Task task) throws IOException {
        dBconnection=new DBconnection();
//Добавляем данные в таблицу
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO TASKS(Task_name, Task_text, Task_attachment, Task_from_employee, Employee_id, Task_term, Status_task_id, Task_time,Task_is_letter) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setString(4, task.getTaskFromEmployee());
            preparedStatement.setInt(5, task.getEmployeeId());
            preparedStatement.setDate(6, task.getTaskTerm());
            preparedStatement.setInt(7, Integer.parseInt(StatusTask.NOT_DONE));
            preparedStatement.setTime(8,task.getTaskTime());
            preparedStatement.setInt(9,task.getTaskIsLetter());
            preparedStatement.execute();


//Копируем файл если он прикреплен или задача не сформирована из вкладки Письма
            if (task.getTaskIsLetter()==0&&task.getTaskAttachmentFile()!=null) {
                File destFile = new File(task.getTaskAttachment());
                Files.copy(task.getTaskAttachmentFile().toPath(), destFile.toPath());
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
                Path path = Paths.get(task.getTaskAttachment());
                Files.delete(path);

                if (task.getTaskIsLetter()==0&&task.getTaskAttachmentFile()!=null) {
                    File destFile = new File(task.getTaskAttachment());
                    Files.copy(task.getTaskAttachmentFile().toPath(), destFile.toPath());

                }

            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }
        }
    }

    public void updateTask(Task task) throws IOException {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Task_name=?, Task_text=?, Task_attachment=?, Task_from_employee=?, Employee_id=?, Task_term=?, Status_task_id=?, Task_time=?,Task_is_letter=? WHERE  Task_id =?");
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setString(4, task.getTaskFromEmployee());
            preparedStatement.setInt(5, task.getEmployeeId());
            preparedStatement.setDate(6,task.getTaskTerm());
            preparedStatement.setString(7, task.getStatusTaskId());
            preparedStatement.setTime(8,task.getTaskTime());
            preparedStatement.setInt(9,task.getTaskIsLetter());
            preparedStatement.setInt(10, task.getTaskId());
            preparedStatement.execute();


            //Копируем файл если он прикреплен или задача не сформирована из вкладки Письма
            if (task.getTaskIsLetter()==0&&task.getTaskAttachmentFile()!=null) {
                try {
                    Path path = Paths.get(task.getOldFile());
                    Files.delete(path);
                }catch (NoSuchFileException e){

                }


                if (task.getTaskIsLetter()==0&&task.getTaskAttachmentFile()!=null) {
                    File destFile = new File(task.getTaskAttachment());
                    Files.copy(task.getTaskAttachmentFile().toPath(), destFile.toPath());

                }
                ADInfo.getAdInfo().dialog(Alert.AlertType.CONFIRMATION, "Файл обновлен!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTask(Task task) {
        dBconnection = new DBconnection();
        try {
            //удаляем данные из таблицы

            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE  FROM  TASKS WHERE Task_id = ?");
            preparedStatement.setInt(1, task.getTaskId());
            preparedStatement.execute();


            //удаляем файл с сервера, если это не письмо
            if (task.getTaskAttachment()!=null&&task.getTaskIsLetter()==0) {
                Path path = Paths.get(task.getTaskAttachment());
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

    public ObservableList<Task> listMyTasks(int id) {
        this.dBconnection = new DBconnection();
        ObservableList<Task> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Employee_id='"+id+"' AND STATUS_TASKS.Status_task_id!="+StatusTask.DONE+"AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED);
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
                task.setStatusTaskId(resultSet.getString("Status_task_id"));
                task.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                task.setTaskTime(resultSet.getTime("Task_time"));
                task.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                listData.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public ObservableList<Task> listMyDoneTasks(int id) {
        this.dBconnection = new DBconnection();
        ObservableList<Task> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Employee_id='"+id+"' AND STATUS_TASKS.Status_task_id="+StatusTask.DONE);
            while (resultSet.next()){
                Task task= new Task();
                task.setTaskId(resultSet.getInt("Task_id"));
                task.setTaskName(resultSet.getString("Task_name"));
                task.setTaskText(resultSet.getString("Task_text"));
                task.setEmployeeId(resultSet.getInt("Employee_id"));
                task.setTaskAttachment(resultSet.getString("Task_attachment"));
                task.setEmployeeName(resultSet.getString("Employee_name"));
                task.setTaskTerm(resultSet.getDate("Task_term"));
                task.setStatusTaskName(resultSet.getString("Status_task_name"));
                task.setStatusTaskId(resultSet.getString("Status_task_id"));
                task.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                task.setTaskTime(resultSet.getTime("Task_time"));
                task.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                listData.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public ObservableList<Task> listFromEmpTasks(String userName) {
        this.dBconnection = new DBconnection();
        ObservableList<Task> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Task_from_employee='"+userName+"' AND STATUS_TASKS.Status_task_id!="+StatusTask.CANCELED);
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
                task.setStatusTaskId(resultSet.getString("Status_task_id"));
                task.setTaskTime(resultSet.getTime("Task_time"));
                task.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                task.setTaskIsLetter(resultSet.getInt("Task_is_letter"));

                listData.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public ObservableList<Task> listArchiveTasks(int idStatus) {
        this.dBconnection = new DBconnection();
        ObservableList<Task> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM TASKS,EMPLOYEES,STATUS_TASKS WHERE TASKS.Employee_id=EMPLOYEES.Employee_id AND TASKs.status_task_id=STATUS_TASKS.Status_task_id AND TASKS.Status_task_id='"+idStatus+"'");
            while (resultSet.next()){
                Task task= new Task();
                task.setTaskId(resultSet.getInt("Task_id"));
                task.setTaskName(resultSet.getString("Task_name"));
                task.setTaskText(resultSet.getString("Task_text"));
                task.setTaskAttachment(resultSet.getString("Task_attachment"));
                task.setEmployeeId(resultSet.getInt("Employee_id"));

                task.setTaskTerm(resultSet.getDate("Task_term"));
                task.setEmployeeName(resultSet.getString("Employee_name"));
                task.setStatusTaskName(resultSet.getString("Status_task_name"));
                task.setStatusTaskId(resultSet.getString("Status_task_id"));
                task.setTaskFromEmployee(resultSet.getString("Task_from_employee"));
                task.setTaskTime(resultSet.getTime("Task_time"));
                task.setTaskIsLetter(resultSet.getInt("Task_is_letter"));
                listData.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public void doneTask(Task task) {
        dBconnection=new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE TASKS SET Status_task_id=?, Task_text=?, Task_attachment=? WHERE  Task_id =?");
            preparedStatement.setInt(1, Integer.parseInt(task.getStatusTaskId()));
            preparedStatement.setString(2, task.getTaskText());
            preparedStatement.setString(3, task.getTaskAttachment());
            preparedStatement.setInt(4, task.getTaskId());

            preparedStatement.execute();

            //Загружаем файл на сервер
            if (task.getTaskIsLetter()==0&&task.getTaskAttachmentFile()!=null) {
                if (task.getOldFile()!=null){
                try {
                    Path path = Paths.get(task.getOldFile());

                    Files.delete(path);
                }catch (IOException e){

                }



                if (task.getTaskIsLetter()==0&&task.getTaskAttachmentFile()!=null) {
                    File destFile = new File(task.getTaskAttachment());
                    Files.copy(task.getTaskAttachmentFile().toPath(), destFile.toPath());

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
    public void performedTask(int id) {
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
    public void overdueTask(int id) {
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
    public void canceledTask(int id) {
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
    public void openTaskAttachment(int id) {
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
    public void downloadAttachmentFile(int id) {

    }
}
