package controller;

import argusDocSettings.ServerFilePath;

import com.jfoenix.controls.*;
import entity.Employee;
import entity.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import java.util.List;
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
    private JFXComboBox<String> juristComboBox;
    @FXML
    private JFXComboBox<String> oripComboBox;
    @FXML
    private JFXComboBox<String> technicalComboBox;
    @FXML
    private JFXComboBox<String> bookkeepingComboBox;


    @FXML
    private JFXDatePicker juristDatePicker;
    @FXML
    private JFXDatePicker oripDatePicker;
    @FXML
    private JFXDatePicker technicalDatePicker;
    @FXML
    private JFXDatePicker bookkeepingDatePicker;


    @FXML
    private JFXButton addLetterButton;

    @FXML
    private JFXButton attachmentFileButton;

    @FXML
    private JFXDatePicker datePickerLetter;

    @FXML
    private JFXTextArea textAreaLetter = new JFXTextArea();
    @FXML
    private JFXTextField txtLetterNumber;
    @FXML
    private JFXComboBox<String> nameLetterComboBox = new JFXComboBox<String>();
    @FXML
    private JFXTextField juristNumberText;
    @FXML
    private JFXTextArea textAreaOripEdit;
    @FXML
    private JFXTextField txtTechnicalLiter;
    @FXML
    private JFXTextField txtTechnicalPassword;
    @FXML
    private JFXDatePicker datePickerBookkeepingDone;

    private ArrayList<String> list = new ArrayList<String>();
    private ObservableList<String> listNameLetter;

    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;

    final FileChooser fileChooser=new FileChooser();
    File attachmentFile;

    Letter letter;

    public  void initialize() throws RemoteException {

//Формируем список для отображения в nameLetterComboBox
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

        employeeService.listEmployees();//вызов метода необходим для заполнения массива listEmployee в классе employeeService

//Заполняем элементы combobox и checkcombobox в соответсвии с прикрепленными там сотрудниками
        juristCheckComboBox.getItems().setAll(employeeService.listEmployeesNameJurist());
        technicalCheckComboBox.getItems().setAll(employeeService.listEmployeesNameTechnical());
        oripCheckComboBox.getItems().setAll(employeeService.listEmployeesNameOrip());
        bookkeepingCheckComboBox.getItems().setAll(employeeService.listEmployeesNameBookkeeping());

        juristComboBox.getItems().setAll(employeeService.listEmployeesNameJurist());
        technicalComboBox.getItems().setAll(employeeService.listEmployeesNameTechnical());
        oripComboBox.getItems().setAll(employeeService.listEmployeesNameOrip());
        bookkeepingComboBox.getItems().setAll(employeeService.listEmployeesNameBookkeeping());
    }




    public void cancelAddLetterButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelAddLetterButton.getScene().getWindow();
        stage.close();
    }

    public void addLetterButton(ActionEvent actionEvent) throws IOException {
        if (textAreaLetter.getText().isEmpty()||attachmentFile==null||nameLetterComboBox.getValue().isEmpty()||txtLetterNumber.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            letter = new Letter();

            //Добавляем письмо в таблицу и загружаем на сервер

            letter.setLetterName(nameLetterComboBox.getValue());
            letter.setLetterNumber(txtLetterNumber.getText());
            letter.setLetterDate(Date.valueOf(datePickerLetter.getValue()));
            letter.setAttachmentFile(attachmentFile);
            letter.setLetterResolution(textAreaLetter.getText());
            letter.setLetterFilePath(ServerFilePath.LETTERS_FILE_PATH+attachmentFile.getName());
            letter.setLetterJuristNumber(juristNumberText.getText());
            letter.setLetterOripText(textAreaOripEdit.getText());
            letter.setLetterTechnicalLiter(txtTechnicalLiter.getText());
            letter.setLetterTechnicalPassword(txtTechnicalPassword.getText());
            if (datePickerBookkeepingDone.getValue()!=null) {
                letter.setLetterBookkeepingDate(Date.valueOf(datePickerBookkeepingDone.getValue()));
            }
            if (juristComboBox.getValue()!=null&&juristDatePicker.getValue()!=null) {
                letter.setLetterJuristFio(juristComboBox.getValue()+null);
                letter.setLetterJuristDate(Date.valueOf(juristDatePicker.getValue()));
            }
            if (oripComboBox.getValue()!=null&&oripDatePicker.getValue()!=null) {
                letter.setLetterOripFio(oripComboBox.getValue()+null);
                letter.setLetterOripDate(Date.valueOf(oripDatePicker.getValue()));
            }
            if (technicalComboBox.getValue()!=null&&technicalDatePicker.getValue()!=null) {
                letter.setLetterTechnicalFio(technicalComboBox.getValue()+null);
                letter.setLetterTechnicalDate(Date.valueOf(technicalDatePicker.getValue()));
            }
            if (bookkeepingComboBox.getValue()!=null&&bookkeepingDatePicker.getValue()!=null) {
                letter.setLetterBookkeepingFio(bookkeepingComboBox.getValue()+null);
                letter.setLetterBookkeepingDate(Date.valueOf(bookkeepingDatePicker.getValue()));
            }


            try {
                //Копируем файл на сервер
                File destFile = new File(letter.getLetterFilePath());
                Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());
                //Добавляем запись в БД
                letterService.addLetter(letter);

            } catch (SQLException e) {
                e.printStackTrace();
            }catch (java.nio.file.NoSuchFileException ex) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не удалось загрузить файл, Хранилище недоступно!");
            }catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                //alert.setTitle("Delete File");
                //alert.setHeaderText("Письмо с таким именем уже существует! Хотите заменить?");


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

            List<String> listEmployeeNameForTask = new ArrayList<String>();

            listEmployeeNameForTask.addAll(juristCheckComboBox.getCheckModel().getCheckedItems());
            listEmployeeNameForTask.addAll(technicalCheckComboBox.getCheckModel().getCheckedItems());
            listEmployeeNameForTask.addAll(oripCheckComboBox.getCheckModel().getCheckedItems());
            listEmployeeNameForTask.addAll(bookkeepingCheckComboBox.getCheckModel().getCheckedItems());

            System.out.println(listEmployeeNameForTask);
            int letterId =0;
            try {
                letterId = letterService.getMaxId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < listEmployeeNameForTask.size(); i++) {
//формируем задачи для исполнителей
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskName(nameLetterComboBox.getValue());
                taskEntity.setTaskText(textAreaLetter.getText());
                taskEntity.setTaskAttachment(ServerFilePath.LETTERS_FILE_PATH + attachmentFile.getName());
                taskEntity.setTaskFromEmployee(authorizedUser.getEmployeeName());
                taskEntity.setEmployeeId(employeeService.getIdEmployeeByName(listEmployeeNameForTask.get(i)));
                taskEntity.setTaskTerm(java.sql.Date.valueOf(datePickerLetter.getValue()));
                taskEntity.setStatusTaskId(StatusTask.NOT_DONE);
                taskEntity.setTaskTime(null);
                taskEntity.setTaskIsLetter(1);
                taskEntity.setLetterId(letterId);



                try {
                    taskService.addTask(taskEntity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

            listEmployeeNameForTask.clear();
            System.out.println(listEmployeeNameForTask);
            Stage stage = (Stage) addLetterButton.getScene().getWindow();
            stage.close();
        }


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
