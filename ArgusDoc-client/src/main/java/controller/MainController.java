package controller;


import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Duration;
import notification.NotificationEvent;
import service.*;



import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import jfxtras.scene.control.CalendarPicker;
import entity.*;
import dialog.ADInfo;


import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainController {
    NotificationEvent notificationEvent=new NotificationEvent();
    public Employee authorizedUser;


    private ObservableList<Document> observableListDocument ;
    private ObservableList<Document> observableListDocumentsByDepartment ;

    private ObservableList<TaskEntity> observableListMyTaskEntity ;
    private ObservableList<TaskEntity> observableListMyLetterTaskEntity ;
    private ObservableList<TaskEntity> observableListMyDoneTaskEntity;
    private ObservableList<TaskEntity> observableListFromEmpTaskEntity;
    private ObservableList<TaskEntity> observableListArchiveTaskEntity;

    private ObservableList<Event> observableListSelectDayEvent;
    private ObservableList<Event> observableListAllEvent;

    private ObservableList<String> observableListDepartmentName;
    private ObservableList<Letter> observableListLetter;



    String statusTab="myTask";


    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);


    java.sql.Date datesql = new java.sql.Date(calendar.getTimeInMillis());


    Date date = new Date();
    SimpleDateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat dateForView = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());


    private double xOffset;
    private double yOffset;

    private TaskEntity taskEntity;
    private Document document = new Document();
    private Letter letter = new Letter();
    private Event event = new Event();

    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;

// Window control

    @FXML
    private JFXButton myTasksButton = new JFXButton();
    @FXML
    private JFXButton myLetterButton = new JFXButton();
    @FXML
    private JFXButton fromEmpTaskButton = new JFXButton();
    @FXML
    private JFXButton calendarTabButton = new JFXButton();

    @FXML
    private JFXProgressBar progressBar = new JFXProgressBar();

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
    private TableView<TaskEntity> tableTask;
    private TableColumn<TaskEntity, String> idTask;
    private TableColumn<TaskEntity, String> nameTask;
    private TableColumn<TaskEntity, String> textTask;
    private TableColumn<TaskEntity, String> attachmentTask;
    private TableColumn<TaskEntity, String> employeeTask;
    private TableColumn<TaskEntity, String> termTask;
    private TableColumn<TaskEntity, String> statusTask;
    private TableColumn<TaskEntity, String> sender;
    private TableColumn<TaskEntity, String> timeTask;
    private TableColumn<TaskEntity, String> isLetter;
    @FXML
    private Label labelUserAuth;
// Calendar Tab
    @FXML TableView<Event> tableEvent;
    private TableColumn<Event, String> nameEvent;
    private TableColumn<Event, String> dateEvent;
    private TableColumn<Event, String> timeEvent;
    @FXML
    private Label labelSelectedDate;
    @FXML
    private TextArea textAreaEvent;
    @FXML
    private CalendarPicker calendarPicker;
    @FXML
    private JFXTimePicker timePickerEvent;
    @FXML
    private JFXToggleButton repeateEventToggleBtn;
    @FXML
    private JFXComboBox<String> comboBoxRepeate;


//Letter Tab
    @FXML
    private TextField txtFilter;
    @FXML
    private TableView<Letter> tableLetter;
    private TableColumn<Letter, String> idLetter;
    private TableColumn<Letter, String> nameLetter;
    private TableColumn<Letter, String> passwordLetter;
    private TableColumn<Letter, String> numberLetter;
    private TableColumn<Letter, String> filePathLetter;
    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }



    public void initialize(Employee authorizedUser) throws RemoteException {
        this.authorizedUser=authorizedUser;
        refreshData();


        serviceStart();
    /*initialize table Document Template tab*/
//заполняем таблицу данными
        documentNameTemplate = new TableColumn<Document, String>("Название документа");
        documentNameTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
        documentFilePathTemplate = new TableColumn<Document, String>("Путь файла");
        documentFilePathTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentFilePath"));

        tableDocumentTemplate.getColumns().setAll(documentNameTemplate);

        tableDocumentTemplate.setItems(observableListDocument);
//задаем размер колонок в таблицу
        documentNameTemplate.prefWidthProperty().bind(tableTask.widthProperty().multiply(1));


    /*initialize combobox Document template tab*/

        departmentService.listDepartments();

        comboBoxDocument_Template.setItems(observableListDepartmentName);
        comboBoxDocument_Template.setPromptText("Выберите отдел:");
    /*initialize TaskEntity tab*/
//заполняем таблицу данными
        idTask = new TableColumn<TaskEntity, String>("Id");
        idTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskId"));
        nameTask = new TableColumn<TaskEntity, String>("Название задачи");
        nameTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskName"));
        textTask = new TableColumn<TaskEntity, String>("Текст задачи");
        textTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskText"));
        attachmentTask = new TableColumn<TaskEntity, String>("Прикрепленный файл");
        attachmentTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskAttachment"));
        employeeTask = new TableColumn<TaskEntity, String>("Исполнитель");
        employeeTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("employeeName"));
        termTask = new TableColumn<TaskEntity, String>("Дата");
        termTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskTerm"));
        statusTask = new TableColumn<TaskEntity, String>("");
        statusTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("statusTaskId"));

        sender = new TableColumn<TaskEntity, String>("Отправитель");
        sender.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskFromEmployee"));
        timeTask = new TableColumn<TaskEntity, String>("Время");
        timeTask.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskTime"));
        isLetter = new TableColumn<TaskEntity, String>("Письмо");
        isLetter.setCellValueFactory(new PropertyValueFactory<TaskEntity, String>("taskIsLetter"));

// задаем размер колонок в таблице
        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);

        tableTask.setItems(observableListMyTaskEntity);

        labelUserAuth.setText(authorizedUser.getEmployeeName());


        colorRow();//цвет ячеек

//Calendar Tab
        nameEvent = new TableColumn<Event, String>("Напоминание");
        nameEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventName"));
        dateEvent = new TableColumn<Event, String>("Дата");
        dateEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventDate"));
        timeEvent = new TableColumn<Event, String>("Время");
        timeEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventTime"));

        timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.30));
        nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.70));
        dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));


        tableEvent.getColumns().setAll(timeEvent, nameEvent ,dateEvent);

        tableEvent.setItems(observableListSelectDayEvent);

        timePickerEvent.setIs24HourView(true);


        calendarPicker.calendarProperty().addListener( (observable) -> {
            if (calendarPicker.getCalendar()!=null) {
                calendar = calendarPicker.getCalendar();
                datesql = new java.sql.Date(calendar.getTimeInMillis());



                int selectedMonth=calendar.get(Calendar.MONTH);
                int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
                int selectedYear = calendar.get(Calendar.YEAR);
                System.out.println(year+" "+day+" "+month);
                System.out.println(selectedYear+" "+selectedDay+" "+selectedMonth);
                if (month==selectedMonth&&day==selectedDay&&year==selectedYear){
                    labelSelectedDate.setText("сегодня");
                } else {
                    labelSelectedDate.setText(dateForView.format(datesql));
                }
                timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.30));
                nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.70));
                dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
                try {
                    refreshData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else {
                datesql = new java.sql.Date(calendar.getInstance().getTimeInMillis());


                labelSelectedDate.setText("сегодня");
                timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.30));
                nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.70));
                dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
                tableEvent.setItems(observableListSelectDayEvent);
                try {
                    refreshData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                }
        });





// If access - administrator
    if (authorizedUser.getAccessId()==1) {

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


        tableLetter.getColumns().setAll(numberLetter, nameLetter, passwordLetter);

        numberLetter.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.25));
        nameLetter.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.50));
        passwordLetter.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.24));


        tableLetter.setItems(observableListLetter);


    }


        anchorTask.toFront();
        myTaskBtnBar.toFront();
    }
/*
Calendar tab
* */
    public void clickTableEvent(MouseEvent mouseEvent) {
        Event event = tableEvent.getSelectionModel().getSelectedItems().get(0);
        if (event!=null){
            this.event = event;
        }

    }

    public void removeEvent(ActionEvent actionEvent) throws RemoteException {
        if (event!=null){
            try {

                ////////////
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                //alert.setTitle("Delete File");
                alert.setHeaderText("Вы действительно хотите удалить напоминание?");

                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    eventService.removeEvent(event);

                } else if (option.get() == ButtonType.CANCEL) {

                } else {

                }
                ////////////


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        refreshTableEvent();
    }

    public void addEvent(ActionEvent actionEvent) throws RemoteException {
        if (textAreaEvent.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите текст напоминания!");
        } else {
            Event event = new Event();
            event.setEventName(textAreaEvent.getText());
            event.setEventTime(Time.valueOf(timePickerEvent.getValue()));
            event.setEventDate(datesql);
            event.setEmployeeId(authorizedUser.getEmployeeId());

            try {
                eventService.addEvent(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            clearEventText();
            refreshTableEvent();

        }
    }

    private void refreshTableEvent() throws RemoteException {


        tableEvent.setItems(observableListSelectDayEvent);
    }

    private void clearEventText() {
        textAreaEvent.setText("");
        timePickerEvent.setValue(null);
    }

    public void repeateEventToggleBtn(ActionEvent actionEvent) {

    }

    public void showAllEventButton(ActionEvent actionEvent) throws RemoteException {

        tableEvent.setItems(observableListAllEvent);
        tableEvent.getColumns().setAll(dateEvent, timeEvent, nameEvent);
        nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.50));
        dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.25));
        timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.25));
    }
    public void clickCalendarPicker(MouseEvent mouseEvent) {

    }


/*Document template tab*/
    public void clickTableDocumentTemplate(MouseEvent mouseEvent) {

    Document clickDocument = tableDocumentTemplate.getSelectionModel().getSelectedItems().get(0);

        document = clickDocument;


    }
    public void choiceDepartmentDocument(ActionEvent actionEvent) throws RemoteException {

        tableDocumentTemplate.setItems(observableListDocumentsByDepartment);

    }

    public void openDocumentButton(ActionEvent actionEvent) throws RemoteException {

        try {

            File file = new File(document.getDocumentFilePath());
            java.awt.Desktop.getDesktop().open(file);

        }catch (NullPointerException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не выбран!");
        }catch (IllegalArgumentException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не найден!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printDocumentButton(ActionEvent actionEvent) throws RemoteException {
       try {
           File file = new File(document.getDocumentFilePath());
           java.awt.Desktop.getDesktop().print(file);

       }catch (NullPointerException e){
           ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не выбран!");
       }  catch (IOException e) {
           e.printStackTrace();
       }catch (IllegalArgumentException e) {
           ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не найден!");
       }

        // }

    }


/*Tasks  tab*/

    public void openAddTaskButton(ActionEvent actionEvent) throws IOException {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFXML/Add_task_window.fxml"));

            Parent root = loader.load();

            AddTaskController addTaskController = loader.getController();
            addTaskController.authorizedUser=authorizedUser;


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

        if (taskEntity !=null) {
            EditTaskController editTaskController = new EditTaskController();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/Edit_task_window.fxml"));


            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));
                EditTaskController editController = fxmlLoader.getController();
                editController.authorizedUser= authorizedUser;
                editController.taskEntity = taskEntity;
                editController.initTab(taskEntity);

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

    public void openDoneTaskButton(ActionEvent actionEvent) throws RemoteException {

        try {

            if (taskEntity.getTaskIsLetter()==0) {

                if (taskEntity != null) {

                    FXMLLoader fxmlLoader = new FXMLLoader();

                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/Done_task_window.fxml"));
                    try {

                        fxmlLoader.load();
                        Stage stage = new Stage();
                        Parent root = fxmlLoader.getRoot();
                        stage.setScene(new Scene(root));
                        DoneTaskController doneController = fxmlLoader.getController();
                        doneController.setTaskEntity(taskEntity);
                        doneController.initTab(taskEntity);

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
                } else {
                    ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
                }
            } else {
                if (taskEntity!=null) {
                    FXMLLoader fxmlLoader = new FXMLLoader();

                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/Done_letter_window.fxml"));
                    try {

                        fxmlLoader.load();
                        Stage stage = new Stage();
                        Parent root = fxmlLoader.getRoot();
                        stage.setScene(new Scene(root));
                        DoneLetterController doneLetterController = fxmlLoader.getController();
                        doneLetterController.initialize(authorizedUser, taskEntity.getLetterId(), taskEntity.getTaskId());

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

        }catch (NullPointerException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }

    taskEntity=null;

    }



    public void myLetterButton(ActionEvent actionEvent) {


        anchorTask.toFront();
        myTaskBtnBar.toFront();

        ObservableList<TaskEntity> list = observableListMyLetterTaskEntity;

        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {
                        tableTask.setDisable(true);
                        progressBar.setVisible(true);

                        final int max = 100;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }

                        statusTab="myLetter";
                        taskEntity =null;

                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.50));
                        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.20));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));

                        tableTask.setItems(list);
                        // задаем размер колонок в таблице
                        colorRow();

                        progressBar.setVisible(false);
                        tableTask.setDisable(false);
                        return null;
                    }
                };
            }
        };


        Platform.runLater(() ->{
        service.start();
        progressBar.progressProperty().bind(service.progressProperty());
        });

    }
    public void myTasksButton(ActionEvent actionEvent) throws RemoteException {


        anchorTask.toFront();
        myTaskBtnBar.toFront();
        ObservableList<TaskEntity> list = observableListMyTaskEntity;

        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {
                        tableTask.setDisable(true);
                        progressBar.setVisible(true);

                        final int max = 100;
                for (int i=1; i<=max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);
                }


                        statusTab="myTask";
                        taskEntity =null;




                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


                        updateProgress(60, max);


                        tableTask.setItems(list);
                        // задаем размер колонок в таблице
                        colorRow();






                        progressBar.setVisible(false);
                        tableTask.setDisable(false);
                        updateProgress(max, max);


                        return null;
                    }
                };
            }
        };


        Platform.runLater(() ->{
        service.start();
        progressBar.progressProperty().bind(service.progressProperty());

        });
    }

    public void myDoneTasksButton(ActionEvent actionEvent) throws RemoteException {

        anchorTask.toFront();
        myTaskDoneBtnBar.toFront();
        ObservableList<TaskEntity> list = observableListMyDoneTaskEntity;


        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {

                        tableTask.setDisable(true);
                        progressBar.setVisible(true);
                        final int max = 100;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }
                        statusTab="myDoneTask";
                        taskEntity =null;


                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));

                        tableTask.setItems(list);
                        // задаем размер колонок в таблице
                        colorRow();

                        tableTask.setDisable(false);
                        progressBar.setVisible(false);

                        return null;
                    }
                };
            }
        };

        Platform.runLater(() ->{
        service.start();
        progressBar.progressProperty().bind(service.progressProperty());
        });
    }
    public void fromEmpTaskButton(ActionEvent actionEvent) throws RemoteException {


        anchorTask.toFront();
        fromEmpTaskBtnBar.toFront();
        ObservableList<TaskEntity> list = observableListFromEmpTaskEntity;

        tableTask.getColumns().setAll(nameTask, employeeTask, termTask, timeTask, statusTask);

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {

                        tableTask.setDisable(true);
                        progressBar.setVisible(true);
                        final int max = 1000;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }
                        statusTab="fromEmpTask";
                        taskEntity =null;

                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                        employeeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


                        tableTask.setItems(list);
                        colorRow();
                        // задаем размер колонок в таблице

                        tableTask.setDisable(false);
                        progressBar.setVisible(false);
                        return null;
                    }
                };
            }
        };

        Platform.runLater(() ->{
        service.start();
        progressBar.progressProperty().bind(service.progressProperty());

        });

    }

    public void archiveTasks(ActionEvent actionEvent) throws RemoteException {

        anchorTask.toFront();
        archiveTaskBtnBar.toFront();

        tableTask.getColumns().setAll(nameTask,sender, employeeTask, termTask, timeTask,statusTask);

        ObservableList<TaskEntity> list = observableListArchiveTaskEntity;


        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {

                        tableTask.setDisable(true);
                        progressBar.setVisible(true);
                        final int max = 1000;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }

                        statusTab="archiveTask";
                        taskEntity =null;

                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.25));
                        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.25));
                        employeeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.20));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


                        tableTask.setItems(list);
                        colorRow();
                        // задаем размер колонок в таблице





                        tableTask.setDisable(false);
                        progressBar.setVisible(false);
                        return null;
                    }
                };
            }
        };

        Platform.runLater(() ->{
        service.start();
        progressBar.progressProperty().bind(service.progressProperty());
        });


    }

    public void clickTableTask(MouseEvent mouseEvent) {
        TaskEntity taskEntity = tableTask.getSelectionModel().getSelectedItems().get(0);
        if (taskEntity !=null){
            this.taskEntity = taskEntity;
            System.out.println(taskEntity.toString());
        }

    }

    public void acceptTask(ActionEvent actionEvent) throws RemoteException {
        if (taskEntity !=null) {
            taskService.performedTask(taskEntity.getTaskId());
            refreshData();
        }
    }


    public void deleteTaskButton(ActionEvent actionEvent) throws RemoteException {
        if (taskEntity !=null){

            try {
                ////////////
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                //alert.setTitle("Delete File");
                alert.setHeaderText("Вы действительно хотите удалить задачу?");

                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    taskService.removeTask(this.taskEntity);
//удаляем файл с сервера, если это не письмо
                    if (taskEntity.getTaskAttachment()!=null&& taskEntity.getTaskIsLetter()==0) {
                        Path path = Paths.get(taskEntity.getTaskAttachment());
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.out.println("файл уже удален");
                            // ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "");
                        }
                    }

                } else if (option.get() == ButtonType.CANCEL) {

                } else {

                }
                ////////////




            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }
    public void templateTabButton(ActionEvent actionEvent) throws RemoteException {
        anchorTemplate.toFront();

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {
                        tableDocumentTemplate.setDisable(true);
                        progressBar.setVisible(true);
                        final int max = 1000;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }

                        statusTab="templateTab";

                        departmentService.listDepartments();

                        tableDocumentTemplate.setItems(observableListDocument);
         /*       ObservableList<String> observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
                comboBoxDocument_Template.setItems(observableListDepartmentName);*/

                        tableDocumentTemplate.setDisable(false);
                        progressBar.setVisible(false);



                        return null;
                    }
                };
            }
        };

        service.start();
        progressBar.progressProperty().bind(service.progressProperty());
    }

    public void calendarTabButton(ActionEvent actionEvent) {


        anchorCalendar.toFront();

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {

                        progressBar.setVisible(true);
                        final int max = 1000;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }
                        statusTab="calendarTab";

                        progressBar.setVisible(false);
                        return null;
                    }
                };
            }
        };

        service.start();
        progressBar.progressProperty().bind(service.progressProperty());
    }

    public void letterTabButton(ActionEvent actionEvent) throws RemoteException {
        statusTab="letterTab";
        anchorLetter.toFront();
        ObservableList<Letter> list = observableListLetter;
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override public Void call() throws RemoteException {
                        tableLetter.setDisable(true);
                        progressBar.setVisible(true);
                        final int max = 1000;
                        for (int i=1; i<=max; i++) {
                            if (isCancelled()) {
                                break;
                            }
                            updateProgress(i, max);
                        }

                        tableLetter.setItems(list);

                        tableLetter.setDisable(false);
                        progressBar.setVisible(false);
                        return null;
                    }
                };
            }
        };

        service.start();
        progressBar.progressProperty().bind(service.progressProperty());

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

    /*    Task task = new Task<Void>() {

            @Override public Void call() throws RemoteException {



                return null;
            }
        };
        new Thread(task).start();*/


    }


//Letter Tab




    public void openViewEditLetterWindow(ActionEvent actionEvent) {


        if (letter.getLetterName()!=null) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/viewFXML/view-edit_letter_window.fxml"));
            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));
                EditViewLetterController editViewLetterController = fxmlLoader.getController();
                editViewLetterController.initialize(authorizedUser, letter.getLetterId());

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

    public void openAddLetterWindow(ActionEvent actionEvent) {
        if (true) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/viewFXML/Add_letter_window.fxml"));
            try {

                fxmlLoader.load();
                Stage stage = new Stage();
                Parent root = fxmlLoader.getRoot();
                stage.setScene(new Scene(root));
                AddLetterController addLetterController = fxmlLoader.getController();
                addLetterController.authorizedUser=authorizedUser;

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

    public void removeLetter(ActionEvent actionEvent) throws RemoteException {
        try {
            ////////////
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            //alert.setTitle("Delete File");
            alert.setHeaderText("Вы действительно хотите удалить письмо?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                letterService.removeLetter(letter.getLetterId(), letter.getLetterFilePath());

            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }
            ////////////



        } catch (IOException e) {
            System.out.println("Файл уже удален!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clickLetterTable(MouseEvent mouseEvent) {
        Letter letter = tableLetter.getSelectionModel().getSelectedItems().get(0);
        if (letter!=null){
            this.letter = letter;
        }
    }



    private void colorRow() {
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            statusTask.setCellFactory(column -> {
                                return new TableCell<TaskEntity, String>() {
                                    @Override
                                    protected void updateItem(String item, boolean empty) {

                                        setStyle("");
                                        super.updateItem(item, empty);

                                        setText(empty ? "" : getItem().toString());
                                        setGraphic(null);

                                        TableRow<TaskEntity> currentRow = getTableRow();

                                        if (!isEmpty()) {
                                            switch (item) {
                                                case "1":
                                                    currentRow.setStyle("-fx-background-color:#6BFF61");
                                                    break;
                                                case "2":
                                                    currentRow.setStyle("-fx-background-color:#FBCEB1; -fx-font-weight: bold; -fx-font-size: 16");
                                                    break;
                                                case "3":
                                                    currentRow.setStyle("-fx-background-color:#F3FF80");
                                                    break;
                                                case "4":
                                                    currentRow.setStyle("-fx-background-color:red");
                                                    break;
                                                case "5":
                                                    currentRow.setStyle("-fx-background-color:#BCCDC4");
                                                    break;
                                                default:
                                                    currentRow.setStyle("");
                                                    break;
                                            }
                                        }
                                    }
                                };
                            });
                            return null;
                        }
                    };
                }
            };


        Platform.runLater(() -> {
            service.start();
        });
    }

    public void keyPressSort(KeyEvent keyEvent) throws RemoteException {
           /*Сортировка*/

        switch (statusTab){
            case "myTask":
// 1. Wrap the ObservableList in a FilteredList (initially display all data).
              //  ObservableList<TaskEntity> observableListMyTaskEntities = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
                FilteredList<TaskEntity> filteredMyTaskEntity = new FilteredList<TaskEntity>(observableListMyTaskEntity, p -> true);

                // 2. Set the filter Predicate whenever the filter changes.
                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredMyTaskEntity.setPredicate(task -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false; // Does not match.
                    });
                });

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<TaskEntity> sortedMyTaskEntity = new SortedList<>(filteredMyTaskEntity);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedMyTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();
                // 5. Add sorted (and filtered) data to the table.
                tableTask.setItems(sortedMyTaskEntity);


                break;
            case "myDoneTask":
                // 1. Wrap the ObservableList in a FilteredList (initially display all data).
               // ObservableList<TaskEntity> observableListMyDoneTaskEntities = FXCollections.observableArrayList(taskService.listMyDoneTasks(authorizedUser.getEmployeeId()));
                FilteredList<TaskEntity> filteredMyDoneTaskEntity = new FilteredList<TaskEntity>(observableListMyDoneTaskEntity, p -> true);

                // 2. Set the filter Predicate whenever the filter changes.
                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredMyDoneTaskEntity.setPredicate(task -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false; // Does not match.
                    });
                });

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<TaskEntity> sortedMyDoneTaskEntity = new SortedList<>(filteredMyDoneTaskEntity);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedMyDoneTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();
                // 5. Add sorted (and filtered) data to the table.
                tableTask.setItems(sortedMyDoneTaskEntity);
                break;
            case "fromEmpTask":
                // 1. Wrap the ObservableList in a FilteredList (initially display all data).
                //ObservableList<TaskEntity> observableListFromEmpTaskEntities = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                FilteredList<TaskEntity> filteredFromEmpTaskEntity = new FilteredList<TaskEntity>(observableListFromEmpTaskEntity, p -> true);

                // 2. Set the filter Predicate whenever the filter changes.
                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredFromEmpTaskEntity.setPredicate(task -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (task.getEmployeeName().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false; // Does not match.
                    });
                });

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<TaskEntity> sortedFromEmpTaskEntity = new SortedList<>(filteredFromEmpTaskEntity);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedFromEmpTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();
                // 5. Add sorted (and filtered) data to the table.
                tableTask.setItems(sortedFromEmpTaskEntity);
                break;
            case "archiveTask":
                // 1. Wrap the ObservableList in a FilteredList (initially display all data).
                //ObservableList<TaskEntity> observableListArchiveTaskEntities = FXCollections.observableArrayList(taskService.listArchiveTasks(Integer.parseInt(StatusTask.CANCELED)));
                FilteredList<TaskEntity> filteredArchiveTaskEntity = new FilteredList<TaskEntity>(observableListArchiveTaskEntity, p -> true);

                // 2. Set the filter Predicate whenever the filter changes.
                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredArchiveTaskEntity.setPredicate(task -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (task.getEmployeeName().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        } else if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false; // Does not match.
                    });
                });

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<TaskEntity> sortedTaskEntityArchive = new SortedList<>(filteredArchiveTaskEntity);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedTaskEntityArchive.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();
                // 5. Add sorted (and filtered) data to the table.
                tableTask.setItems(sortedTaskEntityArchive);
                break;
            case "templateTab":
                // 1. Wrap the ObservableList in a FilteredList (initially display all data).
                //ObservableList<Document> observableListDocuments = FXCollections.observableArrayList(documentService.listDocuments());
                FilteredList<Document> filteredDocument = new FilteredList<Document>(observableListDocument, p -> true);

                // 2. Set the filter Predicate whenever the filter changes.
                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredDocument.setPredicate(document -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (document.getDocumentName().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        }
                        return false; // Does not match.
                    });
                });

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<Document> sortedDocument = new SortedList<>(filteredDocument);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedDocument.comparatorProperty().bind(tableDocumentTemplate.comparatorProperty());

                // 5. Add sorted (and filtered) data to the table.
                tableDocumentTemplate.setItems(sortedDocument);
                break;
            case "calendarTab":
                break;
            case "letterTab":
                // 1. Wrap the ObservableList in a FilteredList (initially display all data).
                //ObservableList<Letter> observableListLetters = FXCollections.observableArrayList(letterService.listLetter());
                FilteredList<Letter> filteredLetter = new FilteredList<Letter>( observableListLetter, p -> true);

                // 2. Set the filter Predicate whenever the filter changes.
                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredLetter.setPredicate(letter -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (letter.getLetterName().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (letter.getLetterNumber().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches last name.
                        }
                        return false; // Does not match.
                    });
                });

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<Letter> sortedLetter = new SortedList<>(filteredLetter);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedLetter.comparatorProperty().bind(tableLetter.comparatorProperty());

                // 5. Add sorted (and filtered) data to the table.
                tableLetter.setItems(sortedLetter);
                break;
            default:
                break;

        }


    }


    public void refreshTabIcon(MouseEvent mouseEvent) throws RemoteException {
        refreshData();
        colorRow();
    }

    public  void refreshData() throws RemoteException {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        observableListDocument = FXCollections.observableArrayList(documentService.listDocuments());
                        observableListDocumentsByDepartment = FXCollections.observableArrayList(documentService.listDocumentsByDepartment(comboBoxDocument_Template.getValue()));

                        observableListMyTaskEntity = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
                        observableListMyLetterTaskEntity = FXCollections.observableArrayList(taskService.listMyLetterTasks(authorizedUser.getEmployeeId()));
                        observableListMyDoneTaskEntity = FXCollections.observableArrayList(taskService.listMyDoneTasks(authorizedUser.getEmployeeId()));
                        observableListFromEmpTaskEntity = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                        observableListArchiveTaskEntity = FXCollections.observableArrayList(taskService.listArchiveTasks(Integer.parseInt(StatusTask.CANCELED)));

                        observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
                        ObservableList<Event> observableListTodayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), new java.sql.Date(System.currentTimeMillis())));
                        observableListAllEvent = FXCollections.observableArrayList(eventService.listAllEvent(authorizedUser.getEmployeeId()));

                        observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
                        observableListLetter = FXCollections.observableArrayList(letterService.listLetter());

//Уведомления о новых письмах
                        int newLetter = 0;
                        int idSummLetter = 0;
                        String messageNameLetter="";
                        for (int i = 0;i<observableListMyLetterTaskEntity.size();i++){
                            if (observableListMyLetterTaskEntity.get(i).getStatusTaskId().equals(StatusTask.NOT_DONE)){
                                newLetter++;
                                idSummLetter=idSummLetter+observableListMyLetterTaskEntity.get(i).getTaskId();
                                messageNameLetter=messageNameLetter+" "+observableListMyLetterTaskEntity.get(i).getTaskName();
                            }
                        }
                        if (newLetter>0) {

                            int finalNewLetter = newLetter;
                            String finalMessageNameLetter = messageNameLetter;
                            int finalIdSummLetter = idSummLetter;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    myLetterButton.setText("   Мои письма("+(finalNewLetter)+")");
                                    notificationEvent.newLetter(finalMessageNameLetter, finalIdSummLetter);

                                }
                            });

                        } else if (newLetter==0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    myLetterButton.setText("   Мои письма");

                                }
                            });

                        }
//Уведомления о новых задачах
                        int newTask = 0;
                        int idSummNewTask = 0;
                        String messageNameTask="";
                        for (int i = 0;i<observableListMyTaskEntity.size();i++){
                            if (observableListMyTaskEntity.get(i).getStatusTaskId().equals(StatusTask.NOT_DONE)){
                                newTask++;
                                messageNameTask=messageNameTask+" "+observableListMyTaskEntity.get(i).getTaskName();
                                idSummNewTask=idSummNewTask+observableListMyTaskEntity.get(i).getTaskId();
                            }
                        }
                        if (newTask>0) {
                            int finalNewTask = newTask;
                            String finalMessageNameTask = messageNameTask;
                            int finalIdSummNewTask = idSummNewTask;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    myTasksButton.setText("   Мои задачи("+(finalNewTask)+")");
                                    notificationEvent.newTask(finalMessageNameTask, finalIdSummNewTask);

                                }
                            });

                        } else if (newTask==0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    myTasksButton.setText("   Мои задачи");

                                }
                            });

                        }
//Уведомления о выполненых задачах
                        int newFromEmpTask = 0;
                        int idSummNewFromEmpTask = 0;
                        String messageNameFromEmpTask="";
                        for (int i = 0;i<observableListFromEmpTaskEntity .size();i++){
                            if (observableListFromEmpTaskEntity .get(i).getStatusTaskId().equals(StatusTask.DONE)){
                                newFromEmpTask++;
                                messageNameFromEmpTask=messageNameFromEmpTask+" "+observableListFromEmpTaskEntity .get(i).getTaskName();
                                idSummNewFromEmpTask=idSummNewFromEmpTask+observableListFromEmpTaskEntity.get(i).getTaskId();
                            }
                        }
                        if (newFromEmpTask>0) {
                            int finalNewFromEmpTask = newFromEmpTask;
                            String finalMessageNameFromEmpTask = messageNameFromEmpTask;
                            int finalIdSummNewFromEmpTask = idSummNewFromEmpTask;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    fromEmpTaskButton.setText("         Назначенные("+(finalNewFromEmpTask)+")");
                                    notificationEvent.newFromEmpTask(finalMessageNameFromEmpTask, finalIdSummNewFromEmpTask);

                                }
                            });

                        } else if (newFromEmpTask==0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    fromEmpTaskButton.setText("         Назначенные");

                                }
                            });

                        }
//Уведомления о событиях
                        int newEvent = 0;
                        long dateNow = System.currentTimeMillis();
                        String messageEvent="";
                        java.sql.Date today = new java.sql.Date(dateNow);



                        try {
                            for (int i=0;i<observableListTodayEvent.size();i++){

                                if (((observableListTodayEvent.get(i).getEventTime().getTime()+observableListTodayEvent.get(i).getEventDate().getTime()+25200000)<dateNow)&&(String.valueOf(observableListTodayEvent.get(i).getEventDate()).equals(String.valueOf(today)))){
                                    newEvent++;
                                    messageEvent=messageEvent+" "+observableListTodayEvent.get(i).getEventName();
                                }

                            }
                        }catch (ArrayIndexOutOfBoundsException e){
                            System.out.println("На сегодня дел нет.");
                        }
                        if (newEvent>0){
                            int finalNewEvent = newEvent;
                            String finalMessageEvent = messageEvent;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    calendarTabButton.setText("   Календарь(" + (finalNewEvent)+")");
                                    notificationEvent.newEvent(finalMessageEvent);

                                }
                            });

                        } else if (newEvent==0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    calendarTabButton.setText("   Календарь");
                                }
                            });

                        }


//Обновление текущей вкладки
                        switch (statusTab){
                            case "myTask":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableTask.setItems(observableListMyTaskEntity);
                                    }
                                });


                                break;
                            case "myLetter":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableTask.setItems(observableListMyLetterTaskEntity);
                                    }
                                });

                                break;
                            case "myDoneTask":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableTask.setItems(observableListMyDoneTaskEntity);
                                    }
                                });

                                break;

                            case "fromEmpTask":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableTask.setItems(observableListFromEmpTaskEntity);
                                    }
                                });

                                break;

                            case "archiveTask":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableTask.setItems(observableListArchiveTaskEntity);
                                    }
                                });

                                break;

                            case "calendarTab":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableEvent.setItems(observableListSelectDayEvent);
                                    }
                                });
                            case "letterTab":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableLetter.setItems(observableListLetter);
                                    }
                                });

                                break;

                            case "templateTab":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tableDocumentTemplate.setItems(observableListDocument);
                                    }
                                });

                                break;

                            default:{
                                break;
                            }
                        }
                        colorRow();
                        return null;
                    }
                };
            }
        };
                service.start();


    }
    public void serviceStart(){
        ScheduledService<Void> service = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("Hello");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    refreshData();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        return null;
                    }
                };
            }
        };
        service.setPeriod(Duration.seconds(30));
        service.start();


    }


}
