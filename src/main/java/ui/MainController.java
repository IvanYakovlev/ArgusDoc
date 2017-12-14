package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Department;

public class MainController {
    @FXML private TableView<Department> tableDepartment;
    @FXML private TextField txtDepartment;

    public void addDepartment(ActionEvent actionEvent) {
    }

    public void removeDepartment(ActionEvent actionEvent) {
    }
}
