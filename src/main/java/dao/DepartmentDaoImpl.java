package dao;


import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import model.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    DBconnection dBconnection;

    private void dialog(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType, s);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Информация");
        alert.showAndWait();
    }

    public void addDepartment(Department department) {
        this.dBconnection=new DBconnection();

        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Departments(Department_name) VALUES (?)");
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            dialog(Alert.AlertType.INFORMATION, "Данный отдел уже существует!");
        }
    }

    @Override
    public void updateDepartment(Department department) {
        this.dBconnection =new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE Departments SET Department_name=? WHERE Department_id=?");
            preparedStatement.setInt(2,department.getDepartmentId());
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            dialog(Alert.AlertType.INFORMATION, "Данный отдел уже существует!");
        }
    }




    public void removeDepartment(int id) {
        dBconnection = new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE FROM Departments WHERE Department_id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            dialog(Alert.AlertType.INFORMATION, "Удаление невозможно, так как есть пользователи в данном отделе!");
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

    @Override
    public ObservableList<Integer> listDepartmentId() {
        dBconnection = new DBconnection();
        ObservableList<Integer> listData = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Department_id FROM DEPARTMENTS";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                listData.addAll(resultSet.getInt("Department_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public Department getDepartmentById(int id)   {
        dBconnection = new DBconnection();
        Department department=null;
        try {
            String sql = "SELECT * FROM Departments WHERE Department_id="+id;
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);

                department = new Department();
                department.setDepartmentId(resultSet.getInt("Department_id"));
                department.setDepartmentName(resultSet.getString("Department_name"));

            } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }
}
