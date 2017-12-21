package dao;


import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public void updateDepartment(Department department) {
        dBconnection =new DBconnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = dBconnection.connect().prepareStatement("UPDATE Departments SET Department_name=? WHERE Department_id=?");
            preparedStatement.setInt(2,department.getDepartmentId());
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeDepartment(int id) {

    }

    public ObservableList<Department> listDepartments() {
        dBconnection = new DBconnection();
        ObservableList<Department> listData = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM Departments";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                Department department = new Department();
                department.setDepartmentId(resultSet.getInt("Department_id"));
                department.setDepartmentName(resultSet.getString("Department_name"));

                listData.add(department);
            }
        } catch (Exception ex) {
            System.out.println("error list");
        }
        return listData;

    }
}
