package controller;

import argusDocSettings.ServerFilePath;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import entity.TaskEntity;
import service.*;

import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entity.StatusTask;

import java.io.File;
import java.rmi.RemoteException;

public class DoneTaskController {

    private TaskEntity taskEntity = new TaskEntity();

    public TaskEntity getTaskEntity() {
        return taskEntity;
    }

    public void setTaskEntity(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;
    }


    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;
    final FileChooser fileChooser=new FileChooser();
    File attachmentFile;
    @FXML
    private JFXButton downloadFile = new JFXButton();
    @FXML
    private JFXButton openFile = new JFXButton();

    @FXML
    private JFXButton attachmentFileButton ;
    @FXML
    private ButtonBar viewButtonBar = new ButtonBar();
    @FXML
    private ButtonBar editButtonBar = new ButtonBar();
    @FXML
    private JFXButton cancelDoneTaskButton = new JFXButton();
    @FXML
    private Label labelNameTask = new Label();
    @FXML
    private JFXTextArea textAreaTask = new JFXTextArea();

    @FXML
    private JFXButton doneTaskButton = new JFXButton();


    public void initialize(){
        viewButtonBar.toFront();

    }
    public void cancelDoneTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelDoneTaskButton.getScene().getWindow();
        stage.close();
    }

    public void doneTaskButton(ActionEvent actionEvent) throws RemoteException {
        if (textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTaskId(this.taskEntity.getTaskId());
            taskEntity.setTaskText(textAreaTask.getText());
            taskEntity.setStatusTaskId(StatusTask.DONE);
            if (attachmentFile!=null) {
                taskEntity.setTaskAttachment(ServerFilePath.TASKS_FILE_PATH + attachmentFile.getName());
            }
            taskEntity.setTaskAttachmentFile(attachmentFile);
           // taskEntity.setTaskIsLetter(0);
            taskEntity.setOldFile(this.taskEntity.getTaskAttachment());
            taskService.doneTask(taskEntity);



            Stage stage = (Stage) doneTaskButton.getScene().getWindow();
            stage.close();

        }
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
    public void initTab(TaskEntity taskEntity){
        labelNameTask.setText(taskEntity.getTaskName());
        textAreaTask.setText(taskEntity.getTaskText());
        if (taskEntity.getTaskAttachment()!=null){
            downloadFile.setVisible(true);
            openFile.setVisible(true);
        }
    }

    public void stopDoneTaskButton(ActionEvent actionEvent) {
        textAreaTask.setEditable(false);
        viewButtonBar.toFront();
    }

    public void downloadFile(ActionEvent actionEvent) {
        System.out.println(taskEntity.getTaskAttachment());

    }

    public void doTaskDone(ActionEvent actionEvent) {

        textAreaTask.setEditable(true);
        editButtonBar.toFront();
        // если задача сформирована с помощью письма, файл прикреплять не нужно
        System.out.println(taskEntity);
        if (this.taskEntity.getTaskIsLetter()==1){
            attachmentFileButton.setVisible(false);
        }

    }

    public void openFile(ActionEvent actionEvent) throws RemoteException {
        taskService.openTaskAttachment(taskEntity.getTaskId());
    }
}
