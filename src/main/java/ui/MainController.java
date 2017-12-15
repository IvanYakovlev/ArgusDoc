package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import service.DepartmentService;

import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class MainController {
    // Инъекции Spring
    @Autowired
    private DepartmentService departmentService;
    // Инъекции JavaFX

    @FXML
    private TableView<Department> tableDepartment;
    @FXML
    private TextField txtDepartment;
    // Variables
    private ObservableList<Department> data;

    /**
     * Инициализация контроллера от JavaFX.
     * Метод вызывается после того как FXML загрузчик произвел инъекции полей.
     * <p>
     * Обратите внимание, что имя метода <b>обязательно</b> должно быть "initialize",
     * в противном случае, метод не вызовется.
     * <p>
     * Также на этом этапе еще отсутствуют бины спринга
     * и для инициализации лучше использовать метод,
     * описанный аннотацией @PostConstruct,
     * который вызовется спрингом, после того, как им будут произведены все инъекции.
     * {@link MainController#init()}
     */
    @FXML
    public void initialize() {
        // Этап инициализации JavaFX
    }

    /**
     * На этом этапе уже произведены все возможные инъекции.
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        List<Department> departmentList = departmentService.listDepartment();
        data = FXCollections.observableArrayList(departmentList);

        // Столбцы таблицы
        TableColumn<Department,String> idColumn = new TableColumn<Department, String>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("id"));

        TableColumn<Department, String> nameColumn = new TableColumn<Department, String>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));



        tableDepartment.getColumns().setAll(idColumn, nameColumn);

        // Данные таблицы
        tableDepartment.setItems(data);
    }

    public void addDepartment(ActionEvent actionEvent) {
        String nameDepartment = txtDepartment.getText();
        if (StringUtils.isEmpty(nameDepartment)) {
            return;
        }
        Department department =new Department();
        department.setDepartmentName(nameDepartment);
        departmentService.addDepartment(department);
        data.add(department);

        txtDepartment.setText("");

    }
    public void removeDepartment (ActionEvent actionEvent){
    }
}
