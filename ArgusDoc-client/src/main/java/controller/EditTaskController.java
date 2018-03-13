package controller;

import argusDocSettings.ServerFilePath;
import com.jfoenix.controls.*;
import entity.Employee;
import entity.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.*;
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
import entity.StatusTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class EditTaskController {

    public Employee authorizedUser ;

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

    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;
    public TaskEntity taskEntity;

    public void initialize() throws RemoteException {


        employeeService.listEmployees();
        ObservableList<String> observableListEmployeesName = FXCollections.observableArrayList(employeeService.listEmployeesName());
        comboBoxEmployee.setItems(observableListEmployeesName);
        timePickerTask.setIs24HourView(true);
        paneViewTask.toFront();
        viewButtonBar.toFront();
    }


    public void editTaskButton(ActionEvent actionEvent) throws IOException {
        if (txtTaskName.getText().isEmpty() ||  comboBoxEmployee.getValue()==null || datePickerTask.getValue()==null||textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTaskId(this.taskEntity.getTaskId());
            taskEntity.setTaskText(textAreaTask.getText());
            taskEntity.setTaskName(txtTaskName.getText());
            taskEntity.setTaskAttachmentFile(attachmentFile);
            if (attachmentFile!=null) {
                taskEntity.setTaskAttachment(ServerFilePath.TASKS_FILE_PATH + attachmentFile.getName());
            }
            taskEntity.setTaskFromEmployee(authorizedUser.getEmployeeName());
            taskEntity.setEmployeeId(employeeService.getIdEmployeeByName(comboBoxEmployee.getValue()));
            taskEntity.setTaskTerm(java.sql.Date.valueOf(datePickerTask.getValue()));
            taskEntity.setTaskTime(java.sql.Time.valueOf(timePickerTask.getValue()));
            taskEntity.setStatusTaskId(StatusTask.NOT_DONE);
            taskEntity.setTaskIsLetter(0);
            taskEntity.setOldFile(this.taskEntity.getTaskAttachment());
            System.out.println(this.taskEntity.getTaskAttachment());
            try {
                taskService.updateTask(taskEntity);
//Копируем файл если он прикреплен или задача сформирована не из вкладки Письма
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
    public void initTab(TaskEntity taskEntity){
        txtTaskName.setText(taskEntity.getTaskName());
        textAreaTask.setText(taskEntity.getTaskText());
        textViewAreaTask.setText(taskEntity.getTaskText());
        labelViewTask.setText(taskEntity.getTaskName());
        comboBoxEmployee.getSelectionModel().select(taskEntity.getEmployeeName());
        if (taskEntity.getTaskAttachment()==null){
            downloadFileButton.setVisible(false);
            openFileButton.setVisible(false);
        }
    }




    public void cancelEditTaskIcon(MouseEvent mouseEvent) {


            Stage stage = (Stage) editTaskButton.getScene().getWindow();
            stage.close();

    }

    public void openFileButton(ActionEvent actionEvent) throws RemoteException {
        try {
            File file = new File(taskEntity.getTaskAttachment());
            java.awt.Desktop.getDesktop().open(file);
            //taskService.openTaskAttachment(taskEntity.getTaskId());


        } catch (IOException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не найден!");
        }
        catch (IllegalArgumentException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не найден!");
        }
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

    public void toArchiveTaskButton(ActionEvent actionEvent) throws RemoteException {
        if (taskEntity !=null) {
            taskService.canceledTask(taskEntity.getTaskId());
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }

        Stage stage = (Stage) editTaskButton.getScene().getWindow();
        stage.close();
    }
}
