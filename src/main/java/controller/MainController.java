package controller;

import dao.DepartmentDao;
import dao.DepartmentDaoImpl;
import dbConnection.DBconnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import model.Department;

import java.sql.SQLException;


public class MainController {

    @FXML
    private TableView<Department> tableDepartment;
    @FXML
    private TextField txtDepartment;
    @FXML
    private TableColumn<Department, String> idDep;
    @FXML
    private TableColumn<Department, String> nameDep;
    // Variables
    private ObservableList<Department> data;

    private DBconnection dBconnection;

    private int idDepartment;

    public int getIdEmployee() {
        return idEmployee;
    }

    private int idEmployee;

    public int getIdDepartment() {
        return idDepartment;
    }

    @FXML

    DepartmentDao departmentDao = new DepartmentDaoImpl();


    public void initialize() {

        departmentDao=new DepartmentDaoImpl();
        idDep= new TableColumn<Department, String>("id");
        idDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentId"));
        nameDep = new TableColumn<Department, String>("Название отдела");
        nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

        tableDepartment.getColumns().setAll(idDep,nameDep);
        tableDepartment.setItems((ObservableList<Department>) departmentDao.listDepartments());



    }
    private void dialog(Alert.AlertType alertType, String s){
        Alert alert = new Alert(alertType,s);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Информация");
        alert.showAndWait();
    }

    public void clearDepartment(){
        txtDepartment.setText("");

    }

    public void refreshTableDepartment(){
        data = (ObservableList<Department>) departmentDao.listDepartments();
        tableDepartment.setItems(data);
    }
    public void addDepartment(ActionEvent actionEvent) throws SQLException {
        if (txtDepartment.getText().isEmpty()){
            dialog(Alert.AlertType.INFORMATION, "Введите название отдела!");}
            else {
                Department department = new Department();
                department.setDepartmentName(txtDepartment.getText());
                departmentDao.addDepartment(department);
                clearDepartment();
                refreshTableDepartment();

        }

    }
    public void removeDepartment (ActionEvent actionEvent){
        departmentDao.removeDepartment(this.idDepartment);
        clearDepartment();
        refreshTableDepartment();

    }

    public void clickTableDepartment(MouseEvent mouseEvent) {
        Department department = tableDepartment.getSelectionModel().getSelectedItems().get(0);
        txtDepartment.setText(department.getDepartmentName());
        this.idDepartment=department.getDepartmentId();
    }

    public void updateDepartment(ActionEvent actionEvent) {
        departmentDao.updateDepartment(this.idDepartment, txtDepartment.getText());
        clearDepartment();
        refreshTableDepartment();
    }
}
