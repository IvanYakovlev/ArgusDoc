package dao;

import model.Department;

import java.util.List;

public interface DepartmentDao {
    public void addDepartment(Department department);
    public void updateDepartment(Department department);
    public void removeDepartment(int id);
    public List<Department> listDepartments();

}
