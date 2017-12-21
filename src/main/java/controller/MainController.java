package controller;

import dao.DepartmentDaoImpl;
import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Department;

import javax.crypto.spec.DESedeKeySpec;
import java.util.List;


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
    @FXML
    DepartmentDaoImpl departmentDao;


    public void initialize() {
        departmentDao=new DepartmentDaoImpl();
        idDep= new TableColumn<Department, String>("ID");
        idDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentId"));
        nameDep = new TableColumn<Department, String>("Name");
        nameDep.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));

        tableDepartment.getColumns().setAll(idDep,nameDep);
        tableDepartment.setItems(departmentDao.listDepartments());


    }


    public void addDepartment(ActionEvent actionEvent) {
    /*    Department department = new Department();
        department.setDepartmentName(txtDepartment.getText());
      */


    }
    public void removeDepartment (ActionEvent actionEvent){
    }

    public void clickTableDepartment(MouseEvent mouseEvent) {
    }
}
