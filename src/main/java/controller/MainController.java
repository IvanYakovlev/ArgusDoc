package controller;

import dao.*;
import dbConnection.DBconnection;
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
    private ComboBox<String> comboBoxAccess = new ComboBox<>();
    @FXML
    private ComboBox<String> comboBoxDepartment = new ComboBox<>();
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
        departmentDao=new DepartmentDaoImpl();
        idDep= new TableColumn<Department, String>("id");
        idDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentId"));
        nameDep = new TableColumn<Department, String>("Название отдела");
        nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

        tableDepartment.getColumns().setAll(idDep,nameDep);
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

    tableEmployee.getColumns().setAll(idEmpl,fioEmpl,loginEmpl,passwordEmpl,departmentEmpl,accessEmpl);
    tableEmployee.setItems(employeeDao.listEmployees());

    comboBoxAccess.setItems(accessDao.listAccessName());
    comboBoxDepartment.setItems(departmentDao.listDepartmentName());


    }
    private void dialog(Alert.AlertType alertType, String s){
        Alert alert = new Alert(alertType,s);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Информация");
        alert.showAndWait();
    }
/*Departments CRUD*/
    public void clearDepartmentText(){
        txtDepartment.setText("");

    }

    public void refreshTableDepartment(){
        dataDepartment = (ObservableList<Department>) departmentDao.listDepartments();
        tableDepartment.setItems(dataDepartment);
        comboBoxDepartment.setItems(departmentDao.listDepartmentName());

    }
    public void addDepartmentButton(ActionEvent actionEvent) throws SQLException {
        if (txtDepartment.getText().isEmpty()){
            dialog(Alert.AlertType.INFORMATION, "Введите название отдела!");}
            else {
                Department department = new Department();
                department.setDepartmentName(txtDepartment.getText());
                departmentDao.addDepartment(department);
                clearDepartmentText();
                refreshTableDepartment();

        }

    }
    public void removeDepartmentButton(ActionEvent actionEvent){
        departmentDao.removeDepartment(this.idDepartment);
        clearDepartmentText();
        refreshTableDepartment();

    }

    public void clickTableDepartment(MouseEvent mouseEvent) {
        Department department = tableDepartment.getSelectionModel().getSelectedItems().get(0);
        txtDepartment.setText(department.getDepartmentName());
        this.idDepartment=department.getDepartmentId();
    }

    public void updateDepartmentButton(ActionEvent actionEvent) {
        departmentDao.updateDepartment(this.idDepartment, txtDepartment.getText());
        clearDepartmentText();
        refreshTableDepartment();
    }



/*Employees CRUD*/
    public void clickTableEmployee(MouseEvent mouseEvent) {
    }

    public void updateEmployeeButton(ActionEvent actionEvent) {
    }

    public void removeEmployeeButton(ActionEvent actionEvent) {

    }

    public void addEmployeeButton(ActionEvent actionEvent) {

    }
}
