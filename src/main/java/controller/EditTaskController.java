package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class EditTaskController {
    MainController mainController = new MainController();
    @FXML
    private JFXButton cancelEditTaskButton;
    @FXML
    private JFXTimePicker timePickerTask = new JFXTimePicker();
    private int taskId;
    public void initialize(){

        timePickerTask.setIs24HourView(true);

    }
    public void cancelEditTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelEditTaskButton.getScene().getWindow();
        stage.close();
    }

    public void editTaskButton(ActionEvent actionEvent) {
        System.out.println(mainController.getIdTask());
    }

    public void attachmentTaskButton(ActionEvent actionEvent) {
    }
}
