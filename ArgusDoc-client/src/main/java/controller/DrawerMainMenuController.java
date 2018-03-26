package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import main.Main;

public class DrawerMainMenuController {

    public void exitProgram(ActionEvent actionEvent) {
        Platform.exit();
        MainController.tray.remove(MainController.trayIcon);
    }
}
