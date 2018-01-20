package controller;

import com.jfoenix.controls.*;
import dao.EmployeeDao;
import dao.EmployeeDaoImpl;
import dao.TaskDao;
import dao.TaskDaoImpl;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;
import model.Task;

import java.awt.*;
import java.util.Date;

public class AddTaskController {
    @FXML
    private JFXTextField txtTaskName;
    @FXML
    private JFXComboBox<String> comboBoxEmployee;
    @FXML
    private JFXDatePicker datePickerTask;
    @FXML
    private JFXTimePicker timePickerTask;
    @FXML
    private JFXTextArea textAreaTask;
    @FXML
    private JFXButton cancelAddTaskButton;
    private TaskDao taskDao;
    private EmployeeDao employeeDao;

    public void addTaskButton(ActionEvent actionEvent) {
        if (txtTaskName.getText().isEmpty() ||  comboBoxEmployee.getValue()==null || datePickerTask.getValue()==null||textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            taskDao = new TaskDaoImpl();
            employeeDao = new EmployeeDaoImpl();
            Task task = new Task();
            task.setTaskName(txtTaskName.getText());
            task.setEmployeeId(employeeDao.getIdEmployeeByName(comboBoxEmployee.getValue()));
            task.setTaskTerm(java.sql.Date.valueOf(datePickerTask.getValue()));
            task.setTaskText(textAreaTask.getText());
            taskDao.addTask(task);

        }
    }

    public void cancelAddTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddTaskButton.getScene().getWindow();
        stage.close();

    }

    public void attachmentFileButton(ActionEvent actionEvent) {
    }
}
