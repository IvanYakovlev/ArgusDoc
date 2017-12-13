package service;

import dao.DepartmentDao;
import model.Department;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentService{
    private DepartmentDao departmentDao;

    @Override
    @Transactional
    public void addDepartment(Department department) {
        this.departmentDao.addDepartment(department);
    }

    @Override
    @Transactional
    public void removeDepartment(int id) {
        this.departmentDao.removeDepartment(id);
    }

    @Override
    @Transactional
    public List<Department> listDepartment() {
        return this.departmentDao.listDepartments();
    }
}
