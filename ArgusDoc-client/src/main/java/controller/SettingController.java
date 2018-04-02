package controller;

import argusDocSettings.ServerFilePath;
import com.jfoenix.controls.JFXTextField;
import entity.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import service.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dialog.ADInfo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;

public class SettingController {

    private double xOffset;
    private double yOffset;

    private TaskEntity taskEntity = new TaskEntity();
    private int idEmployee;
    private Document document = new Document();
    private int idDepartment;

    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;

    private ObservableList<Department> dataDepartment;
    private ObservableList<Employee> dataEmployee;
    private ObservableList<Document> dataDocument;

    final FileChooser fileChooser=new FileChooser();


//Department settings
@FXML
private FontAwesomeIconView closeSettingWindow;
    @FXML
    private TableView<Department> tableDepartment;

    private TableColumn<Department, String> nameDep;
    @FXML
    private JFXTextField txtDepartment;

//Employee settings

    @FXML
    private ComboBox<String> comboBoxEmployee_Access = new ComboBox<>();
    @FXML
    private ComboBox<String> comboBoxEmployee_Department = new ComboBox<>();
    @FXML
    private TextField txtFIOEmployee;

    @FXML
    private TextField txtPasswordEmployee;
    @FXML
    private TableView<Employee> tableEmployee;
    private TableColumn<Employee, String> fioEmpl;
    private TableColumn<Employee, String> passwordEmpl;
    private TableColumn<Employee, String> departmentEmpl;
    private TableColumn<Employee, String> accessEmpl;

//Documents settings

    @FXML
    private TableView<Document> tableDocument;
    private TableColumn<Document, String> nameDoc;
    private TableColumn<Document, String> departmentDoc;

    @FXML
    private Button documentButtonId;
    @FXML
    private ComboBox<String> comboBoxDocument_Department = new ComboBox<>();



    public void initialize() throws RemoteException {

//initialize Departments settings table

        nameDep = new TableColumn<Department, String>("Название отдела");
        nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

        nameDep.prefWidthProperty().bind(tableDepartment.widthProperty().multiply(1));

        tableDepartment.getColumns().setAll(nameDep);
        ObservableList<Department> observableListDepartments = FXCollections.observableArrayList(departmentService.listDepartments());
        tableDepartment.setItems(observableListDepartments);

//initialize Employee settings table

        fioEmpl = new TableColumn<Employee, String>("ФИО");
        fioEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeName"));
        passwordEmpl = new TableColumn<Employee, String>("Пароль");
        passwordEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeePassword"));
        departmentEmpl = new TableColumn<Employee, String>("Отдел");
        departmentEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("departmentName"));
        accessEmpl = new TableColumn<Employee, String>("Уровень доступа");
        accessEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("accessName"));

        fioEmpl.prefWidthProperty().bind(tableEmployee.widthProperty().multiply(0.35));
        passwordEmpl.prefWidthProperty().bind(tableEmployee.widthProperty().multiply(0.15));
        departmentEmpl.prefWidthProperty().bind(tableEmployee.widthProperty().multiply(0.30));
        accessEmpl.prefWidthProperty().bind(tableEmployee.widthProperty().multiply(0.20));

        tableEmployee.getColumns().setAll( fioEmpl, passwordEmpl, departmentEmpl, accessEmpl);
        ObservableList<Employee> observableListEmployees = FXCollections.observableArrayList(employeeService.listEmployees());
        tableEmployee.setItems(observableListEmployees);

//initialize combobox Employee settings tab

        accessService.listAccess();
        ObservableList<String> observableListAccessName = FXCollections.observableArrayList(accessService.listAccessName());
        comboBoxEmployee_Access.setItems(observableListAccessName);
        ObservableList<String> observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
        comboBoxEmployee_Department.setItems(observableListDepartmentName);
//initialize Document settings table

        nameDoc = new TableColumn<Document, String>("Название документа");
        nameDoc.setCellValueFactory(new PropertyValueFactory<Document, String>("documentName"));
        departmentDoc = new TableColumn<Document, String>("Отдел");
        departmentDoc.setCellValueFactory(new PropertyValueFactory<Document, String>("departmentName"));

        tableDocument.getColumns().setAll( nameDoc, departmentDoc);

        nameDoc.prefWidthProperty().bind(tableEmployee.widthProperty().multiply(0.70));
        departmentDoc.prefWidthProperty().bind(tableEmployee.widthProperty().multiply(0.30));

        ObservableList<Document> observableListDocument = FXCollections.observableArrayList(documentService.listDocuments());
        tableDocument.setItems(observableListDocument);

//initialize combobox Document settings tab

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        ObservableList<String> observableListDepartmentName2 = FXCollections.observableArrayList(departmentService.listDepartmentName());
        comboBoxDocument_Department.setItems(observableListDepartmentName2);
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

//Departments tab CRUD

    public void clearDepartmentText() {
        txtDepartment.setText("");

    }

    public void refreshTableDepartment() throws RemoteException {
        ObservableList<Department> observableListDepartments = FXCollections.observableArrayList(departmentService.listDepartments());
        dataDepartment = observableListDepartments;
        tableDepartment.setItems(dataDepartment);

        ObservableList<String> observableListDepartmentName = FXCollections.observableArrayList(departmentService.listDepartmentName());
        comboBoxEmployee_Department.setItems(observableListDepartmentName);
        comboBoxDocument_Department.setItems(observableListDepartmentName);

    }

    public void addDepartmentButton(ActionEvent actionEvent) throws RemoteException {
        if (txtDepartment.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите название отдела!");
        } else {
            Department department = new Department();
            department.setDepartmentName(txtDepartment.getText());

            try {
                departmentService.addDepartment(department);
            }catch (SQLException e){
                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Отдел с таким названием уже существует!");
            }

            clearDepartmentText();
            refreshTableDepartment();

        }

    }

    public void removeDepartmentButton(ActionEvent actionEvent) throws RemoteException {

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/1.jpg"));
            alert.setTitle("Удаление");
            alert.setHeaderText("Вы действительно хотите удалить отдел?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                departmentService.removeDepartment(this.idDepartment);

            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }

        }catch (SQLException e){

          ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Удаление невозможно, так как есть пользователи или документы в выбранном отделе!");
        }

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

    public void updateDepartmentButton(ActionEvent actionEvent) throws RemoteException {
        if (txtDepartment.getText().isEmpty()) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Введите название отдела!");
        } else {
            Department department = new Department();
            department.setDepartmentName(txtDepartment.getText());
            department.setDepartmentId(this.idDepartment);

            try {
                departmentService.updateDepartment(department);
            }catch (SQLException e){
                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Отдел с таким названием уже существует!");
            }

            clearDepartmentText();
            refreshTableDepartment();
        }
    }

//Employees tab CRUD

    public void clearEmployeeTab() {
        txtFIOEmployee.setText("");
        txtPasswordEmployee.setText("");
        comboBoxEmployee_Access.setValue(null);
        comboBoxEmployee_Department.setValue(null);

    }

    public void refreshTableEmployee() throws RemoteException {
        ObservableList<Employee> observableListEmployee = FXCollections.observableArrayList(employeeService.listEmployees());
        dataEmployee = observableListEmployee;
        tableEmployee.setItems(dataEmployee);
    }

    public void clickTableEmployee(MouseEvent mouseEvent) {
        Employee employee = tableEmployee.getSelectionModel().getSelectedItems().get(0);
        if (employee!=null) {
            this.idEmployee = employee.getEmployeeId();
            txtFIOEmployee.setText(employee.getEmployeeName());
            txtPasswordEmployee.setText(employee.getEmployeePassword());
            comboBoxEmployee_Access.setValue(employee.getAccessName());
            comboBoxEmployee_Department.setValue(employee.getDepartmentName());
        }
    }

    public void updateEmployeeButton(ActionEvent actionEvent) throws RemoteException {
        if (txtFIOEmployee.getText().isEmpty() ||  txtPasswordEmployee.getText().isEmpty() || comboBoxEmployee_Department.getValue()==null || comboBoxEmployee_Access.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            Employee employee = new Employee();
            employee.setEmployeeName(txtFIOEmployee.getText());
            employee.setEmployeePassword(txtPasswordEmployee.getText());
            employee.setDepartmentId(departmentService.getIdDepartmentByName(comboBoxEmployee_Department.getValue()));
            employee.setAccessId(accessService.getIdAccessByName(comboBoxEmployee_Access.getValue()));
            employee.setEmployeeId(this.idEmployee);
            try {
                employeeService.updateEmployee(employee);
            } catch (SQLException e) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Данный пользователь уже существует!");
            }

            refreshTableEmployee();
        }
    }

    public void removeEmployeeButton(ActionEvent actionEvent) throws RemoteException {
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/1.jpg"));
            alert.setTitle("Удаление");
            alert.setHeaderText("Вы действительно хотите удалить сотрудника?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                employeeService.removeEmployee(this.idEmployee);

            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        clearEmployeeTab();
        refreshTableEmployee();
    }

    public void addEmployeeButton(ActionEvent actionEvent) throws RemoteException {
        if (txtFIOEmployee.getText().isEmpty() ||  txtPasswordEmployee.getText().isEmpty() || comboBoxEmployee_Department.getValue()==null || comboBoxEmployee_Access.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {

            Employee employee = new Employee();
            employee.setEmployeeName(txtFIOEmployee.getText());
            employee.setEmployeePassword(txtPasswordEmployee.getText());
            employee.setDepartmentId(departmentService.getIdDepartmentByName(comboBoxEmployee_Department.getValue()));
            employee.setAccessId(accessService.getIdAccessByName(comboBoxEmployee_Access.getValue()));
            try {
                employeeService.addEmployee(employee);
            } catch (SQLException e) {

                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Данный пользователь уже существует!");
            }
            clearEmployeeTab();
            refreshTableEmployee();
        }
    }

//Documents tab CRUD

    public void addDocumentButton(ActionEvent actionEvent) throws IOException {
        if (comboBoxDocument_Department.getValue()==null) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Не все поля заполнены!");
        } else {
            File choseFile = fileChooser.showOpenDialog(documentButtonId.getScene().getWindow());
            if (choseFile == null) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не загружен!");
            } else {
                Document document = new Document();
                document.setDocumentName(choseFile.getName());
                document.setDocumentFile(choseFile);
                document.setDocumentFilePath(ServerFilePath.DOCUMENTS_FILE_PATH+choseFile.getName());
                document.setDepartmentId(departmentService.getIdDepartmentByName(comboBoxDocument_Department.getValue()));
                try {
                    documentService.addDocument(document);

                } catch (SQLException e) {
                    ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Документ с таким названием уже существует!");
                }
                try {
                    //Копируем файл на сервер

                    File destFile = new File(document.getDocumentFilePath());
                    System.out.println(document.getDocumentFile().toPath());
                    System.out.println(destFile.toPath());
                    Files.copy(document.getDocumentFile().toPath(), destFile.toPath());
                }catch (FileAlreadyExistsException e){
                    ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Документ с таким названием уже существует!");
                }


                clearDocumentTab();
                refreshTableDocument();
            }
        }

    }

    private void refreshTableDocument() throws RemoteException {
        ObservableList<Document> observableListDocument = FXCollections.observableArrayList(documentService.listDocuments());
        dataDocument = observableListDocument;
        tableDocument.setItems(dataDocument);

    }

    private void clearDocumentTab() {
        comboBoxDocument_Department.setValue(null);
    }

    public void removeDocumentButton(ActionEvent actionEvent) throws RemoteException {
        if (document.getDocumentFilePath()!=null) {

            try {

                ////////////
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/images/1.jpg"));
                alert.setTitle("Удаление");
                alert.setHeaderText("Вы действительно хотите удалить документ?");


                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    documentService.removeDocument(document.getDocumentId(), document.getDocumentFilePath());
//удаляем файл с сервера
                    if (document.getDocumentFilePath()!=null) {
                        Path path = Paths.get(document.getDocumentFilePath());
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.out.println("файл уже удален");
                        }
                    }


                } else if (option.get() == ButtonType.CANCEL) {

                } else {

                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            clearDocumentTab();
            refreshTableDocument();
        }
    }

    public void clickTableDocument(MouseEvent mouseEvent) {
        Document clickDocument = tableDocument.getSelectionModel().getSelectedItems().get(0);
        System.out.println(clickDocument);

        document = clickDocument;
        if (document!=null) {
            comboBoxDocument_Department.setValue(document.getDepartmentName());
        }
    }

    public void closeSettingWindow(MouseEvent mouseEvent) {
        Stage stage = (Stage) closeSettingWindow.getScene().getWindow();
        stage.close();
    }
}
