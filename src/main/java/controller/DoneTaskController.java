package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
    private JFXButton cancelDoneTaskButton = new JFXButton();
    @FXML
    private Label labelNameTask = new Label();
    @FXML
    private JFXTextArea textAreaTask = new JFXTextArea();


    public void cancelDoneTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelDoneTaskButton.getScene().getWindow();
        stage.close();
    }

    public void doneTaskButton(ActionEvent actionEvent) {
    }

    public void attachmentFileButton(ActionEvent actionEvent) {
    }
    public void initTab(Task task){
        labelNameTask.setText(task.getTaskName());
        textAreaTask.setText(task.getTaskText());

    }
}
