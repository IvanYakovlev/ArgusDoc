package controller;

import argusDocSettings.serverFilePath;
import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dao.*;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Employee;
import model.Letter;
import model.StatusTask;
import model.Task;
import org.controlsfx.control.CheckComboBox;
import sun.util.calendar.LocalGregorianCalendar;

import java.io.*;
import java.sql.Date;
import java.util.Calendar;

public class AddLetterController {

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
    @FXML
    private JFXTextField txtLetterNumber;

    private TaskDao taskDao;
    private EmployeeDao employeeDao;
    private LetterDao letterDao;
    final FileChooser fileChooser=new FileChooser();
    File attachmentFile;

    public  void initialize(){
        employeeDao = new EmployeeDaoImpl();
        taskDao = new TaskDaoImpl();
        letterDao = new LetterDaoImpl();
        employeeDao.listEmployees();
        checkComboBoxEmployee.getItems().setAll(employeeDao.listEmployeesName());


    }
    @FXML
    private JFXButton cancelAddLetterButton = new JFXButton();



    public void cancelAddLetterButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddLetterButton.getScene().getWindow();
        stage.close();
    }

    public void addLetterButton(ActionEvent actionEvent) throws IOException {
        if (txtLetterName.getText().isEmpty() ||  checkComboBoxEmployee.getItems().isEmpty() || datePickerLetter.getValue()==null||textAreaLetter.getText().isEmpty()||attachmentFile==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            for (int i = 0; i < checkComboBoxEmployee.getCheckModel().getCheckedIndices().size(); i++) {
//формируем задачи для исполнителей
                Task task = new Task();
                task.setTaskName(txtLetterName.getText());
                task.setTaskText(textAreaLetter.getText());
                task.setTaskAttachment(serverFilePath.LETTERS_FILE_PATH + attachmentFile.getName());
                task.setTaskFromEmployee(AuthorizedUser.getUser().getEmployeeName());
                task.setEmployeeId(employeeDao.getIdEmployeeByName(checkComboBoxEmployee.getItems().get(checkComboBoxEmployee.getCheckModel().getCheckedIndices().get(i))));
                task.setTaskTerm(java.sql.Date.valueOf(datePickerLetter.getValue()));
                task.setStatusTaskId(StatusTask.NOT_DONE);
                task.setTaskTime(null);
                taskDao.addTask(task);


            }

//Добавляем письмо в таблицу и загружаем на сервер
            Letter letter = new Letter();
            letter.setLetterName(txtLetterName.getText());
            letter.setLetterDate(Date.valueOf(datePickerLetter.getValue()));
            letter.setAttachmentFile(attachmentFile);
            letter.setLetterPassword(Integer.parseInt(txtLetterPassword.getText()));
            letter.setLetterNumber(txtLetterNumber.getText());
            letter.setLetterFilePath(serverFilePath.LETTERS_FILE_PATH+attachmentFile.getName());
            letterDao.addLetter(letter);


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

            attachmentFile=file;
        }
    }
}
