package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class EditTaskController {
    @FXML
    private JFXButton cancelEditTaskButton;

    public void cancelEditTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelEditTaskButton.getScene().getWindow();
        stage.close();
    }

    public void editTaskButton(ActionEvent actionEvent) {
    }

    public void attachmentTaskButton(ActionEvent actionEvent) {
    }
}
