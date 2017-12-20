package controller;

import dbConnection.DBconnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Department;


public class MainController {

    @FXML
    private TableView<Department> tableDepartment;
    @FXML
    private TextField txtDepartment;
    // Variables
    private ObservableList<Department> data;
    private DBconnection dBconnection;
    @FXML
    public void initialize() {


        /*List<Department> departmentList = departmentService.listDepartment();
        data = FXCollections.observableArrayList(departmentList);

        // Столбцы таблицы
        TableColumn<Department,String> idColumn = new TableColumn<Department, String>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("id"));

        TableColumn<Department, String> nameColumn = new TableColumn<Department, String>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));



        tableDepartment.getColumns().setAll(idColumn, nameColumn);

        // Данные таблицы
        tableDepartment.setItems(data);  // Этап инициализации JavaFX*/
    }


    public void addDepartment(ActionEvent actionEvent) {


    }
    public void removeDepartment (ActionEvent actionEvent){
    }
}
