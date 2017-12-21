package dao;


import dbConnection.DBconnection;
import model.Department;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    DBconnection dBconnection;


    public void addDepartment(Department department) {
        this.dBconnection=new DBconnection();

        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Departments VALUE (?,?)");
            preparedStatement.setInt(1,department.getDepartmentId());
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDepartment(int id) {

    }

    public List<Department> listDepartments() {
        return null;
    }
}
