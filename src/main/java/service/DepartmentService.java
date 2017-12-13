package service;

import model.Department;

import java.util.List;

public interface DepartmentService {

    public void addDepartment(Department department);

    public void removeDepartment(int id);

    public List<Department> listDepartment();

}
