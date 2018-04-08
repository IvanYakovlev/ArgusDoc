package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class DrawerMainMenuController {

    @FXML
    private JFXButton changeUser;

    @FXML
    private JFXButton aboutProgramm;

    @FXML
    private JFXButton exitProgram;

   private double xOffset = 0;
   private double yOffset = 0;

    public void initialize(){
        changeUser.setFocusTraversable(false);
        aboutProgramm.setFocusTraversable(false);
        exitProgram.setFocusTraversable(false);
    }
    public void exitProgram(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void changeUser(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) changeUser.getScene().getWindow();

        stage.close();



        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource("../viewFXML/Authorization_window.fxml"));

        Parent root = fxmlLoader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Авторизация");
        primaryStage.setMinHeight(120);
        primaryStage.setMinWidth(220);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("images/1.jpg"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public void aboutProgramm(ActionEvent actionEvent) {
    }
}
