package dialog;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class ADInfo {
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
        alert.showAndWait();
    }
}
