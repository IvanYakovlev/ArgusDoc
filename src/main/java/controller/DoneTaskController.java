package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class DoneTaskController {

    @FXML
    private JFXButton cancelDoneTaskButton;

    public void cancelDoneTaskButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelDoneTaskButton.getScene().getWindow();
        stage.close();
    }

    public void doneTaskButton(ActionEvent actionEvent) {
    }

    public void attachmentFileButton(ActionEvent actionEvent) {
    }
}
