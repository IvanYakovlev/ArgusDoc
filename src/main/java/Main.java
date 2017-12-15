/*
*Ivan Yakovlev*/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main extends Application {

    @Value("${ui.title:JavaFX приложение}")//
    private String windowTitle;

    @Qualifier("mainView")
    @Autowired
    private ControllersCFG.ViewHolder view;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("viewFXML/Main_window.fxml"));
        primaryStage.setTitle("ArgusDoc");
        primaryStage.setMinHeight(715);
        primaryStage.setMinWidth(1000);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("icon.jpg"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}