package controller;

import argusDocSettings.ServerFilePath;

import com.jfoenix.controls.*;
import entity.Employee;
import entity.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import service.*;
import dialog.ADInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entity.Letter;
import entity.StatusTask;
import org.controlsfx.control.CheckComboBox;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class AddLetterController {


    public Employee authorizedUser ;


    @FXML
    private JFXButton cancelAddLetterButton = new JFXButton();
    @FXML
    private CheckComboBox<String> juristCheckComboBox;

    @FXML
    private CheckComboBox<String> technicalCheckComboBox;
    @FXML
    private CheckComboBox<String> oripCheckComboBox;
    @FXML
    private CheckComboBox<String> bookkeepingCheckComboBox;

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
    @FXML
    private JFXComboBox<String> nameLetterComboBox = new JFXComboBox<String>();
    private ArrayList<String> list = new ArrayList<String>();
    private ObservableList<String> listNameLetter;

    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;
    final FileChooser fileChooser=new FileChooser();
    File attachmentFile;




    public  void initialize() throws RemoteException {

        //employeeService.listEmployees();

        list.add("Постановка объекта на охрану");
        list.add("Включение ответственных");
        list.add("Исключение ответственных");
        list.add("Возобновление договора");
        list.add("Изменение режима работы");
        list.add("Изменение номера телефона для СМС");
        list.add("Подключение услуги СМС");
        list.add("Отключение услуги СМС");
        list.add("Подключение услуги Telegram");
        list.add("Отключение услуги Telegram");
        list.add("Постановка объекта под охрану");
        list.add("Расторжение договора о постановке");
        list.add("Приостановка договора об охране");
        list.add("Отчет о постановке/снятии за период");
        list.add("Проведение сверки");
        list.add("Заявление ответственного лица с договором");
        list.add("Гарантийное письмо");
        list.add("Изготовление ключей");
        list.add("Коммерческое предложения");
        listNameLetter = FXCollections.observableArrayList(list);

        nameLetterComboBox.setItems(listNameLetter);
        employeeService.listEmployees();
        juristCheckComboBox.getItems().setAll(employeeService.listEmployeesNameJurist());
        System.out.println(employeeService.listEmployeesNameTechnical());
        technicalCheckComboBox.getItems().setAll(employeeService.listEmployeesNameTechnical());
        oripCheckComboBox.getItems().setAll(employeeService.listEmployeesNameTechnical());
        bookkeepingCheckComboBox.getItems().setAll(employeeService.listEmployeesNameBookkeeping());

    }




    public void cancelAddLetterButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddLetterButton.getScene().getWindow();
        stage.close();
    }

    public void addLetterButton(ActionEvent actionEvent) throws IOException {/*
        if (txtLetterName.getText().isEmpty() ||  checkComboBoxEmployee.getItems().isEmpty() || datePickerLetter.getValue()==null||textAreaLetter.getText().isEmpty()||attachmentFile==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            for (int i = 0; i < checkComboBoxEmployee.getCheckModel().getCheckedIndices().size(); i++) {
//формируем задачи для исполнителей
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskName(txtLetterName.getText());
                taskEntity.setTaskText(textAreaLetter.getText());
                taskEntity.setTaskAttachment(ServerFilePath.LETTERS_FILE_PATH + attachmentFile.getName());
                taskEntity.setTaskFromEmployee(authorizedUser.getEmployeeName());
                taskEntity.setEmployeeId(employeeService.getIdEmployeeByName(checkComboBoxEmployee.getItems().get(checkComboBoxEmployee.getCheckModel().getCheckedIndices().get(i))));
                taskEntity.setTaskTerm(java.sql.Date.valueOf(datePickerLetter.getValue()));
                taskEntity.setStatusTaskId(StatusTask.NOT_DONE);
                taskEntity.setTaskTime(null);
                taskEntity.setTaskIsLetter(1);

                try {
                    taskService.addTask(taskEntity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

//Добавляем письмо в таблицу и загружаем на сервер
            Letter letter = new Letter();
            letter.setLetterName(txtLetterName.getText());
            letter.setLetterDate(Date.valueOf(datePickerLetter.getValue()));
            letter.setAttachmentFile(attachmentFile);
            letter.setLetterPassword(Integer.parseInt(txtLetterPassword.getText()));
            letter.setLetterNumber(txtLetterNumber.getText());
            letter.setLetterFilePath(ServerFilePath.LETTERS_FILE_PATH+attachmentFile.getName());
            try {
                letterService.addLetter(letter);
            } catch (SQLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                //alert.setTitle("Delete File");
                alert.setHeaderText("Письмо с таким именем уже существует! Хотите заменить?");


                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    Path path = Paths.get(letter.getLetterFilePath());
                    Files.delete(path);
                    File destFile = new File(letter.getLetterFilePath());
                    Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());



                } else if (option.get() == ButtonType.CANCEL) {

                } else {

                }
            }

            Stage stage = (Stage) addLetterButton.getScene().getWindow();
            stage.close();
        }


*/
    }

    public void attachmentFileButton(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(attachmentFileButton.getScene().getWindow());
        if (file == null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Письмо не выбрано!");
        } else {

            attachmentFile=file;
        }
    }

    public void nameLetterComboBox(ActionEvent actionEvent) {
    }
}
