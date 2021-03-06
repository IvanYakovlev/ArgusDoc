package controller;

import argusDocSettings.FileManager;
import argusDocSettings.ServerFilePath;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import entity.TaskEntity;
import javafx.stage.DirectoryChooser;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;


public class DoneTaskController {

    private TaskEntity taskEntity = new TaskEntity();
    private TaskService taskService = ServiceRegistry.taskService;
    final FileChooser fileChooser=new FileChooser();
    DirectoryChooser directoryChooser = new DirectoryChooser();
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
    public Boolean okButton = false;

    public void initialize(TaskEntity taskEntity){
        this.taskEntity = taskEntity;
        labelNameTask.setText(taskEntity.getTaskName());
        textAreaTask.setText(taskEntity.getTaskText());
        if (taskEntity.getTaskAttachment()!=null){
            downloadFile.setVisible(true);
            openFile.setVisible(true);
        }
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


            TaskEntity taskEntity = this.taskEntity;
            taskEntity.setTaskId(this.taskEntity.getTaskId());
            taskEntity.setTaskText(textAreaTask.getText());
            taskEntity.setStatusTaskId(StatusTask.DONE);
            if (attachmentFile!=null) {
                taskEntity.setTaskAttachment(ServerFilePath.TASKS_FILE_PATH + attachmentFile.getName());
            }
            taskEntity.setTaskAttachmentFile(attachmentFile);
           // taskEntity.setTaskIsLetter(0);
            taskEntity.setOldFile(this.taskEntity.getTaskAttachment());


            try {
                taskService.doneTask(taskEntity);

                //Загружаем файл на сервер
                if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                    if (taskEntity.getOldFile()!=null){
                        try {
                            Path path = Paths.get(taskEntity.getOldFile());

                            Files.delete(path);
                        }catch (IOException e){

                        }

                        //if (taskEntity.getTaskIsLetter()==0&& taskEntity.getTaskAttachmentFile()!=null) {
                        File destFile = new File(taskEntity.getTaskAttachment());
                        Files.copy(taskEntity.getTaskAttachmentFile().toPath(), destFile.toPath());

                        // }
                        ADInfo.getAdInfo().dialog(Alert.AlertType.CONFIRMATION, "Файл обновлен!");

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) doneTaskButton.getScene().getWindow();
            stage.close();

        }
        okButton = true;
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


    public void stopDoneTaskButton(ActionEvent actionEvent) {
        textAreaTask.setEditable(false);
        viewButtonBar.toFront();
    }

    public void downloadFile(ActionEvent actionEvent) {

        File file = new File(taskEntity.getTaskAttachment());
        String choosingDirectory = String.valueOf(directoryChooser.showDialog(downloadFile.getScene().getWindow()));
        System.out.println(choosingDirectory);
        FileManager.downloadFile(file, choosingDirectory);

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
        FileManager.openFile(taskEntity.getTaskAttachment());
    }
}
