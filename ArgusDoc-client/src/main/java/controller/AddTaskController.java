package controller;

import argusDocSettings.ServerFilePath;

import com.jfoenix.controls.*;
import entity.Employee;
import entity.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import service.*;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entity.StatusTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;

public class AddTaskController {

    public Employee authorizedUser ;

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
    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;

    public  void initialize() throws RemoteException {

        employeeService.listEmployees();
        ObservableList<String> observableListEmployeesName = FXCollections.observableArrayList(employeeService.listEmployeesName());
        comboBoxEmployee.setItems(observableListEmployeesName);
        timePickerTask.setIs24HourView(true);

    }

    public void addTaskButton(ActionEvent actionEvent) throws IOException {
        if (txtTaskName.getText().isEmpty() ||  comboBoxEmployee.getValue()==null || datePickerTask.getValue()==null||textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTaskName(txtTaskName.getText());
            taskEntity.setTaskText(textAreaTask.getText());
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
            taskEntity.setLetterId(0);


            try {
                taskService.addTask(taskEntity);
                if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                    File destFile = new File(taskEntity.getTaskAttachment());
                    Files.copy(taskEntity.getTaskAttachmentFile().toPath(), destFile.toPath());
                }


            }  catch (SQLException e) {
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
