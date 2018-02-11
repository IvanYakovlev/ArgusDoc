package controller;

import argusDocSettings.ServerFilePath;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.StatusTask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class EditTaskController {
    MainController mainController = new MainController();
    final FileChooser fileChooser=new FileChooser();
    File attachmentFile;

    @FXML
    private Pane paneEditTask;
    @FXML
    private Pane paneViewTask;
    @FXML
    private ButtonBar editButtonBar;
    @FXML
    private ButtonBar viewButtonBar;


    @FXML
    private Label labelViewTask;
    @FXML
    private JFXTextArea textViewAreaTask;

    @FXML
    private JFXButton attachmentFileButton;
    @FXML
    private JFXButton openFileButton;
    @FXML
    private JFXButton downloadFileButton;
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
        paneViewTask.toFront();
        viewButtonBar.toFront();
    }


    public void editTaskButton(ActionEvent actionEvent) throws IOException {
        if (txtTaskName.getText().isEmpty() ||  comboBoxEmployee.getValue()==null || datePickerTask.getValue()==null||textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            Task task = new Task();
            task.setTaskId(this.task.getTaskId());
            task.setTaskText(textAreaTask.getText());
            task.setTaskName(txtTaskName.getText());
            task.setTaskAttachmentFile(attachmentFile);
            if (attachmentFile!=null) {
                task.setTaskAttachment(ServerFilePath.TASKS_FILE_PATH + attachmentFile.getName());
            }
            task.setTaskFromEmployee(AuthorizedUser.getUser().getEmployeeName());
            task.setEmployeeId(employeeDao.getIdEmployeeByName(comboBoxEmployee.getValue()));
            task.setTaskTerm(java.sql.Date.valueOf(datePickerTask.getValue()));
            task.setTaskTime(java.sql.Time.valueOf(timePickerTask.getValue()));
            task.setStatusTaskId(StatusTask.NOT_DONE);
            task.setTaskIsLetter(0);
            task.setOldFile(this.task.getTaskAttachment());
            System.out.println(this.task.getTaskAttachment());
            taskDao.updateTask(task);



            Stage stage = (Stage) editTaskButton.getScene().getWindow();
            stage.close();

        }
    }

    public void attachmentFileButton(ActionEvent actionEvent) {

        File file = fileChooser.showOpenDialog(attachmentFileButton.getScene().getWindow());
        if (file == null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Файл не выбран!");
        } else {

            attachmentFile=file;
        }
    }
    public void initTab(Task task){
        txtTaskName.setText(task.getTaskName());
        textAreaTask.setText(task.getTaskText());
        textViewAreaTask.setText(task.getTaskText());
        labelViewTask.setText(task.getTaskName());
        comboBoxEmployee.getSelectionModel().select(task.getEmployeeName());
        if (task.getTaskAttachment()==null){
            downloadFileButton.setVisible(false);
            openFileButton.setVisible(false);
        }
    }




    public void cancelEditTaskIcon(MouseEvent mouseEvent) {


            Stage stage = (Stage) editTaskButton.getScene().getWindow();
            stage.close();

    }

    public void openFileButton(ActionEvent actionEvent) {
        taskDao.openTaskAttachment(task.getTaskId());
    }

    public void downloadFileButton(ActionEvent actionEvent) {

    }

    public void doEditTaskButton(ActionEvent actionEvent) {
        paneEditTask.toFront();
        editButtonBar.toFront();
    }



    public void cancelEditTaskButton(ActionEvent actionEvent) {
        paneViewTask.toFront();
        viewButtonBar.toFront();
    }

    public void toArchiveTaskButton(ActionEvent actionEvent) {
        if (task!=null) {
            taskDao.canceledTask(task.getTaskId());
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }

        Stage stage = (Stage) editTaskButton.getScene().getWindow();
        stage.close();
    }
}
