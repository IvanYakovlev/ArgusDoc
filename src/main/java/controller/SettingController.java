package controller;

import argusDocSettings.serverFilePath;
import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.JFXTextField;
import dao.*;
import dbConnection.DBconnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dialog.ADInfo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Department;
import model.Document;
import model.Employee;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SettingController {

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


    //Department settings
@FXML
private FontAwesomeIconView closeSettingWindow;
    @FXML
    private TableView<Department> tableDepartment;
    private TableColumn<Department, String> idDep;
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

//Documents settings

    @FXML
    private TableView<Document> tableDocument;
    private TableColumn<Document, String> idDoc;
    private TableColumn<Document, String> nameDoc;
    private TableColumn<Document, String> departmentDoc;
    private TableColumn<Document, String> filePathDoc;
    @FXML
    private Button documentButtonId;
    @FXML
    private ComboBox<String> comboBoxDocument_Department = new ComboBox<>();



    public void initialize() {
        accessDao = new AccessDaoImpl();
        employeeDao = new EmployeeDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        documentDao = new DocumentDaoImpl();
        taskDao = new TaskDaoImpl();
        letterDao = new LetterDaoImpl();
    /*initialize Departments settings table*/


        idDep = new TableColumn<Department, String>("id");
        idDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentId"));
        nameDep = new TableColumn<Department, String>("Название отдела");
        nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

        tableDepartment.getColumns().setAll(idDep, nameDep);
        tableDepartment.setItems(departmentDao.listDepartments());

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
        filePathDoc = new TableColumn<Document, String>("Путь файла");
        filePathDoc.setCellValueFactory(new PropertyValueFactory<Document, String>("documentFilePath"));

        tableDocument.getColumns().setAll(idDoc, nameDoc, departmentDoc, filePathDoc);
        tableDocument.setItems(documentDao.listDocuments());

    /*initialize combobox Document settings tab*/

        comboBoxDocument_Department.setItems(departmentDao.listDepartmentName());
    }

         /*Departments tab CRUD*/
    public void clearDepartmentText() {
        txtDepartment.setText("");

    }

    public void refreshTableDepartment() {
        dataDepartment = departmentDao.listDepartments();
        tableDepartment.setItems(dataDepartment);
        comboBoxEmployee_Department.setItems(departmentDao.listDepartmentName());
        comboBoxDocument_Department.setItems(departmentDao.listDepartmentName());
      //  comboBoxDocument_Template.setItems(departmentDao.listDepartmentName());

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
            File choseFile = fileChooser.showOpenDialog(documentButtonId.getScene().getWindow());
            if (choseFile == null) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Документ не загружен!");
            } else {
                Document document = new Document();
                document.setDocumentName(choseFile.getName());
                document.setDocumentFile(choseFile);
                document.setDocumentFilePath(serverFilePath.DOCUMENTS_FILE_PATH+choseFile.getName());
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

    }

    private void clearDocumentTab() {

        comboBoxDocument_Department.setValue(null);
    }

    public void removeDocumentButton(ActionEvent actionEvent) {
        if (document.getDocumentFilePath()!=null) {
            documentDao.removeDocument(document.getDocumentId(), document.getDocumentFilePath());

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
