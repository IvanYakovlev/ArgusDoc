package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AddLetterController {
    @FXML
    private JFXButton cancelAddLetterButton = new JFXButton();

    public void cancelAddLetterButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddLetterButton.getScene().getWindow();
        stage.close();
    }

    public void addLetterButton(ActionEvent actionEvent) {
    }

    public void attachmentFileButton(ActionEvent actionEvent) {
    }
}
