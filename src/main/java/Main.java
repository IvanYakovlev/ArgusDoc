/*
*Ivan Yakovlev*/

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.StatusTask;



public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

        @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("viewFXML/Authorization_window.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setMinHeight(120);
        primaryStage.setMinWidth(220);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("images/icon.jpg"));
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

        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);

    }
}