package controller;

import argusDocSettings.serverFilePath;
import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.*;
import dbConnection.DBconnection;


import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.*;
import dialog.ADInfo;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class MainController {
    //Connection
    private DBconnection dBconnection;

    private double xOffset;
    private double yOffset;

    private Task task= new Task();
    private int idEmployee;
    private Document document = new Document();
    private int idDepartment;

    DepartmentDao departmentDao;
    EmployeeDao employeeDao;
    AccessDao accessDao;
    DocumentDao documentDao;
    LetterDao letterDao;
    TaskDao taskDao;

    private ObservableList<Department> dataDepartment;
    private ObservableList<Employee> dataEmployee;
    private ObservableList<Document> dataDocument;

    final FileChooser fileChooser=new FileChooser();


// Window control

    @FXML
    private JFXButton settingTabButton = new JFXButton();
    @FXML
    private JFXButton letterTabButton = new JFXButton();
    @FXML
    private ButtonBar myTaskBtnBar = new ButtonBar();
    @FXML
    private ButtonBar myTaskDoneBtnBar = new ButtonBar();
    @FXML
    private ButtonBar fromEmpTaskBtnBar = new ButtonBar();
    @FXML
    private ButtonBar archiveTaskBtnBar = new ButtonBar();
    @FXML
    private AnchorPane anchorTask = new AnchorPane();
    @FXML
    private AnchorPane anchorTemplate = new AnchorPane();
    @FXML
    private AnchorPane anchorCalendar = new AnchorPane();
    @FXML
    private AnchorPane anchorLetter = new AnchorPane();


//Documents view Tab

    @FXML
    private TableView<Document> tableDocumentTemplate;
    private TableColumn<Document, String> documentNameTemplate;
    private TableColumn<Document, String> documentFilePathTemplate;
    @FXML
    private ComboBox<String> comboBoxDocument_Template = new ComboBox<>();

//Tasks tab

    @FXML
    private TableView<Task> tableTask;
    private TableColumn<Task, String> idTask;
    private TableColumn<Task, String> nameTask;
    private TableColumn<Task, String> textTask;
    private TableColumn<Task, String> attachmentTask;
    private TableColumn<Task, String> employeeTask;
    private TableColumn<Task, String> termTask;
    private TableColumn<Task, String> statusTask;
    private TableColumn<Task, String> sender;
    private TableColumn<Task, String> timeTask;
    @FXML
    private Label labelUserAuth;

//Letter Tab

    @FXML
    private TableView<Letter> tableLetter;
    private TableColumn<Letter, String> idLetter;
    private TableColumn<Letter, String> nameLetter;
    private TableColumn<Letter, String> passwordLetter;
    private TableColumn<Letter, String> numberLetter;
    private TableColumn<Letter, String> filePathLetter;

    public void initialize() {
        accessDao = new AccessDaoImpl();
        employeeDao = new EmployeeDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        documentDao = new DocumentDaoImpl();
        taskDao = new TaskDaoImpl();
        letterDao = new LetterDaoImpl();


    /*initialize table Document Template tab*/
    documentNameTemplate = new TableColumn<Document, String>("Название документа");
    documentNameTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
    documentFilePathTemplate = new TableColumn<Document, String>("Путь файла");
    documentFilePathTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentFilePath"));

    tableDocumentTemplate.getColumns().setAll(documentNameTemplate,documentFilePathTemplate);
    tableDocumentTemplate.setItems(documentDao.listDocuments());
    /*initialize combobox Document template tab*/

    departmentDao.listDepartments();
    comboBoxDocument_Template.setItems(departmentDao.listDepartmentName());
    comboBoxDocument_Template.setPromptText("Выберите отдел:");
    /*initialize Task tab*/

    idTask = new TableColumn<Task, String>("Id");
    idTask.setCellValueFactory(new PropertyValueFactory<Task, String>("taskId"));
    nameTask = new TableColumn<Task, String>("Название задачи");
    nameTask.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
    textTask = new TableColumn<Task, String>("Текст задачи");
    textTask.setCellValueFactory(new PropertyValueFactory<Task, String>("taskText"));
    attachmentTask = new TableColumn<Task, String>("Прикрепленный файл");
    attachmentTask.setCellValueFactory(new PropertyValueFactory<Task, String>("taskAttachment"));
    employeeTask = new TableColumn<Task, String>("Исполнитель");
    employeeTask.setCellValueFactory(new PropertyValueFactory<Task, String>("employeeName"));
    termTask = new TableColumn<Task, String>("Срок выполнения");
    termTask.setCellValueFactory(new PropertyValueFactory<Task, String>("taskTerm"));
    statusTask = new TableColumn<Task, String>("Статус задачи");
    statusTask.setCellValueFactory(new PropertyValueFactory<Task, String>("statusTaskName"));
    sender = new TableColumn<Task, String>("Отправитель");
    sender.setCellValueFactory(new PropertyValueFactory<Task, String>("taskFromEmployee"));
    timeTask = new TableColumn<Task, String>("Время выполнения");
    timeTask.setCellValueFactory(new PropertyValueFactory<Task, String>("taskTime"));


    tableTask.getColumns().setAll(idTask, nameTask, textTask, attachmentTask, employeeTask, sender, termTask, timeTask, statusTask);

    tableTask.setItems(taskDao.listMyTasks(AuthorizedUser.getUser().getEmployeeId()));

    labelUserAuth.setText(AuthorizedUser.getUser().getEmployeeName());
// If access - administrator
    if (AuthorizedUser.getUser().getAccessId()==1) {

        letterTabButton.setVisible(true);
        settingTabButton.setVisible(true);
    /*initialize Letter  tab*/

        idLetter = new TableColumn<Letter, String>("id");
        idLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterId"));
        nameLetter = new TableColumn<Letter, String>("Название");
        nameLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterName"));
        passwordLetter = new TableColumn<Letter, String>("Пароль");
        passwordLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterPassword"));
        numberLetter = new TableColumn<Letter, String>("Номер");
        numberLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterNumber"));
        filePathLetter = new TableColumn<Letter, String>("файл");
        filePathLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterFilePath"));

        tableLetter.getColumns().setAll(idLetter, nameLetter, passwordLetter, numberLetter, filePathLetter);
        tableLetter.setItems(letterDao.listLetter());

        }
    }





/*Document template tab*/
public void clickTableDocumentTemplate(MouseEvent mouseEvent) {

    Document clickDocument = tableDocumentTemplate.getSelectionModel().getSelectedItems().get(0);

        document = clickDocument;


}
    public void choiceDepartmentDocument(ActionEvent actionEvent) {
        tableDocumentTemplate.setItems(documentDao.listDocumentsByDepartment(comboBoxDocument_Template.getValue()));

    }

    public void openDocumentButton(ActionEvent actionEvent) {
        if (document.getDocumentName()!=null) {
            documentDao.openDocument(document.getDocumentId());
        }

    }

    public void printDocumentButton(ActionEvent actionEvent) {
        if (document.getDocumentName()!=null) {
            documentDao.printDocument(document.getDocumentId());
        }

    }


/*Tasks  tab*/
    public void refreshTaskTab(){

        tableTask.setItems(taskDao.listMyTasks(AuthorizedUser.getUser().getEmployeeId()));

    }


    public void openAddTaskButton(ActionEvent actionEvent) throws IOException {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/viewFXML/Add_task_window.fxml"));
            stage.setTitle("Создание задачи");
            stage.setMinHeight(150);
            stage.setMinWidth(300);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.initStyle(StageStyle.TRANSPARENT);
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
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openEditTaskButton(ActionEvent actionEvent) {

        if (task!=null) {
            EditTaskController editTaskController = new EditTaskController();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/Edit_task_window.fxml"));
            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));
                EditTaskController editController = fxmlLoader.getController();
                editController.task =task;
                editController.initTab(task);

                stage.setTitle("Редактирование задачи");
                stage.setMinHeight(150);
                stage.setMinWidth(300);
                stage.setResizable(false);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                stage.initStyle(StageStyle.TRANSPARENT);
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
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    }
                });
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }

    public void openDoneTaskButton(ActionEvent actionEvent) {
        if (task!=null) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/viewFXML/Done_task_window.fxml"));
            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));
                DoneTaskController doneController = fxmlLoader.getController();
                doneController.setTask(task);
                doneController.initTab(task);

                stage.setTitle("Выполнение задачи");
                stage.setMinHeight(150);
                stage.setMinWidth(300);
                stage.setResizable(false);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                stage.initStyle(StageStyle.TRANSPARENT);
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
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    }
                });
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }





    public void myTasksButton(ActionEvent actionEvent) {
        task=null;
        anchorTask.toFront();
        myTaskBtnBar.toFront();
        taskDao = new TaskDaoImpl();
        tableTask.setItems(taskDao.listMyTasks(AuthorizedUser.getUser().getEmployeeId()));
    }

    public void myDoneTasksButton(ActionEvent actionEvent) {
        task=null;
        anchorTask.toFront();
        myTaskDoneBtnBar.toFront();
        taskDao = new TaskDaoImpl();
        tableTask.setItems(taskDao.listMyDoneTasks(AuthorizedUser.getUser().getEmployeeId()));
    }
    public void fromEmpTasjButton(ActionEvent actionEvent) {
        task=null;
        anchorTask.toFront();
        fromEmpTaskBtnBar.toFront();
        taskDao = new TaskDaoImpl();
        tableTask.setItems(taskDao.listFromEmpTasks((AuthorizedUser.getUser().getEmployeeName())));
    }

    public void archiveTasks(ActionEvent actionEvent) {
        task=null;
        anchorTask.toFront();
        archiveTaskBtnBar.toFront();
        taskDao = new TaskDaoImpl();
        tableTask.setItems(taskDao.listArchiveTasks(StatusTask.CANCELED));

    }

    public void clickTableTask(MouseEvent mouseEvent) {
        Task task = tableTask.getSelectionModel().getSelectedItems().get(0);
        if (task!=null){
            this.task = task;
        }
    }

    public void canceledTaskButton(ActionEvent actionEvent) {
        if (task!=null) {
            taskDao.canceledTask(task.getTaskId());
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }


    public void deleteTaskButton(ActionEvent actionEvent) {
        if (task!=null){
            taskDao = new TaskDaoImpl();
            taskDao.removeTask(task.getTaskId());
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }
    public void templateTabButton(ActionEvent actionEvent) {
        departmentDao.listDepartments();
        tableDocumentTemplate.setItems(dataDocument);
        anchorTemplate.toFront();
    }

    public void calendarTabButton(ActionEvent actionEvent) {
        anchorCalendar.toFront();
    }

    public void letterTabButton(ActionEvent actionEvent) {

        anchorLetter.toFront();
        tableLetter.setItems(letterDao.listLetter());
    }

    public void settingTabButton(ActionEvent actionEvent) {

            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/viewFXML/Setting_window.fxml"));
            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));

                stage.setTitle("Новая задача");

                stage.setMinWidth(800);
                stage.setMinHeight(470);
                stage.setResizable(false);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                stage.initStyle(StageStyle.TRANSPARENT);
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
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    }
                });
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

    }


//Letter Tab
    public void openLetter(ActionEvent actionEvent) {
    }

    public void openAddLetterWindow(ActionEvent actionEvent) {
        if (true) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/viewFXML/Add_letter_window.fxml"));
            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));
               /* DoneTaskController doneController = fxmlLoader.getController();
                doneController.setTask(task);
                doneController.initTab(task);*/

                stage.setTitle("Новая задача");
                stage.setMinHeight(150);
                stage.setMinWidth(300);
                stage.setResizable(false);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                stage.initStyle(StageStyle.TRANSPARENT);
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
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    }
                });
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }

    public void removeLetter(ActionEvent actionEvent) {
    }
}
