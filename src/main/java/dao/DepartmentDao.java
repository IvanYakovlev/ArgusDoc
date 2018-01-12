package dao;

import javafx.collections.ObservableList;
import model.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentDao {
    public void addDepartment(Department department);
    public void updateDepartment(Department department);
    public void removeDepartment(int id);
    public List<Department> listDepartments();
    public ObservableList<String> listDepartmentName();
    public int getIdDepartmentByName(String value);

}
