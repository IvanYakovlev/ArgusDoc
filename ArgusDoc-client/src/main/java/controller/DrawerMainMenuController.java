package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.Main;

public class DrawerMainMenuController {

    @FXML
    private JFXButton changeUser;

    @FXML
    private JFXButton aboutProgramm;

    @FXML
    private JFXButton exitProgram;

    public void initialize(){
        changeUser.setFocusTraversable(false);
        aboutProgramm.setFocusTraversable(false);
        exitProgram.setFocusTraversable(false);
    }
    public void exitProgram(ActionEvent actionEvent) {
        Platform.exit();
        MainController.tray.remove(MainController.trayIcon);
    }

    public void changeUser(ActionEvent actionEvent) {
    }

    public void aboutProgramm(ActionEvent actionEvent) {
    }
}
