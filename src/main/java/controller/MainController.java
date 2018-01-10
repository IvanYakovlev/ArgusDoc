package controller;

import dao.*;
import dbConnection.DBconnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import model.Access;
import model.Department;
import model.Employee;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class MainController {
    //Department settings
    @FXML
    private TableView<Department> tableDepartment;
    @FXML
    private TextField txtDepartment;
    private TableColumn<Department, String> idDep;
    private TableColumn<Department, String> nameDep;


    // Variables
    private ObservableList<Department> dataDepartment;

    DepartmentDao departmentDao;

    private int idDepartment;

    public int getIdDepartment() {
        return idDepartment;
    }


//Employee settings

    @FXML
    private ComboBox<Integer> comboBoxAccess = new ComboBox<>();
    @FXML
    private ComboBox<Integer> comboBoxDepartment = new ComboBox<>();
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

    public int getIdEmployee() {
        return idEmployee;
    }


    //Connection
    private DBconnection dBconnection;

    public void initialize() {
/*initialize Departments table*/
        departmentDao = new DepartmentDaoImpl();
        idDep = new TableColumn<Department, String>("id");
        idDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentId"));
        nameDep = new TableColumn<Department, String>("Название отдела");
        nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

        tableDepartment.getColumns().setAll(idDep, nameDep);
        tableDepartment.setItems((ObservableList<Department>) departmentDao.listDepartments());
/*initialize Employee table*/
        employeeDao = new EmployeeDaoImpl();
        accessDao = new AccessDaoImpl();
        idEmpl = new TableColumn<Employee, String>("id");
        idEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeId"));
        fioEmpl = new TableColumn<Employee, String>("ФИО");
        fioEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeName"));
        loginEmpl = new TableColumn<Employee, String>("Логин");
        loginEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeLogin"));
        passwordEmpl = new TableColumn<Employee, String>("Пароль");
        passwordEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeePassword"));
        departmentEmpl = new TableColumn<Employee, String>("Отдел");
        departmentEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("departmentID"));
        accessEmpl = new TableColumn<Employee, String>("Уровень доступа");
        accessEmpl.setCellValueFactory(new PropertyValueFactory<Employee, String>("accessId"));

        tableEmployee.getColumns().setAll(idEmpl, fioEmpl, loginEmpl, passwordEmpl, departmentEmpl, accessEmpl);
        tableEmployee.setItems(employeeDao.listEmployees());
/*initialize combobox employee tab*/
        comboBoxAccess.setItems(accessDao.listAccessId());
        comboBoxDepartment.setItems(departmentDao.listDepartmentId());


    }

    private void dialog(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType, s);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Информация");
        alert.showAndWait();
    }

    /*Departments tab CRUD*/
    public void clearDepartmentText() {
        txtDepartment.setText("");

    }

    public void refreshTableDepartment() {
        dataDepartment = (ObservableList<Department>) departmentDao.listDepartments();
        tableDepartment.setItems(dataDepartment);
        comboBoxDepartment.setItems(departmentDao.listDepartmentId());

    }

    public void addDepartmentButton(ActionEvent actionEvent) throws SQLException {
        if (txtDepartment.getText().isEmpty()) {
            dialog(Alert.AlertType.INFORMATION, "Введите название отдела!");
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
            dialog(Alert.AlertType.INFORMATION, "Введите название отдела!");
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
        comboBoxAccess.setItems(accessDao.listAccessId());
        comboBoxDepartment.setItems(departmentDao.listDepartmentId());

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
          //  comboBoxAccess.setValue(employee.getAccessId());
           // comboBoxDepartment.setValue(employee.getDepartmentID());
        }
    }

    public void updateEmployeeButton(ActionEvent actionEvent) {
        if (txtFIOEmployee.getText().isEmpty() || txtLoginEmployee.getText().isEmpty() || txtPasswordEmployee.getText().isEmpty() || comboBoxDepartment.isArmed() || comboBoxAccess.isArmed()) {
            dialog(Alert.AlertType.INFORMATION, "Не все поля заполнены!");
        } else {
            Employee employee = new Employee();
            employee.setEmployeeName(txtFIOEmployee.getText());
            employee.setEmployeeLogin(txtLoginEmployee.getText());
            employee.setEmployeePassword(txtPasswordEmployee.getText());
          //  employee.setDepartmentID(comboBoxDepartment.getValue());
          //  employee.setAccessId(comboBoxAccess.getValue());
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
        if (txtFIOEmployee.getText().isEmpty() || txtLoginEmployee.getText().isEmpty() || txtPasswordEmployee.getText().isEmpty() || comboBoxDepartment.isArmed() || comboBoxAccess.isArmed()) {
            dialog(Alert.AlertType.INFORMATION, "Не все поля заполнены!");
        } else {
            Employee employee = new Employee();
            employee.setEmployeeName(txtFIOEmployee.getText());
            employee.setEmployeeLogin(txtLoginEmployee.getText());
            employee.setEmployeePassword(txtPasswordEmployee.getText());
           // employee.setDepartmentID(comboBoxDepartment.getValue());
          //  employee.setAccessId(comboBoxAccess.getValue());
            employeeDao.addEmployee(employee);
            clearEmployeeTab();
            refreshTableEmployee();
        }
    }


    /*Documents tab CRUD*/
    public void documentAddButton(ActionEvent actionEvent) throws IOException {
        File file = new File("C:\\123.odt");
        java.awt.Desktop.getDesktop().open(file);
    }
}
