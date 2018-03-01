package controller;


import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
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

import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import jfxtras.scene.control.CalendarPicker;
import entity.*;
import dialog.ADInfo;



import java.io.IOException;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainController {

    public Employee authorizedUser;


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

    private TaskEntity taskEntity = new TaskEntity();
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
    /*initialize table Document Template tab*/
//заполняем таблицу данными
        documentNameTemplate = new TableColumn<Document, String>("Название документа");
        documentNameTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
        documentFilePathTemplate = new TableColumn<Document, String>("Путь файла");
        documentFilePathTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentFilePath"));

        tableDocumentTemplate.getColumns().setAll(documentNameTemplate);

        ObservableList<Document> observableListDocument = FXCollections.observableArrayList(ServiceRegistry.documentService.listDocuments());
        tableDocumentTemplate.setItems(observableListDocument);
//задаем размер колонок в таблицу
        documentNameTemplate.prefWidthProperty().bind(tableTask.widthProperty().multiply(1));


    /*initialize combobox Document template tab*/

        departmentService.listDepartments();
        ObservableList<String> observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
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

        ObservableList<TaskEntity> observableListMyTaskEntity = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
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

        ObservableList<Event> observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
        tableEvent.setItems(observableListSelectDayEvent);


        timePickerEvent.setIs24HourView(true);
        calendarPicker.calendarProperty().addListener( (observable) -> {
            if (calendarPicker.getCalendar()!=null) {
                calendar = calendarPicker.getCalendar();
                datesql = new java.sql.Date(calendar.getTimeInMillis());
                try {
                    ObservableList<Event> observableListSelectedDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
                    tableEvent.setItems(observableListSelectedDayEvent);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
            }
            else {
                datesql = new java.sql.Date(calendar.getInstance().getTimeInMillis());
                try {
                    ObservableList<Event> observableSelectedDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
                    tableEvent.setItems(observableSelectedDayEvent);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                labelSelectedDate.setText("сегодня");
                timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.30));
                nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.70));
                dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
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


        tableLetter.getColumns().setAll(nameLetter, passwordLetter, numberLetter);
        ObservableList<Letter> observableListLetter = FXCollections.observableArrayList(letterService.listLetter());
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
                eventService.removeEvent(event);
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

        ObservableList<Event> observableListSelectedDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
        tableEvent.setItems(observableListSelectedDayEvent);
    }

    private void clearEventText() {
        textAreaEvent.setText("");
        timePickerEvent.setValue(null);
    }

    public void repeateEventToggleBtn(ActionEvent actionEvent) {

    }

    public void showAllEventButton(ActionEvent actionEvent) throws RemoteException {
        ObservableList<Event> observableListAllEvent = FXCollections.observableArrayList(eventService.listAllEvent(authorizedUser.getEmployeeId()));
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
        ObservableList<Document> observableListDocumentsByDepartment = FXCollections.observableArrayList(documentService.listDocumentsByDepartment(comboBoxDocument_Template.getValue()));
        tableDocumentTemplate.setItems(observableListDocumentsByDepartment);

    }

    public void openDocumentButton(ActionEvent actionEvent) throws RemoteException {

        try {
            documentService.openDocument(document.getDocumentId());
        }catch (NullPointerException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не выбран!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не найден!");
        }

    }

    public void printDocumentButton(ActionEvent actionEvent) throws RemoteException {
       try {
           documentService.printDocument(document.getDocumentId());
       }catch (NullPointerException e){
           ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не выбран!");
       } catch (SQLException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }


        // }

    }


/*Tasks  tab*/
    public void refreshTaskTab() throws RemoteException {
        ObservableList<TaskEntity> observableListMyTaskEntities = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
        tableTask.setItems(observableListMyTaskEntities);

    }


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
        if (taskEntity !=null) {
            taskService.performedTask(taskEntity.getTaskId());
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
        }
        else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }




    public void myTasksButton(ActionEvent actionEvent) throws RemoteException {


        anchorTask.toFront();
        myTaskBtnBar.toFront();


        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);
        Task task = new Task<Void>() {
            @Override public Void call() throws RemoteException {
                tableTask.setDisable(true);
                progressBar.setVisible(true);

                final int max = 100;
               /* for (int i=1; i<=max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);
                }*/


                statusTab="myTask";
                taskEntity =null;




                nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


                updateProgress(60, max);

                ObservableList<TaskEntity> observableListMyTaskEntities = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
                tableTask.setItems(observableListMyTaskEntities);


                // задаем размер колонок в таблице
                colorRow();

                progressBar.setVisible(false);
                tableTask.setDisable(false);
                updateProgress(max, max);


                return null;
            }
        };

        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());


    }

    public void myDoneTasksButton(ActionEvent actionEvent) throws RemoteException {

        anchorTask.toFront();
        myTaskDoneBtnBar.toFront();


        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);
        Task task = new Task<Void>() {
            @Override public Void call() throws RemoteException {

                tableTask.setDisable(true);
                progressBar.setVisible(true);
                final int max = 100;
               /* for (int i=1; i<=max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);
                }*/
                statusTab="myDoneTask";
                taskEntity =null;


                nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


                updateProgress(60, max);
                ObservableList<TaskEntity> observableListMyDoneTaskEntities = FXCollections.observableArrayList(taskService.listMyDoneTasks(authorizedUser.getEmployeeId()));
                tableTask.setItems(observableListMyDoneTaskEntities);



                // задаем размер колонок в таблице
                colorRow();

                tableTask.setDisable(false);
                progressBar.setVisible(false);
                updateProgress(max, max);
                return null;
            }
        };
        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());

    }
    public void fromEmpTasjButton(ActionEvent actionEvent) throws RemoteException {


        anchorTask.toFront();
        fromEmpTaskBtnBar.toFront();


        tableTask.getColumns().setAll(nameTask, employeeTask, termTask, timeTask, statusTask);
        Task task = new Task<Void>() {
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


                ObservableList<TaskEntity> observableListFromEmpTaskEntities = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                tableTask.setItems(observableListFromEmpTaskEntities);
                colorRow();
                // задаем размер колонок в таблице


                tableTask.setDisable(false);
                progressBar.setVisible(false);
                return null;
            }
        };
        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());



    }

    public void archiveTasks(ActionEvent actionEvent) throws RemoteException {

        anchorTask.toFront();
        archiveTaskBtnBar.toFront();

        tableTask.getColumns().setAll(nameTask,sender, employeeTask, termTask, timeTask,statusTask);

        Task task = new Task<Void>() {
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


                ObservableList<TaskEntity> observableListrchiveTaskEntities = FXCollections.observableArrayList(taskService.listArchiveTasks(Integer.parseInt(StatusTask.CANCELED)));
                tableTask.setItems(observableListrchiveTaskEntities);
                colorRow();
                // задаем размер колонок в таблице

                tableTask.setDisable(false);
                progressBar.setVisible(false);
                return null;
            }
        };
        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());



    }

    public void clickTableTask(MouseEvent mouseEvent) {
        TaskEntity taskEntity = tableTask.getSelectionModel().getSelectedItems().get(0);
        if (taskEntity !=null){
            this.taskEntity = taskEntity;
        }

    }

    public void acceptTask(ActionEvent actionEvent) throws RemoteException {
        if (taskEntity !=null) {
            taskService.performedTask(taskEntity.getTaskId());
        }
    }


    public void deleteTaskButton(ActionEvent actionEvent) throws RemoteException {
        if (taskEntity !=null){

            taskService.removeTask(this.taskEntity);
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
    }
    public void templateTabButton(ActionEvent actionEvent) throws RemoteException {
        anchorTemplate.toFront();

        Task task = new Task<Void>() {
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
                ObservableList<Document> observableListDocuments = FXCollections.observableArrayList(documentService.listDocuments());
                tableDocumentTemplate.setItems(observableListDocuments);
                ObservableList<String> observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
                comboBoxDocument_Template.setItems(observableListDepartmentName);

                tableDocumentTemplate.setDisable(false);
                progressBar.setVisible(false);



                return null;
            }
        };
        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());
    }

    public void calendarTabButton(ActionEvent actionEvent) {


        anchorCalendar.toFront();

        Task task = new Task<Void>() {
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
        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());
    }

    public void letterTabButton(ActionEvent actionEvent) throws RemoteException {
        statusTab="letterTab";
        anchorLetter.toFront();

        Task task = new Task<Void>() {
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
                ObservableList<Letter> observableListLetter = FXCollections.observableArrayList(letterService.listLetter());
                tableLetter.setItems(observableListLetter);


                tableLetter.setDisable(false);
                progressBar.setVisible(false);
                return null;
            }
        };
        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());

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




    public void openLetter(ActionEvent actionEvent) throws RemoteException {
        try {
            letterService.openLetter(letter.getLetterId());
        } catch (IllegalArgumentException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Письмо было удалено с сервера!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            letterService.removeLetter(letter.getLetterId(), letter.getLetterFilePath());
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
        statusTask.setCellFactory(column -> {
            return new TableCell<TaskEntity, String>() {
                @Override
                protected void updateItem( String item, boolean empty) {

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
    }

    public void keyPressSort(KeyEvent keyEvent) throws RemoteException {
           /*Сортировка*/

        switch (statusTab){
            case "myTask":
// 1. Wrap the ObservableList in a FilteredList (initially display all data).
                ObservableList<TaskEntity> observableListMyTaskEntities = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
                FilteredList<TaskEntity> filteredMyTaskEntity = new FilteredList<TaskEntity>(observableListMyTaskEntities, p -> true);

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
                ObservableList<TaskEntity> observableListMyDoneTaskEntities = FXCollections.observableArrayList(taskService.listMyDoneTasks(authorizedUser.getEmployeeId()));
                FilteredList<TaskEntity> filteredMyDoneTaskEntity = new FilteredList<TaskEntity>(observableListMyDoneTaskEntities, p -> true);

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
                ObservableList<TaskEntity> observableListFromEmpTaskEntities = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                FilteredList<TaskEntity> filteredFromEmpTaskEntity = new FilteredList<TaskEntity>(observableListFromEmpTaskEntities, p -> true);

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
                ObservableList<TaskEntity> observableListArchiveTaskEntities = FXCollections.observableArrayList(taskService.listArchiveTasks(Integer.parseInt(StatusTask.CANCELED)));
                FilteredList<TaskEntity> filteredArchiveTaskEntity = new FilteredList<TaskEntity>(observableListArchiveTaskEntities, p -> true);

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
                ObservableList<Document> observableListDocuments = FXCollections.observableArrayList(documentService.listDocuments());
                FilteredList<Document> filteredDocument = new FilteredList<Document>(observableListDocuments, p -> true);

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
                ObservableList<Letter> observableListLetters = FXCollections.observableArrayList(letterService.listLetter());
                FilteredList<Letter> filteredLetter = new FilteredList<Letter>( observableListLetters, p -> true);

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


    public void refreshTabIcon(MouseEvent mouseEvent) {
        refreshTab();

    }

    public void refreshTab(){

        switch (statusTab){

            case "MyTask":
                listMyTasks
                break;

            case "MyDoneTask":
                listMyDoneTasks
                break;

            case "FromEmplTask":
                listFromEmpTasks
                break;

            case "ArchiveTask":
                listArchiveTasks
                break;

            case "calendarTab":
                listLetter
                break;

            default:{
                break;
            }
        }

    }
}
