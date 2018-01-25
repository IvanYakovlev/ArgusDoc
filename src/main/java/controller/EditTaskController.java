package controller;

import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.*;
import dao.EmployeeDao;
import dao.EmployeeDaoImpl;
import dao.TaskDao;
import dao.TaskDaoImpl;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.StatusTask;
import model.Task;

import java.time.LocalDate;

public class EditTaskController {
    MainController mainController = new MainController();
    @FXML
    private JFXButton cancelEditTaskButton;
    @FXML
    private JFXButton editTaskButton;
    @FXML
    private JFXTimePicker timePickerTask= new JFXTimePicker();
    @FXML
    private JFXDatePicker datePickerTask = new JFXDatePicker();
    @FXML
    public JFXTextField txtTaskName = new JFXTextField();
    @FXML
    private JFXComboBox<String> comboBoxEmployee;
    @FXML
    private JFXTextArea textAreaTask;

    private EmployeeDao employeeDao;

    private TaskDao taskDao;
    public Task task;

    public void initialize(){

        employeeDao = new EmployeeDaoImpl();
        employeeDao.listEmployees();
        taskDao = new TaskDaoImpl();
        comboBoxEmployee.setItems(employeeDao.listEmployeesName());
        timePickerTask.setIs24HourView(true);

    }
    public void cancelEditTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelEditTaskButton.getScene().getWindow();
        stage.close();
    }

    public void editTaskButton(ActionEvent actionEvent) {
        if (txtTaskName.getText().isEmpty() ||  comboBoxEmployee.getValue()==null || datePickerTask.getValue()==null||textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            Task task = new Task();
            task.setTaskId(this.task.getTaskId());
            task.setTaskText(textAreaTask.getText());
            task.setTaskName(txtTaskName.getText());
            task.setTaskAttachment("file");
            task.setTaskFromEmployee(AuthorizedUser.getUser().getEmployeeName());
            task.setEmployeeId(employeeDao.getIdEmployeeByName(comboBoxEmployee.getValue()));
            task.setTaskTerm(java.sql.Date.valueOf(datePickerTask.getValue()));
            task.setTaskTime(java.sql.Time.valueOf(timePickerTask.getValue()));
            task.setStatusTaskId(StatusTask.NOT_DONE);

            taskDao.updateTask(task);



            Stage stage = (Stage) editTaskButton.getScene().getWindow();
            stage.close();

        }
    }

    public void attachmentTaskButton(ActionEvent actionEvent) {
    }
    public void initTab(Task task){
        txtTaskName.setText(task.getTaskName());
        textAreaTask.setText(task.getTaskText());
        comboBoxEmployee.getSelectionModel().select(task.getEmployeeName());
    }
}
