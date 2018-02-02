package controller;

import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dao.EmployeeDao;
import dao.EmployeeDaoImpl;
import dao.TaskDao;
import dao.TaskDaoImpl;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Employee;
import model.StatusTask;
import model.Task;
import org.controlsfx.control.CheckComboBox;

import java.io.File;

public class AddLetterController {
    File attachmentFile;
    @FXML
    CheckComboBox<String> checkComboBoxEmployee;
    @FXML
    private JFXButton addLetterButton;
    @FXML
    private JFXButton attachmentFileButton;

    @FXML
    private JFXTextField txtLetterName;
    @FXML
    private JFXTextField txtLetterPassword;
    @FXML
    private JFXDatePicker datePickerLetter;
    @FXML
    private JFXTextArea textAreaLetter;

    private TaskDao taskDao;
    private EmployeeDao employeeDao;
    final FileChooser fileChooser=new FileChooser();
    public  void initialize(){
        employeeDao = new EmployeeDaoImpl();
        taskDao = new TaskDaoImpl();
        employeeDao.listEmployees();
        checkComboBoxEmployee.getItems().setAll(employeeDao.listEmployeesName());


    }
    @FXML
    private JFXButton cancelAddLetterButton = new JFXButton();

    public void cancelAddLetterButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddLetterButton.getScene().getWindow();
        stage.close();
    }

    public void addLetterButton(ActionEvent actionEvent) {
        if (txtLetterName.getText().isEmpty() ||  checkComboBoxEmployee.getItems().isEmpty() || datePickerLetter.getValue()==null||textAreaLetter.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
           for (int i = 0; i<checkComboBoxEmployee.getCheckModel().getCheckedIndices().size();i++) {

            Task task = new Task();
            task.setTaskName(txtLetterName.getText());
            task.setTaskText(textAreaLetter.getText());
            task.setTaskAttachment("file");
            task.setTaskFromEmployee(AuthorizedUser.getUser().getEmployeeName());
            task.setEmployeeId(employeeDao.getIdEmployeeByName(checkComboBoxEmployee.getItems().get(checkComboBoxEmployee.getCheckModel().getCheckedIndices().get(i))));
            task.setTaskTerm(java.sql.Date.valueOf(datePickerLetter.getValue()));
            task.setStatusTaskId(StatusTask.NOT_DONE);
            task.setTaskTime(null);
            taskDao.addTask(task);



        }
        Stage stage = (Stage) addLetterButton.getScene().getWindow();
        stage.close();
    }

    }

    public void attachmentFileButton(ActionEvent actionEvent) {
        File file;
        file = fileChooser.showOpenDialog(attachmentFileButton.getScene().getWindow());
        if (file == null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Письмо не выбрано!");
        } else {
           attachmentFile= file;
            System.out.println(attachmentFile.getName());
            System.out.println(attachmentFile.getPath());
        }
    }
}
