package dao;

import javafx.collections.ObservableList;
import model.Employee;

import java.util.List;

public interface EmployeeDao {
    public void addEmployee(Employee employee);
    public void updateEmployee(Employee employee);
    public void removeEmployee(int id);
    public ObservableList<Employee> listEmployees();
}
