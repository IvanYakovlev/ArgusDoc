package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class MainController2 {

    @FXML
    private ButtonBar myTaskBtnBar = new ButtonBar();
    @FXML
    private ButtonBar myTaskDoneBtnBar = new ButtonBar();
    @FXML
    private ButtonBar fromEmpTaskBtnBar = new ButtonBar();
    @FXML
    private ButtonBar archiveTaskBtnBar = new ButtonBar();
    @FXML
    private AnchorPane anchorTask = new AnchorPane();
    @FXML
    private AnchorPane anchorTemplate = new AnchorPane();
    @FXML
    private AnchorPane anchorCalendar = new AnchorPane();
    @FXML
    private AnchorPane anchorLetter = new AnchorPane();
    @FXML
    private AnchorPane anchorSetting = new AnchorPane();



    public void myTasksButton(ActionEvent actionEvent) {
        anchorTask.toFront();
        myTaskBtnBar.toFront();
    }

    public void myDoneTasksButton(ActionEvent actionEvent) {
        anchorTask.toFront();
        myTaskDoneBtnBar.toFront();
    }

    public void fromEmpTasjButton(ActionEvent actionEvent) {
        anchorTask.toFront();
        fromEmpTaskBtnBar.toFront();
    }

    public void archiveTasks(ActionEvent actionEvent) {
        anchorTask.toFront();
        archiveTaskBtnBar.toFront();
    }

    public void templateTabButton(ActionEvent actionEvent) {
        anchorTemplate.toFront();
    }

    public void calendarTabButton(ActionEvent actionEvent) {
        anchorCalendar.toFront();
    }

    public void letterTabButton(ActionEvent actionEvent) {
        anchorLetter.toFront();
    }

    public void settingTabButton(ActionEvent actionEvent) {
        anchorSetting.toFront();
    }
}
