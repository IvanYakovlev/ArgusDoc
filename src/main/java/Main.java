/*
*Ivan Yakovlev*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.StatusTask;
import user.UserAuth;


public class Main extends Application {

        @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("viewFXML/Main_window.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setMinHeight(715);
        primaryStage.setMinWidth(1000);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("images/icon.jpg"));

    }


    public static void main(String[] args) {
        UserAuth user = new UserAuth("4434");
        System.out.println(user.toString());
        launch(args);

    }
}