package controller;

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
import javafx.stage.Stage;
import model.StatusTask;
import model.Task;

public class DoneTaskController {

    private Task task = new Task();

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }



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
    private TaskDao taskDao = new TaskDaoImpl();

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
            task.setTaskAttachment("newfile");


            taskDao.doneTask(task);



            Stage stage = (Stage) doneTaskButton.getScene().getWindow();
            stage.close();

        }
    }

    public void attachmentFileButton(ActionEvent actionEvent) {
    }
    public void initTab(Task task){
        labelNameTask.setText(task.getTaskName());
        textAreaTask.setText(task.getTaskText());

    }

    public void stopDoneTaskButton(ActionEvent actionEvent) {
        viewButtonBar.toFront();
    }

    public void downloadFile(ActionEvent actionEvent) {

    }

    public void doTaskDone(ActionEvent actionEvent) {
        editButtonBar.toFront();
    }

    public void openFile(ActionEvent actionEvent) {
    }
}
