package controller;

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


import java.io.IOException;
import java.sql.SQLException;


public class MainController {

    private double xOffset;
    private double yOffset;
    private Employee userAuth;

    public void setUserAuth(Employee userAuth) {
        this.userAuth = userAuth;
    }
    @FXML
    private JFXButton settingTabButton = new JFXButton();
    @FXML
    private JFXButton letterTabButton = new JFXButton();

    public Employee getUserAuth() {
        return userAuth;
    }
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
    @FXML
    private AnchorPane anchorSetting = new AnchorPane();

    //Department settings
    @FXML
    private TableView<Department> tableDepartment;
    private TableColumn<Department, String> idDep;
    private TableColumn<Department, String> nameDep;

    @FXML
    private JFXTextField txtDepartment;

// Variables
    private ObservableList<Department> dataDepartment;

    DepartmentDao departmentDao;

    private int idDepartment;


//Employee settings

    @FXML
    private ComboBox<String> comboBoxEmployee_Access = new ComboBox<>();
    @FXML
    private ComboBox<String> comboBoxEmployee_Department = new ComboBox<>();
    @FXML
    private TextField txtFIOEmployee;
    @FXML
    private TextField txtLoginEmployee;
    @FXML
    private TextField txtPasswordEmployee;
    @FXML
    private TableView<Employee> tableEmployee;
    private TableColumn<Employee, String> idEmpl;
    private TableColumn<Employee, String> fioEmpl;
    private TableColumn<Employee, String> loginEmpl;
    private TableColumn<Employee, String> passwordEmpl;
    private TableColumn<Employee, String> departmentEmpl;
    private TableColumn<Employee, String> accessEmpl;

    //Variables

    EmployeeDao employeeDao;

    AccessDao accessDao;

    private ObservableList<Employee> dataEmployee;

    private int idEmployee;
//Documents settings
    private int idDocument;
    private ObservableList<Document> dataDocument;

    DocumentDao documentDao;

    final FileChooser fileChooser=new FileChooser();

    @FXML
    private TableView<Document> tableDocument;
    private TableColumn<Document, String> idDoc;
    private TableColumn<Document, String> nameDoc;
    private TableColumn<Document, String> departmentDoc;

    @FXML
    private Button documentButtonId;

    @FXML
    private ComboBox<String> comboBoxDocument_Department = new ComboBox<>();
//Documents view Tab
    @FXML
    private TableView<Document> tableDocumentTemplate;

    private TableColumn<Document, String> documentNameTemplate;

    @FXML
    private ComboBox<String> comboBoxDocument_Template = new ComboBox<>();

    //Connection
    private DBconnection dBconnection;
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
    TaskDao taskDao;
    private Task task= new Task();

    public void initialize() {
        accessDao = new AccessDaoImpl();
        employeeDao = new EmployeeDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        documentDao = new DocumentDaoImpl();
        taskDao = new TaskDaoImpl();
// If access - administrator
if (AuthorizedUser.getUser().getAccessId()==1)
{
    /*initialize Departments settings table*/
    settingTabButton.setVisible(true);
    letterTabButton.setVisible(true);


    idDep = new TableColumn<Department, String>("id");

    idDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentId"));
    nameDep = new TableColumn<Department, String>("Название отдела");
    nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

    tableDepartment.getColumns().setAll(idDep, nameDep);
    tableDepartment.setItems((ObservableList<Department>) departmentDao.listDepartments());
    /*initialize Employee settings table*/


    idEmpl = new TableColumn<Employee, String>("id");
    idEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeId"));
    fioEmpl = new TableColumn<Employee, String>("ФИО");
    fioEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeName"));
    loginEmpl = new TableColumn<Employee, String>("Логин");
    loginEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeLogin"));
    passwordEmpl = new TableColumn<Employee, String>("Пароль");
    passwordEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeePassword"));
    departmentEmpl = new TableColumn<Employee, String>("Отдел");
    departmentEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("departmentName"));
    accessEmpl = new TableColumn<Employee, String>("Уровень доступа");
    accessEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("accessName"));

    tableEmployee.getColumns().setAll(idEmpl, fioEmpl, loginEmpl, passwordEmpl, departmentEmpl, accessEmpl);
    tableEmployee.setItems(employeeDao.listEmployees());
    /*initialize combobox Employee settings tab*/
    accessDao.listAccess();
    comboBoxEmployee_Access.setItems(accessDao.listAccessName());

    comboBoxEmployee_Department.setItems(departmentDao.listDepartmentName());
    /*initialize Document settings table*/

    idDoc = new TableColumn<Document, String>("id");
    idDoc.setCellValueFactory(new PropertyValueFactory<Document, String>("documentId"));
    nameDoc = new TableColumn<Document, String>("Название документа");
    nameDoc.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
    departmentDoc = new TableColumn<Document, String>("Отдел");
    departmentDoc.setCellValueFactory(new PropertyValueFactory<Document, String>("departmentName"));

    tableDocument.getColumns().setAll(idDoc, nameDoc, departmentDoc);
    tableDocument.setItems(documentDao.listDocuments());
    /*initialize combobox Document settings tab*/

    comboBoxDocument_Department.setItems(departmentDao.listDepartmentName());
}



    /*initialize table Document Template tab*/
    documentNameTemplate = new TableColumn<Document, String>("Название документа");
    documentNameTemplate.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
    tableDocumentTemplate.getColumns().setAll(documentNameTemplate);
    tableDocumentTemplate.setItems(documentDao.listDocuments());
    /*initialize combobox Document template tab*/
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

    }

    /*Departments tab CRUD*/
    public void clearDepartmentText() {
        txtDepartment.setText("");

    }

    public void refreshTableDepartment() {
        dataDepartment = (ObservableList<Department>) departmentDao.listDepartments();
        tableDepartment.setItems(dataDepartment);
        comboBoxEmployee_Department.setItems(departmentDao.listDepartmentName());
        comboBoxDocument_Department.setItems(departmentDao.listDepartmentName());
        comboBoxDocument_Template.setItems(departmentDao.listDepartmentName());

    }

    public void addDepartmentButton(ActionEvent actionEvent) throws SQLException {
        if (txtDepartment.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите название отдела!");
        } else {
            Department department = new Department();
            department.setDepartmentName(txtDepartment.getText());
            departmentDao.addDepartment(department);
            clearDepartmentText();
            refreshTableDepartment();

        }

    }

    public void removeDepartmentButton(ActionEvent actionEvent) {
        departmentDao.removeDepartment(this.idDepartment);
        clearDepartmentText();
        refreshTableDepartment();

    }

    public void clickTableDepartment(MouseEvent mouseEvent) {
        Department department = tableDepartment.getSelectionModel().getSelectedItems().get(0);
        if (department!=null){
            txtDepartment.setText(department.getDepartmentName());
            this.idDepartment = department.getDepartmentId();
        }
    }

    public void updateDepartmentButton(ActionEvent actionEvent) {
        if (txtDepartment.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите название отдела!");
        } else {
            Department department = new Department();
            department.setDepartmentName(txtDepartment.getText());
            department.setDepartmentId(this.idDepartment);
            departmentDao.updateDepartment(department);
            clearDepartmentText();
            refreshTableDepartment();

        }
    }


    /*Employees tab CRUD*/
    public void clearEmployeeTab() {
        txtFIOEmployee.setText("");
        txtLoginEmployee.setText("");
        txtPasswordEmployee.setText("");
        comboBoxEmployee_Access.setValue(null);
        comboBoxEmployee_Department.setValue(null);

    }

    public void refreshTableEmployee() {
        dataEmployee = employeeDao.listEmployees();
        tableEmployee.setItems(dataEmployee);
    }

    public void clickTableEmployee(MouseEvent mouseEvent) {
        Employee employee = tableEmployee.getSelectionModel().getSelectedItems().get(0);
        if (employee!=null) {
            this.idEmployee = employee.getEmployeeId();
            txtFIOEmployee.setText(employee.getEmployeeName());
            txtLoginEmployee.setText(employee.getEmployeeLogin());
            txtPasswordEmployee.setText(employee.getEmployeePassword());
            comboBoxEmployee_Access.setValue(employee.getAccessName());
            comboBoxEmployee_Department.setValue(employee.getDepartmentName());
        }
    }

    public void updateEmployeeButton(ActionEvent actionEvent) {
        if (txtFIOEmployee.getText().isEmpty() || txtLoginEmployee.getText().isEmpty() || txtPasswordEmployee.getText().isEmpty() || comboBoxEmployee_Department.getValue()==null || comboBoxEmployee_Access.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            Employee employee = new Employee();
            employee.setEmployeeName(txtFIOEmployee.getText());
            employee.setEmployeeLogin(txtLoginEmployee.getText());
            employee.setEmployeePassword(txtPasswordEmployee.getText());
            employee.setDepartmentId(departmentDao.getIdDepartmentByName(comboBoxEmployee_Department.getValue()));
            employee.setAccessId(accessDao.getIdAccessByName(comboBoxEmployee_Access.getValue()));
            employee.setEmployeeId(this.idEmployee);
            employeeDao.updateEmployee(employee);

            refreshTableEmployee();
        }
    }

    public void removeEmployeeButton(ActionEvent actionEvent) {
        employeeDao.removeEmployee(this.idEmployee);

        clearEmployeeTab();
        refreshTableEmployee();
    }

    public void addEmployeeButton(ActionEvent actionEvent) {
        if (txtFIOEmployee.getText().isEmpty() || txtLoginEmployee.getText().isEmpty() || txtPasswordEmployee.getText().isEmpty() || comboBoxEmployee_Department.getValue()==null || comboBoxEmployee_Access.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {

            Employee employee = new Employee();
            employee.setEmployeeName(txtFIOEmployee.getText());
            employee.setEmployeeLogin(txtLoginEmployee.getText());
            employee.setEmployeePassword(txtPasswordEmployee.getText());
            employee.setDepartmentId(departmentDao.getIdDepartmentByName(comboBoxEmployee_Department.getValue()));
            employee.setAccessId(accessDao.getIdAccessByName(comboBoxEmployee_Access.getValue()));
            employeeDao.addEmployee(employee);
            clearEmployeeTab();
            refreshTableEmployee();
        }
    }


    /*Documents tab CRUD*/
    public void addDocumentButton(ActionEvent actionEvent) throws IOException {
        if (comboBoxDocument_Department.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            Document document = new Document();

            document.setDocumentFile(fileChooser.showOpenDialog(documentButtonId.getScene().getWindow()));
            if (document.getDocumentFile() == null) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не загружен!");
            } else {
                document.setDocumentName(document.getDocumentFile().getName());

                document.setDepartmentId(departmentDao.getIdDepartmentByName(comboBoxDocument_Department.getValue()));
                documentDao.addDocument(document);
                clearDocumentTab();
                refreshTableDocument();
            }
        }

    }

    private void refreshTableDocument() {
        dataDocument = documentDao.listDocuments();
        tableDocument.setItems(dataDocument);
        tableDocumentTemplate.setItems(dataDocument);
    }

    private void clearDocumentTab() {

        comboBoxDocument_Department.setValue(null);
    }

    public void removeDocumentButton(ActionEvent actionEvent) {
        documentDao.removeDocument(this.idDocument);

        clearDocumentTab();
        refreshTableDocument();
    }

    public void clickTableDocument(MouseEvent mouseEvent) {
        Document document = tableDocument.getSelectionModel().getSelectedItems().get(0);
        if (document!=null) {
            this.idDocument = document.getDocumentId();
            comboBoxDocument_Department.setValue(document.getDepartmentName());

        }
    }

/*Document template tab*/
public void clickTableDocumentTemplate(MouseEvent mouseEvent) {
    Document document = tableDocumentTemplate.getSelectionModel().getSelectedItems().get(0);
    if (document!=null) {
        idDocument = document.getDocumentId();

    }
}
    public void choiceDepartmentDocument(ActionEvent actionEvent) {
        tableDocumentTemplate.setItems(documentDao.listDocumentsByDepartment(comboBoxDocument_Template.getValue()));

    }

    public void openDocumentButton(ActionEvent actionEvent) {
        documentDao.openDocument(this.idDocument);
    }

    public void printDocumentButton(ActionEvent actionEvent) {
        documentDao.printDocument(this.idDocument);
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
        anchorTemplate.toFront();
    }

    public void calendarTabButton(ActionEvent actionEvent) {
        anchorCalendar.toFront();
    }

    public void letterTabButton(ActionEvent actionEvent) {
        anchorLetter.toFront();
    }

    public void settingTabButton(ActionEvent actionEvent) {
        anchorSetting.toFront();
    }

    public void closeMainIcon(MouseEvent mouseEvent) {
        Stage stage = (Stage) txtFIOEmployee.getScene().getWindow();
        stage.close();
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
