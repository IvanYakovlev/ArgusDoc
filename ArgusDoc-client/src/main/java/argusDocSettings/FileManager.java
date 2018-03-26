package argusDocSettings;

import dialog.ADInfo;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class FileManager {

    public static void downloadFile(File file, String choosingDirectory) {
        if (choosingDirectory.equals("null")){
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не сохранен!");
        } else {
            File destFile = new File(choosingDirectory + "\\" + file.getName());
            try {
                Files.copy(file.toPath(), destFile.toPath());
                ADInfo.getAdInfo().dialog(Alert.AlertType.INFORMATION, "Файл сохранен!");
            } catch (IOException e) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не найден!");
            }

        }
    }

    public static void openFile(String taskAttachment) {
        try {
            File file = new File(taskAttachment);
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не найден!");
        }
        catch (IllegalArgumentException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Файл не найден!");
        }
    }
  public static void printFile(String documentFilePath){
      try {
          File file = new File(documentFilePath);
          java.awt.Desktop.getDesktop().print(file);

      }catch (NullPointerException e){
          ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не выбран!");
      }  catch (IOException e) {
          e.printStackTrace();
      }catch (IllegalArgumentException e) {
          ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не найден!");
      }
  }

}
