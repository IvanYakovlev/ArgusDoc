package controller;

import argusDocSettings.FileManager;
import com.jfoenix.controls.*;
import dialog.ADInfo;
import entity.Employee;
import entity.Letter;
import entity.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoneLetterController {

    @FXML
    private JFXButton downloadLetterFile = new JFXButton();

    @FXML
    private JFXButton cancelDoneLetterButton;
    @FXML
    private JFXButton doneLetterButton;
    @FXML
    private JFXTextArea textAreaLetter;
    @FXML
    private Label labelNameLetter;
    @FXML
    private Label labelNumberLetter;
    @FXML
    private Label lebelDateLetter;

    @FXML
    private JFXTextField juristNumberText;
    @FXML
    private JFXTextArea textAreaOrip;
    @FXML
    private JFXTextField txtTechnicalLiter;
    @FXML
    private JFXTextField txtTechnicalPassword;





    @FXML
    private Label labelJuristFio;
    @FXML
    private Label labelJuristDate;

    @FXML
    private Label labelOripFio;
    @FXML
    private Label labelOripDate;
    @FXML
    private Label labelTechnicalFio;
    @FXML
    private Label labelTechnicalDate;
    @FXML
    private Label labelBookkeepingFio;
    @FXML
    private Label labelbookeepingDate;


    @FXML
    private JFXListView<String> listViewTechnical = new JFXListView<String>();
    @FXML
    private JFXListView<String> listViewJurist = new JFXListView<String>();
    @FXML
    private JFXListView<String> listViewOrip = new JFXListView<String>();
    @FXML
    private JFXListView<String> listViewBookkeeping = new JFXListView<String>();

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
    int taskId;


    private java.sql.Date dateDone;
    private String userDone;

    public void initialize(Employee authorizedUser, int letterId,int taskId) throws RemoteException {

        this.letter = letterService.getLetterById(letterId);
        this.authorizedUser = authorizedUser;
        this.taskId = taskId;

        this.dateDone = new java.sql.Date(System.currentTimeMillis());
        this.userDone = authorizedUser.getEmployeeName();

        labelNameLetter.setText(letter.getLetterName());
        labelNumberLetter.setText(letter.getLetterNumber());
        if (letter.getLetterDate()!=null) {
            lebelDateLetter.setText(String.valueOf(letter.getLetterDate().toLocalDate()));
        }

        if (letter.getLetterJuristFio()!=null&&letter.getLetterJuristDate()!=null) {

                String listJurist = letter.getLetterJuristFio();
                ArrayList<String> arrayJurist = new ArrayList<>();
                for (String fio : listJurist.split("null")) {
                    if (!fio.equals("")) {
                        arrayJurist.add(fio);
                    }

                listViewJurist.getItems().setAll(arrayJurist);
            }
            labelJuristDate.setText(String.valueOf(letter.getLetterJuristDate()));
        }
        if (letter.getLetterOripFio()!=null&&letter.getLetterOripDate()!=null) {

                String listOrip = letter.getLetterOripFio();
                ArrayList<String> arrayOrip = new ArrayList<>();
                for (String fio : listOrip.split("null")) {
                    if (!fio.equals("")) {
                        arrayOrip.add(fio);
                    }
                }
                listViewOrip.getItems().setAll(arrayOrip);

            labelOripDate.setText(String.valueOf(letter.getLetterOripDate()));
        }
        if (letter.getLetterTechnicalFio()!=null&&letter.getLetterTechnicalDate()!=null) {

                String listTechnical = letter.getLetterTechnicalFio();
                ArrayList<String> arrayTechnical = new ArrayList<>();
                for (String fio : listTechnical.split("null")) {
                    if (!fio.equals("")) {
                        arrayTechnical.add(fio);
                    }
                }
                listViewTechnical.getItems().setAll(arrayTechnical);
            labelTechnicalDate.setText(String.valueOf(letter.getLetterTechnicalDate()));
        }
        if (letter.getLetterBookkeepingFio()!=null&&letter.getLetterBookkeepingDate()!=null) {

                String listBookkeeping = letter.getLetterBookkeepingFio();
                ArrayList<String> arrayBookkeeping = new ArrayList<>();
                for (String fio : listBookkeeping.split("null")) {
                    if (!fio.equals("")) {
                        arrayBookkeeping.add(fio);
                    }
                }
                listViewBookkeeping.getItems().setAll(arrayBookkeeping);

            labelbookeepingDate.setText(String.valueOf(letter.getLetterBookkeepingDate()));
        }



        textAreaLetter.setText(letter.getLetterResolution());
        juristNumberText.setText(letter.getLetterJuristNumber());
        textAreaOrip.setText(letter.getLetterOripText());
        txtTechnicalLiter.setText(letter.getLetterTechnicalLiter());
        txtTechnicalPassword.setText(letter.getLetterTechnicalPassword());
        attachmentFile = new File(letter.getLetterFilePath());


        switch (authorizedUser.getDepartmentName()){
            case "Юридический отдел":{
                juristNumberText.setEditable(true);
                break;
            }
            case "Технический отдел":{
                txtTechnicalLiter.setEditable(true);
                txtTechnicalPassword.setEditable(true);
                break;
            }
            case "ОРиП":{
                textAreaOrip.setEditable(true);
                break;
            }
            case "Бухгалтерия":{

                break;
            }
            default:{

                break;
            }


        }


    }




    public void cancelDoneLetterButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelDoneLetterButton.getScene().getWindow();
        stage.close();
    }

    public void doneLetterButton(ActionEvent actionEvent) throws IOException, SQLException {
        Letter letter = new Letter();
        TaskEntity taskEntity = new TaskEntity();
        letter.setLetterId(this.letter.getLetterId());
        switch (authorizedUser.getDepartmentName()){
            case "Юридический отдел":{
                letter.setLetterJuristNumber(juristNumberText.getText());
                letter.setLetterJuristFio(null+this.letter.getLetterJuristFio()+userDone+null);
                letter.setLetterJuristDate(dateDone);
                letterService.updateJuristLetter(letter);

                taskEntity.setTaskId(taskId);
                taskService.removeTask(taskEntity);
                break;
            }
            case "Технический отдел":{
                letter.setLetterTechnicalLiter(txtTechnicalLiter.getText());
                letter.setLetterTechnicalPassword(txtTechnicalPassword.getText());
                letter.setLetterTechnicalFio("null"+this.letter.getLetterTechnicalFio()+userDone+null);
                letter.setLetterTechnicalDate(dateDone);
                letterService.updateTechnicalLetter(letter);

                taskEntity.setTaskId(taskId);
                taskService.removeTask(taskEntity);
                break;
            }
            case "ОРиП":{
                letter.setLetterOripText(textAreaOrip.getText());
                letter.setLetterOripFio(null+this.letter.getLetterOripFio()+userDone+null);
                letter.setLetterOripDate(dateDone);
                letterService.updateOripLetter(letter);

                taskEntity.setTaskId(taskId);
                taskService.removeTask(taskEntity);
                break;
            }
            case "Бухгалтерия":{
                letter.setLetterBookkeepingFio(null+this.letter.getLetterBookkeepingFio()+userDone+null);
                letter.setLetterBookkeepingDate(dateDone);
                letterService.updateBookkeepingLetter(letter);

                taskEntity.setTaskId(taskId);
                taskService.removeTask(taskEntity);
                break;
            }
            default:{

                break;
            }


        }

        Stage stage = (Stage) doneLetterButton.getScene().getWindow();
        stage.close();

    }

    public void openLetterFile(ActionEvent actionEvent) {
        FileManager.openFile(letter.getLetterFilePath());


    }

    public void downloadLetterFile(ActionEvent actionEvent) {
        File file = new File(letter.getLetterFilePath());
        String choosingDirectory = String.valueOf(directoryChooser.showDialog(downloadLetterFile.getScene().getWindow()));
        System.out.println(choosingDirectory);
        FileManager.downloadFile(file, choosingDirectory);
    }
}
