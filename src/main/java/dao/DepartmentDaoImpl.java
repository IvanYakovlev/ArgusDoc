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
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Departments(Department_name) VALUES (?)");
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDepartment(int id,String name) {
        this.dBconnection =new DBconnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = dBconnection.connect().prepareStatement("UPDATE Departments SET Department_name=? WHERE Department_id=?");
            preparedStatement.setInt(2,id);
            preparedStatement.setString(1,name);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void removeDepartment(int id) {
        dBconnection = new DBconnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = dBconnection.connect().prepareStatement("DELETE FROM Departments WHERE Department_id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
