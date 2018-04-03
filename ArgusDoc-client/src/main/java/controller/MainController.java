package controller;


import argusDocSettings.FileManager;
import argusDocSettings.ServerFilePath;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.Event;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
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


import java.awt.*;
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


    //создаем трей иконку
    public static java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
    public static java.awt.TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("ArgusDoc-client/src/main/resources/images/trayIcon.jpg"));

    Stage stage;
    NotificationEvent notificationEvent=new NotificationEvent();
    public Employee authorizedUser;


    private ObservableList<Document> observableListDocument ;
    private ObservableList<Document> observableListDocumentsByDepartment ;
    private ObservableList<Event> observableListTodayEvent;
    private ObservableList<TaskEntity> observableListMyTaskEntity ;
    private ObservableList<TaskEntity> observableListMyLetterTaskEntity ;
    private ObservableList<TaskEntity> observableListMyDoneTaskEntity;
    private ObservableList<TaskEntity> observableListFromEmpTaskEntity;
    private ObservableList<TaskEntity> observableListArchiveTaskEntity;

    private ObservableList<Event> observableListSelectDayEvent;
    private ObservableList<Event> observableListAllEvent;

    private ObservableList<String> observableListDepartmentName;
    private ObservableList<Letter> observableListLetter;



    String statusTab="myLetter";


    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    private final java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

    java.sql.Date datesql = new java.sql.Date(System.currentTimeMillis());


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
    private FontAwesomeIconView refreshTabIcon;
    @FXML
    private Button doneEvent;
    @FXML
    private JFXHamburger hamburger ;
    @FXML
    private JFXDrawer drawer ;
    @FXML
    private JFXButton myTasksButton = new JFXButton();
    @FXML
    private JFXButton templateTabButton = new JFXButton();

    @FXML
    private JFXButton myDoneTasksButton = new JFXButton();
    @FXML
    private JFXButton archiveTasks = new JFXButton();

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
    private ComboBox<String> comboBoxDocument_Template;

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
    private TableColumn<Event, String> statusEvent;
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

    ArrayList<String> arrayListEventperiodicity = new ArrayList<String>();
    ObservableList<String> observableListEventperiodicity;
//Letter Tab
    @FXML
    private JFXButton openAddLetterWindow;
    @FXML
    private JFXButton removeLetter;
    @FXML
    private TextField txtFilter;
    @FXML
    private TableView<Letter> tableLetter;
    private TableColumn<Letter, String> idLetter;
    private TableColumn<Letter, String> nameLetter;
    private TableColumn<Letter, String> dateLetter;
    private TableColumn<Letter, String> passwordLetter;
    private TableColumn<Letter, String> numberLetter;
    private TableColumn<Letter, String> filePathLetter;
    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }


    HamburgerBackArrowBasicTransition transition;
    public void initialize(Employee authorizedUser,Stage stage) throws RemoteException {
        this.stage=stage;

        Path documentsPath = Paths.get(ServerFilePath.DOCUMENTS_FILE_PATH);
        Path tasksPath = Paths.get(ServerFilePath.TASKS_FILE_PATH);
        Path lettersPath = Paths.get(ServerFilePath.LETTERS_FILE_PATH);

        if (Files.exists(documentsPath)&&Files.exists(tasksPath)&&Files.exists(lettersPath)) {
            System.out.println("Соединение с хранилищем установлено!");
        } else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Хранилище недоступно! Обратитесь к системному администратору!");
           System.exit(0);

        }

        this.authorizedUser=authorizedUser;

        serviceStart();

//Добавляем приложение В трей
        try {
            //Инициализируем toolkit
            java.awt.Toolkit.getDefaultToolkit();

            //Проверка на поддержку в трее
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            //двойное нажатие мыши - показываем stage
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            //выбор меню по умолчанию - показываем stage
            java.awt.MenuItem openItem = new java.awt.MenuItem("Восстановить");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            // Устанавливаем шрифт для пункта меню по умолчанию
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            //меню выход из приложения, удаляем из трея и закрываем программу
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Выход");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            // устанавливаем  popup меню для приложения
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            // добавляем иконку в трей
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }

    /*initialize table Document Template tab*/
//заполняем таблицу данными
        documentNameTemplate = new TableColumn<Document, String>("Название документа");
        documentNameTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
        documentFilePathTemplate = new TableColumn<Document, String>("Путь файла");
        documentFilePathTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentFilePath"));

        tableDocumentTemplate.getColumns().setAll(documentNameTemplate);
        tableDocumentTemplate.setItems(observableListDocument);
//задаем размер колонок в таблицу
        documentNameTemplate.prefWidthProperty().bind(tableDocumentTemplate.widthProperty().multiply(1));


    /*initialize combobox Document template tab*/

        departmentService.listDepartments();


        observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
        observableListLetter = FXCollections.observableArrayList(letterService.listLetter());
        ////
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
        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.50));
        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.20));
        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));
        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


        tableTask.getColumns().setAll(nameTask, sender, termTask, timeTask, statusTask);

        tableTask.setItems(observableListMyLetterTaskEntity);

        labelUserAuth.setText(authorizedUser.getEmployeeName());


        colorRow();//цвет ячеек

//Calendar Tab

        arrayListEventperiodicity.add("Ежедневно");
        arrayListEventperiodicity.add("Еженедельно");
        arrayListEventperiodicity.add("Ежемесячно");

        observableListEventperiodicity = FXCollections.observableArrayList(arrayListEventperiodicity);

        comboBoxRepeate.setValue("");

        nameEvent = new TableColumn<Event, String>("Напоминание");
        nameEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventName"));
        dateEvent = new TableColumn<Event, String>("Дата");
        dateEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventDate"));
        timeEvent = new TableColumn<Event, String>("Время");
        timeEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventTime"));
        statusEvent = new TableColumn<Event, String>("Статус события");
        statusEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("eventStatus"));

        timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.30));
        nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.70));
        dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
        statusEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));

        tableEvent.getColumns().setAll(timeEvent, nameEvent ,dateEvent,statusEvent);

        tableEvent.setItems(observableListSelectDayEvent);

        timePickerEvent.setIs24HourView(true);


        calendarPicker.calendarProperty().addListener( (observable) -> {
            tableEvent.getColumns().setAll( timeEvent, nameEvent);
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
               // dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
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
               // dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
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
        settingTabButton.setVisible(true);
        openAddLetterWindow.setVisible(true);
        removeLetter.setVisible(true);

    }

    /*initialize Letter  tab*/

        idLetter = new TableColumn<Letter, String>("id");
        idLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterId"));
        nameLetter = new TableColumn<Letter, String>("Название");
        nameLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterName"));
        dateLetter = new TableColumn<Letter,String>("Дата");
        dateLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterDate"));
        passwordLetter = new TableColumn<Letter, String>("Пароль");
        passwordLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterTechnicalPassword"));
        numberLetter = new TableColumn<Letter, String>("Номер");
        numberLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterNumber"));
        filePathLetter = new TableColumn<Letter, String>("файл");
        filePathLetter.setCellValueFactory(new PropertyValueFactory<Letter, String>("letterFilePath"));

        tableLetter.getColumns().setAll(numberLetter, dateLetter,nameLetter, passwordLetter);

        numberLetter.prefWidthProperty().bind(tableLetter.widthProperty().multiply(0.15));
        dateLetter.prefWidthProperty().bind(tableLetter.widthProperty().multiply(0.15));
        nameLetter.prefWidthProperty().bind(tableLetter.widthProperty().multiply(0.50));
        passwordLetter.prefWidthProperty().bind(tableLetter.widthProperty().multiply(0.20));

        tableLetter.setItems(observableListLetter);

        anchorTask.toFront();
        myLetterButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
        myTaskBtnBar.toFront();

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/viewFXML/DrawerMainMenu.fxml"));
            drawer.setSidePane(box);
        } catch (IOException ex) {
           ex.printStackTrace();
        }

//Hamburger
        transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        drawer.toBack();


    }
/*
Calendar tab
* */
    public void clickTableEvent(MouseEvent mouseEvent) {
        Event event = tableEvent.getSelectionModel().getSelectedItems().get(0);
        if (event!=null){
            this.event=event;
            textAreaEvent.setText(event.getEventName());
            timePickerEvent.setValue(event.getEventTime().toLocalTime());
        }
    }

    public void removeEvent(ActionEvent actionEvent) throws RemoteException {
        if (event!=null){
            try {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/images/1.jpg"));
                alert.setTitle("Удаление");
                alert.setHeaderText("Вы действительно хотите удалить напоминание?");

                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    eventService.removeEvent(event);


                } else if (option.get() == ButtonType.CANCEL) {

                } else {

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
        tableEventRefresh(observableListSelectDayEvent);
        colorRowEvent();
    }

    public void addEvent(ActionEvent actionEvent) throws RemoteException {
        if (textAreaEvent.getText().isEmpty()||timePickerEvent.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите текст напоминания!");
        } else {
            Event event = new Event();
            event.setEventName(textAreaEvent.getText());
            event.setEventTime(Time.valueOf(timePickerEvent.getValue()));
            event.setEventDate(datesql);
            event.setEmployeeId(authorizedUser.getEmployeeId());

                switch (comboBoxRepeate.getValue()) {

                    case "Ежедневно": {
                        event.setEventPeriodicity(EventPeriodicity.DAILY);
                         break;
                    }
                    case "Еженедельно": {
                        event.setEventPeriodicity(EventPeriodicity.WEEKLY);
                        break;
                    }
                    case "Ежемесячно": {
                        event.setEventPeriodicity(EventPeriodicity.MONTHLY);
                        break;
                    }
                    default: {
                        event.setEventPeriodicity(EventPeriodicity.SINGLE_TIME);
                        break;
                    }

                }

            try {
                eventService.addEvent(event);
                System.out.println(event.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            clearEventText();
        }
        observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
        tableEventRefresh(observableListSelectDayEvent);
        colorRowEvent();
    }
    public void updateEvent(ActionEvent actionEvent) throws RemoteException {
        if (textAreaEvent.getText().isEmpty()||timePickerEvent.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите текст напоминания!");
        } else {
            Event event = this.event;
            event.setEventName(textAreaEvent.getText());
            event.setEventTime(Time.valueOf(timePickerEvent.getValue()));
            event.setEventDate(datesql);
            event.setEmployeeId(authorizedUser.getEmployeeId());

            switch (comboBoxRepeate.getValue()) {

                case "Ежедневно": {
                    event.setEventPeriodicity(EventPeriodicity.DAILY);
                    break;
                }
                case "Еженедельно": {
                    event.setEventPeriodicity(EventPeriodicity.WEEKLY);
                    break;
                }
                case "Ежемесячно": {
                    event.setEventPeriodicity(EventPeriodicity.MONTHLY);
                    break;
                }
                default: {
                    event.setEventPeriodicity(EventPeriodicity.SINGLE_TIME);
                    break;
                }

            }

            try {
                eventService.updateEvent(event);
                System.out.println(event.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            clearEventText();
        }
        observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
        tableEventRefresh(observableListSelectDayEvent);
        colorRowEvent();

    }
    public void doneEvent(ActionEvent actionEvent) throws RemoteException {
        try {
            eventService.doneEvent(event.getEventId());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
        tableEventRefresh(observableListSelectDayEvent);
        colorRowEvent();
    }

    private void clearEventText() {
        textAreaEvent.setText("");
        timePickerEvent.setValue(null);
    }

    public void repeateEventToggleBtn(ActionEvent actionEvent) {

        if (repeateEventToggleBtn.isSelected()){
            comboBoxRepeate.setVisible(true);
            repeateEventToggleBtn.setText("Повторять");
            comboBoxRepeate.setItems(observableListEventperiodicity);
            comboBoxRepeate.setValue("Ежедневно");
        } else {
            comboBoxRepeate.setValue("");
            comboBoxRepeate.setVisible(false);
            repeateEventToggleBtn.setText("Не повторять");
            comboBoxRepeate.setValue("");
        }


    }

    public void showAllEventButton(ActionEvent actionEvent) throws RemoteException {
        tableEvent.setItems(observableListAllEvent);
        tableEvent.getColumns().setAll(dateEvent, timeEvent, nameEvent,statusEvent);
        nameEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.45));
        dateEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.30));
        timeEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0.25));
        statusEvent.prefWidthProperty().bind(tableEvent.widthProperty().multiply(0));
        colorRowEvent();
    }

/*Document template tab*/
    public void clickTableDocumentTemplate(MouseEvent mouseEvent) {

    Document clickDocument = tableDocumentTemplate.getSelectionModel().getSelectedItems().get(0);

        document = clickDocument;

    }
    public void comboBoxDocument_Template(ActionEvent actionEvent) throws RemoteException {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                observableListDocumentsByDepartment = FXCollections.observableArrayList(documentService.listDocumentsByDepartment(comboBoxDocument_Template.getValue()));
                tableDocumentTemplate.setItems(observableListDocumentsByDepartment);
                return null;
            }
        };
        Platform.runLater(() ->{
            new Thread(task).start();
        });
    }

    public void openDocumentButton(ActionEvent actionEvent) throws RemoteException {
        FileManager.openFile(document.getDocumentFilePath());
    }

    public void printDocumentButton(ActionEvent actionEvent) throws RemoteException {
        FileManager.printFile(document.getDocumentFilePath());
    }

    public void showAllDocumentButton(ActionEvent actionEvent) {
        tableDocumentTemplate.setItems(observableListDocument);
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
            stage.showAndWait();
            if (addTaskController.okButton)
            {
                observableListFromEmpTaskEntity = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                tableTaskRefresh(observableListFromEmpTaskEntity);
                colorRow();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openEditTaskButton(ActionEvent actionEvent) {
        try {

            if (taskEntity.getTaskIsLetter()==0) {

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

                        editController.initialize(taskEntity, authorizedUser);

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
                        stage.showAndWait();
                        if (editController.okButton)
                        {
                            observableListFromEmpTaskEntity = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                            tableTaskRefresh(observableListFromEmpTaskEntity);
                            colorRow();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
                }
            } else {
                if (taskEntity.getLetterId()!=0) {
                    FXMLLoader fxmlLoader = new FXMLLoader();

                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/View-edit_letter_window.fxml"));
                    try {

                        fxmlLoader.load();
                        Stage stage = new Stage();
                        Parent root = fxmlLoader.getRoot();
                        stage.setScene(new Scene(root));
                        EditViewLetterController editViewLetterController = fxmlLoader.getController();
                        editViewLetterController.initialize(authorizedUser, taskEntity.getLetterId());

                        //stage.setTitle("Новая задача");
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
                        stage.showAndWait();
                        if (editViewLetterController.okButton)
                        {
                            observableListFromEmpTaskEntity = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                            tableTaskRefresh(observableListFromEmpTaskEntity);
                            colorRow();
                        }

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
                        doneController.initialize(taskEntity);

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
                        stage.showAndWait();
                        if (doneController.okButton)
                        {
                            if (statusTab.equals("myDoneTask")){
                                observableListMyDoneTaskEntity = FXCollections.observableArrayList(taskService.listMyDoneTasks(authorizedUser.getEmployeeId()));
                                tableTaskRefresh(observableListMyDoneTaskEntity);

                            } else if (statusTab.equals("myTask")){
                                observableListMyTaskEntity = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
                                tableTaskRefresh( observableListMyTaskEntity);
                            }

                            colorRow();
                        }

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
                        stage.showAndWait();
                        if (doneLetterController.okButton)
                        {
                            observableListMyLetterTaskEntity = FXCollections.observableArrayList(taskService.listMyLetterTasks(authorizedUser.getEmployeeId()));
                            tableTaskRefresh(observableListMyLetterTaskEntity);
                            colorRow();
                        }

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
        clearButtonMenuSelected();
        myLetterButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

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
        clearButtonMenuSelected();
        myTasksButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

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

                        Platform.runLater(() ->{
                            tableTask.setItems(list);

                        });
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

    private void clearButtonMenuSelected() {

        myTasksButton.setStyle("");
        myLetterButton.setStyle("");
        myDoneTasksButton.setStyle("");
        fromEmpTaskButton.setStyle("");
        archiveTasks.setStyle("");
        templateTabButton.setStyle("");
        calendarTabButton.setStyle("");
        letterTabButton.setStyle("");
        settingTabButton.setStyle("");

    }

    public void myDoneTasksButton(ActionEvent actionEvent) throws RemoteException {

        clearButtonMenuSelected();
        myDoneTasksButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
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
                        // задаем размер колонок в таблице
                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));

                        Platform.runLater(() ->{
                            tableTask.setItems(list);

                        });

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
        clearButtonMenuSelected();
        fromEmpTaskButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

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
                        // задаем размер колонок в таблице
                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.40));
                        employeeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.30));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));

                        Platform.runLater(() ->{
                            tableTask.setItems(list);

                        });

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

    public void archiveTasks(ActionEvent actionEvent) throws RemoteException {
        clearButtonMenuSelected();
        archiveTasks.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
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
                        // задаем размер колонок в таблице
                        nameTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.25));
                        sender.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.25));
                        employeeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.20));
                        termTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        timeTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0.15));
                        statusTask.prefWidthProperty().bind(tableTask.widthProperty().multiply(0));


                        Platform.runLater(() ->{
                            tableTask.setItems(list);

                        });
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
            observableListMyTaskEntity = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
            tableTaskRefresh( observableListMyTaskEntity);
            colorRow();
        }
    }


    public void removeTaskButton(ActionEvent actionEvent) throws RemoteException {
        if (taskEntity !=null){

            try {
                ////////////
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/images/1.jpg"));
                alert.setTitle("Удаление");
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

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
        {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Задача не выбрана!");
        }
        if (statusTab.equals("archiveTask")){
            observableListArchiveTaskEntity = FXCollections.observableArrayList(taskService.listArchiveTasks(Integer.parseInt(StatusTask.CANCELED)));
            tableTaskRefresh(observableListArchiveTaskEntity);

        } else if (statusTab.equals("fromEmpTask")){
            observableListFromEmpTaskEntity = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
            tableTaskRefresh(observableListFromEmpTaskEntity);
        }

        colorRow();

    }
    public void templateTabButton(ActionEvent actionEvent) throws RemoteException {
        clearButtonMenuSelected();
        templateTabButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
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
        clearButtonMenuSelected();
        calendarTabButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

        anchorCalendar.toFront();
        ObservableList<Event> list = observableListSelectDayEvent;
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
                        Platform.runLater(()->{
                            tableEvent.setItems(list);
                        });


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
        clearButtonMenuSelected();
        letterTabButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
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
    }

//Letter Tab

    public void openViewEditLetterWindow(ActionEvent actionEvent) {

        if (letter.getLetterName()!=null) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/viewFXML/View-edit_letter_window.fxml"));
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
                stage.showAndWait();
                if (editViewLetterController.okButton)
                {
                    observableListLetter = FXCollections.observableArrayList(letterService.listLetter());
                    tableLetterRefresh(observableListLetter);
                    colorRow();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Письмо не выбрано!");
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
                stage.showAndWait();
                if (addLetterController.okButton)
                {
                    observableListLetter = FXCollections.observableArrayList(letterService.listLetter());
                    tableLetterRefresh(observableListLetter);
                    colorRow();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Письмо не выбрано!");
        }
    }

    public void removeLetter(ActionEvent actionEvent) throws RemoteException {
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/1.jpg"));
            alert.setTitle("Удаление");
            alert.setHeaderText("Вы действительно хотите удалить письмо?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                letterService.removeLetter(letter.getLetterId(), letter.getLetterFilePath());

            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }

        } catch (IOException e) {
            System.out.println("Файл уже удален!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        observableListLetter = FXCollections.observableArrayList(letterService.listLetter());
        tableLetterRefresh(observableListLetter);
        colorRow();
    }

    public void clickLetterTable(MouseEvent mouseEvent) {
        Letter letter = tableLetter.getSelectionModel().getSelectedItems().get(0);
        if (letter!=null){
            this.letter = letter;
        }
    }

    private void colorRow() { //красим строки tableTask в зависимости от значения столбца statusTask
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

    private void colorRowEvent() { //Красим строки tableEvent
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        statusEvent.setCellFactory(column -> {
                            return new TableCell<Event, String>() {
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
                                            case "0":
                                                currentRow.setStyle("-fx-background-color:#FBCEB1; -fx-font-weight: bold; -fx-font-size: 16");
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

    public void keyPressSort(KeyEvent keyEvent) throws RemoteException { //Реализуем сортировку по таблицам в соответсвующих вкладках при вводе в txtFilter
           /*Сортировка*/

        switch (statusTab){
            case "myLetter":
                FilteredList<TaskEntity> filteredMyLetterTaskEntity = new FilteredList<TaskEntity>(observableListMyLetterTaskEntity, p -> true);

                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredMyLetterTaskEntity.setPredicate(task -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        } else if(2>0){

                        }
                        return false;
                    });
                });

                SortedList<TaskEntity> sortedMyLetterTaskEntity = new SortedList<>(filteredMyLetterTaskEntity);

                sortedMyLetterTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();

                tableTask.setItems(sortedMyLetterTaskEntity);

                break;
            case "myTask":

                FilteredList<TaskEntity> filteredMyTaskEntity = new FilteredList<TaskEntity>(observableListMyTaskEntity, p -> true);

                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredMyTaskEntity.setPredicate(task -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<TaskEntity> sortedMyTaskEntity = new SortedList<>(filteredMyTaskEntity);

                sortedMyTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();

                tableTask.setItems(sortedMyTaskEntity);

                break;
            case "myDoneTask":

                FilteredList<TaskEntity> filteredMyDoneTaskEntity = new FilteredList<TaskEntity>(observableListMyDoneTaskEntity, p -> true);

                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredMyDoneTaskEntity.setPredicate(task -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<TaskEntity> sortedMyDoneTaskEntity = new SortedList<>(filteredMyDoneTaskEntity);

                sortedMyDoneTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();

                tableTask.setItems(sortedMyDoneTaskEntity);
                break;
            case "fromEmpTask":

                FilteredList<TaskEntity> filteredFromEmpTaskEntity = new FilteredList<TaskEntity>(observableListFromEmpTaskEntity, p -> true);


                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredFromEmpTaskEntity.setPredicate(task -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (task.getEmployeeName().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<TaskEntity> sortedFromEmpTaskEntity = new SortedList<>(filteredFromEmpTaskEntity);

                sortedFromEmpTaskEntity.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();

                tableTask.setItems(sortedFromEmpTaskEntity);
                break;
            case "archiveTask":

                FilteredList<TaskEntity> filteredArchiveTaskEntity = new FilteredList<TaskEntity>(observableListArchiveTaskEntity, p -> true);

                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredArchiveTaskEntity.setPredicate(task -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (task.getTaskName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (task.getEmployeeName().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        } else if (task.getTaskFromEmployee().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<TaskEntity> sortedTaskEntityArchive = new SortedList<>(filteredArchiveTaskEntity);

                sortedTaskEntityArchive.comparatorProperty().bind(tableTask.comparatorProperty());
                colorRow();

                tableTask.setItems(sortedTaskEntityArchive);
                break;
            case "templateTab":

                FilteredList<Document> filteredDocument = new FilteredList<Document>(observableListDocument, p -> true);

                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredDocument.setPredicate(document -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (document.getDocumentName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<Document> sortedDocument = new SortedList<>(filteredDocument);

                sortedDocument.comparatorProperty().bind(tableDocumentTemplate.comparatorProperty());

                tableDocumentTemplate.setItems(sortedDocument);
                break;
            case "calendarTab":
                break;
            case "letterTab":

                FilteredList<Letter> filteredLetter = new FilteredList<Letter>( observableListLetter, p -> true);

                txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredLetter.setPredicate(letter -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }


                        String lowerCaseFilter = newValue.toLowerCase();

                        if (letter.getLetterName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (letter.getLetterNumber().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }else if (letter.getLetterTechnicalPassword().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }else if ((String.valueOf(letter.getLetterDate())).toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<Letter> sortedLetter = new SortedList<>(filteredLetter);

                sortedLetter.comparatorProperty().bind(tableLetter.comparatorProperty());

                tableLetter.setItems(sortedLetter);
                break;
            default:
                break;
        }
    }

    public void refreshTabIcon(MouseEvent mouseEvent) throws RemoteException {//Нажатие на иконку обновления
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                RotateTransition rt = new RotateTransition(Duration.seconds(1),refreshTabIcon);
                rt.setByAngle(360);
                rt.setCycleCount(1);
                rt.setAutoReverse(false);
                rt.play();
                refreshData();
                colorRow();
                return null;
            }
        };
        new Thread(task).start();
    }

    public  void refreshData() throws RemoteException { //Обновление всех ObservableList и таблиц в зависимости от открытой вкладки, формирование уведомлений
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        observableListDocument = FXCollections.observableArrayList(documentService.listDocuments());

                        observableListMyLetterTaskEntity = FXCollections.observableArrayList(taskService.listMyLetterTasks(authorizedUser.getEmployeeId()));
                        observableListMyTaskEntity = FXCollections.observableArrayList(taskService.listMyTasks(authorizedUser.getEmployeeId()));
                        observableListMyDoneTaskEntity = FXCollections.observableArrayList(taskService.listMyDoneTasks(authorizedUser.getEmployeeId()));
                        observableListFromEmpTaskEntity = FXCollections.observableArrayList(taskService.listFromEmpTasks((authorizedUser.getEmployeeName())));
                        observableListArchiveTaskEntity = FXCollections.observableArrayList(taskService.listArchiveTasks(Integer.parseInt(StatusTask.CANCELED)));

                        observableListSelectDayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), datesql));
                        observableListTodayEvent = FXCollections.observableArrayList(eventService.listSelectedDayEvent(authorizedUser.getEmployeeId(), new java.sql.Date(System.currentTimeMillis())));
                        observableListAllEvent = FXCollections.observableArrayList(eventService.listAllEvent(authorizedUser.getEmployeeId()));

                        observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
                        observableListLetter = FXCollections.observableArrayList(letterService.listLetter());

//Уведомления о новых/просроченных письмах
                        long dateNow = System.currentTimeMillis();
                        int newLetter = 0;
                        int idSummLetter = 0;
                        String messageNameLetter="";

                        String messageOverdueLetter="";
                        int overdueLetter = 0;
                        for (int i = 0;i<observableListMyLetterTaskEntity.size();i++){
                            //новые
                            if (observableListMyLetterTaskEntity.get(i).getStatusTaskId().equals(StatusTask.NOT_DONE)){
                                newLetter++;
                                idSummLetter=idSummLetter+observableListMyLetterTaskEntity.get(i).getTaskId();
                                messageNameLetter=messageNameLetter+" "+observableListMyLetterTaskEntity.get(i).getTaskName();
                            }
                            //просроченные
                            if (((observableListMyLetterTaskEntity.get(i).getTaskTerm().getTime())<dateNow)){
                                messageOverdueLetter=messageOverdueLetter+" "+observableListMyLetterTaskEntity.get(i).getTaskName();
                                taskService.overdueTask(observableListMyLetterTaskEntity.get(i).getTaskId());
                                overdueLetter++;
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
                        if (overdueLetter>0) {
                            String finalMessageOverdueLetter = messageOverdueLetter;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    notificationEvent.overdueLetter(finalMessageOverdueLetter);

                                }
                            });
                        }
//Уведомления о новых/просроченных задачах
                        int newTask = 0;
                        int idSummNewTask = 0;
                        String messageNameTask="";

                        String messageOverdueTask="";
                        int overdueTask = 0;

                        for (int i = 0;i<observableListMyTaskEntity.size();i++){
                            //новые
                            if (observableListMyTaskEntity.get(i).getStatusTaskId().equals(StatusTask.NOT_DONE)){
                                newTask++;
                                messageNameTask=messageNameTask+" "+observableListMyTaskEntity.get(i).getTaskName();
                                idSummNewTask=idSummNewTask+observableListMyTaskEntity.get(i).getTaskId();

                            }
                            //просроченные
                            if (((observableListMyTaskEntity.get(i).getTaskTerm().getTime()+observableListMyTaskEntity.get(i).getTaskTime().getTime()+25200000)<dateNow)){
                                messageOverdueTask=messageOverdueTask+" "+observableListMyTaskEntity.get(i).getTaskName();
                                taskService.overdueTask(observableListMyTaskEntity.get(i).getTaskId());
                                overdueTask++;
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
                        if (overdueTask>0) {
                            String finalMessageOverdueTask = messageOverdueTask;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    notificationEvent.overdueTask(finalMessageOverdueTask);

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

//Создаем повторяющиеся события
                        for(int i = 0; i<observableListAllEvent.size();i++){

                            switch (observableListAllEvent.get(i).getEventPeriodicity()){
                                case EventPeriodicity.SINGLE_TIME:{
                                    break;
                                }
                                case EventPeriodicity.DAILY:{
                                    Event dailyEvent = observableListAllEvent.get(i);
                                    dailyEvent.setEventDate(today);

                                    try {
                                        eventService.updateEvent(dailyEvent);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                case EventPeriodicity.WEEKLY:{
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(observableListAllEvent.get(i).getEventDate());

                                    int dayOfWeekEvent = cal.get(Calendar.DAY_OF_WEEK);

                                    if (dayOfWeekEvent==dayOfWeek) {
                                        Event weeklyEvent = observableListAllEvent.get(i);
                                        weeklyEvent.setEventDate(today);

                                        try {
                                             eventService.updateEvent(weeklyEvent);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                }
                                case EventPeriodicity.MONTHLY:{
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(observableListAllEvent.get(i).getEventDate());

                                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                                    if (dayOfMonth==day) {
                                        Event monthlyEvent = observableListAllEvent.get(i);
                                        monthlyEvent.setEventDate(today);

                                        try {
                                            eventService.updateEvent(monthlyEvent);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                }
                                default:{
                                    break;
                                }
                            }
                        }

//Уведомления о событиях
                        int newEvent = 0;

                        String messageEvent="";
                        java.sql.Date today = new java.sql.Date(dateNow);

                        try {
                            for (int i=0;i<observableListTodayEvent.size();i++){

                                if (((observableListTodayEvent.get(i).getEventTime().getTime()+observableListTodayEvent.get(i).getEventDate().getTime()+25200000)<dateNow)&&(String.valueOf(observableListTodayEvent.get(i).getEventDate()).equals(String.valueOf(today)))&&(observableListTodayEvent.get(i).getEventStatus().equals("0"))){
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
                        switch (statusTab) {
                            case "myTask":
                                tableTaskRefresh(observableListMyTaskEntity);
                                break;

                            case "myLetter":
                                tableTaskRefresh(observableListMyLetterTaskEntity);
                                break;

                            case "myDoneTask":
                                tableTaskRefresh(observableListMyDoneTaskEntity);
                                break;

                            case "fromEmpTask":
                                tableTaskRefresh(observableListFromEmpTaskEntity);
                                break;

                            case "archiveTask":
                                tableTaskRefresh(observableListArchiveTaskEntity);
                                break;

                            case "calendarTab":
                                tableEventRefresh(observableListSelectDayEvent);
                                break;

                            case "letterTab": {
                                tableLetterRefresh(observableListLetter);
                            }
                                break;

                            case "templateTab":
                                tableDocumentTemplateRefresh(observableListDocument);
                                break;

                            default:{
                                break;
                            }
                        }
                        colorRow();
                        colorRowEvent();
                        return null;
                    }
                };

            }

        };
                service.start();
    }

    public void tableTaskRefresh(ObservableList<TaskEntity> list){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableTask.setItems(list);
            }
        });

    }
    public void tableLetterRefresh(ObservableList<Letter> list){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableLetter.setItems(list);
            }
        });

    }
    public void tableEventRefresh(ObservableList<Event> list){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableEvent.setItems(list);
            }
        });
    }
    public void tableDocumentTemplateRefresh(ObservableList<Document> list){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableDocumentTemplate.setItems(list);
            }
        });
    }
    public void serviceStart(){//Сервис который запускает обновление каждые 60 секунд
        ScheduledService<Void> service = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

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
        service.setPeriod(Duration.seconds(60));
        service.start();
    }

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    public void clickHamburger(MouseEvent mouseEvent) { //Открывает меню (drawer)

                transition.setRate(transition.getRate()*-1);
                transition.play();

                if(drawer.isShown())
                {
                    drawer.close();
                    drawer.toBack();
                    hamburger.toFront();

                }else {
                    drawer.toFront();
                    drawer.open();
                    hamburger.toFront();
                }
    }
}
