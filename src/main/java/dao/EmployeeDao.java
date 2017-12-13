package dao;

import model.Employee;

import java.util.List;

public interface EmployeeDao {
    public void addEmployee(Employee employee);
    public void removeEmployee(int id);
    public List<Employee> listEmployees();
}
