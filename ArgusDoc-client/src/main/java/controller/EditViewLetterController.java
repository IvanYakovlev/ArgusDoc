package controller;

import argusDocSettings.FileManager;
import argusDocSettings.ServerFilePath;
import com.jfoenix.controls.*;
import dialog.ADInfo;
import entity.Employee;
import entity.Letter;
import entity.StatusTask;
import entity.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditViewLetterController {


    private final FileManager fileManager = new FileManager();
    @FXML
    private JFXButton cancelViewButton = new JFXButton();
    @FXML
    private JFXButton editLetterButton = new JFXButton();
    @FXML
    private JFXButton attachmentFileButton;


//edit pane
    @FXML
    private CheckComboBox<String>  juristCheckComboBox;
    @FXML
    private CheckComboBox<String>  oripCheckComboBox;
    @FXML
    private CheckComboBox<String>  technicalCheckComboBox;
    @FXML
    private CheckComboBox<String>  bookkeepingCheckComboBox;
    @FXML
    private JFXTextArea textAreaLetterEdit;
    @FXML
    private JFXComboBox<String> nameLetterComboBox;
    @FXML
    private JFXTextField txtLetterNumber;
    @FXML
    private JFXDatePicker datePickerLetter;
    @FXML
    private JFXTextField juristNumberText;
    @FXML
    private JFXTextArea textAreaOripEdit;
    @FXML
    private JFXTextField txtTechnicalLiter;
    @FXML
    private JFXTextField txtTechnicalPassword;
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

//view pane
    @FXML
    private JFXButton downloadFileButton = new JFXButton();
    @FXML
    private Label labelJuristNamber;
    @FXML
    private JFXButton openEditView;
    @FXML
    private Label labelJuristDate;
    @FXML
    private JFXTextArea txtAreaOrip;

    @FXML
    private Label labelOripDate;
    @FXML
    private Label labelTechnicalLiter;
    @FXML
    private Label labelTechnicalPassword;

    @FXML
    private Label technicalDate;

    @FXML
    private Label labelbookeepingDate;
    @FXML
    private Pane editPane;
    @FXML
    private Pane viewPane;
    @FXML
    private JFXTextArea textAreaLetterView;

    @FXML
    private JFXListView<String> listViewTechnical = new JFXListView<String>();
    @FXML
    private JFXListView<String> listViewJurist = new JFXListView<String>();
    @FXML
    private JFXListView<String> listViewOrip = new JFXListView<String>();
    @FXML
    private JFXListView<String> listViewBookkeeping = new JFXListView<String>();
    @FXML
    private Label labelLetterName;
    @FXML
    private Label labelLetterNumber;
    @FXML
    private Label labelLetterDate;

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
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    File attachmentFile;


    Letter letter;
    Employee authorizedUser;
    public Boolean okButton = false;

    public void initialize(Employee authorizedUser, int letterId) throws RemoteException {

        this.letter = letterService.getLetterById(letterId);
        this.authorizedUser = authorizedUser;

        if (authorizedUser.getAccessId()==1) {
            openEditView.setVisible(true);
        }
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


        nameLetterComboBox.setValue(letter.getLetterName());
        txtLetterNumber.setText(letter.getLetterNumber());
        datePickerLetter.setValue(letter.getLetterDate().toLocalDate());

        labelLetterName.setText(letter.getLetterName());
        labelLetterNumber.setText(letter.getLetterNumber());
        labelLetterDate.setText(String.valueOf(letter.getLetterDate()));
        //Инициализируем viewPane

        initView();
        //Инициализируем editPane
        employeeService.listEmployees();
        juristCheckComboBox.getItems().setAll(employeeService.listEmployeesNameJurist());
        technicalCheckComboBox.getItems().setAll(employeeService.listEmployeesNameTechnical());
        oripCheckComboBox.getItems().setAll(employeeService.listEmployeesNameOrip());
        bookkeepingCheckComboBox.getItems().setAll(employeeService.listEmployeesNameBookkeeping());
        textAreaLetterEdit.setText(letter.getLetterResolution());
        juristNumberText.setText(letter.getLetterJuristNumber());
        textAreaOripEdit.setText(letter.getLetterOripText());
        txtTechnicalLiter.setText(letter.getLetterTechnicalLiter());
        txtTechnicalPassword.setText(letter.getLetterTechnicalPassword());
        attachmentFile = new File(letter.getLetterFilePath());

        juristComboBox.getItems().setAll(employeeService.listEmployeesNameJurist());
        technicalComboBox.getItems().setAll(employeeService.listEmployeesNameTechnical());
        oripComboBox.getItems().setAll(employeeService.listEmployeesNameOrip());
        bookkeepingComboBox.getItems().setAll(employeeService.listEmployeesNameBookkeeping());

        if (letter.getLetterJuristFio()!=null&&letter.getLetterJuristDate()!=null) {
            juristComboBox.setValue(letter.getLetterJuristFio());
            juristDatePicker.setValue(letter.getLetterJuristDate().toLocalDate());

        }
        if (letter.getLetterOripFio()!=null&&letter.getLetterOripDate()!=null) {
            oripComboBox.setValue(letter.getLetterOripFio());
            oripDatePicker.setValue(letter.getLetterOripDate().toLocalDate());
        }
        if (letter.getLetterTechnicalFio()!=null&&letter.getLetterTechnicalDate()!=null) {


            technicalComboBox.setValue(letter.getLetterTechnicalFio());
            technicalDatePicker.setValue(letter.getLetterTechnicalDate().toLocalDate());
        }
        if (letter.getLetterBookkeepingFio()!=null&&letter.getLetterBookkeepingDate()!=null) {
            bookkeepingComboBox.setValue(letter.getLetterBookkeepingFio());
            bookkeepingDatePicker.setValue(letter.getLetterBookkeepingDate().toLocalDate());
        }


        viewPane.toFront();
    }
    public void nameLetterComboBox(ActionEvent actionEvent) {
    }

    public void attachmentFileButton(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(attachmentFileButton.getScene().getWindow());
        if (file == null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Письмо не выбрано!");
        } else {

            attachmentFile=file;
        }
    }

    public void cancelEditLetterButton(ActionEvent actionEvent) {
        viewPane.toFront();

    }

    public void editLetterButton(ActionEvent actionEvent) {

        if (textAreaLetterEdit.getText().isEmpty()||attachmentFile==null||nameLetterComboBox.getValue().isEmpty()||txtLetterNumber.getText().isEmpty()||datePickerLetter==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            Letter letter = new Letter();

            //Обновляем письмо в таблицу и загружаем на сервер
            letter.setLetterId(this.letter.getLetterId());
            letter.setLetterName(nameLetterComboBox.getValue());
            letter.setLetterNumber(txtLetterNumber.getText());
            letter.setLetterDate(Date.valueOf(datePickerLetter.getValue()));
            letter.setAttachmentFile(attachmentFile);
            letter.setLetterResolution(textAreaLetterEdit.getText());
            letter.setLetterFilePath(ServerFilePath.LETTERS_FILE_PATH+attachmentFile.getName());
            letter.setLetterJuristNumber(juristNumberText.getText());

            letter.setLetterTechnicalLiter(txtTechnicalLiter.getText());
            letter.setLetterTechnicalPassword(txtTechnicalPassword.getText());

            letter.setLetterOripText(textAreaOripEdit.getText());
            if (technicalDatePicker.getValue()!=null&&technicalComboBox.getValue()!=null) {
                letter.setLetterTechnicalFio(technicalComboBox.getValue()+null);
                letter.setLetterTechnicalDate(Date.valueOf(technicalDatePicker.getValue()));
            }
            if (bookkeepingDatePicker.getValue()!=null&&bookkeepingComboBox.getValue()!=null) {
                letter.setLetterBookkeepingDate(Date.valueOf(bookkeepingDatePicker.getValue()));
                letter.setLetterBookkeepingFio(bookkeepingComboBox.getValue()+null);
            }
            if (juristDatePicker.getValue()!=null&&juristComboBox.getValue()!=null) {
                letter.setLetterJuristFio(juristComboBox.getValue()+null);
                letter.setLetterJuristDate(Date.valueOf(juristDatePicker.getValue()));
            }
            if (oripDatePicker.getValue()!=null&&oripComboBox.getValue()!=null) {
                letter.setLetterOripDate(Date.valueOf(oripDatePicker.getValue()));
                letter.setLetterOripFio(oripComboBox.getValue()+null);
            }
            letter.toString();
            try {

                letterService.updateLetter(letter);

                //Копируем файл на сервер
                File destFile = new File(letter.getLetterFilePath());
                Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());

            } catch (IOException e) {
                //e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/images/1.jpg"));
                //alert.setTitle("Delete File");
                alert.setHeaderText("Письмо с таким именем уже существует! Хотите заменить?");


                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    Path path = Paths.get(letter.getLetterFilePath());
                    try {
                        Files.delete(path);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    File destFile = new File(letter.getLetterFilePath());
                    try {
                        Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


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

            for (int i = 0; i < listEmployeeNameForTask.size(); i++) {
//формируем задачи для исполнителей
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskName(nameLetterComboBox.getValue());
                taskEntity.setTaskText(textAreaLetterEdit.getText());
                taskEntity.setTaskAttachment(ServerFilePath.LETTERS_FILE_PATH + attachmentFile.getName());
                taskEntity.setTaskFromEmployee(authorizedUser.getEmployeeName());
                try {
                    taskEntity.setEmployeeId(employeeService.getIdEmployeeByName(listEmployeeNameForTask.get(i)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                taskEntity.setTaskTerm(java.sql.Date.valueOf(datePickerLetter.getValue()));
                taskEntity.setStatusTaskId(StatusTask.NOT_DONE);
                taskEntity.setTaskTime(null);
                taskEntity.setTaskIsLetter(1);
                taskEntity.setLetterId(this.letter.getLetterId());

                try {
                    taskService.addTask(taskEntity);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            viewPane.toFront();
            textAreaLetterView.setText(letter.getLetterResolution());
            labelJuristNamber.setText(letter.getLetterJuristNumber());
            labelLetterName.setText(letter.getLetterName());
            labelLetterNumber.setText(letter.getLetterNumber());
            labelLetterDate.setText(String.valueOf(letter.getLetterDate()));


            initView();
        }
        okButton=true;
    }

    public void openEditView(ActionEvent actionEvent) {
        editPane.toFront();

    }

    public void openLetterFile(ActionEvent actionEvent) {
        FileManager.openFile(letter.getLetterFilePath());


    }

    public void cancelViewButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelViewButton.getScene().getWindow();
        stage.close();
    }
    public void initView(){
        textAreaLetterView.setText(letter.getLetterResolution());
        labelJuristNamber.setText(letter.getLetterJuristNumber());


        if (letter.getLetterJuristFio() != null) {
            String listJurist = letter.getLetterJuristFio();
            ArrayList<String> arrayJurist = new ArrayList<>();
            for (String fio : listJurist.split("null")) {
                if (!fio.equals("")) {
                    arrayJurist.add(fio);
                }
            }
            listViewJurist.getItems().setAll(arrayJurist);
        }

        labelJuristDate.setText(String.valueOf(letter.getLetterJuristDate()));

        txtAreaOrip.setText(letter.getLetterOripText());


        if (letter.getLetterOripFio() != null) {
            String listOrip = letter.getLetterOripFio();
            ArrayList<String> arrayOrip = new ArrayList<>();
            for (String fio : listOrip.split("null")) {
                if (!fio.equals("")) {
                    arrayOrip.add(fio);
                }
            }
            listViewOrip.getItems().setAll(arrayOrip);
        }


        labelOripDate.setText(String.valueOf(letter.getLetterOripDate()));
        labelTechnicalLiter.setText(letter.getLetterTechnicalLiter());
        labelTechnicalPassword.setText(letter.getLetterTechnicalPassword());

        if (letter.getLetterTechnicalFio() != null) {
            String listTechnical = letter.getLetterTechnicalFio();
            ArrayList<String> arrayTechnical = new ArrayList<>();
            for (String fio : listTechnical.split("null")) {
                if (!fio.equals("")) {
                    arrayTechnical.add(fio);
                }
            }
            listViewTechnical.getItems().setAll(arrayTechnical);
        }

        technicalDate.setText(String.valueOf(letter.getLetterTechnicalDate()));


        if (letter.getLetterBookkeepingFio()!= null) {
            String listBookkeeping = letter.getLetterBookkeepingFio();
            ArrayList<String> arrayBookkeeping = new ArrayList<>();
            for (String fio : listBookkeeping.split("null")) {
                if (!fio.equals("")) {
                    arrayBookkeeping.add(fio);
                }
            }
            listViewBookkeeping.getItems().setAll(arrayBookkeeping);
        }

        labelbookeepingDate.setText(String.valueOf(letter.getLetterBookkeepingDate()));
    }

    public void downloadFileButton(ActionEvent actionEvent) {
        File file = new File(letter.getLetterFilePath());
        String choosingDirectory = String.valueOf(directoryChooser.showDialog(downloadFileButton.getScene().getWindow()));
        System.out.println(choosingDirectory);
        FileManager.downloadFile(file, choosingDirectory);
    }

}

