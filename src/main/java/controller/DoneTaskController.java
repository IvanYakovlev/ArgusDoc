package controller;

import argusDocSettings.ServerFilePath;
import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import dao.TaskDao;
import dao.TaskDaoImpl;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.StatusTask;
import model.Task;

import java.io.File;

public class DoneTaskController {

    private Task task = new Task();

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }


    TaskDao taskDao = new TaskDaoImpl();
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

    public void doneTaskButton(ActionEvent actionEvent) {
        if (textAreaTask.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {


            Task task = new Task();
            task.setTaskId(this.task.getTaskId());
            task.setTaskText(textAreaTask.getText());
            task.setStatusTaskId(StatusTask.DONE);
            task.setTaskAttachment(ServerFilePath.TASKS_FILE_PATH+attachmentFile.getName());
            task.setTaskAttachmentFile(attachmentFile);
            task.setTaskIsLetter(false);

            taskDao.doneTask(task);



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
    public void initTab(Task task){
        labelNameTask.setText(task.getTaskName());
        textAreaTask.setText(task.getTaskText());
        if (task.getTaskAttachment()!=null){
            downloadFile.setVisible(true);
            openFile.setVisible(true);
        }
    }

    public void stopDoneTaskButton(ActionEvent actionEvent) {
        textAreaTask.setEditable(false);
        viewButtonBar.toFront();
    }

    public void downloadFile(ActionEvent actionEvent) {
        System.out.println(task.getTaskAttachment());

    }

    public void doTaskDone(ActionEvent actionEvent) {

        textAreaTask.setEditable(true);
        editButtonBar.toFront();

    }

    public void openFile(ActionEvent actionEvent) {
        taskDao.openTaskAttachment(task.getTaskId());
    }
}
