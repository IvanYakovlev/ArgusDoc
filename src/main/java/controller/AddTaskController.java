package controller;

import argusDocSettings.ServerFilePath;
import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.*;
import dao.*;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Employee;
import model.StatusTask;
import model.Task;

import java.awt.*;
import java.io.File;
import java.sql.Time;
import java.util.Date;

public class AddTaskController {

    final FileChooser fileChooser=new FileChooser();
    File attachmentFile;


    @FXML
    private JFXButton  attachmentFileButton;
    @FXML
    private JFXTextField txtTaskName;
    @FXML
    private JFXComboBox<String> comboBoxEmployee = new JFXComboBox<String>();
    @FXML
    private JFXDatePicker datePickerTask;
    @FXML
    private JFXTimePicker timePickerTask = new JFXTimePicker();
    @FXML
    private JFXTextArea textAreaTask;
    @FXML
    private JFXButton cancelAddTaskButton;
    @FXML
    private JFXButton addTaskButton;
    private TaskDao taskDao;
    private EmployeeDao employeeDao;

    public  void initialize(){
        employeeDao = new EmployeeDaoImpl();
        taskDao = new TaskDaoImpl();
        employeeDao.listEmployees();
        comboBoxEmployee.setItems(employeeDao.listEmployeesName());
        timePickerTask.setIs24HourView(true);

    }

    public void addTaskButton(ActionEvent actionEvent) {
        if (txtTaskName.getText().isEmpty() ||  comboBoxEmployee.getValue()==null || datePickerTask.getValue()==null||textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            Task task = new Task();
            task.setTaskName(txtTaskName.getText());
            task.setTaskText(textAreaTask.getText());
            task.setTaskAttachmentFile(attachmentFile);
            if (attachmentFile!=null) {
                task.setTaskAttachment(ServerFilePath.TASKS_FILE_PATH + attachmentFile.getName());
            }
            task.setTaskFromEmployee(AuthorizedUser.getUser().getEmployeeName());
            task.setEmployeeId(employeeDao.getIdEmployeeByName(comboBoxEmployee.getValue()));
            task.setTaskTerm(java.sql.Date.valueOf(datePickerTask.getValue()));
            task.setTaskTime(java.sql.Time.valueOf(timePickerTask.getValue()));
            task.setStatusTaskId(StatusTask.NOT_DONE);
            task.setTaskIsLetter(false);
            taskDao.addTask(task);



            Stage stage = (Stage) addTaskButton.getScene().getWindow();
            stage.close();

        }
    }

    public void cancelAddTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddTaskButton.getScene().getWindow();
        stage.close();

    }

    public void attachmentFileButton(ActionEvent actionEvent) {
        File file;
        file = fileChooser.showOpenDialog(attachmentFileButton.getScene().getWindow());
        if (file == null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Файл не выбран!");
        } else {

            attachmentFile=file;
        }
    }
}
