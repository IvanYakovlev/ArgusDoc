package dialog;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ADInfo {

    /*Класс-singleton для показа диалоговых окон*/

    private static ADInfo adInfo;

    public static ADInfo getAdInfo(){
        if (adInfo==null){
            adInfo = new ADInfo();
        }
        return adInfo;
    }
    private ADInfo(){

    }

    public void dialog(Alert.AlertType alertType, String s) {

        Alert alert = new Alert(alertType, s);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Информация");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/images/1.jpg"));
        alert.showAndWait();
    }
}
