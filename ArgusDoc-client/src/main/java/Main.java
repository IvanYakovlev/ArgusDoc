import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    public static Stage stage;

        @Override
    public void start(Stage primaryStage) throws Exception {
// не завершать приложение когда последнее окно закрыто
            Platform.setImplicitExit(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFXML/Authorization_window.fxml"));


            Parent root = loader.load();

            primaryStage.setTitle("Авторизация");
            primaryStage.setMinHeight(120);
            primaryStage.setMinWidth(220);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(("images/1.jpg"))));
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

            stage=primaryStage;
            primaryStage.show();

        }

    public static void main(String[] args) {
        launch(args);

    }
}
