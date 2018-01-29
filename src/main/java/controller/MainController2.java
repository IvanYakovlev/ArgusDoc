package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class MainController2 {

    @FXML
    private JFXButton a = new JFXButton();
    @FXML
    private JFXButton b = new JFXButton();
    @FXML
    private JFXButton c = new JFXButton();
    @FXML
    private JFXButton d = new JFXButton();
    @FXML
    private Pane paneTask = new Pane();
    @FXML
    private AnchorPane bp = new AnchorPane();
    @FXML
    private AnchorPane cp = new AnchorPane();
    @FXML
    private AnchorPane dp = new AnchorPane();
    @FXML
    private AnchorPane ep = new AnchorPane();
    private Pane  paneTemplate = new Pane();
    public void myTasksTab(Event event) {
        System.out.println("my");
    }

    public void doneMyTask(Event event) {
        System.out.println("done");
    }

    public void fromEmpTask(Event event) {
        System.out.println("task");
    }

    public void Archive(Event event) {
        System.out.println("archive");
    }

    public void aMewnu(ActionEvent actionEvent) {
        b.setMinSize(0,0);
        c.setMinSize(0,0);

    }

    public void b(ActionEvent actionEvent) {
        paneTask.toFront();
        bp.toFront();
    }

    public void c(ActionEvent actionEvent) {
        paneTask.toFront();
        cp.toFront();
    }

    public void d(ActionEvent actionEvent) {
        paneTask.toFront();
        dp.toFront();
    }

    public void e(ActionEvent actionEvent) {
        paneTask.toFront();
        ep.toFront();
    }

    public void a(ActionEvent actionEvent) {
        paneTask.toFront();
        bp.toFront();
    }

    public void y(ActionEvent actionEvent) {
        paneTemplate.toFront();
    }
}
