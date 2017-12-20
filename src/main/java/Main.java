/*
*Ivan Yakovlev*/
import dbConnection.DBconnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    DBconnection dBconnection;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("viewFXML/Main_window.fxml"));
        primaryStage.setTitle("ArgusDoc");
        primaryStage.setMinHeight(715);
        primaryStage.setMinWidth(1000);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("images/icon.jpg"));
        dBconnection.connect();
        System.out.println("пиу");
        dBconnection.disconect();
    }


    public static void main(String[] args) {
        launch(args);
    }
}