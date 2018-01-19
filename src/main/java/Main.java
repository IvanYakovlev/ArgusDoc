/*
*Ivan Yakovlev*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.StatusTask;



public class Main extends Application {

        @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("viewFXML/Authorization_window.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setMinHeight(120);
        primaryStage.setMinWidth(220);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("images/icon.jpg"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);

    }
}